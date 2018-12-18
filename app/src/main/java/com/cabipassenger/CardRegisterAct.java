package com.cabipassenger;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * this class is used to get payment card card details from users
 *
 * @author developer
 */
public class CardRegisterAct extends MainActivity implements OnClickListener {
    private LinearLayout Donelay;
    private TextView BackBtn;
    private TextView leftIcon;
    private TextView HeadTitle;
    private TextView creditcardTxt;
    private TextView cashTxt;
    private TextView promocodeTxt;
    private EditText cardnumEdt;
    private CheckBox termsTxt;
    private TextView termsTxtCash, termscard;
    private CheckBox termscheck;
    private EditText cvvEdt;
    private EditText cardnameEdt;
    private CheckBox isDefaultChk;
    private LinearLayout carddetail_lay;
    private LinearLayout cashLayout;
    private Spinner monthspn;
    private Spinner yearspn;
    private Button submitBtn;
    private Dialog mDialog;

    private String[] monthLst;
    private String[] yearLst;
    private String expmonth = "";
    private String expyear = "";
    private boolean monthtouched;
    private boolean yeartouched;
    private int isDefault = 0;
    private Calendar cal;
    private int curmonth;
    private int curyear;
    private String alert_msg;
    private String payment_option;
    private Bundle alert_bundle = new Bundle();
    private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char DIVIDER = ' ';
    // Change this to what you want... ' ', '-' etc..
    private static final char space = ' ';
    private ArrayAdapter<String> monthadapter;
    private ArrayAdapter<String> yearadapter;
    private Dialog loadingDialog;

    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        setLocale();
        return R.layout.cardregister_lay;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), CardRegisterAct.this);
        TaxiUtil.mActivitylist.add(this);
        FontHelper.applyFont(this, findViewById(R.id.cardroot_lay));
        Donelay = (LinearLayout) findViewById(R.id.rightlay);
        Donelay.setVisibility(View.INVISIBLE);
        findViewById(R.id.headlayout).setVisibility(View.VISIBLE);
        leftIcon = (TextView) findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        BackBtn = (TextView) findViewById(R.id.back_text);
        BackBtn.setVisibility(View.GONE);
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.menu_payment));
        creditcardTxt = (TextView) findViewById(R.id.creditcardTxt);
        cashTxt = (TextView) findViewById(R.id.cashTxt);
        promocodeTxt = (TextView) findViewById(R.id.promocodeTxt);
        carddetail_lay = (LinearLayout) findViewById(R.id.carddetail_lay);
        cashLayout = (LinearLayout) findViewById(R.id.cashLayout);
        monthspn = (Spinner) findViewById(R.id.monthspn);
        yearspn = (Spinner) findViewById(R.id.yearspn);
        cardnumEdt = (EditText) findViewById(R.id.cardnumEdt);
        // cardnumEdt.addTextChangedListener(new FourDigitCardFormatWatcher());
        cvvEdt = (EditText) findViewById(R.id.cvvEdt);
        cardnameEdt = (EditText) findViewById(R.id.cardnameEdt);
        termsTxt = (CheckBox) findViewById(R.id.termsTxt);
        termsTxtCash = (TextView) findViewById(R.id.termsTxtCash);
        termscard = (TextView) findViewById(R.id.termscard);
        termscheck = (CheckBox) findViewById(R.id.termscheck);
        isDefaultChk = (CheckBox) findViewById(R.id.isDefaultChk);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        monthLst = getResources().getStringArray(R.array.monthlistary);
        yearLst = getResources().getStringArray(R.array.yearary);
        int selection_yearpos = 0;
        for (int i = 0; i < yearLst.length; i++) {
            if (yearLst[i].toString().equalsIgnoreCase(String.valueOf(curyear))) {
                selection_yearpos = i;
            }
        }


        monthLst = getResources().getStringArray(R.array.monthlistary);
        yearLst = new String[20];
        cal = Calendar.getInstance();
        curmonth = cal.get(Calendar.MONTH);
        curyear = cal.get(Calendar.YEAR);
        ArrayList<String> yearLstArr = new ArrayList<>();
        ArrayList<String> monthLstArr = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            yearLst[i] = String.valueOf(curyear + i);
        }
        for (int i = 0; i < monthLst.length; i++)
            monthLstArr.add(monthLst[i]);
        for (int i = 0; i < yearLst.length; i++)
            yearLstArr.add(yearLst[i]);

        monthadapter = new FontHelper.MySpinnerAdapterWhite(CardRegisterAct.this, R.layout.monthitem_spinnerlay, monthLstArr);
        monthspn.setAdapter(monthadapter);

        yearadapter = new FontHelper.MySpinnerAdapterWhite(CardRegisterAct.this, R.layout.monthitem_spinnerlay, yearLstArr);
        yearspn.setAdapter(yearadapter);
        cal = Calendar.getInstance();
        curmonth = cal.get(Calendar.MONTH);
        curyear = cal.get(Calendar.YEAR);
        monthspn.setSelection(curmonth);
        monthspn.setSelected(true);
        try {
            alert_bundle = getIntent().getExtras();
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("Message");
                //payment_option = alert_bundle.getString("PaymentOption");
                //setUIDisplay();
            }
