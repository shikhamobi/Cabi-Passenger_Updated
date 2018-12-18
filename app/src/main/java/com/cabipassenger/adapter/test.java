/*
package com.cabipassenger.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cabipassenger.ContinousRequest;
import com.cabipassenger.MainActivity;
import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;
import com.cabipassenger.data.DriverData;
import com.cabipassenger.data.MapWrapperLayout;
import com.cabipassenger.data.apiData.PlacesDetail;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.DialogInterface;
import com.cabipassenger.interfaces.FragPopFront;
import com.cabipassenger.interfaces.PickupDropSet;
import com.cabipassenger.route.Route;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.service.GetPassengerUpdate;
import com.cabipassenger.util.Address_s;
import com.cabipassenger.util.AppController;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.CustomMarker;
import com.cabipassenger.util.Dialog_Common;
import com.cabipassenger.util.DotsProgressBarSearch;
import com.cabipassenger.util.FindDistance;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.LocationSearchActivityNew;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.NetworkStatus;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.ShowToast;
import com.cabipassenger.util.TaxiUtil;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.cabipassenger.features.ApproximateCalculation.approxFare;


*/
/**
 * Created by developer on 4/16/16.
 * Class for home page fragment
 * Contains all functionalites for booktaxi,ride later,fare estimate etc.
 *//*

public class BookTaxiNewFrag extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnCameraChangeListener
        , DialogInterface, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, FragPopFront, PickupDropSet {

    private static boolean FREE_TO_MOVE = true;
    private String selected_carmodel_name;
    private Point display_size;
    private Dialog progressDialog;
    private NiftyDialogBuilder guest_booking_dialog, guest_booking_dialog_add, guest_booking_dialog_Contact, alert;

    public enum BOOKING_STATE {STATE_ONE, STATE_TWO, STATE_THREE}

    private static final int MY_PERMISSIONS_REQUEST_CONTACTS = 222;
    private final static String BOOK_NOW_ANIM = "BookNowA", MAP_SHOW = "map_showA", CANCEL_ANIM = "cancelA";
    */
/*public static ArrayList<ReadContact> get_contacts = new ArrayList<>();*//*

    private static boolean GEOCODE_EXPIRY = false;
    public static String CURRENT_COUNTRY_CODE = "IN";
    public static double E_time = 0;
    public static BOOKING_STATE booking_state = BOOKING_STATE.STATE_TWO;
    public static int z;
    public static String selectModel = "1";
    public static String speed = "100";
    private static LinearLayout CurrentCarModel;
    public static GoogleMap map;
    public static SearchFragment sf;
    private TextView no_gust;
    private boolean conformbooking = false;
    private Dialog errorAddressDialog;

    private String carModel = "1";
    private LinearLayout confirm_request, guest_booking;
    private boolean doubleBackToExitPressedOnce;
    private LatLng dragLatLng;
    private LinearLayout fav_bot_lay;
    private String current_address;
    private int displayWidth;
    private int displayHeight;
    private LinearLayout id_all, id_alls, fare_estimate_lay_;
    private RelativeLayout main;
    private TextView Title;
    private SupportMapFragment mapFragments;
    private ProgressDialog progressDialog_cont;
    NiftyDialogBuilder alert_savedlocation, categorydiaog_;

    private Dialog errorDialog;

    // private NiftyDialogBuilder guest_booking_dialog, guest_booking_dialog_add, guest_booking_dialog_Contact, alert;
    // private Dialog errorAddressDialog;
    // private CountDownTimer countDownTimer;
    //  private Marker currentLocationMarker;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String defaultCityName = "";
    //  private int next_higher_model;
    //  private String pass_confirm_mess = "";
    private AdView mAdView;

    private TextView availablecars, model_avail;
    private ImageView infoimage;
    private Handler handlerServercall1;
    private Marker dropmap;
    private int availablecarcount;
    private TextView request_taxi, book_later_r, book_later_b;
    private int fav_driver_available;
    private int book_fav_Driver;
    private String fav_driver_message;
    private float Zoom = 16f;
    private CoordinatorLayout carlayout;
    private LinearLayout book_now_bottom;
    private String PickupTime = "", PickupTimeandDate = "";
    private int _hour = 0;
    private int _min = 0;
    private int _date = 0;
    private int _month = 0;
    private int _year = 0;
    private String _ampm = "AM";
    private Address_s address;
    private Runnable callAddress_drag;
    private MapWrapperLayout mapWrapperLayout;
    private Dialog alertmDialog;
    private String Booking_type;
    private double friend1S, friend2S, friend3S;
    private double friend_a = 100, friend1S_a, friend2S_a, friend3S_a;
    private Double approx_fare = 0.0;
    private LinearLayout fare_estimate_lay, promo_code_lay, cash_card_lay;
    private TextView fare_estimate;
    private int car_type;

    public static double approx_diste, approx_timee;
    private Dialog mcDialog;
    private boolean isBookAfter;
    private String approx_travel_time, approx_travel_dist;
    private String promo_code = "";
    private HorizontalScrollView carScroll;
    private LinearLayout parent;
    private TextView txt_min_car1_;
    private DotsProgressBarSearch dotsProgressBar1;
    private DecimalFormat df = new DecimalFormat("####0.00");
    private Bundle alert_bundle;
    private String alert_msg;
    private Dialog dt_mDialog;
    private boolean book_again_msg;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double LastKnownLat;
    private double LastKnownLng;
    private LatLng LastKnownLatLng;
    private LatLng lat_lang;
    private TextView paymenttype;
    private int int_paymenttype = 0;
    private boolean bol_isFromBooknow = false;
    private ImageView mov_cur_loc_first;
    private TextView fare_minimum_ppl;
    private String min_fare;
    private String min_ppl = "";
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private SplitFareDialog splitFareDialog;
    private Handler handler;
    private ImageView mapppin, navi_icon_book;
    private JSONArray model_array;
    private Location location;
    private TextView skip_fav;

    private LinearLayout skip_fav_lay;
    private int DISTANCE_TYPE_FOR_ETA = 1;
    private int DISTANCE_TYPE_FOR_BOOK_LATER = 2;
    private int DISTANCE_TYPE_FOR_FARE = 3;
    private Marker pickup_marker;
    Marker drop;
    private LinearLayout confirm_ride_lay, select_car_lay;
    //cancel_fare_detail;
    private ImageView txt_model_name_confirm;
    private TextView txt_model_confirm;
    private TextView estimated_time_c;
    private TextView estimated_fare_c;
    private TextView capacity;
    private TextView request_taxi_c;
    private TextView done_c, pickloc_confirm;
    private TextView instruction_header;
    private ArrayList<PlacesDetail> popular_places = new ArrayList<>();

    private ImageView Read_contact_;
    private ImageView cancle_guest;
    private TextView txt_min_car1;
    private TextView approx_far_pass;
    private TextView fare_frag;

    public static double P_latitude;
    public static double P_longitude;

    public static double D_latitude;
    public static double D_longitude;

    public static boolean flag = false;
    public static boolean issaved = true;

    RecyclerView rvContacts;
    private Dialog mDialog;
    EditText name_gust, mobile;
    public static final int RequestPermissionCode = 1;
    int clikable = 0;
    boolean temp_declar = true;

    */
/**
     * Initialize view and setup the neccessary components
     *//*

    public void priorChanges(final View v) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        main = (RelativeLayout) v.findViewById(R.id.booktaxilay_home);
//        FontHelper.applyFont(getActivity(), main);
        carlayout = (CoordinatorLayout) v.findViewById(R.id.carlayout);
        carlayout.setVisibility(View.GONE);
        // If user have any ongoing trip, then system runs the service to get the trip status.
        if (SessionSave.getSession("trip_id", getActivity()).equals("")) {
            Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
            getActivity().stopService(intent);
        } else {
            Intent intent = new Intent(getActivity(), GetPassengerUpdate.class);
            getActivity().startService(intent);
        }

        sf = new SearchFragment();
        sf.setSearchFragmentListner(this);
        Bundle bundle = new Bundle();
        bundle.putString("type", "home");
        sf.setArguments(bundle);

        getChildFragmentManager().beginTransaction().add(R.id.search_frame_lay, sf, "").commit();
        mapppin = (ImageView) v.findViewById(R.id.mapppin);

        confirm_ride_lay = (LinearLayout) v.findViewById(R.id.confirm_ride_lay);
        select_car_lay = (LinearLayout) v.findViewById(R.id.select_car_lay);
        confirm_request = (LinearLayout) v.findViewById(R.id.confirm_request);
        fav_bot_lay = (LinearLayout) v.findViewById(R.id.fav_bot_lay);
        fare_frag = (TextView) v.findViewById(R.id.fare_frag);
        FontHelper.applyFont(getActivity(), fare_frag);
        fare_frag.setMovementMethod(new ScrollingMovementMethod());
//        fare_estimate_lay_ = (LinearLayout) v.findViewById(R.id.fare_estimate_lay_);
        estimated_fare_c = (TextView) v.findViewById(R.id.estimated_fare_c);
        // approx_far_pass = (TextView) v.findViewById(R.id.approx_far_pass);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();


        View bottomSheet = v.findViewById(R.id.design_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mAdView = (AdView) v.findViewById(R.id.adView);
        */
/*mAdView.setAdUnitId(getString(R.string.banner_home_footer));
        //  mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_home_footer));
        AdRequest adRequest = new AdRequest.Builder();
        //  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)*//*



//        View bottomSheet = v.findViewById(R.id.design_bottom_sheet);
//
        //  mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


       */
/* cancle_guest = (ImageView) v.findViewById(R.id.cancle_guest);
        cancle_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showdialog(v);
            }
        });*//*


     */
/*final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,20,0,0);*//*


        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {


                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                    select_car_lay.setVisibility(View.GONE);
                    confirm_ride_lay.setVisibility(View.VISIBLE);
                    // confirm_ride_lay.setLayoutParams(params);
                    select_car_lay.setAlpha(1);
                    System.out.println("_____________open");
                    // confirm_ride_lay.setAlpha(1);
                    // confirm_ride_lay.setScaleY((1));
                    if ((int) E_time != 0)
                        estimated_time_c.setText(String.valueOf((int) E_time) + " " + NC.getString(R.string.mins));
                    else
                        estimated_time_c.setText("-");
                    capacity.setText("1-" + min_ppl);
                    //  mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    System.out.println("_____________Collll");
                    select_car_lay.setVisibility(View.VISIBLE);
                    // confirm_ride_lay.setLayoutParams(params);
//                    confirm_ride_lay.setAlpha(1);
//                    confirm_ride_lay.setScaleY((1));
                    confirm_ride_lay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                if (select_car_lay.isShown()) {
//
//                    System.out.println("____________offf" + slideOffset);
//                    select_car_lay.setAlpha((float) (1 - slideOffset));
//                    confirm_ride_lay.setAlpha((float) (slideOffset ));
//                    confirm_ride_lay.setScaleY(slideOffset);
//                    confirm_ride_lay.setVisibility(View.VISIBLE);
//                }
            }
        });


        mapWrapperLayout = (MapWrapperLayout) v.findViewById(R.id.map_relative_layout);
        //SlideTxt = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        //  cancel_fare_detail = (LinearLayout) v.findViewById(R.id.cancel_fare_detail);
        Title = (TextView) v.findViewById(R.id.header_titleTxt);
        Title.setText(NC.getResources().getString(R.string.taxiname));
        id_all = (LinearLayout) v.findViewById(R.id.id_all);
        id_alls = (LinearLayout) v.findViewById(R.id.id_alls);
        instruction_header = (TextView) v.findViewById(R.id.instruction_header);
        carScroll = (HorizontalScrollView) v.findViewById(R.id.carScroll);
//        fare_estimate_lay = (LinearLayout) v.findViewById(R.id.fare_estimate_lay);
        fare_estimate = (TextView) v.findViewById(R.id.fare_estimate);
      */
/*  fare_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fare_estimate_lay.performClick();
            }
        });*//*

        promo_code_lay = (LinearLayout) v.findViewById(R.id.promo_code_lay);
        cash_card_lay = (LinearLayout) v.findViewById(R.id.cash_card_lay);

        //fare_eta = (TextView) v.findViewById(R.id.fare_eta);
        fare_minimum_ppl = (TextView) v.findViewById(R.id.fare_minimum_ppl);
        // fare_minimum = (TextView) v.findViewById(R.id.fare_minimum);
        // Set the available car model based on getcoreconfig response.
        skip_fav = (TextView) v.findViewById(R.id.skip_fav);
        skip_fav_lay = (LinearLayout) v.findViewById(R.id.skip_fav_lay);

        skip_fav_lay.setVisibility(View.GONE);
        navi_icon_book = (ImageView) v.findViewById(R.id.navi_icon_book);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Initialize(v);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 200);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("onActivityCreated", "onActivityCreated:");

        TaxiUtil.islocpickup = false;
        setcarModel();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_taxi_home, container, false);
        priorChanges(v);
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;
        try {


            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    // This is where you do your work in the UI thread.
                    // Your worker tells you in the message what to do.
                    System.out.println("______________split");
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    // ft.commitAllowingStateLoss();

                    splitFareDialog = new SplitFareDialog();
                    splitFareDialog.show(ft, "dialog");
                    //  splitFareDialog.setStyle(getDialog);
                    //  splitFareDialog.setStyle( DialogFragment.STYLE_NORMAL, R.style.You_Dialog );
                }

            };

            navi_icon_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (booking_state == BOOKING_STATE.STATE_TWO || booking_state == BOOKING_STATE.STATE_ONE)
                        ((MainHomeFragmentActivity) getActivity()).left_icon.performClick();
                    else
                        onBackPress();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }


    */
/**
     * Check for any bundle message from Activity initialize view and its click listener
     *//*

    public void Initialize(View v) {
        try {
            alert_bundle = this.getArguments();
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("alert_message");
                book_again_msg = alert_bundle.getBoolean("book_again");
//            } else {
//                alert_bundle = getActivity().getIntent().getExtras();
//                if (alert_bundle != null) {
//                    alert_msg = alert_bundle.getString("alert_message");
//                    System.out.println("mainnnnn14__nonull" + alert_msg);
//                }
            }
            System.out.println("mainnnnn14__" + alert_msg);
            if (alert_msg != null && alert_msg.length() != 0) {
                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
                // this.setArguments(new Bundle());
                getActivity().getIntent().putExtras(new Bundle());
                // booking_state = BOOKING_STATE.STATE_THREE;
                //   clearsetPickDropMarker();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        callAddress_drag = new Runnable() {
            @Override
            public void run() {
                System.out.println("Locationaasscc" + callAddress_drag);
                Log.e("Locationaa", "onCameraChange: ca" + z);
                if (z == 1) {
                    try {
                       */
/* LatLng ss = null;
                        if (dragLatLng != null)
                            ss = dragLatLng;
                        else
                            ss = sf.getPickuplatlng();*//*

                        if (address != null)
                            address.cancel(true);
                        Log.e("Location_address", "onCameraChange: ca");
                        if (flag) {
                            address = new Address_s(getActivity(), new LatLng(D_latitude, D_longitude));
                            address.execute().get();
                        } else {
                            address = new Address_s(getActivity(), new LatLng(P_latitude, P_longitude));
                            address.execute().get();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FindNearestlocal();
                }
            }
        };

        request_taxi_c = (TextView) v.findViewById(R.id.request_taxi_c);
       */
/* mov_cur_loc = (ImageView) v.findViewById(R.id.mov_cur_loc);
        mov_cur_loc.setVisibility(View.GONE);*//*

        mov_cur_loc_first = (ImageView) v.findViewById(R.id.mov_cur_loc_first);

        pickloc_confirm = (TextView) v.findViewById(R.id.pickloc_confirm);
        // Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "currentLocation.png").error(R.drawable.mapmove).into(mov_cur_loc);
        //  Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "currentLocation.png").error(R.drawable.mapmove).into(mov_cur_loc_first);
        //  Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "setPickupPin.png").error(R.drawable.mappin).into(mapppin);

        book_later_r = (TextView) v.findViewById(R.id.book_later_r);
        book_later_b = (TextView) v.findViewById(R.id.book_later_b);
        request_taxi = (TextView) v.findViewById(R.id.request_taxi);


        */
/*book_now_bottom = (LinearLayout) v.findViewById(R.id.book_now_bottom);*//*

        handlerServercall1 = new Handler(Looper.getMainLooper());
        // book_now_r = (RelativeLayout) v.findViewById(R.id.book_now_r);
        //   mov_cur_loc.setOnClickListener(this);
        mov_cur_loc_first.setOnClickListener(this);
       */
/* mov_cur_loc_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mov_cur_loc.performClick();
            }
        });*//*

        */
