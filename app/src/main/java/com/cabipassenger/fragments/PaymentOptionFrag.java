package com.cabipassenger.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.TermsAndConditions;
import com.cabipassenger.adapter.CreditCardAdapter;
import com.cabipassenger.data.CreditCardData;
import com.cabipassenger.features.CToast;
import com.cabipassenger.features.Validation;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.DatePicker_CardExpiry;
import com.cabipassenger.util.DrawableJava;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.FourDigitCardFormatWatcher;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * this method is used to show the payment to user
 */

public class PaymentOptionFrag extends Fragment implements DatePicker_CardExpiry.DateDialogInterface {
    // Class members declarations.
    protected static final int DATE_DIALOG_ID = 0;
    private LinearLayout CancelBtn;
    private Button DoneBtn;
    private TextView CancelTxt;
    private TextView HeadTitle;
    private LinearLayout CarddetailLay;
    private LinearLayout ListLay;
    private Spinner CardtypeSpn;
    private Spinner pay_monthspn;
    private Spinner pay_yearspn;
    private EditText CardnoEdt;
    //private TextView  CardnoTxt;//ExpiryEdt
    private EditText CvvEdt;
    private ListView List;
    private CheckBox pay_termsTxt;
    private  TextView termsTxtCash;
    public static boolean Add;
    private static String Type = "";
    private String mMonth;
    private int mDay;
    private String mYear;
    private static String cardId;
    private int cardPos;
    String c = "";
    String[] cardtype;
    private ArrayAdapter<String> card_adapter;

    private boolean pay_monthtouched;
    private boolean pay_yeartouched;

    private CheckBox Defaultcheck;
    private TaxiUtil mUtil;
    private static int DefaultCheck;
    private static String OriginalCard, OriginalCvv;
    private CreditCardAdapter adapter;
    private String xCardno, xCvv;
    private Button bookingsBtn;
    private TextView back_text;
    // private Button removecardBtn;
    private DatePicker_CardExpiry datePicker_Dialog;
    private Dialog mshowDialog;
    private Dialog alertmDialog;
    private TextView ontxt;