//            if (alert_msg != null && alert_msg.length() != 0)
//                alert_view(CardRegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        //Setting dynamic payment option
        setUIDisplay();

        cardnumEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().startsWith("4")) {
                    if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                        cardnumEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_visa, 0, 0, 0);
                    else
                        cardnumEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.card_visa, 0);
                } else if (s.toString().length() >= 2) {
                    int prefix = Integer.parseInt(s.toString().substring(0, 2));
                    if (prefix >= 51 && prefix <= 55) {
                        if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                            cardnumEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_master, 0, 0, 0);
                        else
                            cardnumEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.card_master, 0);
                    } else {
                        cardnumEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                } else {
                    cardnumEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }
        });

        if (SessionSave.getSession("skip_credit", CardRegisterAct.this).equals("0")) {
            creditcardTxt.setVisibility(View.GONE);
        }
        yearspn.setSelection(selection_yearpos);
        yearspn.setSelected(true);
        monthspn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                // TODO Auto-generated method stub
                monthtouched = true;
                expmonth = "" + (curmonth + 1);
                monthadapter.notifyDataSetChanged();
                return false;
            }
        });
        monthspn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView txt = ((TextView) view);
                txt.setTextColor(getResources().getColor(R.color.textviewcolor_light));
//                txt.setGravity(Gravity.CENTER);
                txt.setGravity(Gravity.LEFT);
                if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                    txt.setGravity(Gravity.RIGHT);
                if (!monthtouched)
                    txt.setText("" + NC.getResources().getString(R.string.reg_month));
                else {
                    expmonth = parent.getItemAtPosition(arg2).toString();
                    if (yearspn.getSelectedItem().equals(String.valueOf(curyear)) && Integer.parseInt(expmonth) <= curmonth) {
                        expmonth = String.valueOf(curmonth + 1);
                        monthspn.setSelection(curmonth);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        System.out.println("______**" + yeartouched + "__" + curyear + "__" + expyear + "__" + expmonth);
        yearspn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                // TODO Auto-generated method stub
                yeartouched = true;
                expyear = "" + curyear;
                yearadapter.notifyDataSetChanged();
                return false;
            }
        });
        yearspn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView txt = ((TextView) view);
                txt.setTextColor(getResources().getColor(R.color.textviewcolor_light));
                //   txt.setGravity(Gravity.CENTER);
                txt.setGravity(Gravity.LEFT);
                if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                    txt.setGravity(Gravity.RIGHT);
                System.out.println("______*****" + yeartouched + "__" + curyear + "__" + expyear + "__" + expmonth);
                if (!yeartouched)
                    txt.setText("" + NC.getResources().getString(R.string.reg_year));
                else {
                    if (parent.getItemAtPosition(arg2).toString().equals(String.valueOf(curyear)) && Integer.valueOf(expmonth) < (curmonth + 1))
                        monthspn.setSelection(curmonth);
                    expyear = parent.getItemAtPosition(arg2).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        isDefaultChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked)
                    isDefault = 1;
                else
                    isDefault = 0;
            }
        });


        if (SessionSave.getSession("IsReferAvail", CardRegisterAct.this).equals("1"))
            promocodeTxt.setVisibility(View.INVISIBLE);
        else
            promocodeTxt.setVisibility(View.GONE);


        setonClickListener();
    }

    /**
     * this method is used to set tha layout images as per the language<br>
     * alignment especially for arabic & english
     */
    private void setUIDisplay() {
        creditcardTxt.setVisibility(View.GONE);
        cashLayout.setVisibility(View.GONE);
        try {
            JSONArray jsonArray = new JSONArray(SessionSave.getSession("passenger_payment_option", CardRegisterAct.this));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getInt("pay_mod_id") == 1) {
                    cashLayout.setVisibility(View.VISIBLE);

                    //cashTxt.setText(jsonObject.getString("pay_mod_name"));
                    // if(jsonObject.getString("pay_mod_name").trim().equals("Cash"))
                    cashTxt.setText(NC.getString(R.string.cash));


                    if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                        cashTxt.setGravity(Gravity.RIGHT);

                } else if (jsonObject.getInt("pay_mod_id") == 2) {
                    creditcardTxt.setVisibility(View.VISIBLE);
//                    if(jsonObject.getString("pay_mod_name").trim().equals("Cash"))
//                        cashTxt.setText(NC.getString(R.string.cash));
//                    else
                    // cashTxt.setText(NC.getString(R.string.payment_card));
                    if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa")) {
                        cashTxt.setGravity(Gravity.RIGHT);
                        creditcardTxt.setGravity(Gravity.RIGHT);
                    }

                    //  creditcardTxt.setText(jsonObject.getString("pay_mod_name"));
                    creditcardTxt.setText(NC.getString(R.string.payment_card));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setonClickListener() {

        creditcardTxt.setOnClickListener(this);
        cashLayout.setOnClickListener(this);

        BackBtn.setOnClickListener(this);
        promocodeTxt.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        termsTxt.setOnClickListener(this);
        termsTxtCash.setOnClickListener(this);
        termscard.setOnClickListener(this);
        termscheck.setOnClickListener(this);
    }


    static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    public static Editable additional(Editable s) {
        Editable ss = null;
        if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
            ss = s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
        }
        return ss;
    }

    static boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
        for (int i = 0; i < s.length(); i++) { // chech that every element is right
            if (i > 0 && (i + 1) % dividerModulo == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    static String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }
        return formatted.toString();
    }

    /**
     * this method is used to register the card details
     */
    private void CardRegisterData() {
        String CardNumber = cardnumEdt.getText().toString().trim().replaceAll("\\s", "");
        String cvvNumber = cvvEdt.getText().toString().trim();
        String cardName = cardnameEdt.getText().toString().trim();
        if (validations(ValidateAction.isValidCard, CardRegisterAct.this, CardNumber))
            if (validations(ValidateAction.isNullMonth, CardRegisterAct.this, expmonth))
                if (validations(ValidateAction.isNullYear, CardRegisterAct.this, expyear))
                    if (validations(ValidateAction.isValidCvv, CardRegisterAct.this, cvvNumber))
                        //  if (validations(ValidateAction.isNullCardname, CardRegisterAct.this, cardName))
                        if (!termsTxt.isChecked())
                            alert_view(CardRegisterAct.this, "Message", "" + NC.getResources().getString(R.string.agree_the_terms_and_condition), "" + NC.getResources().getString(R.string.ok), "");
                        else {
                            try {
                                JSONObject j = new JSONObject();
                                j.put("email", SessionSave.getSession("Email", CardRegisterAct.this));
                                j.put("creditcard_no", CardNumber);
                                j.put("expdatemonth", expmonth);
                                j.put("expdateyear", expyear);
                                j.put("creditcard_cvv", cvvNumber);
                                j.put("savecard", "1");
                                j.put("default", isDefault);
                                j.put("card_holder_name", cardName);
                                final String url = "type=passenger_card_details";
                                System.out.println("card details" + "............." + j);
                                new CardRegister(url, j);
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
    }

    /**
     * This class used to register with promo code
     * <p/>
     * This class used to register with promo code
     * <p/>
     *
     * @author developer
     */
    private class PromoCode implements APIResult {
        private PromoCode(final String url, JSONObject data) {
            // new APIService_Volley_JSON(CardRegisterAct.this, this, data, false).execute(url);
            new APIService_Retrofit_JSON(CardRegisterAct.this, this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", CardRegisterAct.this) + "&" + url).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        mDialog.dismiss();
                        alert_view(CardRegisterAct.this, "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    } else
                        alert_view(CardRegisterAct.this, "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(CardRegisterAct.this, getString(R.string.server_con_error));
                    }
                });
//                alert_view(CardRegisterAct.this, "Message", "" + result, "" + NC.getResources().getString(R.string.ok), "");
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
    private class CardRegister implements APIResult {
        private CardRegister(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(CardRegisterAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            cashTxt.setEnabled(true);
            cashTxt.setOnClickListener(CardRegisterAct.this);
            TaxiUtil.closeDialog(mshowDialog);
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Register", "", CardRegisterAct.this);
                        SessionSave.saveSession("Email", json.getJSONObject("detail").getString("email"), CardRegisterAct.this);
                        SessionSave.saveSession("Id", json.getJSONObject("detail").getString("id"), CardRegisterAct.this);
                        SessionSave.saveSession("ProfileImage", json.getJSONObject("detail").getString("profile_image"), CardRegisterAct.this);
                        SessionSave.saveSession("ProfileName", json.getJSONObject("detail").getString("name"), CardRegisterAct.this);
                        SessionSave.saveSession("Phone", json.getJSONObject("detail").getString("phone"), CardRegisterAct.this);
                        SessionSave.saveSession("About", json.getJSONObject("detail").getString("aboutpage_description"), CardRegisterAct.this);
                        SessionSave.saveSession("Currency", json.getJSONObject("detail").getString("site_currency") + " ", CardRegisterAct.this);
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), CardRegisterAct.this);
                        SessionSave.saveSession("RefCode", json.getJSONObject("detail").getString("referral_code"), CardRegisterAct.this);
                        SessionSave.saveSession("RefAmount", json.getJSONObject("detail").getString("referral_code_amount"), CardRegisterAct.this);

                        if (json.getJSONObject("detail").getString("split_fare").equals("1"))
                            SessionSave.saveSession(TaxiUtil.isSplitOn, true, CardRegisterAct.this);
                        else
                            SessionSave.saveSession(TaxiUtil.isSplitOn, false, CardRegisterAct.this);
                        SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, CardRegisterAct.this);
                        try {
                            if (CardRegisterAct.this != null) {
                                showLoading(CardRegisterAct.this);
                                final Intent i = new Intent(CardRegisterAct.this, MainHomeFragmentActivity.class);
                                i.putExtra("alert_message", json.getString("message"));
                                startActivity(i);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else
                        alert_view(CardRegisterAct.this, "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(CardRegisterAct.this, getString(R.string.server_con_error));
                    }
                });
//                alert_view(CardRegisterAct.this, "Message", "" + result, "" + NC.getResources().getString(R.string.ok), "");
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.creditcardTxt:
                cashTxt.setEnabled(true);
                if (carddetail_lay.isShown()) {
                    carddetail_lay.setVisibility(View.GONE);

                    if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                        creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, R.drawable.credit_card, 0);
                    else
                        creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, R.drawable.right_arrow, 0);
                } else {
                    carddetail_lay.setVisibility(View.VISIBLE);

                    if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                        creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_arrow, 0, R.drawable.credit_card, 0);
                    else
                        creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, R.drawable.down_arrow, 0);

                }
                submitBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.back_text:
                finish();
                break;
            case R.id.promocodeTxt:
                carddetail_lay.setVisibility(View.GONE);
                if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar") || SessionSave.getSession("Lang", CardRegisterAct.this).equals("fa"))
                    creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, R.drawable.credit_card, 0);
                else
                    creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, R.drawable.right_arrow, 0);
                ShowPromoDilaog();
                break;
            case R.id.submitBtn:
                CardRegisterData();
                break;
            case R.id.cashLayout:

                cashApi();


                break;
