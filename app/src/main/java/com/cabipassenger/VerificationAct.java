package com.cabipassenger;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * this class used to verify the otp  <br>
 * also it extends the properties of main activity
 * </p>
 *
 * @author developer
 */

public class VerificationAct extends MainActivity implements OnClickListener {
    private LinearLayout Donelay;
    private TextView BackBtn;
    private TextView leftIcon;
    private TextView DoneBtn;
    private TextView HeadTitle;
    private EditText verifyno1Txt;
    private EditText verifyno2Txt;
    private EditText verifyno3Txt;
    private EditText verifyno4Txt;
    private Button resendBut;
    private TextView sentemailTxt;
    private String alert_msg;
    private Bundle alert_bundle = new Bundle();

    private LinearLayout CancelBtn;
    private boolean alertShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        return R.layout.verificationlay;
    }

    /**
     * <p>
     * this method is used for field declarations
     * <p>
     * this method is used to get otp numbers in separate edittext
     * and focus other edittext when current edditext gets input from user
     * </p>
     */

    @SuppressLint("NewApi")
    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), VerificationAct.this);
        TaxiUtil.mActivitylist.add(this);
        FontHelper.applyFont(this, findViewById(R.id.otproot_lay));
        findViewById(R.id.headlayout).setVisibility(View.VISIBLE);
        Donelay = (LinearLayout) findViewById(R.id.rightlay);
        Donelay.setVisibility(View.VISIBLE);
        leftIcon = (TextView) findViewById(R.id.leftIcon);
        DoneBtn = (TextView) findViewById(R.id.rightIconTxt);
        DoneBtn.setVisibility(View.GONE);
        leftIcon.setVisibility(View.VISIBLE);
        leftIcon.setBackgroundResource(R.drawable.back);
        BackBtn = (TextView) findViewById(R.id.back_text);
        CancelBtn = (LinearLayout) findViewById(R.id.leftIconTxt);
        BackBtn.setVisibility(View.INVISIBLE);
        DoneBtn.setText(NC.getResources().getString(R.string.save));
        DoneBtn.setText("" + NC.getResources().getString(R.string.done));
        DoneBtn.setBackground(getResources().getDrawable(R.drawable.draw_back_bg));
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.verify_email));
        verifyno1Txt = (EditText) findViewById(R.id.verifyno1Txt);
        verifyno2Txt = (EditText) findViewById(R.id.verifyno2Txt);
        verifyno3Txt = (EditText) findViewById(R.id.verifyno3Txt);
        verifyno4Txt = (EditText) findViewById(R.id.verifyno4Txt);
        resendBut = (Button) findViewById(R.id.resendBut);
        sentemailTxt = (TextView) findViewById(R.id.sentemailTxt);
