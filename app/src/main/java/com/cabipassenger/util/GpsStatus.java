package com.cabipassenger.util;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cabipassenger.MainActivity;
import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;

/**
 * This class is used to get the network status when it is enable/disable.
 */
public class GpsStatus extends BroadcastReceiver {
    public Context mContext;
    Context appContext;
    private String message;
    public static Dialog mDialog;
    public static int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        try {
          //  System.out.println("Gps");
            if (!isGpsEnabled(mContext)) {
                if(TaxiUtil.sContext != null&&TaxiUtil.sContext instanceof MainHomeFragmentActivity)
                    MainHomeFragmentActivity.gpsalert(TaxiUtil.sContext, false);
//                if (count == 0) {
//                    message = "Gps connection is Disable!!";
//                    if (!TaxiUtil.current_act.equals("SplashAct")) {
//                        if (TaxiUtil.sContext != null)
//                            MainActivity.gpsalert(TaxiUtil.sContext, false);
//                    }
//
//
//                }
                count++;
            } else {
                count = 0;
                message = " Gps connection is Enable!!";
                if (!TaxiUtil.current_act.equals("SplashAct")) {
                    if (TaxiUtil.sContext != null)
                        MainActivity.gpsalert(TaxiUtil.sContext, true);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void generatNotification(String message2) {
        // TODO Auto-generated method stub
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher, message, System.currentTimeMillis());
        String title = NC.getString(R.string.app_name);
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        notificationIntent.putExtra("message", message);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        //notification.setLatestEventInfo(mContext, title, message, intent);


        Notification.Builder builder = new Notification.Builder(mContext);

        builder.setAutoCancel(false);
        builder.setTicker(NC.getString(R.string.app_name));
        builder.setContentTitle(title);
        builder.setContentText(message);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.small_logo);
            builder.setColor(ContextCompat.getColor(mContext,R.color.button_accept));
        } else {
            builder.setSmallIcon(R.drawable.small_logo);
        }
        builder.setContentIntent(intent);
        builder.setOngoing(true);
        //builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        builder.build();

        Notification myNotication = builder.getNotification();


        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, myNotication);
    }

    public void gpsalert(boolean isconnect, String title, String message, String success_txt, String failure_txt) {
        if (!isconnect) {
            final View view = View.inflate(TaxiUtil.sContext, R.layout.alert_view, null);
            mDialog = new Dialog(TaxiUtil.sContext, R.style.NewDialog);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            if (!mDialog.isShowing())
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
                    Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    TaxiUtil.sContext.startActivity(mIntent);
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mDialog.dismiss();
                }
            });
        } else {
            try {
                mDialog.dismiss();
                // Intent intent = new Intent(TaxiUtil.sContext, TaxiUtil.sContext.getClass());
                // mContext.startActivity(intent);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
/**
 * this method is used detect whether the gps is enabled or not
 */

    public boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        else
            return false;
    }

    public boolean isNetworkEnabled(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        return  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }
}