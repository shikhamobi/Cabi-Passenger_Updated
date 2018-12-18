package com.cabipassenger.util;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cabipassenger.MainActivity;
import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is used to get the network status when it is enable/disable.
 */
public class NetworkStatus extends BroadcastReceiver {
    public Context mContext;
    Context appContext;
    private String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        try {
          //  System.out.println("_____________netChange");

            if (!getConnectivityStatus(mContext)) {
                if (TaxiUtil.sContext != null && TaxiUtil.sContext instanceof MainHomeFragmentActivity)
                    ((MainHomeFragmentActivity)(TaxiUtil.sContext )).networkError( false);
                else if (TaxiUtil.sContext != null && TaxiUtil.sContext instanceof MainActivity)
                    MainActivity.isConnect(TaxiUtil.sContext, false);
            }
//            else if (TaxiUtil.sContext != null && !(TaxiUtil.sContext instanceof MainHomeFragmentActivity))
//                new URLReachable(mContext).execute();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static boolean isOnline(Context mContext2) {

        if (mContext2 != null) {
           // System.out.println("_____________isonline");
            ConnectivityManager connectivity = (ConnectivityManager) mContext2.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            ConnectionQuality cq = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
                            try {
                         //       System.out.println("_____________isonline4");
//                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                                StrictMode.setThreadPolicy(policy);
                                //  System.out.println("_____________isonline1" + isURLReachable(mContext2));
//                                if(mContext2 instanceof SplashActivity)
//                                new URLReachable(mContext2).execute();
                                return true;
//                                else
//                                    return false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
            }
        }
       // System.out.println("_____________isonline2");


        return false;
    }

    private static class URLReachable extends AsyncTask<URL, Boolean, Boolean> {
        Context mContext;

        URLReachable(Context mContext) {
            this.mContext = mContext;
        }

        protected Boolean doInBackground(URL... urls) {
            try {
                URL url = new URL("http://google.com");   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);
                urlc.setReadTimeout(10 * 1000);
                // 10 s.

                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !");
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
            // return isURLReachable(mContext);
        }


        protected void onPostExecute(Boolean result) {
          //  System.out.println("connection_reachable " + result);
//            if(mContext!=null && mContext instanceof Activity && ((Activity)mContext).getCurrentFocus()!=null)
//            MainActivity.isConnect(mContext, result);
        }
    }


    static public boolean isURLReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://google.com");   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);
                // 10 s.

                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !");
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean getConnectivityStatus(Context context) {
        boolean conn = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                if (activeNetwork.isConnected()) {
                    conn = true;
                } else {
                    conn = false;
                }
            }
        } else {
            conn = false;
        }
        return conn;
    }

    public void alert_viewq(String title, String message, String success_txt, String failure_txt) {
        final Dialog mDialog;
        final View view = View.inflate(mContext, R.layout.alert_view, null);
        mDialog = new Dialog(mContext, R.style.NewDialog);
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
                mDialog.dismiss();
            }
        });
        button_failure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        });
    }
}