/*infoimage = (ImageView) v.findViewById(R.id.infoimage);*//*

        availablecars = (TextView) v.findViewById(R.id.availablecars);

        model_avail = (TextView) v.findViewById(R.id.model_avail);
        paymenttype = (TextView) v.findViewById(R.id.cash_card);


        txt_model_name_confirm = (ImageView) v.findViewById(R.id.txt_model_name_confirm);
        txt_model_confirm = (TextView) v.findViewById(R.id.txt_model_confirm);
        estimated_time_c = (TextView) v.findViewById(R.id.estimated_time_c);

        capacity = (TextView) v.findViewById(R.id.capacity);
        done_c = (TextView) v.findViewById(R.id.done_c);
    */
/*    infoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) E_time != 0)
                    estimated_time_c.setText(String.valueOf((int) E_time) + " " + NC.getString(R.string.mins));
                else
                    estimated_time_c.setText("-");

                capacity.setText("1-" + min_ppl);
                select_car_lay.setVisibility(View.GONE);
                confirm_ride_lay.setVisibility(View.VISIBLE);
                mov_cur_loc_first.setVisibility(View.GONE);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                    }
//                }, 100);

            }
        });*//*

        request_taxi_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               */
/* for (int i = 0; i < popular_places.size(); i++) {
                    if (popular_places.get(i).getLocation_name().contains(sf.getPickuplocTxt())) {
                        issaved = false;
                        System.out.println("arraylist***** " + popular_places.get(i).getLocation_name());
                        break;
                    } else {
                        issaved = true;
                    }
                }


                if (((favourite_db.getlocation(sf.getPickuplocTxt()) > 2) && (issaved))
                    *//*
*/
/*&& favourite_db.count_check > 3*//*
*/
/*) {
                    // alert showing Do u want save this Location
                    showdialog_savedlocation(view);
                } else {
                    if (availablecarcount > 0) {*//*

                sf.searchlayGone();
                SessionSave.saveSession("notmovemarkers", "1", getActivity());
                booking_state = BOOKING_STATE.STATE_THREE;
                clearsetPickDropMarker();
                // insert to saved booking to local

                    */
/*} else {
                        sf.searchlayvisible();
                        ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                    }*//*

                */
/*}*//*


            }
        });
        done_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // booking_state = BOOKING_STATE.STATE_TWO;
                //   clearsetPickDropMarker();
                //onBackPress();
                if (confirm_ride_lay.isShown()) {
                    confirm_ride_lay.setVisibility(View.GONE);
                    select_car_lay.setVisibility(View.VISIBLE);
                    mov_cur_loc_first.setVisibility(View.VISIBLE);
//                     mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });


//        cancel_fare_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                select_car_lay.setVisibility(View.VISIBLE);
//                confirm_ride_lay.setVisibility(View.GONE);
//                estimated_time_c.setText(String.valueOf((int) E_time));
//                capacity.setText(min_ppl);
//            }
//        });


        try {
            JSONArray jsonArray = new JSONArray(SessionSave.getSession("passenger_payment_option", getActivity()));

//          */
/* if(jsonArray.length() > 1)
//               cash_card_lay.setEnabled(true);
//            else
//           {
//               int_paymenttype = 1;
//               paymenttype.setText(NC.getResources().getString(R.string.payment_cash));
//               cash_card_lay.setEnabled(false);
//           }*//*

            if (jsonArray.length() == 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.getInt("pay_mod_id") == 1) {

                    int_paymenttype = 1;
                    paymenttype.setText(NC.getResources().getString(R.string.payment_cash));
                    cash_card_lay.setEnabled(false);

                } else if (jsonObject.getInt("pay_mod_id") == 2) {
                    int_paymenttype = 0;
                    cash_card_lay.setEnabled(false);
                    paymenttype.setText(NC.getResources().getString(R.string.card));

                }
            } else {
                int_paymenttype = 0;
                cash_card_lay.setEnabled(true);
                paymenttype.setText(NC.getResources().getString(R.string.cash_card));
            }
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //paymenttype.setText(SessionSave.getSession("pay_mod_name", getActivity()));


        // lay_model_one.setOnClickListener(this);

        // book_now_r.setOnClickListener(this);
        book_later_r.setOnClickListener(this);
        book_later_b.setOnClickListener(this);
        request_taxi.setOnClickListener(this);
//        book_now_r.setOnRippleCompleteListener(new LinearLayout.OnRippleCompleteListener() {
//
//            @Override
//            public void onComplete(LinearLayout LinearLayout) {
//
//            }
//        });
//        book_later_r.setOnRippleCompleteListener(new LinearLayout.OnRippleCompleteListener() {
//
//            @Override
//            public void onComplete(LinearLayout LinearLayout) {
//                isBookAfter = true;
//                book_later_fun();
//            }
//
//        });

        promo_code_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promocode();
            }
        });
        cash_card_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new PaymentOptionFrag()).addToBackStack(null).commit();
                if (!SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)) {
                    final View view = View.inflate(getActivity(), R.layout.paymentdialog, null);
                    final Dialog dialog = new Dialog(getActivity(), R.style.NewDialog);
                    dialog.setContentView(view);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    FontHelper.applyFont(getActivity(), view);

                    Colorchange.ChangeColor((ViewGroup) dialog.findViewById(R.id.inner_content), getActivity());
                    // set the custom dialog components - text, image and button
                    final RadioGroup rgrp = (RadioGroup) dialog.findViewById(R.id.paymentdialog_rgrp);
                    final RadioButton rbtn_cash = (RadioButton) dialog.findViewById(R.id.paymentdialog_rbtn_cash);
                    final RadioButton rbtn_card = (RadioButton) dialog.findViewById(R.id.paymentdialog_rbtn_card);
                    final Button btn_submit = (Button) dialog.findViewById(R.id.paymentdialog_btn_submit);
                    final Button btn_cancel = (Button) dialog.findViewById(R.id.paymentdialog_btn_cancel);

                    if (int_paymenttype == 1)
                        rbtn_cash.setChecked(true);
                    else if (int_paymenttype == 2)
                        rbtn_card.setChecked(true);
                    else {
                        rbtn_cash.setChecked(false);
                        rbtn_card.setChecked(false);
                    }
                    // Point pointSize = new Point();
                    // getActivity().getWindowManager().getDefaultDisplay().getSize(pointSize);
                    btn_submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            int selectedId = rgrp.getCheckedRadioButtonId();

                            // find the radio button by returned id
                            RadioButton radioButton = (RadioButton) dialog.findViewById(selectedId);

                            if (radioButton != null) {
                                if (radioButton.getText().toString().equals(NC.getResources().getString(R.string.payment_cash))) {
                                    int_paymenttype = 1;
                                    paymenttype.setText(NC.getResources().getString(R.string.payment_cash));
                                } else if (radioButton.getText().toString().equals(NC.getResources().getString(R.string.payment_card))) {
                                    int_paymenttype = 2;
                                    paymenttype.setText(NC.getResources().getString(R.string.payment_card));
                                }

                                dialog.dismiss();
                            } else {
                                Toast.makeText(getActivity(), NC.getResources().getString(R.string.select_payment), Toast.LENGTH_LONG).show();
                            }

                            Log.e("selec payment type ", String.valueOf(int_paymenttype));


                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            int_paymenttype = 0;
                            paymenttype.setText(NC.getResources().getString(R.string.cash_card));
                            dialog.dismiss();
                        }
                    });

                } else {
                    ShowToast.center(getActivity(), NC.getString(R.string.mode_selection_split));
                }
            }
        });


   */
/* private void setUIDisplay() {
        creditcardTxt.setVisibility(View.GONE);
        cashLayout.setVisibility(View.GONE);
        try {
            JSONArray jsonArray = new JSONArray(SessionSave.getSession("passenger_payment_option", CardRegisterAct.this));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getInt("pay_mod_id") == 1) {
                    cashLayout.setVisibility(View.VISIBLE);

                    cashTxt.setText(jsonObject.getString("pay_mod_name"));

                    if(SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar"))
                        cashTxt.setGravity(Gravity.RIGHT);

                } else if (jsonObject.getInt("pay_mod_id") == 2) {
                    creditcardTxt.setVisibility(View.VISIBLE);

                    if(SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar")) {
                        cashTxt.setGravity(Gravity.RIGHT);
                        creditcardTxt.setGravity(Gravity.RIGHT);
                    }

                    creditcardTxt.setText(jsonObject.getString("pay_mod_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//*


//        fare_estimate_lay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                if (!(sf.getPickuplocTxt().toString().trim().length() == 0 || (sf.getPickuplocTxt().toString().trim().equals(NC.getResources().getString(R.string.fetching_address))))) {
//                    JSONArray array = null;
//                    try {
//                        array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    }
//
//                    ((MainHomeFragmentActivity) getActivity()).toolbar_logo.setVisibility(View.GONE);
//                    ((MainHomeFragmentActivity) getActivity()).toolbar_titletm.setVisibility(View.GONE);
//                    ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);
//
//                    ((MainHomeFragmentActivity) getActivity()).toolbar_title.setVisibility(View.VISIBLE);
//                    ((MainHomeFragmentActivity) getActivity()).toolbar_title.setText(NC.getResources().getString(R.string.on_going));
//                    //  getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new OnGoingFrag()).addToBackStack(null).commit();
//
//                    FareEstimateFrag fef = new FareEstimateFrag();
//                    Bundle b = new Bundle();
//                    try {
//                        //array.getJSONObject(car_type).getString("model_id")
//                        b.putString("car", carModel);
//                        b.putString("e_time", String.valueOf(E_time));
//                        b.putString("pick", sf.getPickuplocTxt());
//                        b.putString("drop", sf.getDroplocTxt());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    fef.setArguments(b);
//                    getActivity().getSupportFragmentManager().beginTransrequest_taxi_caction().add(R.id.mainFrag, fef).addToBackStack(null).commit();
//                } else {
//                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
//                }
//
//            }
//        });

//        cancel_b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                bol_isFromBooknow = false;
//                bottomVisGoneAnim(CANCEL_ANIM);
//                mov_cur_loc.setVisibility(View.GONE);
//                ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);
//
//
//                //((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
//                //((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");
//            }
//        });

        ((MainHomeFragmentActivity) getActivity()).cancel_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                ((MainHomeFragmentActivity) getActivity()).cancelbtn = false;
                bol_isFromBooknow = false;
                //  bottomVisGoneAnim(CANCEL_ANIM);
                //mov_cur_loc.setVisibility(View.GONE);
                ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);


                //((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
                //((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");
            }
        });

        Colorchange.ChangeColor((ViewGroup) v, getActivity());
        intializeMap();


      */
/*  skip_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                booking_state = BOOKING_STATE.STATE_TWO;
                sf.setPickuplatlng(LastKnownLatLng);
                clearsetPickDropMarker();
            }
        });*//*



    }

    */
/**
     * Intialize necessary fields for map
     *//*


    public void intializeMap() {
        mapFragments = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragments == null)
            Toast.makeText(getActivity(), "nullll", Toast.LENGTH_SHORT).show();
        else {
            mapFragments.getMapAsync(this);

        }

    }

    */
/**
     * Self permission result for version 6.0 above
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     *//*
//
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 133) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragments.getMapAsync(this);
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(getActivity(), "No Permission", Toast.LENGTH_LONG).show();
            }
        } else {
            // If request is cancelled, the result arrays are empty.
            System.out.println("___" + grantResults.length + "____" + grantResults[0]);
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                // getGPS();
                // startActivity(new Intent(SplashAct.this,SplashAct.class));

                //      callSplitFare();
                Message message = handler.obtainMessage(0, "");
                message.sendToTarget();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

            }
            //  return;
        }
        //  break;

    }

    @Override
    public void onPause() {
        System.out.println("____________onpause11111");
        //   ((MainHomeFragmentActivity)  getActivity()).toolbar.setVisibility(View.VISIBLE);
        if (splitFareDialog != null)
            if (splitFareDialog.isVisible())
                splitFareDialog.dismiss();
        super.onPause();
    }

    @Override
    public void onResume() {
        System.out.println("____OnresumecccCalled______");
        super.onResume();
        TaxiUtil.iscar = true;
        TaxiUtil.islocpickup = false;
        ((MainHomeFragmentActivity) getActivity()).toolbar.setVisibility(View.GONE);

        System.out.println("____OnresumecccCalled");
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof BookTaxiNewFrag) {
            ((MainHomeFragmentActivity) getActivity()).homePage_title();
            ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);

            ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
            ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");

            ((MainHomeFragmentActivity) getActivity()).enableSlide();
            */
/*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CurrentCarModel != null)
                        CurrentCarModel.performClick();
                }
            }, 500);*//*


            Display display = getActivity().getWindowManager().getDefaultDisplay();
            display_size = new Point();
            display.getSize(display_size);

        }
       */
/* if (!SessionSave.getSession("savedposition", getActivity()).equals("")) {
            cancle_guest.setVisibility(View.VISIBLE);

        } else {
            cancle_guest.setVisibility(View.INVISIBLE);
        }*//*



        //current_address


    }

    */
/**
     * move the map camera view to current location of the user
     *//*


    public static void movetoCurrentloc() {
        LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());


        if (ll != null && map != null)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16f));


        // map.setOnCameraChangeListener(BookTaxiNewFrag.this);

        if (CurrentCarModel != null)
            CurrentCarModel.performClick();
    }

    public void movetoLastloc() {
        // LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());


        if (LastKnownLatLng != null && map != null)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, 16f));


        // map.setOnCameraChangeListener(BookTaxiNewFrag.this);


    }

    */
/**
     * Method to car layout in bottom if more than 3 models available add cars to scrollview
     *
     * @param
     *//*

    private void setcarModel() {


        System.out.println("modelarrbook*** " + SessionSave.getSession("model_details", getActivity()));
        try {
            model_array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            System.out.println("modelarrbook*** " + model_array.length());
            if (model_array.length() <= 4) {
                id_alls.setVisibility(View.GONE);
                id_all.setVisibility(View.VISIBLE);
                for (int n = 0; n < model_array.length(); n++) {
                    int i = 0;

                    i = n;

                    System.out.println("___________KKKKK" + SessionSave.getSession("Lang", getActivity()) + "__" + i + "____" + model_array.getJSONObject(i).getString("model_name"));
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_all, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    final LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);
                    carlay.setLayoutParams(params);
                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + model_array.getJSONObject(i).getString("model_name"));
                        ((TextView) v.findViewById(R.id.txt_model1)).setTypeface(Typeface.DEFAULT_BOLD);
                        approx_far_pass = (TextView) v.findViewById(R.id.pickup_approx_fare);
//                        approx_far_pass.setTypeface(approx_far_pass.getTypeface(), Typeface.BOLD);
                        Log.d("Image_url ", model_array.getJSONObject(i).getString("unfocus_image"));

                        Picasso.with(getActivity()).load(model_array.getJSONObject(i).getString("unfocus_image")).error(R.drawable.car2_unfocus).into((ImageView) v.findViewById(R.id.txt_dra_car1));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    carlay.setTag(i);
                    carlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            SplashActivity.selected_carmodel = Integer.parseInt(v.getTag().toString());
                            if (SplashActivity.selected_carmodel != 0)
                                SessionSave.saveSession("selected_carmodel", String.valueOf(SplashActivity.selected_carmodel), getActivity());

                            Log.e("onclick ", String.valueOf(SplashActivity.selected_carmodel));
                            carlay_click(v, 1);
                        }
                    });

                    id_all.addView(v);

                    Log.e("SelectedCarModel ", String.valueOf(SplashActivity.selected_carmodel));

                    String pos = SessionSave.getSession("selected_carmodel", getActivity());

                    if (pos.isEmpty())
                        SplashActivity.selected_carmodel = 1;
                    else
                        SplashActivity.selected_carmodel = Integer.parseInt(pos);

                    if (i == SplashActivity.selected_carmodel) {
                        CurrentCarModel = carlay;
                    }

                    if (i < (model_array.length() - 1)) {
                        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(
                                1, ViewGroup.LayoutParams.MATCH_PARENT);
                        View vv = new View(getActivity());
                        vv.setLayoutParams(paramsv);
                        vv.setBackgroundColor(CL.getColor(getActivity(), R.color.white));

                        id_all.addView(vv);
                    }
                }
            } else {
                id_all.setVisibility(View.GONE);
                id_alls.setVisibility(View.VISIBLE);
                for (int n = 0; n < model_array.length(); n++) {

                    System.out.println("___________nandu" + n + "____" + model_array.getJSONObject(n).getString("model_name"));
                    int i = 0;
                    i = n;
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_alls, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);

                    ViewGroup.LayoutParams lp = carlay.getLayoutParams();
                    if (lp instanceof ViewGroup.MarginLayoutParams) {
                        ((ViewGroup.MarginLayoutParams) lp).rightMargin = 17;
                        ((ViewGroup.MarginLayoutParams) lp).leftMargin = 17;
                    }
                    carlay.setTag(i);
                    carlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            SplashActivity.selected_carmodel = Integer.parseInt(v.getTag().toString());
                            if (SplashActivity.selected_carmodel != 0)
                                SessionSave.saveSession("selected_carmodel", String.valueOf(SplashActivity.selected_carmodel), getActivity());
                            Log.e("onclick ", String.valueOf(SplashActivity.selected_carmodel));
                            carlay_click(v, 2);

                        }
                    });

                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + model_array.getJSONObject(i).getString("model_name"));
                        ((TextView) v.findViewById(R.id.txt_model1)).setTypeface(Typeface.DEFAULT_BOLD);
                        */
