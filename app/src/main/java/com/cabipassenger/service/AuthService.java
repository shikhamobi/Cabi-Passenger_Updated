package com.cabipassenger.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by developer on 30/11/16.
 * This service is to get the authentication key--> which need to append with all api's
 * Authentication key is valid only for every 30 minutes
 * This service call authentication key api for every 27 minutes
 */
public class AuthService extends Service {
 int getdetailtimer =  1620000;
//int getdetailtimer =  60000;
    private final Timer mTimer = new Timer();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // http://192.168.1.56:1002/mobileapi117/index/dGF4aV9hbGw=/?type=get_authentication
        //{"mobilehost":"192.168.1.56:1002","encode":"MTkyLjE2OC4xLjU2OjEwMDItMTQ4MDUwMjUzMw=="}


        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String lastCalled = SessionSave.getSession("auth_last_call_type", this);
        if (lastCalled.equals("")) {
            mTimer.scheduleAtFixedRate(new GetAuthKey(), 0, getdetailtimer);
        } else {

            //3000(millliseconds in a second)*60(seconds in a minute)*5(number of minutes)=300000
            if (Math.abs(Long.parseLong(lastCalled) - System.currentTimeMillis()) > 300000) {
                //server timestamp is within 5 minutes of current system time
                System.out.println("______calling");
                if (mTimer != null)
                    mTimer.scheduleAtFixedRate(new GetAuthKey(), 0, getdetailtimer);
            } else {
                //server is not within 5 minutes of current system time
                if (mTimer != null)
                mTimer.scheduleAtFixedRate(new GetAuthKey(), 0, getdetailtimer);
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }
//void callRepeat(){
//    while (true){
//        System.out.println("naga______calling_sleep" );
//        getAuthKeyy();
//        //SystemClock.sleep(500000);
//
//        //callRepeat();
//    }
//}
//    private void getAuthKeyy() {
//        String url = "type=get_authentication";
//        JSONObject j = new JSONObject();
//        try {
//            j.put("mobilehost", TaxiUtil.API_BASE_URL.replace("mobileapi117/index/", ""));
//            j.put("encode", TaxiUtil.DYNAMIC_AUTH_KEY);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println("naga______callinghiii" + url);
//
//    }

    //private void getAuthKey()
    private class GetAuthKey extends TimerTask implements APIResult {



        @Override
        public void run() {
            String url = "type=get_authentication";
            TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", AuthService.this);
//            TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", AuthService.this);
//            TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", AuthService.this);

            JSONObject j = new JSONObject();
            try {
                URL urls = new URL(TaxiUtil.API_BASE_URL);
                String host = urls.getHost();
                j.put("mobilehost", host);
                j.put("encode", TaxiUtil.DYNAMIC_AUTH_KEY);
                j.put("user_id", SessionSave.getSession("Id",AuthService.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("url____" + url);
            new APIService_Retrofit_JSON(AuthService.this, this, j, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            System.out.println("callingrrrr" + result);
            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                       // TaxiUtil.DYNAMIC_AUTH_KEY=json.getString("encode");
                        SessionSave.saveSession("auth_last_call_type", String.valueOf(ts), AuthService.this);
                    }
                    // alert_view(this, "" + getResources().getString(R.string.message), "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
//                        if (json.getInt("status") == 2)
//                            alert_view(this, "" + getResources().getString(R.string.message), "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
                } else {
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                ShowToast(this, result);
//                            }
//                        });
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // }
    }
}
