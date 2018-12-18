package com.cabipassenger.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.cabipassenger.UserHomeAct;
import com.cabipassenger.data.CreditCardData;
import com.cabipassenger.data.DriverData;
import com.cabipassenger.data.FavouriteData;
import com.cabipassenger.data.FavouriteDriverData;
import com.cabipassenger.data.HelpData;
import com.cabipassenger.data.SearchListData;
import com.cabipassenger.data.SplitStatusData;
import com.cabipassenger.data.TripHistoryData;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 *this class is used to put common methods and variables for reuse in whole project
 */

public class TaxiUtil {
    public static final String PROMO_LIST = "promo_list";
    public static Context mContext;
    public static String TaxiType = "RH7PVsKE18qGe6Y6YOC8kdRntOEyDnD0uW";
    //public static String APIBase_Path = "http://www.taximobility.com/mobileapi116/index/dGF4aV9hbGw/?";
    // public static String APIBase_Path = "http://192.168.1.77:1038/mobileapi116/index/dGF4aV9hbGw/?";
    //  public static String APIBase_Path = "http://newpack.taximobility.com/mobileapi116/index/dGF4aV9hbGw=/?";

 // public static String APIBase_Path = "http://192.168.1.169:1009/mobileapi117/index/dGF4aV9hbGw/?";
 //  public static String APIBase_Path = "http://testtaxi.know3.com/mobileapi117/index/dGF4aV9hbGw/?";
  //public static String APIBase_Path = "http://192.168.1.169:1009/mobileapi117/index/dGF4aV9hbGw=/?";
    public static String API_BASE_URL = "";
    public static final String COMPANY_KEY="";
    public static  final String DYNAMIC_AUTH_KEY = "";



  //  http://192.168.1.169:2222/mobileapi117/index/dGF4aV9hbGw=/?type=getcoreconfig

    // public static String APIBase_Path = "http://192.168.1.47:1028/mobileapi117/index/dGF4aV9hbGw/?";

    //public static String APIBase_Path = "http://192.168.1.116:1027/mobileapi116/index/dGF4aV9hbGw/?";
    //public static String APIBase_Path = "http://www.taximobility.com/mobileapi116/index/dGF4aV9hbGw/?";
    // QA
    // local0001137
    // http://192.168.1.169:1001/mobileapi115/index/dGF4aV9hbGw=/?type=getcoreconfig";
    public static ArrayList<Activity> mActivitylist = new ArrayList<Activity>();
    public static Vector<SearchListData> SEARCH_LIST_ITEM = new Vector<>();
    public static ArrayList<SplitStatusData> SPLIT_STATUS_ITEM = new ArrayList<>();
    public static int close=0;
    public final String[] PERMISSIONS = new String[]{"publish_stream", "read_stream", "offline_access", "em" + "ail"};
    public ProgressDialog mProgress;
    public static String mDevice_id = "";
    public static String mDevice_token = "";
    public static ArrayList<FavouriteData> mFavouritelist = new ArrayList<FavouriteData>();
    public static ArrayList<HelpData> mHelplist = new ArrayList<HelpData>();
    public static ArrayList<CreditCardData> mCreditcardlist
            = new ArrayList<CreditCardData>();
    public static String Address = "";
    public static String pAddress;
    public static String dAddress;
    public static double Latitude;
    public static double Longitude;
    public static ArrayList<TripHistoryData> mTripHistorylist = new ArrayList<TripHistoryData>();
    public static ArrayList<DriverData> mDriverdata = new ArrayList<DriverData>();
    public static ArrayList<HashMap<String, String>> bookinglist = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> HSbookinglist = new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> MonthHistorylist = new ArrayList<HashMap<String, String>>();
    public static ArrayList<FavouriteDriverData> mFavouriteDriverlist = new ArrayList<FavouriteDriverData>();
    public static String Passenger_Path;
    public static boolean islocpickup = false;
    public static boolean iscar = false;

    public static String Taxibase_Path;
    public static boolean FromFav = false;
    public static boolean isTutorial = false;
    public static double p_lat, p_lng, d_lat, d_lng;
    public static String p_place, d_place;
    public static boolean mTimerStart = false;
    public static int LocationResult = 420;
    public static String current_act = "";



    /*
     * Location Utils
     */
    public static final int MILLISECONDS_PER_SECOND = 1000;
    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 1;
    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;
    public static Context sContext;

    //split on/off
    public static String isSplitOn = "IS_SPLIT";
    public static String isFavDriverOn = "IS_Fav";
    public static String isSkipFavOn = "IS_SKIP_FAV";
    public static  String PASSENGER_LANGUAGE_TIME = "PASSENGER_LANG_TIME";
    public static  String PASSENGER_COLOR_TIME = "PASSENGER_COLOR_TIME";

    public static  String amount = " ";


    public TaxiUtil(Context context) {
        mContext = context;
        mProgress = new ProgressDialog(mContext);
    }

    public static <T> T fromJson(String data, Class<T> classn) {

        return new Gson().fromJson(data, classn);
    }