    private ArrayAdapter<String> pay_monthadapter;
    private ArrayAdapter<String> pay_yearadapter;
    private String[] pay_monthLst;
    private String[] pay_yearLst;
    private Calendar cal;
    private int curmonth;
    private int curyear;
    private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char DIVIDER = ' ';
    private EditText pay_cardnameEdt;
    private TextView CardnoTxt;
    private Button removecardBtn;
    private View v;
    private FrameLayout edit_bg_card;
    // Set the layout to activity.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.paymentoptionlay, container, false);
        priorChanges(v);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

//            }
//        },100);
        return v;
    }

    public void priorChanges(View v) {

        DoneBtn = (Button) v.findViewById(R.id.saveB);
        DoneBtn.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        pay_monthspn = (Spinner) v.findViewById(R.id.pay_monthspn);
        pay_yearspn = (Spinner) v.findViewById(R.id.pay_yearspn);
        pay_termsTxt = (CheckBox) v.findViewById(R.id.pay_termsTxt);
        termsTxtCash=(TextView)v.findViewById(R.id.termsTxtCash);
        edit_bg_card = (FrameLayout) v.findViewById(R.id.edit_bg_card);
        HeadTitle.setText(NC.getResources().getString(R.string.payment));

        FontHelper.applyFont(getActivity(), v.findViewById(R.id.headlayout));


        Initialize(v);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Colorchange.ChangeColor((ViewGroup)(PaymentOptionFrag.this.v),getActivity());
                Colorchange.ChangeColor((ViewGroup) (((ViewGroup) getActivity()
                        .findViewById(android.R.id.content)).getChildAt(0)), getActivity());
                DrawableJava.draw_edittext_bg(edit_bg_card, CL.getColor(getActivity(), R.color.header_bgcolor), CL.getColor(getActivity(), R.color.header_bgcolor));
            }
        }, 100);

        //   Colorchange.ChangeColor((ViewGroup)v,getActivity());
    }

    // Initialize the views on layout
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Initialize(View v) {
        // TODO Auto-generated method stub
        try {
            JSONObject j = new JSONObject();
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            j.put("card_type", "");
            j.put("default", "");
            new GetCardlist("type=get_credit_card_details", j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        TaxiUtil.current_act = "PaymentOptionsAct";
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.payment_contain));

        adapter = new CreditCardAdapter(getActivity());
        CancelBtn = (LinearLayout) v.findViewById(R.id.leftIconTxt);


        // DoneBtn.setBackground(NC.getResources().getDrawable(R.drawable.draw_back_header_bgcolor));

        DoneBtn.setText(NC.getResources().getString(R.string.edit));
        //  DoneBtn.setBackground(NC.getResources().getDrawable(R.drawable.draw_back_header_bgcolor));
        ontxt = (TextView) v.findViewById(R.id.ontxt);
        CarddetailLay = (LinearLayout) v.findViewById(R.id.carddetail);
        ListLay = (LinearLayout) v.findViewById(R.id.cardlist);
        CarddetailLay.setVisibility(View.GONE);
        ListLay.setVisibility(View.VISIBLE);
        CardtypeSpn = (Spinner) v.findViewById(R.id.cardtypeSpn);
        CardnoEdt = (EditText) v.findViewById(R.id.cardnoEdt);
        pay_cardnameEdt = (EditText) v.findViewById(R.id.pay_cardnameEdt);
        CardnoTxt = (TextView) v.findViewById(R.id.cardnoText);
        CardnoTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CardnoEdt.setText("");
                CardnoEdt.setHint(CardnoTxt.getText().toString());
                v.setVisibility(View.GONE);
            }
        });

        termsTxtCash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                TermsConditions();
            }
        });

        CardnoEdt.addTextChangedListener(new FourDigitCardFormatWatcher(getActivity()));

        CvvEdt = (EditText) v.findViewById(R.id.cvvEdt);
        Defaultcheck = (CheckBox) v.findViewById(R.id.pay_checkBox1);
        bookingsBtn = (Button) v.findViewById(R.id.bookingBtn);
        removecardBtn = (Button) v.findViewById(R.id.removecardBtn);
        List = (ListView) v.findViewById(R.id.list);
        List.setDivider(null);

        pay_monthLst = getResources().getStringArray(R.array.monthlistary);
        pay_yearLst = new String[20];
        cal = Calendar.getInstance();
        curmonth = cal.get(Calendar.MONTH);
        curyear = cal.get(Calendar.YEAR);
        ArrayList<String> pay_yearLstArr = new ArrayList<>();
        ArrayList<String> pay_monthLstArr = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            pay_yearLst[i] = String.valueOf(curyear + i);
        }
        for (int i = 0; i < pay_monthLst.length; i++)
            pay_monthLstArr.add(pay_monthLst[i]);
        for (int i = 0; i < pay_yearLst.length; i++)
            pay_yearLstArr.add(pay_yearLst[i]);
        pay_monthadapter = new FontHelper.MySpinnerAdapterWhite(getActivity(), R.layout.monthitem_spinnerlay, pay_monthLstArr);
        pay_monthspn.setAdapter(pay_monthadapter);
        pay_yearadapter = new FontHelper.MySpinnerAdapterWhite(getActivity(), R.layout.monthitem_spinnerlay, pay_yearLstArr);
        pay_yearspn.setAdapter(pay_yearadapter);


        int selection_yearpos = 0;
        for (int i = 0; i < pay_yearLst.length; i++) {
            if (pay_yearLst[i].toString().equalsIgnoreCase(String.valueOf(curyear))) {
                selection_yearpos = i;
            }
        }

        pay_yearspn.setSelection(selection_yearpos);
        pay_yearspn.setSelected(true);
        pay_monthspn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                // TODO Auto-generated method stub
                pay_monthtouched = true;
                mMonth = "" + (curmonth + 1);
                pay_monthadapter.notifyDataSetChanged();
                return false;
            }
        });
        pay_monthspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView txt = ((TextView) view);
                txt.setTextColor(getResources().getColor(R.color.textviewcolor_light));
                txt.setGravity(Gravity.LEFT);
                if (SessionSave.getSession("Lang", getActivity()).equals("fa")||SessionSave.getSession("Lang", getActivity()).equals("ar"))
                    txt.setGravity(Gravity.RIGHT);

                if (!pay_monthtouched)
                    txt.setText("" + NC.getResources().getString(R.string.reg_month));
                else {
                   // System.out.println("________mm" + curmonth + "___" + mMonth + "___" + curyear + "___" + parent.getItemAtPosition(arg2).toString().trim());

                    mMonth = parent.getItemAtPosition(arg2).toString();
                    if (pay_yearspn.getSelectedItem().equals(String.valueOf(curyear)) && Integer.parseInt(mMonth) <= curmonth) {
                        mMonth = String.valueOf(curmonth + 1);
                        pay_monthspn.setSelection(curmonth);
                    } else if (!pay_yeartouched)
                        pay_monthspn.setSelection(curmonth);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
              //  System.out.println("________mm" + curmonth + "___" + mMonth + "___" + curyear + "___");
                if (!pay_yeartouched)
                    pay_monthspn.setSelection(curmonth);
            }
        });

        pay_yearspn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                // TODO Auto-generated method stub
                pay_yeartouched = true;
                mYear = "" + curyear;
                pay_yearadapter.notifyDataSetChanged();
                return false;
            }
        });
        pay_yearspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                // TODO Auto-generated method stub
                TextView txt = ((TextView) view);
                txt.setTextColor(getResources().getColor(R.color.textviewcolor_light));
                //    txt.setGravity(Gravity.CENTER);
                txt.setGravity(Gravity.LEFT);
                if (SessionSave.getSession("Lang", getActivity()).equals("fa")||SessionSave.getSession("Lang", getActivity()).equals("ar"))
                    txt.setGravity(Gravity.RIGHT);
                if (!pay_yeartouched)
                    txt.setText("" + NC.getResources().getString(R.string.reg_year));
                else {
               //     System.out.println("________" + curmonth + "___" + mMonth + "___" + curyear + "___" + parent.getItemAtPosition(arg2).toString().trim());
                    if (parent.getItemAtPosition(arg2).toString().trim().equals(String.valueOf(curyear)) && Integer.valueOf(mMonth) < (curmonth + 1))
                        pay_monthspn.setSelection(curmonth);
                    mYear = parent.getItemAtPosition(arg2).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        // To move from getActivity() activity to taxi search activity.
        bookingsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i;
                if (!SessionSave.getSession("trip_id", getActivity()).equals("")) {
                    showLoading(getActivity());
                    //  i = new Intent(getActivity(), OngoingTrip.class);
//                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
//                    startActivity(i);
//                    finish();
                } else {
                    //showLoading(getActivity());
                    ((MainHomeFragmentActivity) getActivity()).homePage();
                    //  i = new Intent(getActivity(), BookTaxiAct.class);
//                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
//                    startActivity(i);
//                    finish();
                }
            }
        });

        // To change the card number edit text value on focus.
        CardnoEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    if (CardnoEdt.getText().toString().contains("X"))
                        CardnoEdt.setText("");

                } else {
                    if (CardnoEdt.getText().length() <= 0) {
//                        CardnoEdt.setText(xCardno);
                        CardnoTxt.setText(xCardno);
                        CardnoTxt.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        CardnoEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.toString().startsWith("4")) {
                    if (SessionSave.getSession("Lang", getActivity()).equals("fa")||SessionSave.getSession("Lang", getActivity()).equals("ar"))
                        CardnoEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_visa, 0, 0, 0);
                    else
                        CardnoEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.card_visa, 0);
                } else if (s.toString().length() >= 2) {
                    int prefix = Integer.parseInt(s.toString().substring(0, 2));
                    if (prefix >= 51 && prefix <= 55) {
                        if (SessionSave.getSession("Lang", getActivity()).equals("fa")||SessionSave.getSession("Lang", getActivity()).equals("ar"))
                            CardnoEdt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.card_master, 0, 0, 0);
                        else
                            CardnoEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.card_master, 0);
                    } else {
                        CardnoEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                } else {
                    CardnoEdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
        // To change the CVV number edit text value on focus.
        CvvEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    if (CvvEdt.getText().toString().contains("X"))
                        CvvEdt.setText("");
                } else {
                    if (CvvEdt.getText().length() <= 0) {
                        CvvEdt.setText(xCvv);
                    }
                }
            }
        });
        // To set the default card for payment on check.
        Defaultcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean checked) {
                // TODO Auto-generated method stub
                if (checked) {
                    checkChange(true);
                    DefaultCheck = 1;
                } else {
                    checkChange(false);
                    DefaultCheck = 0;
                }
            }
        });
        // To show the Slider menu for move from one activity to another activity.
        CancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HeadTitle.setText(NC.getResources().getString(R.string.payment));
                if (CarddetailLay.isShown()) {
                    CardnoTxt.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessage(1);
                    return;
                } else {
                    // menu.toggle();
                }
            }
        });

        // To add the passengers card details or edit the card details based on the boolean value.
        DoneBtn.setOnClickListener(new OnClickListener() {
            private String cardno;
            private String cvv;

            @Override
            public void onClick(View v) {

                try {

                    if (Add) {
                        cardno = (CardnoEdt.getText().toString().trim()).replaceAll("\\s", "");
                        cvv = CvvEdt.getText().toString().trim();
                        if (Type.equals("" + NC.getResources().getString(R.string.personalcard))) {
                            Type = "P";
                        } else if (Type.equals("" + NC.getResources().getString(R.string.businesscard))) {
                            Type = "B";
                        }
                        if (Type.equals("")) {
                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_card_type), "" + NC.getResources().getString(R.string.ok), "");
                        }
                        if (Validation.validations(Validation.ValidateAction.isValidCard, getActivity(), cardno))
                            if (Validation.validations(Validation.ValidateAction.isNullMonth, getActivity(), mMonth)) {
                                if (Validation.validations(Validation.ValidateAction.isNullYear, getActivity(), mYear)) {
                                    if (Validation.validations(Validation.ValidateAction.isValidCvv, getActivity(), cvv))
                                        if (!pay_termsTxt.isChecked())
                                            alert_view(getActivity(), "Message", "" + NC.getResources().getString(R.string.agree_the_terms_and_condition), "" + NC.getResources().getString(R.string.ok), "");
                                        else {
                                            try {
                                                JSONObject j = new JSONObject();
                                                j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                                                j.put("email", SessionSave.getSession("Email", getActivity()));
                                                j.put("creditcard_no", cardno);
                                                j.put("expdatemonth", (mMonth));
                                                j.put("expdateyear", mYear);
                                                j.put("creditcard_cvv", cvv);
                                                j.put("card_type", Type);
                                                j.put("default", DefaultCheck);
                                                j.put("card_holder_name", pay_cardnameEdt.getText().toString());
                                               //  Log.e("_r_", j.toString());

                                                String url = "type=add_card_details";
                                                new CreditCard(url, j);
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                e.printStackTrace();
                                            }
                                        }
                                }
                            }
                    } else {
                        if (CardnoTxt.getVisibility() == View.VISIBLE)
                            cardno = OriginalCard;
                        else
                            cardno = (CardnoEdt.getText().toString().trim()).replaceAll("\\s", "");

                      //  System.out.println("Pass Api" + cardno);
                        cvv = CvvEdt.getText().toString().trim();
                        if (Type.trim().equals("" + NC.getResources().getString(R.string.personalcard))) {
                            Type = "P";
                        } else if (Type.trim().equals("" + NC.getResources().getString(R.string.businesscard))) {
                            Type = "B";
                        }
                        if (Type.equals("")) {
                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_card_type), "" + NC.getResources().getString(R.string.ok), "");
                        }

                        if (Validation.validations(Validation.ValidateAction.isValidCard, getActivity(), cardno))
                            if (Validation.validations(Validation.ValidateAction.isNullMonth, getActivity(), mMonth)) {
                                if (Validation.validations(Validation.ValidateAction.isNullYear, getActivity(), mYear)) {
                                    if (Validation.validations(Validation.ValidateAction.isValidCvv, getActivity(), cvv)) {
                                        if (cardno.contains("X")) {
                                            cardno = OriginalCard;
                                        }
                                        if (!pay_termsTxt.isChecked())
                                            alert_view(getActivity(), "Message", "" + NC.getResources().getString(R.string.agree_the_terms_and_condition), "" + NC.getResources().getString(R.string.ok), "");
                                        else {
                                            try {
                                                JSONObject j = new JSONObject();
                                                j.put("passenger_cardid", cardId);
                                                j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                                                j.put("email", SessionSave.getSession("Email", getActivity()));
                                                j.put("creditcard_no", cardno);
                                                j.put("expdatemonth", (mMonth));
                                                j.put("expdateyear", mYear);
                                                j.put("creditcard_cvv", cvv);
                                                j.put("card_type", Type);
                                                j.put("default", DefaultCheck);
                                                j.put("card_holder_name", pay_cardnameEdt.getText().toString());
                                                String url = "type=edit_card_details";
                                                new CreditCard(url, j);
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                            }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        c = "" + NC.getResources().getString(R.string.personalcard) + "~" + NC.getResources().getString(R.string.businesscard) + "~";
        cardtype = c.split("~");
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < cardtype.length; i++) {
            arr.add(cardtype[i]);
        }
        card_adapter = new FontHelper.MySpinnerAdapter(getActivity(), R.layout.spinneritem_lay, arr);
        card_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        CardtypeSpn.setAdapter(card_adapter);
        // To select the card type from spinner.
        CardtypeSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                Colorchange.ChangeColor(parent, getActivity());
                Type = parent.getItemAtPosition(arg2).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // To select the expire date from date dialog.
        /*ExpiryEdt.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FragmentManager fm = getChildFragmentManager();
                datePicker_Dialog = new DatePicker_CardExpiry();
                Bundle bundle = new Bundle();
                if (ExpiryEdt.getText().toString() != null && ExpiryEdt.getText().toString().contains("/")) {
                    String[] date = ExpiryEdt.getText().toString().split("/");
                    bundle.putInt("month", Integer.parseInt(date[0]));
                    bundle.putInt("year", Integer.parseInt(date[1]));

                    Log.e("MY", date[0] + " " + date[1]);

                    datePicker_Dialog.setArguments(bundle);
                    System.out.println("_______1**" + Integer.parseInt(date[0]) + "__" + Integer.parseInt(date[1]));
                }
                datePicker_Dialog.show(fm, "datePicker");
                //showDialog(DATE_DIALOG_ID);
            }
        });*/
        // To get the passenger card details,Call the API after layout loaded.

        removecardBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (!Defaultcheck.isChecked())
                if (SessionSave.getSession("trip_id", getActivity()).equals("")) {

                    try {
                        final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
                        final Dialog mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                        mDialog.setContentView(view);
                        mDialog.setCancelable(false);
                        mDialog.show();
                        FontHelper.applyFont(getActivity(), mDialog.findViewById(R.id.alert_id));
                        final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
                        final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
                        final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
                        final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
                        title_text.setText("" + NC.getResources().getString(R.string.message));
                        message_text.setText("" + NC.getResources().getString(R.string.confirmdelete));
                        button_success.setText("" + NC.getResources().getString(R.string.yes));
                        button_failure.setText("" + NC.getResources().getString(R.string.no));
                        button_success.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                // TODO
                                // Auto-generated
                                // method stub
                                try {
                                    mDialog.dismiss();
                                    // TODO Auto-generated method stub
                                    try {
                                        JSONObject j = new JSONObject();
                                        j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                                        j.put("passenger_cardid", cardId);
                                        new Deletecard("type=credit_card_delete", j);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        });
                        button_failure.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                // TODO
                                // Auto-generated
                                // method stub
                                mDialog.dismiss();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getActivity(), NC.getResources().getString(R.string.Trip_in_progress), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void alert_views(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                    mHandler.sendEmptyMessage(1);
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


    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
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

    @Override
    public void onSuccess(int month, int year) {
        //  mYear = year;
        // mMonth = month;

       // Log.e("test ", String.valueOf(mYear) + " " + String.valueOf(mMonth));

        updateDisplay();
        datePicker_Dialog.dismiss();
    }

    public void showLoading(Context context) {
        View view = View.inflate(context, R.layout.progress_bar, null);
        mshowDialog = new Dialog(context, R.style.dialogwinddow);
        mshowDialog.setContentView(view);
        mshowDialog.setCancelable(false);
        mshowDialog.show();


        ImageView iv = (ImageView) mshowDialog.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);
    }

    @Override
    public void failure(String inputText) {
        datePicker_Dialog.dismiss();
    }

    /**
     * CreditCard class is used to request and process the response for both add and edit card API
     */

    private class CreditCard implements APIResult {
        public CreditCard(String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false, 3000).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        mHandler.sendEmptyMessage(1);
                    } else {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (Exception e) {
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), getString(R.string.server_con_error));

                    }
                });
            }
        }
    }

    /**
     * GetCardlist class is used to get the passenger card details and update it into UI
     */
    private class GetCardlist implements APIResult {
        public GetCardlist(String string, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(string);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    if (TaxiUtil.mCreditcardlist.size() != 0) {
                        TaxiUtil.mCreditcardlist.clear();
                    }
                    String id = "", type = "", month = "", year = "", card = "", cvv = "", default_card = "", original_cardno = "", original_cvv = "";
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        JSONArray jarry = json.getJSONArray("detail");
                        int length = jarry.length();
                        for (int i = 0; i < length; i++) {
                            id = jarry.getJSONObject(i).getString("passenger_cardid");
                            type = jarry.getJSONObject(i).getString("card_type");
                            month = jarry.getJSONObject(i).getString("expdatemonth");
                            year = jarry.getJSONObject(i).getString("expdateyear");
                            card = jarry.getJSONObject(i).getString("masked_creditcard_no");
                            cvv = jarry.getJSONObject(i).getString("masked_creditcard_cvv");
                            original_cardno = jarry.getJSONObject(i).getString("creditcard_no");
                            //original_cvv = jarry.getJSONObject(i).getString("creditcard_cvv");
                            original_cvv = jarry.getJSONObject(i).getString("creditcard_cvv");
                            default_card = jarry.getJSONObject(i).getString("default_card");

                            CreditCardData data = new CreditCardData(id, type, month, year, card, cvv, default_card, original_cardno, original_cvv);
                            data.setName(jarry.getJSONObject(i).getString("card_holder_name"));
                            TaxiUtil.mCreditcardlist.add(data);
                        }
                        CreditCardData data = new CreditCardData("", "", "", "", "" + NC.getResources().getString(R.string.addcard), "", "", "", "");
                        TaxiUtil.mCreditcardlist.add(data);
                    } else if (json.getInt("status") == 2) {
                        // CToast.ShowToast(getActivity(), json.getString("message"));
                        CreditCardData data = new CreditCardData("", "", "", "", "" + NC.getResources().getString(R.string.addcard), "", "", "", "");
                        TaxiUtil.mCreditcardlist.add(data);
                        SessionSave.saveSession("Credit_Card", "0", getActivity());
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.nocard), "" + NC.getResources().getString(R.string.ok), "");
                    }
                    if (TaxiUtil.mCreditcardlist.size() != 0) {
                        mHandler.sendEmptyMessage(0);
                        SessionSave.saveSession("Credit_Card", "1", getActivity());
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                        }
                    });
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void back_Clicked() {
        mHandler.sendEmptyMessage(1);
    }

    /**
     * Deletecard class is used to get the passenger card details and update it into UI
     */

    private class Deletecard implements APIResult {
        public Deletecard(String string, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(string);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        TaxiUtil.mCreditcardlist.remove(cardPos);

                        alert_views(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    } else {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                        }
                    });
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void checkChange(boolean on) {
        if (on) {
            ontxt.setText(NC.getString(R.string.ontxt));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.FILL_PARENT);
            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            params.setMargins(0, 0, 6, 0);
            ontxt.setLayoutParams(params);
            Defaultcheck.setBackgroundResource(R.drawable.on_btn);
            DefaultCheck = 1;
        } else {
            ontxt.setText(NC.getString(R.string.offtxt));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.FILL_PARENT);
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            params.setMargins(6, 0, 0, 0);
            ontxt.setLayoutParams(params);
            Defaultcheck.setBackgroundResource(R.drawable.off_btn);
            DefaultCheck = 0;
        }

    }

    /**
     * handler is used to update the UI change whenever user performed the ADD,EDIT and OPEN the particular card details.
     */

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 0) {
                List.setAdapter(adapter);
                List.setOnItemClickListener(new OnItemClickListener() {
                    private int position;

                    @SuppressLint("NewApi")
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
                        // TODO Auto-generated method stub
                        CardnoTxt.setVisibility(View.VISIBLE);
                        CancelTxt.setText(NC.getResources().getString(R.string.cancel));
                        CancelTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        if (!TaxiUtil.mCreditcardlist.get(pos).getCard().equals("" + NC.getResources().getString(R.string.addcard))) {
                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.edit_card));
                            HeadTitle.setText(NC.getResources().getString(R.string.payment));
                            ListLay.setVisibility(View.GONE);
                            DoneBtn.setVisibility(View.VISIBLE);
                            CarddetailLay.setVisibility(View.VISIBLE);
                            removecardBtn.setVisibility(View.VISIBLE);
                            // Toast.makeText(getActivity(), "hii", Toast.LENGTH_SHORT).show();
                            CardnoTxt.setText(NC.getString(R.string.cardnumber));
                            if (TaxiUtil.mCreditcardlist.size() == 2)
                                Defaultcheck.setEnabled(false);

                            Add = false;
                            DoneBtn.setText(NC.getResources().getString(R.string.save));
                            //  DoneBtn.setBackground(NC.getResources().getDrawable(R.drawable.draw_back_header_bgcolor));
                            mYear = TaxiUtil.mCreditcardlist.get(pos).getYear();
                            mMonth = TaxiUtil.mCreditcardlist.get(pos).getMonth();

                            xCardno = TaxiUtil.mCreditcardlist.get(pos).getCard();
                            CardnoTxt.setText(xCardno);
                            //  CardnoEdt.setText(xCardno);


                            //ExpiryEdt.setText(mMonth + "/" + mYear);
                            xCvv = TaxiUtil.mCreditcardlist.get(pos).getCvv();
                            CvvEdt.setText(xCvv);
                            OriginalCard = TaxiUtil.mCreditcardlist.get(pos).getOriginal_cardno();
                            cardId = TaxiUtil.mCreditcardlist.get(pos).getId();
                            cardPos = pos;
                            OriginalCvv = TaxiUtil.mCreditcardlist.get(pos).getOriginal_cvv();
                            if (TaxiUtil.mCreditcardlist.get(pos).getType().equals("P")) {
                                position = 0;
                            } else {
                                position = 1;
                            }
                            checkChange(false);
                            Defaultcheck.setChecked(false);
                            if (TaxiUtil.mCreditcardlist.get(pos).getDefault_card().equals("1")) {
                                Defaultcheck.setChecked(true);
                                checkChange(true);
                                removecardBtn.setVisibility(View.GONE);
                                Defaultcheck.setEnabled(false);

                            } else {
                                Defaultcheck.setEnabled(true);
                            }

                            try {
                                pay_monthtouched = true;
                                pay_yeartouched = true;
                                pay_monthspn.setSelection(Integer.parseInt(mMonth) - 1);
                                if (Integer.parseInt(mYear) > curyear)
                                    pay_yearspn.setSelection(Integer.parseInt(mYear) - curyear);
                                else
                                    Toast.makeText(getActivity(), NC.getString(R.string.your_card_expiry), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            pay_cardnameEdt.setText(TaxiUtil.mCreditcardlist.get(pos).getName());
                            CardtypeSpn.setSelection(position);
                        } else if (TaxiUtil.mCreditcardlist.get(pos).getCard().equals("" + NC.getResources().getString(R.string.addcard))) {
                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.add_card));
                            HeadTitle.setText(NC.getResources().getString(R.string.payment));
                            CardtypeSpn.setAdapter(card_adapter);
                            pay_cardnameEdt.setText("");
                            pay_monthspn.setSelection(0);
                            pay_yearspn.setSelection(0);
                            Type = "";
                            Add = true;
                            CardnoTxt.setText("");
                            //CardnoEdt.setText("");
                            // ExpiryEdt.setText("");
                            CvvEdt.setText("");
                            xCvv = "";
                            xCardno = "";
                            ListLay.setVisibility(View.GONE);
                            DoneBtn.setVisibility(View.VISIBLE);
                          //  System.out.println("*****" + adapter.getCount());
                            //changessss
                            if (adapter.getCount() != 1) {
                                Defaultcheck.setChecked(false);
                                Defaultcheck.setEnabled(true);
                                checkChange(false);
                            } else {
                                Defaultcheck.setChecked(true);
                                Defaultcheck.setEnabled(false);
                                checkChange(true);
                            }
                            CarddetailLay.setVisibility(View.VISIBLE);
                            CardnoTxt.setText(NC.getString(R.string.cardnumber));
                            Calendar cal = Calendar.getInstance();
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                            DoneBtn.setText(NC.getResources().getString(R.string.save));
                        }
                    }
                });
            }
            if (msg.what == 1) {
                ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.payment));
                DoneBtn.setVisibility(View.GONE);
                Add = false;
                try {
                    JSONObject j = new JSONObject();
                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                    j.put("card_type", "");
                    j.put("default", "");
                    new GetCardlist("type=get_credit_card_details", j);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                removecardBtn.setVisibility(View.GONE);
                CarddetailLay.setVisibility(View.GONE);
                ListLay.setVisibility(View.VISIBLE);
                HeadTitle.setText(NC.getResources().getString(R.string.payment));
                CancelTxt.setText(NC.getResources().getString(R.string.back));
                CancelTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_back, 0, 0, 0);
                ((MainHomeFragmentActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
            }
        }

        ;
    };

    /**
     * updateDisplay() method used to update UI from calendar dialog
     */

    private void updateDisplay() {

        // ExpiryEdt.setText(new StringBuilder().append(mMonth).append("/").append(mYear));
    }


    // The callback received when the user "sets" the date in the dialog
  /*  private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            updateDisplay();
        }
    };*/
    private DatePicker datePicker;

    // getActivity() custom dialog to get the calendar dialog.
