package com.cabipassenger;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.cabipassenger.util.ShowToast;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * this class is used to login user
 * <p>
 * it extends Mainactivity to access properties of main activity
 * <p>
 * <p>
 * </p>
 *
 * @author developer
 */
public class LoginAct extends MainActivity {
    // Class members declarations.
    private EditText EmailEdt;
    private EditText PasswordEdt;
    private TextView ForgotTxt;
    private LinearLayout CancelBtn;
    private TextView hidePwd;
    private TextView HeadTitle;
    private String phone;
    private String password;
    private TaxiUtil mUtil;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.cabi:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private String Access_token;
    private LoginButton fbloginBtn;
    private ImageView showpwdImg;
    private Spinner mobilecodespn;
    private LinearLayout DoneBtn;
    private ArrayAdapter<String> mobileno_adapter;
    private String countrycode = "";
    private String[] mobilenoary;
    private String fbaccesstoken = "";
    private String fbuserid = "";
    private String[] countryCodeArray;
    private int positionFirst;
    private TextView backarrow;

    private List<String> val = new ArrayList<String>();
    private List<String> code = new ArrayList<String>();
    private List<String> country = new ArrayList<String>();
    private List<String> arryaCountryList = new ArrayList<String>();
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

            new AlertDialog.Builder(LoginAct.this).setTitle(title).setMessage(alertMessage).setPositiveButton(NC.getString(R.string.ok), null).show();
        }
    };
    private LinearLayout signuplay;
    private String fbname = "";
    private String lname = "";
    private TextView Spinnertext;


    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    // Set the layout to activity.
    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        setLocale();
        return R.layout.loginactlay;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    // Initialize the views on layout
    @SuppressLint("NewApi")
    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build();