    public static String toString(Object s) {
        return new Gson().toJson(s);
    }


    public static  String firstLetterCaps(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();

    }

    public static  boolean isCurrentTimeZone(long s){
        long dateInMillis = System.currentTimeMillis()/1000;
        System.out.println("____________LLL"+s +"__"+dateInMillis+"__"+ TimeUnit.MILLISECONDS.toSeconds(Math.abs(s-dateInMillis)));
        return  TimeUnit.MILLISECONDS.toSeconds(Math.abs(s-dateInMillis))<24;
    }
    /**
     * This is method for check the Internet connection
     * <p>
     * <P>
     * This is method for check the Internet connection
     * </p>
     *
     * @param boolean
     * @return boolean is online
     */
    public static boolean isOnline(Context ctx) {

        mContext = ctx;
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo element : info) {
                    if (element.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This is method for check the ConnectivityStatus
     * <p>
     * <P>
     * This is method for check the ConnectivityStatus
     * </p>
     *
     * @param boolean
     * @return boolean is connection
     */
    public static boolean getConnectivityStatus(Context context) {

        boolean conn = false;
        mContext = context;
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    /**
     * This is method for logout from the application
     * <p>
     * <P>
     * This is method for logout from the application
     * </p>
     *
     * @param void context
     * @return void clear session
     */
    private static void logout(Context ctx) {

        mContext = ctx;
        clearsession(ctx);
        int length = mActivitylist.size();
        if (length != 0) {
            for (int i = 0; i < length; i++) {
                mActivitylist.get(i).finish();
            }
        }
        Intent i_s = new Intent(mContext, UserHomeAct.class);
        mContext.startActivity(i_s);
    }

    /**
     * This is method for logout from the application
     * <p>
     * <P>
     * This is method for logout from the application
     * </p>
     *
     * @param void context
     * @return void clear session
     */
    public static class Logout implements APIResult {
        public Logout(String string, Context ctx, JSONObject data) {
            // TODO Auto-generated constructor stub
            mContext = ctx;
            new APIService_Retrofit_JSON(mContext, this, data, false).execute(string);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                logout(mContext);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Logout", "", mContext);
                        try {
                            ((Activity) mContext).finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext, json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * This is class for get the company details from api
     * <p>
     * <P>
     * This is class for get the company details from api
     * </p>
     *
     * @param object url
     * @return object company details
     */
    public class CoreConfig implements APIResult {
        byte[] data;

        public CoreConfig(String string) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(mContext, this, "", true).execute(string);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                JSONObject json;
                try {
                    json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        JSONArray array = json.getJSONArray("message");
                        SessionSave.saveSession("About", array.getJSONObject(0).getString("aboutpage_description"), mContext);
                        SessionSave.saveSession("Currency", array.getJSONObject(0).getString("site_currency")+" ", mContext);
                        SessionSave.saveSession("AdminMail", array.getJSONObject(0).getString("admin_email"), mContext);
                        String str = array.getJSONObject(0).getString("app_name");
                        try {
                            data = str.getBytes("UTF-8");
                            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                            Log.i("Base 64 ", base64);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
    }

    public static String encodeToBase64() {

        String live_key = "ntaxi_" + TaxiType;
        String encodedString = "";
        try {
            byte[] byteData = null;
            if (Build.VERSION.SDK_INT >= 8) {
                byteData = android.util.Base64.encode(live_key.getBytes(), android.util.Base64.DEFAULT);
            }
            encodedString = new String(byteData);
        } catch (Exception e) {
            Log.i("Exception ", "" + e);
        }
        try {
            encodedString = URLEncoder.encode(encodedString, "utf-8");
            encodedString = encodedString.replace("%0A", "");
        } catch (UnsupportedEncodingException e) {
            Log.i("Conversion ", "" + e.toString());
        }
        return encodedString;
    }

    /*
     * method to clear the cache. input-context
     */
    public static void trimCache(Context context) {

        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //method to close dialog
    public static void closeDialog(Dialog dialog) {

        try {
            if (dialog != null)
                if (dialog.isShowing())
                    dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    private static void clearsession(Context ctx) {

        try {
            SessionSave.saveSession("TaxiStatus", "", ctx);
            SessionSave.saveSession("Logout", "", ctx);
            SessionSave.saveSession("Id", "", ctx);
            SessionSave.saveSession("service_type_name", "", ctx);
            SessionSave.saveSession("AdminMail", "", ctx);
            SessionSave.saveSession("Email", "", ctx);
            SessionSave.saveSession("Pass_Tripid", "", ctx);
            SessionSave.saveSession("Register", "", ctx);
            SessionSave.saveSession("PLAT", "", ctx);
            SessionSave.saveSession("PLNG", "", ctx);
            SessionSave.saveSession("service_type", "", ctx);
            SessionSave.saveSession("NotifyMessage", "", ctx);
            SessionSave.saveSession("Server_Response", "", ctx);
            SessionSave.saveSession("Server_bookinglist", "", ctx);
            SessionSave.saveSession("trip_id", "", ctx);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