//    public Dialog onCreateDialog(int id) {
//
//        switch (id) {
//            case DATE_DIALOG_ID:
//                final Calendar calendar = Calendar.getInstance();
//                mYear = calendar.get(Calendar.YEAR);
//                mMonth = calendar.get(Calendar.MONTH)+1;
//                mDay = calendar.get(Calendar.DAY_OF_MONTH);
//                final DatePickerDialog dojDPDilog = new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay);
//                SimpleDateFormat format1 = new SimpleDateFormat("EE dd MMM yyyy");
//
//
//                dojDPDilog.setTitle_m(format1.format(calendar.getTime()));
////                FontHelper.applyFont(getActivity(),dojDPDilog.g);
//                try {
//                    java.lang.reflect.Field mDatePickerField = dojDPDilog.getClass().getDeclaredField("mDatePicker");
//                    mDatePickerField.setAccessible(true);
//                    try {
//                        datePicker = (DatePicker) mDatePickerField.get(dojDPDilog);
//                        Field datePickerFields[] = mDatePickerField.getType().getDeclaredFields();
//                        for (Field datePickerField : datePickerFields) {
//                            if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName())) {
//                                datePickerField.setAccessible(true);
//                                Object dayPicker = new Object();
//                                dayPicker = datePickerField.get(datePicker);
//                                ((View) dayPicker).setVisibility(View.GONE);
//                            }
//                        }
//                    } catch (IllegalArgumentException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                }
//                datePicker.init(mYear, mMonth, mDay, new OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        if (year < calendar.get(Calendar.YEAR)) {
//                            view.updateDate(calendar.get(Calendar.YEAR), mMonth, mDay);
//                        }
//                        if (monthOfYear < calendar.get(Calendar.MONTH) && year == calendar.get(Calendar.YEAR)) {
//                            view.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), mDay);
//                        }
//                        if (dayOfMonth < calendar.get(Calendar.DAY_OF_MONTH) && year == calendar.get(Calendar.YEAR) && monthOfYear == calendar.get(Calendar.MONTH)) {
//                            view.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//                        }
//                    }
//                });
//                FontHelper.overrideFonts(getActivity(), dojDPDilog.getDatePicker());
//                //  View v=FontHelper.applyFont(getActivity(),dojDPDilog.getDatePicker(),0);
//                return dojDPDilog;
//        }
//        return null;
//    }

    // To show the slider menu for move from one activity to another activity.


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(getActivity());

        if (mshowDialog != null && mshowDialog.isShowing()) {
            mshowDialog.dismiss();
            mshowDialog = null;

        }
        super.onDestroy();
    }

    /**
     * To show the terms and condtion webpage
     */
    private void TermsConditions() {

        try {
            //			JSONObject j = new JSONObject();
            //			j.put("pagename", "termsconditions");
            //			j.put("device_type", "1");
            String url = "&type=dynamic_page&pagename=3&device_type=1";
            new ShowWebpage(url, null);
            //			new ShowWebpage("type=dynamic_page", j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private class ShowWebpage implements APIResult {
        public ShowWebpage(final String string, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, true, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&encode=" + SessionSave.getSession("encode", getActivity()) + string).execute();

            //  new APIService_Retrofit_JSON(getActivity(), this,  true).execute(TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", getActivity()) + string);
//            new APIService_HTTP_JSON(getActivity(), this, data, false, TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + string).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            try {
                if (isSuccess) {
                    final Intent intent = new Intent(getActivity(), TermsAndConditions.class);
                    final Bundle bundle = new Bundle();
                    intent.putExtra("content", result);
                    bundle.putString("name", NC.getString(R.string.termcond));
                    bundle.putBoolean("status", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                        }
                    });
//                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + result, "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);


    }

    public void onBackPressed() {
        if (ListLay.getVisibility() == View.GONE)
            mHandler.sendEmptyMessage(1);
        else {
            getActivity().getSupportFragmentManager().beginTransaction().remove((getActivity()).getSupportFragmentManager().findFragmentByTag("PaymentOptionFrag")).commit();
            ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
            ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");
            ((MainHomeFragmentActivity) getActivity()).toolbar.setVisibility(View.GONE);
        }
    }
}