package com.cabipassenger;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.DialogInterface;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.Dialog_Common;
import com.cabipassenger.util.DotsProgressBar;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * this class is used to request taxi
 *
 * @author developer
 */
public class ContinousRequest extends MainActivity implements DialogInterface {
    // Class members declarations.
    TextView Cancel, headertxt, request_txt;
    private DotsProgressBar progressBar;
    private long startTime = 90 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer1;
    private int toaststatus = 0;
    private String result;
    InputStream in;
    LinearLayout leftIconTxt, loading_contain;
    private String json = "", url = "";

    // Set the layout to activity.
    @Override
    public int setLayout() {
        setLocale();
        return R.layout.loading;
    }

    // Initialize the views on layout
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void Initialize() {
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), ContinousRequest.this);
        try {
            json = getIntent().getStringExtra("json");
            url = getIntent().getStringExtra("url");
        } catch (Exception e) {
            e.printStackTrace();
            SessionSave.saveSession("trip_id", "", ContinousRequest.this);
            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
            getApplicationContext().stopService(intent);
            finish();
        }

        TaxiUtil.sContext = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Cancel = (TextView) findViewById(R.id.ReqcancelTxt);
        headertxt = (TextView) findViewById(R.id.header_titleTxt);
        leftIconTxt = (LinearLayout) findViewById(R.id.leftIconTxt);
        leftIconTxt.setVisibility(View.INVISIBLE);
        loading_contain = (LinearLayout) findViewById(R.id.loading_contain);
        headertxt.setText(NC.getResources().getString(R.string.book_taxi));
        request_txt = (TextView) findViewById(R.id.request_txt);
        progressBar = (DotsProgressBar) findViewById(R.id.dotsProgressBar);
        progressBar.setDotsCount(3);
        TaxiUtil.current_act = "ContinousRequest";
        FontHelper.applyFont(this, findViewById(R.id.loading_contain));

        if (!SessionSave.getSession("request_time", ContinousRequest.this).equalsIgnoreCase("") && !SessionSave.getSession("request_time", ContinousRequest.this).equalsIgnoreCase(null))
            startTime = (Long.parseLong(SessionSave.getSession("request_time", ContinousRequest.this))) * 1000;
        countDownTimer1 = new CountDownTimer(startTime, interval) {
            int time = 1;

            @Override
            public void onTick(final long millisUntilFinished_) {
                final long sec = millisUntilFinished_ / 1000;
                time++;
            }

            @Override
            public void onFinish() {
                try {
                    if (!SessionSave.getSession("trip_id", ContinousRequest.this).equals("")) {
                        JSONObject j = new JSONObject();
                        j.put("passenger_tripid", SessionSave.getSession("trip_id", ContinousRequest.this));
                        new CancelTrip("type=getdriver_reply", j);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();


                }
            }
        }.start();

        // this api initiate the continuous request to the driver.
        try {
            JSONObject j = new JSONObject();
            j.put("passenger_tripid", SessionSave.getSession("trip_id", ContinousRequest.this));
            j.put("cancel_type", "0");
            final String url = "type=getdriver_update";
            new Getdriver(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            SessionSave.saveSession("trip_id", "", ContinousRequest.this);
            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
            getApplicationContext().stopService(intent);
            finish();
        }
        Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                try {
                    request_txt.setText("" + NC.getResources().getString(R.string.canceling_taxi));
                    Cancel.setClickable(false);
                    JSONObject j = new JSONObject();
                    j.put("passenger_tripid", SessionSave.getSession("trip_id", ContinousRequest.this));
                    final String url = SessionSave.getSession("base_url", ContinousRequest.this) + SessionSave.getSession("api_key", ContinousRequest.this) + "/?" + "lang=" + SessionSave.getSession("Lang", ContinousRequest.this) + "&" + "type=getdriver_reply" + "&" + "encode=" + SessionSave.getSession("encode", ContinousRequest.this);
                    new Getpassenger("type=getdriver_reply", j);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    clearIdAndRedirect();
                }
                SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                Intent intent = new Intent(getApplicationContext(), ContinousRequest.class);
                getApplicationContext().stopService(intent);
                finish();
                final Intent i = new Intent(ContinousRequest.this, MainHomeFragmentActivity.class);
                //  Bundle extras = new Bundle();
                // extras.putString("alert_message", "" + json.getString("message"));
                //i.putExtras(extras);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    public void clearIdAndRedirect() {
        if (ContinousRequest.this != null) {
            SessionSave.saveSession("trip_id", "", ContinousRequest.this);
            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
            getApplicationContext().stopService(intent);
            finish();
        }
    }

    public class Getpassenger implements APIResult {
        public Getpassenger(String url, JSONObject j) {
            new APIService_Retrofit_JSON(ContinousRequest.this, this, j, false, 3000).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            if (isSuccess) {
                if (!result.equalsIgnoreCase("")) {
                    try {
                        final JSONObject json = new JSONObject(result);
                        if (json.getInt("status") == 3) {
                            countDownTimer1.cancel();
                            if (toaststatus == 0) {
                                ShowToast(ContinousRequest.this, json.getString("message"));
                                toaststatus = 1;
                            }
                            SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                            getApplicationContext().stopService(intent);
                            finish();

                        } else {
                            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                            getApplicationContext().stopService(intent);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        clearIdAndRedirect();
                    }
                }
            } else {
                SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                getApplicationContext().stopService(intent);
                finish();
            }
        }
    }


    @Override
    public void onSuccess(View view, Dialog dialog, String resultcode) {
        if (resultcode.equals("No_fav_avail")) {
            if (!(json.equals("") || url.equals(""))) {
                JSONObject jj = null;
                try {
                    jj = new JSONObject(json);
                    jj.remove("fav_driver_booking_type");
                    jj.put("fav_driver_booking_type", "0");
                } catch (JSONException e) {
                    SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                    Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                    getApplicationContext().stopService(intent);
                    finish();
                    e.printStackTrace();
                }

                new SearchTaxi(url, jj);
            }
        }//else if(resultcode.equals())
        dialog.dismiss();
    }

    @Override
    public void onFailure(View view, Dialog dialog, String resultcode) {
        dialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), ContinousRequest.class);
        getApplicationContext().stopService(intent);
        finish();
        clearIdAndRedirect();
    }

    /**
     * continuous request response handling.
     */
    public class Getdriver implements APIResult {
        public Getdriver(final String url, JSONObject data) {
            //TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", ContinousRequest.this) + "&" + url
            new APIService_Retrofit_JSON_NoProgress(ContinousRequest.this, this, data, false, 3000).execute(url);
            // new APIService_Volley_JSON_No_Progress(ContinousRequest.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            System.out.println("SSS_rrrv" + isSuccess + "RR" + result);
            if (isSuccess) {
                try {

                    System.out.println("-----driver_update" + result);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("trip_id", json.getString("trip_id"), ContinousRequest.this);
                        // ShowToast(ContinousRequest.this, json.getString("message"));
                        countDownTimer1.cancel();
                        Intent intent = new Intent(ContinousRequest.this, GetPassengerUpdate.class);
                        ContinousRequest.this.startService(intent);
                        final Intent i = new Intent(ContinousRequest.this, MainHomeFragmentActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", "" + json.getString("message"));
                        i.putExtras(extras);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == 2) {
                        if (toaststatus == 0) {
                            // ShowToast(ContinousRequest.this, json.getString("message"));
                            toaststatus = 1;
                        }
                        SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                        countDownTimer1.cancel();
                        final Intent i = new Intent(ContinousRequest.this, MainHomeFragmentActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", "" + json.getString("message"));
                        i.putExtras(extras);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                    } else if (json.getInt("status") == 4) {
                        new Dialog_Common().setmCustomDialog(ContinousRequest.this, ContinousRequest.this, NC.getResources().getString(R.string.message), json.getString("message"),
                                NC.getResources().getString(R.string.ok),
                                NC.getResources().getString(R.string.no_thanks), "No_fav_avail");

                    }
                } catch (final Exception e) {
                    SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                    Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                    getApplicationContext().stopService(intent);
                    finish();
                    e.printStackTrace();
                }
            } else {
                System.out.println("SSS_rrrv1" + isSuccess + "RR" + result);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(ContinousRequest.this, getString(R.string.server_con_error));
                    }
                });
                clearIdAndRedirect();
            }
        }
    }

    /**
     * continuous request cancel response handling.
     */

    private class CancelTrip implements APIResult {
        public CancelTrip(final String string, JSONObject data) {
            new APIService_Retrofit_JSON_NoProgress(ContinousRequest.this, this, data, false).execute(string);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 3) {
                        countDownTimer1.cancel();
                        if (toaststatus == 0) {
                            //ShowToast(ContinousRequest.this, json.getString("message"));
                            toaststatus = 1;
                        }
                        SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                        Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getApplicationContext().stopService(intent);
//                        final Intent i = new Intent(ContinousRequest.this, MainHomeFragmentActivity.class);
//                        startActivity(i);
//                        finish();
                        final Intent i = new Intent(ContinousRequest.this, MainHomeFragmentActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("alert_message", "" + json.getString("message"));
                        i.putExtras(extras);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                    }
                } catch (final Exception e) {
                    SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                    Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                    getApplicationContext().stopService(intent);
                    finish();
                }
            } else {
                System.out.println("SSS_rrrv2" + isSuccess + "RR" + result);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(ContinousRequest.this, getString(R.string.server_con_error));
                    }
                });
                SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                getApplicationContext().stopService(intent);
                finish();
            }
        }
    }

    /**
     * this class is used to search taxi
     *
     * @author developer
     */
    public class SearchTaxi implements APIResult {
        String msg = "";
        String url = "";

        /**
         * this method is used to call SearchTaxi api
         *
         * @param url  passing url as a parameter
         * @param data passing jsonobject as parameter
         *             <p/>
         *             this method is invoked while calling this method with these params
         */
        public SearchTaxi(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(ContinousRequest.this, this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", ContinousRequest.this) + "&" + url).execute();
            // new APIService_Retrofit_JSON(ContinousRequest.this, this, data, false).execute(url);
            this.url = url;
            System.out.println("url---" + url + "--" + data);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            System.out.println("SSS_rrr" + isSuccess + "RR" + result);
            if (isSuccess) {
                try {

                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("trip_id", json.getJSONObject("detail").getString("passenger_tripid"), ContinousRequest.this);
                        SessionSave.saveSession("Pass_Tripid", json.getJSONObject("detail").getString("passenger_tripid"), ContinousRequest.this);
                        SessionSave.saveSession("request_time", json.getJSONObject("detail").getString("total_request_time"), ContinousRequest.this);
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), ContinousRequest.this);
                        //ApproxiTime.setText("");
                        // handlerServercall.removeCallbacks(runnableServercall);


                        final Intent i = new Intent(ContinousRequest.this, ContinousRequest.class);
                        i.putExtra("url", url);
                        i.putExtra("json", result);
                        startActivity(i);
                        finish();


                    } else if (json.getInt("status") == 2 || json.getInt("status") == 5 || json.getInt("status") == 6 || json.getInt("status") == 3) {
                        alert_view(ContinousRequest.this, "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        final Intent i = new Intent(ContinousRequest.this, MainHomeFragmentActivity.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                    }

                } catch (final Exception e) {
                    SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                    Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                    getApplicationContext().stopService(intent);
                    finish();
                    e.printStackTrace();
                }
            } else {
                System.out.println("SSS_rrrv3" + isSuccess + "RR" + result);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(ContinousRequest.this, getString(R.string.server_con_error));
                    }
                });
                SessionSave.saveSession("trip_id", "", ContinousRequest.this);
                Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
                getApplicationContext().stopService(intent);
                finish();
            }
        }
    }
}