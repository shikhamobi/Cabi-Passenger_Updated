package com.cabipassenger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabipassenger.data.apiData.ApiRequestData;
import com.cabipassenger.data.apiData.CompanyDomainResponse;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.service.AuthService;
import com.cabipassenger.service.CoreClient;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.service.ServiceGenerator;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.NetworkStatus;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cabipassenger.MainActivity.mgpsDialog;


/**
 * Created by developer on 30/6/17.
 */

public class SplashActivity extends AppCompatActivity {
    private static boolean CORE_CALLED;
    public static String CURRENT_COUNTRY_CODE = "SAU";
    public static String CURRENT_COUNTRY_ISO_CODE = "SAU";
    private static final int MY_PERMISSIONS_REQUEST_GPS = 420;
    private Location mLastLocation;
    public static String SENDER_ID = "422275317979";
    public static String REG_ID;
    private double latitude;
    private double longitude;
    ProgressBar progressBar1;
    TextView status_text;
    private VideoView vv;
    private Dialog loadingDialog;
    private Dialog errorDialog;
    private LocationRequest mLocationRequest;
    public static boolean NO_NEED_TO_PLAY = true;
    private static int UPDATE_INTERVAL = 5000; // 10 sec
    private static int FASTEST_INTERVAL = 1000;
    private int randomNum;
    private int phaseCompleted;
    private boolean askDomain = false;
    private Dialog urlPopup;
    private boolean CompanyResponseError;
    private long getCore_Utc;
    private String getCoreLangTime;
    private String getCoreColorTime;
    public static ArrayList<String> fields = new ArrayList<>();
    public static ArrayList<String> fields_value = new ArrayList<>();
    public static boolean isBUISNESSKEY = true;
    public static HashMap<String, Integer> fields_id = new HashMap<>();
    private Dialog versionAlertDialog;
    private Context context;
    //private GoogleCloudMessaging gcm;
    public static int selected_carmodel = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int curVersion = 0;
        try {
            PackageManager manager = getPackageManager();
            try {
                curVersion = manager.getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (curVersion != 0)
                if (SessionSave.getSession(String.valueOf(curVersion), this).trim().equals("")) {

                    SessionSave.saveSession("base_url", "", SplashActivity.this);
                    SessionSave.saveSession("api_key", "", SplashActivity.this);
                    SessionSave.saveSession("encode", "", SplashActivity.this);
                    SessionSave.saveSession("image_path", "", SplashActivity.this);
                    SessionSave.saveSession(String.valueOf(curVersion), "No", SplashActivity.this);

                    //            CommonData.DYNAMIC_AUTH_KEY = "";
                    //            CommonData.API_BASE_URL = "";
                    //            ServiceGenerator.COMPANY_KEY = "";
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.cabipassenger", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.splashlay);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        status_text = (TextView) findViewById(R.id.status_text);
        vv = (VideoView) findViewById(R.id.videoView);
        randomNum = (int) (Math.random() * 500);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (phaseCompleted == 0) {
            if (ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GPS);
            } else {
                ifPermissionGranted();
            }
        }

//
//        PromoDataList promoDataLists;
//        if (!SessionSave.getSession(TaxiUtil.PROMO_LIST, this).trim().equals("")) {
//            promoDataLists = TaxiUtil.fromJson(SessionSave.getSession(TaxiUtil.PROMO_LIST, this), PromoDataList.class);
//          //  System.out.println("heelllll" + promoDataLists.promoDatas.size());
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            String token = FirebaseInstanceId.getInstance().getToken();

            SessionSave.saveSession("device_token", token, SplashActivity.this);
           // gcmRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void callApi() {
        if (!SessionSave.getSession("base_url", SplashActivity.this).trim().equals("")) {
            if (!NO_NEED_TO_PLAY) {

                Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                vv.setVideoURI(path);

                vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
//                        if (!CORE_CALLED && !CompanyResponseError)
                        //  showDialog();

                    }
                });
                vv.start();
                vv.setVisibility(View.VISIBLE);
                NO_NEED_TO_PLAY = !NO_NEED_TO_PLAY;
            }

