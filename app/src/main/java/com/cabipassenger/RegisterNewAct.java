package com.cabipassenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.data.Autocortctryname;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * this class is used to register the user details
 *
 * @author developer
 */
public class RegisterNewAct extends MainActivity implements OnClickListener {
    private LinearLayout Donelay;
    private TextView BackBtn, hidePwd, hideconPwd;
    private TextView leftIcon;
    private TextView HeadTitle;
    private EditText fnameEdt;
    private EditText lnameEdt;
    private EditText emailEdt;
    private EditText mobileEdt;
    private EditText passwordEdt;
    private EditText confirmpswdEdt;
    private EditText referalEdt;
    private ImageView referralinfoBut;
    private ImageView showpwdImg;
    private ImageView showconpwdImg;
    private Button submitBtn;
    private Spinner mobilecodespn;
    private ArrayAdapter<String> mobileno_adapter;
    private String countrycode = "";
    private String[] mobilenoary;
    private String fbaccesstoken = "";
    private String fbuserid = "";
    private String[] countryCodeArray;
    private int positionFirst;
    private List<String> val = new ArrayList<String>();
    private List<String> code = new ArrayList<String>();
    private List<String> country = new ArrayList<String>();
    private List<String> arryaCountryList = new ArrayList<String>();
    private String fbname = "";
    private String lname = "";
    private Dialog mDialog;
    private TextView backarrow;
    private LinearLayout CancelBtn;
    private TextView Spinnertext;
    //siva 21/02/2018
    private EditText promoEdt;
    private ImageView promoinfo;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        return R.layout.registernewlay;
    }

    @Override
    public void priorChanges() {
        FontHelper.applyFont(this, findViewById(R.id.login_contain));
        findViewById(R.id.headlayout).setVisibility(View.VISIBLE);
        super.priorChanges();
    }

    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        if (!SessionSave.getSession("facebook_key", RegisterNewAct.this).equals(""))
            FacebookSdk.setApplicationId(SessionSave.getSession("facebook_key", RegisterNewAct.this));
        else
            FacebookSdk.setApplicationId("1659604664078736");
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), RegisterNewAct.this);
        TaxiUtil.mActivitylist.add(this);
        Donelay = (LinearLayout) findViewById(R.id.rightlay);
        Donelay.setVisibility(View.INVISIBLE);
        leftIcon = (TextView) findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        BackBtn = (TextView) findViewById(R.id.back_text);
        CancelBtn = (LinearLayout) findViewById(R.id.leftIconTxt);
//        BackBtn.setVisibility(View.VISIBLE);
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        FontHelper.applyFont(this, HeadTitle);
        HeadTitle.setText(NC.getResources().getString(R.string.register));
//        Toast.makeText(RegisterNewAct.this, NC.getResources().getString(R.string.register), Toast.LENGTH_SHORT).show();
        fnameEdt = (EditText) findViewById(R.id.fnameEdt);
        lnameEdt = (EditText) findViewById(R.id.lnameEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);
        passwordEdt = (EditText) findViewById(R.id.passwordEdt);
        confirmpswdEdt = (EditText) findViewById(R.id.confirmpswdEdt);
        passwordEdt.setTypeface(Typeface.DEFAULT);
        confirmpswdEdt.setTypeface(Typeface.DEFAULT);
//        passwordEdt.setTransformationMethod(new PasswordTransformationMethod());
        referalEdt = (EditText) findViewById(R.id.referalEdt);
        // referalEdt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        referralinfoBut = (ImageView) findViewById(R.id.referralinfo);
        showpwdImg = (ImageView) findViewById(R.id.showpwd);
        showconpwdImg = (ImageView) findViewById(R.id.showconpwd);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        mobilecodespn = (Spinner) findViewById(R.id.mobilecodespn);
        hidePwd = (TextView) findViewById(R.id.hidePwd);
        hideconPwd = (TextView) findViewById(R.id.hideconpwd);
        Spinnertext = (TextView) findViewById(R.id.spinner_value);
        promoEdt = (EditText) findViewById(R.id.promoEdt);
        promoinfo = (ImageView) findViewById(R.id.promoinfo);

        backarrow = (TextView) findViewById(R.id.leftIcon);
        backarrow.setBackgroundResource(R.drawable.back);
        backarrow.setVisibility(View.VISIBLE);


        backarrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

