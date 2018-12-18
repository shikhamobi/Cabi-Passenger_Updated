package com.cabipassenger.fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cabipassenger.ContinousRequest;
import com.cabipassenger.MainActivity;
import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.data.DriverData;
import com.cabipassenger.data.MapWrapperLayout;
import com.cabipassenger.data.apiData.PlacesDetail;
import com.cabipassenger.features.CToast;
import com.cabipassenger.features.Validation;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.DialogInterface;
import com.cabipassenger.interfaces.FragPopFront;
import com.cabipassenger.interfaces.PickupDropSet;
import com.cabipassenger.route.Route;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.service.GetPassengerUpdate;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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


/**
 * Created by developer on 4/16/16.
 * Class for home page fragment
 * Contains all functionalites for booktaxi,ride later,fare estimate etc.
 */


/**
 * Created by developer on 4/16/16.
 * Class for home page fragment
 * Contains all functionalites for booktaxi,ride later,fare estimate etc.
 */
public class BookTaxiNewFrag extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnCameraChangeListener
        , DialogInterface, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, FragPopFront, PickupDropSet {


    public static String CURRENT_COUNTRY_CODE = "SAU";
    private static boolean FREE_TO_MOVE = true;
    private String selected_carmodel_name;
    private Point display_size;
    private Dialog progressDialog;
    private Dialog errorDialog;
    private Dialog errorAddressDialog;
    private CountDownTimer countDownTimer;
    private int next_higher_model;
    private String pass_confirm_mess = "";
    private String travel_model_id = "";
    private String bookingLocation = "";
    public static boolean Route_anim = false;
    private int discount, discount_percentage;
    private String promocode_type = "";


    public enum BOOKING_STATE {STATE_ONE, STATE_TWO, STATE_THREE}

    private static final int MY_PERMISSIONS_REQUEST_CONTACTS = 222;
    private final static String BOOK_NOW_ANIM = "BookNowA", MAP_SHOW = "map_showA", CANCEL_ANIM = "cancelA";

    public static double E_time = 0;
    public static BOOKING_STATE booking_state = BOOKING_STATE.STATE_ONE;
    public static int z;
    public static String selectModel = "1";
    public static String speed = "45";
    private static LinearLayout CurrentCarModel;
    public static GoogleMap map;
    public static SearchFragment sf;

    private String carModel = "1";
    private LinearLayout confirm_request;
    private boolean doubleBackToExitPressedOnce;
    private LatLng dragLatLng;
    private LinearLayout fav_bot_lay;
    private String current_address;
    private int displayWidth;
    private int displayHeight;
    private LinearLayout id_all, id_alls;
    private RelativeLayout main;
    private TextView Title;
    private SupportMapFragment mapFragments;
    private ImageView mov_cur_loc_first;
    private TextView availablecars, model_avail;
    private Handler handlerServercall1;
    private Marker dropmap;
    private int availablecarcount;
    private TextView request_taxi, book_later_r, book_later_b;
    private int fav_driver_available;
    private int book_fav_Driver;
    private String fav_driver_message;
    private static float Zoom = 16f;
    private CoordinatorLayout carlayout;
    private LinearLayout book_now_bottom;
    private TextView pickup_approx_fare;
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
    private Double approx_fare = 0.0, approx_fare_temp = 0.0;
    private LinearLayout fare_estimate_lay, promo_code_lay, cash_card_lay;
    private TextView fare_estimate;
    private int car_type;
    private int selected_carmodel = 0;
    private double approx_diste, approx_timee;
    private Dialog mcDialog;
    private boolean isBookAfter, isBookLater;
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
    private static LatLng LastKnownLatLng;
    private TextView paymenttype;
    private int int_paymenttype = 0;
    private boolean bol_isFromBooknow = false;
    private ImageView mov_cur_loc;
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
    private ImageView mapppin_common, mapppin_drop;
    //cancel_fare_detail;
    private ImageView txt_model_name_confirm;
    private TextView txt_model_confirm;
    private TextView estimated_time_c;
    private TextView estimated_fare_c;
    private TextView capacity, request_taxi_c/*, esti_fare_c*/;
    private TextView done_c, pickloc_confirm;
    private TextView instruction_header;
    private ArrayList<PlacesDetail> popular_places = new ArrayList<>();
    private TextView fare_frag;
    private LinearLayout carlay_new;


    // public static double D_latitude;
    // public static double D_longitude;

    public static boolean pick_flag = true, drop_flag = false;

    /**
     * Initialize view and setup the neccessary components
     */
    public void priorChanges(final View v) {
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
        mapppin_common = (ImageView) v.findViewById(R.id.mapppin_common);
        mapppin_drop = (ImageView) v.findViewById(R.id.mapppin_drop);

        View bottomSheet = v.findViewById(R.id.design_bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    select_car_lay.setVisibility(View.GONE);
                    confirm_ride_lay.setVisibility(View.VISIBLE);
                    select_car_lay.setAlpha(1);
                    confirm_ride_lay.setAlpha(1);
                    confirm_ride_lay.setScaleY((1));
                    if ((int) E_time != 0)
                        estimated_time_c.setText(String.valueOf((int) E_time) + " " + NC.getString(R.string.mins));
                    else
                        estimated_time_c.setText("-");
                    capacity.setText("1-" + min_ppl);
                    //  mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    System.out.println("_____________Collll");
                    select_car_lay.setVisibility(View.VISIBLE);
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
        Title = (TextView) v.findViewById(R.id.header_titleTxt);
        Title.setText(NC.getResources().getString(R.string.taxiname));
        id_all = (LinearLayout) v.findViewById(R.id.id_all);
        id_alls = (LinearLayout) v.findViewById(R.id.id_alls);
        instruction_header = (TextView) v.findViewById(R.id.instruction_header);
        carScroll = (HorizontalScrollView) v.findViewById(R.id.carScroll);
        fare_estimate = (TextView) v.findViewById(R.id.fare_estimate);
        fare_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fare_estimate_lay.performClick();
            }
        });
        promo_code_lay = (LinearLayout) v.findViewById(R.id.promo_code_lay);
        cash_card_lay = (LinearLayout) v.findViewById(R.id.cash_card_lay);

        fare_minimum_ppl = (TextView) v.findViewById(R.id.fare_minimum_ppl);
        skip_fav = (TextView) v.findViewById(R.id.skip_fav);
        skip_fav_lay = (LinearLayout) v.findViewById(R.id.skip_fav_lay);
        navi_icon_book = (ImageView) v.findViewById(R.id.navi_icon_book);
        main = (RelativeLayout) v.findViewById(R.id.booktaxilay_home);
        pickloc_confirm = (TextView) v.findViewById(R.id.pickloc_confirm);
        FontHelper.applyFont(getActivity(), main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    Initialize(v);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 500);
       /* bottomSheet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // BookTaxiNewFrag.pick_flag = false;
               // BookTaxiNewFrag.drop_flag = false;
             //   SearchFragment.SET_FOR_PICKUP = true;
                sf.dummy_edittext.setFocusable(true);
                sf.dummy_edittext.requestFocus();
                sf.currentlocETxt.setCursorVisible(false);
                sf.drop_location_edittext.setCursorVisible(false);
                return false;
            }

        });*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("onActivityCreated", "onActivityCreated: ");
        setcarModel();

    }

    @Override
    public void onStart() {
        super.onStart();

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
                    try {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }

                        splitFareDialog = new SplitFareDialog();
                        splitFareDialog.show(ft, "dialog");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };

            navi_icon_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (booking_state == BOOKING_STATE.STATE_ONE || booking_state == BOOKING_STATE.STATE_TWO)
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

    /**
     * Check for any bundle message from Activity initialize view and its click listener
     */
    public void Initialize(View v) {


        try {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
            alert_bundle = this.getArguments();
            if (alert_bundle != null) {
                alert_msg = alert_bundle.getString("alert_message");
                book_again_msg = alert_bundle.getBoolean("book_again");
            }
            System.out.println("mainnnnn14__" + alert_msg);
            if (alert_msg != null && alert_msg.length() != 0) {
                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + alert_msg, "" + NC.getResources().getString(R.string.ok), "");
                getActivity().getIntent().putExtras(new Bundle());

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        callAddress_drag = new Runnable() {
            @Override
            public void run() {
                Log.e("Locationaa", "onCameraChange: ca" + z);
                if (z == 1) {

                    if (NetworkStatus.isOnline(getActivity())) {
                        try {
                            LatLng ss = null;
                            if (dragLatLng != null)
                                ss = dragLatLng;
                            else
                                ss = sf.getPickuplatlng();
                            if (address != null)
                                address.cancel(true);
                            Log.e("Location_address", "onCameraChange: ca");
                            address = new Address_s(getActivity(), new LatLng(ss.latitude, ss.longitude));
                            address.execute().get();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        FindNearestlocal();

                    } else {
                        errorInConnection(getString(R.string.check_internet_connection));
                    }
                }
            }
        };

        request_taxi_c = (TextView) v.findViewById(R.id.request_taxi_c);
        request_taxi_c.setText(NC.getString(R.string.request_taxi));

        /*esti_fare_c = (TextView) v.findViewById(R.id.esti_fare_c);*/
        mov_cur_loc = (ImageView) v.findViewById(R.id.mov_cur_loc);
        mov_cur_loc_first = (ImageView) v.findViewById(R.id.mov_cur_loc_first);


        //  Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "currentLocation.png").error(R.drawable.mapmove).into(mov_cur_loc_first);
        Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "currentLocation.png").error(R.drawable.mapmove).into(mov_cur_loc);
        Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "currentLocation.png").error(R.drawable.mapmove).into(mov_cur_loc_first);

        book_later_r = (TextView) v.findViewById(R.id.book_later_r);
        book_later_b = (TextView) v.findViewById(R.id.book_later_b);
        request_taxi = (TextView) v.findViewById(R.id.request_taxi);

        pickup_approx_fare = (TextView) v.findViewById(R.id.pickup_approx_fare);
        handlerServercall1 = new Handler(Looper.getMainLooper());
        mov_cur_loc.setOnClickListener(this);
        mov_cur_loc_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mov_cur_loc.performClick();
            }
        });
        availablecars = (TextView) v.findViewById(R.id.availablecars);

        model_avail = (TextView) v.findViewById(R.id.model_avail);
        paymenttype = (TextView) v.findViewById(R.id.cash_card);


        txt_model_name_confirm = (ImageView) v.findViewById(R.id.txt_model_name_confirm);
        txt_model_confirm = (TextView) v.findViewById(R.id.txt_model_confirm);
        estimated_time_c = (TextView) v.findViewById(R.id.estimated_time_c);
        estimated_fare_c = (TextView) v.findViewById(R.id.estimated_fare_c);
        capacity = (TextView) v.findViewById(R.id.capacity);
        done_c = (TextView) v.findViewById(R.id.done_c);

        request_taxi_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sf.getPickuplocTxt().trim().equals("") && !pickloc_confirm.getText().toString().equals(getString(R.string.fetching_address))) {
                    if (availablecarcount > 0) {

                        bol_isFromBooknow = true;
                        CurrentCarModel.performClick();
                    } else {
                        ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                    }
                } else {
                    // ShowToast.center(getActivity(), getString(R.string.fetching_address));
                    getPickupAdress();
                }

              /*  if (!sf.getPickuplocTxt().trim().equals("")) {
                    if (availablecarcount > 0) {
                        // Toast.makeText(getActivity(), "df"+next_higher_model, Toast.LENGTH_SHORT).show();
                        if (next_higher_model == 1) {
                            ConfirmBookNowHigher(pass_confirm_mess);
                        } else {
                            booking_state = BOOKING_STATE.STATE_THREE;
                            pickloc_confirm.setText(sf.getPickuplocTxt());
                            //siva 21/03/2018
                            request_taxi.setClickable(true);
                            request_taxi.setVisibility(View.VISIBLE);
                            clearsetPickDropMarker();
                        }
                    } else {
                        ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                    }
                } else {
                    ShowToast.center(getActivity(), getString(R.string.fetching_address));
                    getPickupAdress();
                }*/


            }
        });

        done_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPress();
            }
        });

       /* esti_fare_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });*/
        try {
            JSONArray jsonArray = new JSONArray(SessionSave.getSession("passenger_payment_option", getActivity()));
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* if (SessionSave.getSession("ride_later", getActivity()).equals("0")) {
            book_later_r.setVisibility(View.VISIBLE);
        } else
            book_later_r.setVisibility(View.GONE);*/

        book_later_r.setOnClickListener(this);
        book_later_b.setOnClickListener(this);
        request_taxi.setOnClickListener(this);

        promo_code_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowPromoDilaog();
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

                            //   Log.e("selec payment type ", String.valueOf(int_paymenttype));


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


        ((MainHomeFragmentActivity) getActivity()).cancel_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                ((MainHomeFragmentActivity) getActivity()).cancelbtn = false;
                bol_isFromBooknow = false;
                bottomVisGoneAnim(CANCEL_ANIM);
                mov_cur_loc.setVisibility(View.GONE);
                ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);


            }
        });

        Colorchange.ChangeColor((ViewGroup) v, getActivity());
        intializeMap();


        skip_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sf.getPickuplat() == 0) {
                    getPickupAdress();
                } else {
                    if (!sf.getPickuplocTxt().trim().equals("")) {
                        booking_state = BOOKING_STATE.STATE_TWO;
                        sf.setPickuplatlng(LastKnownLatLng);
                        clearsetPickDropMarker();
                    } else {
                        //  showDialog();
                        CToast.ShowToast(getActivity(), getString(R.string.fetching_address));
                        getPickupAdress();
                    }
                }
            }
        });


    }

    /**
     * Intialize necessary fields for map
     */

    public void intializeMap() {
        mapFragments = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragments == null)
            Toast.makeText(getActivity(), "nullll", Toast.LENGTH_SHORT).show();
        else {
            mapFragments.getMapAsync(this);

        }

    }

    /**
     * Self permission result for version 6.0 above
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

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
        if (splitFareDialog != null)
            if (splitFareDialog.isVisible())
                splitFareDialog.dismiss();
        super.onPause();
        System.out.println("passsss");
    }


    protected void startLocationUpdates() {
        try {
            if (mGoogleApiClient != null && mLocationRequest != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            else if (mGoogleApiClient != null)
                mGoogleApiClient.connect();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // prakash@abinfosoft.com

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onResume() {
        System.out.println("____OnresumecccCalled______");


        stopLocationUpdates();
        startLocationUpdates();
        if (LastKnownLatLng == null) {
            countDownTimer = new CountDownTimer(20000, 1000) {

                public void onTick(long millisUntilFinished) {
                    System.out.println("seconds remaining: " + millisUntilFinished);
                    //countDown = (int) (millisUntilFinished / 1000);
                    try {
                        if (LastKnownLatLng != null && countDownTimer != null) {
                            System.out.println("seconds remaining" + LastKnownLatLng);
                            //  mapWrapperLayout.setVisibility(View.VISIBLE);
                            countDownTimer.cancel();
                        }


                        if (LastKnownLatLng == null && getActivity() != null && (millisUntilFinished < 11000 && millisUntilFinished > 10000)) {
                            if (!(((MainHomeFragmentActivity) getActivity()).gpsAlert != null && ((MainHomeFragmentActivity) getActivity()).gpsAlert.isShowing())) {
                                //   latLongAlert("We are trying to get your location in low accuracy");
                                if (mLocationRequest.getPriority() != LocationRequest.PRIORITY_LOW_POWER) {
                                    stopLocationUpdates();
                                    mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                                    startLocationUpdates();
                                }
                            }
//                            else {
//                                sf.pickupClicked(true);
//                            }
                            Toast.makeText(getActivity(), getString(R.string.getting_gps_low), Toast.LENGTH_SHORT).show();

                        } else if ((millisUntilFinished < 2000 && millisUntilFinished > 1400)) {
                            System.out.println("seconds clicked" + millisUntilFinished);
                            if (!(((MainHomeFragmentActivity) getActivity()).gpsAlert != null && ((MainHomeFragmentActivity) getActivity()).gpsAlert.isShowing())) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (LastKnownLatLng == null && getActivity() != null)
                                            sf.pickupClicked(true);
                                    }
                                }, 2000);
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                public void onFinish() {
//                    if (LastKnownLatLng == null && getActivity() != null && getActivity().getCurrentFocus() != null) {
//                        sf.pickupClicked(true);
//                        // latLongAlert(getString(R.string.cant_getAddress));
//
//                    }
                }
            }.start();
        }


        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).toolbar.setVisibility(View.GONE);

        System.out.println("____OnresumecccCalled");
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof BookTaxiNewFrag) {
            ((MainHomeFragmentActivity) getActivity()).homePage_title();
            ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);

            ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.menu);
            ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("menu");


            ((MainHomeFragmentActivity) getActivity()).enableSlide();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CurrentCarModel != null)
                        CurrentCarModel.performClick();

                }
            }, 500);
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            display_size = new Point();
            display.getSize(display_size);

        }


    }

    /**
     * move the map camera view to current location of the user
     */

    public static void movetoCurrentloc() {
        //  LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());

        LatLng ll = new LatLng(LastKnownLatLng.latitude, LastKnownLatLng.longitude);
        if (ll != null && map != null)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, Zoom));


        // map.setOnCameraChangeListener(BookTaxiNewFrag.this);

        if (CurrentCarModel != null)
            CurrentCarModel.performClick();
    }

    /**
     * Method to car layout in bottom if more than 3 models available add cars to scrollview
     */
    private void setcarModel() {

        try {
            model_array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            if (model_array.length() <= 4) {
                id_alls.setVisibility(View.GONE);
                id_all.setVisibility(View.VISIBLE);
                for (int n = 0; n < model_array.length(); n++) {
                    int i = 0;

                    //To reverse the car model array
//                    if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
//                        i = model_array.length() - (n + 1);
//                    else
                    i = n;

                    System.out.println("___________KKKKK" + SessionSave.getSession("Lang", getActivity()) + "__" + i + "____" + model_array.getJSONObject(i).getString("model_name"));
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_all, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    final LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);

                    carlay_new = (LinearLayout) v.findViewById(R.id.lay_cir_car1);
                    carlay.setLayoutParams(params);

                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + model_array.getJSONObject(i).getString("model_name").toUpperCase());

                        Log.d("Image_url ", model_array.getJSONObject(i).getString("unfocus_image"));

                        Picasso.with(getActivity()).load(model_array.getJSONObject(i).getString("unfocus_image")).error(R.drawable.car2_unfocus).into((ImageView) v.findViewById(R.id.txt_dra_car1));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    carlay_new.setTag(i);
                    carlay_new.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (TaxiUtil.mDriverdata != null) {
                                TaxiUtil.mDriverdata.clear();
                            }

                            selected_carmodel = Integer.parseInt(v.getTag().toString());

                            SessionSave.saveSession("selected_carmodel", String.valueOf(selected_carmodel), getActivity());

                            Log.e("onclick ", String.valueOf(selected_carmodel));
                            carlay_click(v, 1);
                        }
                    });

                    id_all.addView(v);

                    Log.e("SelectedCarModel ", String.valueOf(selected_carmodel));

                    String pos = SessionSave.getSession("selected_carmodel", getActivity());

                    if (pos.isEmpty())
                        selected_carmodel = 0;
                    else
                        selected_carmodel = Integer.parseInt(pos);

                    if (i == selected_carmodel) {
                        CurrentCarModel = carlay_new;
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
                    int i = 0;
                    //To reverse the car model array
//                    if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
//                        i = model_array.length() - (n + 1);
//                    else
                    i = n;
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_alls, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);
                    carlay_new = (LinearLayout) v.findViewById(R.id.lay_cir_car1);
                    ViewGroup.LayoutParams lp = carlay.getLayoutParams();
                    if (lp instanceof ViewGroup.MarginLayoutParams) {
                        ((ViewGroup.MarginLayoutParams) lp).rightMargin = 17;
                        ((ViewGroup.MarginLayoutParams) lp).leftMargin = 17;
                    }
                    carlay_new.setTag(i);
                    carlay_new.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (TaxiUtil.mDriverdata != null) {
                                TaxiUtil.mDriverdata.clear();
                            }
                            selected_carmodel = Integer.parseInt(v.getTag().toString());
                            SessionSave.saveSession("selected_carmodel", String.valueOf(selected_carmodel), getActivity());
                            Log.e("onclick ", String.valueOf(selected_carmodel));
                            carlay_click(v, 2);

                        }
                    });

                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + model_array.getJSONObject(i).getString("model_name").toUpperCase());

                        Log.d("Image_url ", model_array.getJSONObject(i).getString("unfocus_image"));


                        Picasso.with(getActivity()).load(model_array.getJSONObject(i).getString("unfocus_image")).error(R.drawable.car2_unfocus).into((ImageView) v.findViewById(R.id.txt_dra_car1));

                    } catch (JSONException e) {

                        Log.d("Image_url ", model_array.getJSONObject(i).getString("unfocus_image"));
                        e.printStackTrace();
                    }

                    id_alls.addView(v);

                    Log.e("SelectedCarModel ", String.valueOf(selected_carmodel));

                    String pos = SessionSave.getSession("selected_carmodel", getActivity());

                    if (pos.isEmpty())
                        selected_carmodel = 0;
                    else
                        selected_carmodel = Integer.parseInt(pos);

                    if (i == selected_carmodel) {
                        CurrentCarModel = carlay_new;
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


    /**
     * Click event for car model clicked
     *
     * @param v=>model_layout clicked,type to find its parent view
     * @param type->          model type
     */
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
                        Log.e("circlefocus__", SessionSave.getSession("image_path", getActivity()) + "circle_focus.png");

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
            j.put("drop_latitude", sf.getDroplat() == null ? "" : sf.getDroplat());
            j.put("drop_longitude", sf.getDroplng() == null ? "" : sf.getDroplng());
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            j.put("promo_code", promo_code);

            j.put("skip_fav", SessionSave.getSession(TaxiUtil.isSkipFavOn, getActivity(), false) ? "2" : "1");
            System.out.println("input nearestdriver_list " + j.toString());
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

    private void NearestTaxiCheck() {
        try {
            JSONObject j = new JSONObject();
            j.put("latitude", sf.getPickuplat());
            j.put("longitude", sf.getPickuplng());
            j.put("motor_model", carModel);
            j.put("drop_latitude", sf.getDroplat() == null ? "" : sf.getDroplat());
            j.put("drop_longitude", sf.getDroplng() == null ? "" : sf.getDroplng());
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            j.put("promo_code", promo_code);

            j.put("skip_fav", SessionSave.getSession(TaxiUtil.isSkipFavOn, getActivity(), false) ? "2" : "1");
            System.out.println("input nearestdriver_list " + j.toString());
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
    }

    /**
     * Automatically called as once the map is ready to use
     */

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        mapWrapperLayout.init(map, getPixelsFromDp(getActivity(), 39 + 20), true, mBottomSheetBehavior);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle() != null && marker.getTitle().equals("pickup")) {
                    sf.pickupClicked(false);
                } else if (marker.getTitle() != null && marker.getTitle().equals("drop")) {
                    sf.dropClicked(false);
                }
                return false;
            }
        });


        if (LastKnownLatLng != null) {

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, Zoom));
            sf.setPickuplatlng(LastKnownLatLng);
            if (sf.getPickuplocTxt().trim().equals(""))
                getPickupAdress();
        }

        Log.d("onMapReady", "onMapReady");
        map.setOnCameraChangeListener(this);
        try {
            if (address != null)
                address.cancel(true);
            if (book_again_msg) {
                System.out.println("____**" + alert_bundle.getString("drop_location") + "__" + alert_bundle.getString("pickup_location"));
                if (alert_bundle.getString("drop_location") != null) {
                    sf.setDroplatlng(new LatLng(alert_bundle.getDouble("drop_latitude"), alert_bundle.getDouble("drop_longitude")));
                    sf.setDroplocTxt(alert_bundle.getString("drop_location"));
                    if (!sf.getPickuplocTxt().trim().equals(""))
                        sf.dropVisible();
                    // else
                    //     sf.dropGone(); //kavi
                }


                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(alert_bundle.getDouble("pickup_latitude"),
                        alert_bundle.getDouble("pickup_longitude")), Zoom));
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPickupAdress() {
        try {
            SearchFragment.SET_FOR_PICKUP = true;
            pick_flag = true;
            drop_flag = false;
            if (location != null) {
                LastKnownLat = location.getLatitude();
                LastKnownLng = location.getLongitude();
                try {
                    SessionSave.saveSession("PLAT", "" + LastKnownLat, getActivity());
                    SessionSave.saveSession("PLNG", "" + LastKnownLng, getActivity());
                    if (address == null || (address.getStatus() != AsyncTask.Status.PENDING || address.getStatus() != AsyncTask.Status.RUNNING)) {
                        address = new Address_s(getActivity(), new LatLng(LastKnownLat, LastKnownLng));
                        address.execute().get();
                    }
//                    SessionSave.saveSession("PLAT", "" + LastKnownLat, getActivity());
//                    SessionSave.saveSession("PLNG", "" + LastKnownLng, getActivity());
//                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//                    List<Address> addresses = geocoder.getFromLocation(LastKnownLat, LastKnownLng, 1);
//                    //                if (addresses.size() > 0)
//                    //                    System.out.println(addresses.get(0).getLocality());
//                    current_address = addresses.get(0).getAddressLine(1);
//                    sf.setPickuplocTxt(current_address);
//                    if (sf.getPickuplocTxt().trim().equals("")) {
//                        address = new Address_s(getActivity(), new LatLng(LastKnownLat, LastKnownLng));
//                        address.execute().get();
//                    }
//                    System.out.println("_____________cityName" + current_address);
                } catch (Exception e) {
                    e.printStackTrace();
                    // if (sf.getPickuplocTxt().toString().trim().equals("")) {
                    address = new Address_s(getActivity(), new LatLng(LastKnownLat, LastKnownLng));
                    address.execute().get();
                    //}
                }
                LastKnownLatLng = new LatLng(LastKnownLat, LastKnownLng);
                if (map != null)
                    sf.setPickuplatlng(LastKnownLatLng);
                //            P_latitude = LastKnownLat;
                //            P_longitude = LastKnownLng;

            } else {
                // Toast.makeText(getActivity(), "sdfsdfsd", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mov_cur_loc:
                try {
                    if (map != null && LastKnownLatLng != null & getActivity() != null)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, Zoom));
                    else
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.6665548, -71.2542562), 7.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                    if (map != null)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.6665548, -71.2542562), 7.0f));
                }
                break;

