package com.cabipassenger.fragments;

/**
 * this class is used to follow the steps after the trip is started
 */


import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.data.MapWrapperLayout;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.route.Route;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.util.AppCacheImage;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.LatLngInterpolator;
import com.cabipassenger.util.LocationSearchActivity;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.Paymentgetway;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.SvgImage;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class OnGoingFrag extends Fragment implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener, OnMapReadyCallback {
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private static final int MY_PERMISSIONS_REQUEST_CALL = 124;
    private static final int MY_PERMISSIONS_REQUEST_GPS = 125;
    // Stores the current instantiation of the location client in getActivity() object
    private LinearLayout SlideTxt;
    FrameLayout map_lay;
    private TextView DoneTxt;
    private TextView Title;
    private TextView T_Drivername;
    private TextView T_Pickuploc;
    private TextView T_Pickuptime;
    private TextView T_Droploc;
    private TextView nodataTxt;
    private LinearLayout Transitlay;
    private DisplayImageOptions options;
    private GoogleMap map;
    private double Latitude;
    private double Longitude;
    private LatLng myLocation;
    private LatLng pickLocation;
    private LatLng dropLocation;
    private int STATUS;
    static JSONObject json;
    public int mTripid;
    private int mTravelstatus;
    private LinearLayout Driverlay, lay_call, lay_call_cancel, apptimelay;
    private TextView CancelTxt, book_taxi;
    private TextView Driver_name;
    private ImageView Driver_img;
    private TextView Taxino_txt;
    private ImageView Rating;
    private Route mRoute;
    private double D_latitude, D_longitude, previous_latitude, previous_longitude, lat1, log1;
    private String ondriverlat;
    private String ondriverlng;
    private final Handler lHandler = new Handler();
    public static TextView minfareTxt, back_text;
    private static String pLatitude, pLongitude, dLatitude, dLongitude;
    public ListView Reason_list;
    RelativeLayout lay_no_data;
    public ArrayAdapter<String> listAdapter;
    String[] Reasons = new String[]{"Change the travel plan", "Booking place in error", "Cab delay/issues"};
    public Dialog r_mDialog, cvv_Dialog;
    ArrayList<String> ReasonsList = new ArrayList<String>();
    Dialog mDialog;
    private float zoom = 14f;
    private String estimate_time;
    private double distancemtr;
    private String alert_msg;
    private Marker dmarker, driverlcn_marker;
    private Bundle alert_bundle = new Bundle();
    public String sReason;
    public String timetoreach;
    float bearing;
    float animateBearing;
    private String googleMapUrl;
    int layoutheight;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 5000; // 10 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private static int DISPLACEMENT = 0; // 10 meters
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private TextView minMaxBtn;
    private FrameLayout tripInfo;
    // Animate marker
    ArrayList<LatLng> listPoint = new ArrayList<LatLng>();
    ArrayList<LatLng> savedpoint = new ArrayList<LatLng>();
    ArrayList<String> savedlocation = new ArrayList<String>();
    ArrayList<LatLng> _trips = new ArrayList<LatLng>();
    Marker _marker;
    LatLngInterpolator _latLngInterpolator = new LatLngInterpolator.Spherical();
    TextView sendEmail;
    private boolean animStarted = false;
    private boolean animLocation = false;
    LatLng savedLatLng = null;
    private double speed = 0.0;
    private static int SPLASH_TIME_OUT = 60000;
    private MapWrapperLayout mapWrapperLayout;
    private Marker a_marker;
    private Dialog alertmDialog;
    private LinearLayout dropppp, pickupp;
    FrameLayout pickup_pinlay;
    ImageView pickup_pin;
    LinearLayout callbottom_lay;
    private LinearLayout searchlayl;
    private LinearLayout infoLayout;
    private Marker passMarker;
    private String driverphone;
    private LinearLayout lay_Drop_loc, Close_drop_loc;
    private ImageView pick_fav;


    private static String LocationRequestedBy = "";
    private View pickup_drop_Sep, time_sep;
    private TextView calltxt;
    private TextView call_ccancel;
    private TextView manufacturing, taxi_color;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ongoinglay, container, false);
        Initialize(v);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());
        if (true) {
//            drop_fav.setVisibility(View.VISIBLE);
//            lay_pick_fav.setVisibility(View.VISIBLE);
            dropppp.setVisibility(View.GONE);
            pickup_pin.setVisibility(View.VISIBLE);
            pickup_pinlay.setVisibility(View.GONE);

            // searchlayl.setBackgroundResource(R.color.transparent);

            pickup_drop_Sep.setVisibility(View.GONE);

            if (SessionSave.getSession("Lang", getActivity()).equals("fa") || SessionSave.getSession("Lang", getActivity()).equals("ar"))
                pickupp.setBackgroundResource(R.drawable.rect_home_bottom);
            else
                pickupp.setBackgroundResource(R.drawable.rect_home_bottom);
            //  intializeHome();
//            pickup_pinlay.setVisibility(View.VISIBLE);
//            pickup_pin.setVisibility(View.GONE);
//            pickup_drop_Sep.setVisibility(View.VISIBLE);
//
//            if(SessionSave.getSession("Lang",getActivity()).equals("ar"))
//                pickupp.setBackgroundResource(R.color.transparent);
//            else
//                pickupp.setBackgroundResource(R.color.transparent);
//
//            searchlayl.setBackgroundResource(R.drawable.rect_home_bottom);
        }
        return v;
    }

    /**
     * Initialize view for help fragment
     *
     * @param v
     */

    public void Initialize(View v) {
        // TODO Auto-generated method stub
        /*
         * Get the details from notification
		 */

        try {
            alert_bundle = this.getArguments();
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("alert_message");
            }
            if (alert_msg != null && alert_msg.length() != 0)
                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        }
        TaxiUtil.current_act = "OngoingTrip";
        map_lay = (FrameLayout) v.findViewById(R.id.map_lay);
        nodataTxt = (TextView) v.findViewById(R.id.nodataTxt);
        lay_no_data = (RelativeLayout) v.findViewById(R.id.lay_no_data);
        callbottom_lay = (LinearLayout) v.findViewById(R.id.callbottom_lay);
        book_taxi = (TextView) v.findViewById(R.id.booktaxilay);
        lay_Drop_loc = (LinearLayout) v.findViewById(R.id.lay_pick_fav);
        Close_drop_loc = (LinearLayout) v.findViewById(R.id.drop_fav);
        pick_fav = (ImageView) v.findViewById(R.id.pick_fav);
        manufacturing = (TextView) v.findViewById(R.id.manufacturing);
        taxi_color = (TextView) v.findViewById(R.id.taxi_color);

        book_taxi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new BookTaxiNewFrag()).commit();
            }
        });
        // Start the service if passenger have any ongoing trip.
        if (SessionSave.getSession("trip_id", getActivity()).equals("")) {
            Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
            getActivity().stopService(intent);
        } else {
            Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
            getActivity().startService(intent);
        }
        // Create a new global location parameters object
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.ongoing_contain));
        /*
         * API call to get ongoing trip details
		 */
        if (!SessionSave.getSession("trip_id", getActivity()).equals("")) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

//            map = mapFrag.getMap();
            mapWrapperLayout = (MapWrapperLayout) v.findViewById(R.id.map_relative_layout);
            mapFrag.getMapAsync(this);