/*approx_far_pass = (TextView) v.findViewById(R.id.txt_far_details);*//*

                        Log.d("Image_url ", model_array.getJSONObject(i).getString("unfocus_image"));


                        Picasso.with(getActivity()).load(model_array.getJSONObject(i).getString("unfocus_image"))
                                .error(R.drawable.car2_unfocus).into((ImageView) v.findViewById(R.id.txt_dra_car1));

                    } catch (JSONException e) {

                        Log.d("Image_url ", model_array.getJSONObject(i).getString("unfocus_image"));
                        e.printStackTrace();
                    }
                    id_alls.addView(v);

                    Log.e("SelectedCarModel ", String.valueOf(SplashActivity.selected_carmodel));

                    String pos = SessionSave.getSession("selected_carmodel", getActivity());

                    if (pos.isEmpty())
                        SplashActivity.selected_carmodel = 1;
                    else
                        SplashActivity.selected_carmodel = Integer.parseInt(pos);

                    if (i == SplashActivity.selected_carmodel) {
                        CurrentCarModel = carlay;
                    }

                    if (i < (model_array.length() - 1)) {
                        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(
                                1, ViewGroup.LayoutParams.MATCH_PARENT);
                        View vv = new View(getActivity());
                        vv.setLayoutParams(paramsv);
                        vv.setBackgroundColor(CL.getColor(getActivity(), R.color.white));

                        id_alls.addView(vv);
                    }
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception cirlce" + e);
            e.printStackTrace();
        }
    }


    private void carlay_click(View v, int type) {

        int pos = Integer.parseInt(v.getTag().toString());
        System.out.println("_____________OO" + getActivity());
        int carPos = pos;

        try {

            final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            carModel = array.getJSONObject(carPos).getString("model_id");
            selectModel = String.valueOf(pos + 1);
            SessionSave.saveSession("carModel", "" + carModel, getActivity());
            int viewCount = 0;
            if (type == 1)
                parent = id_all;
            else
                parent = id_alls;
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) instanceof ViewGroup) {
                    ViewGroup vv = (ViewGroup) parent.getChildAt(i);
                    if (i != (pos + viewCount)) {
                        vv.findViewById(R.id.lay_cir_car1).setBackgroundResource(R.drawable.circle_unfocus);
                        ((TextView) vv.findViewById(R.id.txt_model1)).setTextColor(CL.getColor(getActivity(), R.color.textviewcolor_light));
                        ((TextView) vv.findViewById(R.id.txt_min_car1)).setTextColor(CL.getColor(getActivity(), R.color.transparent));


                        Picasso.with(getActivity()).load(array.getJSONObject(i - viewCount).getString("unfocus_image")).error(R.drawable.car2_unfocus).into((ImageView) vv.findViewById(R.id.txt_dra_car1));
                        if (sf.getPickuplatlng() != null) {
                            availablecars.setText("");
                            model_avail.setText("");
                            (vv.findViewById(R.id.dotsProgressBar1)).setVisibility(View.GONE);
                        }
                    } else {
                        // Log.e("circlefocus__", SessionSave.getSession("image_path", getActivity()) + "circle_focus.png");

                        final LinearLayout temp = (LinearLayout) vv;
                        Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "circle_focus.png").asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable drawable = new BitmapDrawable(resource);
                                temp.findViewById(R.id.lay_cir_car1).setBackground(drawable);

                            }
                        });
                        txt_model_confirm.setText(model_array.getJSONObject(i - viewCount).getString("model_name").toUpperCase());
                        selected_carmodel_name = model_array.getJSONObject(i - viewCount).getString("model_name").toUpperCase();
                        ((TextView) vv.findViewById(R.id.txt_model1)).setTextColor(CL.getColor(getActivity(), R.color.button_accept));
                        ((TextView) vv.findViewById(R.id.txt_min_car1)).setTextColor(CL.getColor(getActivity(), R.color.button_accept));
                        txt_min_car1_ = ((TextView) vv.findViewById(R.id.txt_min_car1));
                        txt_model_confirm.setText(model_array.getJSONObject(i - viewCount).getString("model_name").toUpperCase());
                        Picasso.with(getActivity()).load(array.getJSONObject(i - viewCount).getString("focus_image")).error(R.drawable.car2_unfocus).into(txt_model_name_confirm);

                        Picasso.with(getActivity()).load(array.getJSONObject(i - viewCount).getString("focus_image")).error(R.drawable.car2_unfocus).into((ImageView) vv.findViewById(R.id.txt_dra_car1));


                        dotsProgressBar1 = ((DotsProgressBarSearch) vv.findViewById(R.id.dotsProgressBar1));

                        if (sf.getPickuplatlng() != null) {
                            availablecars.setText("");
                            model_avail.setText("");
                            dotsProgressBar1.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    viewCount = viewCount + 1;
                }
            }
            JSONObject j = new JSONObject();
            j.put("latitude", sf.getPickuplat());
            j.put("longitude", sf.getPickuplng());
            j.put("motor_model", carModel);
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            j.put("city_name", defaultCityName.trim().equals("") ? SessionSave.getSession("default_city_name", getActivity()) : defaultCityName);
            j.put("skip_fav", "2");
            final String url = "type=nearestdriver_list";
            if (sf.getPickuplat() != 0.0 && sf.getPickuplng() != 0.0) {
                SessionSave.saveSession("LastServer_latitude", "" + sf.getPickuplat(), getActivity());
                SessionSave.saveSession("LastServer_longitude", sf.getPickuplng() + "", getActivity());

                if (TaxiUtil.isOnline(getActivity())) {
                    new GetNearTaxi(url, j);
                } else {
                    errorInConnection(getResources().getString(R.string.check_internet_connection));
                    // alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_internet_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } else {
                // CToast.ShowToast(getActivity(),"null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CurrentCarModel = (LinearLayout) v;
    }

    public static void sessionMarker() {
        SessionSave.saveSession("remove_marker_clear", "0", AppController.getInstance());
        // SessionSave.saveSession("notmovemarkers", "1", AppController.getInstance());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        mapWrapperLayout.init(map, getPixelsFromDp(getActivity(), 39 + 20), true, mBottomSheetBehavior);
        SessionSave.saveSession("notmovemarkers", "1", getActivity());

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle() != null && marker.getTitle().equals("pick_up")) {
                    sf.pickupClicked();
                } else if (marker.getTitle() != null && marker.getTitle().equals("drop")) {
                    sf.dropClicked(true);
                }
                return false;
            }
        });


        Log.d("onMapReady", "onMapReady");
        map.setOnCameraChangeListener(this);

        try {
            if (address != null)
                address.cancel(true);
//            P_longitude = LastKnownLng;
//            P_latitude = LastKnownLat;
            if (book_again_msg) {


                System.out.println("____**" + alert_bundle.getString("drop_location") + "__" + alert_bundle.getString("pickup_location"));
                if (alert_bundle.getString("drop_location") != null) {
//                    sf.setDroplng(alert_bundle.getDouble("drop_longitude"));
//                    sf.setDroplat(alert_bundle.getDouble("drop_latitude"));
                    sf.setDroplatlng(new LatLng(alert_bundle.getDouble("drop_latitude"), alert_bundle.getDouble("drop_longitude")));
                    D_latitude = alert_bundle.getDouble("drop_latitude");
                    D_longitude = alert_bundle.getDouble("drop_longitude");

                    sf.setDroplocTxt(alert_bundle.getString("drop_location"));
                    sf.dropVisible();

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(alert_bundle.getDouble("pickup_latitude"),
                            alert_bundle.getDouble("pickup_longitude")), 16f));

                    address = new Address_s(getActivity(), new LatLng(alert_bundle.getDouble("pickup_latitude"),
                            alert_bundle.getDouble("pickup_longitude")));
                    address.execute().get();
                    dropLocationPin(alert_bundle.getDouble("pickup_latitude"), alert_bundle.getDouble("pickup_longitude"),
                            alert_bundle.getDouble("drop_latitude"), alert_bundle.getDouble("drop_longitude"));
                } else {
                    // map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(alert_bundle.getDouble("pickup_latitude"),
                            alert_bundle.getDouble("pickup_longitude")), Zoom));
              */
/*  map.addMarker(new MarkerOptions()
                        .position(new LatLng(alert_bundle.getDouble("pickup_latitude"),
                                alert_bundle.getDouble("pickup_longitude")))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current)));*//*


                    address = new Address_s(getActivity(), new LatLng(alert_bundle.getDouble("pickup_latitude"),
                            alert_bundle.getDouble("pickup_longitude")));
                    address.execute().get();
                    droploactiontopickup(alert_bundle.getDouble("pickup_latitude"), alert_bundle.getDouble("pickup_longitude"));

                }

//                sf.setPickuplat(alert_bundle.getDouble("pickup_latitude"));
//                sf.setPickuplng(alert_bundle.getDouble("pickup_longitude"));
                sf.setPickuplatlng(new LatLng(alert_bundle.getDouble("pickup_latitude"), alert_bundle.getDouble("pickup_longitude")));
                System.out.println("bookpickupnew*****@ " + alert_bundle.getString("pickup_location"));
                sf.setPickuplocTxt(alert_bundle.getString("pickup_location"));
                System.out.println("dropfav**** " + alert_bundle.getString("drop_location") + " ****** " + alert_bundle.getString("pickup_location"));

            } else {
//                address = new Address_s(getActivity(), new LatLng(P_latitude, P_longitude));
//                address.execute().get();

                if (LastKnownLatLng != null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, Zoom));
                    sf.setPickuplatlng(LastKnownLatLng);
                } else {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(SessionSave.getSession("PLAT", getActivity())), Double.parseDouble(SessionSave.getSession("PLNG", getActivity()))), Zoom));
                }

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(P_latitude,
                                P_longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));
                address = new Address_s(getActivity(), new LatLng(P_latitude, P_longitude));
                address.execute().get();

                //set  adreees

            }


//            } else {
//                address = new Address_s(getActivity(), new LatLng(P_latitude, P_longitude));
//                address.execute().get();
//            }
            // onPressLoadData(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        carlayout.setVisibility(View.VISIBLE);
        if (CurrentCarModel != null)
            CurrentCarModel.performClick();
        temp_declar = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mov_cur_loc_first:

                map.clear();
                SessionSave.saveSession("notmovemarkers", "1", getActivity());
                SessionSave.saveSession("remove_marker_clear", "0", getActivity());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, Zoom));
                BookTaxiNewFrag.flag = false;

                P_latitude = LastKnownLatLng.latitude;
                P_longitude = LastKnownLatLng.longitude;

                try {
                    address = new Address_s(getActivity(), new LatLng(Double.valueOf(LastKnownLatLng.latitude),
                            Double.valueOf(LastKnownLatLng.longitude)));
                    address.execute().get();

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(P_latitude,
                                    P_longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));
                    if (D_latitude != 0.0 && D_longitude != 0.0) {

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(D_latitude, D_longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.droplocation)));
                    }

                    //  droploactiontopickup(alert_bundle.getDouble("pickup_latitude"), alert_bundle.getDouble("pickup_longitude"));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                break;

//            case R.id.book_now_r:
//
//
//                if (availablecarcount > 0) {
//                    //  approx_far_pass.setText(NC.getResources().getString(R.string.pick_up_aprox_text) + " " + E_time + " " + NC.getResources().getString(R.string.minutes_));
//                    bottomVisGoneAnim(BOOK_NOW_ANIM);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            ((MainHomeFragmentActivity) getActivity()).cancelbtn = true;
//                            mov_cur_loc.setVisibility(View.VISIBLE);
//                            ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.VISIBLE);
//                            ((MainHomeFragmentActivity) getActivity()).cancel_b.setText(NC.getString(R.string.cancel));
//                            ((MainHomeFragmentActivity) getActivity()).cancel_b.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(final View v) {
//
//                                    ((MainHomeFragmentActivity) getActivity()).cancelbtn = false;
//                                    bol_isFromBooknow = false;
//                                    bottomVisGoneAnim(CANCEL_ANIM);
//                                    mov_cur_loc.setVisibility(View.GONE);
//                                    ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);
//
//
//                                    //((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
//                                    //((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");
//                                }
//                            });
//
//
//                        }
//                    }, 200);
//                } else {
//                    Toast.makeText(getActivity(), NC.getString(R.string.no_taxi), Toast.LENGTH_SHORT).show();
//                }
//
//                clearPreviousData();
//                break;
            case R.id.book_later_r:
                isBookAfter = true;
                book_later_fun();
                break;
            case R.id.book_later_b:
                isBookAfter = true;
                book_later_fun();
                break;
            case R.id.request_taxi:
                bol_isFromBooknow = true;
                conformbooking = true;
                CurrentCarModel.performClick();


//                        lat_lang = new LatLng(sf.getPickuplatlng(), sf.getDroplatlng());


//               */
/* if (availablecarcount > 0) {
//                if (!(sf.getPickuplocTxt().toString().trim().length() == 0 || (sf.getPickuplocTxt().toString().trim().equals(NC.getResources().getString(R.string.fetching_address))))) {
//                    if (sf.getDroplocTxt().toString().trim().length() > 0) {
//                        isBookAfter = false;
//                        if (SplashAct.isBUISNESSKEY)
//                            new FindApproxDistance();
//                        else {
//                            double Driverdistance = 0.5 + FindDistance.distance(P_latitude, P_longitude, D_latitude, D_longitude, SessionSave.getSession("Metric_type", getActivity()));
//                            new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), false).execute();
//                        }
//                    } else if (fav_driver_available > 0 && SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)) {
//                        new Dialog_Common().setmCustomDialog(getActivity(), this, NC.getResources().getString(R.string.message), fav_driver_message,
//                                NC.getResources().getString(R.string.ok),
//                                NC.getResources().getString(R.string.no_thanks), "1");
//                    } else {
//                        bookNow();
//                    }
//                } else {
//                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
//                }
//                } else {
//                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.nodrivers), "" + NC.getResources().getString(R.string.ok), "");
//                }        *//*

//                // bookNow();
                break;


        }
    }

    */
/**
     * Clear the data setups for the previous trip
     *//*

    private void clearPreviousData() {
        friend1S = 0;
        friend1S_a = 0;
        friend2S = 0;
        friend2S_a = 0;
        friend3S = 0;
        friend3S_a = 0;
        book_fav_Driver = 0;
        friend_a = 100;
        promo_code = "";
    }


    void moveCamera() {
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        if (sf.getDroplatlng() != null)
//            builder.include(sf.getDroplatlng());
//        if (sf.getPickuplatlng() != null)
//            builder.include(sf.getPickuplatlng());
//        else
//            builder.include(LastKnownLatLng);
//        LatLngBounds bounds = builder.build();
//        int padding = 100; // offset from edges of the map in pixels
//        System.out.println("__HHHd0f" + select_car_lay.getHeight());
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, displayWidth - 200, mapWrapperLayout.getHeight() - 200, padding);
//        map.moveCamera(cu);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.
        builder.include(pickup_marker.getPosition());
        builder.include(drop.getPosition());


//        if (sf.getDroplatlng() != null)
//            builder.include(sf.getDroplatlng());
//        if (sf.getPickuplatlng() != null)
//            builder.include(sf.getPickuplatlng());
//        else
//            builder.include(LastKnownLatLng);

        LatLngBounds bounds = builder.build();

        final int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
        int adjustHeight = (height - (carlayout.getHeight()));
        System.out.println("_________________>>>" + height + "___" + adjustHeight);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, adjustHeight, padding);
        if (FREE_TO_MOVE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FREE_TO_MOVE = true;
                }
            }, 3000);


            map.moveCamera(cu);
            if (pickup_marker != null && map.getProjection().toScreenLocation(pickup_marker.getPosition()).x > (width / 2)) {
                // map.setR
                updateCameraBearing();
            }

//            map.animateCamera(cu, new GoogleMap.CancelableCallback() {
//                @Override
//                public void onCancel() {
//                    //DO SOMETHING HERE IF YOU WANT TO REACT TO A USER TOUCH WHILE ANIMATING
//
//                }
//
//                @Override
//                public void onFinish() {
//                    System.out.println("_piooiopi" + map.getProjection().toScreenLocation(pickup_marker.getPosition()).x + "___" + (width / 2));
//                    if (pickup_marker != null && map.getProjection().toScreenLocation(pickup_marker.getPosition()).x > (width / 2)) {
//                        // map.setR
//                        updateCameraBearing();
//                    }
//                }
//            });
            FREE_TO_MOVE = false;
        }

    }

    private void updateCameraBearing() {
        if (map == null) return;
        CameraPosition camPos = CameraPosition
                .builder(
                        map.getCameraPosition() // current Camera
                )
                .bearing(200)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    */