//            case R.id.book_now_r:
//
//
//                if (availablecarcount > 0) {
//                    //  pickup_approx_fare.setText(NC.getResources().getString(R.string.pick_up_aprox_text) + " " + E_time + " " + NC.getResources().getString(R.string.minutes_));
//                    bottomVisGoneAnim(BOOK_NOW_ANIM);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            ((MainHomeFragmentActivity) getActivity()).cancelbtn = true;
//                            mov_cur_loc_first.setVisibility(View.VISIBLE);
//                            ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.VISIBLE);
//                            ((MainHomeFragmentActivity) getActivity()).cancel_b.setText(NC.getString(R.string.cancel));
//                            ((MainHomeFragmentActivity) getActivity()).cancel_b.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(final View v) {
//
//                                    ((MainHomeFragmentActivity) getActivity()).cancelbtn = false;
//                                    bol_isFromBooknow = false;
//                                    bottomVisGoneAnim(CANCEL_ANIM);
//                                    mov_cur_loc_first.setVisibility(View.GONE);
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
                if (sf.getPickuplatlng() != null && sf.getPickuplocTxt().trim().length() > 0) {

                    if (sf.getDroplatlng() != null && (sf.getDroplocTxt() != null && sf.getDroplocTxt().toString().trim().length() > 0)) {
                        NearestTaxiCheck();
                        isBookLater = true;
                        //  notesPopup();

                    } else {
                        ShowToast.center(getActivity(), getString(R.string.select_the_drop_location));
                    }
                } else {
                    ShowToast.center(getActivity(), getString(R.string.select_the_pickup_location));
                }

                break;
            case R.id.book_later_b:
                if (sf.getPickuplatlng() != null && sf.getPickuplocTxt().trim().length() > 0) {
                    if (sf.getDroplatlng() != null && (sf.getDroplocTxt() != null && sf.getDroplocTxt().toString().trim().length() > 0)) {
                        // if (sf.getDroplatlng() != null) {
                        CurrentCarModel.performClick();
                        isBookLater = true;
                        //  notesPopup();

                    } else {
                        ShowToast.center(getActivity(), getString(R.string.select_the_drop_location));
                    }
                } else {
                    ShowToast.center(getActivity(), getString(R.string.select_the_pickup_location));
                }

                break;
            case R.id.request_taxi:
                if (!sf.getPickuplocTxt().trim().equals("") && !pickloc_confirm.getText().toString().equals(getString(R.string.fetching_address))) {
                    if (availablecarcount > 0) {

                        bol_isFromBooknow = true;
                        CurrentCarModel.performClick();
                    } else {
                        ShowToast.center(getActivity(), NC.getString(R.string.no_taxi));
                    }
                } else {
                    // ShowToast.center(getActivity(), getString(R.string.fetching_address));
                    getPickupAdress();
                }

                break;

        }
    }

    /**
     * Clear the data setups for the previous trip
     */
    /*private void clearPreviousData() {
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
*/

    void moveCamera() {


        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.
        if (pickup_marker != null)
            builder.include(pickup_marker.getPosition());
        if (drop != null)
            builder.include(drop.getPosition());


        LatLngBounds bounds = builder.build();

        final int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
        int adjustHeight = (height - (carlayout.getHeight()));
        System.out.println("_________________>>>" + height + "___" + adjustHeight);
/*
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
                updateCameraBearing();
            }

            FREE_TO_MOVE = false;
        }*/

    }

   /* private void updateCameraBearing() {
        if (map == null) return;
        CameraPosition camPos = CameraPosition
                .builder(
                        map.getCameraPosition() // current Camera
                )
                .bearing(200)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    *//**
     * Book later dialog popup
     *//*
    private void book_later_fun() {
        try {

            Locale locale = new Locale("EN");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config, null);
            if (dt_mDialog != null && dt_mDialog.isShowing())
                dt_mDialog.cancel();

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
//            FontHelper.overrideFonts(getActivity(), _datePicker);
//            FontHelper.overrideFonts(getActivity(), _timePicker);
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
                if (getActivity() != null)
                    ((MainHomeFragmentActivity) getActivity()).setLocale();
            }
        });
    }*/


    /**
     * Custom alert dialog used in entire project.can call from anywhere with the following
     *
     * @param title       set the title for alert dialog
     * @param message     set the message for alert dialog
     * @param success_txt set the success text in success button
     * @param failure_txt set the failure text in failure button
     */

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

    /**
     * this method is used to call book taxi api
     */

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
            String P_Address = sf.getPickuplocTxt();
            String D_Address = sf.getDroplocTxt();
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
                    j.put("approx_distance", approx_travel_dist);
                    j.put("approx_duration", approx_timee);
                    j.put("cityname", "");
                    j.put("distance_away", E_time);
                    j.put("sub_logid", "");
                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                    j.put("request_type", "1");
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
                    j.put("airport_booking", SessionSave.getSession("airport_booking", getActivity()));
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
                    j.put("travel_modelid", Integer.parseInt(travel_model_id.equals("") ? "0" : travel_model_id));
                    j.put("booked_location", bookingLocation);
                    j.put("booked_latitude", LastKnownLat);
                    j.put("booked_longitude", LastKnownLng);
                    String curVersion = "";
                    PackageManager manager = getActivity().getPackageManager();
                    try {
                        curVersion = manager.getPackageInfo(getActivity().getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    j.put("passenger_app_version", curVersion);
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


    /**
     * this method is used to set time
     *
     * @param day   set day of the month
     * @param hours set hour of the day
     * @param mins  set minutes of the hour
     * @param month set month of the year
     * @param sec   set seconds of the minute
     * @param year  set the selected year
     */

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

    /**
     * this method is used to get current date and time
     *
     * @param _datePicker this is used to pick the date
     * @param _timePicker this is used to pick time
     */

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
        System.out.println(" clearsetPickDropMarker " + booking_state);
        if (getActivity() != null) {
            if (false) {

                carlayout.setVisibility(View.GONE);
                // getPickupAdress();
                //  map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                //  map.getUiSettings().setZoomControlsEnabled(true);
                ((MainHomeFragmentActivity) getActivity()).enableSlide();
                //LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());
                if (LastKnownLatLng != null && map != null) {
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, Zoom));
                    map.setMyLocationEnabled(false);
                    if (LastKnownLatLng != null)
                        map.addMarker(new MarkerOptions()
                                .position(LastKnownLatLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));
//                if(CurrentCarModel!=null)
//                    CurrentCarModel.performClick();
                }
                navi_icon_book.setImageResource(R.drawable.menu);
                //System.out.println("stateoneLo" + sf.getPickuplocTxt().trim());
                instruction_header.setVisibility(View.GONE);
//                if (this.location == null)
//                    instruction_header.setVisibility(View.GONE);
//                else {
//                    instruction_header.setVisibility(View.VISIBLE);
//                    instruction_header.setText(getString(R.string.loading_map));
//                }
                map.getUiSettings().setMapToolbarEnabled(false);
                pickup_approx_fare.setText("");
                estimated_fare_c.setText("-");
                if (!sf.getPickuplocTxt().trim().equals(""))
                    sf.dropVisible();
                else
                    sf.dropGone();

                mapppin.setVisibility(View.GONE);
                sf.setDroplatlng(null);
                sf.setDroplocTxt("");
                approx_fare = 0.0;
                confirm_request.setVisibility(View.GONE);
                select_car_lay.setVisibility(View.GONE);
                confirm_ride_lay.setVisibility(View.GONE);
                skip_fav_lay.setVisibility(View.GONE);
                map.setOnCameraChangeListener(null);
                setfav_drop();
                mapWrapperLayout.getLayoutParams().height = displayHeight;
                if (sf.getPickuplocTxt().equals("")) {
                    try {
                        if (address != null && address.getStatus() != AsyncTask.Status.PENDING || address.getStatus() != AsyncTask.Status.RUNNING)
                            getPickupAdress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                pickloc_confirm.setText("");
            } else if (booking_state == BOOKING_STATE.STATE_TWO || booking_state == BOOKING_STATE.STATE_ONE) {

                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                // map.getUiSettings().setZoomControlsEnabled(true);
                ((MainHomeFragmentActivity) getActivity()).disableSlide();
                map.clear();
                instruction_header.setText(getString(R.string.tap_to_edit));
                instruction_header.setVisibility(View.GONE);
                //ShowToast.top(getActivity(),getString(R.string.tap_to_edit));
                mapppin.setVisibility(View.GONE);
                map.getUiSettings().setMapToolbarEnabled(false);

                if (select_car_lay.isShown()) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                if (map != null) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    map.setMyLocationEnabled(false);
                    map.setOnCameraChangeListener(BookTaxiNewFrag.this);
                }


                navi_icon_book.setImageResource(R.drawable.menu);
                carlayout.setVisibility(View.VISIBLE);
                if (!confirm_ride_lay.isShown())
                    select_car_lay.setVisibility(View.VISIBLE);
                confirm_request.setVisibility(View.GONE);
                skip_fav_lay.setVisibility(View.GONE);

                if (sf.getPickuplatlng() != null) {
                    /// sf.dropVisible();
                    if (pickup_marker != null)        //            pickup_marker = map.addMarker(new MarkerOptions()
                        pickup_marker.remove();
                    //   clearsetPickDropMarker();
                    System.out.println("settting markk1");
                    // comment today by elumalai

                    Setpickupmarker(sf.getPickuplatlng());

                    /*add by mani*/
                    for (int i = 0; i < TaxiUtil.mDriverdata.size(); i++) {

                        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(new LatLng(Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()))));
                    }

                  /*  pickup_marker = map.addMarker(new MarkerOptions()
                            .position(sf.getPickuplatlng())
                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt()))));*/
                    //    pickup_marker.setTitle("pickup");
                    //   pickup_marker.setAnchor(0.0f, 1f);

                    //rotateMarkerAccordingToPosition(pickup_marker);
                    if (sf.getDroplatlng() == null) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sf.getPickuplatlng(), Zoom));
                    }
                }


                if (sf.getDroplatlng() != null) {
                    System.out.println("_________Drop");
                    //instruction_header.setVisibility(View.VISIBLE);
                    closePopup();
                    // comment today by elumalai
                    Setdropmarker(sf.getDroplatlng());
                   /* drop = map.addMarker(new MarkerOptions()
                            .position(sf.getDroplatlng())
                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(), getActivity()))));*/
                    // sf.dropGone(); //kavi
                    // drop.setTitle("drop");
                    //  drop.setAnchor(1.0f, 1f);
                    LatLng ll = sf.getPickuplatlng() == null ? LastKnownLatLng : sf.getPickuplatlng();
                    System.out.println("_________" + sf.getPickuplocTxt() + "___" + ll.latitude);
                    if (ll != null && ll.longitude != 0.0) {

//                    pickup_marker = map.addMarker(new MarkerOptions()
//                            .position(ll)
//                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity()))));
//                    pickup_marker.setTitle("pickup");pickup_marker.setAnchor(0.0f,1f);
                        if (Route_anim) {
                            mapppin_common.setVisibility(View.GONE);
                            mapppin_drop.setVisibility(View.GONE);
                            new Route().drawRoute(map, getActivity(), ll, sf.getDroplatlng(), "en", Color.parseColor("#000000"));
                            double Driverdistance;
                            if (SessionSave.getSession("isBUISNESSKEY", getActivity(), true)) {
                                new FindApproxDistance(getActivity(), ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);
                            } else {
                                Driverdistance = 0.5 + FindDistance.distance(ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), SessionSave.getSession("Metric_type", getActivity()), location);
                                new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_FARE).execute();

                            }
                        }


                    }

                    //  int layHeight = select_car_lay.getHeight();

                    select_car_lay.post(new Runnable() {
                        @Override
                        public void run() {
                            int layHeight = select_car_lay.getHeight();
                            int requiredMapHeight = displayHeight - layHeight;
                            System.out.println("____________ss" + layHeight + "____" + displayHeight + "___" + requiredMapHeight + 20);
                            //  mapWrapperLayout.getLayoutParams().height = requiredMapHeight;
                            try {
                                moveCamera();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    // setbounds(sf.getPickuplatlng(), sf.getDroplatlng());

                } else {
                    if (!sf.getPickuplocTxt().trim().equals("")) {
                        sf.dropVisible();
                    }

                    //else
                    //  sf.dropGone(); //kavi
                }
                if (pickloc_confirm != null)
                    pickloc_confirm.setText("");
            } else if (booking_state == BOOKING_STATE.STATE_THREE) {
                map.clear();
                map.getUiSettings().setMapToolbarEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                //  map.getUiSettings().setZoomControlsEnabled(true);
                ((MainHomeFragmentActivity) getActivity()).disableSlide();
                carlayout.setVisibility(View.GONE);
                if (map != null) {
                    map.setMyLocationEnabled(false);
                }
                mapWrapperLayout.getLayoutParams().height = displayHeight;
                instruction_header.setVisibility(View.VISIBLE);
                instruction_header.setText(getString(R.string.move_map_to));
                closePopup();
                //ShowToast.top(getActivity(),getString(R.string.move_map_to));
                navi_icon_book.setImageResource(R.drawable.back);
                skip_fav_lay.setVisibility(View.GONE);

//                if (sf.getPickuplocTxt().trim().equals("")) {
//                    handlerServercall1.postDelayed(callAddress_drag, 0);
//
//                }
                LatLng ll = new LatLng(sf.getPickuplat(), sf.getPickuplng());
                if (ll != null && map != null)  // && map.getCameraPosition().zoom < 12
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 14f));
                sf.dropGone();
                confirm_request.setVisibility(View.VISIBLE);
                select_car_lay.setVisibility(View.GONE);
                confirm_ride_lay.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (map != null && booking_state == BOOKING_STATE.STATE_THREE && getActivity() != null) {
                            //map.setOnCameraChangeListener(BookTaxiNewFrag.this);
                            map.setOnCameraChangeListener(null); //kavi
                        }
                    }
                }, 1000);

                // Glide.with(this).load(R.drawable.pickup_pin).error(R.drawable.pickup_pin).into(mapppin); //kavi
                // System.out.println("____SDD____1" + sf.getPickuplocTxt());
                if (sf.getPickuplocTxt() != null) {
                    pickloc_confirm.setText(sf.getPickuplocTxt());
                    //   mapppin.setVisibility(View.VISIBLE); //kavi
                    Log.e("clear", "clearsetPickDropMarker: 1");
                    if (pickup_marker != null)        //            pickup_marker = map.addMarker(new MarkerOptions()
                        pickup_marker.remove();
                    System.out.println("settting markk123");
                    Setpickupmarker(sf.getPickuplatlng());
//                    // comment today by elumalai
//                    pickup_marker = map.addMarker(new MarkerOptions()
//                            .position(sf.getPickuplatlng())
//                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt()))));

                    //  pickup_marker.setTitle("pickup");
                    //  pickup_marker.setAnchor(0.0f, 1f);
                    if (LastKnownLatLng != null && LastKnownLng != 0.0) {
                        Log.e("clear", "clearsetPickDropMarker:2 ");
                        if (sf.getDroplatlng() != null) {
                            map.clear();
                            // comment today by elumalai
                            Setdropmarker(sf.getDroplatlng());
//                            drop = map.addMarker(new MarkerOptions()
//                                    .position(sf.getDroplatlng())
//                                    .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(), getActivity()))));
//                            // sf.dropGone(); //kavi
                            //drop.setTitle("drop");
                            //  drop.setAnchor(1.0f, 1f);
                            Log.e("clear", "clearsetPickDropMarker:3 " + dragLatLng + "__" + sf.getDroplatlng());
                            //   new Route().drawRoute(map, getActivity(), dragLatLng == null ? sf.getPickuplatlng() : dragLatLng, sf.getDroplatlng(), "en", Color.parseColor("#000000"));
                            new Route().drawRoute(map, getActivity(), ll, sf.getDroplatlng(), "en", Color.parseColor("#000000")); //kavi
//                map.addMarker(new MarkerOptions()
//                        .position(LastKnownLatLng)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin)));
                            ;
                          /*  map.addMarker(new MarkerOptions()
                                    .position(sf.getDroplatlng())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_pin)));*/ //kavi
                        }


                    }
                } else
                    errorInGettingAddress(getString(R.string.server_con_error));


            }
        }
    }

    private void rotateMarkerAccordingToPosition(Marker pickup_marker) {
        //   if()
    }

    /**
     * this method is used to check the am & pm for given input time
     *
     * @param inputHour hour is given as input
     */

    private String ampmValidation(int inputHour) {

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

    /**
     * this method is used to find the nearest driver
     */

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
                    // comment today by elumalai
/*
                    dropmap = map.addMarker(new MarkerOptions().position(new LatLng(TaxiUtil.d_lat, TaxiUtil.d_lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pick)).draggable(false));
*/
                    availablecarcount = 0;
                for (int i = 0; i < TaxiUtil.mDriverdata.size(); i++) {
                    if (i == 0) {

                        if (SessionSave.getSession("isBUISNESSKEY", getActivity(), true))
                            new FindApproxDistance(getActivity(), sf.getPickuplat(), sf.getPickuplng(), sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);

                        if (SessionSave.getSession("isBUISNESSKEY", getActivity(), true))
                            new FindApproxDistance(getActivity(), sf.getPickuplat(), sf.getPickuplng(), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()), DISTANCE_TYPE_FOR_ETA);
                        else
                            Driverdistance = 0.5 + FindDistance.distance(sf.getPickuplat(), sf.getPickuplng(), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()), SessionSave.getSession("Metric_type", getActivity()), location);

                    }
                    availablecarcount++;
                    m = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(new LatLng(Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()))));
                }
                if (availablecarcount != 0) {
                    if (!SessionSave.getSession("isBUISNESSKEY", getActivity(), true))
                        new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_ETA).execute();

                    availablecars.setText("- " + String.format(Locale.UK, String.valueOf(availablecarcount)) + " " + NC.getResources().getString(R.string.avaiable_car));
                    model_avail.setText(selected_carmodel_name);
                    dotsProgressBar1.setVisibility(View.GONE);
                } else {
                    dotsProgressBar1.setVisibility(View.GONE);
                    E_time = 0;
                    Approxi_time("no");
                    availablecars.setText("- " + NC.getResources().getString(R.string.nodrivers));
                    model_avail.setText(selected_carmodel_name);
                    if (booking_state == BOOKING_STATE.STATE_TWO || booking_state == BOOKING_STATE.STATE_ONE) {
                        System.out.println("___________notaxi" + (int) E_time);
                        if (pickup_marker != null)
                            pickup_marker.remove();
                        //if (!sf.getPickuplocTxt().trim().equals("") && booking_state != BOOKING_STATE.STATE_ONE) {
                        if (!sf.getPickuplocTxt().trim().equals("")) { //kavi
                            System.out.println("settting markk2");
                            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                            // comment today by elumalai
                            Setpickupmarker(sf.getPickuplatlng());
                            /* pickup_marker = map.addMarker(new MarkerOptions()
                                    .position(sf.getPickuplatlng())
                                    .icon(BitmapDescriptorFactory.fromBitmap(b)));*/
                            //   pickup_marker.setTitle("pickup");
                            //   pickup_marker.setAnchor(0.0f, 1f);
                        }
                    }
                }

                if (bol_isFromBooknow) {
                    if (availablecarcount > 0) {

                        bol_isFromBooknow = false;

                        if (!(sf.getPickuplocTxt().toString().trim().length() == 0 || (sf.getPickuplocTxt().toString().trim().equals(NC.getResources().getString(R.string.fetching_address))))) {
                            if (sf.getDroplocTxt() != null && sf.getDroplocTxt().toString().trim().length() > 0) {
                                isBookAfter = false;
                                if (SessionSave.getSession("isBUISNESSKEY", getActivity(), true))
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
                Approxi_time("no");
                if (SessionSave.getSession("isBUISNESSKEY", getActivity(), true))
                    new FindApproxDistance(getActivity(), sf.getPickuplat(), sf.getPickuplng(), sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);
                availablecars.setText("- " + NC.getResources().getString(R.string.nodrivers));
                model_avail.setText(selected_carmodel_name);
            }

            System.gc();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


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


    public void errorInGettingAddress(String message) {
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
                                try {
                                    address = new Address_s(getActivity(), new LatLng(LastKnownLat, LastKnownLng));
                                    address.execute().get();
                                } catch (InterruptedException e) {
                                    errorInConnection(getString(R.string.error));
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    errorInConnection(getString(R.string.error));
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

    /**
     * this method is used to calculate difference between dates
     */

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

    /**
     * this method is used to change map position
     */

    @Override
    public void onCameraChange(CameraPosition pos) {
        if (NetworkStatus.isOnline(getActivity())) {
            Log.d("onCameraChange", "onCameraChange" + pos.target.latitude + "---" + book_again_msg + "map zoom level" + map.getCameraPosition().zoom);
            System.gc();
//            if (sf != null)
//                sf.removeFav();

            try {
                //  if (book_again_msg != null) {
                Zoom = map.getCameraPosition().zoom;
                if (book_again_msg) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            book_again_msg = false;
                        }
                    }, 3000);
                } else {
                    //if ((booking_state == BOOKING_STATE.STATE_THREE || booking_state == BOOKING_STATE.STATE_TWO)&&MapWrapperLayout.moved) {
                    if (MapWrapperLayout.moved) { //kavi
                        dragLatLng = pos.target;
                        Log.e("Location", "onCameraChange: ca");
                        if (!SessionSave.getSession("notmovemarkers", getActivity()).equals("1")) {
                            request_taxi.setVisibility(View.GONE);
                            handlerServercall1.removeCallbacks(callAddress_drag);
                            handlerServercall1.postDelayed(callAddress_drag, 1000);
                            MapWrapperLayout.moved = false;
                        } else if (pick_flag || drop_flag) {
//                        pickloc_confirm.setText(sf.getPickuplocTxt());
//                        request_taxi.setVisibility(View.VISIBLE);
                            // if (pickloc_confirm.getText().toString().trim().equals("") || pickloc_confirm.getText().toString().trim().equals("getString(R.string.fetching_address)")) {
                            request_taxi.setVisibility(View.VISIBLE);
                            pickloc_confirm.setText(getString(R.string.fetching_address));
                            handlerServercall1.removeCallbacks(callAddress_drag);
                            handlerServercall1.postDelayed(callAddress_drag, 1000);
                        }
                        //}
                    }
                }
//

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }

    /**
     * this method is used to fetching address for pick up location
     */
    public static void setfetch_address() {
        //closeDialog();
        //  sf.setPickuplocTxt(FETCH_ADDRESS);

    }

//    public void getAproxDropLocation()
//
//    {
//
//        handlerServercall1.removeCallbacks(callAddress_drag);
//        handlerServercall1.postDelayed(callAddress_drag, 2000);
//    }

    /*
    * this method is used to
    * */
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


    /*
    * this method is used to get the location accuracy
    * */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (getActivity() != null) {
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(15000); // Update location every second
                System.out.println("onconnected");
                instruction_header.setVisibility(View.VISIBLE);
                instruction_header.setText(getString(R.string.loading_map));

                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                getPickupAdress();

                if (sf.getPickuplatlng() != null)
                    map.setOnCameraChangeListener(this);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

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
        Log.d("onMapReady", "locationChanged" + LastKnownLat + "__" + LastKnownLng);
        LastKnownLatLng = new LatLng(LastKnownLat, LastKnownLng);
        /*if (pick_flag)
            sf.setPickuplatlng(LastKnownLatLng);*/

       /* if (map != null && location != null && booking_state == BOOKING_STATE.STATE_ONE) {
            instruction_header.setVisibility(View.GONE);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LastKnownLatLng, Zoom));
            sf.setPickuplatlng(LastKnownLatLng);

            Log.d("onMapReady", "locationChanged" + sf.getPickuplocTxt());
            if (sf.getPickuplocTxt().trim().equals("")) {
                getPickupAdress();

            }
        }*/ //kavi
        System.out.println("address onlocation drop:" + BookTaxiNewFrag.drop_flag + "pickup:" + BookTaxiNewFrag.pick_flag);
        if (sf.currentlocETxt.getText().length() == 0) {
            if (!pick_flag)
                getPickupAdress();

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapWrapperLayout.setVisibility(View.VISIBLE);
                skip_fav.setVisibility(View.GONE);
                if (getActivity() != null && popular_places.size() == 0 && skip_fav.getText().toString().equalsIgnoreCase(getString(R.string.skip_popular))) {
                    System.out.println("Called__current");
                    if (CurrentCarModel != null)
                        CurrentCarModel.performClick();
                }

            }
        }, 1000);

        this.location = location;
        //if (sf.getPickuplat() == 0) {
        //     getPickupAdress();
        // } //kavi 4.9.17
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
        if (getActivity() != null && CurrentCarModel != null && booking_state != BOOKING_STATE.STATE_THREE)
            CurrentCarModel.performClick();
        if (fav_bot_lay != null) {
            for (int i = 0; i < fav_bot_lay.getChildCount(); i++) {
                ((TextView) fav_bot_lay.getChildAt(i).findViewById(R.id.fav_label)).setTypeface(Typeface.DEFAULT_BOLD);
            }
        }
    }

    @Override
    public void pickUpSet(double latitude, double longtitue) {
//        map.addMarker(new MarkerOptions().position(new LatLng(P_latitude, P_longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).title("" +NC.getResources().getString(R.string.droplocation)));
//        carlayout.setVisibility(View.VISIBLE);
//        skip_fav.setVisibility(View.GONE);
//        if (CurrentCarModel != null && booking_state != BOOKING_STATE.STATE_THREE)
//            CurrentCarModel.performClick();
        // System.out.println("pickupSetting kavi" + booking_state + ",");
        //   System.out.println("pickupSetting" + latitude);
        if (LastKnownLatLng == null)
            LastKnownLatLng = new LatLng(latitude, longtitue);

        if (!carlayout.isShown() && booking_state != BOOKING_STATE.STATE_THREE) {
            if (LocationSearchActivityNew.SET_FOR_PICKUP) {
                booking_state = BOOKING_STATE.STATE_TWO;
                //  clearsetPickDropMarker();
                System.out.println("touchedBook" + getActivity().getCurrentFocus());
                //  LocationSearchActivityNew.SET_FOR_PICKUP = false;


            }
        }
        if (SearchFragment.SET_FOR_PICKUP) {
            clearsetPickDropMarker(); //kavi
            SearchFragment.SET_FOR_PICKUP = false;
        }
        if (booking_state == BOOKING_STATE.STATE_THREE) {
            clearsetPickDropMarker();
            pickloc_confirm.setText(sf.getPickuplocTxt());
            request_taxi.setVisibility(View.VISIBLE);
        } else if (booking_state == BOOKING_STATE.STATE_ONE) {
            closeDialog();
            if (!sf.getPickuplocTxt().trim().equals("")) {
                sf.dropVisible();
//                if (CurrentCarModel != null)
//                    CurrentCarModel.performClick();
            } //else
            //  sf.dropGone(); //kavi

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapWrapperLayout.setVisibility(View.VISIBLE);
                skip_fav.setVisibility(View.GONE); //kavi
            }
        }, 1000);
        Setpickupmarker(LastKnownLatLng);
    }

    @Override
    public void dropSet(double latitude, double longtitue) {
        // map.addMarker(new MarkerOptions().position(new LatLng(P_latitude, P_longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).title("" +NC.getResources().getString(R.string.droplocation)));
        booking_state = BOOKING_STATE.STATE_TWO;
        //  sf.dropGone(); //kavi
        // if (!carlayout.isShown())
        clearsetPickDropMarker();
        //clearsetPickDropMarker();
//        carlayout.setVisibility(View.VISIBLE);
//        skip_fav.setVisibility(View.GONE);
//
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(latitude, longtitue))
//                .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView("0", getActivity()))));
    }

    @Override
    public void requestPickupAddress() {
        getPickupAdress();
    }

    /**
     * To Get nearest driver list for particular model_id
     */

    private class GetNearTaxi implements APIResult {
        private Marker m;

        public GetNearTaxi(final String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            SessionSave.saveSession("LastServer_latitude", "" + sf.getPickuplat(), getActivity());
            SessionSave.saveSession("LastServer_longitude", sf.getPickuplng() + "", getActivity());

            if (bol_isFromBooknow)
                new APIService_Retrofit_JSON_NoProgress(getActivity(), this, data, false).execute(url);
            else
                new APIService_Retrofit_JSON_NoProgress(getActivity(), this, data, false).execute(url);

            //  availablecars.setVisibility(View.INVISIBLE);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            availablecars.setVisibility(View.VISIBLE);
            model_avail.setVisibility(View.VISIBLE);
            if (skip_fav.getVisibility() == View.GONE)
                carlayout.setVisibility(View.VISIBLE);
            if (TaxiUtil.mDriverdata.size() != 0)
                TaxiUtil.mDriverdata.clear();
            if (m != null) {
                m.remove();
            }
            if (map != null) {
                map.clear();
            }
//            clearsetPickDropMarker();
            if (TaxiUtil.d_lat != 0) {
                if (dropmap != null)
                    dropmap.remove();
                // comment today by elumalai
/*
                dropmap = map.addMarker(new MarkerOptions().position(new LatLng(TaxiUtil.d_lat, TaxiUtil.d_lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pick)).draggable(false));
*/
            }
            E_time = 0;
            if (isSuccess) {
                String Driverid, Drivername, Lat, Lng, Nearest, distance, driver_coordinates;
                availablecarcount = 0;
                try {

                    /*if (map != null)
                        map.clear();*/
                    if (booking_state == BOOKING_STATE.STATE_TWO) {
                        if (sf.getDroplatlng() != null) {

                            //   System.out.println("_________Drop");
                            instruction_header.setVisibility(View.VISIBLE);
                            closePopup();
                            // comment today by elumalai
                            Setdropmarker(sf.getDroplatlng());
                            /*drop = map.addMarker(new MarkerOptions()
                                    .position(sf.getDroplatlng())
                                    .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(), getActivity()))));*/
                            sf.dropGone();
                            // drop.setTitle("drop");
                            //  drop.setAnchor(1.0f, 1f);
                            LatLng ll = sf.getPickuplatlng() == null ? LastKnownLatLng : sf.getPickuplatlng();
                            //    System.out.println("_________" + sf.getPickuplocTxt() + "___" + ll.latitude);
                            if (ll != null && ll.longitude != 0.0) {

//                    pickup_marker = map.addMarker(new MarkerOptions()
//                            .position(ll)
//                            .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity()))));
//                    pickup_marker.setTitle("pickup");pickup_marker.setAnchor(0.0f,1f);
                               /* new Route().drawRoute(map, getActivity(), ll, sf.getDroplatlng(), "en", Color.parseColor("#000000"));
                                double Driverdistance;
                                if (SplashActivity.isBUISNESSKEY)
                                    new FindApproxDistance(getActivity(), ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), DISTANCE_TYPE_FOR_FARE);
                                else {
                                    Driverdistance = 0.5 + FindDistance.distance(ll.latitude, ll.longitude, sf.getDroplat(), sf.getDroplng(), SessionSave.getSession("Metric_type", getActivity()), location);
                                    new Approximate_Time(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), DISTANCE_TYPE_FOR_FARE).execute();

                                }*/
                            }

                            //  int layHeight = select_car_lay.getHeight();

//                            select_car_lay.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int layHeight = select_car_lay.getHeight();
//                                    int requiredMapHeight = displayHeight - layHeight;
//                                    System.out.println("____________ss" + layHeight + "____" + displayHeight + "___" + requiredMapHeight + 20);
//                                    mapWrapperLayout.getLayoutParams().height = requiredMapHeight;
//                                    try {
//                                        moveCamera();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });

                        } else {
                            if (!sf.getPickuplocTxt().trim().equals(""))
                                sf.dropVisible();
                            else
                                sf.dropGone();
                        }
                    }


                    SessionSave.saveSession("Server_Response", result, getActivity());
                    //   System.out.println("favdriver" + result);

                    final JSONObject json = new JSONObject(result);

                    //siva 05/03/2018
                    if (json.has("promocode_status")) {
                        if (json.getString("promocode_status").equals("1")) {
                            ((MainHomeFragmentActivity) getActivity()).promoVisibility("1");
                        } else if (json.getString("promocode_status").equals("0")) {
                            ((MainHomeFragmentActivity) getActivity()).promoVisibility("0");
                        }
                    }
                    /*discount, discount_percentage;
                     promocode_type = ""*/
                    if (json.has("promo_type")) {
                        System.out.println("type promo" + "" + json.getString("promo_type"));
                        promocode_type = json.getString("promo_type");
                        if (json.getString("promo_type").equals("1")) {
                            discount = Integer.parseInt(json.getString("promo_discount"));
                            System.out.println("type promo..." + "" + discount);
                        } else {
                            discount_percentage = Integer.parseInt(json.getString("promo_discount"));
                            System.out.println("type promo..per" + "" + discount_percentage);
                        }

                    }

                    //  speed = json.getJSONObject("fare_details").getString("taxi_speed");
                    // System.out.println("_dddnagaaaaa" + speed);
                    popular_places.clear();
                    if (json.getJSONArray("favourite_places").length() > 0 && !SessionSave.getSession(TaxiUtil.isSkipFavOn, getActivity(), false)) {
                        // SessionSave.saveSession("popular_places", json.getJSONArray("favourite_places").toString(), getActivity());
                        for (int j = 0; j < json.getJSONArray("favourite_places").length(); j++) {
                            int i = 0;

                            //To reverse the array
                            if (SessionSave.getSession("Lang", getActivity()).equals("ar") || SessionSave.getSession("Lang", getActivity()).equals("fa"))
                                i = json.getJSONArray("favourite_places").length() - (j + 1);
                            else
                                i = j;

                            JSONObject jo = json.getJSONArray("favourite_places").getJSONObject(i);
                            Double latc = jo.getDouble("latitude");
                            Double lngc = jo.getDouble("longtitute");
                            float[] dist = new float[1];
                            new Location("").distanceBetween(LastKnownLat, LastKnownLng, latc, lngc, dist);
                            // System.out.println("___________dist" + dist[0]);
                            if (dist[0] > 1000) {
                                PlacesDetail o = new PlacesDetail();
                                o.setLabel_name(TaxiUtil.firstLetterCaps(jo.getString("label_name")));
                                o.setLatitude(jo.getDouble("latitude"));
                                o.setLongtitute(jo.getDouble("longtitute"));
                                o.setLocation_name(jo.getString("location_name"));
                                o.setAndroid_image_unfocus(jo.getString("android_icon"));
                                popular_places.add(o);
                            }
                            skip_fav.setText(NC.getString(R.string.skip_favourite));
                        }
                    } else if (json.getJSONArray("popular_places").length() > 0) {
                        for (int j = 0; j < json.getJSONArray("popular_places").length(); j++) {
                            int i = 0;
                            //To reverse the array
                            if (SessionSave.getSession("Lang", getActivity()).equals("ar") || SessionSave.getSession("Lang", getActivity()).equals("fa"))
                                i = json.getJSONArray("popular_places").length() - (j + 1);
                            else
                                i = j;
                            JSONObject jo = json.getJSONArray("popular_places").getJSONObject(i);
                            Double latc = jo.getDouble("latitude");
                            Double lngc = jo.getDouble("longtitute");
                            float[] dist = new float[1];
                            new Location("").distanceBetween(LastKnownLat, LastKnownLng, latc, lngc, dist);
                            //    System.out.println("___________dist" + dist[0]);
                            if (dist[0] > 1000) {
                                PlacesDetail o = new PlacesDetail();
                                o.setLabel_name(TaxiUtil.firstLetterCaps(jo.getString("label_name")));
                                o.setLatitude(jo.getDouble("latitude"));
                                o.setLongtitute(jo.getDouble("longtitute"));
                                o.setLocation_name(jo.getString("location_name"));
                                o.setAndroid_image_unfocus((jo.getString("android_icon")));
                                popular_places.add(o);
                            }
                            skip_fav.setText(NC.getString(R.string.skip_popular));
                        }
                    }

                    if (popular_places.size() == 0) {
                        skip_fav.setText(NC.getString(R.string.skip_drop_location));

                    } else {

                        if (popular_places.size() > 3)
                            for (int i = popular_places.size() - 1; i >= 3; i--) {
                                popular_places.remove(i);
                            }

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
                        }
                        SessionSave.saveSession("popular_places", popular_place_array.toString(), getActivity());
                    }


                    setfav_drop();
                    if (json.getInt("status") == 1) {

                        fav_driver_available = json.getInt("fav_drivers");
                        fav_driver_message = json.getString("fav_driver_message");
//
                        final JSONArray jarray = json.getJSONArray("detail");

                        SessionSave.saveSession("driver_around_miles", json.getString("driver_around_miles"), getActivity());
                        final int l = jarray.length();

                        if (jarray.length() > 0) {
                            if (jarray.getJSONObject(0).has("travel_modelid")) {
                                travel_model_id = jarray.getJSONObject(0).getString("travel_modelid");
                                next_higher_model = jarray.getJSONObject(0).getInt("next_higher_model");
                                pass_confirm_mess = jarray.getJSONObject(0).getString("pass_confirm_mess");
                            } else {
                                travel_model_id = "";
                            }
                        }
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
//                            next_higher_model =  jarray.getJSONObject(i).getInt("next_higher_model");
//                        pass_confirm_mess =  jarray.getJSONObject(i).getString("pass_confirm_mess");
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
                        Approxi_time("yes");
                        dotsProgressBar1.setVisibility(View.GONE);
                        availablecars.setText("- " + NC.getResources().getString(R.string.nodrivers));
                        model_avail.setText(selected_carmodel_name);
                        min_fare = json.getJSONObject("fare_details").getString("min_fare");
                        min_ppl = json.getJSONObject("fare_details").getString("model_size");
                        fare_frag.setText(json.getJSONObject("fare_details").getString("description"));
                        // eta_ = "0";
                        //   fare_minimum.setText(SessionSave.getSession("Currency", getActivity()) + " " + min_fare);
                        //fare_minimum_ppl.setText(min_ppl + " " + getString(R.string.ppl));
                        fare_minimum_ppl.setText("1-" + min_ppl);
                        // FindNearestlocal();
                        if (booking_state == BOOKING_STATE.STATE_TWO) {
                            //    System.out.println("___________notaxi" + (int) E_time);
                            if (pickup_marker != null)
                                pickup_marker.remove();
                            //  System.out.println("settting markk3");
                            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());
                            try {
                                // comment today by elumalai
                                Setpickupmarker(sf.getPickuplatlng());
                                FindNearestlocal();
                                /*pickup_marker = map.addMarker(new MarkerOptions()
                                        .position(sf.getPickuplatlng())
                                        .icon(BitmapDescriptorFactory.fromBitmap(b)));*/
                                //  pickup_marker.setTitle("pickup");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //  pickup_marker.setAnchor(0.0f, 1f);
                        }
//                        if (bol_isFromBooknow) {
//                            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.nodrivers), "" + NC.getResources().getString(R.string.ok), "");
//                        }
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {

                try {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                //  ShowToast(getActivity(), result);
                                Toast.makeText(getActivity(), getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

            fav_label.setTypeface(Typeface.DEFAULT_BOLD);
            fav_label.setText(p.getLabel_name());
            fav_bot_lay.addView(v);
            // fav_label.setSelected(true);

//            fav_label.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            fav_label.setSelected(true);
        }


    }

    /**
     * this method is used to show appoximate time to arrival of taxi
     *
     * @param status its passed the parameter status yes or no
     */

    public void Approxi_time(String status) {
        // TODO Auto-generated method stub
        if (txt_min_car1_ != null) {
            if ((int) E_time != 0)
                txt_min_car1_.setText((int) E_time + "" + " " + NC.getResources().getString(R.string.min));
            else
                estimated_time_c.setText("-");
//            fare_eta.setText(E_time + "" + " " + NC.getResources().getString(R.string.min));
            if (status.equals("yes"))
                txt_min_car1_.setVisibility(View.INVISIBLE);
            else {
                //    fare_eta.setText("-");
                txt_min_car1_.setVisibility(View.INVISIBLE);
            }
        }
        if (dotsProgressBar1 != null)
            dotsProgressBar1.setVisibility(View.GONE);

    }

    /**
     * this class is used to ssearch taxi if available
     */

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

                    } else {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
                    }

                } catch (final Exception e) {
                    // book_now_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                    // book_now_r.setClickable(true);
                    // book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.booknow_header_bgcolor));
                    SessionSave.saveSession("trip_id", "", getActivity());
                    book_later_r.setClickable(true);
                    errorInConnection(NC.getResources().getString(R.string.check_internet_connection));
                    //  Toast.makeText(getContext(), getString(R.string.connection_error) + " " + getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    /**
     * For Calculating approx distance for eta and trip if user has buisness key
     */

    public class FindApproxDistance implements APIResult {
        int type;

        public FindApproxDistance() {
            ArrayList<LatLng> points = new ArrayList<>();
            LatLng pick = new LatLng(sf.getPickuplat(), sf.getPickuplng());
            LatLng drop = new LatLng(sf.getDroplat(), sf.getDroplng());
            points.add(pick);
            points.add(drop);
            String url = new Route().GetDistanceTime(getActivity(), points, "en", false);
            if (url != null && !url.equals(""))
                new APIService_Retrofit_JSON(getActivity(), this, true, url).execute();
            else
                Apicall_Book_After("", "");

            type = DISTANCE_TYPE_FOR_BOOK_LATER;
            // new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
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
                new APIService_Retrofit_JSON_NoProgress(c, this, null, true, url, true).execute();

            // new APIService_Retrofit_JSON(c, this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                if (getView() != null)
                    try {
                        System.out.println("elu carmodel" + result);
                        if (new JSONObject(result).getJSONArray("routes").length() != 0) {
                            JSONObject obj = new JSONObject(result).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                            JSONObject ds = obj.getJSONObject("distance");
                            String dis = ds.getString("value");
                            JSONObject timee = obj.getJSONObject("duration");
                            String time = timee.getString("value");
                            double times = Double.parseDouble(time) / 60;
                            double dist = Double.parseDouble(dis) / 1000;

                            System.out.println("elu carmodel" + "___" + times + "___" + dist);

                            //    Toast.makeText(getActivity(),"**"+String.valueOf(type) , Toast.LENGTH_SHORT).show();
                            if (type == DISTANCE_TYPE_FOR_ETA) {

                                try {
                                    if (time.equals("0"))
                                        E_time = 1.0;
                                    else
                                        E_time = Double.parseDouble(df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf((int) times)))));
                                    System.out.println("fareeeeeeeeeeeee" + "___" + times + "___" + (int) E_time + "__" + E_time);
                                    int eTime = (int) E_time;

                                    if (sf.getDroplatlng() == null)
                                        pickup_approx_fare.setText("");
                                   /* else {
                                        if (promocode_type.equals("1")) {
                                            approx_fare = approxFare(getActivity(), dist, times) - discount;
                                        } else if (discount_percentage != 0) {
                                            approx_fare = approxFare(getActivity(), dist, times) * discount_percentage / 100;
                                        } else
                                            approx_fare = approxFare(getActivity(), dist, times);
                                    }*/
//
// if (pickup_approx_fare != null)
//                                        pickup_approx_fare.setText(NC.getResources().getString(R.string.pick_up_aprox_text) + " " + E_time + " " + NC.getResources().getString(R.string.minutes_));
//                                    if (pickup_approx_fare != null)
//                                        pickup_approx_fare.setText(String.valueOf(approx_fare));
                                    if (!sf.getPickuplocTxt().trim().equals("") && booking_state != BOOKING_STATE.STATE_ONE) {
                                        if (pickup_marker != null)        //            pickup_marker = map.addMarker(new MarkerOptions()
                                            pickup_marker.remove();
                                        System.out.println("settting markk4" + E_time + "__" + eTime);
                                        if (TaxiUtil.mDriverdata.size() != 0) {
                                            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf(eTime), getActivity(), sf.getPickuplocTxt());
                                            // comment today by elumalai
                                            Setpickupmarker(sf.getPickuplatlng());
                                            /* pickup_marker = map.addMarker(new MarkerOptions()
                                                    .position(sf.getPickuplatlng())
                                                    .icon(BitmapDescriptorFactory.fromBitmap(b)));*/

                                        } else
                                            // comment today by elumalai
                                           /* pickup_marker = map.addMarker(new MarkerOptions()
                                                    .position(sf.getPickuplatlng())
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));*/
                                            // pickup_marker.setTitle("pickup");
                                            //  pickup_marker.setAnchor(0.0f, 1f);
                                            Approxi_time("yes");
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else if (type == DISTANCE_TYPE_FOR_BOOK_LATER) {

                                System.out.println("elu DISTANCE_TYPE_FOR_BOOK_LATER" + "__" + E_time);

                                approx_travel_time = String.valueOf(times);
                                approx_travel_dist = String.valueOf(dist);

                                if (isBookAfter)
                                    Apicall_Book_After(approx_travel_dist, approx_travel_time);
                                else {
                                    //  approx_fare = approxFare(getActivity(), dist, times);
                                    if (promocode_type.equals("1")) {
                                        approx_fare = 0.0;
                                        approx_fare = approxFare(getActivity(), dist, times) - discount;
                                    } else if (discount_percentage != 0) {
                                        approx_fare = 0.0;
                                        approx_fare_temp = approxFare(getActivity(), dist, times) * discount_percentage / 100;
                                        approx_fare = approx_fare_temp;
                                    } else {
                                        approx_fare = 0.0;
                                        approx_fare = approxFare(getActivity(), dist, times);
                                    }
                                    System.out.println("Fare 3" + "__" + approx_fare);

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
                                System.out.println("elu DISTANCE_TYPE_FOR_FARE" + "__" + E_time);
                                /*discount, discount_percentage;
                     promocode_type = ""*/

                                if (promocode_type.equals("1")) {
                                    approx_fare = 0.0;
                                    approx_fare = approxFare(getActivity(), dist, times) - discount;
                                } else if (discount_percentage != 0) {
                                    approx_fare = 0.0;
                                    approx_fare_temp = approxFare(getActivity(), dist, times) - approxFare(getActivity(), dist, times) * discount_percentage / 100;
                                    System.out.println("Fare 34..." + "__" + approx_fare_temp);
                                    approx_fare = approx_fare_temp;
                                    System.out.println("Fare 34rrrr" + "__" + approx_fare);
                                } else {
                                    approx_fare = 0.0;
                                    approx_fare = approxFare(getActivity(), dist, times);
                                }


                                System.out.println("Fare" + "__" + approx_fare);
                                if (approx_fare < 0) {
                                    pickup_approx_fare.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, "0"))));
                                    estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, "0"))));
                                } else {
                                    pickup_approx_fare.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                                    estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                                }

                            }
                        } else if (isBookAfter == true)
                            Apicall_Book_After("0", "0");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


            } else {
                ShowToast.center(getActivity(), getString(R.string.server_con_error));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (sf != null)
            sf.onActivityResult(requestCode, resultCode, data);
    }

    /*
    * this method is used to call the api
    * */
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
                            Double approx_timee = 0.0, approx_diste = 0.0;
                            try {
                                approx_diste = Double.parseDouble(approx_distance) / 1000;
                                approx_timee = Double.parseDouble(apprpx_time) / 60;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            j.put("approx_distance", approx_travel_dist);
                            j.put("approx_duration", approx_timee);
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("request_type", "1");
                            //j.put("promo_code", ed_promocode.getText().toString());
                            j.put("promo_code", promo_code);
                            j.put("now_after", "1");

                            j.put("approx_distance", approx_diste);
                            j.put("approx_duration", approx_timee);
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("request_type", "1");
                            //j.put("promo_code", ed_promocode.getText().toString());
                            j.put("promo_code", promo_code);
                            j.put("now_after", "1");
                            j.put("notes", SessionSave.getSession("notes", getActivity()));
                            j.put("passenger_app_version", MainActivity.APP_VERSION);
                            j.put("travel_modelid", Integer.parseInt(travel_model_id.equals("") ? "0" : travel_model_id));
                            j.put("booked_location", bookingLocation);
                            j.put("booked_latitude", LastKnownLat);
                            j.put("booked_longitude", LastKnownLng);
                            final String url = "type=savebooking";
//                            book_later_r.setBackgroundColor(NC.getResources().getColor(R.color.header_header_bgcolorColor));
                            book_later_r.setClickable(true);
                            Booking_type = "after";
                            SessionSave.saveSession("SearchUrl", url, getActivity());
                            //    System.out.println("api called");
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

                ImageView iv = (ImageView) progressDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(getActivity())
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);
                System.out.println("showing progress");

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * for calculating eta and trip distance if user does not have buisness key
     */

    private class Approximate_Time extends AsyncTask<Void, Void, Void> {
        double Ddistance;
        String Smetric;
        int type;
        double time;

        Approximate_Time(double distance, String metric, int isETA) {
            Log.e("************", "Approximate_Time: show");
            //   showDialog();
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

//            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity());
//            pickup_marker = map.addMarker(new MarkerOptions()
//                    .position(LastKnownLatLng)
//                    .icon(BitmapDescriptorFactory.fromBitmap(b)));
//            pickup_marker.setTitle("pickup");pickup_marker.setAnchor(0.0f,1f);
            if ((booking_state == BOOKING_STATE.STATE_TWO || booking_state == BOOKING_STATE.STATE_ONE) && sf.getPickuplatlng() != null) {        //            Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity());
                if (pickup_marker != null)        //            pickup_marker = map.addMarker(new MarkerOptions()
                    pickup_marker.remove();
                System.out.println("settting markk5");//                    .position(LastKnownLatLng)
                if (TaxiUtil.mDriverdata.size() != 0) {
                    Bitmap b = CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt());        //                    .icon(BitmapDescriptorFactory.fromBitmap(b)));

                    Setpickupmarker(sf.getPickuplatlng());
                    // comment today by elumalai
                   /* pickup_marker = map.addMarker(new MarkerOptions()        //            pickup_marker.setTitle("pickup");pickup_marker.setAnchor(0.0f,1f);
                            .position(sf.getPickuplatlng())
                            .icon(BitmapDescriptorFactory.fromBitmap(b)));*/
                } else {
                    // comment today by elumalai
                    /*pickup_marker = map.addMarker(new MarkerOptions()        //            pickup_marker.setTitle("pickup");pickup_marker.setAnchor(0.0f,1f);
                            .position(sf.getPickuplatlng())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));*/
                    //  pickup_marker.setTitle("pickup");
                    //  pickup_marker.setAnchor(0.0f, 1f);
                }
                if (type == DISTANCE_TYPE_FOR_FARE) {
                    if (promocode_type.equals("1")) {
                        approx_fare = 0.0;
                        approx_fare = approxFare(getActivity(), Ddistance, time) - discount;
                    } else if (discount_percentage != 0) {
                        approx_fare = 0.0;
                        approx_fare_temp = approxFare(getActivity(), Ddistance, time) * discount_percentage / 100;
                        approx_fare = approx_fare_temp;
                    } else {
                        approx_fare = 0.0;
                        approx_fare = approxFare(getActivity(), Ddistance, time);
                    }


                    System.out.println("Fare 1" + "__" + approx_fare);
                    // approx_fare = approxFare(getActivity(), Ddistance, time);
                    if (approx_fare < 0) {
                        pickup_approx_fare.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, "0"))));
                        estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, "0"))));
                    } else {
                        pickup_approx_fare.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                        estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
                    }
                }