//            mapWrapperLayout.init(map, getPixelsFromDp(getActivity(), 39 + 20), false,null);
//            map.getUiSettings().setCompassEnabled(true);
//            map.setMyLocationEnabled(false);
//
//            mTripid = Integer.parseInt(SessionSave.getSession("trip_id", getActivity()));
//            try {
//                if (TaxiUtil.isOnline(getActivity())) {
//                    JSONObject j = new JSONObject();
//                    j.put("trip_id", mTripid);
//                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                    new TripDetail("type=get_trip_detail", j);
//                } else {
//                    alert_view(getActivity(), "Message", "" +NC. getResources().getString(R.string.check_internet_connection), "" +NC. getResources().getString(R.string.ok), "");
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
        } else {
            map_lay.setVisibility(View.GONE);
            lay_no_data.setVisibility(View.VISIBLE);

//            Intent i;
//            i = new Intent(getActivity(), MainHomeFragmentActivity.class);
//            startActivity(i);
//            getActivity().finish();
        }
        /*
         * Initialization of Local Variable
		 */
        TaxiUtil.mActivitylist.add(getActivity());
        mRoute = new Route();
        SlideTxt = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        DoneTxt = (TextView) v.findViewById(R.id.rightIconTxt);
        DoneTxt.setVisibility(View.GONE);
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);

        dropppp = (LinearLayout) v.findViewById(R.id.dropppp);
        pickup_pinlay = (FrameLayout) v.findViewById(R.id.pickup_pinlay);

        pickup_drop_Sep = (View) v.findViewById(R.id.pickup_drop_Sep);
        time_sep = v.findViewById(R.id.time_sep);
        pickupp = (LinearLayout) v.findViewById(R.id.pickupp);
        pickup_pin = (ImageView) v.findViewById(R.id.pickup_pin);
        back_text.setVisibility(View.VISIBLE);
        Title = (TextView) v.findViewById(R.id.header_titleTxt);
        Title.setText(NC.getResources().getString(R.string.Trip_in_progress));
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.ongoing_contain));
        options = new DisplayImageOptions.Builder().showImageOnLoading(null) // resource
                // or
                // drawable
                .showImageForEmptyUri(null) // resource or drawable
                .showImageOnFail(null) // resource or drawable
                .resetViewBeforeLoading(false) // default
                .delayBeforeLoading(1000).cacheInMemory(true) // default
                .cacheOnDisc(true) // default
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) // default
                .bitmapConfig(Bitmap.Config.RGB_565) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                // .showImageOnLoading(R.drawable.ic_stub)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
        Transitlay = (LinearLayout) v.findViewById(R.id.intransitlay);
        Driverlay = (LinearLayout) v.findViewById(R.id.driverdetailslay);
        lay_call = (LinearLayout) v.findViewById(R.id.lay_call);
        lay_call_cancel = (LinearLayout) v.findViewById(R.id.lay_call_cancel);
        calltxt = (TextView) v.findViewById(R.id.callText);
        calltxt.setTextColor(CL.getResources().getColor(getActivity(), R.color.button_accept));
        call_ccancel = (TextView) v.findViewById(R.id.cancel_txt);
        call_ccancel.setTextColor(CL.getResources().getColor(getActivity(), R.color.button_accept));
        Glide.with(getActivity())
                .load(SessionSave.getSession("image_path", getActivity()) + "callDriver.png")
                .asBitmap().signature(new StringSignature("35"))
                .into(new SimpleTarget<Bitmap>(40, 40) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        calltxt.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(calltxt.getResources(), resource), null, null, null);
                    }
                });
        Glide.with(getActivity())
                .load(SessionSave.getSession("image_path", getActivity()) + "tripCancel.png")
                .asBitmap().signature(new StringSignature("34"))
                .into(new SimpleTarget<Bitmap>(40, 40) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        call_ccancel.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(call_ccancel.getResources(), resource), null, null, null);
                    }
                });


        apptimelay = (LinearLayout) v.findViewById(R.id.apptimelay);
        Driverlay.setVisibility(View.GONE);
        Transitlay.setVisibility(View.GONE);
        // ---Driver
        Driver_name = (TextView) v.findViewById(R.id.drivername);
        minfareTxt = (TextView) v.findViewById(R.id.minfareTxt);
        Driver_img = (ImageView) v.findViewById(R.id.driverImg);
        Taxino_txt = (TextView) v.findViewById(R.id.taxinoTxt);
        Rating = (ImageView) v.findViewById(R.id.rating);
        // in transit
        T_Drivername = (TextView) v.findViewById(R.id.t_drivername);
        T_Pickuploc = (TextView) v.findViewById(R.id.t_pickup_value);
        T_Pickuptime = (TextView) v.findViewById(R.id.t_pickup_time_value);
        T_Droploc = (TextView) v.findViewById(R.id.t_dropoff_text_value);


        tripInfo = (FrameLayout) v.findViewById(R.id.tripdetail_layout);
        minMaxBtn = (TextView) v.findViewById(R.id.close_icon);
        searchlayl = (LinearLayout) v.findViewById(R.id.searchlayl);

        //Drop_location =  (TextView) v.findViewById(R.id.t_dropoff_text_value);

        T_Droploc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTravelstatus == 2) {
                    LocationRequestedBy = "D";
                    Bundle b = new Bundle();
                    b.putString("type", "D");
                    Intent i = new Intent(getActivity(), LocationSearchActivity.class);
                    i.putExtras(b);
                    startActivityForResult(i, TaxiUtil.LocationResult);
                }
            }
        });

        lay_Drop_loc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                lay_Drop_loc.setVisibility(View.INVISIBLE);
                //dropppp.setVisibility(View.VISIBLE);

                dropVisible();
            }
        });

//        Close_drop_loc.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                dropInVisible();
//            }
//        });

        // getActivity() onclick method used to show the passenger info view.
        infoLayout = (LinearLayout) v.findViewById(R.id.info_layout);
        final TextView mapInfoTxt = (TextView) v.findViewById(R.id.mapinfo_txt);
        // getActivity() onclick method used to hide the passenger info view.