/**
     * Book later dialog popup
     *//*

    private void book_later_fun() {
        try {

            Locale locale = new Locale("EN");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config, null);
//            Locale locale = getResources().getConfiguration().locale;
//            Locale.setDefault(locale);
            z = 1;
            PickupTime = "";
            final View r_view = View.inflate(getActivity(), R.layout.date_time_picker_dialog, null);
            FontHelper.applyFont(getActivity(), r_view.findViewById(R.id.inner_content));
            dt_mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            dt_mDialog.setContentView(r_view);
            dt_mDialog.setCancelable(true);
            dt_mDialog.show();
            Colorchange.ChangeColor((ViewGroup) dt_mDialog.findViewById(R.id.inner_content), getActivity());
            final DatePicker _datePicker = (DatePicker) dt_mDialog.findViewById(R.id.datePicker1);
            final TimePicker _timePicker = (TimePicker) dt_mDialog.findViewById(R.id.timePicker1);
            FontHelper.overrideFonts(getActivity(), _datePicker);
            FontHelper.overrideFonts(getActivity(), _timePicker);
            Calendar c = Calendar.getInstance();
            _timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
            _timePicker.setCurrentMinute(c.get(Calendar.MINUTE) + 1);
            _timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    // TODO Auto-generated method stub
                    Calendar c = Calendar.getInstance();
                    if (_datePicker.getDayOfMonth() == c.get(Calendar.DAY_OF_MONTH)) {
                        if ((c.get(Calendar.HOUR_OF_DAY) + 1) > hourOfDay) {
                            _timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
                            _timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
                        }
                        if ((c.get(Calendar.HOUR_OF_DAY) + 1) >= hourOfDay && (c.get(Calendar.MINUTE)) > minute) {
                            _timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY) + 1);
                            _timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
                        }
                    }
                }
            });
            Time now = new Time();
            now.setToNow();
            _datePicker.updateDate(now.year, now.month, now.monthDay);
            _datePicker.setMinDate(c.getTimeInMillis() - 1000);
            Button butConfirmTime = (Button) dt_mDialog.findViewById(R.id.butConfirmTime);
            TextView f_textview = (TextView) dt_mDialog.findViewById(R.id.f_textview);
            if (SessionSave.getSession("Lang", getActivity()).equals("ar")) {
                f_textview.setText(NC.getString(R.string.select_date_arab));
                butConfirmTime.setText(NC.getString(R.string.submit_arab));
            }
            butConfirmTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (_datePicker.isShown()) {
                        _timePicker.setVisibility(View.VISIBLE);
                        _datePicker.setVisibility(View.GONE);
                    } else {
                        getCurrentDateAndTime(_timePicker, _datePicker);
                        String seletecedString = "" + _year + "/" + _month + "/" + _date + " " + _timePicker.getCurrentHour() + ":" + _timePicker.getCurrentMinute();
                        if (hoursAgo(seletecedString) > 0) {
                            PickupTimeandDate = "" + _year + "-" + _month + "-" + _date + " " + _hour + ":" + _min + ":" + "00" + " " + _ampm;
                            if (sf.getPickuplatlng() != null && !sf.getPickuplocTxt().equalsIgnoreCase("" + NC.getResources().getString(R.string.fetching_address)))
                                new FindApproxDistance();
                            else
                                CToast.ShowToast(getActivity(), "nullllll");
                        } else {
                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.hour_must_greater_than), "" + NC.getResources().getString(R.string.ok), "");
                        }
                        dt_mDialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            // book_now_r.setClickable(true);
            book_later_r.setClickable(true);
        }
        dt_mDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(android.content.DialogInterface dialogInterface) {
                ((MainHomeFragmentActivity) getActivity()).setLocale();
            }
        });
    }


    */
/**
     * Custom alert dialog used in entire project.can call from anywhere with the following
     *
     * @param title       set the title for alert dialog
     * @param message     set the message for alert dialog
     * @param success_txt set the success text in success button
     * @param failure_txt set the failure text in failure button
     *//*


    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), mContext);
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
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

    private void ask_favourite_driver() {

    }

    */
/**
     * this method is used to call book taxi api
     *//*


    private void bookNow() {
        try {
            //  approximate_distanceandtime(D_latitude, D_longitude);
            if (sf.getDroplat() != 0) {
//                double approx_distance = FindDistance.distance(P_latitude, P_longitude, D_latitude, D_longitude, SessionSave.getSession("Metric_type", getActivity()));
//                try {
//                    approx_diste = approx_distance * 1000;
//                    approx_timee = approx_diste / 60;
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
                // new FindApproxDistance();

            }
            z = 1;
            final Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);
            int pYear = cal.get(Calendar.YEAR);
            int pMonth = cal.get(Calendar.MONTH);
            int pDay = cal.get(Calendar.DAY_OF_MONTH);
            updateTimer(hour, minute, pDay, pMonth, pYear, sec);
            if (TaxiUtil.mDriverdata.size() != 0) {
                if (sf.getPickuplocTxt().toString().trim().length() == 0 || (sf.getPickuplocTxt().toString().trim().equals(NC.getResources().getString(R.string.fetching_address))))
                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
                else {
                    JSONObject j = new JSONObject();
                    j.put("latitude", sf.getPickuplat());
                    j.put("longitude", sf.getPickuplng());
                    j.put("pickupplace", "" + sf.getPickuplocTxt() == null ? "" : Uri.encode(sf.getPickuplocTxt()));
                    j.put("dropplace", "" + sf.getDroplocTxt() == null ? "" : Uri.encode(sf.getDroplocTxt()));
                    j.put("drop_latitude", sf.getDroplat() == null ? "" : sf.getDroplat());
                    j.put("drop_longitude", sf.getDroplng() == null ? "" : sf.getDroplng());
                    j.put("pickup_time", PickupTime);
                    j.put("motor_model", carModel);
                    if (sf.getDroplat() != 0) {
                        j.put("approx_distance", approx_diste);
                    } else {
                        j.put("approx_duration", "0");
                    }
                    j.put("approx_duration", approx_timee);
                    j.put("cityname", "");
                    j.put("distance_away", E_time);
                    j.put("sub_logid", "");
                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                    j.put("request_type", "1");
                    // j.put("promo_code", ed_promocode.getText().toString());
                    j.put("promo_code", promo_code);
                    j.put("now_after", "0");
                    j.put("notes", SessionSave.getSession("notes", getActivity()));
                    j.put("passenger_app_version", MainActivity.APP_VERSION);
                    j.put("fav_driver_booking_type", book_fav_Driver);
                    j.put("friend_id2", friend1S);
                    j.put("friend_percentage2", friend1S_a);
                    j.put("friend_id3", friend2S);
                    j.put("friend_percentage3", friend2S_a);
                    j.put("friend_id4", friend3S);
                    j.put("friend_percentage4", friend3S_a);
                    j.put("friend_id1", SessionSave.getSession("Id", getActivity()));
                    j.put("friend_percentage1", friend_a);
                    j.put("passenger_payment_option", int_paymenttype);
                   */
/* j.put("pickup_notes", TaxiUtil.pickupnotes);
                    j.put("drop_notes", TaxiUtil.dropnotes);
                    j.put("drop_makani", TaxiUtil.dropmakanicode);
                    j.put("pickup_makani", TaxiUtil.pickupmakanicode);*//*

                    j.put("payment_mode", SessionSave.getSession("paymode", getActivity()));


                    if (!SessionSave.getSession("savedposition", getActivity()).equals("")) {
                        j.put("is_guest_booking", SessionSave.getSession("is_guest_booking", getActivity()));
                        j.put("guest_name", SessionSave.getSession("guest_name", getActivity()));
                        j.put("guest_phone", SessionSave.getSession("guest_phone", getActivity()));
                    } else {
                        j.put("is_guest_booking", "0");
                        j.put("guest_name", "");
                        j.put("guest_phone", "");
                    }
                    if (approx_fare != 0) {
                        if (friend2S_a != 0)
                            j.put("friend_percentage_amt3", (((double) friend2S_a / 100.00) * (double) approx_fare));
                        if (friend1S_a != 0)
                            j.put("friend_percentage_amt2", (((double) friend1S_a / 100.00) * (double) approx_fare));
                        if (friend3S_a != 0)
                            j.put("friend_percentage_amt4", (((double) friend3S_a / 100.00) * (double) approx_fare));

                        j.put("friend_percentage_amt1", (((double) friend_a / 100.00) * (double) approx_fare));
                    }
                    j.put("approx_trip_fare", approx_fare);
                    System.out.println("jsonnnnnn " + j.toString());
                    final String url = "type=savebooking";
                    SessionSave.saveSession("SearchUrl", url, getActivity());
                    Booking_type = "now";
                    if (!sf.getPickuplocTxt().toString().equalsIgnoreCase("" + NC.getResources().getString(R.string.fetching_address)))
                        new SearchTaxi(url, j);
                    else
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
                }
            } else {
                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.no_taxi), "" + NC.getResources().getString(R.string.ok), "");
            }
            // book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.header_header_bgcolorColor));
            // book_now_r.setClickable(true);
            //  book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
            book_later_r.setClickable(true);
        } catch (Exception e) {
            // TODO: handle exception
            // book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
            //  book_now_r.setClickable(true);
            //  book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
            book_later_r.setClickable(true);
            e.printStackTrace();
        }
    }


    */
/**
     * this method is used to set time
     *
     * @param day   set day of the month
     * @param hours set hour of the day
     * @param mins  set minutes of the hour
     * @param month set month of the year
     * @param sec   set seconds of the minute
     * @param year  set the selected year
     *//*


    public void updateTimer(int hours, final int mins, int day, int month, int year, int sec) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        // Append in a StringBuilder
        final String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(':').append(sec).append(" ").append(timeSet).toString();
        PickupTime = aTime;
        PickupTimeandDate = "" + day + "-" + month + "-" + year + " " + aTime;
    }


    //this method is used to get the pixels from dp
    public static int getPixelsFromDp(final Context context, final float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

//

    */
/**
     * this method is used to get current date and time
     *
     * @param _datePicker this is used to pick the date
     * @param _timePicker this is used to pick time
     *//*


    public void getCurrentDateAndTime(TimePicker _timePicker, DatePicker _datePicker) {

        _hour = _timePicker.getCurrentHour();
        _min = _timePicker.getCurrentMinute();
        _date = _datePicker.getDayOfMonth();
        _month = _datePicker.getMonth() + 1;
        _year = _datePicker.getYear();
        ampmValidation(_hour);
    }


    void clearsetPickDropMarker() {
        Log.e("clearsetPickDropMarker", "clearsetPickDropMarker: ");
        if (getActivity() != null) {
            if (booking_state == BOOKING_STATE.STATE_ONE || booking_state == BOOKING_STATE.STATE_TWO) {

                SessionSave.saveSession("notmovemarkers", "1", getActivity());
                //  map.clear();
                //      carlayout.setVisibility(View.GONE);
                //  map.setMyLocationEnabled(true);
                // SessionSave.saveSession("notmovemarkers", "0", getActivity());
                map.setOnCameraChangeListener(BookTaxiNewFrag.this);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                ((MainHomeFragmentActivity) getActivity()).enableSlide();
                //LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());
                if (LastKnownLatLng != null) {
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sf.getPickuplat(), sf.getPickuplng()), 16f));
                    map.setMyLocationEnabled(false);
                  */
/*  if (LastKnownLatLng != null)

                        map.addMarker(new MarkerOptions()
                                .position(LastKnownLatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_drop1)));*//*

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(sf.getPickuplat(), sf.getPickuplng()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));
//                if(CurrentCarModel!=null)
//                    CurrentCarModel.performClick();
                }

                navi_icon_book.setImageResource(R.drawable.menu);
                carlayout.setVisibility(View.VISIBLE);
                if (!confirm_ride_lay.isShown())
                    select_car_lay.setVisibility(View.VISIBLE);
                confirm_request.setVisibility(View.GONE);
                skip_fav_lay.setVisibility(View.GONE);

                instruction_header.setVisibility(View.GONE);
                map.getUiSettings().setMapToolbarEnabled(false);
                approx_far_pass.setText("");
                estimated_fare_c.setText("-");

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(D_latitude, D_longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.droplocation)));
                approx_fare = 0.0;

//                confirm_request.setVisibility(View.GONE);
//                select_car_lay.setVisibility(View.GONE);
//                confirm_ride_lay.setVisibility(View.GONE);
//                skip_fav_lay.setVisibility(View.VISIBLE);

//                setfav_drop();
                mapWrapperLayout.getLayoutParams().height = displayHeight;
//            } else if (booking_state == BOOKING_STATE.STATE_TWO) {
//                map.setMyLocationEnabled(false);
//                map.getUiSettings().setMyLocationButtonEnabled(false);
//                map.getUiSettings().setCompassEnabled(false);
//                ((MainHomeFragmentActivity) getActivity()).disableSlide();
//                map.clear();
                instruction_header.setText(getString(R.string.tap_to_edit));
                instruction_header.setVisibility(View.GONE);
                //ShowToast.top(getActivity(),getString(R.string.tap_to_edit));
                Glide.with(this).load(R.drawable.map_pick).override(500, 500).error(R.drawable.map_pick).into(mapppin);
                mapppin.setVisibility(View.GONE);
                map.getUiSettings().setMapToolbarEnabled(false);

//                if (select_car_lay.isShown()) {
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//                navi_icon_book.setImageResource(R.drawable.back_rounded);
                carlayout.setVisibility(View.VISIBLE);
                if (!confirm_ride_lay.isShown())
                    select_car_lay.setVisibility(View.VISIBLE);
                confirm_request.setVisibility(View.GONE);
                skip_fav_lay.setVisibility(View.GONE);
//                if (sf.getPickuplatlng() != null) {
//
//                    //   clearsetPickDropMarker();
//                    pickup_marker = map.addMarker(new MarkerOptions()
//                            .position(sf.getPickuplatlng())
//                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt()))));
//                    pickup_marker.setTitle("pick_up");
//                    rotateMarkerAccordingToPosition(pickup_marker);
//                }
//

                if (sf.getDroplatlng() != null) {

//                    instruction_header.setVisibility(View.VISIBLE);
//                    closePopup();
//                    drop = map.addMarker(new MarkerOptions()
//                            .position(sf.getDroplatlng())
//                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(), getActivity()))));
//                    sf.dropGone();
//                    drop.setTitle("drop");
                    LatLng ll = sf.getPickuplatlng() == null ? LastKnownLatLng : sf.getPickuplatlng();
                    System.out.println("_________Drop***ss" + SplashActivity.isBUISNESSKEY + "__" + ll);
                    if (ll != null && ll.longitude != 0.0) {

//                    pickup_marker = map.addMarker(new MarkerOptions()
//                            .position(ll)
//                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity()))));
//                    pickup_marker.setTitle("pick_up");
//                        new Route().drawRoute(map, getActivity(), ll, sf.getDroplatlng(), "en", Color.parseColor("#000000"));
                        double Driverdistance;
                        if (SplashActivity.isBUISNESSKEY)
                            new FindApproxDistance(getActivity(), ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);
                        else {
                            Driverdistance = 0.5 + FindDistance.distance(ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), SessionSave.getSession("Metric_type", getActivity()), location);
                            new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_FARE).execute();

                        }
                    }
                }
//
//                    //  int layHeight = select_car_lay.getHeight();
//
//                    select_car_lay.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            int layHeight = select_car_lay.getHeight();
//                            int requiredMapHeight = displayHeight - layHeight;
//                            System.out.println("____________ss" + layHeight + "____" + displayHeight + "___" + requiredMapHeight + 20);
//                            mapWrapperLayout.getLayoutParams().height = requiredMapHeight;
//                            moveCamera();
//                        }
//                    });
//
//                } else {
//                    sf.dropVisible();
//                }

                // sf.dropVisible();
            } else if (booking_state == BOOKING_STATE.STATE_THREE) {
                map.clear();
                map.getUiSettings().setMapToolbarEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                ((MainHomeFragmentActivity) getActivity()).disableSlide();
                carlayout.setVisibility(View.GONE);
                if (map != null) {
                    map.setMyLocationEnabled(false);
                }
                mapWrapperLayout.getLayoutParams().height = displayHeight;
                instruction_header.setVisibility(View.GONE);
                instruction_header.setText(getString(R.string.move_map_to));
                closePopup();
                //ShowToast.top(getActivity(),getString(R.string.move_map_to));
                // navi_icon_book.setImageResource(R.drawable.back_rounded);
                skip_fav_lay.setVisibility(View.GONE);

                if (sf.getPickuplocTxt().trim().equals("")) {
                    handlerServercall1.postDelayed(callAddress_drag, 0);

                }
                LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());
                if (ll != null && map != null && map.getCameraPosition().zoom < 15)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13f));
                //  sf.dropGone();
                confirm_request.setVisibility(View.VISIBLE);
                select_car_lay.setVisibility(View.GONE);
                confirm_ride_lay.setVisibility(View.GONE);
                map.setOnCameraChangeListener(BookTaxiNewFrag.this);
                //  Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "pickup.png").error(R.drawable.pickuplocation).into(mapppin);

                pickloc_confirm.setText(sf.getPickuplocTxt());
                mapppin.setVisibility(View.GONE);
                Log.e("clear", "clearsetPickDropMarker: 1");

                if (LastKnownLatLng != null && LastKnownLng != 0.0) {
                    Log.e("clear", "clearsetPickDropMarker:2 ");
                    if (sf.getDroplatlng() != null && sf.getDroplatlng().latitude != 0 && sf.getDroplatlng().longitude != 0) {
                        // map.clear();
                        Log.e("clear", "clearsetPickDropMarker:3 " + dragLatLng + "__" + sf.getDroplatlng());

                        new Route().drawRoute(map, getActivity(), dragLatLng == null ? sf.getPickuplatlng() : dragLatLng, sf.getDroplatlng(), "en", Color.parseColor("#000000"));
                      */