//            else if(type==DISTANCE_TYPE_FOR_ETA){
//
//                    approx_fare = approxFare(getActivity(), Ddistance, time);
//                    pickup_approx_fare.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
//                    estimated_fare_c.setText(SessionSave.getSession("Currency", getActivity()) + df.format(Double.parseDouble(String.format(Locale.UK, String.valueOf(approx_fare)))));
//            }
                Approxi_time("yes");
            }
        }
    }

    /*
    * this method is used to calculate time trip time
    * */
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

                // pickup_approx_fare.setText(NC.getResources().getString(R.string.pick_up_aprox_text) + " " + E_time + " " + NC.getResources().getString(R.string.minutes_));
                System.out.println("kavi carmodel_r" + E_time);
            } else if (type == DISTANCE_TYPE_FOR_BOOK_LATER) {
                approx_travel_time = String.valueOf(distance_m);
                approx_travel_dist = String.valueOf(distance);

                if (isBookAfter)
                    Apicall_Book_After(approx_travel_dist, approx_travel_time);
                else {
                    if (sf.getDroplatlng() != null) {
                        if (promocode_type.equals("1")) {
                            approx_fare = 0.0;
                            approx_fare = approxFare(getActivity(), Double.parseDouble(approx_travel_dist), distance_m) - discount;
                        } else if (discount_percentage != 0) {
                            approx_fare = 0.0;
                            approx_fare_temp = approxFare(getActivity(), Double.parseDouble(approx_travel_dist), distance_m) * discount_percentage / 100;
                            approx_fare = approx_fare_temp;

                        } else {
                            approx_fare = 0.0;
                            approxFare(getActivity(), Double.parseDouble(approx_travel_dist), distance_m);
                        }

                        System.out.println("Fare 2" + "__" + approx_fare);
                    }
                    // approx_fare = approxFare(getActivity(), Double.parseDouble(approx_travel_dist), distance_m);

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
//                approx_fare = approxFare(getActivity(), distance, distance_m);
//                pickup_approx_fare.setText(String.valueOf(approx_fare));
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


    /**
     * Dilog pops up which store promo code and verify while save_booking api is called
     *
     * @param
     */
    public void promocode(final View v_temp) {

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
            public void onClick(final View view_) {
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
                    //
                    mDialog.dismiss();
                    if (sf.getPickuplocTxt() != null) {
                        carlay_click(CurrentCarModel, 1);

                    }


                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }


    private void ShowPromoDilaog() {

        try {

            final View view = View.inflate(getActivity(), R.layout.alert_view, null);
            final Dialog mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.show();
            Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id), getActivity());
            FontHelper.applyFont(getActivity(), mDialog.findViewById(R.id.alert_id));
            final TextView titleTxt = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView msgTxt = (TextView) mDialog.findViewById(R.id.message_text);
            msgTxt.setVisibility(View.GONE);
            final EditText promocodeEdt = (EditText) mDialog.findViewById(R.id.forgotmail);
            final Button OK = (Button) mDialog.findViewById(R.id.button_success);
            final Button Cancel = (Button) mDialog.findViewById(R.id.button_failure);
            Cancel.setVisibility(View.GONE);
            promocodeEdt.setVisibility(View.VISIBLE);
            OK.setText("" + NC.getResources().getString(R.string.apply));
            titleTxt.setText("" + NC.getResources().getString(R.string.reg_promocode));
            int maxLengthpromoCode = getResources().getInteger(R.integer.promoMaxLength);
            promocodeEdt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            promocodeEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthpromoCode)});
            InputFilter[] editFilters = promocodeEdt.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] = new InputFilter.AllCaps();
            promocodeEdt.setFilters(newFilters);
            promocodeEdt.setHint("" + NC.getResources().getString(R.string.reg_enterprcode));
            promocodeEdt.setText(promo_code);
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    try {
                        String PromoCode = promocodeEdt.getText().toString();
                        if (PromoCode.trim().equals(""))
                            Toast.makeText(getActivity(), NC.getResources().getString(R.string.promo_code_empty), Toast.LENGTH_SHORT).show();
                        if (Validation.validations(Validation.ValidateAction.isNullPromoCode, getActivity(), PromoCode)) {
                            mDialog.dismiss();
                            promo_code = PromoCode;
                            JSONObject j = new JSONObject();
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("promo_code", promo_code);
                            String url = "type=check_promocode_booking";
                            new CheckPromoCode(url, j);
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    promocodeEdt.setText("");
                    promocodeEdt.setHint("" + NC.getResources().getString(R.string.reg_enterprcode));
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private class CheckPromoCode implements APIResult {

        private CheckPromoCode(final String url, JSONObject data) {
            // new APIService_Volley_JSON(CardRegisterAct.this, this, data, false).execute(url);
            new APIService_Retrofit_JSON(getActivity(), this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + url).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(getActivity(), "Message", "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");

                        if (sf.getPickuplocTxt() != null) {
                            carlay_click(CurrentCarModel, 1);

                        }
                    } else {
                        //alert_view(getActivity(), "Message", "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    CToast.ShowToast(getActivity(), json.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        promo_code = "";
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    promo_code = "";
                }
            } else {
                promo_code = "";
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                    }
                });
