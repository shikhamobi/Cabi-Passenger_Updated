package com.cabipassenger.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.util.AppCacheImage;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.DownloadImageAndsavetoCache;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.RoundedImageView;
import com.cabipassenger.util.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * this class is used to share the app link to friends through social media
 */

public class
InviteFriendsFrag extends Fragment implements OnClickListener {
    private LinearLayout SlideImg;
    private static Typeface ContenttypeFace;
    private LinearLayout Donelay;
    private TextView BackBtn;
    private TextView leftIcon;
    private TextView HeadTitle;
    private TextView ismsTxt;
    private TextView iemailTxt;
    private TextView ifacebookTxt;
    private TextView iwhatsappTxt;
    private TextView itwitterTxt;
    private TextView referalamtTxt;
    private TextView referalcdeTxt;
    private LinearLayout detaillayout;

    private String PENDING_ACTION_BUNDLE_KEY = "";
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private PendingAction pendingAction = PendingAction.NONE;
    private RoundedImageView profileImg;
    private String invitesubject = "";
    private String invitemsg = "";
    private double refamount = 0.0;
    private LinearLayout invite_main;
    LinearLayout invite_loading;
    private String[] packarr;
    private Dialog mshowDialog;


    public void priorChanges(View v) {
        PENDING_ACTION_BUNDLE_KEY = getActivity().getPackageName() + "PendingAction";


        Donelay = (LinearLayout) v.findViewById(R.id.rightlay);
        Donelay.setVisibility(View.INVISIBLE);
        leftIcon = (TextView) v.findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        BackBtn = (TextView) v.findViewById(R.id.back_text);
        BackBtn.setVisibility(View.VISIBLE);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.invite_friend));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.invitefriend_lay, container, false);
        priorChanges(v);
        Initialize(v);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        return v;
    }

    /**
     * initalize view for the entire fragment
     *
     * @param v
     */
    public void Initialize(View v) {
        // TODO Auto-generated method stub
        invite_main = (LinearLayout) v.findViewById(R.id.invite_main);
        invite_loading = (LinearLayout) v.findViewById(R.id.invite_loading);
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.rootContain));
        SlideImg = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        if (!SessionSave.getSession("facebook_key", getActivity()).equals(""))
            FacebookSdk.setApplicationId(SessionSave.getSession("facebook_key", getActivity()));
        else
            FacebookSdk.setApplicationId("1659604664078736");
        ImageView iv = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        ismsTxt = (TextView) v.findViewById(R.id.ismsTxt);
        iemailTxt = (TextView) v.findViewById(R.id.iemailTxt);
        ifacebookTxt = (TextView) v.findViewById(R.id.ifacebookTxt);
        iwhatsappTxt = (TextView) v.findViewById(R.id.iwhatsappTxt);
        itwitterTxt = (TextView) v.findViewById(R.id.itwitterTxt);
        profileImg = (RoundedImageView) v.findViewById(R.id.profileImg);
        referalcdeTxt = (TextView) v.findViewById(R.id.referalcdeTxt);
        referalamtTxt = (TextView) v.findViewById(R.id.referalamtTxt);
        detaillayout = (LinearLayout) v.findViewById(R.id.detaillay);
        referalcdeTxt.setInputType(Typeface.BOLD);
        //referalamtTxt.setInputType(Typeface.BOLD);

        callbackManager = CallbackManager.Factory.create();
        try {
            if (getActivity() != null) {
                FacebookSdk.sdkInitialize(getActivity());
                shareDialog = new ShareDialog(getActivity());
                shareDialog.registerCallback(callbackManager, shareCallback);
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                        handlePendingAction();
                    }
                };
                // Can we present the share dialog for regular links?
                canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
                // Can we present the share dialog for photos?
                canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String Profileimgepath = SessionSave.getSession("ProfileImage", getActivity());
        if (!AppCacheImage.loadBitmap(Profileimgepath, profileImg)) {
          //  System.out.println("Image... not avail in cache");
            new DownloadImageAndsavetoCache(profileImg).execute(Profileimgepath);

        }

        if (!SessionSave.getSession("RefCode", getActivity()).equalsIgnoreCase("") && !SessionSave.getSession("RefCode", getActivity()).equalsIgnoreCase(null))
            referalcdeTxt.setText("" + SessionSave.getSession("RefCode", getActivity()));
        if (!SessionSave.getSession("RefAmount", getActivity()).equalsIgnoreCase("") && !SessionSave.getSession("RefAmount", getActivity()).equalsIgnoreCase(null))
            refamount = Double.parseDouble(SessionSave.getSession("RefAmount", getActivity()));
        referalamtTxt.setText("" + SessionSave.getSession("Currency", getActivity()) + " and earn " + String.format(Locale.UK, "%.2f", refamount));
        Package pack = getActivity().getClass().getPackage();
        String packtxt = pack.toString();
        packarr = packtxt.split(" ");
        invitesubject = "" + NC.getResources().getString(R.string.invite_friend);
        //   invitemsg = " Excuse my brevity,Sign up with the referral code " + SessionSave.getSession("RefCode", getActivity()) + " and earn " + SessionSave.getSession("Currency", getActivity()) + String.format("%.2f", refamount) + ".Download the app from:https://play.google.com/store/apps/details?id=" + packarr[1];
        referalcdeTxt.setTypeface(setcontentTypeface(), Typeface.BOLD);
        // referalamtTxt.setTypeface(setcontentTypeface(), Typeface.BOLD);
        CheckReferral();
        setOnclickListener();
    }

    /**
     * this typeface method is set typeface font
     */
    public Typeface setcontentTypeface() {

        if (ContenttypeFace == null)
            ContenttypeFace = Typeface.createFromAsset(getActivity().getAssets(), FontHelper.FONT_TYPEFACE);
        return ContenttypeFace;
    }

    /**
     * Check the referal amount from server side and updates UI
     */
    private void CheckReferral() {

        try {
            JSONObject j = new JSONObject();
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            String url = "type=invite_with_referral";
            new RefferalAmt(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * getActivity() class used to check RefferalAmt
     * <p>
     * getActivity() class used to check RefferalAmt
     * <p>
     *
     * @author developer
     */
    private class RefferalAmt implements APIResult {
        private RefferalAmt(final String url, JSONObject data) {

            new APIService_Retrofit_JSON_NoProgress(getActivity(), this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub

            if (isSuccess && getActivity() != null) {
                invite_main.setVisibility(View.VISIBLE);
                invite_loading.setVisibility(View.GONE);
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("RefCode", json.getJSONObject("detail").getString("referral_code"), getActivity());
                        SessionSave.saveSession("RefAmount", json.getJSONObject("detail").getString("referral_amount"), getActivity());
                        if (SessionSave.getSession("RefCode", getActivity()) != null)
                            if (!SessionSave.getSession("RefCode", getActivity()).equalsIgnoreCase(""))
                                referalcdeTxt.setText("" + SessionSave.getSession("RefCode", getActivity()));
                        if (!SessionSave.getSession("RefAmount", getActivity()).equalsIgnoreCase("") && !SessionSave.getSession("RefAmount", getActivity()).equalsIgnoreCase(null))
                            refamount = Double.parseDouble(SessionSave.getSession("RefAmount", getActivity()));
                        invitemsg = NC.getResources().getString(R.string.excuse_me_brevity) + SessionSave.getSession("RefCode", getActivity()) + NC.getResources().getString(R.string.and_earn)
                              +" "  + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", refamount)+" " + NC.getResources().getString(R.string.download_from) + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                        referalamtTxt.setText("" + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", refamount));

                        SessionSave.saveSession("referral_settings", json.getJSONObject("detail").getString("referral_settings"), getActivity());
                        SessionSave.saveSession("referral_settings_message", json.getJSONObject("detail").getString("referral_settings_message"), getActivity());
                        if (Integer.parseInt(SessionSave.getSession("referral_settings", getActivity())) == 0)
                            detaillayout.setVisibility(View.GONE);
                        else
                            detaillayout.setVisibility(View.VISIBLE);
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
//                alert_view(getActivity(), "Message", "" + result, "" + NC.getResources().getString(R.string.ok), "");
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            ShowToast(getActivity(), getString(R.string.server_con_error));
                        }
                    });
            }
        }
    }

    /**
     * this method is used to show the toast message in center position
     */

    public void ShowToast(Context context, String s) {
        Toast toast = Toast.makeText(context, "" + s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * this method is used to share the link to fb
     */

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {

          //  Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {

            String title = NC.getString(R.string.error);
            String alertMessage = error.getMessage();

           // Log.d("FB Error ", alertMessage);

            showResult(title, alertMessage);

        }

        @Override
        public void onSuccess(Sharer.Result result) {

         //   Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                NC.getString(R.string.success);
                result.getPostId();
            }
        }

        private void showResult(String title, String alertMessage) {

            new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(alertMessage).setPositiveButton(R.string.ok, null).show();
        }
    };

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    private void setOnclickListener() {

        BackBtn.setOnClickListener(this);
        ismsTxt.setOnClickListener(this);
        iemailTxt.setOnClickListener(this);
        ifacebookTxt.setOnClickListener(this);
        itwitterTxt.setOnClickListener(this);
        iwhatsappTxt.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);

        AppEventsLogger.activateApp(getActivity());
        try {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {

        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {

        if (mshowDialog != null && mshowDialog.isShowing()) {
            mshowDialog.dismiss();
            mshowDialog = null;

        }
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private void onClickPostStatusUpdate() {

        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }

    /**
     * this method is used to post the link to social medias
     */

    private void postStatusUpdate() {

        Profile profile = Profile.getCurrentProfile();

        //		 return new FacebookDialog.ShareDialogBuilder(getActivity()).setCaption("" + NC.getResources().getString(R.string.successfully_completed) + "" + pickup_place).setName("" + NC.getResources().getString(R.string.app_name)).setDescription(SessionSave.getSession("TellfrdMsg", ReceiptAct.getActivity()) + "\n" + NC.getResources().getString(R.string.pass_app_link) + "\n" + NC.getResources().getString(R.string.driver_app_link)).setLink(SessionSave.getSession("api_base", ReceiptAct.getActivity()));
        String link = SessionSave.getSession("api_base", getActivity());
       // ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(invitesubject).setContentDescription(invitemsg).setContentUrl(Uri.parse(SessionSave.getSession("api_base", getActivity()))).build();

       // System.out.println("hinvite_message" + invitemsg);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setQuote(invitemsg)
                .setContentUrl((Uri.parse(SessionSave.getSession("api_base", getActivity()))))
              //  .setContentUrl("")
                .build();
        shareDialog.show(linkContent);
//        if (canPresentShareDialog) {
//
//            shareDialog.show(linkContent);
//        } else if (profile != null && hasPublishPermission()) {
//
//            ShareApi.share(linkContent, shareCallback);
//
//        } else {
//
//            pendingAction = PendingAction.POST_STATUS_UPDATE;
//        }
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
     * getActivity() method used to perform what action to be done is photo share or status update.
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
     * this method is used for share via sms
     */

    public void sendSms() {

        final Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
        intentsms.putExtra("sms_body", invitemsg);
        startActivity(intentsms);
    }

    /**
     * this method is used for email share
     */

    public void sendEmail() {

        final String[] TO = {""};
        final String[] CC = {""};
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + NC.getResources().getString(R.string.taxiname) + "-" + invitesubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n" + invitemsg);
        PackageManager packManager = getActivity().getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.google.android.gm")) {
                emailIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                break;
            }
        }
        try {
            startActivity(Intent.createChooser(emailIntent, NC.getResources().getString(R.string.sendmail)));
        } catch (final android.content.ActivityNotFoundException ex) {
            //  ShowToast(getActivity(), "" + NC.getResources().getString(R.string.there_is_no_email_client_installed));
        }
    }

    /**
     * this method is used For twitter share
     */

    public void initShareIntentTwi(final String type) {

        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, invitemsg);
        tweetIntent.setType("text/plain");
        PackageManager packManager = getActivity().getPackageManager();
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
            // Toast.makeText(getActivity(), "Twitter app isn't found", Toast.LENGTH_LONG).show();
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android"));
            startActivity(i);
        }
    }

    /**
     * this method used for sharing in whatsapp
     */

    public void onClickWhatsApp() {

        PackageManager pm = getActivity().getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "" + invitemsg;
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (NameNotFoundException e) {
            // Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
            startActivity(i);
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_text:
                break;
            case R.id.ismsTxt:
                sendSms();
                break;
            case R.id.iemailTxt:
                sendEmail();
                break;
            case R.id.ifacebookTxt:
                onClickPostStatusUpdate();
                break;
            case R.id.iwhatsappTxt:
                onClickWhatsApp();
                break;
            case R.id.itwitterTxt:
                initShareIntentTwi("com.twitter.android");
                break;
            default:
                break;
        }
    }

    // Slider menu used to move from one activity to another activity.
}