/*  map.addMarker(new MarkerOptions()
                                .position(LastKnownLatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin)));*//*

                        Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                        pickup_marker = map.addMarker(new MarkerOptions()
                                .position(sf.getPickuplatlng())
                                .icon(BitmapDescriptorFactory.fromBitmap(b)));
                        pickup_marker.setTitle("pick_up");

                        drop = map.addMarker(new MarkerOptions()
                                .position(sf.getDroplatlng())
                                .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(), getActivity()))));
                        if (sf.getDroplatlng() != null) {
                            setbounds(sf.getPickuplatlng(), sf.getDroplatlng());
                        }
                        //String locationname, int count, String lat, String lon
                        //  mydb.insertlocation(sf.getPickuplocTxt(), sf.getDroplocTxt(), 1, String.valueOf(sf.getPickuplatlng()), String.valueOf(sf.getDroplatlng()));
//                        lat_lang = new LatLng(sf.getPickuplatlng(), sf.getDroplatlng());
                        // insert in to dp
                        //String p_location, String d_location, int count, String lat, String lon
                    } else {
                        Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                        map.clear();
                        pickup_marker = map.addMarker(new MarkerOptions()
                                .position(sf.getPickuplatlng())
                                .icon(BitmapDescriptorFactory.fromBitmap(b)));
                        setbounds(sf.getPickuplatlng(), sf.getPickuplatlng());
                        pickup_marker.setTitle("pick_up");

                    }


                } */
/*else {
                    map.addMarker(new MarkerOptions()
                            .position(sf.getPickuplatlng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.current)));
                }*//*



            }
        }
    }

    private void setbounds(LatLng pickup, LatLng drop) {
        // BOUNDS SET
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.

        builder.include(pickup);
        builder.include(drop);

        LatLngBounds bounds = builder.build();

        final int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
//        int adjustHeight = (height - (carlayout.getHeight()));
//        System.out.println("_________________>>>" + height + "___" + adjustHeight);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        map.moveCamera(cu);
    }

    private void rotateMarkerAccordingToPosition(Marker pickup_marker) {
        //   if()
    }

    */
/**
     * this method is used to check the am & pm for given input time
     *
     * @param inputHour hour is given as input
     *//*


    private String ampmValidation(int inputHour) {
        _ampm = "AM";
        if (inputHour >= 13) {
            _hour = inputHour - 12;
            _ampm = "PM";
        } else if (inputHour == 12) {
            _ampm = "PM";
        } else if (inputHour == 0) {
            _hour = 12;
            _ampm = "AM";
        }
        return _ampm;
    }

    */
/**
     * this method is used to find the nearest driver
     *//*


    public void FindNearestlocal() {
        // TODO Auto-generated method stub
        Marker m = null;
        System.out.println("__________________" + min_fare + "___" + min_ppl);
        try {
            // fare_minimum.setText(SessionSave.getSession("Currency", getActivity()) + " " + min_fare);

            if (TaxiUtil.mDriverdata.size() != 0) {
                double Driverdistance = 0;
                //  clearsetPickDropMarker();
                if (TaxiUtil.d_lat != 0)
                    //    dropmap = map.addMarker(new MarkerOptions().position(new LatLng(TaxiUtil.d_lat, TaxiUtil.d_lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pick)).draggable(false));
                    availablecarcount = 0;
                for (int i = 0; i < TaxiUtil.mDriverdata.size(); i++) {
                    if (i == 0) {
                        if (SplashActivity.isBUISNESSKEY)
                            new FindApproxDistance(getActivity(), sf.getPickuplat(), sf.getPickuplng(), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()), DISTANCE_TYPE_FOR_ETA);
                        else
                            Driverdistance = 0.5 + FindDistance.distance(sf.getPickuplat(), sf.getPickuplng(), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()), SessionSave.getSession("Metric_type", getActivity()), location);

                    }
                    availablecarcount++;
                    m = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(new LatLng(Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()))));
                }
                if (availablecarcount != 0) {
                    if (!SplashActivity.isBUISNESSKEY)
                        new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_ETA).execute();
                    System.out.println("Car_avaliable" + "  1" + String.valueOf(availablecarcount) + selected_carmodel_name);


                    availablecars.setText("- " + String.format(Locale.UK, String.valueOf(availablecarcount)) + " " + NC.getResources().getString(R.string.avaiable_car));
                    model_avail.setText(selected_carmodel_name);
                    dotsProgressBar1.setVisibility(View.GONE);
                } else {
                    dotsProgressBar1.setVisibility(View.GONE);
                    E_time = 0;
                    approx_fare = 0.0;
                    Approxi_time("no");

                    System.out.println("Car_avaliable" + "  2" + NC.getResources().getString(R.string.nodrivers));


                    availablecars.setText("- " + NC.getResources().getString(R.string.nodrivers));
                    model_avail.setText(selected_carmodel_name);
                    if (booking_state == BOOKING_STATE.STATE_TWO) {
                        System.out.println("___________notaxi" + (int) E_time);
                        if (pickup_marker != null)
                            pickup_marker.remove();
                        // Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                        */
/*pickup_marker = map.addMarker(new MarkerOptions()
                                .position(sf.getPickuplatlng())
                                .icon(BitmapDescriptorFactory.fromBitmap(b)));
                        pickup_marker.setTitle("pick_up");*//*

                    }
                }

                if (bol_isFromBooknow) {
                    if (availablecarcount > 0) {

                        bol_isFromBooknow = false;

                        if (!(sf.getPickuplocTxt().toString().trim().length() == 0 || (sf.getPickuplocTxt().toString().trim().equals(NC.getResources().getString(R.string.fetching_address))))) {
                            if (sf.getDroplocTxt() != null && sf.getDroplocTxt().toString().trim().length() > 0) {
                                isBookAfter = false;
                                if (SplashActivity.isBUISNESSKEY)
                                    new FindApproxDistance();
                                else {
                                    double Driverdistance1 = 0.5 + FindDistance.distance(sf.getPickuplat(), sf.getPickuplng(), sf.getDroplat(), sf.getDroplng(), SessionSave.getSession("Metric_type", getActivity()), location);
                                    new Approximate_Time(Driverdistance1, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_BOOK_LATER).execute();
                                }
                            } else if (fav_driver_available > 0 && SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)) {

                                //If splitfare settings is On then drop location is must
                                if (SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true))
                                    Toast.makeText(getActivity(), NC.getResources().getString(R.string.select_the_drop_location), Toast.LENGTH_SHORT).show();
                                else {
                                    new Dialog_Common().setmCustomDialog(getActivity(), this, NC.getResources().getString(R.string.message), fav_driver_message,
                                            NC.getResources().getString(R.string.ok),
                                            NC.getResources().getString(R.string.no_thanks), "1");
                                }

                            } else {

                                //alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_drop_location), "" + NC.getResources().getString(R.string.ok), "");

                                //If splitfare settings is On then drop location is must
                                if (SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true))
                                    Toast.makeText(getActivity(), NC.getResources().getString(R.string.select_the_drop_location), Toast.LENGTH_SHORT).show();
                                else
                                    bookNow();
                            }
                        } else {
                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    } else {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.nodrivers), "" + NC.getResources().getString(R.string.ok), "");
                    }
                }

            } else {

                E_time = 0;
                approx_fare = 0.0;
                Approxi_time("no");

//                if (bol_isFromBooknow) {
//                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.nodrivers), "" + NC.getResources().getString(R.string.ok), "");
//                }

                System.out.println("Car_avaliable" + "  3" + NC.getResources().getString(R.string.nodrivers));

                availablecars.setText("- " + NC.getResources().getString(R.string.nodrivers));
                model_avail.setText(selected_carmodel_name);
            }

            System.gc();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


//    private void setLocation(final double p_latitude2, final double p_longitude2, final String string) {
//        // TODO Auto-generated method stub
//        try {
//            //   book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.header_header_bgcolorColor));
//            book_now_r.setClickable(true);
//            map.addMarker(new MarkerOptions().icon(null).position(new LatLng(p_latitude2, p_longitude2)).draggable(false).visible(false));
//            if (TaxiUtil.d_lat != 0)
//                dropmap = map.addMarker(new MarkerOptions().position(new LatLng(TaxiUtil.d_lat, TaxiUtil.d_lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pick)).draggable(false));
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p_latitude2, p_longitude2), Zoom));
//        } catch (final Exception e) {
//            e.printStackTrace();
//        }
//    }

    */
/**
     * this method is used to calculate difference between dates
     *//*


    public static int hoursAgo(String datetime) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.ENGLISH).parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date now = Calendar.getInstance().getTime(); // Get time now
        long differenceInMillis = date.getTime() - now.getTime();
        long differenceInHours = (differenceInMillis) / 1000L / 60L / 60L; //
        return (int) differenceInHours;
    }


    private void bottomVisGoneAnim(String animType) {
        if (animType.equals(BOOK_NOW_ANIM)) {
            AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.0f);
            animation2.setDuration(500);

            carlayout.startAnimation(animation2);
            AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
            animation1.setDuration(500);
            animation1.setStartOffset(200);
            book_now_bottom.setVisibility(View.VISIBLE);
            book_now_bottom.startAnimation(animation1);

            animation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    carlayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        } else if (animType.equals(BookTaxiNewFrag.CANCEL_ANIM)) {
//            ObjectAnimator anim = ObjectAnimator.ofFloat(book_now_bottom,"y",book_now_bottom.getHeight());
//            anim.setDuration(400);
//            anim.start();
//            anim.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    book_now_bottom.setVisibility(View.GONE);
            //  carlayout.setVisibility(View.VISIBLE);
//                    ObjectAnimator anims = ObjectAnimator.ofFloat(carlayout, "y",(2*carlayout.getHeight()));
//                    anims.setDuration(1000);
//                    anims.start();
//
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
            book_now_bottom.setVisibility(View.GONE);
            carlayout.setVisibility(View.VISIBLE);

        }
    }

    */
/**
     * this method is used to change map position
     *//*


    @Override
    public void onCameraChange(CameraPosition pos) {
        if (NetworkStatus.isOnline(getActivity())) {
            Log.d("onCameraChange", "onCameraChange" + pos.target.latitude + "---" + book_again_msg);
            System.gc();
//            if (sf != null)
//                sf.removeFav();
            try {
                //  if (book_again_msg != null) {
                System.out.println("Locationaadd" + book_again_msg);
                if (book_again_msg) {
                    System.out.println("Locationaadd" + book_again_msg);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            book_again_msg = false;
                        }
                    }, 1000);
                } else {
                   */
/* if (booking_state == BOOKING_STATE.STATE_THREE || booking_state == BOOKING_STATE.STATE_TWO) {
                        dragLatLng = pos.target;
                        Log.e("Location", "onCameraChange: ca");*//*

                    //  if (alert_bundle.getString("pickup_location") == null)
                    System.out.println("move_marker" + SessionSave.getSession("notmovemarkers", getActivity()));
                    System.out.println("remove_marker" + SessionSave.getSession("remove_marker_clear", getActivity()));
                    System.out.println("flagflagflag" + flag);

                    if (!SessionSave.getSession("notmovemarkers", getActivity()).equals("1")) {
                        //  System.out.println("alertb" + alert_bundle.getString("pickup_location"));
                        if (flag) {
                            D_latitude = pos.target.latitude;
                            D_longitude = pos.target.longitude;
                        } else {
                            P_latitude = pos.target.latitude;
                            P_longitude = pos.target.longitude;
                        }

                        if (!SessionSave.getSession("remove_marker_clear", getActivity()).equals("1")) {
                            if (flag) {
                                address = new Address_s(getActivity(), new LatLng(D_latitude, D_longitude));
                                address.execute().get();
                                dropLocationPin(P_latitude, P_longitude, D_latitude, D_longitude);

                            } else {
                                address = new Address_s(getActivity(), new LatLng(P_latitude, P_longitude));
                                address.execute().get();
                                droploactiontopickup(P_latitude, P_longitude);
                            }
                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }

    */
/**
     * this method is used to fetching address for pick up location
     *//*

    public static void setfetch_address() {

        //sf.setPickuplocTxt("Fetching  address");
    }

    */
/*
    * this method is used to
    * *//*

    public void onSplitSuccess(double primary_Percent, double f1, double f2, double f3, double fa1, double fa2, double fa3) {
        friend_a = primary_Percent;
        friend1S = f1;
        friend1S_a = fa1;
        friend2S = f2;
        friend2S_a = fa2;
        friend3S = f3;
        friend3S_a = fa3;
        if (fav_driver_available > 0 && SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)) {
            new Dialog_Common().setmCustomDialog(getActivity(), this, NC.getResources().getString(R.string.message), fav_driver_message,
                    NC.getResources().getString(R.string.ok),
                    NC.getResources().getString(R.string.no_thanks), "1");
        } else {
            bookNow();
        }

    }

    @Override
    public void onSuccess(View view, Dialog dialog, String resultCode) {
        if (resultCode.equals("1")) {
            book_fav_Driver = 1;
            bookNow();
        } else if (resultCode.equals("4")) {
            book_fav_Driver = 2;
            bookNow();
        } else if (resultCode.equals("GcmDialog")) {
            //approvReques("A");
        }
        dialog.dismiss();
    }


    @Override
    public void onFailure(View view, Dialog dialog, String resultCode) {
        if (resultCode.equals("1")) {
            book_fav_Driver = 2;
            bookNow();
        }
        dialog.dismiss();
    }


    */