//                if (imm.isAcceptingText()) {
//                    InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                }

                //         imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0 );


                SessionSave.saveSession("IsOTPSend", "", RegisterNewAct.this);
                SessionSave.saveSession("f_name", "", RegisterNewAct.this);
                SessionSave.saveSession("l_name", "", RegisterNewAct.this);
                SessionSave.saveSession("e_mail", "", RegisterNewAct.this);
                SessionSave.saveSession("m_no", "", RegisterNewAct.this);
                SessionSave.saveSession("p_wd", "", RegisterNewAct.this);
                SessionSave.saveSession("cp_wd", "", RegisterNewAct.this);
                SessionSave.saveSession("ref_txt", "", RegisterNewAct.this);
                SessionSave.saveSession("promo_txt", "", RegisterNewAct.this);

                //Intent intent = new Intent(getApplicationContext(), UserHomeAct.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(intent);
                finish();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isAcceptingText()) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

                SessionSave.saveSession("IsOTPSend", "", RegisterNewAct.this);
                SessionSave.saveSession("f_name", "", RegisterNewAct.this);
                SessionSave.saveSession("l_name", "", RegisterNewAct.this);
                SessionSave.saveSession("e_mail", "", RegisterNewAct.this);
                SessionSave.saveSession("m_no", "", RegisterNewAct.this);
                SessionSave.saveSession("p_wd", "", RegisterNewAct.this);
                SessionSave.saveSession("cp_wd", "", RegisterNewAct.this);
                SessionSave.saveSession("ref_txt", "", RegisterNewAct.this);
                SessionSave.saveSession("promo_txt", "", RegisterNewAct.this);

                // Intent intent = new Intent(getApplicationContext(), UserHomeAct.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(intent);
                finish();
            }
        });

        String coutryList;

        countryCodeArray = getResources().getStringArray(R.array.country_code);

        for (String s : countryCodeArray) {

            val.add(s);

        }
        for (int i = 0; i < val.size(); i++) {
            /*countryCodeArray = val.get(i).toString().replaceAll("\\s", "").split("&");

            code.add(countryCodeArray[0]);
            country.add(countryCodeArray[1]);

            String coutryList = countryCodeArray[0] + "       " + countryCodeArray[1];
            arryaCountryList.add(coutryList);*/

            countryCodeArray = val.get(i).toString().trim().split("&");
            code.add(countryCodeArray[0]);
            country.add(countryCodeArray[1]);


            coutryList = Autocortctryname.Autocortct(countryCodeArray[0], countryCodeArray[1]);

            arryaCountryList.add(coutryList);

        }

        mobileno_adapter = new FontHelper.MySpinnerAdapter(this, R.layout.monthitem_spinnerlay, arryaCountryList);
        mobileno_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mobilecodespn.setAdapter(mobileno_adapter);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + bundle.getString("Message"), "" + NC.getResources().getString(R.string.ok), "");

            emailEdt.setText(bundle.getString("Email"));
            fnameEdt.setText(bundle.getString("FName"));
            lnameEdt.setText(bundle.getString("LName"));
            fbaccesstoken = bundle.getString("FbaccToken");
            fbuserid = bundle.getString("Fbuserid");
        } else {
            if (!SessionSave.getSession("f_name", RegisterNewAct.this).equals("")) {
                fnameEdt.setText(SessionSave.getSession("f_name", RegisterNewAct.this));
                lnameEdt.setText(SessionSave.getSession("l_name", RegisterNewAct.this));
                emailEdt.setText(SessionSave.getSession("e_mail", RegisterNewAct.this));
                mobileEdt.setText(SessionSave.getSession("m_no", RegisterNewAct.this));
                passwordEdt.setText(SessionSave.getSession("p_wd", RegisterNewAct.this));
                confirmpswdEdt.setText(SessionSave.getSession("cp_wd", RegisterNewAct.this));
                referalEdt.setText(SessionSave.getSession("ref_txt", RegisterNewAct.this));
                promoEdt.setText(SessionSave.getSession("promo_txt", RegisterNewAct.this));
            }

        }


        Spinnertext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mobilecodespn.performClick();
            }
        });


        try {
            String mobcodeary[];

            for (int i = 0; i < country.size(); i++) {
                if ((SessionSave.getSession("country_code", RegisterNewAct.this)).trim().equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(i).toString().trim())).trim().replaceAll("\\s", ""))) {
                    //findPosition
                    positionFirst = i;
                    mobilecodespn.setSelection(positionFirst);

                    break;
                }
                // Setting Default as Kuwait if not get Country
                else {
                    positionFirst = 966;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        Point pointSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(pointSize);
        mobilecodespn.setDropDownWidth(pointSize.x);
        mobilecodespn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                mobilenoary = parent.getItemAtPosition(arg2).toString().split("\\s+");
                TextView txt = ((TextView) view);
                if (txt != null) {
                    txt.setTextColor(CL.getResources().getColor(RegisterNewAct.this, R.color.black));
                    txt.setGravity(Gravity.CENTER_VERTICAL);
                    txt.setText("" + mobilenoary[0]);
                    Spinnertext.setText("" + mobilenoary[0]);
                    countrycode = mobilenoary[0];
                    int width = 0;
                    if (mobilenoary[0].length() > 4)
                        width = 100;
                    else if (mobilenoary[0].length() == 4)
                        width = 75;
                    else
                        width = 60;
                }
//                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(dpToPx(width), LinearLayout.LayoutParams.WRAP_CONTENT);
//                mobilecodespn.setLayoutParams(params);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        /* newly add for password hide and show*/

        hidePwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hidePwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(RegisterNewAct.this, passwordEdt);

                } else {
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hidePwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(RegisterNewAct.this, passwordEdt);
                }
            }
        });
        passwordEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {

                    if (passwordEdt.getText().toString().length() > 0)
                        hidePwd.setVisibility(View.VISIBLE);
                    else
                        hidePwd.setVisibility(View.INVISIBLE);
                } else {
                    hidePwd.setVisibility(View.GONE);
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(RegisterNewAct.this, passwordEdt);
                    if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {

                        hidePwd.setText("" + NC.getResources().getString(R.string.show));

                    }

                }
            }
        });

        passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (passwordEdt.getText().toString().length() > 0) {
                    hidePwd.setVisibility(View.VISIBLE);
                } else {
                    hidePwd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
//        passwordEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if (hasFocus)
//                    showpwdImg.setVisibility(View.VISIBLE);
//                else {
//                    showpwdImg.setVisibility(View.GONE);
//                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//            }
//        });

        hideconPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hideconPwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hideconPwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(RegisterNewAct.this, confirmpswdEdt);
                } else {
                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hideconPwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(RegisterNewAct.this, confirmpswdEdt);
                }
            }
        });
        confirmpswdEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {

                    if (confirmpswdEdt.getText().toString().length() > 0)
                        hideconPwd.setVisibility(View.VISIBLE);
                    else
                        hideconPwd.setVisibility(View.INVISIBLE);

                } else {
                    hideconPwd.setVisibility(View.GONE);
                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(RegisterNewAct.this, confirmpswdEdt);
                    if (hideconPwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        hideconPwd.setText("" + NC.getResources().getString(R.string.show));
                    }


                }
            }
        });

        confirmpswdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (confirmpswdEdt.getText().toString().length() > 0) {
                    hideconPwd.setVisibility(View.VISIBLE);
                } else {
                    hideconPwd.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
//        confirmpswdEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if (hasFocus)
//                    showconpwdImg.setVisibility(View.VISIBLE);
//                else {
//                    showconpwdImg.setVisibility(View.GONE);
//                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//            }
//        });
        callbackManager = CallbackManager.Factory.create();
        fbregBtn = (LoginButton) findViewById(R.id.fbregBtn);
        fbregBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Access_token = loginResult.getAccessToken().getToken();
                handlePendingAction();
                updateUI();
            }

            @Override
            public void onCancel() {

                if (pendingAction != PendingAction.NONE) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                updateUI();
            }

            @Override
            public void onError(FacebookException exception) {

                if (pendingAction != PendingAction.NONE && exception instanceof FacebookAuthorizationException) {
                    showAlert();
                    pendingAction = PendingAction.NONE;
                }
                updateUI();
            }

            private void showAlert() {

                new AlertDialog.Builder(RegisterNewAct.this).setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted).setPositiveButton(R.string.ok, null).show();
            }
        });
        fbregBtn.setReadPermissions("email");
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // updateUI();
                // It's possible that we were waiting for Profile to be
                // populated in order to
                // post a status update.
                handlePendingAction();
            }
        };
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, shareCallback);
        if (BsavedInstanceState != null) {
            String name = BsavedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
        setOnclickListener();
        // System.err.println("country code" + GetCountryZipCode());
    }

    private void setOnclickListener() {

        BackBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        referralinfoBut.setOnClickListener(this);
        showpwdImg.setOnClickListener(this);
        showconpwdImg.setOnClickListener(this);
        promoinfo.setOnClickListener(this);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * this method is used to call the register api
     */

    private void RegisterData() {

        String fnameTxt = fnameEdt.getText().toString();
        String lnameTxt = lnameEdt.getText().toString();
        String emailTxt = emailEdt.getText().toString();
        String mobilenoTxt = mobileEdt.getText().toString();
        String passwdTxt = passwordEdt.getText().toString();
        String conpasswdTxt = confirmpswdEdt.getText().toString();
        String referalTxt = referalEdt.getText().toString();
        String promoTxt = promoEdt.getText().toString();

        if (validations(ValidateAction.isValidFirstname, RegisterNewAct.this, fnameTxt))
            if (validations(ValidateAction.isValidLastname, RegisterNewAct.this, lnameTxt))
                if (validations(ValidateAction.isValidMail, RegisterNewAct.this, emailTxt))
                    if (validations(ValidateAction.isValidphone, RegisterNewAct.this, mobilenoTxt))
                        if (validations(ValidateAction.isValidPassword, RegisterNewAct.this, passwdTxt))
                            if (validations(ValidateAction.isValidConfirmPassword, RegisterNewAct.this, conpasswdTxt))
                                if (!conpasswdTxt.equals(passwdTxt))
                                    ShowToast(RegisterNewAct.this, NC.getResources().getString(R.string.confirmation_password_mismatch_with_password));
                                else {
                                    try {
                                        JSONObject j = new JSONObject();
                                        j.put("first_name", fnameTxt);
                                        j.put("last_name", lnameTxt);
                                        j.put("email", emailTxt);
                                        j.put("phone", mobilenoTxt);
                                        j.put("country_code", countrycode);
                                        j.put("password", passwdTxt);
                                        j.put("confirm_password", passwdTxt);
                                        j.put("referral_codereferral_code", referalTxt);
                                        j.put("deviceid", TaxiUtil.mDevice_id);

                                        j.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
                                        j.put("devicetype", "1");
                                        j.put("accesstoken", "");
                                        j.put("userid", "");

                                        j.put("deviceid", TaxiUtil.mDevice_id);
                                        //siva 21/02/2018
                                        j.put("promo_code", promoTxt);
                                        final String url = "type=passenger_signup_single";
                                        new SignUp(url, j);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                }
    }

    /**
     * This class used to register with account details
     * <p>
     * This class used to register with account details
     * </p>
     *
     * @author developer
     */
    private class SignUp implements APIResult {
        private SignUp(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(RegisterNewAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), RegisterNewAct.this);
                        SessionSave.saveSession("Register", "1", RegisterNewAct.this);

                        SessionSave.saveSession("IsOTPSend", "1", RegisterNewAct.this);
                        SessionSave.saveSession("countrycode", countrycode, RegisterNewAct.this);

                        SessionSave.saveSession("f_name", fnameEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("l_name", lnameEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("e_mail", emailEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("m_no", mobileEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("p_wd", passwordEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("cp_wd", confirmpswdEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("ref_txt", referalEdt.getText().toString(), RegisterNewAct.this);
                        SessionSave.saveSession("promo_txt", promoEdt.getText().toString(), RegisterNewAct.this);

                        // SessionSave.saveSession("Phone",  mobileEdt.getText().toString(), RegisterNewAct.this);

                        if (referalEdt.getText().toString().length() > 0)
                            SessionSave.saveSession("IsReferAvail", "1", RegisterNewAct.this);
                        else
                            SessionSave.saveSession("IsReferAvail", "0", RegisterNewAct.this);


                        final Intent i = new Intent(RegisterNewAct.this, VerificationAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), RegisterNewAct.this);
                        SessionSave.saveSession("Register", "2", RegisterNewAct.this);
                        final Intent i = new Intent(RegisterNewAct.this, CardRegisterAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else {
                        SessionSave.saveSession("Register", "", RegisterNewAct.this);
                        alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(RegisterNewAct.this, getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    private final String PENDING_ACTION_BUNDLE_KEY = "com.cabi:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private String Access_token;
    private LoginButton fbregBtn;
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {


        }

        @Override
        public void onError(FacebookException error) {

            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {

            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {

            new AlertDialog.Builder(RegisterNewAct.this).setTitle(title).setMessage(alertMessage).setPositiveButton(R.string.ok, null).show();
        }
    };

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    @Override
    protected void onResume() {

        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    /**
     * <p>
     * This method used store the FB logged user details into bundle
     * </p>
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    /**
     * <p>
     * This method used get the FB logged user details
     * </p>
     */
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

    /**
     * <p>
     * This method used to check active session available or not,<br>
     * then call the passenger_fb_connect api with fb user information.
     * </p>
     */
    private void updateUI() {

        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        final String randompwd = getRandomString(7);
        final Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            Bundle params = new Bundle();
            params.putString("fields", "id,name,email");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {

                    try {

                        JSONObject data = response.getJSONObject();
                        fbaccesstoken = Access_token;
                        fbuserid = data.getString("id");
                        fbname = profile.getFirstName();
                        lname = profile.getLastName();
//                        System.out.println("user.getProperty(email)" + data.getString("id") + data.getString("name") + data.getString("email") + Access_token);
//                        JSONObject j = new JSONObject();
//                        j.put("accesstoken", Access_token);
//                        j.put("userid", data.getString("id"));
//                        j.put("fname", profile.getFirstName());
//                        j.put("lname", profile.getLastName());
//                        j.put("fbemail", data.getString("email"));
//                        j.put("devicetoken", "");
//                        j.put("deviceid", "" + SessionSave.getSession("mDevice_id", LoginAct.this));
//                        j.put("devicetype", "1");
//                        new FbLogin("type=passenger_fb_connect", j);


                        if (data.has("email")) {
                            String emailid = data.getString("email");
                            callfb(data.getString("id"), emailid, profile.getFirstName(), profile.getLastName());

                        } else {
                            //getEmailId(data.getString("id"), profile.getFirstName(), profile.getLastName());
                            callfb(data.getString("id"), "", profile.getFirstName(), profile.getLastName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();
        } else {
            Bundle params = new Bundle();
            params.putString("fields", "id,name,email");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {

                    try {

                        JSONObject data = response.getJSONObject();
                        fbaccesstoken = Access_token;
                        fbuserid = data.getString("id");
                        fbname = data.getString("name");
                        String emailid = "";
                        if (data.has("email")) {
                            emailid = data.getString("email");
                            callfb(data.getString("id"), emailid, data.getString("name"), "");
                        } else {
//                            getEmailId(data.getString("id"), data.getString("name"), "");
                            callfb(data.getString("id"), "", data.getString("name"), "");
                        }
                        //  System.out.println("user.getProperty(email)" + data.getString("id") + data.getString("name") + data.getString("email") + Access_token);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();
        }
    }

    void callfb(String id, String emailid, String firstname, String lastname) {
        try {
            JSONObject j = new JSONObject();
            j.put("accesstoken", Access_token);
            j.put("userid", id);
            j.put("fname", "" + fbname);
            j.put("lname", "" + lname);
            j.put("fbemail", emailid);
            j.put("devicetoken", FirebaseInstanceId.getInstance().getToken());

            j.put("deviceid", "" + SessionSave.getSession("mDevice_id", RegisterNewAct.this));
            j.put("devicetype", "1");
            new FbLogin("type=passenger_fb_connect", j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {

        PendingAction previouslyPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction) {
            case POST_PHOTO:
                break;
            case POST_STATUS_UPDATE:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

        SessionSave.saveSession("IsOTPSend", "", RegisterNewAct.this);
        SessionSave.saveSession("f_name", "", RegisterNewAct.this);
        SessionSave.saveSession("l_name", "", RegisterNewAct.this);
        SessionSave.saveSession("e_mail", "", RegisterNewAct.this);
        SessionSave.saveSession("m_no", "", RegisterNewAct.this);
        SessionSave.saveSession("p_wd", "", RegisterNewAct.this);
        SessionSave.saveSession("cp_wd", "", RegisterNewAct.this);
        SessionSave.saveSession("ref_txt", "", RegisterNewAct.this);
        SessionSave.saveSession("promo_txt", "", RegisterNewAct.this);

        // Intent intent = new Intent(getApplicationContext(), UserHomeAct.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_text:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isAcceptingText()) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                LoginManager.getInstance().logOut();
                finish();
                break;
            case R.id.submitBtn:
                RegisterData();
                break;
            case R.id.referralinfo:
                String s = SessionSave.getSession("referral_code_info", RegisterNewAct.this);

                alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + s.replace("-", "\n"), "" + NC.getResources().getString(R.string.ok), "");
                break;
            case R.id.showpwd:
                passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                FontHelper.applyFont(RegisterNewAct.this, passwordEdt);
                break;
            case R.id.showconpwd:
                confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                FontHelper.applyFont(RegisterNewAct.this, confirmpswdEdt);
                break;

            case R.id.promoinfo:
                String promostring = SessionSave.getSession("promo_code_info", RegisterNewAct.this);

                alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + "Enter promo code here to earn amount in wallet", "" + NC.getResources().getString(R.string.ok), "");
                break;
            default:
                break;
        }
    }

    //generate random password
    private static final String ALLOWED_CHARACTERS = "0123456789qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM";

    private static String getRandomString(final int sizeOfRandomString) {

        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    /**
     * this method is used to set max length when password shows text
     */

    public void setEditTextMaxLength(final EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }


    /**
     * this method is used to mail id from forget password alert
     */

    public void getEmailId(final String id, final String firstname, final String lastname) {
        // TODO Auto-generated method stub

        final View view = View.inflate(RegisterNewAct.this, R.layout.forgot_popup, null);
        mDialog = new Dialog(RegisterNewAct.this, R.style.NewDialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.show();
        final TextView t = (TextView) mDialog.findViewById(R.id.f_textview);
        t.setText(NC.getResources().getString(R.string.email));
        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
        mail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mail.setHint(NC.getResources().getString(R.string.enter_the_email));
        mDialog.findViewById(R.id.for_sep).setVisibility(View.VISIBLE);
        //mail.setMaxEms(60);
        setEditTextMaxLength(mail, 60);
        final TextView OK = (TextView) mDialog.findViewById(R.id.okbtn);
        final TextView Cancel = (TextView) mDialog.findViewById(R.id.cancelbtn);
        Cancel.setVisibility(View.GONE);

        Point pointSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(pointSize);

        OK.setOnClickListener(new OnClickListener() {
            private String emailid;

            @Override
            public void onClick(final View arg0) {
                // TODO Auto-generated method stub
                try {
                    emailid = mail.getText().toString();
                    if (validations(ValidateAction.isValidMail, RegisterNewAct.this, emailid)) {
                        callfb(id, emailid, firstname, lastname);
                    } else {
                        Toast.makeText(RegisterNewAct.this, NC.getResources().getString(R.string.enter_the_valid_email), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });

        Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {

                LoginManager.getInstance().logOut();
                mDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStop() {
        TaxiUtil.closeDialog(mDialog);
        super.onStop();
    }

    /**
     * This method used to login through fb
     * <p>
     * This method used to login through fb
     * </p>
     *
     * @author developer
     */
    private class FbLogin implements APIResult {
        private String email;
        private String id;
        private String photo;
        private String tellfrdMsg = "";
        private JSONObject fbData;

        public FbLogin(final String string, final JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(RegisterNewAct.this, this, data, false, SessionSave.getSession("base_url", RegisterNewAct.this) + SessionSave.getSession("api_key", RegisterNewAct.this) + "/?" + "lang=" + SessionSave.getSession("Lang", RegisterNewAct.this) + "&" + string).execute();
//            new APIService_Volley_JSON(RegisterNewAct.this, this, data, false).execute(string);
            fbData = data;
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        // successalert(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        tellfrdMsg = detail.getString("telltofriend_message");
                        id = detail.getString("id");
                        photo = detail.getString("profile_image");
                        SessionSave.saveSession("Email", email, RegisterNewAct.this);
                        SessionSave.saveSession("Id", id, RegisterNewAct.this);
                        SessionSave.saveSession("ProfileImage", photo, RegisterNewAct.this);
                        SessionSave.saveSession("ProfileName", detail.getString("name"), RegisterNewAct.this);
                        SessionSave.saveSession("RefCode", json.getJSONObject("detail").getString("referral_code"), RegisterNewAct.this);
                        SessionSave.saveSession("RefAmount", json.getJSONObject("detail").getString("referral_code_amount"), RegisterNewAct.this);
                        SessionSave.saveSession("Register", "", RegisterNewAct.this);
                        SessionSave.saveSession("Tellfrdmsg", tellfrdMsg, RegisterNewAct.this);
                        SessionSave.saveSession("About", json.getJSONObject("detail").getString("aboutpage_description"), RegisterNewAct.this);
                        SessionSave.saveSession("Currency", json.getJSONObject("detail").getString("site_currency") + " ", RegisterNewAct.this);
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), RegisterNewAct.this);

                        Intent intent = new Intent(getApplicationContext(), MainHomeFragmentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (json.getInt("status") == 2) {
                        SessionSave.saveSession("Register", "2", RegisterNewAct.this);
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, RegisterNewAct.this);
                        GetPhoneNumber();
                    } else if (json.getInt("status") == 3) {
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, RegisterNewAct.this);
                        SessionSave.saveSession("Register", "1", RegisterNewAct.this);
                        final Intent i = new Intent(RegisterNewAct.this, RegisterNewAct.class);
                        Bundle data = new Bundle();
                        data.putString("Message", json.getString("message"));
                        i.putExtras(data);
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == -2) {
                        alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");

                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, RegisterNewAct.this);
                        SessionSave.saveSession("Register", "1", RegisterNewAct.this);
//                        final Intent i = new Intent(RegisterNewAct.this, VerificationAct.class);
//                        Bundle data = new Bundle();
//                        data.putString("Message", json.getString("message"));
//                        startActivity(i);
//                        finish();
                    } else if (json.getInt("status") == 4) {
//                        alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, RegisterNewAct.this);
                        SessionSave.saveSession("Register", "2", RegisterNewAct.this);
                        final Intent i = new Intent(RegisterNewAct.this, CardRegisterAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == 10) {

//
                        getEmailId(fbuserid, fbname, "");
                        //   GetPhoneNumber();
                    } else {
                        LoginManager.getInstance().logOut();
                        GetPhoneNumber();
                        alert_view(RegisterNewAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(RegisterNewAct.this, getString(R.string.server_con_error));
                        }
                    });
                }

            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

//    /**
//     * This method used to get the mobile no for fb user
//     * <p>
//     * This method used to get the mobile no for fb user
//     * </p>
//     *
//     * @param String mobile_number
//     */
//    public void GetPhoneNumber() {
//        // TODO Auto-generated method stub
//        final View view = View.inflate(RegisterNewAct.this, R.layout.forgot_popupnew, null);
//        mDialog = new Dialog(RegisterNewAct.this, R.style.NewDialog);
//        mDialog.setContentView(view);
//        mDialog.setCancelable(false);
//        mDialog.show();
//        final TextView t = (TextView) mDialog.findViewById(R.id.f_textview);
//        final Spinner ftmobilecodespn = (Spinner) mDialog.findViewById(R.id.mobilecodespn);
//        Point pointSize = new Point();
//        getWindowManager().getDefaultDisplay().getSize(pointSize);
//        ftmobilecodespn.setDropDownWidth(pointSize.x);
//        t.setText(NC.getResources().getString(R.string.userphno));
//        ftmobilecodespn.setVisibility(View.VISIBLE);
//        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
//        mail.setInputType(InputType.TYPE_CLASS_NUMBER);
////        mDialog.findViewById(R.id.for_sep).setVisibility(View.VISIBLE);
//        FontHelper.applyFont(RegisterNewAct.this, mail);
//        final TextView OK = (TextView) mDialog.findViewById(R.id.okbtn);
//        final TextView Cancel = (TextView) mDialog.findViewById(R.id.cancelbtn);
//       // Cancel.setVisibility(View.GONE);
//        ftmobilecodespn.setAdapter(mobileno_adapter);
//        try {
//            String mobcodeary[];
//            for (int i = 0; i < country.size(); i++) {
//                if (SessionSave.getSession("country_name", RegisterNewAct.this).equalsIgnoreCase(country.get(i).toString())) {
//                    //findPosition
//                    positionFirst = i;
//                    ftmobilecodespn.setSelection(positionFirst);
//                    System.out.println("positionFirst " + positionFirst);
//                    break;
//                }
//                // Setting Default as Kuwait if not get Country
//                else {
//                    positionFirst = 93;
//                }
//            }
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
////        Point pointSize = new Point();
////        getWindowManager().getDefaultDisplay().getSize(pointSize);
////        ftmobilecodespn.setDropDownWidth(pointSize.x);
//        ftmobilecodespn.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                mobilenoary = parent.getItemAtPosition(arg2).toString().split("\\s+");
//                TextView txt = ((TextView) view);
//                txt.setTextColor(getResources().getColor(R.color.black));
//                txt.setGravity(Gravity.CENTER_VERTICAL);
//                txt.setText("" + mobilenoary[0]);
//                countrycode = mobilenoary[0];
//                int width=0;
//
//                if(mobilenoary[0].length() >4)
//                    width=mobilenoary[0].length()*16;
//                else
//                    width=mobilenoary[0].length()*25;
//
//                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
//                ftmobilecodespn.setLayoutParams(params);
//                System.out.println("countrycode-->" + countrycode);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//            }
//        });
//        OK.setOnClickListener(new OnClickListener() {
//            private String Phone;
//
//            @Override
//            public void onClick(final View arg0) {
//                // TODO Auto-generated method stub
//                try {
//                    Phone = mail.getText().toString();
//                    if (validations(ValidateAction.isValueNULL, RegisterNewAct.this, Phone)) {
//                        JSONObject j = new JSONObject();
//                        j.put("fbemail", SessionSave.getSession("Email", RegisterNewAct.this));
//                        j.put("mobile", Phone);
//                        j.put("country_code", countrycode);
//                        final String url = "type=passenger_mobile_otp";
//                        new MobilenumberUpdate(url, j);
//                        mail.setText("");
//                        mDialog.dismiss();
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Cancel.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(final View arg0) {
//                LoginManager.getInstance().logOut();
//                mDialog.dismiss();
//            }
//        });
//    }

    /**
     * This method used to get the mobile no for fb user
     * <p>
     * This method used to get the mobile no for fb user
     * </p>
     *
     * @param
     */
    public void GetPhoneNumber() {
        // TODO Auto-generated method stub
        final View view = View.inflate(RegisterNewAct.this, R.layout.forgot_popupnew, null);
        final Dialog mDialog = new Dialog(RegisterNewAct.this, R.style.NewDialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.show();
        final TextView t = (TextView) mDialog.findViewById(R.id.f_textview);
        final Spinner ftmobilecodespn = (Spinner) mDialog.findViewById(R.id.mobilecodespn);
        ftmobilecodespn.setVisibility(View.VISIBLE);
        Point pointSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(pointSize);
        ftmobilecodespn.setDropDownWidth(pointSize.x);
        t.setText(NC.getResources().getString(R.string.userphno));
        final TextView ftspinner_value = (TextView) mDialog.findViewById(R.id.ftspinner_value);
        ftspinner_value.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ftmobilecodespn.performClick();
            }
        });


        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
//        mDialog.findViewById(R.id.for_sep).setVisibility(View.VISIBLE);
        mail.setInputType(InputType.TYPE_CLASS_NUMBER);
        final TextView OK = (TextView) mDialog.findViewById(R.id.okbtn);
        final TextView Cancel = (TextView) mDialog.findViewById(R.id.cancelbtn);
        // Cancel.setVisibility(View.GONE);
        ftmobilecodespn.setVisibility(View.VISIBLE);
        ftmobilecodespn.setAdapter(mobileno_adapter);
        try {
            String mobcodeary[];
            for (int i = 0; i < country.size(); i++) {
                if (SessionSave.getSession("country_code", RegisterNewAct.this).trim().equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(i).toString().trim())).trim().replaceAll("\\s", ""))) {
                    //findPosition
                    positionFirst = i;
                    ftmobilecodespn.setSelection(positionFirst);

                    break;
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
//        Point pointSize = new Point();
//        getWindowManager().getDefaultDisplay().getSize(pointSize);
//        ftmobilecodespn.setDropDownWidth(pointSize.x);
        ftmobilecodespn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                mobilenoary = parent.getItemAtPosition(arg2).toString().split("\\s+");
                TextView txt = ((TextView) view);
                txt.setTextColor(getResources().getColor(R.color.black));
                txt.setGravity(Gravity.CENTER_VERTICAL);
                txt.setText("" + mobilenoary[0]);
                ftspinner_value.setText("" + mobilenoary[0]);
                countrycode = mobilenoary[0];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        OK.setOnClickListener(new OnClickListener() {
            private String Phone;

            @Override
            public void onClick(final View arg0) {
                // TODO Auto-generated method stub
                try {
                    Phone = mail.getText().toString();
                    if (validations(ValidateAction.isValidphone, RegisterNewAct.this, Phone)) {
                        JSONObject j = new JSONObject();
                        j.put("fbemail", SessionSave.getSession("Email", RegisterNewAct.this));
                        j.put("mobile", Phone);
                        j.put("country_code", countrycode);
                        final String url = "type=passenger_mobile_otp";
                        new MobilenumberUpdate(url, j);
                        mail.setText("");
                        //Karthick Edit
                        mDialog.show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });

        Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View arg0) {

                LoginManager.getInstance().logOut();
                mDialog.dismiss();

            }
        });
    }

    /**
     * This class used to update the mobileno to the api
     * <p>
     * This class used to update the mobileno to the api
     * </p>
     */
    private class MobilenumberUpdate implements APIResult {
        public MobilenumberUpdate(final String url, final JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(RegisterNewAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
//                        alert_view(RegisterNewAct.this, "Success", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), RegisterNewAct.this);
                        final Intent i = new Intent(RegisterNewAct.this, CardRegisterAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else {
                        // LoginManager.getInstance().logOut();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    ShowToast(RegisterNewAct.this, json.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        //alert_view(RegisterNewAct.this, "Success", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void successalert(final Context mContext, String title, String message, String success_txt, String failure_txt) {

        final Dialog mDialog;
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            mDialog = new Dialog(mContext, R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            FontHelper.applyFont(mContext, mDialog.findViewById(R.id.alert_id));
            mDialog.show();
            final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mDialog.dismiss();
                    showLoading(mContext);
                    Intent intent = new Intent(getApplicationContext(), MainHomeFragmentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
