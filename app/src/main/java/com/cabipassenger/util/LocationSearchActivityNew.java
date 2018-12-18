package com.cabipassenger.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainActivity;
import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;
import com.cabipassenger.adapter.PlacesAutoCompleteAdapter;
import com.cabipassenger.adapter.Taxi_searchList_adapter;
import com.cabipassenger.data.SearchListData;
import com.cabipassenger.data.apiData.PlacesDetail;
import com.cabipassenger.service.FourSquareService;
import com.cabipassenger.service.ServiceGenerator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by developer on 14/3/17.
 */
//public class LocationSearchActivityNew {
//}
public class LocationSearchActivityNew extends MainActivity implements Taxi_searchList_adapter.LocationListItemClick, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(6.4626999, 68.14712), new LatLng(28.20453, 97.34466));
    private LatLngBounds latLngBounds = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    public static boolean SET_FOR_PICKUP ;

    private EditText drop_location;
    private ExploreAsyncTask explore;
    private RecyclerView rv;
    private Taxi_searchList_adapter adapter;
    private String type;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private TextView addHome;
    private TextView addWork;
    private TextView addFavourite;

    private TextView addHomeSub;
    private TextView addWorkSub;
    private TextView addFavouriteSub;

    private LinearLayout addHomeLayout;
    private LinearLayout addWorkLayout;
    private LinearLayout addFavouriteLayout;


    private LinearLayout taxi__locationsearch_viewgroup_content;

    private EditText currentlocTxt;
    private boolean isFourSquare = false;
    private String formattedDate;
    private PlacesDetail pickup_obj;
    private PlacesDetail drop_obj;
    private boolean onlyDrop;
    LinearLayout pickupp;
    FrameLayout pickup_pinlay;
    private JSONArray popular_places = null;
    private ArrayList<PlacesDetail> popular_places_array = new ArrayList<>();
    private boolean onlyPickup;

    @Override
    public int setLayout() {
        return R.layout.taxi_location_fragment_new;
    }

    @Override
    public void Initialize() {
        FontHelper.applyFont(this, findViewById(R.id.top));
        Colorchange.ChangeColor((ViewGroup) findViewById(R.id.top), this);
        drop_location = (EditText) findViewById(R.id.taxi__locationsearch_edittext_search);
        currentlocTxt = (EditText) findViewById(R.id.currentlocTxt);
        pickupp = (LinearLayout) findViewById(R.id.pickupp);
        pickup_pinlay = (FrameLayout) findViewById(R.id.pickup_pinlay);
        drop_location.setHintTextColor(CL.getColor(this, R.color.textviewcolor_light));
        drop_location.setTextColor(CL.getColor(this, R.color.black));

        currentlocTxt.setHintTextColor(CL.getColor(this, R.color.textviewcolor_light));
        currentlocTxt.setTextColor(CL.getColor(this, R.color.black));

        currentlocTxt.setHint(getResources().getString(R.string.picklocation));

        addHome = (TextView) findViewById(R.id.add_home);
        addFavourite = (TextView) findViewById(R.id.add_favourite);
        addWork = (TextView) findViewById(R.id.add_work);

        addHomeSub = (TextView) findViewById(R.id.add_home_subtext);
        addFavouriteSub = (TextView) findViewById(R.id.add_favourite_subtext);
        addWorkSub = (TextView) findViewById(R.id.add_work_subtext);


        addHomeLayout = (LinearLayout) findViewById(R.id.home_layout);
        addFavouriteLayout = (LinearLayout) findViewById(R.id.favourite_layout);
        addWorkLayout = (LinearLayout) findViewById(R.id.work_layout);


        taxi__locationsearch_viewgroup_content = (LinearLayout) findViewById(R.id.taxi__locationsearch_viewgroup_content);

        rv = (RecyclerView) findViewById(R.id.taxi__locationsearch_listview_locations);

        try {
            popular_places = new JSONArray(SessionSave.getSession("popular_places", this));
            for (int j = 0; j < popular_places.length(); j++) {
                JSONObject jo = popular_places.getJSONObject(j);
                PlacesDetail o = new PlacesDetail();
                o.setLabel_name(jo.getString("label_name"));
                o.setLatitude(jo.getDouble("latitude"));
                o.setLongtitute(jo.getDouble("longtitute"));
                o.setLocation_name(jo.getString("location_name"));
                popular_places_array.add(o);

                switch (j) {
                    case 0:
                        addHomeLayout.setVisibility(View.VISIBLE);
                        addHome.setText(o.label_name);
                        addHomeSub.setText(o.getLocation_name());
                        break;
                    case 1:
                        addWorkLayout.setVisibility(View.VISIBLE);
                        addWork.setText(o.label_name);
                        addWorkSub.setText(o.getLocation_name());
                        break;
                    case 2:
                        addFavouriteLayout.setVisibility(View.VISIBLE);
                        addFavourite.setText(o.label_name);
                        addFavouriteSub.setText(o.getLocation_name());
                        break;
                    default:
                        break;

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        addHomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_item_clicked(0);
            }
        });
        addWorkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_item_clicked(1);
            }
        });
        addFavouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_item_clicked(2);
            }
        });
        if (SessionSave.getSession("isFourSquare", LocationSearchActivityNew.this).trim().equals("1")) {
            isFourSquare = true;
        }

        if (!isFourSquare) {

            buildGoogleApiClient();
//
//            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                    .setTypeFilter(Place.TYPE_COUNTRY)
//                    .setCountry(SplashAct.CURRENT_COUNTRY_CODE)
//                    .build();
          //  AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).setCountry(SplashActivity.CURRENT_COUNTRY_ISO_CODE).build();
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).setCountry(SplashActivity.CURRENT_COUNTRY_ISO_CODE).build();
            if (!SessionSave.getSession("PLAT", LocationSearchActivityNew.this).trim().equals("")) {
                try {
                    Double lat = Double.parseDouble(SessionSave.getSession("PLAT", LocationSearchActivityNew.this));
                    Double lon = Double.parseDouble(SessionSave.getSession("PLNG", LocationSearchActivityNew.this));


                    latLngBounds = getBoundingBox(lat, lon, 50000);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else
                latLngBounds = null;

            mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(LocationSearchActivityNew.this, R.layout.taxi__search_list_item,
                    mGoogleApiClient,
                    latLngBounds, typeFilter);


            rv.setLayoutManager(new WrapContentLinearLayoutManager(LocationSearchActivityNew.this));
            rv.setAdapter(mAutoCompleteAdapter);


        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            formattedDate = df.format(c.getTime());

        }


        ImageView btn_back = (ImageView) findViewById(R.id.back_icon);
        ImageButton locationsearch_clear = (ImageButton) findViewById(R.id.taxi__locationsearch_imagebutton_clear);

     //   Glide.with(this).load(SessionSave.getSession("image_path", this) + "headerLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.header_titleTxt));
        locationsearch_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drop_location.setText("");
            }
        });


        Bundle b = getIntent().getExtras();
        if (b != null) {
            type = b.getString("type");
            pickup_obj = b.getParcelable("pickup_obj");
            drop_obj = b.getParcelable("drop_obj");
            try {
                onlyDrop = b.getBoolean("onlyDrop");
            } catch (Exception e) {
                e.printStackTrace();
            }
            onlyPickup=b.getBoolean("onlyPickup");
        //    System.out.println("Cameeeee"+onlyPickup);
        }

        if (type.equals("D")) {
            drop_location.setHint(NC.getString(R.string.enter_dest));
            drop_location.requestFocus();
            drop_location.performClick();
            if (pickup_obj != null) {
                currentlocTxt.setText(pickup_obj.getLocation_name());
            }
            SET_FOR_PICKUP = false;
        } else {
            currentlocTxt.setHint(NC.getString(R.string.enter_pickup));
            currentlocTxt.requestFocus();
            currentlocTxt.performClick();
            if (drop_obj != null) {
                drop_location.setText(drop_obj.getLocation_name());
            }
            SET_FOR_PICKUP = true;
        }
       // System.out.println("seconds remaining:"+type+"__"+SET_FOR_PICKUP+"__"+isFourSquare);
        if (onlyDrop) {
            pickupp.setVisibility(View.GONE);
            pickup_pinlay.setVisibility(View.GONE);
            findViewById(R.id.pickup_drop_Sep).setVisibility(View.GONE);

            if (SessionSave.getSession("Lang", LocationSearchActivityNew.this).equals("ar")||SessionSave.getSession("Lang",LocationSearchActivityNew.this).equals("fa"))
                drop_location.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dot, 0);
            else
                drop_location.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dot, 0, 0, 0);

            drop_location.setCompoundDrawablePadding(20);
        }

        if(onlyPickup){
            findViewById(R.id.dropppp).setVisibility(View.GONE);
            pickup_pinlay.setVisibility(View.GONE);
            findViewById(R.id.pickup_drop_Sep).setVisibility(View.GONE);

            if (SessionSave.getSession("Lang", LocationSearchActivityNew.this).equals("ar")||SessionSave.getSession("Lang",LocationSearchActivityNew.this).equals("fa"))
                currentlocTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dot, 0);
            else
                currentlocTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dot, 0, 0, 0);

            currentlocTxt.setCompoundDrawablePadding(20);
        }

        drop_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SET_FOR_PICKUP = false;
            }
        });

        currentlocTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println("touchedCL1");
            }
        });
        drop_location.addTextChangedListener(new TextWatcher() {
            String search_text[];

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Check FourSquare is enabled or Not
                if (!isFourSquare) {
                    if (s != null && s.length() > 0) {
                        mAutoCompleteAdapter.getFilter().filter(s);

                        taxi__locationsearch_viewgroup_content.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        rv.getAdapter().notifyDataSetChanged();
                    } else {
                        rv.setVisibility(View.GONE);
                        taxi__locationsearch_viewgroup_content.setVisibility(View.VISIBLE);
                    }
                } else {

                    search_text = drop_location.getText().toString().split(",");

                    if (s != null && s.length() > 0) {
                        if (explore != null) {
                            explore.cancel(true);
                            explore = null;
                        }
                        explore = new ExploreAsyncTask();
                        explore.execute(s.toString());

                        taxi__locationsearch_viewgroup_content.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
//                        rv.getAdapter().notifyDataSetChanged();
                    } else {
                        rv.setVisibility(View.GONE);
                        taxi__locationsearch_viewgroup_content.setVisibility(View.VISIBLE);
                    }


                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        currentlocTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (!isFourSquare) {
                    if (s != null && s.length() > 0) {
                        mAutoCompleteAdapter.getFilter().filter(s);

                        taxi__locationsearch_viewgroup_content.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        rv.getAdapter().notifyDataSetChanged();
                    } else {
                        rv.setVisibility(View.GONE);
                        taxi__locationsearch_viewgroup_content.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (s != null && s.length() > 0) {
                        if (explore != null) {
                            explore.cancel(true);
                            explore = null;
                        }
                        explore = new ExploreAsyncTask();
                        explore.execute(s.toString());
                        taxi__locationsearch_viewgroup_content.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        //rv.getAdapter().notifyDataSetChanged();
                    } else {
                        rv.setVisibility(View.GONE);
                        taxi__locationsearch_viewgroup_content.setVisibility(View.VISIBLE);
                    }

                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        currentlocTxt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
                SET_FOR_PICKUP = true;
             //   System.out.println("touchedCL"+SET_FOR_PICKUP);
                currentlocTxt.setText("");
                currentlocTxt.setHint(NC.getString(R.string.enter_pickup));
                if (drop_obj != null)
                    drop_location.setText(drop_obj.getLocation_name());

//                drop_location.setFocusable(false);
//                drop_location.setFocusableInTouchMode(false);
//                drop_location.setText(NC.getString(R.string.enter_dest));

                rv.setVisibility(View.GONE);
                taxi__locationsearch_viewgroup_content.setVisibility(View.VISIBLE);



                return false;
            }
        });

        drop_location.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
               // System.out.println("touchedDR");
                drop_location.setText("");
                drop_location.setHint(NC.getString(R.string.enter_dest));

                if (pickup_obj != null)
                    currentlocTxt.setText(pickup_obj.getLocation_name());
//
//                currentlocTxt.setFocusable(false);
//                currentlocTxt.setFocusableInTouchMode(false);
//                currentlocTxt.setText(NC.getString(R.string.enter_pickup));

                rv.setVisibility(View.GONE);
                taxi__locationsearch_viewgroup_content.setVisibility(View.VISIBLE);

                SET_FOR_PICKUP = false;

                return false;
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isAcceptingText()) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                finish();
            }
        });

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                            if (imm.isAcceptingText()) {
                                InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        }

                        if (isFourSquare) {
                            if (SET_FOR_PICKUP)
                                currentlocTxt.setText(TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd1() + " , " + TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd2());
                            else
                                drop_location.setText(TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd1() + " , " + TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd2());

                            TaxiUtil.Latitude = Double.valueOf(TaxiUtil.SEARCH_LIST_ITEM.get(position).getLat());
                            TaxiUtil.Longitude = Double.valueOf(TaxiUtil.SEARCH_LIST_ITEM.get(position).getLng());
                            TaxiUtil.Address = drop_location.getText().toString();

                            Bundle conData = new Bundle();
                            conData.putString("param_result", TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd1() + " , " + TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd2());
                            conData.putDouble("lat", TaxiUtil.Latitude);
                            conData.putDouble("lng", TaxiUtil.Longitude);
                            conData.putBoolean("set_for_pickup", SET_FOR_PICKUP);
                            Intent intent = new Intent();
                            intent.putExtras(conData);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            try {

                                final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                                final String placeId = String.valueOf(item.placeId);

                                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                        .getPlaceById(mGoogleApiClient, placeId);

                                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                    @Override
                                    public void onResult(@NonNull PlaceBuffer places) {
                                        if (places.getCount() == 1) {

                                        //    System.out.println("toucheddd"+SET_FOR_PICKUP);
                                            if (SET_FOR_PICKUP)
                                                currentlocTxt.setText(places.get(0).getName().toString()+" "+places.get(0).getAddress());
                                            else
                                                drop_location.setText(places.get(0).getName().toString()+" "+places.get(0).getAddress());
                                            TaxiUtil.Latitude = places.get(0).getLatLng().latitude;
                                            TaxiUtil.Longitude = places.get(0).getLatLng().longitude;

                                            Bundle conData = new Bundle();
                                            conData.putString("param_result",places.get(0).getName().toString()+" "+ places.get(0).getAddress().toString());
                                            conData.putDouble("lat", TaxiUtil.Latitude);
                                            conData.putDouble("lng", TaxiUtil.Longitude);
                                            conData.putBoolean("set_for_pickup", SET_FOR_PICKUP);
                                            Intent intent = new Intent();
                                            intent.putExtras(conData);
                                            setResult(RESULT_OK, intent);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                                           // Toast.makeText(getApplicationContext(), getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                })
        );
    }

    private void fav_item_clicked(int i) {
        if (SET_FOR_PICKUP)
            currentlocTxt.setText(popular_places_array.get(i).getLocation_name());
        else
            drop_location.setText(popular_places_array.get(i).getLocation_name());
        TaxiUtil.Latitude = popular_places_array.get(i).getLatitude();
        TaxiUtil.Longitude = popular_places_array.get(i).getLongtitute();

        Bundle conData = new Bundle();
        conData.putString("param_result", popular_places_array.get(i).getLabel_name()+" "+popular_places_array.get(i).getLocation_name());
        conData.putDouble("lat", TaxiUtil.Latitude);
        conData.putDouble("lng", TaxiUtil.Longitude);
        conData.putBoolean("set_for_pickup", SET_FOR_PICKUP);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(int pos) {

    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
//            Log.v("Google API", "Connecting");
//            mGoogleApiClient.connect();
//            startLocationUpdates();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            Log.v("Google API", "Dis-Connecting");
//            mGoogleApiClient.disconnect();
//            stopLocationUpdates();
//        }
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, LocationSearchActivityNew.this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    //    Log.i("Connected ", "Connection started");


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update location every second
        // location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, LocationSearchActivityNew.this);
        else
            mGoogleApiClient.connect();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                // Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }


    @Override
    public void onBackPressed() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        finish();

    }

    private class ExploreAsyncTask extends AsyncTask<String, Void, String> {

        String result = null;
        String city;
        String state;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            FourSquareService client = new ServiceGenerator(LocationSearchActivityNew.this, "https://api.foursquare.com/v2/", true).createService(FourSquareService.class);
            Call<Object> coreResponse = client.requestExplore(formattedDate,
                    SessionSave.getSession("PLAT", LocationSearchActivityNew.this) + "," + SessionSave.getSession("PLNG", LocationSearchActivityNew.this), params[0], SessionSave.getSession("android_foursquare_api_key", LocationSearchActivityNew.this));
            coreResponse.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    Object dataResponse = null;

                    try {
                        if (response.body() != null) {
                            dataResponse = response.body();

                            Gson gson = new Gson();
                            result = gson.toJson(dataResponse);

                        }

                        try {
                            if (result != null) {


                                JSONObject json = new JSONObject(result);

                                TaxiUtil.SEARCH_LIST_ITEM.clear();


                                if (json.getJSONObject("meta").getInt("code") == 200) {

                               //     Log.d("doInBackground ", "Success: ");

                                    JSONArray minivenues = json.getJSONObject("response").getJSONArray("minivenues");
                                    for (int i = 0; i < minivenues.length(); i++) {
                                        SearchListData data = new SearchListData();

                                        JSONObject c = minivenues.getJSONObject(i);

                                        String address = c.getString("name");
                                        data.setAdd1(address);
                                        data.setPlace_id(c.getString("id"));


                                        JSONObject location = c.getJSONObject("location");
                                        // if(location.has(""))
                                        String lat = location.getString("lat");
                                        String lng = location.getString("lng");

                                        data.setLat(lat);
                                        data.setLng(lng);


                                        if (location.has("city")) {
                                            city = location.getString("city");
                                        }
                                        if (location.has("state")) {
                                            state = location.getString("state");
                                        }

                                        if (city != null && state != null) {
                                            data.setAdd2(city + " , " + state);
                                        } else if (city != null) {
                                            data.setAdd2(city);
                                        } else if (state != null) {
                                            data.setAdd2(state);
                                        }


                                        TaxiUtil.SEARCH_LIST_ITEM.add(data);
                                    }

                                    try {
                                        if (adapter == null) {
                                            adapter = new Taxi_searchList_adapter(LocationSearchActivityNew.this);
                                            rv.setLayoutManager(new WrapContentLinearLayoutManager(LocationSearchActivityNew.this));
                                            rv.setVisibility(View.VISIBLE);
                                            rv.setAdapter(adapter);
                                        }

                                        if (TaxiUtil.SEARCH_LIST_ITEM != null && TaxiUtil.SEARCH_LIST_ITEM.size() > 0) {
                                            rv.setVisibility(View.VISIBLE);
                                            addHome.setVisibility(View.GONE);
                                            addWork.setVisibility(View.GONE);
                                            rv.getAdapter().notifyDataSetChanged();
                                        } else {
                                            rv.setVisibility(View.GONE);
                                            addHome.setVisibility(View.VISIBLE);
                                            addWork.setVisibility(View.VISIBLE);
                                        }

                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        e.printStackTrace();
                                    }


                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (dataResponse != null) {
                       // System.out.println("rrc_____" + dataResponse);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            return result;
        }

    }


    private void getBounds() {
        FourSquareService client = new ServiceGenerator(LocationSearchActivityNew.this, "https://maps.googleapis.com/", true).createService(FourSquareService.class);
        Call<Object> coreResponse = client.requestBounds("india");
        coreResponse.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                Object dataResponse;

                try {
                    if (response.body() != null) {
                        dataResponse = response.body();

                        Gson gson = new Gson();
                        String result = gson.toJson(dataResponse);

                       // Log.d("doInBackground ", "northeast: " + result);


                        if (result != null) {

                            JSONObject json = new JSONObject(result);

                            if (json.getString("status").equalsIgnoreCase("OK")) {

                                JSONArray results = json.getJSONArray("results");

                                String southWestLat = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds").getJSONObject("southwest").getString("lat");
                                String southWestLng = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds").getJSONObject("southwest").getString("lng");

                                String northEastLat = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast").getString("lat");
                                String northEastLng = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast").getString("lng");

                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private LatLngBounds getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInMeters) {

        final double[] boundingBox = new double[4];

        final double latRadian = Math.toRadians(pLatitude);

        final double degLatKm = 110.574235;
        final double degLongKm = 110.572833 * Math.cos(latRadian);
        final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
        final double deltaLong = pDistanceInMeters / 1000.0 /
                degLongKm;

        final double minLat = pLatitude - deltaLat;
        final double minLong = pLongitude - deltaLong;
        final double maxLat = pLatitude + deltaLat;
        final double maxLong = pLongitude + deltaLong;

        boundingBox[0] = minLat;
        boundingBox[1] = minLong;
        boundingBox[2] = maxLat;
        boundingBox[3] = maxLong;


        return new LatLngBounds(new LatLng(minLat, minLong), new LatLng(maxLat, maxLong));
    }
}