            if (NetworkStatus.isOnline(SplashActivity.this))
                new SplashActivity.GetAuthKey();
            else
                errorInSplash(getString(R.string.check_internet_connection));
        } else {
            if (askDomain) {
                cancelLoading();

                if (NetworkStatus.isOnline(SplashActivity.this))
                    getUrl();
                else
                    errorInSplash(getString(R.string.check_internet_connection));
            } else
                urlApi("");

        }
    }

    private void urlApi(String keyy) {
        String url = "";
        try {

          // url = "http://ksa.cabi.me/passengerapi112/index/";   /*live client*/

           //  url = "http://192.168.1.125/passengerapi112/index/";    //local server
             url = "http://162.243.20.44/passengerapi112/index/";   /*development server*/


        } catch (Exception e) {
            e.printStackTrace();
        }
        CoreClient client = new ServiceGenerator(SplashActivity.this, url, true).createService(CoreClient.class);
        ApiRequestData.BaseUrl request = new ApiRequestData.BaseUrl();

       /* request.company_domain = "tagmytaxi";
        request.company_main_domain = "ksa.cabi.me";*/

         request.company_domain = "cabi";                     //local $ development
        //request.company_main_domain = "192.168.1.125";       //local
        request.company_main_domain = "162.243.20.44";       //development

        request.device_type = "1";

        Call<CompanyDomainResponse> response = client.callData(TaxiUtil.COMPANY_KEY, request);
        response.enqueue(new Callback<CompanyDomainResponse>() {
            @Override
            public void onResponse(Call<CompanyDomainResponse> call, Response<CompanyDomainResponse> response) {
                CompanyDomainResponse cr = response.body();
                cancelLoading();
                if (cr != null && SplashActivity.this != null) {
                    if (cr.status != null) {
                        if (cr.status.trim().equals("1")) {
                            SessionSave.saveSession("base_url", cr.baseurl, SplashActivity.this);
                            SessionSave.saveSession("api_key", cr.apikey, SplashActivity.this);
                            SessionSave.saveSession("image_path", cr.androidPaths.static_image, SplashActivity.this);
                            SessionSave.saveSession("encode", cr.encode, SplashActivity.this);
                            TaxiUtil.API_BASE_URL = cr.baseurl;
                            String totalLanguage = "";
                            for (int i = 0; i < cr.androidPaths.passenger_language.size(); i++) {
                                String key_ = "";
                                totalLanguage += cr.androidPaths.passenger_language.get(i).language.replaceAll(".xml", "") + "____";
                                SessionSave.saveSession("LANG" + String.valueOf(i), cr.androidPaths.passenger_language.get(i).language, SplashActivity.this);
                                SessionSave.saveSession("LANGTemp" + String.valueOf(i), cr.androidPaths.passenger_language.get(i).design_type, SplashActivity.this);
                                SessionSave.saveSession("LANGCode" + String.valueOf(i), cr.androidPaths.passenger_language.get(i).language_code, SplashActivity.this);
                                SessionSave.saveSession(cr.androidPaths.passenger_language.get(i).language, cr.androidPaths.passenger_language.get(i).url, SplashActivity.this);
                                //     System.out.println("******" + i + "__" + cr.androidPaths.passenger_language.get(i).language);
                                if (cr.androidPaths.passenger_language.get(i).language.equalsIgnoreCase("English")) {
                                    //       System.out.println("******ccc" + i + "__" + cr.androidPaths.passenger_language.get(i).language);
                                    SessionSave.saveSession("Lang", cr.androidPaths.passenger_language.get(i).language_code, SplashActivity.this);
                                    SessionSave.saveSession("LANGTempDef", cr.androidPaths.passenger_language.get(i).design_type, SplashActivity.this);
                                    SessionSave.saveSession("LANGDef", cr.androidPaths.passenger_language.get(i).language, SplashActivity.this);
                                }
                            }

                            if (SessionSave.getSession("LANGDef", SplashActivity.this).trim().equals(""))
                                SessionSave.saveSession("LANGDef", SessionSave.getSession("LANG0", SplashActivity.this), SplashActivity.this);
                            if (SessionSave.getSession("LANGTempDef", SplashActivity.this).trim().equals(""))
                                SessionSave.saveSession("LANGTempDef", SessionSave.getSession("LANGTemp0", SplashActivity.this), SplashActivity.this);
                            SessionSave.saveSession("lang_json", totalLanguage, SplashActivity.this);
                            SessionSave.saveSession("colorcode", cr.androidPaths.colorcode, SplashActivity.this);
                            if (SessionSave.getSession("Lang", SplashActivity.this).equals(""))
                                SessionSave.saveSession("Lang", cr.androidPaths.passenger_language.get(0).language_code.replaceAll(".xml", ""), SplashActivity.this);
                            String url = "type=getcoreconfig";
                            new SplashActivity.CoreConfigCall(url);
                        } else {
                            CompanyResponseError = true;
                            errorInSplash(cr.message == null ? getString(R.string.server_con_error) : cr.message);
                        }
                    }
                } else {
                    CompanyResponseError = true;
                    errorInSplash(getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<CompanyDomainResponse> call, Throwable t) {
                CompanyResponseError = true;
                cancelLoading();
                errorInSplash(getString(R.string.server_error));
            }
        });
    }


    private void registerInBackground() {

       /* new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    REG_ID = gcm.register(SENDER_ID);

                    msg = "Device registered, registration ID=" + REG_ID;

                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    msg = "Error :" + ex.getMessage();

                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    if (REG_ID != null)
                        if (REG_ID.length() != 0) {
                            SessionSave.saveSession("device_token", "" + REG_ID, SplashActivity.this);
                        }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }.execute(null, null, null);
        // EC.decrypt()*/
    }


    private synchronized void getAndStoreColorValues(String result) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("*");
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    CL.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

                }
            }

            getColorValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void getColorValueDetail() {
        Field[] fieldss = R.color.class.getDeclaredFields();
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "color", getPackageName());

            if (CL.nfields_byName.containsKey(fieldss[i].getName())) {
                CL.fields.add(fieldss[i].getName());
                CL.fields_value.add(getResources().getString(id));
                CL.fields_id.put(fieldss[i].getName(), id);
            } else {
                System.out.println("Clvalue__" + fieldss[i].getName());
            }
        }


        for (Map.Entry<String, String> entry : CL.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            CL.nfields_byID.put(CL.fields_id.get(h), CL.nfields_byName.get(h));
        }

    }

    private class CoreConfigCall implements APIResult {
        public CoreConfigCall(final String url) {
            // TODO Auto-generated constructor stub
            // showDialog();

            new APIService_Retrofit_JSON_NoProgress(SplashActivity.this, this, "", true).execute("type=getcoreconfig");
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            cancelLoading();

            if (isSuccess) {
                try {

                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        if (!(SessionSave.getSession("wholekey", SplashActivity.this).equals(""))) {
                            getAndStoreStringValues(SessionSave.getSession("wholekey", SplashActivity.this));
                            getAndStoreColorValues(SessionSave.getSession("wholekeyColor", SplashActivity.this));
                        }
                        final JSONArray array = json.getJSONArray("detail");
                        startService(new Intent(SplashActivity.this, AuthService.class));
                        // utc_time
                        SessionSave.saveSession("play_store_version", array.getJSONObject(0).getString("android_passenger_version"), SplashActivity.this);
                        SessionSave.saveSession("tax", array.getJSONObject(0).getString("tax"), SplashActivity.this);
                        SessionSave.saveSession("facebook_share", array.getJSONObject(0).getString("facebook_share"), SplashActivity.this);
                        SessionSave.saveSession("twitter_share", array.getJSONObject(0).getString("twitter_share"), SplashActivity.this);
                        SessionSave.saveSession("About", array.getJSONObject(0).getString("aboutpage_description"), SplashActivity.this);
                        SessionSave.saveSession("Currency", array.getJSONObject(0).getString("site_currency") + " ", SplashActivity.this);
                        SessionSave.saveSession("AdminMail", array.getJSONObject(0).getString("admin_email"), SplashActivity.this);
                        SessionSave.saveSession("TellfrdMsg", array.getJSONObject(0).getString("share_content"), SplashActivity.this);
                        SessionSave.saveSession("ShaerMsg", array.getJSONObject(0).getString("tell_to_friend_subject"), SplashActivity.this);
                        SessionSave.saveSession("Metric", array.getJSONObject(0).getString("metric"), SplashActivity.this);
                        SessionSave.saveSession("country_code", array.getJSONObject(0).getString("country_code"), SplashActivity.this);
                        SessionSave.saveSession("android_web_key", array.getJSONObject(0).getString("android_google_api_key"), SplashActivity.this);
                        SessionSave.saveSession("country_iso_code", array.getJSONObject(0).getString("country_iso_code"), SplashActivity.this);
                        try {
                            SessionSave.saveSession("default_city_id", array.getJSONObject(0).getString("default_city_id"), SplashActivity.this);
                            SessionSave.saveSession("default_city_name", array.getJSONObject(0).getString("default_city_name"), SplashActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CURRENT_COUNTRY_CODE = array.getJSONObject(0).getString("country_code");
                        CURRENT_COUNTRY_ISO_CODE = array.getJSONObject(0).getString("country_iso_code");

                        SessionSave.saveSession("android_foursquare_api_key", array.getJSONObject(0).getString("android_foursquare_api_key"), SplashActivity.this);

                        if ((array.getJSONObject(0).getString("google_business_key")).equals("1"))
                            isBUISNESSKEY = true;
                        SessionSave.saveSession("isBUISNESSKEY", isBUISNESSKEY, SplashActivity.this);

                        //  System.out.println("isBUISNESSKEY" + "....." + isBUISNESSKEY);

                        if (SessionSave.getSession("Metric", SplashActivity.this).equals("MILES"))
                            SessionSave.saveSession("Metric_type", "m", SplashActivity.this);
                        else if (SessionSave.getSession("Metric", SplashActivity.this).equals("KM"))
                            SessionSave.saveSession("Metric_type", "k", SplashActivity.this);
                        else
                            SessionSave.saveSession("Metric_type", "k", SplashActivity.this);

                        // SessionSave.saveSession("android_google_api_key", array.getJSONObject(0).getString("android_google_api_key"), SplashActivity.this);
                        SessionSave.saveSession("api_base", array.getJSONObject(0).getString("api_base"), SplashActivity.this);
                        SessionSave.saveSession("logo_base", array.getJSONObject(0).getString("logo_base"), SplashActivity.this);
                        SessionSave.saveSession("site_logo", array.getJSONObject(0).getString("site_logo"), SplashActivity.this);
                        SessionSave.saveSession("Cancellation_setting", array.getJSONObject(0).getString("cancellation_setting"), SplashActivity.this);
                        SessionSave.saveSession("facebook_key", array.getJSONObject(0).getString("facebook_key"), SplashActivity.this);
                        SessionSave.saveSession("android_google_api_key", array.getJSONObject(0).getString("android_google_api_key"), SplashActivity.this);
                        SessionSave.saveSession("skip_credit", array.getJSONObject(0).getString("skip_credit"), SplashActivity.this);
                        SessionSave.saveSession("model_details", array.getJSONObject(0).getString("model_details"), SplashActivity.this);
                        SessionSave.saveSession("referral_code_info", array.getJSONObject(0).getString("referral_code_info"), SplashActivity.this);

                        SessionSave.saveSession("referral_settings", array.getJSONObject(0).getString("referral_settings"), SplashActivity.this);
                        SessionSave.saveSession("referral_settings_message", array.getJSONObject(0).getString("referral_settings_message"), SplashActivity.this);

                        SessionSave.saveSession("passenger_payment_option", array.getJSONObject(0).getString("passenger_payment_option"), SplashActivity.this);

                        //siva 21/02/2018
                        SessionSave.saveSession("promo_code_info", array.getJSONObject(0).getString("referral_code_info"), SplashActivity.this);

                        JSONArray jsonarray = new JSONArray(array.getJSONObject(0).getString("passenger_payment_option"));
                        SessionSave.saveSession("pay_mod_name", jsonarray.getJSONObject(0).getString("pay_mod_name"), SplashActivity.this);

                        getCore_Utc = array.getJSONObject(0).getLong("utc_time");
                        SessionSave.saveSession("current_time", getCore_Utc, SplashActivity.this);
                        SessionSave.saveSession("current_time_local", array.getJSONObject(0).getLong("current_time"), SplashActivity.this);
                        boolean deflanAvail = false;
                        try {
                            getCoreLangTime = json.getJSONObject("language_color_status").getString("android_passenger_language");
                            getCoreColorTime = json.getJSONObject("language_color_status").getString("android_passenger_colorcode");
                            SessionSave.saveSession("isFourSquare", array.getJSONObject(0).getString("android_foursquare_status"), SplashActivity.this);
                            String totalLanguage = "";
                            JSONArray pArray = json.getJSONObject("language_color").getJSONObject("android").getJSONArray("passenger_language");
                            for (int i = 0; i < pArray.length(); i++) {
                                String key_ = "";
                                totalLanguage += pArray.getJSONObject(i).getString("language").replaceAll(".xml", "") + "____";
                                SessionSave.saveSession("LANG" + String.valueOf(i), pArray.getJSONObject(i).getString("language"), SplashActivity.this);
                                SessionSave.saveSession("LANGTemp" + String.valueOf(i), pArray.getJSONObject(i).getString("design_type"), SplashActivity.this);
                                SessionSave.saveSession("LANGCode" + String.valueOf(i), pArray.getJSONObject(i).getString("language_code"), SplashActivity.this);
                                SessionSave.saveSession(pArray.getJSONObject(i).getString("language"), pArray.getJSONObject(i).getString("url"), SplashActivity.this);
                                if (!SessionSave.getSession("LANGDef", SplashActivity.this).equals("") && pArray.getJSONObject(i).getString("language").contains(SessionSave.getSession("LANGDef", SplashActivity.this))) {
                                    deflanAvail = true;
                                }
                            }

                            if (SessionSave.getSession("LANGDef", SplashActivity.this).trim().equals("") || !deflanAvail) {
                                SessionSave.saveSession("LANGDef", SessionSave.getSession("LANG0", SplashActivity.this), SplashActivity.this);
                                SessionSave.saveSession("LANGTempDef", SessionSave.getSession("LANGTemp0", SplashActivity.this), SplashActivity.this);
                                SessionSave.saveSession("Lang", pArray.getJSONObject(0).getString("language_code").replaceAll(".xml", ""), SplashActivity.this);
                                String url = SessionSave.getSession(SessionSave.getSession("LANG" + String.valueOf(0), SplashActivity.this), SplashActivity.this);
                                SessionSave.saveSession("currentStringUrl", url, SplashActivity.this);
                            }
                            SessionSave.saveSession("lang_json", totalLanguage, SplashActivity.this);
                            SessionSave.saveSession("colorcode", json.getJSONObject("language_color").getJSONObject("android").getString("colorcode"), SplashActivity.this);
                        } catch (JSONException e) {
                            errorInSplash(getString(R.string.server_con_error));
                            e.printStackTrace();
                        }
                        //android_passenger_language
                        if (!SessionSave.getSession(TaxiUtil.PASSENGER_LANGUAGE_TIME, SplashActivity.this).trim().equals(getCoreLangTime)) {

                            new SplashActivity.callString(getCoreColorTime);
                        } else if (!SessionSave.getSession(TaxiUtil.PASSENGER_COLOR_TIME, SplashActivity.this).trim().equals(getCoreColorTime)) {
                            new SplashActivity.callColor(getCoreLangTime);

                        } else if (VersionCheck()) {
                            System.out.println("___________defffver");
                            versionAlert(SplashActivity.this);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    Intent i = null;
                                    if (SessionSave.getSession("Id", SplashActivity.this).equals("")) {
                                        if (!SessionSave.getSession("IsOTPSend", SplashActivity.this).equals("")) {
                                            i = new Intent(SplashActivity.this, VerificationAct.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(i);
                                            overridePendingTransition(0, 0);
                                            finish();
                                        } else {
                                            i = new Intent(SplashActivity.this, UserHomeAct.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(i);
                                            overridePendingTransition(0, 0);
                                            finish();
                                        }

                                    } else {
                                        if (SessionSave.getSession("trip_id", SplashActivity.this).equals("")) {
//                                            if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {
                                            i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);

                                            startActivity(i);
                                            overridePendingTransition(0, 0);
                                            finish();
                                            //}
                                        } else {
                                            if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {
                                                i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                                startActivity(i);
                                                overridePendingTransition(0, 0);
                                                finish();
                                            }
                                        }
                                    }
                                }
                            }, 1);
                        }
                    } else {
                        errorInSplash(json.getString("message"));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    errorInSplash(getString(R.string.server_con_error));
                    e.printStackTrace();
                }
            } else {
                errorInSplash(getString(R.string.server_error));
//                //runOnUiThread(new Runnable() {
//                    public void run() {
//                        ShowToast(SplashActivity.this, result);
//                    }
//                });
            }
        }
    }


    public void versionAlert(final Context mContext) {
        try {
            if (versionAlertDialog != null)
                versionAlertDialog.dismiss();
            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
            versionAlertDialog = new Dialog(mContext, R.style.NewDialog);
            versionAlertDialog.setContentView(view);
            FontHelper.applyFont(mContext, versionAlertDialog.findViewById(R.id.alert_id));
            versionAlertDialog.setCancelable(false);
            if (!versionAlertDialog.isShowing())
                versionAlertDialog.show();
            final TextView title_text = (TextView) versionAlertDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) versionAlertDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) versionAlertDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) versionAlertDialog.findViewById(R.id.button_failure);
            final View view1 = (View) versionAlertDialog.findViewById(R.id.view1);
            view1.setVisibility(View.GONE);
            button_failure.setVisibility(View.GONE);
            title_text.setText("" + mContext.getResources().getString(R.string.version_up_title));
            message_text.setText("" + mContext.getResources().getString(R.string.version_up_message));
            button_success.setText("" + mContext.getResources().getString(R.string.version_up_now));
            button_failure.setText("" + mContext.getResources().getString(R.string.version_up_later));
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName()));
                    mContext.startActivity(intent);
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub

                    //callApi();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Intent i = null;
                            if (SessionSave.getSession("Id", SplashActivity.this).equals("")) {

                                if (!SessionSave.getSession("IsOTPSend", SplashActivity.this).equals("")) {
                                    i = new Intent(SplashActivity.this, VerificationAct.class);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                    finish();
                                } else {
                                    i = new Intent(SplashActivity.this, UserHomeAct.class);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                    finish();
                                }

                            } else {
                                if (SessionSave.getSession("trip_id", SplashActivity.this).equals("")) {
                                    if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {
                                        i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(0, 0);
                                        finish();
                                    }
                                } else {
                                    if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {

                                        i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        // ActivityCompat.finishAffinity(SplashActivity.this);
                                        startActivity(i);
                                        overridePendingTransition(0, 0);
                                        finish();
                                    }
                                }
                            }
                        }
                    }, 1);


                    versionAlertDialog.dismiss();
