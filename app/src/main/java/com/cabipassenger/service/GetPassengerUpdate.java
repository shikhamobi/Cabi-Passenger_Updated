package com.cabipassenger.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.ReceiptAct;
import com.cabipassenger.data.SplitStatusData;
import com.cabipassenger.fragments.SplitFareStatusDialog;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.Paymentgetway;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author developer This class used to get the trip status as notification. Continuously hit the server with fixed time interval and get the status about ongoing trip, generate that as notification.Based on the trip status this will redirect the control to different activity.
 */
public class GetPassengerUpdate extends Service {
    private final Timer mTimer = new Timer();
    int getdetailtimer = 1000 * 4;
    private InputStream in;
    private int mStatus;
    String result;
    public static MainHomeFragmentActivity context;
    Notification notification;
    private int Notification_ID = 1;
    private NotificationManager notificationManager;
    private String driver_latitute;
    private String driver_longtitute;
    private String msg;
    private String jsonString;
    public static int currentshown;
    Handler handler;

    String Dlocationname = "";
    Double Dlat = 0.0;
    Double Dlon = 0.0;
    IBinder binder = new MyBinder();

    class MyBinder extends Binder {

        GetPassengerUpdate getService() {
            return GetPassengerUpdate.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);


        try {
            Bundle b = intent.getExtras();

            if (b != null) {
                Dlocationname = b.getString("Dlname");
                Dlat = b.getDouble("Dlatitude");
                Dlon = b.getDouble("Dlongitude");
            } else {
                Dlocationname = "";
                Dlat = 0.0;
                Dlon = 0.0;
            }
        } catch (Exception e) {
            Dlocationname = "";
            Dlat = 0.0;
            Dlon = 0.0;
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        mTimer.scheduleAtFixedRate(new getpassengerupdates(), 0, getdetailtimer);
    }

    @Override
    public void onDestroy() {
        cancelNotify();
        mTimer.cancel();
        super.onDestroy();
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    private class getpassengerupdates extends TimerTask {
        @Override
        public void run() {
            try {
                //   TaxiUtil.DYNAMIC_AUTH_KEY=SessionSave.getSession("encode",GetPassengerUpdate.this);
                TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", GetPassengerUpdate.this);
                //TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", GetPassengerUpdate.this);
                JSONObject j = new JSONObject();
                j.put("trip_id", SessionSave.getSession("trip_id", GetPassengerUpdate.this));
                j.put("request_type", "0");
                j.put("drop_lat", Dlat);
                j.put("drop_long", Dlon);
                j.put("drop_location_name", Dlocationname);
                j.put("passenger_id", SessionSave.getSession("Id", GetPassengerUpdate.this));
                //   Log.e("LocationUpdate: ", j.toString());

                String url1 = "type=getpassenger_update";
                String url = TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", GetPassengerUpdate.this) + "&" + url1 + "&encode=" + TaxiUtil.DYNAMIC_AUTH_KEY;
                //  Log.v("Pass service response", "" + url + "___" + j.toString());
                new Getpassenger(url1, j);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.small_logo : R.drawable.ic_launcher;
    }

    @SuppressWarnings("deprecation")
    public void generateNotification(Context context, String message, Class<?> class1) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String title = NC.getActivity().getString(R.string.app_name);
        Intent notificationIntent = new Intent(this, class1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setAutoCancel(false);
        builder.setTicker(message);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(getNotificationIcon());
//        builder.setColor(Color.YELLOW);
        builder.setContentIntent(pendingIntent);
        // builder.setOngoing(true);
        builder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap());
        //builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.small_logo);
            builder.setColor(ContextCompat.getColor(getBaseContext(), R.color.button_accept));
        } else {
            builder.setSmallIcon(R.drawable.small_logo);
        }

        Notification myNotication = builder.getNotification();


        //notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(Notification_ID, myNotication);
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    public class Getpassenger implements APIResult {

        public Getpassenger(String Url, JSONObject j) {
            // TODO Auto-generated method stub
            result = "";
            try {
                new APIService_Retrofit_JSON(GetPassengerUpdate.this, this, j, false).execute(Url);
//            java.net.URL url = new java.net.URL(Url);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            System.out.println("_______"+j);
//            byte[] postDataBytes = j.toString().getBytes("UTF-8");
//
//           // byte[] postDataBytes = j.toString().getBytes("UTF-8");
//            String base64 = Base64.encodeToString(postDataBytes, Base64.DEFAULT);
//           // Log.w("Driver service response", history_Url + "____" +base64);
//            conn.getOutputStream().write(base64.getBytes());
//            in = new BufferedInputStream(conn.getInputStream());
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(postDataBytes);
//            jsonString = j.toString();
//            StringEntity se = new StringEntity(jsonString);
//            httpPost.setEntity(se);
//            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
//            HttpResponse httpResponse = httpclient.execute(httpPost);


            } catch (Exception e) {
                e.printStackTrace();
            }
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
//            String line = "", name = "";
//            while ((line = reader.readLine()) != null) {
//                name += line;
//            }
//            in.close();
//            result = name;
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            //  Log.w("Pass service response", "" + result);


        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            if (result != null && result.length() > 0) {
                try {
                    JSONObject json = new JSONObject(result);
                    //    Log.e("GetPassengerUpdate ", json.toString());

                    mStatus = json.getInt("status");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Dlocationname = "";
                                    Dlat = 0.0;
                                    Dlon = 0.0;
                                }
                            }, 1000);
                        }
                    });


