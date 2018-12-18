package com.cabipassenger.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.ReceiptAct;
import com.cabipassenger.TripDetailsAct;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by developer on 19/6/18.
 */

public class Paymentgetway extends Activity {

    private String message;
    private String f_tripid;
    private String f_distance;
    private String f_metric;
    private String f_totalfare;
    private String f_nightfareapplicable;
    private String f_nightfare;
    private String f_eveningfare_applicable = "0";

    private String f_eveningfare;
    private String f_pickup = "", drop_location = "";
    private String f_waitingtime;
    private String f_waitingcost;
    private String f_waitingmin;
    private String f_taxamount;
    private String f_tripfare;
    private String f_creditcard;
    private String f_farediscount = "";
    private String promotax = "";
    private String promoamt = "";
    private String f_paymodid = "";
    private String p_dis = "";
    private String f_walletamt = "";
    private String f_payamt = "";
    private double m_distance;
    private double m_tripfare;
    private double m_totalfare;
    private double m_taxamount;
    private double m_waitingcost;
    private double m_walletamt;
    private double m_payamt;
    private double f_fare;
    private double f_tips;
    private double f_total;
    private EditText farecalTxt;
    private EditText tipsTxt;
    private TextView HeadTitle;
    //    private TextView paycashTxt;
//    private TextView paycardTxt;
//    private TextView payuncardTxt;
    //   private TextView accountTxt;
    private TextView totalamountTxt;
    private TextView actdistanceTxt;
    private TextView metricTxt;
    private TextView promopercentTxt;
    private TextView b_farecalCurrency;
    private TextView b_tipsCurrency;
    private TextView b_pickuplocation;
    private TextView b_droplocation;
    private TextView b_total_amt_curency;
    private TextView b_waitingcost, b_tax, b_discount, b_roundtrip, v_trip_fare;
    private TextView remarks;
    private TextView walletamountTxt;
    private TextView amountpayTxt;
    private TextView idwaitingcost;
    private Dialog mDialog;
    private LinearLayout lay_fare;
    private LinearLayout walletlay;
    private LinearLayout paylay;
    private boolean istipsTxt_focus = false;
    private String cmpTax = "";
    private LinearLayout promoLayout, tax_lay;
    private TextView txtCmp, slideImg;// backup;
    public static Activity mFlagger;
    Intent details;
    private String promodiscount_amount;
    RadioButton radiowalletButton;
    TextView radiocashButton, radiocardButton, radiouncardButton;
    private String f_minutes_traveled;
    private String f_minutes_fare;
    private String Cvv;
    View vid_discount;
    private LinearLayout noWallet;
    private boolean isArab;
    private String base_fare = "";
    private boolean fromStreetPickUp;
    private String fare_calculation_type = "3";
    private LinearLayout distance_lay, minutes_lay;
    private TextView minutes_value;
    private TextView walletamountCurrency;
    private TextView amountpayCurrency;
    private LinearLayout eve_fare_lay;
    TextView eve_fare;
    private TextView paynow;
    public static int Payment_status = 0;
    private boolean doubleBackToExitPressedOnce = false;
    private String telr_payment_gateway_url = "";

    public static boolean paymentstatus = false;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teler_payment);


        FontHelper.applyFont(this, findViewById(R.id.id_farelay));
        b_pickuplocation = (TextView) findViewById(R.id.pickuplocTxt);
        b_droplocation = (TextView) findViewById(R.id.droplocTxt);
        b_farecalCurrency = (TextView) findViewById(R.id.farecalCurrency);
        actdistanceTxt = (TextView) findViewById(R.id.actdistanceTxt);
        metricTxt = (TextView) findViewById(R.id.metricTxt);
        b_tipsCurrency = (TextView) findViewById(R.id.tipsCurrency);
        farecalTxt = (EditText) findViewById(R.id.farecalTxt);
        tipsTxt = (EditText) findViewById(R.id.tipsTxt);
        tipsTxt.setText("0");

        HeadTitle = (TextView) findViewById(R.id.headerTxt);
        distance_lay = (LinearLayout) findViewById(R.id.distance_lay);
        minutes_lay = (LinearLayout) findViewById(R.id.minutes_lay);

        minutes_value = (TextView) findViewById(R.id.min_value);
        eve_fare_lay = (LinearLayout) findViewById(R.id.eve_fare_lay);
        eve_fare = (TextView) findViewById(R.id.eve_fare);