//                alert_view(getActivity(), "Message", "" + result, "" + getResources().getString(R.string.ok), "");
            }
        }
    }


    @Override
    public void onStop() {
        //To prevent window leakage error close all dialogs before activity stops.
//if(splitFareDialog!=null)
//    if(splitFareDialog.isVisible())
//    splitFareDialog.dismiss();
        closeDialog();
        ((MainHomeFragmentActivity) getActivity()).cancel_b.setVisibility(View.GONE);
        if (alertmDialog != null)
            if (alertmDialog.isShowing())
                alertmDialog.dismiss();
        if (dt_mDialog != null)
            if (dt_mDialog.isShowing())
                dt_mDialog.dismiss();
        if (Dialog_Common.mCustomDialog != null)
            if (Dialog_Common.mCustomDialog.isShowing())
                Dialog_Common.mCustomDialog.dismiss();

        if (errorDialog != null && errorDialog.isShowing())
            errorDialog.dismiss();
        super.onStop();
    }

    public void onBackPress() {

        if (booking_state == BOOKING_STATE.STATE_ONE) {

            if (confirm_ride_lay.isShown()) {//kavi

                select_car_lay.setVisibility(View.VISIBLE);
                confirm_ride_lay.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            } else {
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
            }
        } else if (booking_state == BOOKING_STATE.STATE_TWO) {
            System.out.println("_________called");
            if (sf.getPickuplat() != LastKnownLatLng.latitude) {
                bookingLocation = "";
                request_taxi.setVisibility(View.VISIBLE);
            }
            if (confirm_ride_lay.isShown()) {

                select_car_lay.setVisibility(View.VISIBLE);
                confirm_ride_lay.setVisibility(View.GONE);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            } else {
                booking_state = BOOKING_STATE.STATE_ONE;
                clearsetPickDropMarker();
            }
        } else if (booking_state == BOOKING_STATE.STATE_THREE) {

            //  mov_cur_loc_first.performClick();
            if (!pickloc_confirm.getText().toString().equals(getString(R.string.fetching_address))) {

                booking_state = BOOKING_STATE.STATE_TWO;
                SearchFragment.SET_FOR_PICKUP = true;
                BookTaxiNewFrag.pick_flag = true;
                BookTaxiNewFrag.drop_flag = false;
                SessionSave.saveSession("notmovemarkers", "0", getActivity());
                clearsetPickDropMarker();
            }
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


    public class Address_s extends AsyncTask<String, String, String> {
        public Context mContext;
        LatLng mPosition;
        String Address = "";
        Geocoder geocoder;
        private double latitude;
        private double longitude;
        List<android.location.Address> addresses = null;

        public Address_s(Context context, LatLng position) {

            mContext = context;
            mPosition = position;
            latitude = mPosition.latitude;
            longitude = mPosition.longitude;
            geocoder = new Geocoder(context, Locale.getDefault());
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // showDialog();
            SessionSave.saveSession("notes", "", mContext);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                System.out.println("address size11:" + latitude + "%$#" + longitude);

                if (Geocoder.isPresent()) {
                    System.out.println("address size:!" + latitude + "%$#" + longitude);
                    addresses = geocoder.getFromLocation(latitude, longitude, 3);

                    System.out.println("address size:@" + addresses.size());

                    if (addresses.size() == 0) {
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);

                    } else {
                        System.out.println("address size:@@" + addresses.size());
                        for (int i = 0; i < addresses.size(); i++) {
                            Address += addresses.get(0).getAddressLine(i) + ", ";
                            CURRENT_COUNTRY_CODE = addresses.get(0).getCountryCode();
                        }
                        if (Address.length() > 0)
                            Address = Address.substring(0, Address.length() - 2);
                    }
                } else {
                    System.out.println("address size:@V" + addresses.size());
                    if (NetworkStatus.isOnline(mContext))
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
                    else {
                        BookTaxiNewFrag.setfetch_address();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("errrrrrrr", "" + e.fillInStackTrace());
                System.out.println("address size:@#");
                if (NetworkStatus.isOnline(mContext))
                    new convertLatLngtoAddressApi(mContext, latitude, longitude);
                else {
                    BookTaxiNewFrag.setfetch_address();
                }
            }
            return Address;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (getActivity() != null) {
                closeDialog();
                TaxiUtil.pAddress = "" + Address;
                if (Address.length() != 0) {
                    if (errorAddressDialog != null && errorAddressDialog.isShowing())
                        errorAddressDialog.cancel();

                    if (bookingLocation.equals(""))
                        bookingLocation = Address;
                    System.out.println("address size:&& drop:" + BookTaxiNewFrag.drop_flag + "pickup:" + BookTaxiNewFrag.pick_flag);
                    if (BookTaxiNewFrag.drop_flag) {
                        if (SearchFragment.SET_FOR_DROP) {
                            SearchFragment.SET_FOR_DROP = false;
                        } else {
                            BookTaxiNewFrag.sf.setDroplocTxt(Address);
                            sf.dropVisible();
                            BookTaxiNewFrag.sf.setDroplatlng(new LatLng(latitude, longitude));
                        }
                    } else if (BookTaxiNewFrag.pick_flag) {

                        BookTaxiNewFrag.sf.setPickuplocTxt(Address);
                        if (getActivity() != null && !sf.getPickuplocTxt().trim().equals("") && sf.getDroplatlng() == null)
                            sf.dropVisible();
                        // else if (getActivity() != null)
                        //   sf.dropGone(); //kavi
                        System.out.println("address size:@# post method " + latitude + "," + longitude);
                        SearchFragment.SET_FOR_PICKUP = true;
                        BookTaxiNewFrag.sf.setPickuplatlng(new LatLng(latitude, longitude));
                        //clearsetPickDropMarker();
                        if (!SessionSave.getSession("notmovemarkers", getActivity()).equals("1")) {
                            request_taxi.setVisibility(View.GONE);
                        } else
                            request_taxi.setVisibility(View.VISIBLE); //kavi
                    }
                } else {
                    // errorInGettingAddress(getString(R.string.server_con_error));
                    //   BookTaxiNewFrag.setfetch_address();
                }

            }
            result = null;

        }

        /**
         * this class is used to GOOGLE PLACE API and convert the address
         */

        public class convertLatLngtoAddressApi implements APIResult {
            public convertLatLngtoAddressApi(Context c, double lati, double longi) {
                String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                new APIService_Retrofit_JSON(c, this, null, true, googleMapUrl, true).execute();
            }

            @Override
            public void getResult(boolean isSuccess, String result) {
                System.out.println("____SDD____3d_" + result + "SSS" + isSuccess);
                if (result != null && !result.toString().trim().equalsIgnoreCase("")) {
                    if (errorAddressDialog != null && errorAddressDialog.isShowing())
                        errorAddressDialog.cancel();
                    setLocation(result.toString());
                } else
                    errorInGettingAddress(getString(R.string.server_con_error));
            }
        }


        public void setLocation(String inputJson) {

            try {
                if (getActivity() != null) {
                    System.out.println("____SDD____3dd" + inputJson);
                    JSONObject object = new JSONObject("" + inputJson);
                    JSONArray array = object.getJSONArray("results");
                    object = array.getJSONObject(0);
                    System.out.println("____SDD____3" + BookTaxiNewFrag.sf.getPickuplocTxt());
                    BookTaxiNewFrag.sf.setPickuplocTxt(object.getString("formatted_address"));
                    if (bookingLocation.equals(""))
                        bookingLocation = object.getString("formatted_address");
                    if (getActivity() != null && !sf.getPickuplocTxt().trim().equals("") && sf.getDroplatlng() == null)
                        sf.dropVisible();
                    else if (getActivity() != null)
                        //  sf.dropGone(); //kavi
                        request_taxi.setVisibility(View.VISIBLE);
                    BookTaxiNewFrag.sf.setPickuplatlng(new LatLng(latitude, longitude));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (mContext != null) {
                    if (!NetworkStatus.isOnline(mContext))
                        Toast.makeText(mContext, mContext.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, mContext.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    BookTaxiNewFrag.setfetch_address();
                }
            }
        }
    }

    /**
     * Dilog pops up which add notes while save_booking api is called
     */
   /* public void notesPopup() {

        // TODO Auto-generated method stub
        final View view = View.inflate(getActivity(), R.layout.add_notes_lay, null);
        final Dialog mDialog = new Dialog(getActivity(), R.style.NewDialog);
        mDialog.setContentView(view);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id), getActivity());
        FontHelper.applyFont(getActivity(), view);
        final TextView t = (TextView) mDialog.findViewById(R.id.title_text);

        final EditText notes_txt = (EditText) mDialog.findViewById(R.id.notes_desc);
        // notes_txt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        // notes_txt.setHint(NC.getResources().getString(R.string.enter_promo_code));

        final TextView OK = (TextView) mDialog.findViewById(R.id.button_ok);

        final TextView Cancel = (TextView) mDialog.findViewById(R.id.button_fail);


        Point pointSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(pointSize);
        Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    notes_txt.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                SessionSave.saveSession("notes", "", getActivity());
                if (isBookLater) {
                    isBookAfter = true;
                    book_later_fun();
                } else {
                    SessionSave.saveSession("notmovemarkers", "1", getActivity());
                    booking_state = BOOKING_STATE.STATE_THREE;
                    pickloc_confirm.setText(sf.getPickuplocTxt());
                    request_taxi.setVisibility(View.VISIBLE);
                    clearsetPickDropMarker();
                   *//* bol_isFromBooknow = true;
                    CurrentCarModel.performClick();*//*
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
                    Phone = notes_txt.getText().toString();
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        notes_txt.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    }
                    if (Phone.trim().equals("")) {
                        Toast.makeText(getActivity(), NC.getResources().getString(R.string.notes_empty), Toast.LENGTH_SHORT).show();
                    } else {
                        SessionSave.saveSession("notes", Phone, getActivity());
                        if (isBookLater) {
                            isBookAfter = true;
                            book_later_fun();
                        } else {
                           *//* bol_isFromBooknow = true;
                            CurrentCarModel.performClick();*//*
                            SessionSave.saveSession("notmovemarkers", "1", getActivity());
                            booking_state = BOOKING_STATE.STATE_THREE;
                            pickloc_confirm.setText(sf.getPickuplocTxt());
                            request_taxi.setVisibility(View.VISIBLE);
                            clearsetPickDropMarker();
                        }
                    }


                    mDialog.dismiss();


                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }*/
    public void ConfirmBookNowHigher(String message) {
        try {


            booking_state = BOOKING_STATE.STATE_THREE;
            pickloc_confirm.setText(sf.getPickuplocTxt());
            request_taxi.setVisibility(View.VISIBLE);
            clearsetPickDropMarker();
//            if (true) {
//                if (errorDialog != null && errorDialog.isShowing())
//                    errorDialog.dismiss();
//                System.out.println("setCanceledOnTouchOutside" + message);
//                final View view = View.inflate(getActivity(), R.layout.netcon_lay, null);
//                errorDialog = new Dialog(getActivity(), R.style.dialogwinddow);
//                errorDialog.setContentView(view);
//                errorDialog.setCancelable(false);
//                errorDialog.setCanceledOnTouchOutside(false);
//                FontHelper.applyFont(getActivity(), errorDialog.findViewById(R.id.alert_id));
//                errorDialog.show();
//                final TextView title_text = (TextView) errorDialog.findViewById(R.id.title_text);
//                final TextView message_text = (TextView) errorDialog.findViewById(R.id.message_text);
//                final Button button_success = (Button) errorDialog.findViewById(R.id.button_success);
//                final Button button_failure = (Button) errorDialog.findViewById(R.id.button_failure);
//                title_text.setText("" + NC.getResources().getString(R.string.message));
//                message_text.setText("" + message);
//                button_success.setText("" + NC.getResources().getString(R.string.ok));
//                button_failure.setText("" + NC.getResources().getString(R.string.cancel));
//                button_success.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        // TODO Auto-generated method stub
//                        if (!SessionSave.getSession("Id", getActivity()).equals("")) {
//                            System.out.println("tryAgain1");
//                            booking_state = BOOKING_STATE.STATE_THREE;
//                            pickloc_confirm.setText(sf.getPickuplocTxt());
//                            request_taxi.setVisibility(View.VISIBLE);
//                            clearsetPickDropMarker();
//                        }
//
//
//                        errorDialog.dismiss();
//
//                    }
//                });
//                button_failure.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        // TODO Auto-generated method stub
//
//
////                        Activity activity = getActivity();
////
////                        final Intent intent = new Intent(Intent.ACTION_MAIN);
////                        intent.addCategory(Intent.CATEGORY_HOME);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        activity.startActivity(intent);
////                        activity.finish();
//                        errorDialog.dismiss();
//
//                    }
//                });
//            } else {
//                try {
//                    errorDialog.dismiss();
//                    if (getActivity() != null) {
//                        Intent intent = new Intent(getActivity(), getActivity().getClass());
//                        getActivity().startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
        int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen
        int adjustHeight = (height - (carlayout.getHeight()));
        System.out.println("_________________>>>" + height + "___" + adjustHeight);

        /*CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, adjustHeight, padding);
        map.moveCamera(cu);*/
    }

    private void Setpickupmarker(LatLng latLng_set) {

        if (pick_flag) {
            if (pickup_marker != null)
                pickup_marker.remove();
            mapppin_drop.setVisibility(View.GONE);
            mapppin_common.setVisibility(View.VISIBLE);
            mapppin_common.setImageResource(R.drawable.pickuplocation);


        } else {
            mapppin_common.setVisibility(View.GONE);
            if (pickup_marker != null)
                pickup_marker.remove();
            /*pickup_marker = map.addMarker(new MarkerOptions()
                    .position(sf.getPickuplatlng())
                    .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromView(String.valueOf((int) E_time), getActivity(), sf.getPickuplocTxt()))));
            pickup_marker.setTitle("pick_up");*/
            //pickup_marker.setFlat(true);
            // addMarkers();

            pickup_marker = map.addMarker(new MarkerOptions().position(sf.getPickuplatlng())
                    .flat(true).title("pickup").icon(BitmapDescriptorFactory.fromResource(R.drawable.pickuplocation)));
        }


    }

    private void Setdropmarker(LatLng latLng_set) {

        if (drop_flag) {
            if (drop != null)
                drop.remove();
            mapppin_common.setVisibility(View.GONE);
            mapppin_drop.setImageResource(R.drawable.droplocation);
            mapppin_drop.setVisibility(View.VISIBLE);

        } else {
            mapppin_drop.setVisibility(View.GONE);

           /* drop = map.addMarker(new MarkerOptions()
                    .position(latLng_set)
                    .icon(BitmapDescriptorFactory.fromBitmap(CustomMarker.getMarkerBitmapFromViewForDrop(sf.getDroplocTxt(),
                            getActivity()))));

            drop.setTitle("drop");
            drop.setFlat(true);*/
            /*if (drop != null)
                drop.remove();*/
            drop = map.addMarker(new MarkerOptions().position(sf.getDroplatlng())
                    .flat(true).title("Drop").icon(BitmapDescriptorFactory.fromResource(R.drawable.droplocation)));

        }

    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}