//        mapInfoTxt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                tripInfo.setVisibility(View.VISIBLE);
//                infoLayout.setVisibility(View.GONE);
//            }
//        });
//        minMaxBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                tripInfo.setVisibility(View.GONE);
//                infoLayout.setVisibility(View.VISIBLE);
//            }
//        });
        infoLayout.setVisibility(View.GONE);
        infoLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                SplitFareStatusDialog splitFareDialog = new SplitFareStatusDialog();
                splitFareDialog.show(fm, "splitStatus");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = "";
        try {
            double lat = 0.0, lng = 0.0;
            if (data != null) {
                Bundle res = data.getExtras();
                result = res.getString("param_result");
                lat = res.getDouble("lat");
                lng = res.getDouble("lng");
                // Toast.makeText(getActivity(), String.valueOf(lat), Toast.LENGTH_SHORT).show();
                // currentlocTxt.setText(result);
            }
            if (LocationRequestedBy.equals("P") && result != null && !result.trim().equals("")) {
            } else if (LocationRequestedBy.equals("D") && result != null && !result.trim().equals("")) {
                T_Droploc.setText(result);
                Intent intent1 = new Intent(getActivity(), GetPassengerUpdate.class);
                getActivity().stopService(intent1);
                Bundle mBundle = new Bundle();
                mBundle.putString("Dlname", result);
                mBundle.putDouble("Dlatitude", lat);
                mBundle.putDouble("Dlongitude", lng);
                Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
                intent.putExtras(mBundle);
                getActivity().startService(intent);
                if (T_Droploc != null) {
                    map.clear();
                    dLatitude = String.valueOf(lat);
                    dLongitude = String.valueOf(lng);
                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).title("" + NC.getResources().getString(R.string.droplocation)));
                    map.addMarker(new MarkerOptions().position(new LatLng(pickLocation.latitude, pickLocation.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).title("" + NC.getResources().getString(R.string.picklocation)));
                    dropLocation = new LatLng(Double.parseDouble(dLatitude), Double.parseDouble(dLongitude));
                    mRoute.drawRoute(map, getActivity(), dropLocation, pickLocation, "en", Color.parseColor("#00BFFF"), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(), "getActivity() device is not supported.", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            buildGoogleApiClient();
            // mGoogleApiClient.connect();
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        mapWrapperLayout.init(map, getPixelsFromDp(getActivity(), 39 + 20), false, null);
        map.getUiSettings().setCompassEnabled(true);
        // map.setMyLocationEnabled(false);

        try {
            LatLng mycurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mycurrentLocation, zoom));
//            if(mapWrapperLayout!=null && !mapWrapperLayout.isShown())
//                mapWrapperLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SessionSave.getSession("trip_id", getActivity()).equals("")) {
            ((MainHomeFragmentActivity) getActivity()).homePage();
        } else {
            mTripid = Integer.parseInt(SessionSave.getSession("trip_id", getActivity()));
            try {
                if (TaxiUtil.isOnline(getActivity())) {
                    JSONObject j = new JSONObject();
                    j.put("trip_id", mTripid);
                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                    new TripDetail("type=get_trip_detail", j);
                } else {
                    alert_view(getActivity(), "Message", "" + NC.getResources().getString(R.string.check_internet_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }

    /**
     * getActivity() class usedonC to get the trip details
     * <p/>
     * <p>
     * getActivity() class used to get the trip details
     * </p>
     *
     * @author developer
     */
    private class TripDetail implements APIResult {
        private String driverimage;
        private String drivername;
        private String taxino, taxi_color_, manufacturing_;
        private String pickuploc;
        private String pickuplat;
        private String pickuplong;
        private String droploc;
        private String droplat;
        private String droplong;
        private String pickuptime;
        private String driverphone = "";
        private String driverlat;
        private String driverlong;
        private String p_taxi_speed;
        private String message = "";

        // On June 18 BY Arun
        private String pickup_location, drop_location, distance, trip_minutes, total_fare, waiting_time;

        public TripDetail(String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            //  System.out.println("nagadriver-"+result);
            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1 && json.getJSONObject("detail").getInt("travel_status") != 0) {
                        //  startLocationUpdates();
                        driverimage = json.getJSONObject("detail").getString("driver_image");
                        drivername = json.getJSONObject("detail").getString("driver_name");
                        taxino = json.getJSONObject("detail").getString("taxi_number"); //taxi_color_,manufacturing_
                        taxi_color_ = json.getJSONObject("detail").getString("taxi_colour");
                        manufacturing_ = json.getJSONObject("detail").getString("taxi_manufacturer");
                        pickuploc = json.getJSONObject("detail").getString("current_location");
                        pickuplat = json.getJSONObject("detail").getString("pickup_latitude");
                        TaxiUtil.p_lat = Double.parseDouble(pickuplat);
                        pickuplong = json.getJSONObject("detail").getString("pickup_longitude");
                        TaxiUtil.p_lng = Double.parseDouble(pickuplong);
                        SessionSave.saveSession("Credit_Card", json.getJSONObject("detail").getString("credit_card_status"), getActivity());
                        droploc = json.getJSONObject("detail").getString("drop_location");
                        droplat = json.getJSONObject("detail").getString("drop_latitude");
                        droplong = json.getJSONObject("detail").getString("drop_longitude");
                        pickuptime = json.getJSONObject("detail").getString("pickup_time");
                        driverphone = json.getJSONObject("detail").getString("driver_phone");
                        OnGoingFrag.this.driverphone = json.getJSONObject("detail").getString("driver_phone");
                        driverlat = json.getJSONObject("detail").getString("driver_latitute");
                        driverlong = json.getJSONObject("detail").getString("driver_longtitute");
                        timetoreach = json.getJSONObject("detail").getString("time_to_reach_passen");


                        // On june'18.. Arun    trip_fare_details
//commented by vijay
                       /* pickup_location = json.getJSONObject("trip_fare_details").getString("pickup_location");
                        drop_location = json.getJSONObject("trip_fare_details").getString("drop_location");
                        distance = json.getJSONObject("trip_fare_details").getString("distance");
                        trip_minutes = json.getJSONObject("trip_fare_details").getString("trip_minutes");
                        total_fare = json.getJSONObject("trip_fare_details").getString("total_fare");
                        waiting_time = json.getJSONObject("trip_fare_details").getString("waiting_time");*/

                         pickup_location = json.getJSONObject("detail").getString("current_location");
                        drop_location = json.getJSONObject("detail").getString("drop_location");
                        distance = json.getJSONObject("detail").getString("distance");
                        trip_minutes = json.getJSONObject("detail").getString("trip_minutes");
                      //  total_fare = json.getJSONObject("detail").getString("total_fare");
                        waiting_time = json.getJSONObject("detail").getString("waiting_time");
                        double time_to_reach;
                        if (timetoreach.equals("")) {
                            time_to_reach = 0.0;
                        } else
                            time_to_reach = Double.parseDouble(timetoreach);
                        System.out.println("estimated----arrivaltime--param" + String.format("%.2f", time_to_reach));
                        //  Log.d("String ", " Round OFF" + String.format("%.2f", time_to_reach));
                        p_taxi_speed = json.getJSONObject("detail").getString("taxi_speed");
                        if (!json.getJSONObject("detail").getBoolean("is_primary"))
                            callbottom_lay.setVisibility(View.GONE);
                        else if (json.getJSONObject("detail").getInt("isSplit_fare") == 1 ? true : false)
                            infoLayout.setVisibility(View.VISIBLE);
                        // is_primary
                        pickLocation = new LatLng(Double.parseDouble(pickuplat), Double.parseDouble(pickuplong));
                        if (!droplat.equalsIgnoreCase("0.0") && !droplat.equalsIgnoreCase(""))
                            dropLocation = new LatLng(Double.parseDouble(droplat), Double.parseDouble(droplong));
                        ondriverlat = driverlat;
                        ondriverlng = driverlong;
                        mTravelstatus = Integer.parseInt(json.getJSONObject("detail").getString("travel_status"));
                        if (drivername.length() > 0) {
                            drivername = Character.toUpperCase(drivername.charAt(0)) + drivername.substring(1);
                        }
                        if (p_taxi_speed.length() > 0 && !p_taxi_speed.equals(null)) {
                            SessionSave.saveSession("taxi_speed", p_taxi_speed, getActivity());
                        }


                        pLatitude = pickuplat;
                        pLongitude = pickuplong;
                        dLatitude = droplat;
                        dLongitude = droplong;
                        if (mTravelstatus == 1) {
                            map_lay.setVisibility(View.GONE);
                            Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
                            getActivity().stopService(intent);
                            SessionSave.saveSession("trip_id", "", getActivity());
                            Transitlay.setVisibility(View.VISIBLE);
                            T_Drivername.setText(drivername);
                            T_Pickuploc.setText(pickuploc);
                            T_Pickuploc.setSelected(true);
                            T_Droploc.setSelected(true);
                            T_Pickuptime.setVisibility(View.GONE);

                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.Confirmation));

                            // T_Pickuptime.setText(DateConversion.date_conversion(pickuptime));
                        }
                        if (mTravelstatus == 2) {
                            // mHandler.sendEmptyMessage(0);
                            Driverlay.setVisibility(View.GONE);
                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.intransit));
                            //  Title.setText(getResources().getString(R.string.intransit));
                            mHandler.sendEmptyMessage(2);
                            Transitlay.setVisibility(View.VISIBLE);
                            T_Drivername.setText(drivername);
                            if (!droploc.trim().equals("")) {
                                dropVisible();
                                T_Droploc.setText(droploc);
                            } else {
                                ViewGroup.LayoutParams par = searchlayl.getLayoutParams();
                                par.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                lay_Drop_loc.setVisibility(View.VISIBLE);
                            }
                            if (!pickuploc.trim().equals("")) {
                                T_Pickuploc.setVisibility(View.VISIBLE);
                                T_Pickuploc.setText(pickuploc);
                                T_Pickuploc.setSelected(true);
                                T_Droploc.setSelected(true);
                            }

                            T_Pickuptime.setVisibility(View.VISIBLE);
                            T_Pickuptime.setText(NC.getString(R.string.picktime) + " " + pickuptime);
                            Transitlay.post(new Runnable() {
                                @Override
                                public void run() {
                                    layoutheight = Transitlay.getHeight();
                                    map.setPadding(0, layoutheight, 0, 0);
                                }
                            });
                            mRoute.drawRoute(map, getActivity(), pickLocation, dropLocation, "en", Color.parseColor("#00BFFF"), true);
                        }
                        if (mTravelstatus == 9) {
                            apptimelay.setVisibility(View.VISIBLE);
                            if (dropLocation != null)
                                lay_Drop_loc.setVisibility(View.GONE);
                            System.out.println("____" + ondriverlat + "___" + ondriverlng + "___" + pickLocation);
                            mRoute.drawRoute(map, getActivity(), new LatLng(Double.parseDouble(ondriverlat), Double.parseDouble(ondriverlng)), pickLocation, "en", Color.RED, true);
                        }
//                        if (mTravelstatus == 3) {
//                            if (!droploc.trim().equals("")) {
//                                mRoute.drawRoute(map, getActivity(), dropLocation, pickLocation, "en", Color.parseColor("#00BFFF"));
//
//
//                            }
//                        }
                        if (mTravelstatus == 9 || mTravelstatus == 3) {
                            mRoute.drawRoute(map, getActivity(), new LatLng(Double.parseDouble(ondriverlat), Double.parseDouble(ondriverlng)), pickLocation, "en", Color.RED, true);

                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.Confirmation));
                            // Title.setText(getResources().getString(R.string.Confirmation));
                            // mHandler.sendEmptyMessage(1);
                            if (!droploc.trim().equals("")) {
                                dropVisible();
                                T_Droploc.setText(droploc);
//                                System.out.println("nagaaaaaatdraw");
//                                System.out.println("nagaaaaaatdraw" + dropLocation.longitude+"  "+dropLocation.latitude+"P__"+pickLocation.latitude+"  "+pickLocation.longitude);
                                mRoute.drawRoute(map, getActivity(), dropLocation, pickLocation, "en", Color.parseColor("#00BFFF"), true);
                            }
//                            else
//                            lay_Drop_loc.setVisibility(View.VISIBLE);
                            Driverlay.setVisibility(View.VISIBLE);
                            Transitlay.setVisibility(View.VISIBLE);
                            Driver_name.setText(drivername);
                            // T_Droploc.setText(droploc);
                            if (!droploc.trim().equals("")) {
                                dropVisible();
                                T_Droploc.setText(droploc);
                            }
                            if (!pickuploc.trim().equals("")) {
                                T_Pickuploc.setVisibility(View.VISIBLE);
                                T_Pickuploc.setText(pickuploc);

                            }
                            T_Pickuploc.setText(pickuploc);
                            T_Pickuploc.setSelected(true);
                            T_Droploc.setSelected(true);
                            minfareTxt.setText("" + NC.getResources().getString(R.string.eta) + " :" + String.format("%.2f", time_to_reach) + " " + NC.getResources().getString(R.string.mins));
                            if (!AppCacheImage.loadBitmap(driverimage, Driver_img)) {
                                ImageLoader.getInstance().displayImage(driverimage, Driver_img, options);
                            }

                            int rating1 = json.getJSONObject("detail").getInt("driver_rating");
                            Taxino_txt.setText(taxino);
                            taxi_color.setText(getResources().getString(R.string.Taxi_color) + ":" + taxi_color_);

                            //taxi_color_,manufacturing_

                           manufacturing.setText(getResources().getString(R.string.Taxi_manu) + ":" + manufacturing_);

                            //   find_ETA(TaxiUtil.p_lat, TaxiUtil.p_lng, Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())), Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())));
                            Driverlay.post(new Runnable() {
                                @Override
                                public void run() {

                                    layoutheight = Driverlay.getHeight();
                                    //   map.setPadding(0, layoutheight, 0, 0);
                                }
                            });
                            if (rating1 == 0) {
                                Rating.setImageResource(R.drawable.star6);
                            }
                            if (rating1 == 1) {
                                Rating.setImageResource(R.drawable.star1);
                            }
                            if (rating1 == 2) {
                                Rating.setImageResource(R.drawable.star2);
                            }
                            if (rating1 == 3) {
                                Rating.setImageResource(R.drawable.star3);
                            }
                            if (rating1 == 4) {
                                Rating.setImageResource(R.drawable.star4);
                            }
                            if (rating1 == 5) {
                                Rating.setImageResource(R.drawable.star5);
                            }
                            lay_call_cancel.setOnClickListener(new OnClickListener() {
                                private Dialog mDialog;

                                @Override
                                public void onClick(View v) {

                                    FragmentManager fm1 = getActivity().getSupportFragmentManager();
                                    ReasonListFrag rl = new ReasonListFrag();
                                    fm1.beginTransaction().add(rl, "").commitAllowingStateLoss();

                                }
                            });
                            lay_call.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calldriver(driverphone);
                                }
                            });
                        }
                        if (mTravelstatus == 5) {

                            if (json.getJSONObject("detail").getInt("payment_type") == 2) {
                                Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
                                getActivity().stopService(intent);
                                Intent int_obj = new Intent(getActivity(), Paymentgetway.class);
                                //   int_obj.putExtra("redirect_url", redirect_url);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(int_obj);

                            } else {
                                message = json.getString("message");
                                map_lay.setVisibility(View.GONE);
                                lay_no_data.setVisibility(View.VISIBLE);
                                ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.payment_complete));
                                // Title.setText("" +NC. getResources().getString(R.string.payment_complete));
                                book_taxi.setVisibility(View.GONE);
                                //  book_taxi.setClickable(true);
                                //  book_taxi.setEnabled(true);
                                nodataTxt.setText("" + message);
                                // Intent i;
                                // i = new Intent(getActivity(), MainHomeFragmentActivity.class);
                                // startActivity(i);
                                //   getActivity().finish();
                            }


                        }
                        if (mTravelstatus == 4) {
                            message = json.getString("message");
                            map_lay.setVisibility(View.GONE);
                            lay_no_data.setVisibility(View.VISIBLE);
                            if(lay_no_data.getVisibility()==View.VISIBLE){

                            }
                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.Trip_in_progress));
                            //   Title.setText("" +NC. getResources().getString(R.string.Trip_in_progress));
                            nodataTxt.setText("" + message);
                            // Intent i;
                            // i = new Intent(getActivity(), MainHomeFragmentActivity.class);
                            // startActivity(i);
                            //   getActivity().finish();
                        }
                        driverlocation(pLatitude, pLongitude, dLatitude, dLongitude, driverlat, driverlong);
                    } else {
                        message = json.getString("message");
                        map_lay.setVisibility(View.GONE);
                        lay_no_data.setVisibility(View.VISIBLE);
                        if(lay_no_data.getVisibility()==View.VISIBLE){
                            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.app_name));
                        }
                        nodataTxt.setText("" + message);
                        SessionSave.saveSession("trip_id", "", getActivity());
                        // Intent i;
                        // i = new Intent(getActivity(), MainHomeFragmentActivity.class);
                        // startActivity(i);
                        //   getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    map_lay.setVisibility(View.GONE);
                    lay_no_data.setVisibility(View.VISIBLE);
                    if(lay_no_data.getVisibility()==View.VISIBLE){
                        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.app_name));
                    }
                }
            } else {
                map_lay.setVisibility(View.GONE);
                lay_no_data.setVisibility(View.VISIBLE);
                if(lay_no_data.getVisibility()==View.VISIBLE){
                    ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.app_name));
                }
                nodataTxt.setText("" + result);
                if (getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                //                alert_view(getActivity(), "" + result, "" + result, "" +NC. getResources().getString(R.string.ok), "");
            }
        }
    }


    /**
     * getActivity() method used to call logout API.
     */
    public void calldriver(final String driverphone) {
        try {
            final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
            final Dialog mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();
            FontHelper.applyFont(getActivity(), mDialog.findViewById(R.id.alert_id));
            final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.calldriver));
            button_success.setText("" + NC.getResources().getString(R.string.call));
            button_failure.setText("" + NC.getResources().getString(R.string.cancel));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mDialog.dismiss();
                        // TODO Auto-generated method stub
                        if (driverphone.length() == 0)
                            Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                            //  alert_view(getAc, "" +NC. getResources().getString(R.string.message), "" +NC. getResources().getString(R.string.invalid_mobile_number), "" +NC. getResources().getString(R.string.ok), "");
                        else {
                            final Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + driverphone));
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                                        MY_PERMISSIONS_REQUEST_CALL);
                                return;
                            } else
                                startActivity(callIntent);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void dropVisible() {
//    lay_Drop_loc.setVisibility(View.GONE);
//
//
//        dropppp.setVisibility(View.VISIBLE);
//       // pick_fav.setImageResource(R.drawable.star);
//        pickup_pinlay.setVisibility(View.VISIBLE);
//        pickup_drop_Sep.setVisibility(View.VISIBLE);
//
//        pickup_pin.setVisibility(View.GONE);
//        if(SessionSave.getSession("Lang",getActivity()).equals("ar"))
//            pickupp.setBackgroundResource(R.color.transparent);
//        else
//            pickupp.setBackgroundResource(R.color.transparent);
//
//        searchlayl.setBackgroundResource(R.drawable.rect_home_bottom);
//
//
//
//
//
//
//    }

    public void dropVisible() {

        dropppp.setVisibility(View.VISIBLE);
        // pick_fav.setImageResource(R.drawable.star);

        SvgImage rainSVG = new SvgImage(getActivity(), R.id.pick_fav, R.raw.star);


        pickup_pinlay.setVisibility(View.VISIBLE);
        pickup_pin.setVisibility(View.GONE);
        pickup_drop_Sep.setVisibility(View.VISIBLE);
        time_sep.setVisibility(View.VISIBLE);
        if (SessionSave.getSession("Lang", getActivity()).equals("fa") || SessionSave.getSession("Lang", getActivity()).equals("ar"))
            pickupp.setBackgroundResource(R.color.transparent);
        else
            pickupp.setBackgroundResource(R.color.transparent);

        searchlayl.setBackgroundResource(R.drawable.rect_home_bottom);

    }

    public void dropInVisible() {
        lay_Drop_loc.setVisibility(View.GONE);
        dropppp.setVisibility(View.GONE);

        pickup_pinlay.setVisibility(View.GONE);
        pickup_drop_Sep.setVisibility(View.GONE);

        pickup_pin.setVisibility(View.VISIBLE);

        if (SessionSave.getSession("Lang", getActivity()).equals("fa") || SessionSave.getSession("Lang", getActivity()).equals("ar"))
            pickupp.setBackgroundResource(R.drawable.rect_home_bottom);
        else
            pickupp.setBackgroundResource(R.drawable.rect_home_bottom);

        searchlayl.setBackgroundResource(R.color.transparent);


    }

    /**
     * getActivity() handler used to handle the ongoing trip UI changes based on status.
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    Driverlay.setVisibility(View.GONE);
                    Transitlay.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    Driverlay.setVisibility(View.VISIBLE);
                    Transitlay.setVisibility(View.GONE);
                    break;
                case 2:
                    transit(pLatitude, pLongitude, dLatitude, dLongitude);
                    break;
            }
        }

        ;
    };

    /**
     * getActivity() method used to make call to the passenger
     * <p>
     * getActivity() method used to make call to the passenger
     * </p>
     *
     * @param session
     */
    private void call(String session) {
        // TODO Auto-generated method stub


        if (session.length() == 0)
            Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            //  alert_view(getAc, "" +NC. getResources().getString(R.string.message), "" +NC. getResources().getString(R.string.invalid_mobile_number), "" +NC. getResources().getString(R.string.ok), "");
        else {
            final Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + session));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_CALL);
                return;
            } else
                startActivity(callIntent);
        }
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setStatus(Uri.parse("tel:" + session));
//        startActivity(callIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        System.out.println("____naga"+grantResults.length+"__"+grantResults[0]+"__"+grantResults[1]+"__"+driverphone+"__"+requestCode);
//        String s=("____naga"+grantResults.length+"__"+grantResults[0]+"__"+grantResults[1]+"__"+driverphone+"__"+requestCode);
//        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                //   System.out.println("_length" + grantResults.length + "__" + grantResults[0] + "__" + grantResults[1] + "__" + driverphone);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + driverphone));
                    startActivity(callIntent);
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_GPS:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new OnGoingFrag()).commit();
                } else {
                    getActivity().finish();
                }

                return;
        }
    }

    /**
     * getActivity() method used to get the driver location and move the marker in driver current location
     * <p>
     * getActivity() method used to get the driver location and move the marker in driver current location
     * </p>
     */
    public void driverlocation(String pLatitude2, String pLongitude2, String dLatitude2, String dLongitude2, String driverlat, String driverlong) {
        // TODO Auto-generated method stub
        double dLat = 0, dLong = 0;
        double pLat = Double.parseDouble(pLatitude2);
        double pLong = Double.parseDouble(pLongitude2);
        try {
            map.clear();
            if (Double.parseDouble(dLatitude2) != 0) {
                dLat = Double.parseDouble(dLatitude2);
                dLong = Double.parseDouble(dLongitude2);
                map.addMarker(new MarkerOptions().position(new LatLng(dLat, dLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).title("" + NC.getResources().getString(R.string.droplocation)));
            }
            if (mTravelstatus == 9) {
                //  map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ondriverlat), Double.parseDouble(ondriverlng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
                map.addMarker(new MarkerOptions().position(new LatLng(pLat, pLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).title("" + NC.getResources().getString(R.string.picklocation)));
            } else
                map.addMarker(new MarkerOptions().position(new LatLng(pLat, pLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).title("" + NC.getResources().getString(R.string.picklocation)));
            myLocation = new LatLng(Double.parseDouble(driverlat), Double.parseDouble(driverlong));

            if (dmarker != null) {
                dmarker.remove();
            }
            //     map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ondriverlat), Double.parseDouble(ondriverlng))).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));

            //  dmarker = map.addMarker(new MarkerOptions().position(myLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
            pickLocation = new LatLng(pLat, pLong);
            dropLocation = new LatLng(dLat, dLong);
            ondriverlat = driverlat;
            ondriverlng = driverlong;
            // mRoute.drawRoute(map, getActivity(), new LatLng(Double.parseDouble(ondriverlat), Double.parseDouble(ondriverlng)), pickLocation, "en", Color.RED);
            // mRoute.drawRoute(map, getActivity(), pickLocation, dropLocation, "en", Color.BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getActivity() class used to cancel the trip
     * <p>
     * getActivity() class used to cancel the trip
     * </p>
     *
     * @author developer
     */
    public class Cancel_afterTrip implements APIResult {

        String alert_message = "";

        public Cancel_afterTrip(String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + url).execute();
            // new APIService_Volley_JSON(getActivity(), getActivity(), data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("trip_id", "", getActivity());
                        SessionSave.saveSession("TaxiStatus", "", getActivity());
                        alert_message = json.getString("message") + "\n" + NC.getResources().getString(R.string.canceled_amount) + " " + SessionSave.getSession("Currency", getActivity()) + json.getString("cancellation_amount") + "\n" + NC.getResources().getString(R.string.canceled_from) + " " + json.getString("cancellation_from");
                        Intent intent = new Intent(getContext(), GetPassengerUpdate.class);
                        getContext().stopService(intent);
                        Intent i_gettaxi = new Intent(getActivity(), MainHomeFragmentActivity.class);
                        i_gettaxi.putExtra("alert_message", alert_message);
                        startActivity(i_gettaxi);
                        getActivity().finish();
                    } else if (json.getInt("status") == 2) {
                        SessionSave.saveSession("trip_id", "", getActivity());
                        SessionSave.saveSession("TaxiStatus", "", getActivity());
                        alert_message = json.getString("message");
                        Intent intent = new Intent(getContext(), GetPassengerUpdate.class);
                        getContext().stopService(intent);
                        Intent i_gettaxi = new Intent(getActivity(), MainHomeFragmentActivity.class);
                        i_gettaxi.putExtra("alert_message", alert_message);
                        startActivity(i_gettaxi);
                        getActivity().finish();
                    } else if (json.getInt("status") == -1) {
                        CToast.ShowToast(getActivity(), json.getString("message"));
                        SessionSave.saveSession("TaxiStatus", "", getActivity());
                        SessionSave.saveSession("trip_id", "", getActivity());
                        alert_message = json.getString("message");
                        Intent intent = new Intent(getContext(), GetPassengerUpdate.class);
                        getContext().stopService(intent);
                        Intent i_gettaxi = new Intent(getActivity(), MainHomeFragmentActivity.class);
                        i_gettaxi.putExtra("alert_message", alert_message);
                        startActivity(i_gettaxi);
                        getActivity().finish();
                    } else if (json.getInt("status") == 3) {
                        CToast.ShowToast(getActivity(), json.getString("message"));
                    } else {
                        CToast.ShowToast(getActivity(), json.getString("message"));
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * transit method used to change the driver location in google map while passenger traveling with driver
     * <p>
     * transit method used to change the driver location in google map while passenger traveling with driver
     * </p>
     */
    public void transit(String pickuplat, String pickuplong, String droplat, String droplong) {
        // TODO Auto-generated method stub
        double dLat = 0, dLong = 0;
        double pLat = 0, pLong = 0;
        try {
            if (pickuplat != null) {
                pLat = Double.parseDouble(pickuplat);
            }
            if (pickuplong != null) {
                pLong = Double.parseDouble(pickuplong);
            }
            map.clear();
            if (!droplat.equals("")) {
                dLat = Double.parseDouble(droplat);
                dLong = Double.parseDouble(droplong);
                map.addMarker(new MarkerOptions().position(new LatLng(dLat, dLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).title("" + NC.getResources().getString(R.string.droplocation)));
            }
            map.addMarker(new MarkerOptions().position(new LatLng(pLat, pLong)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).title("" + NC.getResources().getString(R.string.picklocation)));
            Latitude = mLastLocation.getLatitude();
            Longitude = mLastLocation.getLongitude();
            bearing = mLastLocation.getBearing();
            if (bearing >= 0)
                bearing = bearing + 90;
            else
                bearing = bearing - 90;
            myLocation = new LatLng(Latitude, Longitude);
//            if (dmarker != null) {
//                dmarker.remove();
//            }
            // dmarker = map.addMarker(new MarkerOptions().position(myLocation).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
            pickLocation = new LatLng(pLat, pLong);
            dropLocation = new LatLng(dLat, dLong);
            // mRoute.drawRoute(map, getActivity(), new LatLng(Double.parseDouble(ondriverlat), Double.parseDouble(ondriverlng)), pickLocation, "en", Color.RED);
            // mRoute.drawRoute(map, getActivity(), pickLocation, dropLocation, "en", Color.BLUE);
            bearing = 0;
        } catch (Exception e) {
            Log.e("error__", "Error in on intransist" + e.getMessage());
        }
    }

    // Slider menu used to move from one activity to another activity.


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(getActivity());
        stopLocationUpdates();

        super.onDestroy();
    }

    /**
     * getActivity() method used to calculate the estimate time between driver and pickup location.Gets the driver lat and lng for GETPASSENGERUPDATE API.
     */
    public void find_ETA(double P_latitude, double P_longitude, double d_latitude2, double d_longitude2) {
        try {
            Double distancekm;
            final PointF FromPoint = new PointF((float) P_latitude, (float) P_longitude);
            final PointF ToPoint = new PointF((float) d_latitude2, (float) d_longitude2);
            distancekm = getDistanceBetweenTwoPoints(FromPoint, ToPoint);
            //  System.out.println("DriverCall3" + distancekm + "__" + Double.parseDouble(SessionSave.getSession("taxi_speed", getActivity())));
            distancemtr = distancekm / 1000;
            estimate_time = "";
            if (distancemtr != 0) {
                double time = distancemtr / Double.parseDouble(BookTaxiNewFrag.speed);
                //      System.out.println("DriverCall*" + time);
                time = time * 3600; // time duration in seconds
                double minutes = Math.floor(time / 60);
                time -= minutes * 60;
                //      System.out.println("DriverCall**" + time);
                if (minutes > 0) {
                    estimate_time = "" + Math.round(minutes);// + "M";
                } else {
                    estimate_time = "1";
                }
            } else {
                estimate_time = "1";
            }
              System.out.println("estimated----arrivaltime--local" + estimate_time);
            minfareTxt.setText("" + NC.getResources().getString(R.string.eta) + " :" + estimate_time + NC.getResources().getString(R.string.mins));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * calculate the distance between source and destination.
     */

    private static double getDistanceBetweenTwoPoints(final PointF p1, final PointF p2) {
        final double R = 6371000; // m
        final double dLat = Math.toRadians(p2.x - p1.x);
        final double dLon = Math.toRadians(p2.y - p1.y);
        final double lat1 = Math.toRadians(p1.x);
        final double lat2 = Math.toRadians(p2.x);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final double d = R * c;
        return d;
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // for haversine use R = 6372.8 km instead of 6371 km
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        //double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // simplify haversine:
        //return 2 * R * 1000 * Math.asin(Math.sqrt(a));public
    }

    // Speed Calculation
    private double roundDecimal(double value, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();
        return value;
    }


    private double convertSpeed(double speed) {
        return ((speed * 3600) * 0.001);
    }

    // getActivity() method helps to get the CVV number from user.
    public void getCVV() {

        // System.out.println("creditcard status" + SessionSave.getSession("Credit_Card", getActivity()));
        try {
            if (SessionSave.getSession("Credit_Card", getActivity()).equalsIgnoreCase("1")) {
                r_mDialog.dismiss();
                final View view = View.inflate(getActivity(), R.layout.forgot_popup, null);
                cvv_Dialog = new Dialog(getActivity(), R.style.NewDialog);
                FontHelper.applyFont(getActivity(), view);
                cvv_Dialog.setContentView(view);
                cvv_Dialog.setCancelable(true);
                cvv_Dialog.show();
                final TextView title = (TextView) cvv_Dialog.findViewById(R.id.f_textview);
                final EditText cvv = (EditText) cvv_Dialog.findViewById(R.id.forgotmail);
                final Button OK = (Button) cvv_Dialog.findViewById(R.id.okbtn);
                final Button Cancel = (Button) cvv_Dialog.findViewById(R.id.cancelbtn);
                title.setText("" + NC.getResources().getString(R.string.enter_the_cvv));
                cvv.setHint("" + NC.getResources().getString(R.string.enter_the_cvv));
                cvv.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(4);
                cvv.setFilters(filterArray);
                OK.setOnClickListener(new OnClickListener() {
                    private String sCvv;

                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        try {
                            sCvv = cvv.getText().toString();
                            if (sCvv.trim().length() == 0 || sCvv.trim().length() > 4)
                                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_the_valid_CVV), "" + NC.getResources().getString(R.string.ok), "");
                            else {
                                JSONObject j = new JSONObject();
                                j.put("passenger_log_id", mTripid);
                                j.put("travel_status", "4");
                                j.put("remarks", sReason);
                                j.put("pay_mod_id", "2");
                                j.put("creditcard_cvv", sCvv);
                                new Cancel_afterTrip("type=cancel_trip", j);
                                cvv.setText("");
                                cvv_Dialog.dismiss();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                });
                Cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        cvv_Dialog.dismiss();
                    }
                });
            } else {
                r_mDialog.dismiss();
                JSONObject j = new JSONObject();
                j.put("passenger_log_id", mTripid);
                j.put("travel_status", "4");
                j.put("remarks", sReason);
                j.put("pay_mod_id", "2");
                j.put("creditcard_cvv", "");
                new Cancel_afterTrip("type=cancel_trip", j);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void CancelTrip(int mTripid, String sReason) {
        try {
            JSONObject j = new JSONObject();
            j.put("passenger_log_id", mTripid);
            j.put("travel_status", "4");
            j.put("remarks", sReason);
            j.put("pay_mod_id", "2");
            j.put("creditcard_cvv", "");
            new Cancel_afterTrip("type=cancel_trip", j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (isPermissionGranted()) {
            getGPS();
        }
    }

    public boolean isPermissionGranted() {
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GPS);

                return false;
            } else {
                return true;
            }
        }
        return false;
    }
//    protected void startLocationUpdates() {
//
//        try {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    /**
     * this method is used to get gps
     */

    private void getGPS() {
        try {
            if (mGoogleApiClient != null && mLocationRequest != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {

        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        try {
            if (isPermissionGranted()) {
                if (mGoogleApiClient.isConnected())
                    startLocationUpdates();
                else {
                    mGoogleApiClient.connect();
                }
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    LatLng mycurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    if (!SessionSave.getSession("trip_id", getActivity()).equals("") && STATUS != 5) {
                        //     dmarker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));


                        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(mycurrentLocation, zoom));
                    }
                } else {
//                System.out.println("nagaaaa---null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
//        try {
//
//            if (mGoogleApiClient.isConnected())
//                startLocationUpdates();
//            else {
//                mGoogleApiClient.connect();
//            }
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            if (mLastLocation != null) {
//                LatLng mycurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                if (!SessionSave.getSession("trip_id", getActivity()).equals("") && STATUS != 5) {
//
//                    //     dmarker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
//
//                    try {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                LatLng mycurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mycurrentLocation, 17.0f));
//                            }
//                        }, 500);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    // map.moveCamera(CameraUpdateFactory.newLatLngZoom(mycurrentLocation, zoom));
//                }
//            } else {
////                System.out.println("nagaaaa---null");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
//        System.out.println("listPoint size" + listPoint.size());
        mLastLocation = location;
//        if (pickLocation != null)
//            find_ETA(location.getLatitude(), location.getLongitude(), pickLocation.latitude, pickLocation.longitude);
        try {

            //  passMarker = map.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.dot)).title("" +NC. getResources().getString(R.string.driver_location)));

//            if (map != null) {
//                zoom = map.getCameraPosition().zoom;
//                if (mTravelstatus == 2) {
//                    speed = roundDecimal(convertSpeed(location.getSpeed()), 2);
//                    bearing = location.getBearing();
//                    if (bearing >= 0)
//                        bearing = bearing + 90;
//                    else
//                        bearing = bearing - 90;
//
//                    myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                    if (!animLocation) {
//                        listPoint.add(myLocation);
//                    } else {
//                        savedpoint.add(myLocation);
//                    }
//
//                }
//                dmarker = map.addMarker(new MarkerOptions().position(myLocation).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
//
//
//            } else if (mTravelstatus == 9 || mTravelstatus == 3) {
//                Log.d("carrrrrr lattitude", "onLocationChanged");
//
//
//                LatLng myLocation1 = new LatLng(Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())), Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())));
//                lat1 = Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity()));
//                log1 = Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity()));
//
//                if (!animLocation) {
//                    listPoint.add(myLocation1);
//                } else {
//                    savedpoint.add(myLocation1);
//                }
//
//                if (previous_latitude != 0 && previous_longitude != 0) {
//
//
//                    Log.d("carrrrrr lattitude", "" + lat1 + log1 + previous_latitude + previous_longitude);
//                    double distance = bearing(lat1, log1, previous_latitude, previous_longitude);
//                    double bear = bearing(lat1, log1, previous_latitude, previous_longitude);
//                    //    CToast.ShowToast(getActivity(),"Distance : " +distance);
//
//                    System.out.println("myLocation listPoint " + distance + " Last postion " + bear);
//
//                    if (distance > 50) {
//                        speed = 5;
//                    }
//
//                    bearing = (float) bear;
//                    if (bearing >= 0)
//                        bearing = bearing + 90;
//                    else
//                        bearing = bearing - 180;
//
//
//                }
//
//                if (lat1 != previous_latitude && log1 != previous_longitude) {
//                    previous_latitude = lat1;
//                    previous_longitude = log1;
//                }
//                Log.d("carrrrrr bearing", "" + bearing);
//                if (mTravelstatus == 9) {
//                    try {
//
////                        System.out.println("Distance  in condition    in Condittion" + " origins=" + TaxiUtil.p_lat + "," + TaxiUtil.p_lng + "&destinations=" + Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())) + "," + Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())));
////                        googleMapUrl = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())) + "," + Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())) + "&destinations=" + TaxiUtil.p_lat + "," + TaxiUtil.p_lng + "&mode=driving&sensor=true";
////                        Find_Distance parse1 = new Find_Distance();
////
////                        parse1.execute();
//
//
//                        // find_ETA(Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())), Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())), TaxiUtil.p_lat, TaxiUtil.p_lng);
//
//
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//
//                }
//
//            }
//
//            if (listPoint.size() > 1) {
//
//                if (a_marker != null) {
//                    a_marker.setVisible(false);
//                    a_marker.remove();
//                }
//                if (!animStarted) {
//                    if (savedLatLng != null) {
//                        listPoint.set(0, savedLatLng);
//                        System.out.println("savedLatLng animation fst" + listPoint.get(0));
//                    }
//                    if (speed > 2) {
//                        animStarted = true;
//                        animLocation = true;
//                        dmarker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
//                        dmarker.setVisible(true);
//                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
//                        savedLatLng = listPoint.get(listPoint.size() - 1);
//                        animateLine(listPoint, dmarker, bearing);
//
//                    } else {
//                        if (dmarker != null) {
//                            dmarker.setVisible(false);
//                            dmarker.remove();
//                        }
//
//                        a_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
//                        a_marker.setVisible(true);
//                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
//                    }
//
//                }
//            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        }
    }

    /**
     * this methos is used to animate the map as the map moves
     */

    public void moveCameraToDriverLoc(Double Latitude, Double Longtitude) {
        LatLng LL = new LatLng(Latitude, Longtitude);
        //    System.out.println("DriverCall1" + Latitude + "___" + Longtitude);
        if (mLastLocation != null && Latitude != 0.0)
            find_ETA(mLastLocation.getLatitude(), mLastLocation.getLongitude(), LL.latitude, LL.longitude);
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LL, zoom));
//        map.addMarker(new MarkerOptions().position(LL).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));
//        Location location = new Location("");
//        location.setLatitude(Latitude);
//        location.setLongitude(Longtitude);

//        if (map != null) {
//            zoom = map.getCameraPosition().zoom;
//            if (mTravelstatus == 2) {
//                speed =2;
//                bearing = 0;
//                if (bearing >= 0)
//                    bearing = bearing + 90;
//                else
//                    bearing = bearing - 90;
//                myLocation =LL;
//                if (!animLocation) {
//                    listPoint.add(myLocation);
//                } else {
//                    savedpoint.add(myLocation);
//                }
//
//            }
//            dmarker = map.addMarker(new MarkerOptions().position(myLocation).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title("" +NC. getResources().getString(R.string.driver_location)));


        //  } else
        if (true) {
            //  Log.d("carrrrrr lattitude", "onLocationChanged");
            lat1 = Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity()));
            log1 = Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity()));
            LatLng myLocation1 = new LatLng(lat1, log1);


            float zoomnew = 14f;
            if (map.getCameraPosition().zoom != map.getMinZoomLevel())
                zoomnew = map.getCameraPosition().zoom;
            //  System.out.println("mappppppzpppp" + lat1 + "__" + map.getMaxZoomLevel() + "__" + zoomnew);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation1, zoomnew == 2 ? 14f : zoomnew));
            if (mapWrapperLayout != null && !mapWrapperLayout.isShown())
                mapWrapperLayout.setVisibility(View.VISIBLE);
            if (!animLocation) {
                listPoint.add(myLocation1);
            } else {
                savedpoint.add(myLocation1);
            }
            if (previous_latitude != 0 && previous_longitude != 0) {
                //   Log.d("carrrrrr lattitude", "" + lat1 + log1 + previous_latitude + previous_longitude);
                double distance = bearing(lat1, log1, previous_latitude, previous_longitude);
                double bear = bearing(lat1, log1, previous_latitude, previous_longitude);
                //    CToast.ShowToast(getActivity(),"Distance : " +distance);
                //    System.out.println("myLocation listPoint " + distance + " Last postion " + bear);

                if (distance > 50) {
                    speed = 5;
                }

                bearing = (float) bear;
                bearing = bearing - 180;

//                if (bearing >= 0)
//                    bearing = bearing + 90;
//                else
//                    bearing = bearing - 180;


            }

            if (lat1 != previous_latitude && log1 != previous_longitude) {
                previous_latitude = lat1;
                previous_longitude = log1;
            }
            // Log.d("carrrrrr bearing", "" + bearing);
            if (mTravelstatus == 2) {
                try {
//                    if(pickLocation!=null && dropLocation!)
//                    Log.d("carrrrrr comming", "" + pickLocation.latitude+"__"+dropLocation.longitude);
//                    mRoute.drawRoute(map, getActivity(), pickLocation, dropLocation, "en", Color.parseColor("#00BFFF"));
//                        System.out.println("Distance  in condition in Condittion" + " origins=" + TaxiUtil.p_lat + "," + TaxiUtil.p_lng + "&destinations=" + Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())) + "," + Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())));
//                        googleMapUrl = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())) + "," + Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())) + "&destinations=" + TaxiUtil.p_lat + "," + TaxiUtil.p_lng + "&mode=driving&sensor=true";
//                        Find_Distance parse1 = new Find_Distance();
//
//                        parse1.execute();


                    // find_ETA(Double.parseDouble(SessionSave.getSession("driver_latitute", getActivity())), Double.parseDouble(SessionSave.getSession("driver_longtitute", getActivity())), TaxiUtil.p_lat, TaxiUtil.p_lng);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }

        if (listPoint.size() >= 1) {

            if (a_marker != null) {
                a_marker.setVisible(false);
                a_marker.remove();
            }
            if (!animStarted) {
                if (savedLatLng != null) {
                    listPoint.set(0, savedLatLng);
                    //         System.out.println("savedLatLng animation fst" + listPoint.get(0));
                }
                if (true) {
                    if (dmarker != null) {
                        dmarker.setVisible(false);
                        dmarker.remove();
                    }
                    animStarted = true;
                    animLocation = true;
                    dmarker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).rotation(bearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).title("" + NC.getResources().getString(R.string.driver_location)));
                    dmarker.setVisible(true);
                    //if(mTravelstatus!=9)
                    // map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
                    savedLatLng = listPoint.get(listPoint.size() - 1);
                    animateLine(listPoint, dmarker, bearing);

                } else {
                    if (a_marker != null) {
                        a_marker.setVisible(false);
                        a_marker.remove();
                    }
                    if (dmarker != null) {
                        dmarker.setVisible(false);
                        dmarker.remove();
                    }
                    a_marker = map.addMarker(new MarkerOptions().position(listPoint.get(0)).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).title("" + NC.getResources().getString(R.string.driver_location)));
                    a_marker.setVisible(true);
                    //map.animateCamera(CameraUpdateFactory.newLatLngZoom(listPoint.get(0), zoom));
                }

            }
        }


    }

    /**
     * Computes the bearing in degrees between two points on Earth.
     *
     * @return Bearing between the two points in degrees. A value of 0 means due
     * north.
     */
    public static double bearing(LatLng latLng1, LatLng latLng2) {
        double lat1 = latLng1.latitude;
        double lon1 = latLng1.longitude;
        double lat2 = latLng2.latitude;
        double lon2 = latLng2.longitude;
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLonRad = Math.toRadians(lon2 - lon1);
        double y = Math.sin(deltaLonRad) * Math.cos(lat2Rad);
        double x = Math.cos(lat1Rad) * Math.sin(lat2Rad) - Math.sin(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad);
        return radToBearing(Math.atan2(y, x));
    }

    /**
     * Converts an angle in radians to degrees
     */
    public static double radToBearing(double rad) {
        return (Math.toDegrees(rad) + 360) % 360;
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub
    }

    public Double bearing(Double lat1, Double long1, Double lat2, Double long2) {
        Double brng = Math.atan2(lat1 - lat2, long1 - long2);
        brng = brng * (180 / Math.PI);
        brng = (brng + 360) % 360;
        brng = 360 - brng;
        return brng;
    }

    /*public Double bearing(Double _lat1, Double _long1, Double _lat2, Double _long2) {

        double lat1 = _lat1;
        double lng1 = _long1;

        // destination
        double lat2 = _lat2;
        double lng2 = _long2;

        double dLon = (lng2 - lng1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.toDegrees((Math.atan2(y, x)));
        return (360 - ((brng + 360) % 360));
    }*/

    // Marker Animation with array of latlng
    public void animateLine(ArrayList<LatLng> Trips, Marker marker, float bearings) {
        _trips.clear();
        _trips.addAll(Trips);
        _marker = marker;
        animateBearing = bearings;
        animateMarker();
    }

    /**
     * animate the car with given latlong
     */
    public void animateMarker() {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {

                return _latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = null;

        //  for (int i = 0; i < _trips.size(); i++) {
        animator = ObjectAnimator.ofObject(_marker, property, typeEvaluator, getTriplatlong(_trips));

        //  }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

                //        System.out.println("Animation Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

                //       System.out.println("Animation Repeat");
            }

            @Override
            public void onAnimationStart(Animator animation) {

                //     System.out.println("Status--> started " + _trips.size() + " Animation started ");
            }


            @Override
            public void onAnimationEnd(Animator animation) {

                listPoint.clear();
                //     System.out.println("Status--> ended " + _trips.size() + " Animation ended ");
                try {
                    if (dmarker != null) {
                        //             System.out.println("Status--> sddsds " + _trips.size() + " Animation ended ");

                        a_marker = map.addMarker(new MarkerOptions().position(savedLatLng).rotation(animateBearing).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).title("" + NC.getResources().getString(R.string.driver_location)));
                        a_marker.setVisible(true);
//                                dmarker.setVisible(false);
//                                dmarker.remove();
                        if (savedpoint.size() > 1) {
                            for (int i = 0; i < savedpoint.size(); i++) {
                                listPoint.add(savedpoint.get(i));
                            }
                            savedpoint.clear();
                            animStarted = false;
                            animLocation = true;
                        } else {
                            animStarted = false;
                            animLocation = false;
                        }

                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    private LatLng[] getTriplatlong(ArrayList<LatLng> trips) {
        LatLng triparray[] = new LatLng[trips.size()];
        for (int i = 0; i < trips.size(); i++) {
            triparray[i] = trips.get(i);
        }
        return triparray;
    }


    // Get the google map pixels from xml density independent pixel.
    public static int getPixelsFromDp(final Context context, final float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onStop() {
        ((MainHomeFragmentActivity) getActivity()).disableSlide();
        // ((MainHomeFragmentActivity) getActivity()).setTitle_m(getActivity().getString(R.string.menu_trip_history));
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).left_icon.setVisibility(View.GONE);
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).disableSlide();
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
//        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).left_icon.setVisibility(View.GONE);

    }

    // Get the google API result and convert into JSON format.
//    private JSONObject GetAddress(String Url) {
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet httppost = new HttpGet(Url);
//            HttpResponse response = httpclient.execute(httppost);
//            String jsonResult = Utility.inputStreamToString(response.getEntity().getContent()).toString();
//            JSONObject json = new JSONObject(jsonResult);
//            return json;
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return null;
//    }

//    private class Find_Distance extends AsyncTask<Void, Integer, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                System.out.println("Distance  in condition in Condittion");
//                json = GetAddress(googleMapUrl);
//                if (json != null) {
//                    Double distancekm = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getDouble("value");
//                    distancemtr = distancekm / 1000;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (distancemtr != 0) {
//                double time = distancemtr / Double.parseDouble(SessionSave.getSession("taxi_speed", getActivity()));
//                time = time * 3600; // time duration in seconds
//                double minutes = Math.floor(time / 60);
//                time -= minutes * 60;
//                if (minutes > 0) {
//                    estimate_time = "" + Math.round(minutes);// + "M";
//                } else {
//                    estimate_time = "1";
//                }
//            } else {
//                estimate_time = "1";
//            }
//            minfareTxt.setText("" +NC. getResources().getString(R.string.estimated_arrival) + estimate_time +NC. getResources().getString(R.string.mins));
//        }
//    }


}