//        paycardTxt = (TextView) findViewById(R.id.paycard);
//        payuncardTxt = (TextView) findViewById(R.id.payuncard);
        // accountTxt = (TextView) findViewById(R.id.payaccount);
        totalamountTxt = (TextView) findViewById(R.id.totalamountTxt);
        promopercentTxt = (TextView) findViewById(R.id.promopercentage);
        walletamountTxt = (TextView) findViewById(R.id.walletamountTxt);
        walletamountCurrency = (TextView) findViewById(R.id.walletamountCurrency);
        amountpayCurrency = (TextView) findViewById(R.id.amountpayCurrency);
        amountpayTxt = (TextView) findViewById(R.id.amountpayTxt);
        tax_lay = (LinearLayout) findViewById(R.id.tax_lay);
        txtCmp = (TextView) findViewById(R.id.txtcmpTax);
        //  slideImg = (TextView) findViewById(R.id.slideImg);
        //backup = (TextView) findViewById(R.id.backup);
        walletlay = (LinearLayout) findViewById(R.id.walletlay);
        paylay = (LinearLayout) findViewById(R.id.paylay);
        //  slideImg.setVisibility(View.VISIBLE);
        // slideImg.setText("");
        //backup.setVisibility(View.GONE);
        promoLayout = (LinearLayout) findViewById(R.id.discountlayout);
        lay_fare = (LinearLayout) findViewById(R.id.lay_fare);
        remarks = (TextView) findViewById(R.id.remarks);
        b_total_amt_curency = (TextView) findViewById(R.id.toatalamtCurrency);
        b_waitingcost = (TextView) findViewById(R.id.waitingcost);
        idwaitingcost = (TextView) findViewById(R.id.idwaitingcost);
        b_tax = (TextView) findViewById(R.id.tax);
        b_discount = (TextView) findViewById(R.id.discount);
        b_roundtrip = (TextView) findViewById(R.id.roundtrip);
        v_trip_fare = (TextView) findViewById(R.id.v_trip_fare);
        vid_discount = (View) findViewById(R.id.vid_discount);

        paynow = (TextView) findViewById(R.id.dtatus_check);

      /*  paynow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ShowToast(Paymentgetway.this, "status false");
                return true;
            }
        });*/

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!telr_payment_gateway_url.equals("")) {
                    Intent webview = new Intent(Paymentgetway.this, Paymentgetwaywebview.class);
                    webview.putExtra("telr_payment_gateway_url", telr_payment_gateway_url);
                    startActivity(webview);
                    // finish();


                } else {
                    ShowToast(Paymentgetway.this, "status false");
                }
            }
        });

        String tripid = SessionSave.getSession("trip_id", Paymentgetway.this);
        String url = "type=initiate_telrpayment";
        new Setfare(url, tripid);

    }


    private class Setfare implements APIResult {
        public Setfare(String url, String tripid) {

            try {
                JSONObject j = new JSONObject();
                j.put("trip_id", tripid);
                new APIService_Retrofit_JSON(getApplicationContext(), this, j, false).execute(url);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            try {

                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {

                        telr_payment_gateway_url = json.getString("telr_payment_gateway_url");

                        setFareCalculatorScreen(result);

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            // System.out.println("payment result" + "" + result);

        }
    }

    private void setFareCalculatorScreen(String message) {

        /*if (details != null) {*/
        try {
            JSONObject obj = new JSONObject(message);
            JSONObject json = obj.getJSONObject("detail");
            f_tripid = json.getString("trip_id");
            f_distance = json.getString("distance");
            f_metric = json.getString("metric");
            f_totalfare = json.getString("subtotal_fare");
            f_nightfareapplicable = json.getString("nightfare_applicable");
            f_nightfare = json.getString("nightfare");
            f_eveningfare_applicable = json.getString("eveningfare_applicable");
            f_eveningfare = json.getString("eveningfare");
            f_pickup = json.getString("pickup");
            drop_location = json.getString("drop");
            f_waitingtime = json.getString("waiting_time");
            f_waitingcost = json.getString("waiting_cost");
            f_waitingmin = json.getString("waiting_cost");
            f_taxamount = json.getString("tax_amount");
            f_tripfare = json.getString("trip_fare");
            f_payamt = json.getString("total_fare");
            f_walletamt = json.getString("wallet_amount_used");
            f_minutes_traveled = json.getString("minutes_traveled");
            f_minutes_fare = json.getString("minutes_fare");
            f_creditcard = json.getString("credit_card_status");
            f_farediscount = json.getString("promodiscount_amount");
            base_fare = json.getString("base_fare");
            cmpTax = json.getString("company_tax");
            if (f_eveningfare_applicable.equalsIgnoreCase("1")) {
                eve_fare.setText("" + f_eveningfare);
                eve_fare_lay.setVisibility(View.VISIBLE);
            }
            try {

                fare_calculation_type = json.getString("fare_calculation_type");
                if (fare_calculation_type.trim().equals("1"))
                    minutes_lay.setVisibility(View.GONE);
                else if (fare_calculation_type.trim().equals("2"))
                    distance_lay.setVisibility(View.GONE);


            } catch (JSONException e) {
                e.printStackTrace();
            }

                /**/

            if (f_walletamt.length() != 0)
                m_walletamt = Double.parseDouble(f_walletamt);
            f_walletamt = String.format(Locale.UK, "%.2f", m_walletamt);
            if (f_payamt.length() != 0)
                m_payamt = Double.parseDouble(f_payamt);
            f_payamt = String.format(Locale.UK, "%.2f", m_payamt);
            if (f_waitingcost.length() != 0)
                m_waitingcost = Double.parseDouble(f_waitingcost);
            f_waitingcost = String.format(Locale.UK, "%.2f", m_waitingcost);
            if (f_totalfare.length() != 0)
                m_totalfare = Double.parseDouble(f_totalfare);
            f_totalfare = String.format(Locale.UK, "%.2f", m_totalfare);
            if (f_distance.length() != 0)
                m_distance = Double.parseDouble(f_distance);
            f_distance = String.format(Locale.UK, "%.2f", m_distance);
            if (f_tripfare.length() != 0)
                m_tripfare = Double.parseDouble(f_tripfare);
            f_tripfare = String.format(Locale.UK, "%.2f", m_tripfare);
            if (f_taxamount.length() != 0)
                m_taxamount = Double.parseDouble(f_taxamount);
            f_taxamount = String.format(Locale.UK, "%.2f", m_taxamount);
            idwaitingcost.setText("" + NC.getResources().getString(R.string.waiting_cost) + "(" + f_waitingtime + ")");
            if (!cmpTax.trim().equals("0"))
                txtCmp.setText("" + getResources().getString(R.string.tax) + cmpTax + "" + getResources().getString(R.string.tax_percent));
            else
                tax_lay.setVisibility(View.GONE);
            farecalTxt.setText(f_totalfare);
//                if(isArab)
//                    v_trip_fare.setText("" +f_tripfare+" "+ SessionSave.getSession("site_currency", FarecalcAct.this) );
//                else
            v_trip_fare.setText("" + SessionSave.getSession("site_currency", Paymentgetway.this) + " " + f_tripfare);
            p_dis = json.getString("promo_discount_per");
            promodiscount_amount = json.getString("promo_discount_per");
            if (!p_dis.trim().equals("")) {
                if (!promodiscount_amount.equals("0") && json.getString("promo_type").equals("2"))
                    promopercentTxt.setText("" + NC.getResources().getString(R.string.discount) + "(" + promodiscount_amount + "" + NC.getResources().getString(R.string.tax_percent));
                else
                    promopercentTxt.setText("" + NC.getResources().getString(R.string.discount));
//                    if(isArab)
//                        b_discount.setText("" +f_farediscount+ " " +SessionSave.getSession("site_currency", FarecalcAct.this) );
//                    else
                b_discount.setText("" + SessionSave.getSession("site_currency", Paymentgetway.this) + " " + f_farediscount);
                promoLayout.setVisibility(View.VISIBLE);
                vid_discount.setVisibility(View.GONE);
            } else {
                promoLayout.setVisibility(View.GONE);
                vid_discount.setVisibility(View.GONE);
            }
            if (promoamt.equals("0")) {
                promoLayout.setVisibility(View.GONE);
            }
            metricTxt.setText(f_metric);
            actdistanceTxt.setText("" + f_distance);
            minutes_value.setText(f_minutes_traveled + " " + NC.getString(R.string.mins));
            b_pickuplocation.setText(f_pickup);
//                if (SessionSave.getSession("drop_location", FarecalcAct.this).length() != 0)
//                    b_droplocation.setText("" + SessionSave.getSession("drop_location", FarecalcAct.this));
//                else
            b_droplocation.setText("" + drop_location);
            b_total_amt_curency.setText("" + SessionSave.getSession("site_currency", Paymentgetway.this) + " ");
//                if(!isArab)
            b_waitingcost.setText("" + SessionSave.getSession("site_currency", Paymentgetway.this) + " " + f_waitingcost);
//                else
            //                b_waitingcost.setText(""  + f_waitingcost+" "+ SessionSave.getSession("site_currency", FarecalcAct.this) );
//                if(!isArab)
            b_tax.setText("" + SessionSave.getSession("site_currency", Paymentgetway.this) + " " + f_taxamount);
            //  else
            //    b_tax.setText("" +f_taxamount+" "+SessionSave.getSession("site_currency", FarecalcAct.this) );
            b_roundtrip.setText("" + json.getString("roundtrip"));
            tipsTxt.setHint("0");
            //  remarks.setText("" + objLocationDb.getdistance(f_tripid));
            if (SessionSave.getSession("site_currency", Paymentgetway.this) != null) {
                b_farecalCurrency.setText(SessionSave.getSession("site_currency", Paymentgetway.this) + " ");
                b_tipsCurrency.setText(SessionSave.getSession("site_currency", Paymentgetway.this) + " ");
            }
            f_fare = m_totalfare;
            if (tipsTxt.length() != 0) {
                f_tips = Double.parseDouble(Uri.decode(tipsTxt.getText().toString()));
            }
            f_total = f_fare + f_tips;
            totalamountTxt.setText("" + String.format(Locale.UK, "%.2f", f_total));
            /*JSONArray ary = new JSONArray(json.getString("gateway_details"));
            // the following code for handle the payment mode dynamically.
            int length = ary.length();*/

            walletamountCurrency.setText(SessionSave.getSession("site_currency", Paymentgetway.this) + " ");
            amountpayCurrency.setText(SessionSave.getSession("site_currency", Paymentgetway.this) + " ");
            if (m_walletamt > 0) {
                walletlay.setVisibility(View.VISIBLE);
                walletamountTxt.setText(f_walletamt);
                paylay.setVisibility(View.VISIBLE);
                amountpayTxt.setText(f_payamt);
            }
            amountpayTxt.setText(f_payamt);


        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* }*/
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (Payment_status == 1) {
            showDialog();
            String url = "type=check_payment_status";
            try {
                JSONObject j = new JSONObject();
                j.put("trip_id", f_tripid);
                new Check_paymentstatus(url, j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private class Check_paymentstatus implements APIResult {
        String msg = "";

        public Check_paymentstatus(String url, JSONObject data) {

            if (NetworkStatus.isOnline(Paymentgetway.this)) {
                new APIService_Retrofit_JSON(Paymentgetway.this, this, data, false).execute(url);
            } else {
                alert_view(Paymentgetway.this, "" + NC.getResources().getString(R.string.message), "" + getResources().getString(R.string.change_network), "" + NC.getResources().getString(R.string.ok), "");
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            if (isSuccess) {
                closeDialog();
                try {
                    JSONObject json = new JSONObject(result);
                    msg = json.getString("message");

                    if (json.getString("status").equals("1")) {
                        paymentstatus = true;

                        SessionSave.saveSession("Pass_Tripid", "", Paymentgetway.this);
                        SessionSave.saveSession("trip_id", "", Paymentgetway.this);
                        // showLoading(ReceiptAct.this);
                        Intent i = new Intent(Paymentgetway.this, MainHomeFragmentActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                        finish();

                    } else {
                        ShowToast(Paymentgetway.this, msg);
                    }


                } catch (Exception e) {
                    closeDialog();
                    e.printStackTrace();
                }
            } else {
                closeDialog();
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(Paymentgetway.this, getString(R.string.server_error));
                    }
                });
                lay_fare.setVisibility(View.VISIBLE);
            }
        }

    }


    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(Paymentgetway.this)) {
                View view = View.inflate(Paymentgetway.this, R.layout.progress_bar, null);
                mDialog = new Dialog(Paymentgetway.this, R.style.dialogwinddow);
                mDialog.setContentView(view);
                mDialog.setCancelable(false);
                mDialog.show();

                ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(Paymentgetway.this)
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);

            } else {

            }
        } catch (Exception e) {

        }

    }

    public void closeDialog() {
        try {
            if (mDialog != null)
                if (mDialog.isShowing())
                    mDialog.dismiss();
        } catch (Exception e) {

        }
    }

    public Dialog alertDialog;

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        if (alertDialog != null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        final View view = View.inflate(mContext, R.layout.alert_view, null);

        alertDialog = new Dialog(mContext, R.style.NewDialog);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        FontHelper.applyFont(mContext, alertDialog.findViewById(R.id.alert_id));
        Colorchange.ChangeColor((ViewGroup) alertDialog.findViewById(R.id.alert_id), mContext);

        alertDialog.show();
        final TextView title_text = (TextView) alertDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) alertDialog.findViewById(R.id.message_text);
        final Button button_success = (Button) alertDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) alertDialog.findViewById(R.id.button_failure);
        button_failure.setVisibility(View.GONE);
        title_text.setText(title);
        message_text.setText(message);
        button_success.setText(success_txt);
        button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
//

    }


    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
            return;
        } else {
            doubleBackToExitPressedOnce = false;
            Toast.makeText(this, "Please Complete the Payment. Cannot Exit", Toast.LENGTH_SHORT).show();

        }

    }

    public void ShowToast(Context contex, String message) {

        Toast toast = Toast.makeText(contex, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}