//                    String url = "type=getcoreconfig";
//                    new CoreConfigCall(url);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean VersionCheck() {
        try {
            String newVersion = SessionSave.getSession("play_store_version", SplashActivity.this).equals("") ? "0" : SessionSave.getSession("play_store_version", SplashActivity.this);
            int curVersion = 0;
            PackageManager manager = getPackageManager();
            try {
                curVersion = manager.getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            System.err.println("New version" + newVersion + "curVersion" + curVersion);
            if (curVersion < value(newVersion))
                return true;
            else
                return false;
            //   }
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
        }
        return false;
    }

    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }


    private class callColor implements APIResult {
        public callColor(final String url) {
            // TODO Auto-generated constructor stub
            // new APIService_Retrofit_JSON_NoProgress(SplashActivity.this, this, null, true,TaxiUtil.API_BASE_URL.replace("/mobileapi117/index/","") + "/public/uploads/android/colorcode/passengerAppColors.xml").execute();

            new APIService_Retrofit_JSON_NoProgress(SplashActivity.this, this, null, true, SessionSave.getSession("colorcode", SplashActivity.this), true).execute();


        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                // System.out.println("nagaaaa"+result);
                // ShowToast(SplashActivity.this,"Color");
                SessionSave.saveSession(TaxiUtil.PASSENGER_COLOR_TIME, getCoreColorTime, SplashActivity.this);
                getAndStoreColorValues(result);
                SessionSave.saveSession("wholekeyColor", result, SplashActivity.this);
//                final String url = "type=getcoreconfig";
//                new CoreConfigCall(url);

                // if (VersionCheck())
                if (TaxiUtil.isCurrentTimeZone(getCore_Utc)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Intent i = null;
                            if (SessionSave.getSession("Id", SplashActivity.this).equals("")) {

                                if (!SessionSave.getSession("IsOTPSend", SplashActivity.this).equals("")) {
                                    i = new Intent(SplashActivity.this, VerificationAct.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                    finish();
                                } else {
                                    i = new Intent(SplashActivity.this, UserHomeAct.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(i);
                                    overridePendingTransition(0, 0);
                                    finish();
                                }

                            } else {
                                if (SessionSave.getSession("trip_id", SplashActivity.this).equals("")) {
                                    if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {
                                        i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {

                                        i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }
                        }
                    }, 1);
                } else {
                    cancelLoading();
                    CORE_CALLED = true;
                    errorInSplash(NC.getString(R.string.date_change));
                }
//                else
//                    versionAlert(SplashActivity.this);
                //final String url = "type=getcoreconfig";
                //new CoreConfigCall(url);

            } else
                errorInSplash(getString(R.string.server_con_error));

        }
    }


    private class callString implements APIResult {
        String color_time;

        public callString(final String color_time) {
            // TODO Auto-generated constructor stub
            this.color_time = color_time;

            String urls = SessionSave.getSession("currentStringUrl", SplashActivity.this);

            if (urls.equals("")) {
                urls = SessionSave.getSession(SessionSave.getSession("LANGDef", SplashActivity.this), SplashActivity.this);
                if (SessionSave.getSession("LANGTempDef", SplashActivity.this).trim().equalsIgnoreCase("RTL")) {
                    SessionSave.saveSession("Lang_Country", "ar_EG", SplashActivity.this);
                    SessionSave.saveSession("Lang", "ar", SplashActivity.this);
                    // System.out.println("LLLLTTTTT"+temptype+"____"+types);
                    Configuration config = new Configuration();
                    String langcountry = SessionSave.getSession("Lang_Country", SplashActivity.this);
                    String arry[] = langcountry.split("_");
                    config.locale = new Locale(arry[0], arry[1]);
                    Locale.setDefault(new Locale(arry[0], arry[1]));
                }

            }
            new APIService_Retrofit_JSON_NoProgress(SplashActivity.this, this, null, true, urls, true).execute();
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                // System.out.println("nagaaaa"+result);
                SessionSave.saveSession(TaxiUtil.PASSENGER_LANGUAGE_TIME, getCoreLangTime, SplashActivity.this);
                getAndStoreStringValues(result);
                SessionSave.saveSession("wholekey", result, SplashActivity.this);
                if (SessionSave.getSession("wholekeyColor", SplashActivity.this).trim().equals("") || !SessionSave.getSession(TaxiUtil.PASSENGER_COLOR_TIME, SplashActivity.this).equals(color_time))
                    new SplashActivity.callColor("");
                else {
                    //if (VersionCheck())
                    if (TaxiUtil.isCurrentTimeZone(getCore_Utc)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Intent i = null;
                                if (SessionSave.getSession("Id", SplashActivity.this).equals("")) {

                                    if (!SessionSave.getSession("IsOTPSend", SplashActivity.this).equals("")) {
                                        i = new Intent(SplashActivity.this, VerificationAct.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(i);
                                        overridePendingTransition(0, 0);
                                        finish();
                                    } else {
                                        i = new Intent(SplashActivity.this, UserHomeAct.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(i);
                                        overridePendingTransition(0, 0);
                                        finish();
                                    }

                                } else {
                                    if (SessionSave.getSession("trip_id", SplashActivity.this).equals("")) {
                                        if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {
                                            i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    } else {
                                        if (!SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashActivity.this).equalsIgnoreCase("0.0")) {

                                            i = new Intent(SplashActivity.this, MainHomeFragmentActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                }
                            }
                        }, 1);
                    } else {
                        //CToast.ShowToast(SplashActivity.this, "errrr");
                        cancelLoading();
                        CORE_CALLED = true;
                        errorInSplash(NC.getString(R.string.date_change));
                    }
//                    else
//                        versionAlert(SplashActivity.this);


//                    if (!SessionSave.getSession("base_url", SplashActivity.this).trim().equals("")) {
//                        TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", SplashActivity.this);
//                        TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", SplashActivity.this);
//                        TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", SplashActivity.this);
//                        String url = "type=getcoreconfig";
//                        new CoreConfigCall(url);
//
//                    } else {
//                        if (askDomain)
//                            getUrl();
//                        else
//                            urlApi("");
//
//                    }
                }
//
//                final String url = "type=getcoreconfig";
//                new CoreConfigCall(url);

            } else
                errorInSplash(getString(R.string.server_con_error));

        }
    }


    private synchronized void getAndStoreStringValues(String result) {
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

                }
            }
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void getValueDetail() {
        Field[] fieldss = R.string.class.getDeclaredFields();
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                fields.add(fieldss[i].getName());
                fields_value.add(getResources().getString(id));
                fields_id.put(fieldss[i].getName(), id);

            }
        }

        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            NC.nfields_byID.put(fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

    }

    public void setEditTextMaxLength(int length, EditText edt_text) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        edt_text.setFilters(FilterArray);
    }

    private void getUrl() {
        final View view = View.inflate(SplashActivity.this, R.layout.forgot_popup, null);
        if (urlPopup != null)
            urlPopup.dismiss();
        urlPopup = new Dialog(SplashActivity.this, R.style.NewDialog);
        urlPopup.setContentView(view);
        urlPopup.setCancelable(false);
        urlPopup.setCanceledOnTouchOutside(false);
        FontHelper.applyFont(SplashActivity.this, urlPopup.findViewById(R.id.inner_content));
        urlPopup.setCancelable(false);
        urlPopup.show();
        final EditText mail = (EditText) urlPopup.findViewById(R.id.forgotmail);
        //domain_hint
        mail.setHint("Enter your access key");
        final Button OK = (Button) urlPopup.findViewById(R.id.okbtn);
        OK.setText("Submit");
        int maxLength = 64;
        setEditTextMaxLength(maxLength, mail);
        View for_sepa = (View) urlPopup.findViewById(R.id.for_sep);
        for_sepa.setVisibility(View.GONE);

        final Button Cancel = (Button) urlPopup.findViewById(R.id.cancelbtn);
        Cancel.setVisibility(View.GONE);

        OK.setOnClickListener(new View.OnClickListener() {
            private String Email;

            @Override
            public void onClick(final View v) {
                try {

                    Email = mail.getText().toString();
                    if (Email.length() >= 3) {
                        urlPopup.dismiss();
                        if (!NO_NEED_TO_PLAY) {

                            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                            vv.setVideoURI(path);
                            vv.start();
                            vv.setVisibility(View.VISIBLE);
                            NO_NEED_TO_PLAY = !NO_NEED_TO_PLAY;
                        } else
                            //    showDialog();
                            urlApi(Email);
                        mail.setText("");
                    } else {
                        Toast.makeText(SplashActivity.this, "Please enter valid access key", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }


    private class GetAuthKey implements APIResult {

        public GetAuthKey() {
            // TODO Auto-generated constructor stub
            String url = "type=get_authentication";
            JSONObject j = new JSONObject();
            try {
                URL urls = new URL(SessionSave.getSession("base_url", SplashActivity.this));
                String host = urls.getHost();

                j.put("mobilehost", host);
                j.put("encode", SessionSave.getSession("encode", SplashActivity.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // showDialog();

            new APIService_Retrofit_JSON(SplashActivity.this, this, j, false).execute(url);
        }


        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub

            try {


                if (isSuccess) {

                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {

                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                        SessionSave.saveSession("encode", json.getString("encode"), SplashActivity.this);
                        TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", SplashActivity.this);
                        SessionSave.saveSession("auth_last_call_type", String.valueOf(ts), SplashActivity.this);

                        String url = "type=getcoreconfig";
                        new SplashActivity.CoreConfigCall(url);
                    } else {
                        cancelLoading();
                        String message = json.getString("message");
                        errorInSplash(message);
                    }
                } else {
                    cancelLoading();
                    errorInSplash(getString(R.string.server_con_error));
                }
            } catch (final JSONException e) {
                // TODO Auto-generated catch block
                cancelLoading();
                errorInSplash(getString(R.string.server_con_error));
                e.printStackTrace();
            }
        }
        // }
    }

    @Override
    protected void onStop() {

        if (errorDialog != null && errorDialog.isShowing())
            errorDialog.cancel();
        if (mgpsDialog != null && mgpsDialog.isShowing())
            mgpsDialog.cancel();
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
//        if (mGoogleApiClient != null)
//            mGoogleApiClient.disconnect();
        cancelLoading();
        super.onStop();
    }

    public void ifPermissionGranted() {

        if (isGpsEnabled(SplashActivity.this)) {

            //buildGoogleApiClient();
            if (NetworkStatus.isOnline(SplashActivity.this)) {

                init();
                //  showDialog();
                //  connectGoogleApi();
                if (VersionCheck() && TaxiUtil.isOnline(SplashActivity.this))
                    versionAlert(SplashActivity.this);
                else
                    callApi();
            } else
                errorInSplash(getString(R.string.check_internet_connection));

        } else {
            gpsalert(SplashActivity.this, false);
        }
    }

    public void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FontHelper.applyFont(SplashActivity.this, findViewById(R.id.rootlay));

        try {
            TaxiUtil.current_act = "SplashActivity";

            TaxiUtil.mDevice_id = Settings.Secure.getString(SplashActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

            if (!TaxiUtil.mDevice_id.equals("")) {
                SessionSave.saveSession("mDevice_id", TaxiUtil.mDevice_id, SplashActivity.this);
            }
//Have to check necessity
            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
            getApplicationContext().stopService(intent);
            if (SessionSave.getSession("Lang", SplashActivity.this).equals("")) {
                SessionSave.saveSession("Lang", "en", SplashActivity.this);
                SessionSave.saveSession("Lang_Country", "en_GB", SplashActivity.this);
            }

            //gcmRegister();


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (SessionSave.getSession("Lang", SplashActivity.this).equals("")) {
            SessionSave.saveSession("Lang", "en", SplashActivity.this);
            SessionSave.saveSession("Lang_Country", "en_GB", SplashActivity.this);
        }
        // check google play services for accept google map
    }

    public String gcmRegister() {
        context = getApplicationContext();
       // gcm = GoogleCloudMessaging.getInstance(this);
        if (TextUtils.isEmpty(SENDER_ID)) {
            // Toast.makeText(getApplicationContext(), "GCM Sender ID not set.",
            // Toast.LENGTH_LONG).show();
            return null;
        }
        if (TextUtils.isEmpty(REG_ID)) {
            //registerInBackground();
        }
        return REG_ID;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS: {
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //  connectGoogleApi();
                    callApi();
                } else {

                    finish();
                }
            }
            break;
        }
    }

    public void gpsalert(final Context mContext, boolean isconnect) {
        if (!isconnect) {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            mgpsDialog = new Dialog(mContext, R.style.NewDialog);
            mgpsDialog.setContentView(view);
            FontHelper.applyFont(mContext, mgpsDialog.findViewById(R.id.alert_id));


            mgpsDialog.setCancelable(false);
            if (!mgpsDialog.isShowing())
                mgpsDialog.show();
            final TextView title_text = (TextView) mgpsDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mgpsDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mgpsDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mgpsDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText("" + getResources().getString(R.string.location_disable));
            message_text.setText("" + getResources().getString(R.string.location_enable));
            button_success.setText("" + getResources().getString(R.string.enable));
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(mIntent);
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mgpsDialog.dismiss();
                }
            });
        } else {
            try {

                if (mgpsDialog != null && mgpsDialog.isShowing())
                    mgpsDialog.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }


    public void showDialog() {
        try {

            if (NetworkStatus.isOnline(this)) {
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.cancel();
                View view = View.inflate(this, R.layout.progress_bar, null);
                loadingDialog = new Dialog(this, R.style.dialogwinddow);
                loadingDialog.setContentView(view);
                loadingDialog.setCancelable(false);
                if (this != null)
                    loadingDialog.show();

                ImageView iv = (ImageView) loadingDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(this)
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);

            } else {
                errorInSplash(getString(R.string.check_internet_connection));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void errorInSplash(String message) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();

                final View view = View.inflate(SplashActivity.this, R.layout.netcon_lay, null);
                errorDialog = new Dialog(SplashActivity.this, R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(SplashActivity.this, errorDialog.findViewById(R.id.alert_id));
                errorDialog.show();
                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
                title_text.setText("" + getResources().getString(R.string.message));
                message_text.setText("" + message);
                button_success.setText("" + getResources().getString(R.string.try_again));
                button_failure.setText("" + getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        errorDialog.dismiss();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub


                        Activity activity = (Activity) SplashActivity.this;

                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        errorDialog.dismiss();

                    }
                });
            } else {
                try {
                    errorDialog.dismiss();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
//    }
//
//    public void connectGoogleApi() {
//        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.connect();
//        } else {
//            buildGoogleApiClient();
//            mGoogleApiClient.connect();
//        }
//    }


//    private void SaveLocation() {
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            System.out.println("positionFirst--s" + mLastLocation + "__" + mGoogleApiClient);
//            if (mLastLocation != null) {
//                System.out.println("positionFirst--v");
//                latitude = mLastLocation.getLatitude();
//                longitude = mLastLocation.getLongitude();
//                SessionSave.saveSession("PLAT", "" + latitude, SplashActivity.this);
//                SessionSave.saveSession("PLNG", "" + longitude, SplashActivity.this);
//                getaddressfrom();
//            } else {
//                System.out.println("positionFirst--ds");
//                mLastLocation = getLastKnownLoaction(true);
//                if (mLastLocation != null) {
//                    System.out.println("positionFirst--hs");
//                    latitude = mLastLocation.getLatitude();
//                    longitude = mLastLocation.getLongitude();
////                BookTaxiNewFrag.P_latitude = latitude;
////                BookTaxiNewFrag.P_longitude = longitude;
//                    SessionSave.saveSession("PLAT", "" + latitude, SplashActivity.this);
//                    SessionSave.saveSession("PLNG", "" + longitude, SplashActivity.this);
//                    getaddressfrom();
//                }
//
//            }
//        }
////        System.out.println("splashappLat----" +mLastLocation.getAccuracy()+"+++++"+latitude+"__"+longitude);
//    }
//
//    private Location getLastKnownLoaction(boolean enabledProvidersOnly) {
//        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        Location location = null;
//        List<String> providers = manager.getProviders(enabledProvidersOnly);
//        for (String provider : providers) {
//            location = manager.getLastKnownLocation(provider);
//            // may be try adding some Criteria here
//            if (location != null)
//                return location;
//        }
//        // at this point we've done all we can and no location is returned
//        return null;
//    }

    public void cancelLoading() {
        if (loadingDialog != null && SplashActivity.this != null)
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
    }

    private void getaddressfrom() {

        Geocoder geocoder = new Geocoder(SplashActivity.this, Locale.getDefault());
        status_text.setText(getString(R.string.getting_auth));
        progressBar1.setProgress(50);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address obj = addresses.get(0);

                SessionSave.saveSession("country_name", "" + obj.getCountryName(), SplashActivity.this);
                SessionSave.saveSession("country_names", "" + obj.getCountryCode(), SplashActivity.this);
                CURRENT_COUNTRY_CODE = obj.getCountryCode();
                phaseCompleted = 1;
                cancelLoading();
                callApi();

            } else {
                Locale locale;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    locale = getResources().getConfiguration().getLocales().get(0);
                } else {
                    locale = getResources().getConfiguration().locale;
                }
                SessionSave.saveSession("country_name", "" + locale.getDisplayCountry(), SplashActivity.this);
                SessionSave.saveSession("country_names", "" + locale.getCountry(), SplashActivity.this);


                CURRENT_COUNTRY_CODE = locale.getCountry();
                phaseCompleted = 1;
                callApi();
                cancelLoading();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = getResources().getConfiguration().locale;
            }
            SessionSave.saveSession("country_name", "" + locale.getDisplayCountry(), SplashActivity.this);
            SessionSave.saveSession("country_names", "" + locale.getCountry(), SplashActivity.this);


            CURRENT_COUNTRY_CODE = locale.getCountry();
            phaseCompleted = 1;
            callApi();
            cancelLoading();
            e.printStackTrace();
        }
    }


    public boolean isGpsEnabled(Context context) {
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        return true;
//        else
//            return false;
    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        System.out.println("onconnected");
//        createLocationRequest();
//
//    }

//    private void createLocationRequest() {
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    mLocationRequest, this);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (status_text.getText().toString().trim().contains(getString(R.string.getting_gps)) && SplashActivity.this != null) {
//                        try {
//                            errorInSplash(getString(R.string.error_in_getting_gps));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }, 30000);
//        } else {
//            errorInSplash(getString(R.string.error_in_getting_gps));
//            //  connectGoogleApi();
//        }
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        errorInSplash(getString(R.string.error_in_getting_gps));
//        Log.i("TAXI", "Connection sus: ConnectionResult.getErrorCode() = ");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        errorInSplash(getString(R.string.error_in_getting_gps));
//        Log.i("TAXI", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        if (errorDialog == null || (errorDialog != null && !errorDialog.isShowing())) {
//            if (latitude == 0) {
//                System.out.println("connectedlocationchanged" + latitude);
//                status_text.setText(getString(R.string.getting_code));
//                SaveLocation();
//            } else if (CURRENT_COUNTRY_CODE.equals(""))
//                getaddressfrom();
//            else
//                cancelLoading();
//        }
//
//    }

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            } else {
                return false;
                // not connected to the internet
            }
        }
        return false;
    }
}