                    //   System.out.println("status___" + mStatus);
                    msg = json.getString("message");
                    if (json.has("driver_latitute")) {
                        driver_latitute = json.getString("driver_latitute");
                        driver_longtitute = json.getString("driver_longtitute");
                        SessionSave.saveSession("driver_latitute", driver_latitute, GetPassengerUpdate.this);
                        SessionSave.saveSession("driver_longtitute", driver_longtitute, GetPassengerUpdate.this);

                        if (context != null) {
                            (context).driverLocationUpdate(Double.parseDouble(driver_latitute), Double.parseDouble(driver_longtitute));
                        }

                        try {
                            if (json.has("isSplit_fare"))
                                if (json.getInt("isSplit_fare") == 1 ? true : false) {
                                    TaxiUtil.SPLIT_STATUS_ITEM.clear();
                                    JSONArray splitArray = json.getJSONArray("splitfaredetail");
                                    for (int i = 0; i < splitArray.length(); i++) {
                                        JSONObject SplitArrayData = splitArray.getJSONObject(i);
                                        SplitStatusData obj = new SplitStatusData(getApplicationContext());
                                        obj.setImage(SplitArrayData.getString("profile_image"));
                                        obj.setName(SplitArrayData.getString("firstname"));
                                        obj.setStatus(SplitArrayData.getString("approve_status"));
                                        TaxiUtil.SPLIT_STATUS_ITEM.add(obj);
                                    }
                                    if (SplitFareStatusDialog.rv != null)
                                        if (SplitFareStatusDialog.rv.getAdapter() != null)
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SplitFareStatusDialog.rv.getAdapter().notifyDataSetChanged();
                                                }
                                            });

                                }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (json.has("display")) {
                            if (json.getString("display").equals("1")) {
                                if (mStatus == 3) {
                                    cancelNotify();
                                    generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                    Intent ongoing = new Intent();
                                    Bundle extras = new Bundle();
                                    extras.putString("alert_message", msg);
                                    ongoing.putExtras(extras);
                                    ongoing.setAction(Intent.ACTION_MAIN);
                                    ongoing.addCategory(Intent.CATEGORY_LAUNCHER);
                                    ongoing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ComponentName cn = new ComponentName(GetPassengerUpdate.this, MainHomeFragmentActivity.class);
                                    ongoing.setComponent(cn);
                                    getApplication().startActivity(ongoing);
                                } else if (mStatus == 4) {
                                    cancelNotify();
                                    generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                    Intent home = new Intent();
                                    Bundle extras = new Bundle();
                                    extras.putString("alert_message", msg);
                                    home.putExtras(extras);
                                    home.setAction(Intent.ACTION_MAIN);
                                    home.addCategory(Intent.CATEGORY_LAUNCHER);
                                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ComponentName cn = new ComponentName(GetPassengerUpdate.this, MainHomeFragmentActivity.class);
                                    home.setComponent(cn);
                                    getApplication().startActivity(home);
                                } else if (mStatus == 5) {
                                    cancelNotify();
                                    generateNotification(GetPassengerUpdate.this, json.getString("message"), ReceiptAct.class);
                                    SessionSave.saveSession("receipt_details", result, GetPassengerUpdate.this);
                                    Intent home = new Intent();
                                    home.putExtra("Message", result);
                                    Bundle extras = new Bundle();
                                    extras.putString("alert_message", msg);
                                    home.putExtras(extras);

                                    home.setAction(Intent.ACTION_MAIN);
                                    home.addCategory(Intent.CATEGORY_LAUNCHER);
                                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ComponentName cn = new ComponentName(GetPassengerUpdate.this, ReceiptAct.class);
                                    home.setComponent(cn);
                                    getApplication().startActivity(home);
                                } else if (mStatus == 8 || mStatus == 9 || mStatus == -1) {
                                    cancelNotify();

                                    generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                    GetPassengerUpdate.this.stopSelf();
                                    SessionSave.saveSession("trip_id", "", GetPassengerUpdate.this);
                                    Intent home = new Intent();
                                    Bundle extras = new Bundle();
                                    extras.putString("alert_message", msg);
                                    home.putExtras(extras);
                                    home.setAction(Intent.ACTION_MAIN);
                                    home.addCategory(Intent.CATEGORY_LAUNCHER);
                                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ComponentName cn = new ComponentName(GetPassengerUpdate.this, MainHomeFragmentActivity.class);
                                    home.setComponent(cn);
                                    startActivity(home);

                                } else if (mStatus == 10) {
                                    cancelNotify();
                                    generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                    GetPassengerUpdate.this.stopSelf();
                                    SessionSave.saveSession("trip_id", "", GetPassengerUpdate.this);
                                    Intent home = new Intent();
                                    Bundle extras = new Bundle();
                                    extras.putString("alert_message", msg);
                                    home.putExtras(extras);
                                    home.setAction(Intent.ACTION_MAIN);
                                    home.addCategory(Intent.CATEGORY_LAUNCHER);
                                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ComponentName cn = new ComponentName(GetPassengerUpdate.this, MainHomeFragmentActivity.class);
                                    home.setComponent(cn);
                                    startActivity(home);
                                } else if (mStatus == 11) {

                                    System.out.print("redirect_url" + " ...... " + result);

                                    //     cancelNotify();
                                    //  generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                    //    GetPassengerUpdate.this.stopSelf();
                                    // SessionSave.saveSession("trip_id", "", GetPassengerUpdate.this);


                                    try {

                                        generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                        GetPassengerUpdate.this.stopSelf();

                                        Intent int_obj = new Intent(GetPassengerUpdate.this, Paymentgetway.class);
                                        //   int_obj.putExtra("redirect_url", redirect_url);
                                        startActivity(int_obj);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    cancelNotify();
                                    generateNotification(GetPassengerUpdate.this, json.getString("message"), MainHomeFragmentActivity.class);
                                    Intent home = new Intent();
                                    home.putExtra("Message", result);
                                    Bundle extras = new Bundle();
                                    extras.putString("alert_message", msg);
                                    home.putExtras(extras);
                                    home.setAction(Intent.ACTION_MAIN);
                                    home.addCategory(Intent.CATEGORY_LAUNCHER);
                                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    ComponentName cn = new ComponentName(GetPassengerUpdate.this, MainHomeFragmentActivity.class);
                                    home.setComponent(cn);
                                    getApplication().startActivity(home);


                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelNotify() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

}
