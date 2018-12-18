package com.cabipassenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.DialogInterface;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.Dialog_Common;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class ReceiptAct extends MainActivity {
    // Class members declarations.
    private Bundle mMessage = null;
    private RatingBar P_ratingbar;
    private TextView P_fare, skip_this, leftIcon, header_titleTxt;
    private LinearLayout leftIconTxt;
    private static String NotifyMessage = "";
    private ImageView Fb_share, Tw_share, Msg_share, Mail_share;
    private final Handler mRunOnUi = new Handler();
    private TaxiUtil mUtil;
    private String alert_msg;
    private Bundle alert_bundle = new Bundle();
    public String pickup_place;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.cabi:PendingAction";
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private PendingAction pendingAction = PendingAction.NONE;
    private TextView night_val, evefare_val, promotion_val, tax_val;
    private TextView baseFare, waitingFare, walletAmount, paidAmount, paymentType;
    private TextView minutes_traveled, minutes_fare;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {


        @Override
        public void onCancel() {


        }

        @Override
        public void onError(FacebookException error) {


            String title = NC.getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {


            if (result.getPostId() != null) {
                NC.getString(R.string.success);
                result.getPostId();
            }
        }

        private void showResult(String title, String alertMessage) {

            new AlertDialog.Builder(ReceiptAct.this).setTitle(title).setMessage(alertMessage).setPositiveButton(R.string.ok, null).show();
        }
    };
    private EditText comment;
    private TextView submit;
    private LinearLayout Nightfare;
    private LinearLayout Eveningfare;
    private LinearLayout Minute_lay;
    private Dialog mDialog;


    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    // Set the layout to activity.
    @Override
    public int setLayout() {

        setLocale();
        return R.layout.lay_receipt;
    }

    // Initialize the views on layout
    @Override
    public void Initialize() {
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), ReceiptAct.this);
        if(!SessionSave.getSession("facebook_key",ReceiptAct.this).equals(""))
            FacebookSdk.setApplicationId(SessionSave.getSession("facebook_key",ReceiptAct.this));
        else
            FacebookSdk.setApplicationId("1659604664078736");
        if (BsavedInstanceState != null) {
            String name = BsavedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        findViewById(R.id.headlayout).setVisibility(View.VISIBLE);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // It's possible that we were waiting for Profile to be
                // populated in order to
                // post a status update.
                handlePendingAction();
            }
        };
        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        // Can we present the share dialog for photos?
        try {
            canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TaxiUtil.sContext = this;
        // TaxiUtil.current_act = "ReceiptAct";
        FontHelper.applyFont(this, findViewById(R.id.lay_pay));
        P_fare = (TextView) findViewById(R.id.fare_amt);
        skip_this = (TextView) findViewById(R.id.skip_this);
        leftIcon = (TextView) findViewById(R.id.leftIcon);
        leftIconTxt = (LinearLayout) findViewById(R.id.leftIconTxt);
        Nightfare = (LinearLayout) findViewById(R.id.night_farelay);
        Eveningfare = (LinearLayout) findViewById(R.id.evening_farelay);
        Minute_lay = (LinearLayout) findViewById(R.id.minute_lay);


        header_titleTxt = (TextView) findViewById(R.id.header_titleTxt);
        P_ratingbar = (RatingBar) findViewById(R.id.ratingBar1);
        submit = (TextView) findViewById(R.id.submit);
        comment = (EditText) findViewById(R.id.comment);
        Fb_share = (ImageView) findViewById(R.id.share1);
        Tw_share = (ImageView) findViewById(R.id.share2);
        Msg_share = (ImageView) findViewById(R.id.share3);
        Mail_share = (ImageView) findViewById(R.id.share4);
        minutes_fare = (TextView) findViewById(R.id.minutes_fare);
        baseFare = (TextView) findViewById(R.id.BaseFare);
        waitingFare = (TextView) findViewById(R.id.WaitingFare);
        walletAmount = (TextView) findViewById(R.id.WalletAmt);
        paidAmount = (TextView) findViewById(R.id.PaidAmt);
        paymentType = (TextView) findViewById(R.id.Paymenttype);
        night_val = (TextView) findViewById(R.id.night_val);
        evefare_val = (TextView) findViewById(R.id.evefare_val);
        promotion_val = (TextView) findViewById(R.id.promotion_val);
        tax_val = (TextView) findViewById(R.id.tax_val);
        header_titleTxt.setText("" + NC.getResources().getString(R.string.e_receipt));
        header_titleTxt.setPadding(10, 10, 10, 10);

        leftIcon.setVisibility(View.VISIBLE);
        leftIconTxt.setVisibility(View.VISIBLE);
        leftIcon.setBackgroundResource(R.drawable.back);
        leftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            //    onBackPressed();
                SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
                SessionSave.saveSession("trip_id", "", ReceiptAct.this);

               // showLoading(ReceiptAct.this);
                Intent i;
                i = new Intent(ReceiptAct.this, MainHomeFragmentActivity.class);
                startActivity(i);
                finish();
            }
        });

        leftIconTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              //  onBackPressed();
                SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
                SessionSave.saveSession("trip_id", "", ReceiptAct.this);
               // showLoading(ReceiptAct.this);
                Intent i;
                i = new Intent(ReceiptAct.this, MainHomeFragmentActivity.class);
                startActivity(i);
                finish();
            }
        });


        mUtil = new TaxiUtil(this);
        try {
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("alert_message");
            }
            if (alert_msg != null && alert_msg.length() != 0)
                alert_view(ReceiptAct.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        final Intent i = getIntent();
        mMessage = i.getExtras();
        if (mMessage != null) {
            NotifyMessage = mMessage.getString("Message");
            try {
                final JSONObject obj = new JSONObject(NotifyMessage);
                pickup_place = obj.getString("pickup");



                P_fare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("fare"))));

                baseFare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("base_fare"))));
                waitingFare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("waiting_fare"))));
                walletAmount.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("used_wallet_amount"))));
                paidAmount.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("paid_amount"))));
                paymentType.setText(obj.getString("payment_type"));
                night_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("nightfare"))));
                if (Float.valueOf(obj.getString("nightfare")) <= 0.0)
                    Nightfare.setVisibility(View.GONE);
                if (Float.valueOf(obj.getString("eveningfare")) <= 0.0)
                    Eveningfare.setVisibility(View.GONE);
                evefare_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("eveningfare"))));
                promotion_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("promotion"))));
                tax_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("tax"))));
                paymentType.setText(obj.getString("payment_type"));
                // minutes_traveled.setText(String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("minutes_traveled")))+SessionSave.getSession("Metric",ReceiptAct.this));
                minutes_fare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("minutes_fare"))));
                if (Float.valueOf(obj.getString("minutes_fare")) <= 0.0)
                    Minute_lay.setVisibility(View.GONE);


            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else if (SessionSave.getSession("receipt_details", ReceiptAct.this).length() != 0) {
            try {
                final JSONObject obj = new JSONObject(SessionSave.getSession("receipt_details", ReceiptAct.this));
                pickup_place = obj.getString("pickup");
                P_fare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format("%.2f", Float.valueOf(obj.getString("fare"))));

                baseFare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("base_fare"))));
                waitingFare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("waiting_fare"))));
                walletAmount.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("used_wallet_amount"))));
                paidAmount.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("paid_amount"))));
                paymentType.setText(obj.getString("payment_type"));
                night_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("nightfare"))));
                evefare_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("eveningfare"))));
                promotion_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("promotion"))));
                tax_val.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("tax"))));
                paymentType.setText(obj.getString("payment_type"));
                //   minutes_traveled.setText(String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("minutes_traveled")))+SessionSave.getSession("Metric",ReceiptAct.this));
                minutes_fare.setText(SessionSave.getSession("Currency", ReceiptAct.this) + "" + String.format(Locale.UK, "%.2f", Float.valueOf(obj.getString("minutes_fare"))));

                if (Float.valueOf(obj.getString("nightfare")) <= 0.0)
                    Nightfare.setVisibility(View.GONE);
                if (Float.valueOf(obj.getString("eveningfare")) <= 0.0)
                    Eveningfare.setVisibility(View.GONE);
                if (Float.valueOf(obj.getString("minutes_fare")) <= 0.0)
                    Minute_lay.setVisibility(View.GONE);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        FontHelper.applyFont(this, findViewById(R.id.headlayout));
        Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
        getApplicationContext().stopService(intent);
        SessionSave.saveSession("TaxiStatus", "", ReceiptAct.this);
        SessionSave.saveSession("trip_id", "", ReceiptAct.this);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    final float Rating_number = P_ratingbar.getRating();
                    JSONObject j = new JSONObject();
                    j.put("pass_id", SessionSave.getSession("Pass_Tripid", ReceiptAct.this));
                    j.put("ratings", "" + Rating_number);
                    j.put("comments", comment.getText().toString());
                    if (Rating_number == 0 && comment.getText().toString().trim().equals(""))
                        Toast.makeText(ReceiptAct.this, NC.getString(R.string.no_info), Toast.LENGTH_SHORT).show();
                    else if (!comment.getText().toString().trim().equals("") && Rating_number == 0) {
                        final View view = View.inflate(ReceiptAct.this, R.layout.netcon_lay, null);
                        mDialog = new Dialog(ReceiptAct.this, R.style.dialogwinddow);
                        mDialog.setContentView(view);
                        mDialog.setCancelable(true);
                        FontHelper.applyFont(ReceiptAct.this, view.findViewById(R.id.alert_id));
                        mDialog.show();
                        // final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
                        final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
                        final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
                        final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
                        button_failure.setVisibility(View.VISIBLE);
                        //title_text.setText("" + NC.getResources().getString(R.string.message));

                        message_text.setText("" + NC.getString(R.string.zerorating));
                        button_success.setText("" + NC.getResources().getString(R.string.ok));
                        button_failure.setText("" + NC.getResources().getString(R.string.cancel));
//                        int maxLength = 300;
//                        setEditTextMaxLength(maxLength,message_text);
//                        message_text.setFocusable(false);
                        button_success.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                // TODO Auto-generated method stub
                                try {
                                    mDialog.dismiss();
                                    JSONObject j = new JSONObject();
                                    j.put("pass_id", SessionSave.getSession("Pass_Tripid", ReceiptAct.this));
                                    j.put("ratings", "" + Rating_number);
                                    j.put("comments", comment.getText().toString());
                                    new RatingTrip("type=update_ratings_comments", j);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        });
                        button_failure.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                mDialog.dismiss();
                            }
                        });
                    }
                    // alert_view(ReceiptAct.this, getString(R.string.message), getString(R.string.zerorating), getString(R.string.ok), getString(R.string.cancel));
                    else
                        new RatingTrip("type=update_ratings_comments", j);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    finish();
                }
            }
        });
        // To get the ratings using rating bar..
        P_ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar rBar, final float arg1, final boolean arg2) {

//                try {
//                    final float Rating_number = rBar.getRating();
//                    JSONObject j = new JSONObject();
//                    j.put("pass_id", SessionSave.getSession("Pass_Tripid", ReceiptAct.this));
//                    j.put("ratings", "" + Rating_number);
//                    j.put("comments", comment.getText().toString());
//                    new RatingTrip("type=update_ratings_comments", j);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                    finish();
//                }
            }
        });
        // This click listener finish this activity and move to book taxi activity.
        skip_this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
                SessionSave.saveSession("trip_id", "", ReceiptAct.this);
                showLoading(ReceiptAct.this);
                Intent i;
                i = new Intent(ReceiptAct.this, MainHomeFragmentActivity.class);
                startActivity(i);
                finish();
            }
        });
        // This click listener for facebook share with company detail and pickup place.
        Fb_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                // onFacebookClick();
                onClickPostStatusUpdate();
            }
        });
        // This click listener for twitter share with company details.
        Tw_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                initShareIntentTwi("com.twitter.android");
            }
        });
        // This click listener for message using network service share with company details.
        Msg_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                sendSms();
            }
        });
        // This click listener for mail share with company details and play store app links.
        Mail_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                sendEmail();
            }
        });
    }

    public void setEditTextMaxLength(int length, EditText edt_text) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        edt_text.setFilters(FilterArray);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {

        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void onClickPostStatusUpdate() {

        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    private void postStatusUpdate() {

        Package pack = ReceiptAct.this.getClass().getPackage();
        String packtxt = pack.toString();
        String packarr[] = packtxt.split(" ");
        Profile profile = Profile.getCurrentProfile();
        // return new FacebookDialog.ShareDialogBuilder(this).setCaption("" + NC.getResources().getString(R.string.successfully_completed) + "" + pickup_place).setName("" + NC.getResources().getString(R.string.app_name)).setDescription(SessionSave.getSession("TellfrdMsg", ReceiptAct.this) + "\n" + NC.getResources().getString(R.string.pass_app_link) + "\n" + NC.getResources().getString(R.string.driver_app_link)).setLink(SessionSave.getSession("api_base", ReceiptAct.this));
        ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle("" + NC.getResources().getString(R.string.app_name)).setContentDescription(SessionSave.getSession("TellfrdMsg", ReceiptAct.this)).setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())).build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private boolean hasPublishPermission() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null || allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    /**
     * <p>
     * This method used to perform what action to be done is photo share or status update.
     * </p>
     */
    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {

        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but
        // we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction) {
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    /**
     * this metrhod is used to share the app link to twitterthrough sms
     */

    public void sendSms() {

        final Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
        intentsms.putExtra("sms_body", SessionSave.getSession("TellfrdMsg", ReceiptAct.this) + "\n" + NC.getResources().getString(R.string.pass_app_link));
        startActivity(intentsms);
    }

    /**
     * this metrhod is used to share the app link through email
     */

    public void sendEmail() {

        final String[] TO = {""};
        final String[] CC = {""};
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Experience with "+getString(R.string.app_name));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n" + SessionSave.getSession("TellfrdMsg", ReceiptAct.this) + "\n" + NC.getResources().getString(R.string.pass_app_link));
        try {
            startActivity(Intent.createChooser(emailIntent, NC.getResources().getString(R.string.sendmail)));
        } catch (final android.content.ActivityNotFoundException ex) {
            ShowToast(ReceiptAct.this, "" + NC.getResources().getString(R.string.there_is_no_email_client_installed));
        }
    }

    /**
     * this metrhod is used to share the app link to twitter
     */

    public void initShareIntentTwi(final String type) {

        final String review = "" + NC.getResources().getString(R.string.pass_app_link);
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, review);
        tweetIntent.setType("text/plain");
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android"));
            startActivity(i);
        }
    }

    /**
     * this class is used to call the rating api which user rate the trip
     */

    private class RatingTrip implements APIResult, DialogInterface {
        private Dialog mDialog;
        private String driverID;

        public RatingTrip(final String string, JSONObject data) {
            new APIService_Retrofit_JSON(ReceiptAct.this, this, data, false).execute(string);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {

            if (isSuccess)
                try {

                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        submit.setClickable(false);
                        submit.setVisibility(View.GONE);
                        comment.setVisibility(View.GONE);
                        P_ratingbar.setIsIndicator(true);
                        int favDriver = 0;
                        try {
                            favDriver = json.getInt("set_fav_driver");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (favDriver == 1) {
                            driverID = json.getString("fav_driver_id");
                            new Dialog_Common().setmCustomDialog(ReceiptAct.this, this, NC.getResources().getString(R.string.message),
                                    json.getString("message"), NC.getResources().getString(R.string.ok), NC.getResources().getString(R.string.cancel));
                        } else {
                            final View view = View.inflate(ReceiptAct.this, R.layout.alert_view, null);
                            mDialog = new Dialog(ReceiptAct.this, R.style.dialogwinddow);
                            mDialog.setContentView(view);
                            mDialog.setCancelable(true);
                            FontHelper.applyFont(ReceiptAct.this, view.findViewById(R.id.alert_id));
                            mDialog.show();
                            final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
                            final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
                            final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
                            final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
                            button_failure.setVisibility(View.GONE);
                            title_text.setText("" + NC.getResources().getString(R.string.message));
                            message_text.setText("" + json.getString("message"));
                            button_success.setText("" + NC.getResources().getString(R.string.ok));
                            button_success.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    // TODO Auto-generated method stub
                                    try {
                                        mDialog.dismiss();
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                } catch (final Exception e) {
                    finish();

                    SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
                    SessionSave.saveSession("trip_id", "", ReceiptAct.this);
                }
        }

        @Override
        public void onSuccess(View view, Dialog mDialog, String resultCode) {
            mDialog.dismiss();
            try {
                JSONObject j = new JSONObject();
                j.put("passenger_id", SessionSave.getSession("Id", ReceiptAct.this));
                j.put("driver_id", "" + driverID);
                new FavouriteDriver("type=set_favourite_driver", j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(View view, Dialog mDialog, String resultCode) {
            mDialog.dismiss();

        }
    }

    /**
     * this class is used to call the favourite driver api
     */

    private class FavouriteDriver implements APIResult {

        public FavouriteDriver(final String string, JSONObject data) {

            new APIService_Retrofit_JSON(ReceiptAct.this, this, data, false).execute(string);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {

            if (isSuccess)
                try {
                    final JSONObject json = new JSONObject(result);
                    Toast.makeText(ReceiptAct.this, json.getString("message"), Toast.LENGTH_LONG).show();
                    SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
                    SessionSave.saveSession("trip_id", "", ReceiptAct.this);
                    Intent i;
                    //   Bundle fav=new Bundle();

                    i = new Intent(ReceiptAct.this, MainHomeFragmentActivity.class);
                    if (json.getString("status").trim().equals("1")) {
                        //showLoading(ReceiptAct.this);

                        //   fav.putString("goto","favdriv");
                        i.putExtra("goto", "favdriv");
                    }
                    startActivity(i);
                    finish();
                } catch (final Exception e) {
                    finish();
                    SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
                    SessionSave.saveSession("trip_id", "", ReceiptAct.this);
                }
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
//        SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
//        SessionSave.saveSession("trip_id", "", ReceiptAct.this);
        SessionSave.saveSession("Pass_Tripid", "", ReceiptAct.this);
        SessionSave.saveSession("trip_id", "", ReceiptAct.this);
        //showLoading(ReceiptAct.this);
        Intent i;
        i = new Intent(ReceiptAct.this, MainHomeFragmentActivity.class);
        startActivity(i);
        finish();
    }
}
