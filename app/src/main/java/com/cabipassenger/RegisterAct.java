package com.cabipassenger;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.FourDigitCardFormatWatcher;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * this class is used to register the payment card details of the user
 * @author developer
 * */
public class RegisterAct extends MainActivity {
    private static final int DATE_DIALOG_ID = 0;
    private LinearLayout CancelBtn;
    private TextView DoneBtn;
    private TextView HeadTitle;
    private TextView salutTxt;
    private ImageView barImg;
    private LinearLayout FirstsetpLay;
    private LinearLayout SecondsetpLay;
    private LinearLayout ThirdsetpLay;
    private EditText EmailEdt;
    private EditText MobileEdt;
    private EditText PasswordEdt;
    private EditText ConfrimPswdEdt;
    private EditText FirstnameEdt;
    private EditText LastnameEdt;
    private EditText OnetimePswdEdt;
    private EditText CardNoEdt;
    private TextView ExpiryEdt;
    private EditText CvvEdt;
    private EditText ReferalEdt;
    private final boolean t_status = true;
    private String email;
    private String mobile;
    private String password;
    private String confrimpswd;
    private String Firstname;
    private String Lastname;
    private String OnetimePswd;
    private String savecard = "1";
    private String CardNumber;
    private String Expiry;
    private String Cvv;
    private String ReferalNo;
    private int mMonth;
    private int mDay;
    private int mYear;
    private String Message;
    private TextView resendOTPTxt;
    private ImageView ProfileImg;
    private DisplayImageOptions options;
    private Uri imageUri;
    private Bitmap mBitmap;
    private String encodedImage = "";
    private Spinner Sal_Spn;
    private String Salutation = "";
    private boolean touched;
    private ArrayAdapter<String> adapter;
    private TextView SecureTxt;
    private TextView ReadtermTxt;
    private TextView paybyTxt;
    private TextView skipTxt;
    private CheckBox termsChk;
    private boolean Checked;
    private LinearLayout paybycashlay;
    private CheckBox Defaultcheck;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        setLocale();
        return R.layout.registerlay;
    }

    @SuppressLint("NewApi")
    @Override
    public void Initialize() {
        // include header lay variables
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)),RegisterAct.this);
        FontHelper.applyFont(this, findViewById(R.id.register_contain));
        TaxiUtil.current_act = "RegisterAct";
        TaxiUtil.mActivitylist.add(this);
        TaxiUtil.mDevice_id = Settings.Secure.getString(RegisterAct.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TaxiUtil.mDevice_id.equals("")) {
            SessionSave.saveSession("mDevice_id", TaxiUtil.mDevice_id, RegisterAct.this);
        }
        CancelBtn = (LinearLayout) findViewById(R.id.leftIconTxt);
        CancelBtn.setVisibility(View.INVISIBLE);
        DoneBtn = (TextView) findViewById(R.id.rightIconTxt);
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        DoneBtn.setText(NC.getResources().getString(R.string.done));
        DoneBtn.setBackground(getResources().getDrawable(R.drawable.draw_back_bg));
        HeadTitle.setText(NC.getResources().getString(R.string.create_account));
        barImg = (ImageView) findViewById(R.id.stepbarImg);
        FirstsetpLay = (LinearLayout) findViewById(R.id.firststeplay);
        SecondsetpLay = (LinearLayout) findViewById(R.id.secondsteplay);
        ThirdsetpLay = (LinearLayout) findViewById(R.id.thirdsteplay);
        EmailEdt = (EditText) findViewById(R.id.emailEdt);
        MobileEdt = (EditText) findViewById(R.id.mobileEdt);
        PasswordEdt = (EditText) findViewById(R.id.passwordEdt);
        ConfrimPswdEdt = (EditText) findViewById(R.id.confirmpswdEdt);
        ProfileImg = (ImageView) findViewById(R.id.profile_img);
        FirstnameEdt = (EditText) findViewById(R.id.firstEdt);
        LastnameEdt = (EditText) findViewById(R.id.lastEdt);
        OnetimePswdEdt = (EditText) findViewById(R.id.ont_passwordEdt);
        resendOTPTxt = (TextView) findViewById(R.id.resendOtpTxt);
        CardNoEdt = (EditText) findViewById(R.id.cardnoEdt);
        CardNoEdt.addTextChangedListener(new FourDigitCardFormatWatcher(RegisterAct.this));
        ExpiryEdt = (TextView) findViewById(R.id.expiryEdt);
        CvvEdt = (EditText) findViewById(R.id.cvvEdt);
        Defaultcheck = (CheckBox) findViewById(R.id.reg_checkBox1);
        Defaultcheck.setVisibility(View.GONE);
        ReferalEdt = (EditText) findViewById(R.id.referalEdt);
        salutTxt = (TextView) findViewById(R.id.salutTxt);
        Sal_Spn = (Spinner) findViewById(R.id.salSpn);
        SecureTxt = (TextView) findViewById(R.id.secureTxt);
        ReadtermTxt = (TextView) findViewById(R.id.readtermTxt);
        paybyTxt = (TextView) findViewById(R.id.paybyTxt);
        skipTxt = (TextView) findViewById(R.id.skipTxt);
        termsChk = (CheckBox) findViewById(R.id.termscheck);
        paybycashlay = (LinearLayout) findViewById(R.id.paybycashlay);
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        paybycashlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                if (!Checked)
                    alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.agree_the_terms_and_condition), "" + NC.getResources().getString(R.string.ok), "");
                else {
                    try {
                        savecard = "0";
                        CardNumber = "";
                        Cvv = "";
                        mMonth = 0;
                        mYear = 0;
                        JSONObject j = new JSONObject();
                        j.put("email", SessionSave.getSession("Email", RegisterAct.this));
                        j.put("creditcard_no", CardNumber);
                        j.put("expdatemonth", mMonth);
                        j.put("expdateyear", mYear);
                        j.put("creditcard_cvv", Cvv);
                        j.put("savecard", savecard);
                        final String url = "type=passenger_card_details";
                        new SignUp2(url, j);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            }
        });
        ReadtermTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {
                    JSONObject j = new JSONObject();
                    j.put("pagename", "termsconditions");
                    j.put("device_type", "1");
                    new ShowWebpage("type=dynamic_page", j);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        adapter = new ArrayAdapter<String>(RegisterAct.this, R.layout.spinneritem_lay, getResources().getStringArray(R.array.sal_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sal_Spn.setAdapter(adapter);
        Sal_Spn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                // TODO Auto-generated method stub
                touched = true;
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        Sal_Spn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View v, final int pos, final long arg3) {
                // TODO Auto-generated method stub
                final TextView txt = (TextView) v;
                if (!touched)
                    txt.setText(NC.getResources().getString(R.string.plzselect));
                Salutation = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> arg0) {
            }
        });
        termsChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                Checked = buttonView.isChecked();
            }
        });
        options = new DisplayImageOptions.Builder().showImageOnLoading(null) // resource
                // or
                // drawable
                .showImageForEmptyUri(null) // resource or drawable
                .showImageOnFail(null) // resource or drawable
                .resetViewBeforeLoading(false) // default
                .delayBeforeLoading(1000).cacheInMemory(true) // default
                .cacheOnDisc(true) // default
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) // default
                .bitmapConfig(Bitmap.Config.RGB_565) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                        // .showImageOnLoading(R.drawable.ic_stub)
                .build();
        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
        ExpiryEdt.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);
            }
        });
        if (SessionSave.getSession("Register", RegisterAct.this).equals("1")) {
            DoneBtn.setText(NC.getResources().getString(R.string.done));
            barImg.setImageResource(R.drawable.progress_2);
            FirstsetpLay.setVisibility(View.GONE);
            SecondsetpLay.setVisibility(View.VISIBLE);
            ThirdsetpLay.setVisibility(View.GONE);
        } else if (SessionSave.getSession("Register", RegisterAct.this).equals("2")) {
            DoneBtn.setText(NC.getResources().getString(R.string.done));
            barImg.setImageResource(R.drawable.progress_3);
            FirstsetpLay.setVisibility(View.GONE);
            SecondsetpLay.setVisibility(View.GONE);
            ThirdsetpLay.setVisibility(View.VISIBLE);
            if (SessionSave.getSession("skip_credit", RegisterAct.this).equals("1")) {
                paybycashlay.setVisibility(View.VISIBLE);
            } else {
                paybycashlay.setVisibility(View.GONE);
            }
        } else {
            barImg.setImageResource(R.drawable.progress_1);
            FirstsetpLay.setVisibility(View.VISIBLE);
            SecondsetpLay.setVisibility(View.GONE);
            ThirdsetpLay.setVisibility(View.GONE);
        }
        ProfileImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {
                    new AlertDialog.Builder(RegisterAct.this).setTitle("" + NC.getResources().getString(R.string.profile_image)).setMessage("" + NC.getResources().getString(R.string.choose_an_image)).setCancelable(true).setPositiveButton("" + NC.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // TODO Auto-generated method stub
                            System.gc();
                            final Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 0);
                            dialog.cancel();
                        }
                    }).setNegativeButton("" + NC.getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            final File photo = new File(Environment.getExternalStorageDirectory() + "/iBookRide/img");
                            if (!photo.exists())
                                photo.mkdirs();
                            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                            imageUri = Uri.fromFile(mediaFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);
                        }
                    }).show();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        DoneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    if (SessionSave.getSession("Register", RegisterAct.this).equals("1")) {
                        DoneBtn.setText(NC.getResources().getString(R.string.done));
                        if (SecondsetpLay.isShown()) {
                            Firstname = FirstnameEdt.getText().toString().trim();
                            Lastname = LastnameEdt.getText().toString().trim();
                            OnetimePswd = OnetimePswdEdt.getText().toString().trim();
                            ReferalNo = ReferalEdt.getText().toString().trim();
                            if (validations(ValidateAction.isValidSalutation, RegisterAct.this, Salutation) && !touched)
                                alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.please_select_your_salutation), "" + NC.getResources().getString(R.string.ok), "");
                            else if (validations(ValidateAction.isValidFirstname, RegisterAct.this, Firstname))
                                if (validations(ValidateAction.isValidLastname, RegisterAct.this, Lastname)) {
                                    try {
                                        final String url = "type=passenger_personal_details";
                                        JSONObject j = new JSONObject();
                                        j.put("email", SessionSave.getSession("Email", RegisterAct.this));
                                        j.put("otp", OnetimePswd);
                                        j.put("salutation", Salutation);
                                        j.put("referral_code", ReferalNo);
                                        j.put("firstname", Firstname);
                                        j.put("lastname", Lastname);
                                        j.put("profile_image", encodedImage);
                                        new SignUp1(url, j);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                }
                        }
                        FirstsetpLay.setVisibility(View.GONE);
                        SecondsetpLay.setVisibility(View.VISIBLE);
                        ThirdsetpLay.setVisibility(View.GONE);
                    } else if (SessionSave.getSession("Register", RegisterAct.this).equals("2")) {
                        DoneBtn.setText(NC.getResources().getString(R.string.done));
                        if (ThirdsetpLay.isShown()) {
                            CardNumber = CardNoEdt.getText().toString().trim().replace("-", "");
                            Expiry = ExpiryEdt.getText().toString().trim();
                            Cvv = "";// CvvEdt.getText().toString().trim();
                            if (validations(ValidateAction.isValidCard, RegisterAct.this, CardNumber))
                                if (validations(ValidateAction.isValidExpiry, RegisterAct.this, Expiry))
                                    if (!Checked)
                                        alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.agree_the_terms_and_condition), "" + NC.getResources().getString(R.string.ok), "");
                                    else if (savecard.equalsIgnoreCase("0")) {
                                        CardNumber = "";
                                        Expiry = "";
                                        Cvv = "";
                                    } else {
                                        try {
                                            JSONObject j = new JSONObject();
                                            j.put("email", SessionSave.getSession("Email", RegisterAct.this));
                                            j.put("creditcard_no", CardNumber);
                                            j.put("expdatemonth", mMonth);
                                            j.put("expdateyear", mYear);
                                            j.put("creditcard_cvv", Cvv);
                                            j.put("savecard", savecard);
                                            final String url = "type=passenger_card_details";
                                            new SignUp2(url, j);
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            e.printStackTrace();
                                        }
                                    }
                        }
                        FirstsetpLay.setVisibility(View.GONE);
                        SecondsetpLay.setVisibility(View.GONE);
                        ThirdsetpLay.setVisibility(View.VISIBLE);
                        if (SessionSave.getSession("skip_credit", RegisterAct.this).equals("1")) {
                            paybycashlay.setVisibility(View.VISIBLE);
                        } else {
                            paybycashlay.setVisibility(View.GONE);
                        }
                    } else {
                        FirstsetpLay.setVisibility(View.VISIBLE);
                        SecondsetpLay.setVisibility(View.GONE);
                        ThirdsetpLay.setVisibility(View.GONE);
                        email = EmailEdt.getText().toString().trim();
                        mobile = MobileEdt.getText().toString().trim();
                        password = PasswordEdt.getText().toString().trim();
                        confrimpswd = ConfrimPswdEdt.getText().toString().trim();
                        if (validations(ValidateAction.isValidMail, RegisterAct.this, email))
                            if (validations(ValidateAction.isValueNULL, RegisterAct.this, mobile))
                                if (validations(ValidateAction.isValidPassword, RegisterAct.this, password))
                                    if (validations(ValidateAction.isValidConfirmPassword, RegisterAct.this, confrimpswd))
                                        if (!confrimpswd.equals(password))
                                            alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.confirmation_password_mismatch_with_password), "" + NC.getResources().getString(R.string.ok), "");
                                        else {
                                            try {
                                                JSONObject j = new JSONObject();
                                                j.put("email", email);
                                                j.put("phone", mobile);
                                                j.put("password", password);
                                                j.put("deviceid", "" + SessionSave.getSession("mDevice_id", RegisterAct.this));
                                                j.put("devicetoken", "");
                                                j.put("devicetype", "1");
                                                final String url = "type=passenger_account_details";
                                                new SignUp(url, j);
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                ShowToast(RegisterAct.this, "Invaid format");
                                            }
                                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        resendOTPTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                final String Url = "type=resend_otp&email=" + SessionSave.getSession("Email", RegisterAct.this) + "&user_type=P";
                new ResendOTP(Url);
            }
        });
    }

    /**
     * Function for getting image from sd card or camera
     */
    @Override
    protected void onActivityResult(final int requestcode, final int resultcode, final Intent data) {
        try {
            if (resultcode == RESULT_OK) {
                System.gc();
                switch (requestcode) {
                    case 0:
                        try {
                            new ImageCompressionAsyncTask().execute(data.getDataString());
                        } catch (final Exception e) {
                        }
                        break;
                    case 1:
                        try {
                            new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        } catch (final Exception e) {
        }
    }

    /**
     * This class used to compress the profile image
     * <p>
     * This class used to compress the profile image
     * </p>
     *
     * @author developer
     */
    private class ImageCompressionAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private Dialog mDialog;
        private String result;
        private int orientation;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            final View view = View.inflate(RegisterAct.this, R.layout.progress_bar, null);
            mDialog = new Dialog(RegisterAct.this, R.style.NewDialog);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();

            ImageView iv= (ImageView)mDialog.findViewById(R.id.giff);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
            Glide.with(RegisterAct.this)
                    .load(R.raw.loading_anim)
                    .into(imageViewTarget);
        }

        @Override
        protected Bitmap doInBackground(final String... params) {
            try {
                result = getRealPathFromURI(params[0]);
                final File file = new File(result);
                mBitmap = decodeImageFile(file);
                if (mBitmap != null) {
                    final Matrix matrix = new Matrix();
                    try {
                        final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        if (orientation == 3) {
                            matrix.postRotate(180);
                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        } else if (orientation == 6) {
                            matrix.postRotate(90);
                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        }
                    } catch (final IOException e) {
                    }
                }
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] image = stream.toByteArray();
                encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
            } catch (final Exception e) {
                // TODO: handle exception
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast(getApplicationContext(), "" + NC.getResources().getString(R.string.please_select_image_from_valid_path));
                    }
                });
            }
            return mBitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mDialog.isShowing())
                mDialog.dismiss();
            ProfileImg.setImageBitmap(result);
        }
    }

    /**
     * This class used to resend the one time password
     * <p>
     * This class used to resend the one time password
     * </p>
     *
     * @author developer
     */
    private class ResendOTP implements APIResult {
        public ResendOTP(final String url) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(RegisterAct.this, this, "", true).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Message = json.getString("message");
                    } else {
                        Message = json.getString("message");
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(RegisterAct.this, getString(R.string.server_con_error));
                    }
                });
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
            new APIService_Retrofit_JSON(RegisterAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Message = json.getString("message");
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), RegisterAct.this);
                        SessionSave.saveSession("Register", "1", RegisterAct.this);
                        mhandler.sendEmptyMessage(0);
                    } else {
                        alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(RegisterAct.this, getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(final android.os.Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        barImg.setImageResource(R.drawable.progress_2);
                        alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                        FirstsetpLay.setVisibility(View.GONE);
                        SecondsetpLay.setVisibility(View.VISIBLE);
                        ThirdsetpLay.setVisibility(View.GONE);
                        break;
                    case 1:
                        barImg.setImageResource(R.drawable.progress_3);
                        alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                        FirstsetpLay.setVisibility(View.GONE);
                        SecondsetpLay.setVisibility(View.GONE);
                        ThirdsetpLay.setVisibility(View.VISIBLE);
                        if (SessionSave.getSession("skip_credit", RegisterAct.this).equals("1")) {
                            paybycashlay.setVisibility(View.VISIBLE);
                        } else {
                            paybycashlay.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        SessionSave.saveSession("Register", "0", RegisterAct.this);
                        successalert(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                        break;
                    case 3:
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        ;
    };

    /**
     * This class used to register with personal details
     * <p>
     * This class used to register with personal details
     * </p>
     *
     * @author developer
     */
    private class SignUp1 implements APIResult {
        private SignUp1(final String url, final JSONObject data) {
            new APIService_Retrofit_JSON(RegisterAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Message = json.getString("message");
                        SessionSave.saveSession("Register", "2", RegisterAct.this);
                        mhandler.sendEmptyMessage(1);
                    } else
                        alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(RegisterAct.this, getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    /**
     * This class used to register with card details
     * <p/>
     * This class used to register with card details
     * <p/>
     *
     * @author developer
     */
    private class SignUp2 implements APIResult {
        private SignUp2(final String url, JSONObject data) {
            new APIService_Retrofit_JSON(RegisterAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Register", "", RegisterAct.this);
                        Message = json.getString("message");
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), RegisterAct.this);
                        SessionSave.saveSession("Id", json.getJSONObject("detail").getString("id"), RegisterAct.this);
                        SessionSave.saveSession("About", json.getJSONObject("detail").getString("aboutpage_description"), RegisterAct.this);
                        SessionSave.saveSession("Currency", json.getJSONObject("detail").getString("site_currency")+" ", RegisterAct.this);
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), RegisterAct.this);
                        // SessionSave.saveSession("model_details", json.getJSONObject("detail").getString("model_details"), RegisterAct.this);
                        mhandler.sendEmptyMessage(2);
                    } else
                        alert_view(RegisterAct.this, "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(RegisterAct.this, getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    private void successalert(Context mContext, String title, String message, String success_txt, String failure_txt) {
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
                    final Intent i = new Intent(RegisterAct.this, MainHomeFragmentActivity.class);
                    startActivity(i);
                    SessionSave.saveSession("Register", "0", RegisterAct.this);
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

    private void updateDisplay() {
        ExpiryEdt.setText(new StringBuilder().append(mMonth).append("/").append(mYear));
    }

    // the callback received when the user "sets" the date in the dialog
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };
    private DatePicker datePicker;

    @Override
    protected Dialog onCreateDialog(final int id) {
        switch (id) {
            case DATE_DIALOG_ID: {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog dojDPDilog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
                final SimpleDateFormat format1 = new SimpleDateFormat("EE dd MMM yyyy");
                dojDPDilog.setTitle(format1.format(calendar.getTime()));
                try {
                    final java.lang.reflect.Field mDatePickerField = dojDPDilog.getClass().getDeclaredField("mDatePicker");
                    mDatePickerField.setAccessible(true);
                    try {
                        datePicker = (DatePicker) mDatePickerField.get(dojDPDilog);
                        final Field datePickerFields[] = mDatePickerField.getType().getDeclaredFields();
                        for (final Field datePickerField : datePickerFields)
                            if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName())) {
                                datePickerField.setAccessible(true);
                                Object dayPicker = new Object();
                                dayPicker = datePickerField.get(datePicker);
                                ((View) dayPicker).setVisibility(View.GONE);
                            }
                    } catch (final IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (final IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (final NoSuchFieldException e) {
                    e.printStackTrace();
                }
                datePicker.init(mYear, mMonth, mDay, new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                        // disableDate(view, year, monthOfYear, dayOfMonth);
                        if (year < calendar.get(Calendar.YEAR))
                            view.updateDate(calendar.get(Calendar.YEAR), mMonth, mDay);
                        if (monthOfYear < calendar.get(Calendar.MONTH) && year == calendar.get(Calendar.YEAR))
                            view.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), mDay);
                        if (dayOfMonth < calendar.get(Calendar.DAY_OF_MONTH) && year == calendar.get(Calendar.YEAR) && monthOfYear == calendar.get(Calendar.MONTH))
                            view.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }
                });
                return dojDPDilog;
            }
        }
        return null;
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (SessionSave.getSession("Register", RegisterAct.this).equals("1") || SessionSave.getSession("Register", RegisterAct.this).equals("2"))
                ShowToast(RegisterAct.this, "" + NC.getResources().getString(R.string.please_complete_registration));
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @author developer
     */
    private class ShowWebpage implements APIResult {
        public ShowWebpage(final String string, JSONObject data) {
            // TODO Auto-generated constructor stub
         //   new APIService_Retrofit_JSON(RegisterAct.this, this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY+"/?"  + "lang=" + SessionSave.getSession("Lang", RegisterAct.this) + "&" + string).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final Intent intent = new Intent(RegisterAct.this, TermsAndConditions.class);
                    final Bundle bundle = new Bundle();
                    intent.putExtra("content", result);
                    bundle.putString("name", NC.getString(R.string.termcond));
                    bundle.putBoolean("status", t_status);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(RegisterAct.this, getString(R.string.server_con_error));
                        }
                    });
//                    alert_view(RegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + result, "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(this);
        super.onDestroy();
    }
}