//        try {
//            alert_bundle = getIntent().getExtras();
//            if (alert_bundle != null) {
//                alert_msg = alert_bundle.getString("Message");
//            }
//            System.err.println("booktaxi alert_msg" + alert_msg);
//            if (alert_msg != null && alert_msg.length() != 0)
//                alert_view(VerificationAct.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
        sentemailTxt.setText("" + NC.getResources().getString(R.string.verify_enter_code) + " " + SessionSave.getSession("Email", VerificationAct.this) + " and " + SessionSave.getSession("countrycode", VerificationAct.this) + "-" + SessionSave.getSession("m_no", VerificationAct.this));
        verifyno1Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().trim().length() == 1) {
                    verifyno2Txt.requestFocus();
                    verifyno2Txt.setText("");
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
        verifyno1Txt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                verifyno1Txt.setText("");
                return false;
            }
        });
        verifyno2Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (s.toString().trim().length() == 1) {
                    verifyno3Txt.requestFocus();
                    verifyno3Txt.setText("");
                } else if (count != 0) {
                    ///  verifyno1Txt.requestFocus();
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
        verifyno2Txt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                verifyno2Txt.setText("");
                return false;
            }
        });
        verifyno3Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().trim().length() == 1) {
                    verifyno4Txt.requestFocus();
                    verifyno4Txt.setText("");
                } else if (count != 0) {
                    // verifyno2Txt.requestFocus();
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
        verifyno3Txt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                verifyno3Txt.setText("");
                return false;
            }
        });
        verifyno4Txt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                verifyno4Txt.setText("");
                return false;
            }
        });

        verifyno3Txt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int actionId, KeyEvent event) {


                if (actionId == KeyEvent.KEYCODE_DEL) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            verifyno2Txt.requestFocus();
                        }
                    }, 100);
                    //
                    // return true;
                }
                return false;
            }
        });
        verifyno2Txt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int actionId, KeyEvent event) {


                if (actionId == KeyEvent.KEYCODE_DEL) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            verifyno1Txt.requestFocus();
                        }
                    }, 100);
                    //return true;
                }
                return false;
            }
        });
        verifyno4Txt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int actionId, KeyEvent event) {

                if (actionId == KeyEvent.KEYCODE_DEL) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            verifyno3Txt.requestFocus();
                        }
                    }, 100);

                    //  return true;
                }
                return false;
            }
        });


        verifyno4Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().trim().length() == 1) {
                    if (verifyno1Txt.getText().toString().length() == 1
                            && verifyno2Txt.getText().toString().length() == 1
                            && verifyno3Txt.getText().toString().length() == 1) {

                        verificationData();
                    } else if (count != 0) {
                        alert_view(VerificationAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.verify_valid_code), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    //  verifyno3Txt.requestFocus();
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

        leftIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*SessionSave.saveSession("IsOTPSend", "", VerificationAct.this);
                SessionSave.saveSession("f_name", "", VerificationAct.this);
                SessionSave.saveSession("l_name", "", VerificationAct.this);
                SessionSave.saveSession("e_mail", "", VerificationAct.this);
                SessionSave.saveSession("m_no", "", VerificationAct.this);
                SessionSave.saveSession("p_wd", "", VerificationAct.this);
                SessionSave.saveSession("cp_wd", "", VerificationAct.this);
                SessionSave.saveSession("ref_txt", "", VerificationAct.this);*/
                onBackPressed();
//
//                Intent intent = new Intent(getApplicationContext(), RegisterNewAct.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getApplicationContext(), RegisterNewAct.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        setonclickLisenter();
    }

    private void setonclickLisenter() {
        BackBtn.setOnClickListener(this);
        DoneBtn.setOnClickListener(this);
        resendBut.setOnClickListener(this);
    }

    /**
     * this method is used to get the otp number in edittext and call the api
     */
    private void verificationData() {
        String verifyno1 = verifyno1Txt.getText().toString().trim();
        String verifyno2 = verifyno2Txt.getText().toString().trim();
        String verifyno3 = verifyno3Txt.getText().toString().trim();
        String verifyno4 = verifyno4Txt.getText().toString().trim();
        String otpCode = "";
        if (verifyno1.length() != 0 && verifyno2.length() != 0 && verifyno3.length() != 0 && verifyno4.length() != 0) {
            otpCode = verifyno1 + verifyno2 + verifyno3 + verifyno4;
            try {
                JSONObject j = new JSONObject();
                j.put("email", SessionSave.getSession("Email", VerificationAct.this));
                j.put("otp", otpCode);
                final String url = "type=otp_verify";



                new OtpVerification(url, j);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else {
            //    alert_view(VerificationAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.verify_valid_code), "" + NC.getResources().getString(R.string.ok), "");
        }
    }

    /**
     * This method is used to call resend otp api
     */

    private void resendOtp() {
        try {
            JSONObject j = new JSONObject();
            j.put("email", SessionSave.getSession("Email", VerificationAct.this));
            j.put("user_type", "P");
            final String url = "type=resend_otp";
            new ResendOtp(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * This class used to verify the otp
     * <p>
     * This class used to verify the otp
     * <p>
     *
     * @author developer
     */

    private class OtpVerification implements APIResult {
        private OtpVerification(final String url, JSONObject data) {
            new APIService_Retrofit_JSON(VerificationAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub


            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);



                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("IsOTPSend", "", VerificationAct.this);
                        SessionSave.saveSession("f_name", "", VerificationAct.this);
                        SessionSave.saveSession("l_name", "", VerificationAct.this);
                        SessionSave.saveSession("e_mail", "", VerificationAct.this);
                        SessionSave.saveSession("p_wd", "", VerificationAct.this);
                        SessionSave.saveSession("cp_wd", "", VerificationAct.this);
                        SessionSave.saveSession("ref_txt", "", VerificationAct.this);

                        SessionSave.saveSession("Phone", SessionSave.getSession("m_no", VerificationAct.this), VerificationAct.this);

                        //clearing temp session
                        SessionSave.saveSession("m_no", "", VerificationAct.this);


                        SessionSave.saveSession("Register", "2", VerificationAct.this);
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), VerificationAct.this);
                        final Intent i = new Intent(VerificationAct.this, CardRegisterAct.class);
                        Bundle data = new Bundle();
                        // data.putString("PaymentOption",json.getJSONObject("detail").getString("payment_option"));
                        data.putString("Message", json.getString("message"));
                        i.putExtras(data);
                        startActivity(i);
                        finish();
                    } else
                        alert_view(VerificationAct.this, NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(VerificationAct.this, getString(R.string.server_con_error));
                    }
                });
//				alert_view(VerificationAct.this, "Message", "" + result, ""+NC.getResources().getString(R.string.ok), "");
            }
        }
    }

    /**
     * This class used to resend the otp
     * <p>
     * This class used to resend the otp
     * <p>
     *
     * @author developer
     */
    private class ResendOtp implements APIResult {
        private ResendOtp(final String url, JSONObject data) {
            new APIService_Retrofit_JSON(VerificationAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(VerificationAct.this, NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    } else
                        alert_view(VerificationAct.this, NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(VerificationAct.this, getString(R.string.server_con_error));
                    }
                });
//				alert_view(VerificationAct.this, "Message", "" + result, ""+NC.getResources().getString(R.string.ok), "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rightIconTxt:
                verificationData();
                break;
            case R.id.back_text:
                onBackPressed();
                break;
            case R.id.resendBut:
                resendOtp();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onBackPressed() {

       /* SessionSave.saveSession("IsOTPSend", "", VerificationAct.this);
        SessionSave.saveSession("f_name", "", VerificationAct.this);
        SessionSave.saveSession("l_name", "", VerificationAct.this);
        SessionSave.saveSession("e_mail", "", VerificationAct.this);
        SessionSave.saveSession("m_no", "", VerificationAct.this);
        SessionSave.saveSession("p_wd", "", VerificationAct.this);
        SessionSave.saveSession("cp_wd", "", VerificationAct.this);
        SessionSave.saveSession("ref_txt", "", VerificationAct.this);*/
        if (alertShowed) {
            Intent intent = new Intent(getApplicationContext(), RegisterNewAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            showAlertBack();
        }

        // alertview(VerificationAct.this, "", NC.getResources().getString(R.string.cancel_registration), NC.getResources().getString(R.string.yes), NC.getResources().getString(R.string.no));

    }

    private void showAlertBack() {
        try {
            final View view = View.inflate(VerificationAct.this, R.layout.netcon_lay, null);
            alertmDialog = new Dialog(VerificationAct.this, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(VerificationAct.this, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);

            title_text.setVisibility(View.GONE);
            button_failure.setVisibility(View.VISIBLE);

            message_text.setText(NC.getString(R.string.message_alert_back));
            button_success.setText(NC.getString(R.string.ok));
            button_failure.setText(NC.getString(R.string.cancel));

            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {

                    alertShowed = true;
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                    onBackPressed();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();

                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void alertview(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);

            title_text.setVisibility(View.GONE);
            button_failure.setVisibility(View.VISIBLE);

            message_text.setText(message);
            button_success.setText(success_txt);
            button_failure.setText(failure_txt);

            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {

                    SessionSave.saveSession("IsOTPSend", "", VerificationAct.this);
                    SessionSave.saveSession("f_name", "", VerificationAct.this);
                    SessionSave.saveSession("l_name", "", VerificationAct.this);
                    SessionSave.saveSession("e_mail", "", VerificationAct.this);
                    SessionSave.saveSession("m_no", "", VerificationAct.this);
                    SessionSave.saveSession("p_wd", "", VerificationAct.this);
                    SessionSave.saveSession("cp_wd", "", VerificationAct.this);
                    SessionSave.saveSession("ref_txt", "", VerificationAct.this);

                    Intent intent = new Intent(getApplicationContext(), RegisterNewAct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