/*
    * this method is used to get the location accuracy
    * *//*

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (getActivity() != null) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(1000); // Update location every second
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location != null) {
                    LastKnownLat = location.getLatitude();
                    LastKnownLng = location.getLongitude();
                    try {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(LastKnownLat, LastKnownLng, 1);
                        //                if (addresses.size() > 0)
                        //                    System.out.println(addresses.get(0).getLocality());
                        current_address = addresses.get(0).getAddressLine(1);
                        if (alert_bundle == null || alert_bundle.getString("pickup_location") == null) {
                            // System.out.println("bookpickupnew****@@" + alert_bundle.getString("pickup_location"));
                            sf.setPickuplocTxt(current_address);
                            SessionSave.saveSession("current_address", current_address, getActivity());
                        }
                        System.out.println("pickup_location___" + current_address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LastKnownLatLng = new LatLng(LastKnownLat, LastKnownLng);
                    if (map != null && (alert_bundle.getString("pickup_location") == null))
                        sf.setPickuplatlng(LastKnownLatLng);
                    P_latitude = LastKnownLat;
                    P_longitude = LastKnownLng;


                } else {
                    Toast.makeText(getActivity(), "FusedLocationApi not call", Toast.LENGTH_SHORT).show();
                }
                //        new Handler().postDelayed(new Runnable() {
                //            @Override
                //            public void run() {
                //                movetoCurrentloc();
                //                address = new Address_s(getActivity(), new LatLng(P_latitude, P_longitude));
                //                try {
                //                    address.execute().get();
                //                } catch (InterruptedException e) {
                //                    e.printStackTrace();
                //                } catch (ExecutionException e) {
                //                    e.printStackTrace();
                //                }
                //            }
                //        }, 500);

                if (sf.getPickuplatlng() != null)
                    map.setOnCameraChangeListener(this);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mGoogleApiClient.isConnected())
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, BookTaxiNewFrag.this);
                else
                    mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LastKnownLat = location.getLatitude();
        LastKnownLng = location.getLongitude();
        LastKnownLatLng = new LatLng(LastKnownLat, LastKnownLng);
        if (!TaxiUtil.islocpickup) {
            if (sf.currentlocETxt.getText().toString().trim().length() == 0) {
                BookTaxiNewFrag.flag = false;
                try {
                    address = new Address_s(getActivity(), new LatLng(LastKnownLat, LastKnownLng));
                    address.execute().get();
                    droploactiontopickup(LastKnownLat, LastKnownLng);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        P_latitude = LastKnownLat;
//        P_longitude = LastKnownLng;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void trigger_FragPopFront() {
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof BookTaxiNewFrag) {
            ((MainHomeFragmentActivity) getActivity()).homePage_title();
            ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);

            ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
            ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");


            ((MainHomeFragmentActivity) getActivity()).enableSlide();

        }
//        if (getActivity() != null && CurrentCarModel != null && booking_state != BOOKING_STATE.STATE_THREE)
//            CurrentCarModel.performClick();
        System.out.println("coming or not***...");
        if (CurrentCarModel != null) {
            ((TextView) CurrentCarModel.findViewById(R.id.txt_model1)).setTypeface(Typeface.DEFAULT_BOLD);
            System.out.println("coming or not...");
        }
    }

    @Override
    public void pickUpSet(double latitude, double longtitue) {
//        map.addMarker(new MarkerOptions().position(new LatLng(P_latitude, P_longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).title("" +NC.getResources().getString(R.string.droplocation)));
//        carlayout.setVisibility(View.VISIBLE);
//        skip_fav.setVisibility(View.GONE);
//        if (CurrentCarModel != null && booking_state != BOOKING_STATE.STATE_THREE)
//            CurrentCarModel.performClick();

        if (!carlayout.isShown() && booking_state != BOOKING_STATE.STATE_THREE) {
            if (LocationSearchActivityNew.SET_FOR_PICKUP) {
                booking_state = BOOKING_STATE.STATE_TWO;
                //  clearsetPickDropMarker();
                LocationSearchActivityNew.SET_FOR_PICKUP = false;
            }
        }

        if (booking_state == BOOKING_STATE.STATE_THREE) {
            clearsetPickDropMarker();
            pickloc_confirm.setText(sf.getPickuplocTxt());
        }
    }

    @Override
    public void dropSet(double latitude, double longtitue) {
        booking_state = BOOKING_STATE.STATE_TWO;
        if (!carlayout.isShown())
            clearsetPickDropMarker();
    }


    private class GetNearTaxi implements APIResult {
        private Marker m;

        public GetNearTaxi(final String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            SessionSave.saveSession("LastServer_latitude", "" + sf.getPickuplat(), getActivity());
            SessionSave.saveSession("LastServer_longitude", sf.getPickuplng() + "", getActivity());

            if (bol_isFromBooknow)
                new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
            else
                new APIService_Retrofit_JSON_NoProgress(getActivity(), this, data, false).execute(url);

        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            System.out.println("_________Drop***** " + result);
            availablecars.setVisibility(View.VISIBLE);

            System.out.println("Car_avaliable" + "  7" + NC.getResources().getString(R.string.nodrivers) + selected_carmodel_name);
            model_avail.setVisibility(View.VISIBLE);
            if (skip_fav.getVisibility() == View.GONE)
                carlayout.setVisibility(View.VISIBLE);
            if (TaxiUtil.mDriverdata.size() != 0)
                TaxiUtil.mDriverdata.clear();
            if (m != null) {
                m.remove();
            }
            if (TaxiUtil.d_lat != 0) {
                if (dropmap != null)
                    dropmap.remove();
            }
            E_time = 0;
            if (isSuccess) {
                String Driverid, Drivername, Lat, Lng, Nearest, distance, driver_coordinates;
                availablecarcount = 0;
                try {
                    if (conformbooking) {
                        conformbooking = false;
                    } else {
                        if (map != null)
                            map.clear();
                    }

                    LatLng ll = sf.getPickuplatlng() == null ? LastKnownLatLng : sf.getPickuplatlng();
                    if (booking_state == BOOKING_STATE.STATE_TWO) {
                        if (sf.getDroplatlng() != null && sf.getDroplat() != 0.0) {

                            System.out.println("_________Dropokkkkkkkkkk8888");
                            // instruction_header.setVisibility(View.VISIBLE);
                            closePopup();
//                            drop = map.addMarker(new MarkerOptions()
//                                    .position(sf.getDroplatlng())
//                                    .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(), getActivity()))));
                            // sf.dropGone();
                            // drop.setTitle("drop");
                            // drop.setAnchor(1.0f, 1f);

                            System.out.println("_________" + sf.getPickuplocTxt() + "___" + ll.latitude);
                            if (ll != null && ll.longitude != 0.0) {

                                //  new Route().drawRoute(map, getActivity(), ll, sf.getDroplatlng(), "en", Color.parseColor("#000000"));
                                double Driverdistance;
                                if (SplashActivity.isBUISNESSKEY)
                                    new FindApproxDistance(getActivity(), ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);
                                else {
                                    Driverdistance = 0.5 + FindDistance.distance(ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), SessionSave.getSession("Metric_type", getActivity()), location);
                                    new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_FARE).execute();

                                }
                            }


                        } else {
                            if (!sf.getPickuplocTxt().trim().equals(""))
                                sf.dropVisible();
                            // else
                            // sf.dropGone();
                        }
                    }
                    dropLocationPin(ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng());
                    SessionSave.saveSession("Server_Response", result, getActivity());
                    // TaxiUtil.server_response = result;
                   */
/* if (sf.getDroplatlng() != null) {
                        System.out.println("_________Drop");
                        LatLng ll = sf.getPickuplatlng() == null ? LastKnownLatLng : sf.getPickuplatlng();
                        if (ll != null && ll.longitude != 0.0) {

                            double Driverdistance;
                            if (SplashAct.isBUISNESSKEY)
                                new FindApproxDistance(getActivity(), ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);
                            else {
                                Driverdistance = 0.5 + FindDistance.distance(ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), SessionSave.getSession("Metric_type", getActivity()), location);
                                new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_FARE).execute();
                                if (!approx_far_pass.getText().equals("")) {
                                    approx_far_pass.setTextColor(CL.getColor(getActivity(), R.color.black));
                                    approx_far_pass.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }*//*

                    //  System.out.println("favdriver***" + TaxiUtil.server_response);
                    System.out.println("favdriver" + result);

                    final JSONObject json = new JSONObject(result);
                    speed = json.getJSONObject("fare_details").getString("taxi_speed");
                    System.out.println("_dddnagaaaaa" + speed);
                    System.out.println("elu checking json" + json.toString());
                    popular_places.clear();
                    System.out.println("json" + json.getJSONArray("favourite_places").length());
                    if (json.getJSONArray("favourite_places").length() == 0) {
                        SessionSave.saveSession("popular_places", "", getActivity());
                    }
                    if (json.getJSONArray("favourite_places").length() > 0) {
                   */
/* if (json.getJSONArray("favourite_places").length() > 0) {*//*

                        // SessionSave.saveSession("popular_places", json.getJSONArray("favourite_places").toString(), getActivity());
                        // SessionSave.saveSession("popular_places", json.getJSONArray("favourite_places").toString(), getActivity());
                        for (int j = 0; j < json.getJSONArray("favourite_places").length(); j++) {
                            int i = 0;

                            //To reverse the array
                            if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
                                i = json.getJSONArray("favourite_places").length() - (j + 1);
                            else
                                i = j;

                            JSONObject jo = json.getJSONArray("favourite_places").getJSONObject(i);
                            Double latc = jo.getDouble("latitude");
                            Double lngc = jo.getDouble("longtitute");
                            System.out.print("pkacessss" + ".................." + jo.toString());
                            float[] dist = new float[1];
                            new Location("").distanceBetween(LastKnownLat, LastKnownLng, latc, lngc, dist);
                            System.out.println("___________dist" + dist[0]);
                             */
/*if (dist[0] > 1000) {*//*

                            PlacesDetail o = new PlacesDetail();
                            o.setLabel_name(TaxiUtil.firstLetterCaps(jo.getString("label_name")));
                            o.setLatitude(jo.getDouble("latitude"));
                            o.setLongtitute(jo.getDouble("longtitute"));
                            o.setLocation_name(jo.getString("location_name"));
                            o.setAndroid_image_unfocus(jo.getString("android_icon"));
                            popular_places.add(o);
                            */
/*}*//*

                            skip_fav.setText(NC.getString(R.string.skip_favourite));
                        }
                    } else if (json.getJSONArray("popular_places").length() > 0) {
                        //  SessionSave.saveSession("popular_places", json.getJSONArray("popular_places").toString(), getActivity());
                        for (int j = 0; j < json.getJSONArray("popular_places").length(); j++) {
                            int i = 0;
                            //To reverse the array
                            if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
                                i = json.getJSONArray("popular_places").length() - (j + 1);
                            else
                                i = j;
                            JSONObject jo = json.getJSONArray("popular_places").getJSONObject(i);
                            Double latc = jo.getDouble("latitude");
                            Double lngc = jo.getDouble("longtitute");
                            float[] dist = new float[1];
                            new Location("").distanceBetween(LastKnownLat, LastKnownLng, latc, lngc, dist);
                            System.out.println("___________dist" + dist[0]);
                            */
/*if (dist[0] > 1000) {*//*

                            PlacesDetail o = new PlacesDetail();
                            o.setLabel_name(TaxiUtil.firstLetterCaps(jo.getString("label_name")));
                            o.setLatitude(jo.getDouble("latitude"));
                            o.setLongtitute(jo.getDouble("longtitute"));
                            o.setLocation_name(jo.getString("location_name"));
                            o.setAndroid_image_unfocus((jo.getString("android_icon")));
                            popular_places.add(o);
                           */
/* }*//*

                            skip_fav.setText(NC.getString(R.string.skip_popular));
                        }
                    }

                    if (popular_places.size() > 0) {
                        //skip_fav.setText(NC.getString(R.string.skip_drop_location));
                        JSONArray popular_place_array = new JSONArray();

                        for (int i = 0; i < popular_places.size(); i++) {
                            PlacesDetail o = popular_places.get(i);
                            JSONObject ss = new JSONObject();
                            ss.put("label_name", o.getLabel_name());
                            ss.put("latitude", o.getLatitude());
                            ss.put("longtitute", o.getLongtitute());
                            ss.put("location_name", o.getLocation_name());
                            ss.put("android_icon", o.getAndroid_image_unfocus());
                            popular_place_array.put(ss);
                            System.out.print("popular places" + "......" + ss.toString());
                        }
                        // System.out.print("popular places" + "......" + ss.toString());
                        SessionSave.saveSession("popular_places", popular_place_array.toString(), getActivity());
                        sf.setsearch_favlocation();

                    } else {

                       */
/* if (popular_places.size() > 3)
                            for (int i = popular_places.size() - 1; i >= 3; i--) {
                                popular_places.remove(i);
                            }*//*



                    }


                    //  setfav_drop();
                    if (json.getInt("status") == 1) {

                        fav_driver_available = json.getInt("fav_drivers");
                        fav_driver_message = json.getString("fav_driver_message");
                        final JSONArray jarray = json.getJSONArray("detail");
                        SessionSave.saveSession("driver_around_miles", json.getString("driver_around_miles"), getActivity());
                        final int l = jarray.length();


//                        for(int j=0;j<json.getJSONArray("favourite_places").length();j++){
//                            JSONObject jo=json.getJSONArray("popular_places").getJSONObject(j);
//                            PlacesDetail o=new PlacesDetail();
//                            o.setLabel_name(jo.getString("label_name"));
//                            o.setLatitude(jo.getDouble("latitude"));
//                            o.setLongtitute(jo.getDouble("longtitute"));
//                            o.setLocation_name(jo.getString("location_name"));
//                            favourite_places.add(o);
//                        }

                        for (int i = 0; i < l; i++) {
                            Driverid = jarray.getJSONObject(i).getString("driver_id");
                            Drivername = "";// jarray.getJSONObject(i).getString("name");
                            Lat = jarray.getJSONObject(i).getString("latitude");
                            Lng = jarray.getJSONObject(i).getString("longitude");
                            //speed = jarray.getJSONObject(i).getString("taxi_speed");
                            Nearest = jarray.getJSONObject(i).getString("nearest_driver");
                            distance = jarray.getJSONObject(i).getString("distance_km");
                            driver_coordinates = jarray.getJSONObject(i).getString("driver_coordinates");
                            List<String> listlatlng = new ArrayList<String>();
                            String[] latlng = driver_coordinates.split("#");
                            for (int x = 0; x < latlng.length; x++) {
                                listlatlng.add(latlng[x]);
                            }

                            final DriverData data = new DriverData(Driverid, Drivername, speed, Lat, Lng, Nearest, distance, m, listlatlng);
                            TaxiUtil.mDriverdata.add(data);
                            min_fare = json.getJSONObject("fare_details").getString("min_fare");
                            min_ppl = json.getJSONObject("fare_details").getString("model_size");
                            fare_frag.setText(json.getJSONObject("fare_details").getString("description"));

                            //  eta_ = "0";
                            //  fare_minimum.setText(SessionSave.getSession("Currency", getActivity()) + " " + min_fare);
                            fare_minimum_ppl.setText("1-" + min_ppl);

                        }


                        FindNearestlocal();
                    } else {
                        Approxi_time("no");
                        dotsProgressBar1.setVisibility(View.GONE);
                        System.out.println("Car_avaliable" + "  4" + NC.getResources().getString(R.string.nodrivers) + selected_carmodel_name);
                        availablecars.setText("- " + NC.getResources().getString(R.string.nodrivers));
                        model_avail.setText(selected_carmodel_name);

                        min_fare = json.getJSONObject("fare_details").getString("min_fare");
                        min_ppl = json.getJSONObject("fare_details").getString("model_size");
                        fare_frag.setText(json.getJSONObject("fare_details").getString("description"));
                        // eta_ = "0";
                        //   fare_minimum.setText(SessionSave.getSession("Currency", getActivity()) + " " + min_fare);
                        //fare_minimum_ppl.setText(min_ppl + " " + getString(R.string.ppl));
                        fare_minimum_ppl.setText("1-" + min_ppl);

                        if (booking_state == BOOKING_STATE.STATE_TWO) {
                            System.out.println("___________notaxi" + (int) E_time);
                            if (pickup_marker != null)
                                pickup_marker.remove();
                            //  Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                            */
/*pickup_marker = map.addMarker(new MarkerOptions()
                                    .position(sf.getPickuplatlng())
                                    .icon(BitmapDescriptorFactory.fromBitmap(b)));
                            pickup_marker.setTitle("pick_up");*//*

                        }
//                        if (bol_isFromBooknow) {
//                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.nodrivers), "" + NC.getResources().getString(R.string.ok), "");
//                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        //  ShowToast(getActivity(), result);
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void setfav_drop() {
        fav_bot_lay.removeAllViews();

        for (PlacesDetail p : popular_places) {
            System.out.println("_________________PPP" + p.getLabel_name());
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_fav_itme, fav_bot_lay, false);
            LinearLayout ll = (LinearLayout) v.findViewById(R.id.fav_item_lay);
            ll.setTag(p);
            Glide.with(getActivity()).load(p.android_image_unfocus)
                    .override(100, 100)
                    .centerCrop()
                    .into((ImageView) v.findViewById(R.id.fav_image));

            System.out.println("_________________PPsdfsfs" + p.getLatitude() + "__" + p.getLocation_name());
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlacesDetail p = (PlacesDetail) view.getTag();
                    System.out.println("_________________PPP" + p.getLatitude() + "__" + p.getLocation_name());
                    sf.setDroplocTxt(p.getLocation_name());
                    sf.setDroplatlng(new LatLng(p.getLatitude(), p.getLongtitute()));
                }
            });

            ImageView fav_image = (ImageView) v.findViewById(R.id.fav_image);
            TextView fav_label = (TextView) v.findViewById(R.id.fav_label);
            fav_label.setText(p.getLabel_name());
            fav_bot_lay.addView(v);
        }


    }

    */
/**
     * this method is used to show appoximate time to arrival of taxi
     *
     * @param status its passed the parameter status yes or no
     *//*


    public void Approxi_time(String status) {
        // TODO Auto-generated method stub

        if (txt_min_car1 != null) {
            txt_min_car1.setText("ETA " + (int) E_time + "" + " " + NC.getResources().getString(R.string.min));
            if (status.equals("yes")) {
                txt_min_car1.setVisibility(View.VISIBLE);
            } else {
                txt_min_car1.setText("");
                txt_min_car1.setVisibility(View.INVISIBLE);
            }
        }

        if (approx_far_pass != null) {
            approx_far_pass.setText("Est.- " + SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));

            if (sf.getDroplatlng() != null && sf.getDroplat() != 0.0) {
                approx_far_pass.setVisibility(View.VISIBLE);
            } else {
                approx_far_pass.setText("");
                approx_far_pass.setVisibility(View.INVISIBLE);
            }

        }
        if (dotsProgressBar1 != null)
            dotsProgressBar1.setVisibility(View.GONE);

    }

    */