//        StrictMode.setThreadPolicy(policy);

        if (!SessionSave.getSession("facebook_key", LoginAct.this).equals(""))
            FacebookSdk.setApplicationId(SessionSave.getSession("facebook_key", LoginAct.this));
        else
            FacebookSdk.setApplicationId("1659604664078736");
        Colorchange.ChangeColor((ViewGroup) findViewById(R.id.login_contain), LoginAct.this);
        FontHelper.applyFont(this, findViewById(R.id.login_contain));
        TaxiUtil.mDevice_id = Settings.Secure.getString(LoginAct.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TaxiUtil.mDevice_id.equals("")) {
            SessionSave.saveSession("mDevice_id", TaxiUtil.mDevice_id, LoginAct.this);
        }
        //  Glide.with(this).load(SessionSave.getSession("image_path", this) + "signInLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.body_iv));
        callbackManager = CallbackManager.Factory.create();
        fbloginBtn = (LoginButton) findViewById(R.id.fbloginBtn);
        fbloginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

                new AlertDialog.Builder(LoginAct.this).setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted).setPositiveButton(NC.getString(R.string.ok), null).show();
            }
        });
        fbloginBtn.setReadPermissions("email");
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
        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        // Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
        TaxiUtil.mActivitylist.add(this);
        mUtil = new TaxiUtil(this);
        TaxiUtil.current_act = "LoginAct";
        CancelBtn = (LinearLayout) findViewById(R.id.leftIconTxt);
        DoneBtn = (LinearLayout) findViewById(R.id.signinlay);
        signuplay = (LinearLayout) findViewById(R.id.signuplay);
        findViewById(R.id.headlayout).setVisibility(View.VISIBLE);
        findViewById(R.id.leftIconTxt).setVisibility(View.VISIBLE);

        //signinlay=(LinearLayout)findViewById(R.id.signinlay);
        ForgotTxt = (TextView) findViewById(R.id.forgotpswdTxt);
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        Spinnertext = (TextView) findViewById(R.id.spinner_value);
        EmailEdt = (EditText) findViewById(R.id.emailEdt);
        PasswordEdt = (EditText) findViewById(R.id.passwordEdt);
        PasswordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    try {

                        phone = EmailEdt.getText().toString().trim();
                        password = PasswordEdt.getText().toString().trim();
                        if (validations(ValidateAction.isValueNULL, LoginAct.this, phone))
                            if (validations(ValidateAction.isValidPassword, LoginAct.this, password)) {
                                DoneBtn.setEnabled(false);
                                JSONObject json = new JSONObject();
                                json.put("phone", phone);
                                json.put("country_code", countrycode);

                                json.put("password", Uri.encode(password));
                                String token = FirebaseInstanceId.getInstance().getToken();


                                json.put("deviceid", "" + SessionSave.getSession("mDevice_id", LoginAct.this));
                                json.put("devicetoken", token);
                                json.put("devicetype", "1");
                                new SignIn("type=passenger_login", json);
                            }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        ShowToast(LoginAct.this, "Invaid format");
                    }
                }
                return false;
            }
        });
        hidePwd = (TextView) findViewById(R.id.hidePwd);
        HeadTitle.setText(NC.getResources().getString(R.string.signin));
        //CancelBtn.setVisibility(View.INVISIBLE);
        showpwdImg = (ImageView) findViewById(R.id.showpwd);


        backarrow = (TextView) findViewById(R.id.leftIcon);
        backarrow.setBackgroundResource(R.drawable.back);
        backarrow.setVisibility(View.VISIBLE);


        backarrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserHomeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserHomeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        Spinnertext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mobilecodespn.performClick();
            }
        });

        signuplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SessionSave.saveSession("IsOTPSend", "", LoginAct.this);
                SessionSave.saveSession("f_name", "", LoginAct.this);
                SessionSave.saveSession("l_name", "", LoginAct.this);
                SessionSave.saveSession("e_mail", "", LoginAct.this);
                SessionSave.saveSession("m_no", "", LoginAct.this);
                SessionSave.saveSession("p_wd", "", LoginAct.this);
                SessionSave.saveSession("cp_wd", "", LoginAct.this);
                SessionSave.saveSession("ref_txt", "", LoginAct.this);

                startActivity(new Intent(LoginAct.this, RegisterNewAct.class));
                //finish();
            }
        });

        /**
         * @author developer
         *         <p>
         *         This section help to get the password.
         *         </p>
         *
         * @param mobilenumber
         *
         *            API used:- type=forgot_password
         */
        ForgotTxt.setOnClickListener(new OnClickListener() {
            private Dialog mDialog;

            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {
                    final View view = View.inflate(LoginAct.this, R.layout.forgot_popupnew, null);
                    mDialog = new Dialog(LoginAct.this, R.style.dialogwinddow);
                    mDialog.setContentView(view);
                    mDialog.setCancelable(true);
                    if (!mDialog.isShowing())
                        mDialog.show();
                    FontHelper.applyFont(LoginAct.this, mDialog.findViewById(R.id.inner_content));
                    Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.inner_content), LoginAct.this);
                    mDialog.findViewById(R.id.f_textview);
                    final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
                    mail.setInputType(InputType.TYPE_CLASS_NUMBER);
                    final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
                    final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
                    final Spinner mobilespn = (Spinner) mDialog.findViewById(R.id.mobilecodespn);
                    final TextView ftspinner_value = (TextView) mDialog.findViewById(R.id.ftspinner_value);
                    ftspinner_value.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mobilespn.performClick();
                        }
                    });


                    mobilespn.setAdapter(mobileno_adapter);
                    try {

                        for (int i = 0; i < country.size(); i++) {

                            // if (String.format(Locale.UK, String.valueOf(f_countrycode)).equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(j).toString().trim())).trim().replaceAll("\\s",""))) {
                            if ((SessionSave.getSession("country_code", LoginAct.this)).trim().equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(i).toString().trim())).trim().replaceAll("\\s", ""))) {
                                //findPosition

                                positionFirst = i;
                                mobilespn.setSelection(positionFirst);

                                break;
                            }
                            // Setting Default as India if not get Country
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
                    mobilespn.setDropDownWidth(pointSize.x);
                    mobilespn.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            mobilenoary = parent.getItemAtPosition(arg2).toString().split("\\s+");
                            TextView txt = ((TextView) view);
                            txt.setTextColor(CL.getResources().getColor(LoginAct.this, R.color.black));
                            txt.setGravity(Gravity.CENTER_VERTICAL);
                            txt.setText("" + mobilenoary[0]);
                            ftspinner_value.setText("" + mobilenoary[0]);
                            txt.setBackgroundResource(R.color.white);
                            countrycode = mobilenoary[0];

                            int width = 0;

                            if (mobilenoary[0].length() > 4)
                                width = 100;
                            else if (mobilenoary[0].length() == 4)
                                width = 75;
                            else
                                width = 60;