//            case R.id.termsTxt:
//                try {
//                    //				JSONObject j = new JSONObject();
//                    //				j.put("pagename", "termsconditions");
//                    //				j.put("device_type", "1");
//                    if (termsTxt.isChecked()) {
//                        String url = "&type=dynamic_page&pagename=3&device_type=1";
//                        new ShowWebpage(url, null);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//                break;
            case R.id.termscard:
            case R.id.termsTxtCash:
                try {
                    // if (termsTxtCash.isChecked()) {
                    String url = "&type=dynamic_page&pagename=3&device_type=1";
                    new ShowWebpage(url, null);
//                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void cashApi() {

        SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, CardRegisterAct.this);
        if (!termscheck.isChecked())
            alert_view(CardRegisterAct.this, "Message", "" + NC.getResources().getString(R.string.agree_the_terms_and_condition), "" + NC.getResources().getString(R.string.ok), "");
        else {
//                    if (SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar")||SessionSave.getSession("Lang",CardRegisterAct.this).equals("fa"))
//                        creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, R.drawable.credit_card, 0);
//                    else
//                        creditcardTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_arrow, 0, R.drawable.credit_card, 0);
            showLoading(CardRegisterAct.this);
            try {
                JSONObject j = new JSONObject();
                j.put("email", SessionSave.getSession("Email", CardRegisterAct.this));
                j.put("creditcard_no", "");
                j.put("expdatemonth", "");
                j.put("expdateyear", "");
                j.put("creditcard_cvv", "");
                j.put("savecard", "0");
                j.put("default", isDefault);
                final String url = "type=passenger_card_details";
                new CardRegister(url, j);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    /**
     * this method is used to show promocode dialog
     */

    private void ShowPromoDilaog() {

        try {
            final View view = View.inflate(CardRegisterAct.this, R.layout.alert_view, null);
            mDialog = new Dialog(CardRegisterAct.this, R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.show();
            FontHelper.applyFont(CardRegisterAct.this, mDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id), CardRegisterAct.this);
            final TextView titleTxt = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView msgTxt = (TextView) mDialog.findViewById(R.id.message_text);
            msgTxt.setVisibility(View.GONE);
            final EditText promocodeEdt = (EditText) mDialog.findViewById(R.id.forgotmail);
            final Button OK = (Button) mDialog.findViewById(R.id.button_success);
            final Button Cancel = (Button) mDialog.findViewById(R.id.button_failure);
            Cancel.setVisibility(View.GONE);
            promocodeEdt.setVisibility(View.VISIBLE);
            OK.setText("" + NC.getResources().getString(R.string.ok));
            titleTxt.setText("" + NC.getResources().getString(R.string.referal));
            promocodeEdt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            int maxLengthpromoCode = getResources().getInteger(R.integer.promoMaxLength);
            promocodeEdt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            promocodeEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthpromoCode)});

            InputFilter[] editFilters = promocodeEdt.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.AllCaps();
            promocodeEdt.setFilters(newFilters);
            promocodeEdt.setHint("" + NC.getResources().getString(R.string.reg_enter_referral));
            OK.setOnClickListener(new OnClickListener() {
                private String promocode;

                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    try {
                        promocode = promocodeEdt.getText().toString();
                        if (promocode.length() != 0) {
                            JSONObject j = new JSONObject();
                            j.put("email", SessionSave.getSession("Email", CardRegisterAct.this));
                            j.put("referral_code", promocode);
                            String url = "type=passenger_referral_code";
                            new PromoCode(url, j);
                        } else {
                            alert_view(CardRegisterAct.this, "Message", "" + NC.getResources().getString(R.string.reg_enter_referral), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            Cancel.setOnClickListener(new OnClickListener() {
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

    /**
     * this class is used to show web page for terms and conditions
     *
     * @author developer
     */
    private class ShowWebpage implements APIResult {
        public ShowWebpage(final String string, JSONObject data) {
            // TODO Auto-generated constructor stub
            //   new APIService_Retrofit_JSON(CardRegisterAct.this, this, "", true).execute(TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", CardRegisterAct.this) + string);
            //			new APIService_HTTP_JSON(CardRegisterAct.this, this, data, false, TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", CardRegisterAct.this) + "&" + string).execute();
            new APIService_Retrofit_JSON(CardRegisterAct.this, this, true, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", CardRegisterAct.this) + "&encode=" + SessionSave.getSession("encode", CardRegisterAct.this) + string).execute();

        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final Intent intent = new Intent(CardRegisterAct.this, TermsAndConditions.class);
                    final Bundle bundle = new Bundle();
                    intent.putExtra("content", result);
                    bundle.putString("name", getString(R.string.termcond));
                    bundle.putBoolean("status", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(CardRegisterAct.this, getString(R.string.server_con_error));
                        }
                    });
//                    alert_view(CardRegisterAct.this, "" + NC.getResources().getString(R.string.message), "" + result, "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        TaxiUtil.closeDialog(mDialog);
        super.onStop();
    }
}