/**
     * this class is used to ssearch taxi if available
     *//*


    public class SearchTaxi implements APIResult {
        String msg = "";
        String url = "";
        String data = "";

        public SearchTaxi(final String url, JSONObject data) {
            new APIService_Retrofit_JSON(getActivity(), this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + url).execute();
            // new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
            this.url = url;
            this.data = data.toString();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {

            if (isSuccess) {
                try {

                    final JSONObject json = new JSONObject(result);
                    //  TaxiUtil.dropmakanicode = "";
                    //  TaxiUtil.pickupmakanicode = "";

                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("trip_id", json.getJSONObject("detail").getString("passenger_tripid"), getActivity());
                        SessionSave.saveSession("Pass_Tripid", json.getJSONObject("detail").getString("passenger_tripid"), getActivity());
                        SessionSave.saveSession("request_time", json.getJSONObject("detail").getString("total_request_time"), getActivity());
                        SessionSave.saveSession("Credit_Card", "" + json.getJSONObject("detail").getString("credit_card_status"), getActivity());
                        //ApproxiTime.setText("");
                        // handlerServercall.removeCallbacks(runnableServercall);
                        if (Booking_type.equals("now")) {
                            //  book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                            //  book_now_r.setClickable(true);
                            //  book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                            book_later_r.setClickable(true);
                            booking_state = BOOKING_STATE.STATE_ONE;
                            sf.clearData();

                            boolean temp = false;

                            for (int i = 0; i < popular_places.size(); i++) {
                                if (popular_places.get(i).getLabel_name().contains(sf.getPickuplocTxt())) {
                                    // feature use
                                    temp = true;
                                } else {
                                    //select location db

                                }
                            }


                            final Intent i = new Intent(getActivity(), ContinousRequest.class);
                            i.putExtra("url", url);
                            i.putExtra("json", data);
                            startActivity(i);
                        } else {
                            //  book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                            //  book_now_r.setClickable(true);
                            // book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.header_header_bgcolorColor));
                            book_later_r.setClickable(true);
                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                        }

                    } else if (json.getInt("status") == -6 || json.getInt("status") == 2 || json.getInt("status") == 5 || json.getInt("status") == 6 || json.getInt("status") == 3) {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    } else if (json.getInt("status") == 4) {
                        new Dialog_Common().setmCustomDialog(getActivity(), BookTaxiNewFrag.this, NC.getResources().getString(R.string.message), json.getString("message"),
                                NC.getResources().getString(R.string.ok),
                                NC.getResources().getString(R.string.no_thanks), "4");
                    } else if (json.getInt("status") == -10) {
                        try {
                            // TODO Auto-generated method stub
                            JSONObject j = new JSONObject();
                            j.put("id", SessionSave.getSession("Id", getActivity()));
                            if (SessionSave.getSession("Logout", getActivity()).equals("")) {
                                new TaxiUtil.Logout("type=passenger_logout", getActivity(), j);
                                ((MainHomeFragmentActivity) getActivity()).fbLogout();
                            } else
                                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.bookedtaxi), "" + NC.getResources().getString(R.string.ok), "");
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        ShowToast.center(getActivity(), json.getString("message"));

                    }

                } catch (final Exception e) {
                    // book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                    // book_now_r.setClickable(true);
                    // book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                    book_later_r.setClickable(true);
                    Toast.makeText(getContext(), getString(R.string.server_con_error) + " " + getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), result);
                    }
                });
            }
        }
    }

    */
/**
     * For Calculating approx distance for eta and trip if user has buisness key
     *//*


    public class FindApproxDistance implements APIResult {
        int type;

        public FindApproxDistance() {
            ArrayList<LatLng> points = new ArrayList<>();
            LatLng pick = new LatLng(sf.getPickuplat(), sf.getPickuplng());
            LatLng drop = new LatLng(sf.getDroplat(), sf.getDroplng());
            points.add(pick);
            points.add(drop);
            String url = new Route().GetDistanceTime(getActivity(), points, "en", false);
            System.out.println("UUUUUUUUU" + url);
            if (url != null && !url.equals(""))
                new APIService_Retrofit_JSON(getActivity(), this, true, url).execute();
            else
                Apicall_Book_After("", "");

            type = DISTANCE_TYPE_FOR_BOOK_LATER;
            //  new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
        }

        public FindApproxDistance(Context c, double P_latitude, double P_longitude,
                                  double D_latitude, double D_longitude, int type) {
            this.type = type;
            ArrayList<LatLng> points = new ArrayList<>();
            LatLng pick = new LatLng(P_latitude, P_longitude);
            LatLng drop = new LatLng(D_latitude, D_longitude);
            points.add(pick);

            points.add(drop);
            String url = new Route().GetDistanceTime(c, points, "en", false);

            if (url != null && !url.equals(""))
                new APIService_Retrofit_JSON(c, this, null, true, url, true).execute();

            // new APIService_Retrofit_JSON(c, this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                if (getView() != null)
                    try {
                        if (new JSONObject(result).getJSONArray("routes").length() != 0) {
                            JSONObject obj = new JSONObject(result).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                            JSONObject ds = obj.getJSONObject("distance");
                            String dis = ds.getString("value");
                            JSONObject timee = obj.getJSONObject("duration");
                            String time = timee.getString("value");
                            double times = Double.parseDouble(time) / 60;
                            double dist = Double.parseDouble(dis) / 1000;

                            System.out.println("carmodel" + result + "___" + times + "___" + dist);
                            approx_diste = dist;
                            System.out.println("carmodelapprox_diste" + approx_diste);
                            //    Toast.makeText(getActivity(),"**"+String.valueOf(type) , Toast.LENGTH_SHORT).show();
                            if (type == DISTANCE_TYPE_FOR_ETA) {
                                try {
                                    if (time.equals("0"))
                                        E_time = 1.0;
                                    else
                                        E_time = Double.parseDouble(df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(times)))));
//                                    if (approx_far_pass != null)
//                                        approx_far_pass.setText(NC.getResources().getString(R.string.pick_up_aprox_text) + " " + E_time + " " + NC.getResources().getString(R.string.minutes_));

                                    if (approx_far_pass != null) {
                                        System.out.println("approxfare******&#* " + String.valueOf(approx_fare));
                                        approx_far_pass.setVisibility(View.GONE);
                                    }


                                   */
/* Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                                    pickup_marker = map.addMarker(new MarkerOptions()
                                            .position(LastKnownLatLng)
                                            .icon(BitmapDescriptorFactory.fromBitmap(b)));
                                    pickup_marker.setTitle("pick_up");*//*

                                    Approxi_time("yes");
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (type == DISTANCE_TYPE_FOR_BOOK_LATER) {
                                approx_travel_time = String.valueOf(times);
                                approx_travel_dist = String.valueOf(dist);

                                if (isBookAfter)
                                    Apicall_Book_After(approx_travel_dist, approx_travel_time);
                                else {
                                    approx_fare = approxFare(getActivity(), dist, times);

                                    Log.e("SplitOn ", String.valueOf(SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)));

                                    if (!SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {


                                                if (fav_driver_available > 0 && SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)) {
                                                    new Dialog_Common().setmCustomDialog(getActivity(), BookTaxiNewFrag.this, NC.getResources().getString(R.string.message), fav_driver_message,
                                                            NC.getResources().getString(R.string.ok),
                                                            NC.getResources().getString(R.string.no_thanks), "1");
                                                } else {
                                                    bookNow();
                                                }
                                            }
                                        });
                                    } else {
//
                                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                                                ) {
                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{Manifest.permission.READ_CONTACTS},
                                                    MY_PERMISSIONS_REQUEST_CONTACTS);

                                        } else {
                                            Message message = handler.obtainMessage(0, "");
                                            message.sendToTarget();
                                        }
                                        //callSplitFare();
                                    }

                                }
                            } else if (type == DISTANCE_TYPE_FOR_FARE) {
                                //elu
                                System.out.println("_________dropdrop");
                                approx_fare = approxFare(getActivity(), dist, times);
                                System.out.println("approxfare******&# " + SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                                approx_far_pass.setText("Est.- " + SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                                estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                            }
                        } else {
                            if (isBookAfter)
                                Apicall_Book_After("0", "0");
                        }


                    } catch (JSONException e) {
                        // Apicall_Book_After("", "");
                        e.printStackTrace();
                    }


            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (sf != null)
            sf.onActivityResult(requestCode, resultCode, data);
    }

    */
/*
    * this method is used to call the api
    * *//*

    public void Apicall_Book_After(final String approx_distance, final String apprpx_time) {

        try {

            isBookAfter = false;
            final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
            mcDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mcDialog.setContentView(view);
            mcDialog.setCancelable(false);
            mcDialog.show();
            FontHelper.applyFont(getActivity(), mcDialog.findViewById(R.id.alert_id));
            final TextView title_text = (TextView) mcDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mcDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mcDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mcDialog.findViewById(R.id.button_failure);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.confirm_booking));
            button_success.setText("" + NC.getResources().getString(R.string.ok));
            button_failure.setText("" + NC.getResources().getString(R.string.cancel));
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    System.out.println("api called");
                    try {
                        mcDialog.dismiss();
                        // TODO Auto-generated method stub
//                        String P_Address = sf.getPickuplocTxt();
//                        String D_Address = sf.getDroplocTxt();
                        System.out.println("_______pic" + sf.getPickuplat());
                        if (sf.getPickuplocTxt().trim().length() == 0) {

                            CToast.ShowToast(getActivity(), "" + NC.getResources().getString(R.string.select_the_pickup_location));
                        } else {

                            JSONObject j = new JSONObject();
                            j.put("latitude", sf.getPickuplat());
                            j.put("longitude", sf.getPickuplng());
                            j.put("pickupplace", sf.getPickuplocTxt() != null ? Uri.encode(sf.getPickuplocTxt()) : "");
                            j.put("dropplace", sf.getDroplocTxt() != null ? Uri.encode(sf.getDroplocTxt()) : "");
                            j.put("drop_latitude", sf.getDroplat());
                            j.put("drop_longitude", sf.getDroplng());
                            j.put("pickup_time", PickupTimeandDate);
                            j.put("motor_model", carModel);
                            j.put("cityname", "");
                            j.put("distance_away", E_time);
                            j.put("sub_logid", "");
                            j.put("sub_logid", "");
                            j.put("friend_id2", "0");
                            j.put("friend_percentage2", "0");
                            j.put("friend_id3", "0");
                            j.put("friend_percentage3", "0");
                            j.put("friend_id4", "0");
                            j.put("friend_percentage4", "0");
                            j.put("friend_id1", SessionSave.getSession("Id", getActivity()));
                            j.put("friend_percentage1", "100");
                            j.put("payment_mode", SessionSave.getSession("paymode", getActivity()));

                            Double approx_timee = 0.0, approx_diste = 0.0;
                        */
/*    try {
                                approx_diste = Double.parseDouble(approx_distance) / 1000;
                                approx_timee = Double.parseDouble(apprpx_time) / 60;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }*//*



                            try {
                                approx_diste = Double.parseDouble(approx_distance);
                                approx_timee = Double.parseDouble(apprpx_time) / 60;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            j.put("approx_distance", approx_diste);
                            j.put("approx_duration", approx_timee);
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("request_type", "1");
                            //j.put("promo_code", ed_promocode.getText().toString());
                            j.put("promo_code", promo_code);
                            j.put("now_after", "1");
                            j.put("notes", SessionSave.getSession("notes", getActivity()));
                            j.put("passenger_app_version", MainActivity.APP_VERSION);
                         */
/*   j.put("pickup_notes", TaxiUtil.pickupnotes);
                            j.put("drop_notes", TaxiUtil.dropnotes);
                            j.put("drop_makani", TaxiUtil.dropmakanicode);
                            j.put("pickup_makani", TaxiUtil.pickupmakanicode);*//*

                            if (!SessionSave.getSession("savedposition", getActivity()).equals("")) {
                                j.put("is_guest_booking", SessionSave.getSession("is_guest_booking", getActivity()));
                                j.put("guest_name", SessionSave.getSession("guest_name", getActivity()));
                                j.put("guest_phone", SessionSave.getSession("guest_phone", getActivity()));
                            } else {
                                j.put("is_guest_booking", "0");
                                j.put("guest_name", "");
                                j.put("guest_phone", "");
                            }

                            final String url = "type=savebooking";
                            //      book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.header_header_bgcolorColor));
                            book_later_r.setClickable(false);
                            Booking_type = "after";
                            SessionSave.saveSession("SearchUrl", url, getActivity());
                            System.out.println("api called" + j.toString());
                            new SearchTaxi(url, j);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    mcDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(getActivity())) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                View view = View.inflate(getActivity(), R.layout.progress_bar, null);
                progressDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                progressDialog.setContentView(view);
                progressDialog.setCancelable(false);
                progressDialog.show();

               */
/* ImageView iv = (ImageView) progressDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(getActivity())
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);
*//*

            } else {

            }
        } catch (Exception e) {

        }

    }

    public void closeDialog() {
        try {
            if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        } catch (Exception e) {

        }
    }

    */
/**
     * for calculating eta and trip distance if user does not have buisness key
     *//*


    private class Approximate_Time extends AsyncTask<Void, Void, Void> {
        double Ddistance;
        String Smetric;
        int type;
        double time;

        Approximate_Time(double distance, String metric, int isETA) {
            Log.e("************", "Approximate_Time: show");
            showDialog();
            Ddistance = distance;
            Smetric = metric;
            this.type = isETA;
        }

        @Override
        protected Void doInBackground(final Void... params) {

            try {
                time = calculatetime(Ddistance, Smetric, type);
            } catch (final Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            closeDialog();
            Log.e("************", "Approximate_Time: close");
            super.onPostExecute(result);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("approxfare******& " + String.valueOf(approx_fare));
                    // approx_far_pass.setText(String.valueOf(approx_fare));
                    // approx_far_pass.setVisibility(View.GONE);
                }
            });
//            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity());
//            pickup_marker = map.addMarker(new MarkerOptions()
//                    .position(LastKnownLatLng)
//                    .icon(BitmapDescriptorFactory.fromBitmap(b)));
//            pickup_marker.setTitle("pick_up");

            if (booking_state == BOOKING_STATE.STATE_TWO && sf.getPickuplatlng() != null) {        //            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity());
                if (pickup_marker != null)        //            pickup_marker = map.addMarker(new MarkerOptions()
                    pickup_marker.remove();        //                    .position(LastKnownLatLng)
                //   Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());        //                    .icon(BitmapDescriptorFactory.fromBitmap(b)));

                // add custom marker elumalai
                */
/*pickup_marker = map.addMarker(new MarkerOptions()        //            pickup_marker.setTitle("pick_up");
                        .position(sf.getPickuplatlng())
                        .icon(BitmapDescriptorFactory.fromBitmap(b)));
                     pickup_marker.setTitle("pick_up");*//*


             */
/*   map.addMarker(new MarkerOptions()
                        .position(new LatLng(pickuplat, pickuplatlng)).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.current)).draggable(false));*//*

            }
            if (type == DISTANCE_TYPE_FOR_FARE) {
                approx_fare = approxFare(getActivity(), Ddistance, time);

                System.out.println("approxfare******" + "___" + Ddistance + "__**" + time + SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                approx_far_pass.setText("Est.- " + SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
            }
           */
/* else if(type==DISTANCE_TYPE_FOR_ETA){
                    approx_fare = approxFare(getActivity(), Ddistance, time);
                    approx_far_pass.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                    estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
            }*//*

            Approxi_time("yes");
        }
    }

    */