//                            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(dpToPx(width), LinearLayout.LayoutParams.WRAP_CONTENT);
//                            mobilespn.setLayoutParams(params);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
                    OK.setOnClickListener(new OnClickListener() {
                        private String mobilenumber;

                        @Override
                        public void onClick(final View v) {
                            // TODO Auto-generated method stub
                            try {
                                mobilenumber = mail.getText().toString();
                                if (validations(ValidateAction.isValueNULL, LoginAct.this, mobilenumber)) {
                                    JSONObject j = new JSONObject();
                                    j.put("phone_no", mobilenumber);
                                    j.put("user_type", "P");
                                    j.put("country_code", countrycode);
                                    final String url = "type=forgot_password";
                                    new ForgotPassword(url, j);
                                    mail.setText("");
                                    mDialog.dismiss();
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    });
                    Cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            // TODO Auto-generated method stub

                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }


                            mDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        /**
         * @author developer
         *         <p>
         *         This section helps to login to app.
         *         </p>
         * @param string
         *
         *            mobile number, string password
         *
         *            API used:- type=passenger_login
         */
        DoneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(LoginAct.this);
                    Bundle params = new Bundle();
                    params.putString("user", phone);
                    params.putString(FirebaseAnalytics.Param.ITEM_NAME, phone);
                    params.putString("type", "passenger");
                    mFirebaseAnalytics.logEvent("Login_clicked_by", params);

                    phone = EmailEdt.getText().toString().trim();
                    password = PasswordEdt.getText().toString().trim();
                    if (validations(ValidateAction.isValueNULL, LoginAct.this, phone))
                        if (validations(ValidateAction.isValidPassword, LoginAct.this, password)) {
                            DoneBtn.setEnabled(false);
                            JSONObject json = new JSONObject();
                            json.put("phone", phone);
                            json.put("country_code", countrycode);

                            json.put("password", Uri.encode(password));
                            json.put("deviceid", "" + SessionSave.getSession("mDevice_id", LoginAct.this));
                            json.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
                            json.put("devicetype", "1");
                            new SignIn("type=passenger_login", json);
                        }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    ShowToast(LoginAct.this, "Invaid format");
                }
//                try {
//
//                    phone = EmailEdt.getText().toString().trim();
//                    password = PasswordEdt.getText().toString().trim();
//                    if (validations(ValidateAction.isValueNULL, LoginAct.this, phone))
//                        if (validations(ValidateAction.isValidPassword, LoginAct.this, password)) {
//                            DoneBtn.setEnabled(false);
//                            JSONObject json = new JSONObject();
//                            json.put("phone", phone);
//                            json.put("country_code", countrycode);
//
//                            json.put("password", Uri.encode(password));
//                            json.put("deviceid", "" + SessionSave.getSession("mDevice_id", LoginAct.this));
//                            json.put("devicetoken", SessionSave.getSession("device_token", LoginAct.this));
//                            json.put("devicetype", "1");
//                            new SignIn("type=passenger_login", json);
//                        }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                    ShowToast(LoginAct.this, "Invaid format");
//                }
            }
        });




       /* newly add for password hide and show*/

        hidePwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hidePwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(LoginAct.this, PasswordEdt);

                } else if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                    PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hidePwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(LoginAct.this, PasswordEdt);

                }
            }
        });

        PasswordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (PasswordEdt.getText().toString().length() > 0) {
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


        PasswordEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {

                    if (PasswordEdt.getText().toString().length() > 0)
                        hidePwd.setVisibility(View.VISIBLE);
                    else
                        hidePwd.setVisibility(View.INVISIBLE);


                } else {
                    hidePwd.setVisibility(View.GONE);
                    PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    FontHelper.applyFont(LoginAct.this, PasswordEdt);
                    if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        // PasswordEdt.setText("");
                        hidePwd.setText(NC.getResources().getString(R.string.show));
                    }
                }
            }

        });

