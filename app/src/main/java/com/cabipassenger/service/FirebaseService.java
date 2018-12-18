package com.cabipassenger.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cabipassenger.MainActivity;
import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;
import com.cabipassenger.data.apiData.PromoDataList;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developer on 30/8/17.
 */

public class FirebaseService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 123;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static MainHomeFragmentActivity MAIN_ACT;
    private JSONObject jo;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
       // Log.d("MyFirebaseIIDService", "From: " + remoteMessage.getNotification().getBody());
        onHandleIntent(remoteMessage);
        // Check if message contains a data payload.
     //   System.out.println("MyFirebaseIIDServices" + remoteMessage.getData());
      //  Log.d("MyFirebaseIIDService", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            Log.d("MyFirebaseIIDService", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("sssss", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    protected void onHandleIntent(RemoteMessage remoteMessage) {// Handling gcm message from
        // pubnub
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = remoteMessage.getMessageType();
//        System.out.println("GCMM_________" + messageType);
//        System.out.println("GCMM_________" + extras.getString("message"));
        String message = "";
        try {
            message=  remoteMessage.getData().get("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  System.out.println("MyFirebaseIIDServices" + message);
        if (!message.isEmpty()) {
//            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                //sendNotification("Send error: " + extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                //				sendNotification("Deleted messages on server: "
//                //						+ extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
               // Log.i("", "Received: " + message.toString());
                //  Toast.makeText(GcmIntentService.this, extras.toString(), Toast.LENGTH_SHORT).show();
                generateNotification(this,message, MainHomeFragmentActivity.class);
                JSONObject jos = null;
                try {
                    jos = new JSONObject(message);

        //            System.out.println("VVVVVVVVV" + jos.getString("status"));
                    if (jos.getString("status") != null) {
                        if (jos.getString("status").trim().equals("21")) {
                  //          System.out.println("VVVVVVVVV*_" + SessionSave.getSession(TaxiUtil.PROMO_LIST, this).trim());
                            PromoDataList promoDataLists = null;
                            if (!SessionSave.getSession(TaxiUtil.PROMO_LIST, this).trim().equals(""))
                                promoDataLists = TaxiUtil.fromJson(SessionSave.getSession(TaxiUtil.PROMO_LIST, this), PromoDataList.class);
                            if (promoDataLists == null)
                                promoDataLists = new PromoDataList();
                            PromoDataList.PromoData obj = promoDataLists.getPromoData();
                            obj.setMessage(jos.getString("message"));
                            obj.setStatus(jos.getString("title"));
                            obj.setExpiry_date(jos.getLong("expiry_date"));
                            promoDataLists.promoDatas.add(obj);
                         //   System.out.println("VVVVVVVVV***_" + SessionSave.getSession(TaxiUtil.PROMO_LIST, this).trim());
                            SessionSave.saveSession(TaxiUtil.PROMO_LIST, TaxiUtil.toString(promoDataLists), this);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                //    System.out.println("VVVVVVVVV*****" + e.getLocalizedMessage());
                }
          //  }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
       // GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle(getPackageName()).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getBaseContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
        }
    }

    @SuppressWarnings("deprecation")
    public void generateNotification(Context context, String message, Class<?> class1) {
    //    System.out.println("_______ssss0" + MAIN_ACT);
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Notification notification = new Notification(R.drawable.app_icon, message, System.currentTimeMillis());
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.putExtra("GCMnotification", message);
        SessionSave.saveSession("GCMnotification", message, context);

        int requestID = (int) System.currentTimeMillis();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // notification.setLatestEventInfo(context, title, message, pendingIntent);
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
        String Message = "";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        try {
            jo = new JSONObject(message);

            builder.setAutoCancel(false);
            builder.setOngoing(true);
            if (jo.has("passenger_name"))
                Message = getString(R.string.z_split_fare_with) + " " + jo.getString("passenger_name");
            else {
                Message = jo.getString("message");
                SessionSave.saveSession("GCMnotificationPopup", Message, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.setTicker(NC.getString(R.string.app_name));
        builder.setContentTitle(title);
        builder.setContentText(Message);
       // builder.setSmallIcon(R.drawable.ic_launcher);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.small_logo);
            builder.setColor(ContextCompat.getColor(getBaseContext(),R.color.button_color));
        } else {
            builder.setSmallIcon(R.drawable.small_logo);
        }
        builder.setContentIntent(pendingIntent);

        builder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap());
        //builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();
        Notification myNotication = builder.getNotification();
        myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
      //  System.out.println("_______ssss1" + MAIN_ACT);
        mNotificationManager.notify(NOTIFICATION_ID, myNotication);
      //  System.out.println("_______ssss2" + MAIN_ACT);
        Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification1);
            r.play();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
//        System.out.println("_______"+MAIN_ACT);
        try {
            if (false) {

            } else {
                if (MAIN_ACT != null && !jo.getString("status").equals("21")) {
            //        System.out.println("__________VVVVVVV");
                    Intent home = new Intent();
                    Bundle extras = new Bundle();
                    try {
                        extras.putString("alert_message", jo.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    home.putExtras(extras);
                    home.setAction(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_LAUNCHER);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    ComponentName cn = new ComponentName(FirebaseService.this, MainHomeFragmentActivity.class);
                    home.setComponent(cn);
                    getApplication().startActivity(home);
                    MAIN_ACT.checkGCM();
//                Handler h= new Handler();
//                if(h!=null)
//               h.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
