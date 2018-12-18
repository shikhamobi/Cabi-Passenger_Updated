//package com.Taximobility;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.StrictMode;
//import android.provider.Settings.Secure;
//import android.support.v4.app.ActivityCompat;
//import android.text.InputFilter;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.cabi.data.apiData.ApiRequestData;
//import com.cabi.data.apiData.CompanyDomainResponse;
//import com.cabi.interfaces.APIResult;
//import com.cabi.service.APIService_Retrofit_JSON;
//import com.cabi.service.APIService_Retrofit_JSON_NoProgress;
//import com.cabi.service.AuthService;
//import com.cabi.service.CoreClient;
//import com.cabi.service.GetPassengerUpdate;
//import com.cabi.service.ServiceGenerator;
//import com.cabi.util.CL;
//import com.cabi.util.Colorchange;
//import com.cabi.util.FontHelper;
//import com.cabi.util.GpsStatus;
//import com.cabi.util.NC;
//import com.cabi.util.NetworkStatus;
//import com.cabi.util.SessionSave;
//import com.cabi.util.TaxiUtil;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * <p>
// * this class is used to show splash screen <br>
// * when the app launches
// * <p/>
// * it extends Mainactivity as well as implements ConnectionCallbacks <br>
// * OnConnectionFailedListener to use google api clients
// * <p/>
// * </p>
// *
// * @author developer
// */
//public class SplashAct extends MainActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
//
//    private static final int MY_PERMISSIONS_REQUEST_GPS = 111;
//    private static boolean CORE_CALLED = false;
//    public static String CURRENT_COUNTRY_CODE = "";
//    public static double latitude = 181;
//    public static double longitude;
//    private GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    private TextView buildnoTxt;
//    private String VersionNo = "";
//    private static Dialog mverDialog;
//    public static boolean isBUISNESSKEY = false;
//    public static String SENDER_ID = "612632263740";
//    public static String REG_ID;
//    //  public static String CURRENT_COUNTRY_CODE = "IN";
//    private GoogleCloudMessaging gcm;
//    private Context context;
//    private int code;
//    private VideoView vv;
//    public static ArrayList<String> fields = new ArrayList<>();
//    public static ArrayList<String> fields_value = new ArrayList<>();
//
//    public static HashMap<String, Integer> fields_id = new HashMap<>();
//    private Dialog mDialog;
//    private boolean askDomain = true;
//    public static boolean NO_NEED_TO_PLAY;
//    private static int UPDATE_INTERVAL = 5000; // 10 sec
//    private static int FASTEST_INTERVAL = 1000;
//
//    private long getCore_Utc;
//    private String getCoreLangTime;
//    private String getCoreColorTime;
//    private boolean CompanyResponseError;
//    ProgressBar progressBar1;
//    TextView status_text;
//    private LocationRequest mLocationRequest;
//    private Dialog errorDialog;
//    private CountDownTimer countdownTimer;
//    private long millisUntilFinishedt;
//
//    /**
//     * <p>
//     * This method allows you to set the layout of the container
//     * </p>
//     *
//     * @author developer
//     */
//
//    @Override
//    public int setLayout() {
//        // TODO Auto-generated method stub
//        //  TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", SplashAct.this);
//        TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", SplashAct.this);
//        //  TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", SplashAct.this);
//        return R.layout.splashlay;
//    }
//
//    @Override
//    public void priorChanges() {
//        super.priorChanges();
//        try {
//            Glide.with(SplashAct.this).load(SessionSave.getSession("image_path", SplashAct.this) + "signInLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.body_iv));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void setLocale() {
//        if (SessionSave.getSession("Lang", SplashAct.this).equals("")) {
//
//            if (SessionSave.getSession("LANGCode", this).trim().equals(""))
//                SessionSave.saveSession("Lang", "en", SplashAct.this);
//            else
//                SessionSave.saveSession("Lang", SessionSave.getSession("LANGCode", this), this);
//            SessionSave.saveSession("Lang_Country", "en_US", SplashAct.this);
//        }
//        Configuration config = new Configuration();
//        String langcountry = SessionSave.getSession("Lang_Country", SplashAct.this);
//        String arry[] = langcountry.split("_");
//        config.locale = new Locale(arry[0], arry[1]);
//        Locale.setDefault(new Locale(arry[0], arry[1]));
//        SplashAct.this.getBaseContext().getResources().updateConfiguration(config, SplashAct.this.getResources().getDisplayMetrics());
//
//    }
//
//    @Override
//    public void Initialize() {
//        // TODO Auto-generated method stub
//        //   setMobileDataEnabled(SplashAct.this, true);
//        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
//        status_text = (TextView) findViewById(R.id.status_text);
//        vv = (VideoView) findViewById(R.id.videoView);
//        // com.example.mylibrary.EncryptDecrypt
//
//
//    }
//
//    /**
//     * <p>
//     * GcmRegister needs to register with GCM connection servers before it can receive messages. When an app registers, it receives a registration token and sends it to the app server. The client app should store a boolean value indicating whether the registration token has been sent to the server.
//     * </p>
//     *
//     * @author developer
//     */
//
//    public String gcmRegister() {
//        context = getApplicationContext();
//        gcm = GoogleCloudMessaging.getInstance(this);
//        if (TextUtils.isEmpty(SENDER_ID)) {
//            // Toast.makeText(getApplicationContext(), "GCM Sender ID not set.",
//            // Toast.LENGTH_LONG).show();
//            return null;
//        }
//        if (TextUtils.isEmpty(REG_ID)) {
//            registerInBackground();
//        }
//        return REG_ID;
//    }
//
//
//    /**
//     * <p>
//     * The init method is the first method called in an Applet or JApplet
//     * </p>
//     *
//     * @author developer
//     */
//
//    public void init() {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        FontHelper.applyFont(SplashAct.this, findViewById(R.id.rootlay));
////        try {
////            VersionNo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
////            buildnoTxt = (TextView) findViewById(R.id.buildnoTxt);
////            buildnoTxt.setText("" + getResources().getString(R.string.app_buildversion) + ":" + VersionNo);
////        } catch (Exception e) {
////            // perTODO Auto-generated catch block
////            e.printStackTrace();
////        }
//
//        try {
//            TaxiUtil.current_act = "SplashAct";
//
////            mGpsLocationTracker = new GpsLocationTracker(SplashAct.this);
////            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
////            mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
////            mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            TaxiUtil.mDevice_id = Secure.getString(SplashAct.this.getContentResolver(), Secure.ANDROID_ID);
//
//            if (!TaxiUtil.mDevice_id.equals("")) {
//                SessionSave.saveSession("mDevice_id", TaxiUtil.mDevice_id, SplashAct.this);
//            }
////Have to check necessity
//            Intent intent = new Intent(getApplicationContext(), GetPassengerUpdate.class);
//            getApplicationContext().stopService(intent);
//            if (SessionSave.getSession("Lang", SplashAct.this).equals("")) {
//                SessionSave.saveSession("Lang", "en", SplashAct.this);
//                SessionSave.saveSession("Lang_Country", "en_GB", SplashAct.this);
//            }
////            ndot
//
//
////            if(SessionSave.getSession("domain", SplashAct.this).equals("")){
////                SessionSave.saveSession("domain", "", SplashAct.this);
////                URL u;
////                try {
////
////                    // Log.d("version", "version"+version);
////
////                    u = new URL("http://testingtaxi.taximobility.com");
////
////                    HttpURLConnection huc = (HttpURLConnection) u.openConnection();
////                    huc.setRequestMethod("GET"); //98765432101 OR huc.setRequestMethod
////                    // ("HEAD");
////                    huc.connect();
////                    code = huc.getResponseCode();
////                    System.out.println(code);
////
////                } catch (Exception e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                }
////
////            }else{
////
////            }
//
//            gcmRegister();
//
//
//        }
////        catch (NameNotFoundException e1) {
////            e1.printStackTrace();
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
//        //    }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        if (SessionSave.getSession("Lang", SplashAct.this).equals("")) {
//            SessionSave.saveSession("Lang", "en", SplashAct.this);
//            SessionSave.saveSession("Lang_Country", "en_GB", SplashAct.this);
//        }
//        // check google play services for accept google map
//        if (checkPlayServices()) {
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//        }
//    }    // check last known location from location provider
//
//
//    /**
//     * <p>
//     * The method returns a Location indicating the data from the last known location fix obtained from the given provider.
//     * </p>
//     *
//     * @author developer
//     */
//
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
//
//
//    /**
//     * <p>
//     * The GCM registration process in background
//     * </p>
//     *
//     * @author developer
//     */
//
//    private void registerInBackground() {
//
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(context);
//                    }
//                    REG_ID = gcm.register(SENDER_ID);
//                    Log.d("RegisterActivity", "registerInBackground - regId: " + REG_ID);
//                    msg = "Device registered, registration ID=" + REG_ID;
//
//                } catch (SecurityException e) {
//                    e.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    msg = "Error :" + ex.getMessage();
//                    Log.d("RegisterActivity", "Error: " + msg);
//                }
//                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                try {
//                    if (REG_ID != null)
//                        if (REG_ID.length() != 0) {
//                            SessionSave.saveSession("device_token", "" + REG_ID,
//                                    SplashAct.this);
//                        }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        }.execute(null, null, null);
//        // EC.decrypt()
//    }
//
//
//    /**
//     * <p>
//     * Method to display the get current location
//     * </p>
//     *
//     * @author developer
//     */
//
//    private void createLocationRequest() {
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        System.out.println("connected");
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            System.out.println("connected1");
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    mLocationRequest, this);
//        } else {
//            System.out.println("connected2");
//            connectGoogleApi();
//        }
//    }
//
//    private void SaveLocation() {
//
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            System.out.println("positionFirst--s" + mLastLocation + "__" + mGoogleApiClient);
//            if (mLastLocation != null) {
//                System.out.println("positionFirst--v");
//                latitude = mLastLocation.getLatitude();
//                longitude = mLastLocation.getLongitude();
////            BookTaxiNewFrag.P_latitude = latitude;
////            BookTaxiNewFrag.P_longitude = longitude;
//                SessionSave.saveSession("PLAT", "" + latitude, SplashAct.this);
//                SessionSave.saveSession("PLNG", "" + longitude, SplashAct.this);
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
//                    SessionSave.saveSession("PLAT", "" + latitude, SplashAct.this);
//                    SessionSave.saveSession("PLNG", "" + longitude, SplashAct.this);
//                    getaddressfrom();
//                }
//
//            }
//        }
////        System.out.println("splashappLat----" +mLastLocation.getAccuracy()+"+++++"+latitude+"__"+longitude);
//    }
//
//    /**
//     * Creating google api client object
//     */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Method to verify google play services on the device
//     */
//    private boolean checkPlayServices() {
//        try {
//            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
//            int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
//            if (resultCode != ConnectionResult.SUCCESS) {
//
//
//                if (googleApiAvailability.isUserResolvableError(resultCode)) {
//                    googleApiAvailability.getErrorDialog((Activity) getApplicationContext(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    protected void onStart() {
//        // TODO Auto-generated method stub
//        super.onStart();
//        countdownTimer = new CountDownTimer(120000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                //millisUntilFinishedt= millisUntilFinished;
//                System.out.println("---tickkkkkk" + millisUntilFinished / 10000);
//
//                if (true) {
//                    System.out.println("---tickkkkkk");
//                    System.out.println("---rotae0");
//                    if (NetworkStatus.isOnline(SplashAct.this)) {
//                        gpsalert(SplashAct.this, isGpsEnabled(SplashAct.this));
//                    } else {
//                        errorInSplash(getString(R.string.check_internet_connection));
//                    }
//
//                }
//                try {
//                    Thread.sleep(10000);
//                } catch (Exception e) {
//
//                }
//            }
//
//            public void onFinish() {
//                errorInSplash(getString(R.string.check_internet_connection));
//                System.out.println("---googlecon3344");
//            }
//        }.start();
//        System.out.println("---googleconnectstart");
//        status_text.setText(getString(R.string.getting_gps));
//        new Handler().postDelayed(new Runnable() {
//            //
//            @Override
//            public void run() {
//                if (NetworkStatus.isOnline(SplashAct.this)) {
////                        if (errorDialog != null && errorDialog.isShowing())
////                            errorDialog.cancel();
//                    if (isGpsEnabled(SplashAct.this)) {
//                        System.out.println("---gpssss");
//
//                        if (status_text.getText().toString().trim().equals(getString(R.string.getting_gps))) {
////                                if (SessionSave.getSession("country_name", SplashAct.this).trim().equals(""))
////                                    getaddressfrom();
////                                else
////                                    callApi();
//                            if (latitude == 181) {
//                                if (ActivityCompat.checkSelfPermission(SplashAct.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashAct.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                    ActivityCompat.requestPermissions(SplashAct.this,
//                                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
//                                            MY_PERMISSIONS_REQUEST_GPS);
//                                } else {
//                                    System.out.println("---rotae1");
//                                    //if (NetworkStatus.isOnline(SplashAct.this)) {
//                                    ifPermissionGranted();
////                        } else
////                            errorInSplash(getString(R.string.check_internet_connection));
//                                    //Toast.makeText(SplashAct.this, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
//                                }
//                            } else if (CURRENT_COUNTRY_CODE.equals("")) {
//                                status_text.setText(getString(R.string.getting_code));
//                                showDialog();
//                                getaddressfrom();
//
//                            } else {
//                                status_text.setText(getString(R.string.getting_auth));
//                                NO_NEED_TO_PLAY = true;
//                                showDialog();
//                                callApi();
//                            }
//                        }
//
//
//                    } else
//                        gpsalert(SplashAct.this, true);
//
//                    System.out.println("---googlecon33");
//                } else {
//                    errorInSplash(getString(R.string.check_internet_connection));
//                    System.out.println("---googlecon3344");
//                }
//            }
//        }, 100);
//
////        new Handler().postDelayed(new Runnable() {
////
////            @Override
////            public void run() {
////
////            }
////        }, 100);
//
//    }
//
//
//    public void errorInSplash(String message) {
//        try {
//            if (true) {
//                if (errorDialog != null && errorDialog.isShowing())
//                    errorDialog.dismiss();
//                System.out.println("setCanceledOnTouchOutside" + message);
//                final View view = View.inflate(SplashAct.this, R.layout.netcon_lay, null);
//                errorDialog = new Dialog(SplashAct.this, R.style.dialogwinddow);
//                errorDialog.setContentView(view);
//                errorDialog.setCancelable(false);
//                errorDialog.setCanceledOnTouchOutside(false);
//                FontHelper.applyFont(SplashAct.this, errorDialog.findViewById(R.id.alert_id));
//                errorDialog.show();
//                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
//                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
//                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
//                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
//                title_text.setText("" + NC.getResources().getString(R.string.message));
//                message_text.setText("" + message);
//                button_success.setText("" + NC.getResources().getString(R.string.try_again));
//                button_failure.setText("" + NC.getResources().getString(R.string.cancel));
//                button_success.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        // TODO Auto-generated method stub
//
////                        Intent intent = new Intent(SplashAct.this, SplashAct.class);
////                        startActivity(intent);
////                        finish();
//                        Intent intent = new Intent(SplashAct.this, SplashAct.this.getClass());
//                        Activity activity = (Activity) SplashAct.this;
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        SplashAct.this.startActivity(intent);
//                        System.out.println("tryAgain2");
//                        activity.finish();
//                        errorDialog.dismiss();
//
//                    }
//                });
//                button_failure.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        // TODO Auto-generated method stub
//
//
//                        Activity activity = (Activity) SplashAct.this;
//
//                        final Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        activity.startActivity(intent);
//                        activity.finish();
//                        errorDialog.dismiss();
//
//                    }
//                });
//            } else {
//                try {
//                    errorDialog.dismiss();
//                    if (SplashAct.this != null) {
//                        Intent intent = new Intent(SplashAct.this, SplashAct.this.getClass());
//                        SplashAct.this.startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * <p>
//     * Method to connect google api services
//     * </p>
//     *
//     * @author developer
//     */
//
//
//    public void connectGoogleApi() {
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        } else {
//            buildGoogleApiClient();
//            mGoogleApiClient.connect();
//            System.out.println("---googleconnectstartnull");
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        TaxiUtil.closeDialog(mverDialog);
//        if (errorDialog != null && errorDialog.isShowing())
//            errorDialog.cancel();
//        if (countdownTimer != null)
//            countdownTimer.cancel();
//        if (mDialog != null)
//            mDialog.dismiss();
//        if (mGoogleApiClient != null)
//            mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        //Toast.makeText(SplashAct.this, "locationchanged", Toast.LENGTH_SHORT).show();
//        System.out.println("connectedlocationchanged" + latitude);
//        if (latitude == 181) {
//            System.out.println("connectedlocationchanged" + latitude);
//            status_text.setText(getString(R.string.getting_code));
//            SaveLocation();
//        } else if (CURRENT_COUNTRY_CODE.equals(""))
//            getaddressfrom();
//
//    }
//
//
//    // check application version with play store
//    @SuppressLint("NewApi")
//    private boolean VersionCheck() {
//        try {
//            System.out.println("_callingrrrrgeeeAPP_VERSION" + APP_VERSION);
//            if (APP_VERSION != null) {
//                System.out.println("_callingrrrrgeeeAPP_VERSION" + APP_VERSION);
////                PackageInfo info = null;
////                PackageManager manager = getPackageManager();
////                try {
////                    info = manager.getPackageInfo(getPackageName(), 0);
////                    APP_VERSION = info.versionName;
////                    System.out.println("splashappV----" + APP_VERSION);
////                } catch (PackageManager.NameNotFoundException e) {
////                    e.printStackTrace();
////                }
////            }
////            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////            StrictMode.setThreadPolicy(policy);
////            String curVersion = APP_VERSION;
////            Package pack = SplashAct.this.getClass().getPackage();
////            String packtxt = pack.toString();
//////            String packUrl = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
//////                    .timeout(30000)
//////                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//////                    .referrer("http://www.google.com")
//////                    .get()
//////                    .select("div[itemprop=softwareVersion]")
//////                    .first()
//////
//////                    .ownText();
//////                    .select("div[itemprop=softwareVersion]")
//////                    .first()
//////                    .ownText();
////            // String packUrl = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en";
////            // System.out.println("packageUrl" + packUrl.replaceAll("\\s", ""));
////            //   org.jsoup.nodes.Element element= Jsoup.connect(packUrl).timeout(30000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get().select("div[itemprop=softwareVersion]").first();
//////            VersionChecker versionChecker = new VersionChecker();
//                String newVersion = SessionSave.getSession("play_store_version", SplashAct.this).equals("") ? "0" : SessionSave.getSession("play_store_version", SplashAct.this);
//                int curVersion = 0;
//                PackageManager manager = getPackageManager();
//                try {
//                    curVersion = manager.getPackageInfo(getPackageName(), 0).versionCode;
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                System.err.println("New version" + newVersion + "curVersion" + curVersion);
//                if (curVersion < value(newVersion))
//                    return true;
//                else
//                    return false;
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private long value(String string) {
//        string = string.trim();
//        if (string.contains(".")) {
//            final int index = string.lastIndexOf(".");
//            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
//        } else {
//            return Long.valueOf(string);
//        }
//    }
//
//    synchronized void getValueDetail() {
//        // System.out.println("NagarrrHIIIIIII");
//        Field[] fieldss = R.string.class.getDeclaredFields();
//        // fields =new int[fieldss.length];
//        //  System.out.println("NagarrrHIIIIIII***" + fieldss.length + "___" + NC.nfields_byName.size());
//        for (int i = 0; i < fieldss.length; i++) {
//            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getPackageName());
//            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
//                fields.add(fieldss[i].getName());
//                fields_value.add(getResources().getString(id));
//                fields_id.put(fieldss[i].getName(), id);
//
//            } else {
//                //     System.out.println("Imissedthepunchrefree" + fieldss[i].getName());
//            }
////        for(int h=0;h<NC.nfields_byName.size();h++){
////            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
////        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
//        }
//
//        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
//            String h = entry.getKey();
//            String value = entry.getValue();
//            //  System.out.println("NagarrrHIIIIIII&&&" + fields_id.get(h) + "____" + NC.nfields_byName.get(h));
//            NC.nfields_byID.put(fields_id.get(h), NC.nfields_byName.get(h));
//            // do stuff
//        }
//
//        //System.out.println("NagarrrBye" + fields.size() + "___" + fields_value.size() + "___" + fields_id.size());
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isGpsEnabled(SplashAct.this)) {
//            gpsalert(SplashAct.this, true);
//        }
//
//        //   SvgImage(SplashAct.this,R.id.imgView,R.raw.imgView);
//
//        //      SvgImage rainSVG = new SvgImage(SplashAct.this, R.id.imgView, R.raw.car);
////        rainSVG
//
//
//    }
//
//    public void startApiProcess() {
//
//
//        if (!NO_NEED_TO_PLAY) {
//
//            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
//            vv.setVideoURI(path);
//
//            vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    if (!CORE_CALLED && !CompanyResponseError)
//                        showDialog();
//
//                }
//            });
//
//        } else {
//
//        }
//
//    }
//
//    /**
//     * <p>
//     * Beginning in Android 6.0 (API level 23), users grant permissions to apps while the app is running, not when they install the app.
//     * This approach streamlines the app install process, since the user does not need to grant permissions when they install or update the app.
//     * It also gives the user more control over the app's functionality; for example, a user could choose to give a camera app access to the camera but not to the device location.
//     * The user can revoke the permissions at any time, by going to the app's Settings screen.
//     * </p>
//     *
//     * @author developer
//     */
//
//
//    public void ifPermissionGranted() {
//
//        if (isGpsEnabled(SplashAct.this)) {
//            showDialog();
//            init();
//            buildGoogleApiClient();
//            if (TaxiUtil.isOnline(SplashAct.this))
//                connectGoogleApi();
////                if (VersionCheck())
////                    versionAlert(SplashAct.this);
////                else {
////
////                    connectGoogleApi();
////                    // gpsalert(SplashAct.this, true);
////                }
//            else
//                errorInSplash(getString(R.string.check_internet_connection));
////showDialog();
//
//
//            // }
//
//            //  startService(new Intent(SplashAct.this, LatestLatLng.class));
//        } else {
//            gpsalert(SplashAct.this, false);
//        }
//    }
//
//    //connectGoogleApi()
//    void callApi() {
//        if (!SessionSave.getSession("base_url", SplashAct.this).trim().equals("")) {
//
//            vv = (VideoView) findViewById(R.id.videoView);
//
//
//            if (!NO_NEED_TO_PLAY) {
//
//                Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
//                vv.setVideoURI(path);
//
//                vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
////                        if (!CORE_CALLED && !CompanyResponseError)
////                            showDialog();
//
//                    }
//                });
//                vv.start();
//                vv.setVisibility(View.VISIBLE);
//                NO_NEED_TO_PLAY = !NO_NEED_TO_PLAY;
//            }
//            System.out.println("---rotae2");
//            if (NetworkStatus.isOnline(SplashAct.this))
//                new GetAuthKey();
//            else
//                errorInSplash(getString(R.string.check_internet_connection));
//        }
//        // System.out.println("sdfs");
//        else {
//            setLocale();
////                getAndStoreStringValues(SessionSave.getSession("wholekey", SplashAct.this));
////
////                if (!SessionSave.getSession("base_url", SplashAct.this).trim().equals("")) {
////                    TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", SplashAct.this);
////                    TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", SplashAct.this);
////                    TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", SplashAct.this);
////
////
////                    new GetAuthKey();
////                } else {
//            if (askDomain) {
//                cancelLoading();
//                System.out.println("---rotae3");
//                if (NetworkStatus.isOnline(SplashAct.this))
//                    getUrl();
//                else
//                    errorInSplash(getString(R.string.check_internet_connection));
//            } else
//                urlApi("");
//
//            //  }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_GPS: {
//                // If request is cancelled, the result arrays are empty.
//                //    System.out.println("___"+grantResults.length+"____"+grantResults[0]);
//                if (grantResults.length > 0
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    // getGPS();
//                    // startActivity(new Intent(SplashAct.this,SplashAct.class));
//                    connectGoogleApi();
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    finish();
//                }
//                //  return;
//            }
//            break;
//        }
//    }
//
//    /**
//     * this class is used to get authentication key
//     */
//
//    private class GetAuthKey implements APIResult {
//
//        public GetAuthKey() {
//            // TODO Auto-generated constructor stub
//            String url = "type=get_authentication";
//            JSONObject j = new JSONObject();
//            try {
//                URL urls = new URL(SessionSave.getSession("base_url", SplashAct.this));
//                String host = urls.getHost();
//                System.out.println("domianurl_________" + host + "___" + SessionSave.getSession("encode", SplashAct.this));
//                j.put("mobilehost", host);
//                j.put("encode", SessionSave.getSession("encode", SplashAct.this));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("URL_" + url);
//            new APIService_Retrofit_JSON(SplashAct.this, this, j, false).execute(url);
//        }
//
//
//        @Override
//        public void getResult(final boolean isSuccess, final String result) {
//            // TODO Auto-generated method stub
//
//            try {
//                System.out.println("_callingrrrrgeee1");
//                if (isSuccess) {
//                    System.out.println("_callingrrrrgeee2");
//                    final JSONObject json = new JSONObject(result);
//                    if (json.getInt("status") == 1) {
//                        System.out.println("_callingrrrrgeee3");
//                        Long tsLong = System.currentTimeMillis() / 1000;
//                        String ts = tsLong.toString();
//                        SessionSave.saveSession("encode", json.getString("encode"), SplashAct.this);
//                        //   TaxiUtil.DYNAMIC_AUTH_KEY = SessionSave.getSession("encode", SplashAct.this);
//                        TaxiUtil.API_BASE_URL = SessionSave.getSession("base_url", SplashAct.this);
//                        // TaxiUtil.COMPANY_KEY = SessionSave.getSession("api_key", SplashAct.this);
//                        SessionSave.saveSession("auth_last_call_type", String.valueOf(ts), SplashAct.this);
////                        if (SessionSave.getSession("wholekey", SplashAct.this).equals(""))
////                            new callString("");
////                        else {
//
//                        String url = "type=getcoreconfig";
//                        new CoreConfigCall(url);
//                        // }
//                    } else {
//                        String message = json.getString("message");
//                        errorInSplash(message);
//                    }
//                    //   ShowToast(SplashAct.this, json.getString("message"));
//                    // alert_view(this, "" + getResources().getString(R.string.message), "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
////                        if (json.getInt("status") == 2)
////                            alert_view(this, "" + getResources().getString(R.string.message), "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
//                } else {
//                    errorInSplash(getString(R.string.server_con_error));
////                        runOnUiThread(new Runnable() {
////                            public void run() {
////                                ShowToast(this, result);
////                            }
////                        });
//                }
//            } catch (final JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        // }
//    }
//
//    /**
//     * this class is used to call the string files
//     */
//
//    private class callString implements APIResult {
//        String color_time;
//
//        public callString(final String color_time) {
//            // TODO Auto-generated constructor stub
//            //  progressBar1.setProgress(50);
//            this.color_time = color_time;
//            //ShowToast(SplashAct.this,"String");
//
//            String urls = SessionSave.getSession("currentStringUrl", SplashAct.this);
//            Log.e("link__lang", urls + "___" + SessionSave.getSession("currentStringUrl", SplashAct.this));
//            if (urls.equals("")) {
//                urls = SessionSave.getSession(SessionSave.getSession("LANGDef", SplashAct.this), SplashAct.this);
//                if (SessionSave.getSession("LANGTempDef", SplashAct.this).trim().equalsIgnoreCase("RTL")) {
//                    SessionSave.saveSession("Lang_Country", "ar_EG", SplashAct.this);
//                    SessionSave.saveSession("Lang", "ar", SplashAct.this);
//                    // System.out.println("LLLLTTTTT"+temptype+"____"+types);
//                    Configuration config = new Configuration();
//                    String langcountry = SessionSave.getSession("Lang_Country", SplashAct.this);
//                    String arry[] = langcountry.split("_");
//                    config.locale = new Locale(arry[0], arry[1]);
//                    Locale.setDefault(new Locale(arry[0], arry[1]));
//                }
//
//            }
//            new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, null, true, urls, true).execute();
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, String result) {
//
//            if (isSuccess) {
//                // System.out.println("nagaaaa"+result);
//                SessionSave.saveSession(TaxiUtil.PASSENGER_LANGUAGE_TIME, getCoreLangTime, SplashAct.this);
//                getAndStoreStringValues(result);
//                SessionSave.saveSession("wholekey", result, SplashAct.this);
//                if (SessionSave.getSession("wholekeyColor", SplashAct.this).trim().equals("") || !SessionSave.getSession(TaxiUtil.PASSENGER_COLOR_TIME, SplashAct.this).equals(color_time))
//                    new callColor("");
//                else {
//                    //if (VersionCheck())
//                    if (TaxiUtil.isCurrentTimeZone(getCore_Utc)) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // TODO Auto-generated method stub
//                                Intent i = null;
//                                if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
//
//                                    if (!SessionSave.getSession("IsOTPSend", SplashAct.this).equals("")) {
//                                        i = new Intent(SplashAct.this, VerificationAct.class);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(i);
//                                        overridePendingTransition(0, 0);
//                                        finish();
//                                    } else {
//                                        i = new Intent(SplashAct.this, UserHomeAct.class);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(i);
//                                        overridePendingTransition(0, 0);
//                                        finish();
//                                    }
//
//                                } else {
//                                    if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
//                                        if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//                                            i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                            startActivity(i);
//                                            finish();
//                                        }
//                                    } else {
//                                        if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//
//                                            i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                            startActivity(i);
//                                            finish();
//                                        }
//                                    }
//                                }
//                            }
//                        }, 1);
//                    } else {
//                        //CToast.ShowToast(SplashAct.this, "errrr");
//                        cancelLoading();
//                        CORE_CALLED = true;
//                        alert_view_date(SplashAct.this, NC.getString(R.string.message), NC.getString(R.string.date_change), NC.getString(R.string.ok), NC.getString(R.string.cancel));
//                    }
////
//                }
////
//
//            } else
//                errorInSplash(getString(R.string.server_con_error));
//
//        }
//    }
//
//    /**
//     * this method is used to get subdomain from user
//     */
//
//    private void getUrl() {
//        final View view = View.inflate(SplashAct.this, R.layout.forgot_popup, null);
//        if (mDialog != null)
//            mDialog.dismiss();
//        mDialog = new Dialog(SplashAct.this, R.style.NewDialog);
//        mDialog.setContentView(view);
//        mDialog.setCancelable(false);
//        mDialog.setCanceledOnTouchOutside(false);
//        FontHelper.applyFont(SplashAct.this, mDialog.findViewById(R.id.inner_content));
//        mDialog.setCancelable(true);
//        mDialog.show();
//        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
//        //domain_hint
//        mail.setHint("Enter your access key");
//        final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
//        OK.setText("Submit");
//        int maxLength = 64;
//        setEditTextMaxLength(maxLength, mail);
//        View for_sepa = (View) mDialog.findViewById(R.id.for_sep);
//        for_sepa.setVisibility(View.GONE);
//
//        final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
//        Cancel.setVisibility(View.GONE);
//
//        OK.setOnClickListener(new OnClickListener() {
//            private String Email;
//
//            @Override
//            public void onClick(final View v) {
//                try {
//
//                    Email = mail.getText().toString();
//                    if (Email.length() >= 3) {
//                        mDialog.dismiss();
//                        if (!NO_NEED_TO_PLAY) {
//
//                            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
//                            vv.setVideoURI(path);
//                            vv.start();
//                            vv.setVisibility(View.VISIBLE);
//                            NO_NEED_TO_PLAY = !NO_NEED_TO_PLAY;
//                        } else
//                            showDialog();
//                        urlApi(Email);
//                        mail.setText("");
//                    } else {
//                        Toast.makeText(SplashAct.this, "Please enter valid access key", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//
//    public void setEditTextMaxLength(int length, EditText edt_text) {
//        InputFilter[] FilterArray = new InputFilter[1];
//        FilterArray[0] = new InputFilter.LengthFilter(length);
//        edt_text.setFilters(FilterArray);
//    }
//
//    /**
//     * this method is used to call the live url api
//     */
//    private void urlApi(String keyy) {
////70:1018
//        //  CoreClient client = new ServiceGenerator("http://loadtest.taximobility.com/mobileapi117/index/dGF4aV9hbGw=/?").createService(CoreClient.class);
//        String url = "";
//        try {
////             url = "http://192.168.1.115:1007/mobileapi117/index/";
//          //  url = "http://192.168.3.250/mobileapi117/index/";
//          // url = "http://loadtest.taximobility.com/mobileapi118/index/";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        CoreClient client = new ServiceGenerator(SplashAct.this, url, false).createService(CoreClient.class);
//        ApiRequestData.BaseUrl request = new ApiRequestData.BaseUrl();
//        request.company_domain = keyy;
//        request.company_main_domain = "taximobility.com";
//        request.device_type = "1";
//        // showLoading(this);
//        //SessionSave.saveSession("status", "F", StreetPickUpAct.this);
//        //   SessionSave.saveSession("trip_id", "", StreetPickUpAct.this);
//        //   Call<EndStreetPickupResponse> response = client.endStreetTrip(request, SessionSave.getSession("Lang", StreetPickUpAct.this));
//        Call<CompanyDomainResponse> response = client.callData(TaxiUtil.COMPANY_KEY, request);
//        response.enqueue(new Callback<CompanyDomainResponse>() {
//            @Override
//            public void onResponse(Call<CompanyDomainResponse> call, Response<CompanyDomainResponse> response) {
//                CompanyDomainResponse cr = response.body();
//                cancelLoading();
//                if (cr != null && SplashAct.this != null) {
//                    if (cr.status != null) {
//                        if (cr.status.trim().equals("1")) {
//                            SessionSave.saveSession("base_url", cr.baseurl, SplashAct.this);
//                            SessionSave.saveSession("api_key", cr.apikey, SplashAct.this);
//                            SessionSave.saveSession("encode", cr.encode, SplashAct.this);
//                            SessionSave.saveSession("image_path", cr.androidPaths.static_image, SplashAct.this);
//
//                            //Glide.with(SplashAct.this).load(SessionSave.getSession("image_path",SplashAct.this)+"signInLogo.png").error(R.drawable.sign_in_logo).override(260,50).into(new ImageView(SplashAct.this));
//                            System.out.println("_______" + cr.baseurl + "*********" + cr.apikey);
//                            TaxiUtil.API_BASE_URL = cr.baseurl;
////                            TaxiUtil.COMPANY_KEY = cr.apikey;
////                            TaxiUtil.DYNAMIC_AUTH_KEY = cr.encode;
//
//
//                            Log.e("language_size ", String.valueOf(cr.androidPaths.driver_language.size()));
//                            String totalLanguage = "";
//                            System.out.println("LLLLLLLL" + SessionSave.getSession("lang_json", SplashAct.this));
//
////                    for(int i=0;i<cr.androidPaths.driver_language.size();i++){
////                        SessionSave.saveSession("Lang_English",cr.androidPaths.driver_language.get(0).url,SplashAct.this);
////                        SessionSave.saveSession("Lang_Turkish",cr.androidPaths.driver_language.get(1).url,SplashAct.this);
////                        SessionSave.saveSession("Lang_Arabic",cr.androidPaths.driver_language.get(2).url,SplashAct.this);
////                        SessionSave.saveSession("Lang_Russian",cr.androidPaths.driver_language.get(3).url,SplashAct.this);
////                        SessionSave.saveSession("Lang_German",cr.androidPaths.driver_language.get(4).url,SplashAct.this);
////                        SessionSave.saveSession("Lang_French",cr.androidPaths.driver_language.get(5).url,SplashAct.this);
////                    }
//
//                            for (int i = 0; i < cr.androidPaths.passenger_language.size(); i++) {
////                        SessionSave.saveSession("Langs_English",cr.androidPaths.passenger_language.get(0).url,SplashAct.this);
////                        SessionSave.saveSession("Langs_Turkish",cr.androidPaths.passenger_language.get(1).url,SplashAct.this);
////                        SessionSave.saveSession("Langs_Arabic",cr.androidPaths.passenger_language.get(2).url,SplashAct.this);
////                        SessionSave.saveSession("Langs_Russian",cr.androidPaths.passenger_language.get(3).url,SplashAct.this);
////                        SessionSave.saveSession("Langs_German",cr.androidPaths.passenger_language.get(4).url,SplashAct.this);
////                        SessionSave.saveSession("Langs_French",cr.androidPaths.passenger_language.get(5).url,SplashAct.this);
//
//
////                        for(int i=0;i<cr.androidPaths.driver_language.size();i++){
//                                String key_ = "";
//
//                                totalLanguage += cr.androidPaths.passenger_language.get(i).language.replaceAll(".xml", "") + "____";
//
//                        /*if (cr.androidPaths.passenger_language.get(i).language.equals("English.xml"))
//                            key_ = "Lang_English";
//                        else if (cr.androidPaths.passenger_language.get(i).language.equals("Turkish.xml"))
//                            key_ = "Lang_Turkish";
//                        else if (cr.androidPaths.passenger_language.get(i).language.equals("Arabic.xml"))
//                            key_ = "Lang_Arabic";
//                        else if (cr.androidPaths.passenger_language.get(i).language.equals("Russian.xml"))
//                            key_ = "Lang_Russian";
//                        else if (cr.androidPaths.passenger_language.get(i).language.equals("German.xml"))
//                            key_ = "Lang_German";
//                        else if (cr.androidPaths.passenger_language.get(i).language.equals("Spanish.xml"))
//                            key_ = "Lang_Spanish";*/
//
//                                SessionSave.saveSession("LANG" + String.valueOf(i), cr.androidPaths.passenger_language.get(i).language, SplashAct.this);
//                                SessionSave.saveSession("LANGTemp" + String.valueOf(i), cr.androidPaths.passenger_language.get(i).design_type, SplashAct.this);
//                                SessionSave.saveSession("LANGCode" + String.valueOf(i), cr.androidPaths.passenger_language.get(i).language_code, SplashAct.this);
//                                SessionSave.saveSession(cr.androidPaths.passenger_language.get(i).language, cr.androidPaths.passenger_language.get(i).url, SplashAct.this);
//                                if (cr.androidPaths.passenger_language.get(i).language.contains("English")) {
//                                    SessionSave.saveSession("LANGTempDef", cr.androidPaths.passenger_language.get(i).design_type, SplashAct.this);
//                                    SessionSave.saveSession("LANGDef", cr.androidPaths.passenger_language.get(i).language, SplashAct.this);
//                                }
//                            }
//                            if (SessionSave.getSession("LANGDef", SplashAct.this).trim().equals(""))
//                                SessionSave.saveSession("LANGDef", SessionSave.getSession("LANG0", SplashAct.this), SplashAct.this);
//                            if (SessionSave.getSession("LANGTempDef", SplashAct.this).trim().equals(""))
//                                SessionSave.saveSession("LANGTempDef", SessionSave.getSession("LANGTemp0", SplashAct.this), SplashAct.this);
//
//
//                            SessionSave.saveSession("lang_json", totalLanguage, SplashAct.this);
//
//                            SessionSave.saveSession("colorcode", cr.androidPaths.colorcode, SplashAct.this);
//
//                            if (SessionSave.getSession("Lang", SplashAct.this).equals(""))
//                                SessionSave.saveSession("Lang", cr.androidPaths.passenger_language.get(0).language_code.replaceAll(".xml", ""), SplashAct.this);
////
//                            String url = "type=getcoreconfig";
//                            new CoreConfigCall(url);
//                        } else {
//                            // Toast.makeText(SplashAct.this, cr.message, Toast.LENGTH_LONG).show();
//                            CompanyResponseError = true;
//                            alert_view_company(SplashAct.this, getString(R.string.message), cr.message, getString(R.string.ok), getString(R.string.cancel));
////                            if (askDomain)
////                                getUrl();
//                        }
//                    }
//                } else {
//                    CompanyResponseError = true;
//                    errorInSplash(getString(R.string.server_error));
//                    //  Toast.makeText(SplashAct.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CompanyDomainResponse> call, Throwable t) {
//                //  cancelLoading();
//                CompanyResponseError = true;
//                closeDialog();
//                errorInSplash(getString(R.string.server_error));
//                // Toast.makeText(SplashAct.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//
//    public void alert_view_company(Context mContext, String title, String message, String success_txt, String failure_txt) {
//        try {
//            if (alertmDialog != null && alertmDialog.isShowing())
//                alertmDialog.dismiss();
//            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
//            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
//            alertmDialog.setContentView(view);
//            alertmDialog.setCancelable(true);
//            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
//            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);
//
//
//            alertmDialog.show();
//
//            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
//            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
//            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
//            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
//            title_text.setText(title);
//            message_text.setText(message);
//            button_success.setText(success_txt);
//            button_failure.setVisibility(View.VISIBLE);
//            button_failure.setText(failure_txt);
//            button_success.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//
//                    getUrl();
//                    alertmDialog.dismiss();
//                }
//            });
//            button_failure.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//
//                    finish();
//                    alertmDialog.dismiss();
//
//                }
//            });
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * this method is used get the string files from api
//     */
//
//    private synchronized void getAndStoreStringValues(String result) {
//        try {
//
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
////        Document doc = dBuilder.parse(result);
//            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
//            Document doc = dBuilder.parse(is);
//            Element element = doc.getDocumentElement();
//            element.normalize();
//
//            NodeList nList = doc.getElementsByTagName("*");
//
//            //     System.out.println("nagaSsss___ize"+nList.getLength());
//            int chhh = 0;
//            for (int i = 0; i < nList.getLength(); i++) {
//
//                Node node = nList.item(i);
//                //  System.out.println("nagaSsss___agg"+node.getTextContent()+"___");
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    chhh++;
//
//                    Element element2 = (Element) node;
//                    // if (element2.getAttribute("name").equals("pressBack"))
//                    //  System.out.println("nagaSsss___ize"+ chhh+"___"+element2.getTextContent());
////
//                    //  NC.nfields_value.add(element2.getTextContent());
//                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());
//
////                System.out.println("nagaSsss___"+element2.getAttribute("name"));
////                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
////                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
//                }
//            }
//            // System.out.println("nagaSsss___izelllll"+ fields_id.size());
//            getValueDetail();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * This class used to get configuration details
//     * <p>
//     * This class used to get configuration details
//     * </p>
//     *
//     * @author developer
//     */
//    private class CoreConfigCall implements APIResult {
//        public CoreConfigCall(final String url) {
//            // TODO Auto-generated constructor stub
//
//
//            new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, "", true).execute("type=getcoreconfig");
//        }
//
//        @Override
//        public void getResult(final boolean isSuccess, final String result) {
//            // TODO Auto-generated method stub
//            closeDialog();
//
//            if (isSuccess) {
//                try {
//                    System.out.println("_callingrrrrgeee");
//                    final JSONObject json = new JSONObject(result);
//                    if (json.getInt("status") == 1) {
//                        System.out.println("_callingrrrrgeees");
//                        //   System.out.println("sdfs______________________pre_theen");
//                        if (!(SessionSave.getSession("wholekey", SplashAct.this).equals(""))) {
//                            // System.out.println("wholekeyColor____nn"+SessionSave.getSession("wholekeyColor", SplashAct.this));
//                            getAndStoreStringValues(SessionSave.getSession("wholekey", SplashAct.this));
//                            getAndStoreColorValues(SessionSave.getSession("wholekeyColor", SplashAct.this));
//                        }
//
//
//                        final JSONArray array = json.getJSONArray("detail");
//                        startService(new Intent(SplashAct.this, AuthService.class));
//                        Log.e("Coreconfig ", array.toString());
//
//
//                        // utc_time
//                        System.out.println("_callingrrrrgeeesPPP");
//                        SessionSave.saveSession("play_store_version", array.getJSONObject(0).getString("android_passenger_version"), SplashAct.this);
//                        System.out.println("_callingrrrrgeeesPPPvv");
//                        SessionSave.saveSession("facebook_share", array.getJSONObject(0).getString("facebook_share"), SplashAct.this);
//                        SessionSave.saveSession("twitter_share", array.getJSONObject(0).getString("twitter_share"), SplashAct.this);
//                        SessionSave.saveSession("About", array.getJSONObject(0).getString("aboutpage_description"), SplashAct.this);
//                        SessionSave.saveSession("Currency", array.getJSONObject(0).getString("site_currency") + " ", SplashAct.this);
//                        SessionSave.saveSession("AdminMail", array.getJSONObject(0).getString("admin_email"), SplashAct.this);
//                        SessionSave.saveSession("TellfrdMsg", array.getJSONObject(0).getString("share_content"), SplashAct.this);
//                        SessionSave.saveSession("ShaerMsg", array.getJSONObject(0).getString("tell_to_friend_subject"), SplashAct.this);
//                        SessionSave.saveSession("Metric", array.getJSONObject(0).getString("metric"), SplashAct.this);
//
//                        SessionSave.saveSession("android_foursquare_api_key", array.getJSONObject(0).getString("android_foursquare_api_key"), SplashAct.this);
//
//                        if ((array.getJSONObject(0).getString("google_business_key")).equals("1"))
//                            isBUISNESSKEY = true;
//                        SessionSave.saveSession("isBUISNESSKEY", isBUISNESSKEY, SplashAct.this);
//
//                        if (SessionSave.getSession("Metric", SplashAct.this).equals("MILES"))
//                            SessionSave.saveSession("Metric_type", "m", SplashAct.this);
//                        else if (SessionSave.getSession("Metric", SplashAct.this).equals("KM"))
//                            SessionSave.saveSession("Metric_type", "k", SplashAct.this);
//                        else
//                            SessionSave.saveSession("Metric_type", "k", SplashAct.this);
//
//                        // SessionSave.saveSession("android_google_api_key", array.getJSONObject(0).getString("android_google_api_key"), SplashAct.this);
//                        SessionSave.saveSession("api_base", array.getJSONObject(0).getString("api_base"), SplashAct.this);
//                        SessionSave.saveSession("logo_base", array.getJSONObject(0).getString("logo_base"), SplashAct.this);
//                        SessionSave.saveSession("site_logo", array.getJSONObject(0).getString("site_logo"), SplashAct.this);
//                        SessionSave.saveSession("Cancellation_setting", array.getJSONObject(0).getString("cancellation_setting"), SplashAct.this);
//                        SessionSave.saveSession("facebook_key", array.getJSONObject(0).getString("facebook_key"), SplashAct.this);
//                        SessionSave.saveSession("skip_credit", array.getJSONObject(0).getString("skip_credit"), SplashAct.this);
//                        SessionSave.saveSession("model_details", array.getJSONObject(0).getString("model_details"), SplashAct.this);
//                        SessionSave.saveSession("referral_code_info", array.getJSONObject(0).getString("referral_code_info"), SplashAct.this);
//
//                        SessionSave.saveSession("referral_settings", array.getJSONObject(0).getString("referral_settings"), SplashAct.this);
//                        SessionSave.saveSession("referral_settings_message", array.getJSONObject(0).getString("referral_settings_message"), SplashAct.this);
//
//                        SessionSave.saveSession("passenger_payment_option", array.getJSONObject(0).getString("passenger_payment_option"), SplashAct.this);
//
//                        Log.e("model_details", array.getJSONObject(0).getString("model_details"));
//
//                        JSONArray jsonarray = new JSONArray(array.getJSONObject(0).getString("passenger_payment_option"));
//                        SessionSave.saveSession("pay_mod_name", jsonarray.getJSONObject(0).getString("pay_mod_name"), SplashAct.this);
//
//                        getCore_Utc = array.getJSONObject(0).getLong("utc_time");
//                        boolean deflanAvail = false;
//                        try {
//                            getCoreLangTime = json.getJSONObject("language_color_status").getString("android_passenger_language");
//                            getCoreColorTime = json.getJSONObject("language_color_status").getString("android_passenger_colorcode");
//                            SessionSave.saveSession("isFourSquare", array.getJSONObject(0).getString("android_foursquare_status"), SplashAct.this);
//
//
//                            String totalLanguage = "";
//                            JSONArray pArray = json.getJSONObject("language_color").getJSONObject("android").getJSONArray("passenger_language");
//                            for (int i = 0; i < pArray.length(); i++) {
////
//                                String key_ = "";
//
//                                totalLanguage += pArray.getJSONObject(i).getString("language").replaceAll(".xml", "") + "____";
//
//                                SessionSave.saveSession("LANG" + String.valueOf(i), pArray.getJSONObject(i).getString("language"), SplashAct.this);
//                                SessionSave.saveSession("LANGTemp" + String.valueOf(i), pArray.getJSONObject(i).getString("design_type"), SplashAct.this);
//                                SessionSave.saveSession("LANGCode" + String.valueOf(i), pArray.getJSONObject(i).getString("language_code"), SplashAct.this);
//                                SessionSave.saveSession(pArray.getJSONObject(i).getString("language"), pArray.getJSONObject(i).getString("url"), SplashAct.this);
//                                if (!SessionSave.getSession("LANGDef", SplashAct.this).equals("") && pArray.getJSONObject(i).getString("language").contains(SessionSave.getSession("LANGDef", SplashAct.this))) {
//                                    deflanAvail = true;
//                                }
//
//                            }
//                            System.out.println("___________defff" + deflanAvail);
//                            if (SessionSave.getSession("LANGDef", SplashAct.this).trim().equals("") || !deflanAvail) {
//                                SessionSave.saveSession("LANGDef", SessionSave.getSession("LANG0", SplashAct.this), SplashAct.this);
////                            if (SessionSave.getSession("LANGTempDef", SplashAct.this).trim().equals(""))
//
//                                SessionSave.saveSession("LANGTempDef", SessionSave.getSession("LANGTemp0", SplashAct.this), SplashAct.this);
//                                SessionSave.saveSession("Lang", pArray.getJSONObject(0).getString("language_code").replaceAll(".xml", ""), SplashAct.this);
//                                String url = SessionSave.getSession(SessionSave.getSession("LANG" + String.valueOf(0), SplashAct.this), SplashAct.this);
//                                SessionSave.saveSession("currentStringUrl", url, SplashAct.this);
//                            }
//
//                            SessionSave.saveSession("lang_json", totalLanguage, SplashAct.this);
//
//                            SessionSave.saveSession("colorcode", json.getJSONObject("language_color").getJSONObject("android").getString("colorcode"), SplashAct.this);
//
////                            if (SessionSave.getSession("Lang", SplashAct.this).equals(""))
////                                SessionSave.saveSession("Lang", cr.androidPaths.passenger_language.get(0).language_code.replaceAll(".xml", ""), SplashAct.this);
//
//
//                        } catch (JSONException e) {
//                            errorInSplash(getString(R.string.server_con_error));
//                            e.printStackTrace();
//                        }
//                        //android_passenger_language
//                        if (!SessionSave.getSession(TaxiUtil.PASSENGER_LANGUAGE_TIME, SplashAct.this).trim().equals(getCoreLangTime)) {
//                            new callString(getCoreColorTime);
//                        } else if (!SessionSave.getSession(TaxiUtil.PASSENGER_COLOR_TIME, SplashAct.this).trim().equals(getCoreColorTime)) {
//                            new callColor(getCoreLangTime);
//                        }
//                        if (VersionCheck()) {
//                            versionAlert(SplashAct.this);
//                        } else {
//
//
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // TODO Auto-generated method stub
//                                    Intent i = null;
//                                    if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
//
//                                        if (!SessionSave.getSession("IsOTPSend", SplashAct.this).equals("")) {
//                                            i = new Intent(SplashAct.this, VerificationAct.class);
//                                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                            startActivity(i);
//                                            overridePendingTransition(0, 0);
//                                            finish();
//                                        } else {
//                                            i = new Intent(SplashAct.this, UserHomeAct.class);
//                                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                            startActivity(i);
//                                            overridePendingTransition(0, 0);
//                                            finish();
//                                        }
//
//                                    } else {
//                                        if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
//                                            if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//                                                i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        } else {
//                                            if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//
//                                                i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        }
//                                    }
//                                }
//                            }, 1);
//                        }
//                    } else {
//                        errorInSplash(json.getString("status"));
//                    }
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    errorInSplash(getString(R.string.server_con_error));
//                    e.printStackTrace();
//                }
//            } else {
//                errorInSplash(getString(R.string.server_error));
////                //runOnUiThread(new Runnable() {
////                    public void run() {
////                        ShowToast(SplashAct.this, result);
////                    }
////                });
//            }
//        }
//    }
//
//    /**
//     * <p>
//     * This method used to get configuration details
//     * </p>
//     *
//     * @author developer
//     */
//
//
//    public void versionAlert(final Context mContext) {
//        try {
//            if (mverDialog != null)
//                mverDialog.dismiss();
//            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
//            mverDialog = new Dialog(mContext, R.style.NewDialog);
//            mverDialog.setContentView(view);
//            FontHelper.applyFont(mContext, mverDialog.findViewById(R.id.alert_id));
//            mverDialog.setCancelable(false);
//            if (!mverDialog.isShowing())
//                mverDialog.show();
//            final TextView title_text = (TextView) mverDialog.findViewById(R.id.title_text);
//            final TextView message_text = (TextView) mverDialog.findViewById(R.id.message_text);
//            final Button button_success = (Button) mverDialog.findViewById(R.id.button_success);
//            final Button button_failure = (Button) mverDialog.findViewById(R.id.button_failure);
//            button_failure.setVisibility(View.VISIBLE);
//
//            title_text.setText("" + mContext.getResources().getString(R.string.version_up_title));
//            message_text.setText("" + mContext.getResources().getString(R.string.version_up_message));
//            button_success.setText("" + mContext.getResources().getString(R.string.version_up_now));
//            button_failure.setText("" + mContext.getResources().getString(R.string.version_up_later));
//            button_success.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName()));
//                    mContext.startActivity(intent);
//                }
//            });
//            button_failure.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//
//                    //callApi();
//
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            Intent i = null;
//                            if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
//
//                                if (!SessionSave.getSession("IsOTPSend", SplashAct.this).equals("")) {
//                                    i = new Intent(SplashAct.this, VerificationAct.class);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(i);
//                                    overridePendingTransition(0, 0);
//                                    finish();
//                                } else {
//                                    i = new Intent(SplashAct.this, UserHomeAct.class);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(i);
//                                    overridePendingTransition(0, 0);
//                                    finish();
//                                }
//
//                            } else {
//                                if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
//                                    if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//                                        i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                } else {
//                                    if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//
//                                        i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                }
//                            }
//                        }
//                    }, 1);
//
//
//                    mverDialog.dismiss();
////                    String url = "type=getcoreconfig";
////                    new CoreConfigCall(url);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void alert_view_date(Context mContext, String title, String message, String success_txt, String failure_txt) {
//        try {
//            if (alertmDialog != null && alertmDialog.isShowing())
//                alertmDialog.dismiss();
//            final View view = View.inflate(mContext, R.layout.netcon_lay, null);
//            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
//            alertmDialog.setContentView(view);
//            alertmDialog.setCancelable(true);
//            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
//            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);
//
//
//            alertmDialog.show();
//
//            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
//            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
//            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
//            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
//            title_text.setText(title);
//            message_text.setText(message);
//            button_success.setText(success_txt);
//            button_failure.setVisibility(View.VISIBLE);
//            button_failure.setText(failure_txt);
//            button_success.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//
//                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
//                    alertmDialog.dismiss();
//                }
//            });
//            button_failure.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//                    alertmDialog.dismiss();
//                    finish();
//
//
//                }
//            });
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        // System.out.println("---googleconnectfailed" + result.getErrorMessage());
//
//        errorInSplash(getString(R.string.error_in_getting_address));
//        Toast.makeText(SplashAct.this, "connect failed", Toast.LENGTH_SHORT).show();
//        Log.i("TAXI", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//        // Once connected with google api, get the location
//        //Toast.makeText(SplashAct.this, "connected", Toast.LENGTH_SHORT).show();
//        progressBar1.setProgress(25);
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation == null && latitude == 181)
//            createLocationRequest();
//        else if (latitude == 181)
//            SaveLocation();
//        System.out.println("---googleconnectedd" + mLastLocation);
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        System.out.println("---googleconnects");
//        errorInSplash(getString(R.string.error_in_getting_address));
//        Toast.makeText(SplashAct.this, "suspendedec", Toast.LENGTH_SHORT).show();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//    }
//
//
//    public void showDialog() {
//        try {
//            System.out.println("---rotae4");
//            if (NetworkStatus.isOnline(this)) {
//                if (mDialog != null && mDialog.isShowing())
//                    mDialog.dismiss();
//                if (errorDialog != null && errorDialog.isShowing())
//                    errorDialog.cancel();
//                View view = View.inflate(this, R.layout.progress_bar, null);
//                mDialog = new Dialog(this, R.style.dialogwinddow);
//                mDialog.setContentView(view);
//                mDialog.setCancelable(false);
//                if (this != null)
//                    mDialog.show();
//
//                ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
//                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
//                Glide.with(this)
//                        .load(R.raw.loading_anim)
//                        .into(imageViewTarget);
//
//            } else {
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void closeDialog() {
//        try {
//            if (mDialog != null)
//                if (mDialog.isShowing())
//                    mDialog.dismiss();
//        } catch (Exception e) {
//
//        }
//    }
//
//
////    private void setMobileDataEnabled(Context context, boolean enabled) {
////        try {
////            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
////            final Class conmanClass = Class.forName(conman.getClass().getName());
////            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
////            iConnectivityManagerField.setAccessible(true);
////            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
////            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
////            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
////            setMobileDataEnabledMethod.setAccessible(true);
////            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
////        } catch (Exception e) {
////            // TODO: handle exception
////            e.printStackTrace();
////        }
////    }
//
//
//    /**
//     * this method is is used to get address from geocoder
//     */
//
//    private void getaddressfrom() {
//        System.out.println("positionFirst--");
//        Geocoder geocoder = new Geocoder(SplashAct.this, Locale.getDefault());
//        status_text.setText(getString(R.string.getting_auth));
//        progressBar1.setProgress(50);
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && addresses.size() > 0) {
//                Address obj = addresses.get(0);
//                Log.v("IGA", "Address  " + obj.getCountryCode() + "  ISO CODE   ");
//                SessionSave.saveSession("country_name", "" + obj.getCountryName(), SplashAct.this);
//                SessionSave.saveSession("country_names", "" + obj.getCountryCode(), SplashAct.this);
//                CURRENT_COUNTRY_CODE = obj.getCountryCode();
//                callApi();
//                System.out.println("-----" + CURRENT_COUNTRY_CODE);
//                System.out.println("-------" + SessionSave.getSession("country_names", SplashAct.this));
//            } else {
//                Locale locale;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    locale = context.getResources().getConfiguration().getLocales().get(0);
//                } else {
//                    locale = context.getResources().getConfiguration().locale;
//                }
//                SessionSave.saveSession("country_name", "" + locale.getDisplayCountry(), SplashAct.this);
//                SessionSave.saveSession("country_names", "" + locale.getCountry(), SplashAct.this);
//                System.out.println("____coddd" + locale.getDisplayCountry() + "__" + locale.getCountry());
//
//
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            Locale locale;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                locale = context.getResources().getConfiguration().getLocales().get(0);
//            } else {
//                locale = context.getResources().getConfiguration().locale;
//            }
//            SessionSave.saveSession("country_name", "" + locale.getDisplayCountry(), SplashAct.this);
//            SessionSave.saveSession("country_names", "" + locale.getCountry(), SplashAct.this);
//            System.out.println("____coddd" + locale.getDisplayCountry() + "__" + locale.getCountry());
//            callApi();
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * this method is used to get color codes from api
//     */
//
//    synchronized void getColorValueDetail() {
//        // System.out.println("NagarrrHIIIIIII");
//        Field[] fieldss = R.color.class.getDeclaredFields();
//        // fields =new int[fieldss.length];
//        // System.out.println("NagarrrHIIIIIIIvvvvvv***"+fieldss.length+"___"+CL.nfields_byName.size());
//
////        for(int i=0;i<CL.nfields_byName.size();i++)
////            System.out.println("nagaSsss___ize_fffgetc"+fieldss.length+"___"+CL.nfields_byName.get(i));
//        for (int i = 0; i < fieldss.length; i++) {
//            int id = getResources().getIdentifier(fieldss[i].getName(), "color", getPackageName());
//
//            if (CL.nfields_byName.containsKey(fieldss[i].getName())) {
//                CL.fields.add(fieldss[i].getName());
//                CL.fields_value.add(getResources().getString(id));
//                CL.fields_id.put(fieldss[i].getName(), id);
//                //  System.out.println("NagarrrHIIIIIIIvvvvvv***___"+fieldss[i].getName()+"___"+getResources().getString(id)+"___"+id);
//            } else {
//                System.out.println("Clvalue__" + fieldss[i].getName());
//            }
////        for(int h=0;h<NC.nfields_byName.size();h++){
////            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
////        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
//        }
//
//        for (Map.Entry<String, String> entry : CL.nfields_byName.entrySet()) {
//            String h = entry.getKey();
//            String value = entry.getValue();
//            // System.out.println("NagarrrHIIIIIII&&&"+CL.fields_id.get(h)+"____"+CL.nfields_byName.get(h));
//            CL.nfields_byID.put(CL.fields_id.get(h), CL.nfields_byName.get(h));
//            //  System.out.println("NagarrrHIIIIIII&&&___"+CL.nfields_byID.size());
//            // do stuff
//        }
//
//        // System.out.println("NagarrrBye"+CL.fields.size()+"___"+CL.fields_value.size()+"___"+ CL.fields_id.size());
//    }
//
//    /**
//     * this class is used to get color files from api
//     */
//
//    private class callColor implements APIResult {
//        public callColor(final String url) {
//            // TODO Auto-generated constructor stub
//            // new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, null, true,TaxiUtil.API_BASE_URL.replace("/mobileapi117/index/","") + "/public/uploads/android/colorcode/passengerAppColors.xml").execute();
//
//            new APIService_Retrofit_JSON_NoProgress(SplashAct.this, this, null, true, SessionSave.getSession("colorcode", SplashAct.this), true).execute();
//
//            Log.e("link__color", SessionSave.getSession("colorcode", SplashAct.this));
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, String result) {
//
//            if (isSuccess) {
//                // System.out.println("nagaaaa"+result);
//                // ShowToast(SplashAct.this,"Color");
//                SessionSave.saveSession(TaxiUtil.PASSENGER_COLOR_TIME, getCoreColorTime, SplashAct.this);
//                getAndStoreColorValues(result);
//                SessionSave.saveSession("wholekeyColor", result, SplashAct.this);
////                final String url = "type=getcoreconfig";
////                new CoreConfigCall(url);
//
//                // if (VersionCheck())
//                if (TaxiUtil.isCurrentTimeZone(getCore_Utc)) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            Intent i = null;
//                            if (SessionSave.getSession("Id", SplashAct.this).equals("")) {
//
//                                if (!SessionSave.getSession("IsOTPSend", SplashAct.this).equals("")) {
//                                    i = new Intent(SplashAct.this, VerificationAct.class);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(i);
//                                    overridePendingTransition(0, 0);
//                                    finish();
//                                } else {
//                                    i = new Intent(SplashAct.this, UserHomeAct.class);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(i);
//                                    overridePendingTransition(0, 0);
//                                    finish();
//                                }
//
//                            } else {
//                                if (SessionSave.getSession("trip_id", SplashAct.this).equals("")) {
//                                    if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//                                        i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                } else {
//                                    if (!SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("") && !SessionSave.getSession("PLAT", SplashAct.this).equalsIgnoreCase("0.0")) {
//
//                                        i = new Intent(SplashAct.this, MainHomeFragmentActivity.class);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                }
//                            }
//                        }
//                    }, 1);
//                } else {
//                    //CToast.ShowToast(SplashAct.this, "errrr");
//                    cancelLoading();
//                    CORE_CALLED = true;
//                    alert_view_date(SplashAct.this, NC.getString(R.string.message), NC.getString(R.string.date_change), NC.getString(R.string.ok), NC.getString(R.string.cancel));
//                }
////                else
////                    versionAlert(SplashAct.this);
//
//
//                //final String url = "type=getcoreconfig";
//                //new CoreConfigCall(url);
//
//            } else
//                errorInSplash(getString(R.string.server_con_error));
//
//        }
//    }
//
//    /**
//     * this class is used to parse the color values in xml
//     */
//
//    private synchronized void getAndStoreColorValues(String result) {
//        try {
//
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
////        Document doc = dBuilder.parse(result);
//            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
//            Document doc = dBuilder.parse(is);
//            Element element = doc.getDocumentElement();
//            element.normalize();
//
//            NodeList nList = doc.getElementsByTagName("*");
//
//            //   System.out.println("lislength"+nList.getLength());
//            int chhh = 0;
//            for (int i = 0; i < nList.getLength(); i++) {
//
//                Node node = nList.item(i);
//                //  System.out.println("nagaSsss___agg"+node.getTextContent()+"___");
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    chhh++;
//
//                    Element element2 = (Element) node;
//                    // if(element2.getAttribute("name").equals("pressBack"))
//                    //    System.out.println("nagaSsss___ize_fbn"+ chhh+"___"+element2.getAttribute("name")+"_____"+element2.getTextContent());
//
//                    //  NC.nfields_value.add(element2.getTextContent());
//                    CL.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());
//
////                System.out.println("nagaSsss___"+element2.getAttribute("name"));
////                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
////                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
//                }
//            }
//            //   System.out.println("nagaSsss___izelllllccc"+ CL.fields_id.size());
//
//            getColorValueDetail();
//        } catch (Exception e) {
//            //  System.out.println("nagaSsss___izelllllssssss"+ CL.fields_id.size());
//            e.printStackTrace();
//        }
//    }
//
//}