//        PasswordEdt.addTextChangedListener(new TextWatcher() {
//
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        showpwdImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT);
                FontHelper.applyFont(LoginAct.this, PasswordEdt);
            }
        });
        countryCodeArray = getResources().getStringArray(R.array.country_code);


        for (String s : countryCodeArray) {

            val.add(s);

        }


        String coutryList;

        for (int i = 0; i < val.size(); i++) {
            //countryCodeArray = val.get(i).toString().replaceAll("\\s", "").split("&");

            countryCodeArray = val.get(i).toString().trim().split("&");
            code.add(countryCodeArray[0]);
            country.add(countryCodeArray[1]);


            coutryList = Autocortctryname.Autocortct(countryCodeArray[0], countryCodeArray[1]);

            arryaCountryList.add(coutryList);

        }


        mobilecodespn = (Spinner) findViewById(R.id.mobilecodespn);
        mobileno_adapter = new FontHelper.MySpinnerAdapter(this, R.layout.monthitem_spinnerlay, arryaCountryList);
        mobileno_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mobilecodespn.setAdapter(mobileno_adapter);

        for (int i = 0; i < country.size(); i++) {
//            Log.e("country_codecheck",SessionSave.getSession("country_code", LoginAct.this)+"code"+String.format(Locale.UK, String.valueOf(code.get(i).toString().trim())).trim());
            if ((SessionSave.getSession("country_code", LoginAct.this)).trim().equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(i).toString().trim())).trim().replaceAll("\\s", ""))) {
                //findPosition
//                System.out.println("iffffffffffffffffffffffffffff");
                positionFirst = i;
                mobilecodespn.setSelection(positionFirst);

                break;
            }
            // Setting Default as India if not get Country
            else {
//                System.out.println("elseeeeeeeeeeeeeeeeeeeeeeeee");
                positionFirst = 966;
            }
        }

        Point pointSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(pointSize);
        mobilecodespn.setDropDownWidth(pointSize.x);
        mobilecodespn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView txt = ((TextView) view);
                if (txt != null) {
                    mobilenoary = parent.getItemAtPosition(arg2).toString().split("\\s+");

                    txt.setTextColor(getResources().getColor(R.color.black));
                    txt.setGravity(Gravity.CENTER_VERTICAL);
                    txt.setText("" + mobilenoary[0]);
                    countrycode = mobilenoary[0];
                    Spinnertext.setText("" + mobilenoary[0]);

                    if (Spinnertext.getText().toString().equals("+966")) {
                        EmailEdt.setText("5");
                        EmailEdt.setSelection(EmailEdt.getText().toString().length());
                    }

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
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
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
                        // ShowToast(LoginAct.this, NC.getResources().getString(R.string.try_again));
                    }
                }
            }).executeAsync();
        } else {
            // profilePictureView.setProfileId(null);
            // greeting.setText(null);
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
                        //ShowToast(LoginAct.this, NC.getResources().getString(R.string.try_again));
                    }
                }
            }).executeAsync();
        }
    }

    /**
     * this method is used to call the fb api call
     *
     * @param emailid parameter which passes by getting from user if it <br>
     *                not exist in fb sdk</br>
     * @param id      this id get from fb and passes as parameter
     */

    void callfb(String id, String emailid, String firstname, String lastname) {
        try {
            JSONObject j = new JSONObject();
            j.put("accesstoken", Access_token);
            j.put("userid", id);
            j.put("fname", "" + fbname);
            j.put("lname", "" + lname);
            j.put("fbemail", emailid);
            j.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
            j.put("deviceid", "" + SessionSave.getSession("mDevice_id", LoginAct.this));
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

    /**
     * This class used to remember password
     * <p>
     * This class used to remember password
     * </p>
     *
     * @author developer
     */
    private class ForgotPassword implements APIResult {
        public ForgotPassword(final String url, final JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(LoginAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub

            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    //   if (json.getInt("status") == 1)
                    alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                   else
//                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(LoginAct.this, getString(R.string.server_con_error));
                        }
                    });
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
            new APIService_Retrofit_JSON(LoginAct.this, this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", LoginAct.this) + "&" + string).execute();
//            new APIService_Volley_JSON(LoginAct.this, this, data, false).execute(string);
            fbData = data;
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);

                    if (json.getInt("status") == 1) {
                        //  successalert(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        tellfrdMsg = detail.getString("telltofriend_message");
                        id = detail.getString("id");
                        photo = detail.getString("profile_image");
                        SessionSave.saveSession("ProfileName", detail.getString("name"), LoginAct.this);
                        SessionSave.saveSession("Email", email, LoginAct.this);
                        SessionSave.saveSession("Id", id, LoginAct.this);
                        SessionSave.saveSession("ProfileImage", photo, LoginAct.this);
                        SessionSave.saveSession("RefCode", json.getJSONObject("detail").getString("referral_code"), LoginAct.this);
                        SessionSave.saveSession("RefAmount", json.getJSONObject("detail").getString("referral_code_amount"), LoginAct.this);
                        SessionSave.saveSession("Register", "", LoginAct.this);
                        SessionSave.saveSession("Tellfrdmsg", tellfrdMsg, LoginAct.this);
                        SessionSave.saveSession("About", json.getJSONObject("detail").getString("aboutpage_description"), LoginAct.this);
                        SessionSave.saveSession("Currency", json.getJSONObject("detail").getString("site_currency") + " ", LoginAct.this);
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), LoginAct.this);
                        if (json.getJSONObject("detail").getString("split_fare").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isSplitOn, true, LoginAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isSplitOn, false, LoginAct.this);
                        if (json.getJSONObject("detail").getString("favourite_driver").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, LoginAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, LoginAct.this);
                        if (json.getJSONObject("detail").getString("skip_favourite").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, true, LoginAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, false, LoginAct.this);
                        // mDialog.dismiss();
                        //showLoading(mContext);
                        Intent intent = new Intent(getApplicationContext(), MainHomeFragmentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //  startActivity(i);
                        UserHomeAct.mAct.finish();
                        finish();
                        // SessionSave.saveSession("model_details", json.getJSONObject("detail").getString("model_details"), LoginAct.this);
                    } else if (json.getInt("status") == -10) {
                        ShowToast.center(LoginAct.this, json.getString("message"));

                    } else if (json.getInt("status") == 2) {
                        // alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("Register", "2", LoginAct.this);
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, LoginAct.this);
                        //                        final Intent i = new Intent(LoginAct.this, RegisterNewAct.class);
                        //                        Bundle detail = new Bundle();
                        //                        detail.putString("Message", json.getString("message"));
                        //                        detail.putString("Email", fbData.getString("fbemail"));
                        //                        detail.putString("FName", fbData.getString("fname"));
                        //                        detail.putString("LName", fbData.getString("lname"));
                        //                        detail.putString("FbaccToken", fbData.getString("accesstoken"));
                        //                        detail.putString("Fbuserid", fbData.getString("userid"));
                        //                        System.err.println("fb email" + fbData.getString("fbemail"));
                        //                        i.putExtras(detail);
                        //                        startActivity(i);
                        //                        finish();
                        GetPhoneNumber();
                    } else if (json.getInt("status") == 3) {
                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, LoginAct.this);
                        SessionSave.saveSession("Register", "1", LoginAct.this);

                        SessionSave.saveSession("IsOTPSend", "", LoginAct.this);
                        SessionSave.saveSession("f_name", "", LoginAct.this);
                        SessionSave.saveSession("l_name", "", LoginAct.this);
                        SessionSave.saveSession("e_mail", "", LoginAct.this);
                        SessionSave.saveSession("m_no", "", LoginAct.this);
                        SessionSave.saveSession("p_wd", "", LoginAct.this);
                        SessionSave.saveSession("cp_wd", "", LoginAct.this);
                        SessionSave.saveSession("ref_txt", "", LoginAct.this);

                        final Intent i = new Intent(LoginAct.this, RegisterNewAct.class);
                        Bundle data = new Bundle();
                        data.putString("Message", json.getString("message"));
                        i.putExtras(data);
                        startActivity(i);
                    } else if (json.getInt("status") == -2) {
                        // alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");

                        final JSONObject detail = json.getJSONObject("detail");
                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, LoginAct.this);
                        SessionSave.saveSession("Register", "1", LoginAct.this);
                        //   SessionSave.saveSession("m_no", EmailEdt.getText().toString().trim(), LoginAct.this);

                        final Intent i = new Intent(LoginAct.this, VerificationAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == 4) {

//                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        final JSONObject detail = json.getJSONObject("detail");

                        email = detail.getString("email");
                        SessionSave.saveSession("Email", email, LoginAct.this);
                        SessionSave.saveSession("Register", "2", LoginAct.this);
                        final Intent i = new Intent(LoginAct.this, CardRegisterAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();

                        //   GetPhoneNumber();
                    } else if (json.getInt("status") == 10) {

//                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                        final JSONObject detail = json.getJSONObject("detail");
//                        Log.v(detail + "", "Our Details");
//                        email = detail.getString("email");
//                        SessionSave.saveSession("Email", email, LoginAct.this);
//                        SessionSave.saveSession("Register", "2", LoginAct.this);
//                        final Intent i = new Intent(LoginAct.this, CardRegisterAct.class);
//                        i.putExtra("Message", json.getString("message"));
//                        startActivity(i);
//                        finish();
                        getEmailId(fbuserid, fbname, "");
                        //   GetPhoneNumber();
                    } else {
                        LoginManager.getInstance().logOut();
                        GetPhoneNumber();
                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(LoginAct.this, getString(R.string.server_con_error));
                        }
                    });
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ShowToast(LoginAct.this, getString(R.string.try_again));
            }
        }
    }

    /**
     * This class used to login in app
     * <p>
     * This class used to login in app
     * </p>
     *
     * @author developer
     */
    private class SignIn implements APIResult {
        public SignIn(final String url, JSONObject j) {
            // TODO Auto-generated constructor stub
            // new APIService_Volley_JSON(LoginAct.this, this, j, false).execute(url);
            new APIService_Retrofit_JSON(LoginAct.this, this, j, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", LoginAct.this) + "&" + url).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                DoneBtn.setEnabled(true);
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        //    successalert(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), LoginAct.this);
                        SessionSave.saveSession("Id", json.getJSONObject("detail").getString("id"), LoginAct.this);
                        SessionSave.saveSession("Tellfrdmsg", json.getJSONObject("detail").getString("telltofriend_message"), LoginAct.this);
                        SessionSave.saveSession("Phone", json.getJSONObject("detail").getString("phone"), LoginAct.this);
                        SessionSave.saveSession("ProfileImage", json.getJSONObject("detail").getString("profile_image"), LoginAct.this);
                        SessionSave.saveSession("ProfileName", json.getJSONObject("detail").getString("name"), LoginAct.this);
                        SessionSave.saveSession("About", json.getJSONObject("detail").getString("aboutpage_description"), LoginAct.this);
                        SessionSave.saveSession("Currency", json.getJSONObject("detail").getString("site_currency") + " ", LoginAct.this);
                        SessionSave.saveSession("RefCode", json.getJSONObject("detail").getString("referral_code"), LoginAct.this);
                        SessionSave.saveSession("RefAmount", json.getJSONObject("detail").getString("referral_code_amount"), LoginAct.this);
                        SessionSave.saveSession("Register", "", LoginAct.this);
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), LoginAct.this);

                        SessionSave.saveSession("CountyCode", json.getJSONObject("detail").getString("country_code"), LoginAct.this);


                        if (json.getJSONObject("detail").getString("split_fare").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isSplitOn, true, LoginAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isSplitOn, false, LoginAct.this);
                        if (json.getJSONObject("detail").getString("favourite_driver").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, LoginAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, LoginAct.this);
                        if (json.getJSONObject("detail").getString("skip_favourite").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, true, LoginAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, false, LoginAct.this);
                        //   SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, LoginAct.this);

                        // SessionSave.saveSession(TaxiUtil.isSplitOn, json.getJSONObject("detail").getString("split_fare"), LoginAct.this);


//                        mDialog.dismiss();
//                        showLoading(mContext);
                        Intent intent = new Intent(getApplicationContext(), MainHomeFragmentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //  startActivity(i);
                        if (UserHomeAct.mAct != null)
                            UserHomeAct.mAct.finish();
                        finish();

                        // SessionSave.saveSession("model_details", json.getJSONObject("detail").getString("model_details"), LoginAct.this);
                    } else if (json.getInt("status") == -10) {
                        ShowToast.center(LoginAct.this, json.getString("message"));

                    } else if (json.getInt("status") == -2) {
//                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), LoginAct.this);
                        SessionSave.saveSession("Phone", json.getJSONObject("detail").getString("phone"), LoginAct.this);
                        SessionSave.saveSession("Register", "1", LoginAct.this);
                        SessionSave.saveSession("m_no", EmailEdt.getText().toString().trim(), LoginAct.this);
                        final Intent i = new Intent(LoginAct.this, VerificationAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == -3) {
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), LoginAct.this);
                        SessionSave.saveSession("Register", "2", LoginAct.this);
                        final Intent i = new Intent(LoginAct.this, CardRegisterAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == -5) {
                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        DoneBtn.setEnabled(true);
                    } else if (json.getInt("status") == 4) {

                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        //Karthick Edit

                        DoneBtn.setEnabled(true);
                    } else {
                        alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        DoneBtn.setEnabled(true);
                    }
                } else {
                    DoneBtn.setEnabled(true);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(LoginAct.this, getString(R.string.server_con_error));
                        }
                    });