/*
    * this method is used to calculate time trip time
    * *//*

    public double calculatetime(double distance, String metric, int type) {
        double distance_m = 0.0;

        try {
            double dist = distance;
//            if (SessionSave.getSession("Metric", getActivity()).trim().equals("MILES")) {
//                dist = dist / 1.60934;
//            }
            double timez;
            timez = dist / Double.parseDouble(speed);
            timez = timez * 3600; // time duration in seconds
            double minutes = Math.floor(timez / 60);
            timez -= minutes * 60;
            double seconds1 = Math.floor(timez);
            String timeString = (int) minutes + "." + (int) seconds1;
            // System.out.println("dddnagaaaaa__" + timeString + "__" + dist + "__" + speed);


            float minsfloatValue = Float.parseFloat(timeString);

            distance_m = Math.round(minsfloatValue * 100.0) / 100.0;

            //NSLOG(@"dis_cal---> %ld",(long)distance_m);
            if (distance_m <= 1) {
                distance_m = 1;
            }
//            Toast.makeText(getActivity(),String.valueOf(type) , Toast.LENGTH_SHORT).show();
            if (type == DISTANCE_TYPE_FOR_ETA) {
                E_time = distance_m;
                approx_travel_dist = String.valueOf(distance);
                // approx_fare = approxFare(getActivity(), Double.parseDouble(!approx_travel_dist.equals("")?approx_travel_dist:"0.0"), distance_m);
                //   approx_far_pass.setText(NC.getResources().getString(R.string.pick_up_aprox_text) + " " + E_time + " " + NC.getResources().getString(R.string.minutes_));
                System.out.println("carmodel_r" + E_time);
            } else if (type == DISTANCE_TYPE_FOR_BOOK_LATER) {
                approx_travel_time = String.valueOf(distance_m);
                approx_travel_dist = String.valueOf(distance);

                if (isBookAfter)
                    // Log.e("SplitOn ", String.valueOf(SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)));
                    Apicall_Book_After(approx_travel_dist, approx_travel_time);

                else {
                    approx_fare = approxFare(getActivity(), Double.parseDouble(approx_travel_dist), distance_m);

                    Log.e("SplitOn ", String.valueOf(SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)));

                    if (!SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)) {
                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {


                                                            if (fav_driver_available > 0 && SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)) {
                                                                // if (sf.getDroplocTxt() != null && sf.getDroplocTxt().toString().trim().length() > 0) {
                                                                isBookAfter = false;
                                                                new Dialog_Common().setmCustomDialog(getActivity(), BookTaxiNewFrag.this, NC.getResources().getString(R.string.message), fav_driver_message,
                                                                        NC.getResources().getString(R.string.ok),
                                                                        NC.getResources().getString(R.string.no_thanks), "1");
//                                                                } else {
//                                                                    Toast.makeText(getActivity(), "Please enter drop location", Toast.LENGTH_SHORT).show();
//                                                                }
                                                            } else {
                                                                bookNow();
                                                            }
                                                        }
                                                    }

                        );
                    } else {
//                        FragmentManager fm = getChildFragmentManager();
//                        SplitFareDialog splitFareDialog = new SplitFareDialog();
//                        splitFareDialog.show(fm, "fragment_edit_name");
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                                ) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    MY_PERMISSIONS_REQUEST_CONTACTS);

                        } else {
                            Message message = handler.obtainMessage(0, "");
                            message.sendToTarget();
                        }
                        // callSplitFare();
                    }
                }
            } else if (type == DISTANCE_TYPE_FOR_FARE) {
                // approx_fare = approxFare(getActivity(), distance, distance_m);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (metric) {
            case "k":
                if (distance != 0) {

                }
                break;
            case "m":
                if (distance != 0) {
//
                }
            default:
                break;
        }
        return distance_m;
    }


//public  void callSplitFare(){


//}


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("savedInstanceState", "savedInstanceState");
    }
    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CONTACTS: {
//                // If request is cancelled, the result arrays are empty.
//                System.out.println("___"+grantResults.length+"____"+grantResults[0]);
//                if (grantResults.length > 0
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    // getGPS();
//                    // startActivity(new Intent(SplashAct.this,SplashAct.class));
//                  callSplitFare();
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                //  return;
//            }
//            break;
//        }
//    }


    */
/**
     * Dilog pops up which store promo code and verify while save_booking api is called
     *//*


    public void promocode() {

        // TODO Auto-generated method stub
        final View view = View.inflate(getActivity(), R.layout.forgot_popup, null);
        final Dialog mDialog = new Dialog(getActivity(), R.style.NewDialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.inner_content), getActivity());
        FontHelper.applyFont(getActivity(), view);
        final TextView t = (TextView) mDialog.findViewById(R.id.f_textview);
        final Spinner ftmobilecodespn = (Spinner) mDialog.findViewById(R.id.ftmobilecodespn);
        t.setText(NC.getResources().getString(R.string.userphno));
        final EditText mail = (EditText) mDialog.findViewById(R.id.forgotmail);
        mail.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mail.setHint(NC.getResources().getString(R.string.enter_promo_code));
        mail.setText(promo_code);
        final TextView OK = (TextView) mDialog.findViewById(R.id.okbtn);
        OK.setText(NC.getResources().getString(R.string.apply));
        final TextView Cancel = (TextView) mDialog.findViewById(R.id.cancelbtn);
        Cancel.setVisibility(View.VISIBLE);


        Point pointSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(pointSize);
        Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    mail.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                mDialog.dismiss();
            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            private String Phone;

            @Override
            public void onClick(final View arg0) {
                // TODO Auto-generated method stub
                try {
                    Phone = mail.getText().toString();
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        mail.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                    if (Phone.trim().equals(""))
                        Toast.makeText(getActivity(), NC.getResources().getString(R.string.promo_code_empty), Toast.LENGTH_SHORT).show();
                    promo_code = Phone;
                    mDialog.dismiss();


                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStop() {
        //To prevent window leakage error close all dialogs before activity stops.
//if(splitFareDialog!=null)
//    if(splitFareDialog.isVisible())
//    splitFareDialog.dismiss();
        ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);
        if (alertmDialog != null)
            if (alertmDialog.isShowing())
                alertmDialog.dismiss();
        if (dt_mDialog != null)
            if (dt_mDialog.isShowing())
                dt_mDialog.dismiss();
        if (Dialog_Common.mCustomDialog != null)
            if (Dialog_Common.mCustomDialog.isShowing())
                try {
                    Dialog_Common.mCustomDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        super.onStop();
    }

    public void onBackPress() {

        */
/*if (booking_state == BOOKING_STATE.STATE_ONE || booking_state == BOOKING_STATE.STATE_TWO) {
            if (doubleBackToExitPressedOnce) {
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // getActivity().finish();
                return;
            } else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(getActivity(), NC.getString(R.string.pressBack), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else*//*



        if (booking_state == BOOKING_STATE.STATE_TWO) {
            System.out.println("_________ status Two called");
            if (confirm_ride_lay.isShown()) {
                select_car_lay.setVisibility(View.VISIBLE);
                confirm_ride_lay.setVisibility(View.GONE);
                mov_cur_loc_first.setVisibility(View.VISIBLE);
                //  mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                booking_state = BOOKING_STATE.STATE_TWO;
                clearsetPickDropMarker();
                sf.lastknownlocation();
            }
        } else if (booking_state == BOOKING_STATE.STATE_THREE) {
            System.out.println("_________ status three called");
            sf.searchlayvisible();
            booking_state = BOOKING_STATE.STATE_TWO;
            SessionSave.saveSession("remove_marker_clear", "0", getActivity());
            SessionSave.saveSession("notmovemarkers", "1", getActivity());
            //confirm_ride_lay.setMargins(30, 20, 30, 0);
            sf.lastknownlocation();
            clearsetPickDropMarker();
        }
    }

    private void closePopup() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
//                Animation out = AnimationUtils.makeOutAnimation(getActivity(), true);
//                instruction_header.startAnimation(out);
//                instruction_header.setVisibility(View.GONE);

                instruction_header.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        instruction_header.setAlpha(1);
                        instruction_header.setVisibility(View.GONE);
                    }
                });

                //instruction_header.setVisibility(View.GONE);
            }
        }, 1000);
    }

    public static Location GetDestinationPoint(Location startLoc, float bearing, float depth) {
        Location newLocation = new Location("newLocation");

        double radius = 6371.0; // earth's mean radius in km
        double lat1 = Math.toRadians(startLoc.getLatitude());
        double lng1 = Math.toRadians(startLoc.getLongitude());
        double brng = Math.toRadians(bearing);
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(depth / radius) + Math.cos(lat1) * Math.sin(depth / radius) * Math.cos(brng));
        double lng2 = lng1 + Math.atan2(Math.sin(brng) * Math.sin(depth / radius) * Math.cos(lat1), Math.cos(depth / radius) - Math.sin(lat1) * Math.sin(lat2));
        lng2 = (lng2 + Math.PI) % (2 * Math.PI) - Math.PI;

        // normalize to -180...+180
        if (lat2 == 0 || lng2 == 0) {
            newLocation.setLatitude(0.0);
            newLocation.setLongitude(0.0);
        } else {
            newLocation.setLatitude(Math.toDegrees(lat2));
            newLocation.setLongitude(Math.toDegrees(lng2));
        }

        return newLocation;
    }


    public static void dropLocationPin(Double pickuplat, Double pickuplatlng, Double droplat, Double droplatlng) {

        if (map != null) {
            map.clear();

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(droplat, droplatlng)).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.droplocation)).draggable(false));

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(pickuplat, pickuplatlng)).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)).draggable(false));

        }

    }


    public static void droploactiontopickup(Double pickuplat, Double pickuplatlng) {
        if (map != null) {
            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(pickuplat, pickuplatlng)).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)).draggable(false));

            if (D_latitude != 0.0 && D_longitude != 0.0) {

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(D_latitude, D_longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.droplocation)));
            }


        }
    }



  */
/*  private void showdialog_savedlocation(View view) {
        alert_savedlocation = NiftyDialogBuilder.getInstance(getActivity());
        alert_savedlocation.withTitle(null)
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFFFFFFF")
                .withIcon(null)
                .withDuration(700)
                .withEffect(Effectstype.SlideBottom)
                .isCancelableOnTouchOutside(false)
                .setCustomView(R.layout.alert_xml, view.getContext());
        FontHelper.applyFont(getActivity(), view);
        TextView input_txt = (TextView) alert_savedlocation.findViewById(R.id.input_txt);
        input_txt.setText("Do you want to Saved this Location");

        Button done = (Button) alert_savedlocation.findViewById(R.id.done);
        Button cancel = (Button) alert_savedlocation.findViewById(R.id.cancel);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categorydiaog(view);
                alert_savedlocation.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert_savedlocation.dismiss();
                if (availablecarcount > 0) {
                    sf.searchlayGone();
                    SessionSave.saveSession("notmovemarkers", "1", getActivity());
                    booking_state = BOOKING_STATE.STATE_THREE;
                    clearsetPickDropMarker();
                    // insert to saved booking to local

                } else {
                    sf.searchlayvisible();
                    ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                }
            }
        });


        alert_savedlocation.show();


    }*//*


   */
/* private void categorydiaog(View view) {
        categorydiaog_ = NiftyDialogBuilder.getInstance(getActivity());
        categorydiaog_.withTitle(null)
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFFFFFFF")
                .withIcon(null)
                .withDuration(700)
                .withEffect(Effectstype.SlideBottom)
                .isCancelableOnTouchOutside(false)
                .setCustomView(R.layout.category_file, getContext());
        FontHelper.applyFont(getActivity(), view);
        TextView currentlocation = (TextView) categorydiaog_.findViewById(R.id.pickuplocTxt);
        final EditText category_txt = (EditText) categorydiaog_.findViewById(R.id.category_txt);
        currentlocation.setText(sf.getPickuplocTxt());
        Button submit = (Button) categorydiaog_.findViewById(R.id.submitBtn);
        ImageView close_notes = (ImageView) categorydiaog_.findViewById(R.id.close_notes);
        close_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorydiaog_.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                categorydiaog_.dismiss();
                if (!category_txt.getText().toString().equals("")) {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                        j.put("p_favourite_place", sf.getPickuplocTxt());
                        j.put("p_fav_latitude", sf.getPickuplat());
                        j.put("p_fav_longtitute", sf.getPickuplng());
                        j.put("d_favourite_place", sf.getDroplocTxt());
                        j.put("d_fav_latitude", sf.getDroplat());
                        j.put("d_fav_longtitute", sf.getDroplng());
                        j.put("fav_comments", "");
                        j.put("notes", "");
                        j.put("p_fav_locationtype", category_txt.getText().toString());
                        String url = "type=add_favourite";
                        new FavPlace(url, j);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        categorydiaog_.show();


    }*//*


   */
/* private class FavPlace implements APIResult {
        private String Message;

        public FavPlace(String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    Message = json.getString("message");
                    CToast.ShowToast(getActivity(), Message);
                    if (json.getInt("status") == 1) {
                        //alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                        Toast.makeText(getActivity(), "Location saved successfully", Toast.LENGTH_SHORT);
                        if (availablecarcount > 0) {
                            sf.searchlayGone();
                            SessionSave.saveSession("notmovemarkers", "1", getActivity());
                            booking_state = BOOKING_STATE.STATE_THREE;
                            clearsetPickDropMarker();
                            // insert to saved booking to local

                        } else {
                            sf.searchlayvisible();
                            ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                        }
                    } else if (json.getInt("status") == 2) {
                        // already exitis location
                        if (availablecarcount > 0) {
                            sf.searchlayGone();
                            SessionSave.saveSession("notmovemarkers", "1", getActivity());
                            booking_state = BOOKING_STATE.STATE_THREE;
                            clearsetPickDropMarker();
                            // insert to saved booking to local

                        } else {
                            sf.searchlayvisible();
                            ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                        }

                    } else if (json.getInt("status") == 3) {
                        // category exites
                        if (availablecarcount > 0) {
                            sf.searchlayGone();
                            SessionSave.saveSession("notmovemarkers", "1", getActivity());
                            booking_state = BOOKING_STATE.STATE_THREE;
                            clearsetPickDropMarker();
                            // insert to saved booking to local

                        } else {
                            sf.searchlayvisible();
                            ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                        }

                    } else {
                        //  alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), result);
                    }
                });
            }
        }
    }*//*


    public void errorInConnection(String message) {
        try {
            if (true) {
                if (errorDialog != null && errorDialog.isShowing())
                    errorDialog.dismiss();
                System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
                errorDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                errorDialog.setContentView(view);
                errorDialog.setCancelable(false);
                errorDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(getActivity(), errorDialog.findViewById(R.id.alert_id));
                errorDialog.show();
                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
                title_text.setText("" + NC.getResources().getString(R.string.message));
                message_text.setText("" + message);
                button_success.setText("" + NC.getResources().getString(R.string.try_again));
                button_failure.setText("" + NC.getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        if (!SessionSave.getSession("Id", getActivity()).equals("")) {
                            System.out.println("tryAgain1");
                            if (NetworkStatus.isOnline(getActivity())) {
                                Intent intent = new Intent(getActivity(), MainHomeFragmentActivity.class);
                                getActivity().finish();
                                startActivity(intent);
                            } else {
                                System.out.println("tryAgain3");
                                Toast.makeText(getActivity(), NC.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                            }
                        }


                        errorDialog.dismiss();

                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub


                        Activity activity = getActivity();

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
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), getActivity().getClass());
                        getActivity().startActivity(intent);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


   */
/* *//*
*/
/**
     * Show latitude and longitude text in this Function
     **//*
*/
/*
    public void errorInGettingAddress(String message, final Double latitude, final Double longitude) {
        try {
            if (true) {
                if (errorAddressDialog != null && errorAddressDialog.isShowing())
                    errorAddressDialog.dismiss();
                System.out.println("setCanceledOnTouchOutside" + message);
                final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
                errorAddressDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                errorAddressDialog.setContentView(view);
                errorAddressDialog.setCancelable(false);
                errorAddressDialog.setCanceledOnTouchOutside(false);
                FontHelper.applyFont(getActivity(), errorAddressDialog.findViewById(R.id.alert_id));
                errorAddressDialog.show();
                final TextView title_text = (TextView) errorAddressDialog.findViewById(R.id.title_text);
                final TextView message_text = (TextView) errorAddressDialog.findViewById(R.id.message_text);
                final Button button_success = (Button) errorAddressDialog.findViewById(R.id.button_success);
                final Button button_failure = (Button) errorAddressDialog.findViewById(R.id.button_failure);
                title_text.setText("" + NC.getResources().getString(R.string.message));
                message_text.setText("" + message + " \n" + "Latitude : " + latitude + " Longitude : " + longitude + " ");
                button_success.setText("" + "Ok");
                button_failure.setText("" + NC.getResources().getString(R.string.cancel));
                button_success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub
                        if (!SessionSave.getSession("Id", getActivity()).equals("")) {
                            System.out.println("tryAgain1");
                            if (NetworkStatus.isOnline(getActivity())) {
                                try {
//                                    GEOCODE_EXPIRY = false;
                                    address = new Address_s(getActivity(), new LatLng(LastKnownLat, LastKnownLng));
                                    address.execute().get();
//                                    new convertLatLngtoAddressApi(getActivity(), latitude, longitude);
                                *//*
*/
/*} catch (InterruptedException e) {
                                    errorInConnection("Not able to get address due to status 2299");
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    errorInConnection("Not able to get address due to status 2302");
                                    e.printStackTrace();*//*
*/
/*
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("tryAgain3");
                                Toast.makeText(getActivity(), NC.getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (errorAddressDialog != null && errorAddressDialog.isShowing())
                            errorAddressDialog.dismiss();

                    }
                });
                button_failure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub


                        Activity activity = getActivity();

                        final Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                        errorAddressDialog.dismiss();

                    }
                });
            } else {
                try {
                    errorAddressDialog.dismiss();
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), getActivity().getClass());
                        getActivity().startActivity(intent);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }*//*


}*/