//                    alert_view(LoginAct.this, "" + NC.getResources().getString(R.string.message), "" + result, "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                DoneBtn.setEnabled(true);
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
                    //  startActivity(i);
                    UserHomeAct.mAct.finish();
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(this);
        super.onDestroy();
        profileTracker.stopTracking();
    }

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
        final View view = View.inflate(LoginAct.this, R.layout.forgot_popupnew, null);
        final Dialog mDialog = new Dialog(LoginAct.this, R.style.NewDialog);
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
                if (SessionSave.getSession("country_code", LoginAct.this).trim().equalsIgnoreCase(String.format(Locale.UK, String.valueOf(code.get(i).toString().trim())).trim().replaceAll("\\s", ""))) {
                    //findPosition
                    positionFirst = i;
                    ftmobilecodespn.setSelection(positionFirst);

                    break;
                }
                // Setting Default as India if not get Country
                else {
                    positionFirst = 966;
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
                    if (validations(ValidateAction.isValidphone, LoginAct.this, Phone)) {
                        JSONObject j = new JSONObject();
                        j.put("fbemail", SessionSave.getSession("Email", LoginAct.this));
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

    public void setEditTextMaxLength(final EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    public void getEmailId(final String id, final String firstname, final String lastname) {
        // TODO Auto-generated method stub

        final View view = View.inflate(LoginAct.this, R.layout.forgot_popup, null);
        final Dialog mDialog = new Dialog(LoginAct.this, R.style.NewDialog);
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
        //Cancel.setVisibility(View.GONE);

        Point pointSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(pointSize);

        OK.setOnClickListener(new OnClickListener() {
            private String emailid;

            @Override
            public void onClick(final View arg0) {
                // TODO Auto-generated method stub
                try {
                    emailid = mail.getText().toString();
                    if (validations(ValidateAction.isValidMail, LoginAct.this, emailid)) {
                        callfb(id, emailid, firstname, lastname);
                    } else {
                        Toast.makeText(LoginAct.this, NC.getResources().getString(R.string.enter_the_valid_email), Toast.LENGTH_SHORT).show();
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
            new APIService_Retrofit_JSON(LoginAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), LoginAct.this);
                        final Intent i = new Intent(LoginAct.this, CardRegisterAct.class);
                        i.putExtra("Message", json.getString("message"));
                        startActivity(i);
                        finish();
                    } else {
                        //LoginManager.getInstance().logOut();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    ShowToast(LoginAct.this, json.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        //alert_view(LoginAct.this, "Success", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(LoginAct.this, getString(R.string.server_con_error));
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {


        Intent intent = new Intent(getApplicationContext(), UserHomeAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
//        startActivity(new Intent(LoginAct.this, UserHomeAct.class));
    }
}