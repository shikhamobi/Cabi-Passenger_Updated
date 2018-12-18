package com.cabipassenger.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.R;
import com.cabipassenger.adapter.PlacesAutoCompleteAdapter;
import com.cabipassenger.adapter.PlacesAutoCompleteAdapterNew;
import com.cabipassenger.adapter.Taxi_searchList_adapter;
import com.cabipassenger.data.SearchListData;
import com.cabipassenger.data.apiData.PlacesDetail;
import com.cabipassenger.interfaces.PickupDropSet;
import com.cabipassenger.service.FourSquareService;
import com.cabipassenger.service.ServiceGenerator;
import com.cabipassenger.util.Address_s;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.LocationSearchActivityNew;
import com.cabipassenger.util.RecyclerItemClickListener;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

import static com.cabipassenger.fragments.BookTaxiNewFrag.map;
import static com.cabipassenger.fragments.BookTaxiNewFrag.sf;

public class SearchFragment extends Fragment implements Taxi_searchList_adapter.LocationListItemClick, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String a;
    int keyDel;
    private RelativeLayout searchlay;
    private LinearLayout dropppp;
    private Dialog mDialog;

    private Address_s address;
    private Dialog alertmDialog;
    private RelativeLayout drop_loc_lay;
    PickupDropSet listener;
    private LatLng pickuplatlng;
    private LatLng droplatlng;
    private String currentlocTxt;
    private String current_address;
    private String droplocEdt;
    public EditText currentlocETxt, drop_location_edittext, dummy_edittext;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private PlacesAutoCompleteAdapterNew placesAutoCompleteAdapterNew;
    private LatLng LastKnownLatLng_;
    RecyclerView rv, rv1;
    Taxi_searchList_adapter adapter, adapternew;
    private String formattedDate;
    private GoogleApiClient mGoogleApiClient;
    private ExploreAsyncTask explore;
    private ExploreAsyncTaskNew exploreAsyncTaskNew;
    private boolean isFourSquare = false;
    public static int valu = 0;
    private String pickupHint, dropHint;
    private ImageView closedropdone, closepickupdone;
    private boolean isSearchPick = false;
    private boolean isSearchDrop = false;
    private LatLngBounds latLngBounds = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private ImageButton imageclosebut;
    public ImageView closedrop, closepickup;
    public TextView add_home;
    public static boolean SET_FOR_PICKUP, SET_FOR_DROP;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CAMERA = 123;

    public String getPickupHint() {
        return pickupHint;
    }

    public void setPickupHint(String pickupHint) {
        this.pickupHint = pickupHint;
    }

    public String getDropHint() {
        return dropHint;
    }

    public void setDropHint(String dropHint) {
        this.dropHint = dropHint;
    }

    public void setSearchFragmentListner(PickupDropSet listener) {
        this.listener = listener;
    }

    public Double getDroplng() {

        if (droplatlng == null)
            return 0.0;
        return droplatlng.longitude;
    }

//    public void setDroplng(Double droplng) {
//        this.droplng = droplng;
//        BookTaxiNewFrag.D_longitude = droplng;
//    }

    public Double getDroplat() {

        if (droplatlng == null)
            return 0.0;
        return droplatlng.latitude;
    }

//    public void setDroplat(Double droplat) {
//        this.droplat = droplat;
//        BookTaxiNewFrag.D_latitude = droplat;
//    }

    public Double getPickuplng() {
        if (pickuplatlng == null)
            return 0.0;
        return pickuplatlng.longitude;
    }

//    public void setPickuplng(Double pickuplng) {
//        this.pickuplng = pickuplng;
//        BookTaxiNewFrag.P_longitude = pickuplng;
//    }

    public LatLng getPickuplatlng() {
        return pickuplatlng;
    }

    public LatLng getDroplatlng() {
        /*if (droplatlng.latitude == 0 && droplatlng.latitude == 0)
            return null;*/
        return droplatlng;
    }

    public void setPickuplatlng(LatLng pickuplng) {
        this.pickuplatlng = pickuplng;
        if (pickuplng != null)
            listener.pickUpSet(pickuplatlng.latitude, pickuplatlng.longitude);
        //  currentlocETxt.setFocusable(true);
        //   currentlocETxt.requestFocus();

        //  BookTaxiNewFrag.P_longitude = pickuplng;
    }

    public void setDroplatlng(LatLng droplatlng) {
        this.droplatlng = droplatlng;
//        BookTaxiNewFrag.D_latitude = droplatlng.latitude;
//        BookTaxiNewFrag.D_longitude = droplatlng.longitude;
        //if (droplatlng.latitude != 0 && droplatlng.longitude != 0)
        if (droplatlng != null)
            listener.dropSet(droplatlng.latitude, droplatlng.longitude);
        //  BookTaxiNewFrag.P_longitude = pickuplng;
    }

    public Double getPickuplat() {
        if (pickuplatlng == null)
            return 0.0;
        return pickuplatlng.latitude;

    }

//    public void setPickuplat(Double pickuplat) {
//        this.pickuplat = pickuplat;
//        BookTaxiNewFrag.P_latitude = pickuplat;
//    }

    //private Double droplng = 0.0;
    private Dialog r_mDialog;
    private static String LocationRequestedBy = "";

    public String getPickuplocTxt() {
        if (currentlocTxt == null)
            return "";
        return currentlocTxt;

    }

    public void setPickuplocTxt(String pickuplocTxt) {
        this.pickuplocTxt = pickuplocTxt;
        currentlocTxt = (pickuplocTxt.replace(", null", "").replace(" null", ""));
        currentlocETxt.setText(currentlocTxt);
        System.out.println("currentpick***** " + currentlocTxt);
        rv.setVisibility(View.GONE);
        rv1.setVisibility(View.GONE);
        imageclosebut.setVisibility(View.GONE);
        isSearchPick = true;
        currentlocETxt.setFocusable(true);
        currentlocETxt.setCursorVisible(true);
        currentlocETxt.requestFocus();
    }

    public String getDroplocTxt() {
        if (droplocEdt == null)
            return "";
        return droplocEdt;
    }

    public void setDroplocTxt(String droplocTxt) {
        this.droplocTxt = droplocTxt;
        droplocEdt = (droplocTxt.replace(", null", "").replace("null", ""));
        drop_location_edittext.setText(droplocEdt);

////   BookTaxiNewFrag.dropLocationPin(getPickuplat(),getPickuplng(),getDroplat(),getPickuplng());
        dropVisible();
        rv.setVisibility(View.GONE);
        rv1.setVisibility(View.GONE);
        imageclosebut.setVisibility(View.GONE);
        isSearchDrop = true;
        drop_location_edittext.setFocusable(true);
        drop_location_edittext.setCursorVisible(true);
        drop_location_edittext.requestFocus();

    }

    private String pickuplocTxt, droplocTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_home_new, container, false);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());
        searchlay = (RelativeLayout) v.findViewById(R.id.searchlay);
        drop_loc_lay = (RelativeLayout) v.findViewById(R.id.drop_loc_lay);
        rv = (RecyclerView) v.findViewById(R.id.taxi__locationsearch_listview_locations);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv1 = (RecyclerView) v.findViewById(R.id.taxi__locationsearch_listview_locationsnew);
        rv1.setLayoutManager(new LinearLayoutManager(getActivity()));
        closedropdone = (ImageView) v.findViewById(R.id.closedropdone);
        closepickupdone = (ImageView) v.findViewById(R.id.closepickupdone);   // closedropdone.closepickupdone
        imageclosebut = (ImageButton) v.findViewById(R.id.imageclosebut);
        dropppp = (LinearLayout) v.findViewById(R.id.dropppp);
        currentlocETxt = (EditText) v.findViewById(R.id.currentlocTxt);
        drop_location_edittext = (EditText) v.findViewById(R.id.drop_location_edittext);
        currentlocETxt.setSelected(true);
        drop_location_edittext.setSelected(true);
        closedrop = (ImageView) v.findViewById(R.id.closedrop);
        closepickup = (ImageView) v.findViewById(R.id.closepickup);
        add_home = (TextView) v.findViewById(R.id.add_home);
        dummy_edittext = (EditText) v.findViewById(R.id.dummy_edittext);
        ImageButton locationsearch_clear = (ImageButton) v.findViewById(R.id.taxi__locationsearch_imagebutton_clear);
        closepickupdone.setVisibility(View.VISIBLE);

        locationsearch_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drop_location_edittext.setText("");
                rv.setVisibility(View.GONE);
            }
        });
        imageclosebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.setVisibility(View.GONE);
                rv1.setVisibility(View.GONE);
                imageclosebut.setVisibility(View.GONE);
            }
        });
        // current_address
        //        drop_fav = (LinearLayout
        //                ) v.findViewById(R.id.drop_fav);
        //        drop_close = (ImageView
        //                ) v.findViewById(R.id.drop_close);

        //   pickup_drop_Sep = (View) v.findViewById(R.id.pickup_drop_Sep);

        FontHelper.applyFont(getContext(), searchlay);
        String from = "";
        try {
            from = getArguments().getString("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (from.equals("home")) {
//            drop_fav.setVisibility(View.VISIBLE);
//            lay_pick_fav.setVisibility(View.VISIBLE);
            //  dropppp.setVisibility(View.GONE);
            //pickup_pin.setVisibility(View.VISIBLE);
            //   pickup_pinlay.setVisibility(View.GONE);
            searchlay.setBackgroundResource(R.color.transparent);

//            pickup_drop_Sep.setVisibility(View.GONE);

            intializeHome();
        }
        if (SessionSave.getSession("isFourSquare", getActivity()).trim().equals("1")) {
            isFourSquare = true;
        }
        System.out.println("kavi isFourSquare " + isFourSquare);
        if (!isFourSquare) {

            buildGoogleApiClient();
            System.out.println("plat*** " + SessionSave.getSession("PLAT", getActivity()) + "Plng*** " + SessionSave.getSession("PLNG", getActivity()));
            if (!SessionSave.getSession("PLAT", getActivity()).trim().equals("") || !SessionSave.getSession("PLNG", getActivity()).trim().equals("")) {
                Double lat = Double.parseDouble(SessionSave.getSession("PLAT", getActivity()));
                Double lon = Double.parseDouble(SessionSave.getSession("PLNG", getActivity()));


                latLngBounds = getBoundingBox(lat, lon, 50000);
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE).build();
//            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                    .setTypeFilter(Place.TYPE_COUNTRY)
//                    .setCountry(SplashAct.CURRENT_COUNTRY_CODE)
//                    .build();


                mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.taxi__search_list_item,
                        mGoogleApiClient,
                        latLngBounds, typeFilter);
                placesAutoCompleteAdapterNew = new PlacesAutoCompleteAdapterNew(getActivity(), R.layout.taxi__search_list_item,
                        mGoogleApiClient,
                        latLngBounds, typeFilter);

                rv.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
                rv.setAdapter(mAutoCompleteAdapter);
                rv1.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
                rv1.setAdapter(placesAutoCompleteAdapterNew);
            }
        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            formattedDate = df.format(c.getTime());

        }
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Drop

                        view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            if (imm.isAcceptingText()) {
                                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        }
                        SET_FOR_DROP = false;
                      /*  if (isFourSquare) {
                            String dropnewstr = TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd1() + " , " + TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd2();
                            drop_location_edittext.setText(dropnewstr.replace(", null", ""));

                            TaxiUtil.Latitude = Double.valueOf(TaxiUtil.SEARCH_LIST_ITEM.get(position).getLat());
                            TaxiUtil.Longitude = Double.valueOf(TaxiUtil.SEARCH_LIST_ITEM.get(position).getLng());
                            TaxiUtil.Address = drop_location_edittext.getText().toString();

                            *//*Bundle conData = new Bundle();
                            conData.putString("param_result", dropnewstr.replace(", null", ""));
                            conData.putDouble("lat", TaxiUtil.Latitude);
                            conData.putDouble("lng", TaxiUtil.Longitude);
                            Intent intent = new Intent();
                            intent.putExtras(conData);
                            getActivity().setResult(RESULT_OK, intent);*//* //kavi
                            isSearchDrop = true;
                            rv1.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                            System.out.println(" checking ther   " + TaxiUtil.Latitude + "    " + TaxiUtil.Longitude);
                            //  droploaction(TaxiUtil.Latitude, TaxiUtil.Longitude);

                            droplocEdt = dropnewstr.replace(", null", "");

                            LatLng p = new LatLng(TaxiUtil.Latitude, TaxiUtil.Longitude);
                            setDroplatlng(p);
                            BookTaxiNewFrag.pick_flag = false;
                            BookTaxiNewFrag.drop_flag = false;
                            SearchFragment.SET_FOR_PICKUP = true;
                            sf.dummy_edittext.setFocusable(true);
                            sf.dummy_edittext.requestFocus();
                            sf.currentlocETxt.setCursorVisible(false);
                            sf.drop_location_edittext.setCursorVisible(false);
                        } else {*/
                        try {

                            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                            final String placeId = String.valueOf(item.placeId);

                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);

                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(@NonNull PlaceBuffer places) {
                                    if (places.getCount() == 1) {

                                        drop_location_edittext.setText(places.get(0).getAddress().toString().replace(", null", ""));

                                        TaxiUtil.Latitude = places.get(0).getLatLng().latitude;
                                        TaxiUtil.Longitude = places.get(0).getLatLng().longitude;
                                        TaxiUtil.Address = drop_location_edittext.getText().toString();
                                        System.out.println("elu checking here to   " + TaxiUtil.Latitude + "    " + TaxiUtil.Longitude);

                                        droplocEdt = places.get(0).getAddress().toString();
                                        BookTaxiNewFrag.pick_flag = false;
                                        BookTaxiNewFrag.drop_flag = true;
                                        SearchFragment.SET_FOR_PICKUP = false;
                                        // drop_location_edittext.setText();
                                        LatLng p = new LatLng(TaxiUtil.Latitude, TaxiUtil.Longitude);
                                        sf.setDroplatlng(p);
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.latitude, p.longitude), 16f));

                                    } else {
                                        Toast.makeText(getActivity()
                                                , "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            isSearchDrop = true;
                            rv1.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*}*/

                    }
                })
        );
        rv1.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View View_, int position) {
                        // pickup


                        View_ = getActivity().getCurrentFocus();
                        if (View_ != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            if (imm.isAcceptingText()) {
                                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                                im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        }
                        SET_FOR_PICKUP = true;
                      /*  if (isFourSquare) {
                            String newstr = TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd1() + " , " + TaxiUtil.SEARCH_LIST_ITEM.get(position).getAdd2();
                            currentlocETxt.setText(newstr.replace(", null", ""));

                            TaxiUtil.Latitude = Double.valueOf(TaxiUtil.SEARCH_LIST_ITEM.get(position).getLat());
                            TaxiUtil.Longitude = Double.valueOf(TaxiUtil.SEARCH_LIST_ITEM.get(position).getLng());
                            TaxiUtil.Address = currentlocETxt.getText().toString();

                           *//* Bundle conData = new Bundle();
                            conData.putString("param_result", newstr.replace(", null", ""));
                            conData.putDouble("lat", TaxiUtil.Latitude);
                            conData.putDouble("lng", TaxiUtil.Longitude);
                            Intent intent = new Intent();
                            intent.putExtras(conData);
                            getActivity().setResult(RESULT_OK, intent);*//* //kavi
                            isSearchPick = true;
                            rv1.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                            System.out.println("kavi checking here to   " + TaxiUtil.Latitude + "    " + TaxiUtil.Longitude);

                            currentlocTxt = newstr.replace(", null", "");

                            LatLng p = new LatLng(TaxiUtil.Latitude, TaxiUtil.Longitude);
                            setPickuplatlng(p);

//                            if (droplatlng == null)
//                                BookTaxiNewFrag.movetoCurrentloc();
                        } else {*/
                        try {

                            final PlacesAutoCompleteAdapterNew.PlaceAutocomplete item = placesAutoCompleteAdapterNew.getItem(position);
                            final String placeId = String.valueOf(item.placeId);

                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);

                            final View finalView = View_;
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(@NonNull PlaceBuffer places) {
                                    if (places.getCount() == 1) {

                                        currentlocETxt.setText(places.get(0).getAddress().toString().replace(", null", ""));

                                        TaxiUtil.Latitude = places.get(0).getLatLng().latitude;
                                        TaxiUtil.Longitude = places.get(0).getLatLng().longitude;
                                        TaxiUtil.Address = currentlocETxt.getText().toString();

                                          /*  Bundle conData = new Bundle();
                                            conData.putString("param_result", places.get(0).getAddress().toString());
                                            conData.putDouble("lat", TaxiUtil.Latitude);
                                            conData.putDouble("lng", TaxiUtil.Longitude);
                                            Intent intent = new Intent();
                                            intent.putExtras(conData);
                                            getActivity().setResult(RESULT_OK, intent);*/ //kavi
                                        System.out.println("elu checking here to  ss " + TaxiUtil.Latitude + "    " + TaxiUtil.Longitude);

                                        currentlocTxt = places.get(0).getAddress().toString();


                                        LatLng p = new LatLng(TaxiUtil.Latitude, TaxiUtil.Longitude);
                                        sf.setPickuplatlng(p);
                                        currentlocETxt.setClickable(true);
                                        hideSoftKeyboard(finalView);
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.latitude, p.longitude), 16f));


                                        /*if (droplatlng == null)
                                            BookTaxiNewFrag.movetoCurrentloc();*/
                                    } else {
                                        Toast.makeText(getActivity()
                                                , "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            isSearchPick = true;
                            rv1.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                            //   BookTaxiNewFrag.dropLocationPin(TaxiUtil.Latitude, TaxiUtil.Longitude, getDroplat(), getDroplng());
                            //   BookTaxiNewFrag.dropLocationPin(getPickuplat(), getPickuplng(), TaxiUtil.Latitude, TaxiUtil.Longitude);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*}*/


                    }
                })
        );
        closedrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drop_location_edittext.setText("");
                // LatLng p = new LatLng(0, 0);
                //  sf.setDroplocTxt("");
                //setDroplatlng(p);
                SET_FOR_PICKUP = true;
                SET_FOR_DROP = true;
                BookTaxiNewFrag.drop_flag = true;
                BookTaxiNewFrag.pick_flag = false;
                sf.setDroplatlng(null);
                sf.setDroplocTxt("");
                listener.pickUpSet(getPickuplat(), getPickuplng());
                BookTaxiNewFrag.Route_anim = false;
               /* SET_FOR_PICKUP = true;
                BookTaxiNewFrag.drop_flag = false;
                BookTaxiNewFrag.pick_flag = true;
                listener.pickUpSet(getPickuplat(), getPickuplng());
                currentlocETxt.setFocusable(true);
                currentlocETxt.requestFocus();*/ //kavi

            }
        });
        closepickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentlocETxt.setText("");
                SET_FOR_PICKUP = true;
                BookTaxiNewFrag.drop_flag = false;
                BookTaxiNewFrag.pick_flag = true;
                LatLng p = new LatLng(0, 0);
                sf.setPickuplocTxt("");
                setPickuplatlng(null);
            }
        });
        currentlocETxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SessionSave.saveSession("notmovemarkers", "0", getActivity());
                //   currentlocETxt.setFocusable(true);
                //   currentlocETxt.setCursorVisible(true);
                currentlocETxt.requestFocus();
                BookTaxiNewFrag.pick_flag = true;
                BookTaxiNewFrag.drop_flag = false;
                closepickup.setVisibility(View.GONE);
                closedropdone.setVisibility(View.GONE);
                closepickupdone.setVisibility(View.VISIBLE);
                SET_FOR_PICKUP = true;
                sf.setPickuplatlng(sf.getPickuplatlng());
                if (sf.getDroplatlng() != null) {
                    BookTaxiNewFrag.Route_anim = false;
                    //setDroplatlng(sf.getDroplatlng());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getPickuplat(), getPickuplng()), 16f));

                }

                return false;
            }

        });
       /* currentlocETxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionSave.saveSession("notmovemarkers", "0", getActivity());
                currentlocETxt.setFocusable(true);
                currentlocETxt.setCursorVisible(true);
                currentlocETxt.requestFocus();
                BookTaxiNewFrag.pick_flag = true;
                BookTaxiNewFrag.drop_flag = false;
                SET_FOR_PICKUP = true;
                if (!currentlocETxt.getText().toString().equals("")) {
                    BookTaxiNewFrag.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getPickuplat(), getPickuplng()), 12f));

                }
            }
        });*/
        closepickupdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // currentlocETxt.setFocusable(false);
                //  currentlocETxt.setCursorVisible(false);
                BookTaxiNewFrag.pick_flag = false;
                BookTaxiNewFrag.drop_flag = false;
                //  drop_location_edittext.setFocusable(false);
                //  drop_location_edittext.setCursorVisible(false);
                SET_FOR_PICKUP = false;
                SET_FOR_DROP = false;
                closepickupdone.setVisibility(View.GONE);
                sf.setPickuplatlng(sf.getPickuplatlng());
                if (sf.getDroplatlng() != null) {
                    BookTaxiNewFrag.Route_anim = true;
                    setDroplatlng(sf.getDroplatlng());
                }
                //drop_location_edittext.requestFocus();

            }
        });
        closedropdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionSave.saveSession("notmovemarkers", "1", getActivity());
                //   currentlocETxt.setFocusable(false);
                //   currentlocETxt.setCursorVisible(false);
                BookTaxiNewFrag.pick_flag = false;
                BookTaxiNewFrag.drop_flag = false;
                //  drop_location_edittext.setFocusable(false);
                //   drop_location_edittext.setCursorVisible(false);
                SET_FOR_PICKUP = false;
                SET_FOR_DROP = false;
                closedropdone.setVisibility(View.GONE);
                BookTaxiNewFrag.Route_anim = true;
                setDroplatlng(sf.getDroplatlng());

                //drop_location_edittext.requestFocus();

            }
        });
        drop_location_edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SessionSave.saveSession("notmovemarkers", "0", getActivity());
                //  drop_location_edittext.setFocusable(true);
                // drop_location_edittext.setCursorVisible(true);
                BookTaxiNewFrag.pick_flag = false;
                BookTaxiNewFrag.drop_flag = true;
                SET_FOR_PICKUP = false;
                SET_FOR_DROP = false;
                closepickup.setVisibility(View.VISIBLE);
                closedropdone.setVisibility(View.VISIBLE);
                closepickupdone.setVisibility(View.GONE);
                // drop_location_edittext.requestFocus();


                if (getDroplat() != 0.0 && getDroplng() != 0.0) {


                    if (!drop_location_edittext.getText().toString().equals("")) {
                        BookTaxiNewFrag.Route_anim = false;
                        setDroplatlng(sf.getDroplatlng());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getDroplat(), getDroplng()), 16f));

                    }

                } else {
                    try {
                        setDroplatlng(new LatLng(getPickuplat() + 0.0030, getPickuplng()));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getDroplat(), getDroplng()), 16f));

                        //  address = new Address_s(getActivity(), new LatLng(getDroplat(), getDroplng()));
                        // address.execute().get();
                        //   BookTaxiNewFrag.getAproxDropLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                drop_location_edittext.setSelection(drop_location_edittext.getText().length());
                showSoftKeyboard(v);

                return false;
            }
        });
        drop_location_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionSave.saveSession("notmovemarkers", "0", getActivity());
                drop_location_edittext.setFocusable(true);
                drop_location_edittext.setCursorVisible(true);
                BookTaxiNewFrag.pick_flag = false;
                BookTaxiNewFrag.drop_flag = true;
                SET_FOR_PICKUP = false;
                closedropdone.setVisibility(View.VISIBLE);
                closepickupdone.setVisibility(View.GONE);
                // drop_location_edittext.requestFocus();


                if (getDroplat() != 0.0 && getDroplng() != 0.0) {


                    if (!drop_location_edittext.getText().toString().equals("")) {
                        BookTaxiNewFrag.Route_anim = false;
                        setDroplatlng(sf.getDroplatlng());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getDroplat(), getDroplng()), 16f));

                    }

                } else {
                    try {
                        setDroplatlng(new LatLng(getPickuplat() + 0.0030, getPickuplng()));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getDroplat(), getDroplng()), 16f));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                drop_location_edittext.setSelection(drop_location_edittext.getText().length());
                showSoftKeyboard(v);
            }
        });


        drop_location_edittext.addTextChangedListener(new TextWatcher() {
            String search_text[];

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Check FourSquare is enabled or Not


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isFourSquare) {
                    if (s != null && s.length() > 0) {
                        System.out.println("string search" + s);
                        if (mAutoCompleteAdapter != null)
                            mAutoCompleteAdapter.getFilter().filter(s);

                        if (isSearchDrop) {
                            rv.setVisibility(View.GONE);
                            rv1.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                        } else {
                            rv.getAdapter().notifyDataSetChanged();
                            rv.setVisibility(View.VISIBLE);
                            rv1.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.VISIBLE);

                        }

                    } else {
                        //rv.setVisibility(View.GONE);`
                        // rv1.setVisibility(View.GONE);
                    }
                } else {
                    if (s != null && s.length() > 0) {
                        if (explore != null) {
                            explore.cancel(true);
                            explore = null;
                        }
                        explore = new ExploreAsyncTask();
                        explore.execute(s.toString());

                        if (isSearchDrop) {
                            rv.setVisibility(View.GONE);
                            rv1.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                        } else {
                            rv.setVisibility(View.VISIBLE);
                            rv1.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.VISIBLE);
                        }

                    } else {
                        //rv.setVisibility(View.GONE);
                        // rv1.setVisibility(View.GONE);
                    }

                }
                drop_location_edittext.setSelection(drop_location_edittext.getText().length());
                isSearchDrop = false;
            }
        });
        currentlocETxt.addTextChangedListener(new TextWatcher() {
            String search_text[];

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // Check FourSquare is enabled or Not
                if (!isFourSquare) {
                    if (s != null && s.length() > 0) {
                        if (placesAutoCompleteAdapterNew != null)
                            placesAutoCompleteAdapterNew.getFilter().filter(s);
                        if (isSearchPick) {
                            rv1.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                        } else {
                            if (rv1.getAdapter() != null)
                                rv1.getAdapter().notifyDataSetChanged();
                            rv1.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.VISIBLE);

                        }

                    } else {
                        //  rv1.setVisibility(View.GONE);
                        //  rv.setVisibility(View.GONE);
                        SET_FOR_PICKUP = true;
                        BookTaxiNewFrag.drop_flag = false;
                        BookTaxiNewFrag.pick_flag = true;
                        LatLng p = new LatLng(0, 0);
                        // setPickuplocTxt("");
                        setPickuplatlng(null);
                    }
                } else {
                    if (s != null && s.length() > 0) {

                        if (exploreAsyncTaskNew != null) {
                            exploreAsyncTaskNew.cancel(true);
                            exploreAsyncTaskNew = null;
                        }
                        exploreAsyncTaskNew = new ExploreAsyncTaskNew();
                        exploreAsyncTaskNew.execute(s.toString());
                        if (isSearchPick) {
                            rv1.setVisibility(View.GONE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.GONE);
                        } else {
                            rv1.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
                            imageclosebut.setVisibility(View.VISIBLE);
                        }

                    } else {
                        // rv1.setVisibility(View.GONE);
                        // rv.setVisibility(View.GONE);
                        SET_FOR_PICKUP = true;
                        BookTaxiNewFrag.drop_flag = false;
                        BookTaxiNewFrag.pick_flag = true;
                        LatLng p = new LatLng(0, 0);
                        // setPickuplocTxt("");
                        setPickuplatlng(null);
                    }

                }
                currentlocETxt.setSelection(currentlocETxt.getText().length());
                isSearchPick = false;
            }
        });


        return v;
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(SearchFragment.this)
                .addOnConnectionFailedListener(SearchFragment.this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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

    @Override
    public void onClick(int pos) {

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

            FourSquareService client = new ServiceGenerator(getActivity(), "https://api.foursquare.com/v2/", true).createService(FourSquareService.class);
            Call<Object> coreResponse = client.requestExplore(formattedDate,
                    SessionSave.getSession("PLAT", getActivity()) + "," + SessionSave.getSession("PLNG", getActivity()), params[0], SessionSave.getSession("android_foursquare_api_key", getActivity()));
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

                                    Log.d("doInBackground ", "Success: ");

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

                                       /* JSONArray categoryarr = c.getJSONArray("categories");
                                        if (categoryarr.length() != 0) {
                                            data.setPrefix(categoryarr.getJSONObject(0).getJSONObject("icon").getString("prefix"));
                                            data.setSuffix("bg_32" + categoryarr.getJSONObject(0).getJSONObject("icon").getString("suffix"));
                                        } else {
                                            data.setPrefix(" ");
                                            data.setSuffix(" ");
                                        }
                                        data.setDistance(String.format("%.2f", location.getDouble("distance") / 1000));
                                        System.out.println("searchdrop**** " + String.valueOf(location.getDouble("distance") / 1000));*/
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
                                            adapter = new Taxi_searchList_adapter(getActivity());
                                            rv.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
                                            // rv.setVisibility(View.VISIBLE);
                                            // rv1.setVisibility(View.GONE);
                                            rv.setAdapter(adapter);
                                        }

                                        if (TaxiUtil.SEARCH_LIST_ITEM != null && TaxiUtil.SEARCH_LIST_ITEM.size() > 0) {
                                            //rv.setVisibility(View.VISIBLE);
                                            // rv1.setVisibility(View.GONE);
                                            rv.getAdapter().notifyDataSetChanged();
                                        } else {
                                            // rv.setVisibility(View.GONE);
                                            // rv1.setVisibility(View.GONE);
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
                        System.out.println("rrc_____" + dataResponse);
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


    private class ExploreAsyncTaskNew extends AsyncTask<String, Void, String> {

        String result = null;
        String city;
        String state;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            FourSquareService client = new ServiceGenerator(getActivity(), "https://api.foursquare.com/v2/", true).createService(FourSquareService.class);
            Call<Object> coreResponse = client.requestExplore(formattedDate,
                    SessionSave.getSession("PLAT", getActivity()) + "," + SessionSave.getSession("PLNG", getActivity()), params[0], SessionSave.getSession("android_foursquare_api_key", getActivity()));
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

                                    Log.d("doInBackground ", "Success: ");

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
                                        /*JSONArray categoryarr = c.getJSONArray("categories");
                                        if (categoryarr.length() != 0) {
                                            data.setPrefix(categoryarr.getJSONObject(0).getJSONObject("icon").getString("prefix"));
                                            data.setSuffix("bg_32" + categoryarr.getJSONObject(0).getJSONObject("icon").getString("suffix"));
                                        } else {
                                            data.setPrefix(" ");
                                            data.setSuffix(" ");
                                        }
                                        data.setDistance(String.format("%.2f", location.getDouble("distance") / 1000));
                                        System.out.println("searchcurrent**** " + String.valueOf(location.getDouble("distance") / 1000));
*/ //kavi 22/8/17
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
                                        if (adapternew == null) {
                                            adapternew = new Taxi_searchList_adapter(getActivity());
                                            rv1.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
                                            //rv1.setVisibility(View.VISIBLE);
                                            // rv.setVisibility(View.GONE);
                                            rv1.setAdapter(adapternew);
                                        }

                                        if (TaxiUtil.SEARCH_LIST_ITEM != null && TaxiUtil.SEARCH_LIST_ITEM.size() > 0) {
                                            //rv1.setVisibility(View.VISIBLE);
                                            // rv.setVisibility(View.GONE);
                                            if (rv1.getAdapter() != null)
                                                rv1.getAdapter().notifyDataSetChanged();
                                        } else {
                                            // rv1.setVisibility(View.GONE);
                                            // rv.setVisibility(View.GONE);
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
                        System.out.println("rrc_____" + dataResponse);
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
    public void onResume() {
        super.onResume();

    }


    public void searchlayGone() {
        searchlay.setVisibility(View.GONE);
    }

    public void searchlayvisible() {
        searchlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public void pickupClicked(boolean onlyPickup) {


        LocationRequestedBy = "P";
       /* Bundle b = new Bundle();
        b.putString("type", "P");
        Intent i = new Intent(getActivity(), LocationSearchActivityNew.class);
        if (droplatlng != null) {
            PlacesDetail drop_obj = new PlacesDetail();
            drop_obj.setLatitude(getDroplat());
            drop_obj.setLongtitute(getDroplng());
            drop_obj.setLocation_name(getDroplocTxt());
            i.putExtra("drop_obj", drop_obj);
        }
        if (getPickuplatlng() != null) {
            PlacesDetail pickup_obj = new PlacesDetail();
            pickup_obj.setLatitude(getPickuplat());
            pickup_obj.setLongtitute(getPickuplng());
            pickup_obj.setLocation_name(getPickuplocTxt());
            b.putParcelable("pickup_obj", pickup_obj);

        }
        b.putBoolean("onlyPickup", onlyPickup);
        i.putExtras(b);
        startActivityForResult(i, TaxiUtil.LocationResult);*/ //kavi
        currentlocETxt.setFocusable(true);
        currentlocETxt.setCursorVisible(true);
        currentlocETxt.requestFocus();
        SET_FOR_PICKUP = true;
        BookTaxiNewFrag.pick_flag = true;
        BookTaxiNewFrag.drop_flag = false;
    }

    public void dropClicked(boolean onlyDrop) {


        if (!getPickuplocTxt().trim().equals("")) {
          /*  LocationRequestedBy = "D";
            Bundle b = new Bundle();
            b.putString("type", "D");
            b.putBoolean("onlyDrop", onlyDrop);
            if (getPickuplatlng() != null) {
                PlacesDetail pickup_obj = new PlacesDetail();
                pickup_obj.setLatitude(getPickuplat());
                pickup_obj.setLongtitute(getPickuplng());
                pickup_obj.setLocation_name(getPickuplocTxt());
                b.putParcelable("pickup_obj", pickup_obj);
            }

            Intent i = new Intent(getActivity(), LocationSearchActivityNew.class);
            i.putExtras(b);
            if (droplatlng != null) {
                PlacesDetail drop_obj = new PlacesDetail();
                drop_obj.setLatitude(getDroplat());
                drop_obj.setLongtitute(getDroplng());
                drop_obj.setLocation_name(getDroplocTxt());
                i.putExtra("drop_obj", drop_obj);
            }
            startActivityForResult(i, TaxiUtil.LocationResult);*/

            drop_location_edittext.setFocusable(true);
            drop_location_edittext.setCursorVisible(true);
            BookTaxiNewFrag.pick_flag = false;
            BookTaxiNewFrag.drop_flag = true;
            SET_FOR_PICKUP = false;
            drop_location_edittext.requestFocus();
        } else
            listener.requestPickupAddress();
    }

    private void intializeHome() {

//        lay_pick_fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View arg0) {
//                if (dropppp.getVisibility() == View.VISIBLE) {
//                    addFav();
//                } else {
//                    dropVisible();
//                }


//            }
//        });

//        drop_fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drop_fav.setVisibility(View.VISIBLE);
//                lay_pick_fav.setVisibility(View.VISIBLE);
//                pick_fav.setImageResource(R.drawable.plus1);
//                dropppp.setVisibility(View.GONE);
////                if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
////                    pickupp.setBackgroundResource(R.drawable.rect_home_bottom);
////                else
////                    pickupp.setBackgroundResource(R.drawable.rect_home_bottom);
//                pickup_pin.setVisibility(View.VISIBLE);
//               // pickup_pinlay.setVisibility(View.GONE);
//                pickup_drop_Sep.setVisibility(View.GONE);
//
//                searchlay.setBackgroundResource(R.color.transparent);
//
//                droplocEdt.setText("");
//            }
//        });
        drop_loc_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                LocationRequestedBy = "D";
//                Bundle b = new Bundle();
//                b.putString("type", "D");
//                Intent i = new Intent(getActivity(), LocationSearchActivityNew.class);
//                i.putExtras(b);
//                startActivityForResult(i, TaxiUtil.LocationResult);

                dropClicked(false);
            }
        });

    }

    public void dropGone() {

        //  dropppp.setVisibility(View.GONE);
    }

    public void dropVisible() {

        //   dropppp.setVisibility(View.VISIBLE);
        // pick_fav.setImageResource(R.drawable.star);

        //  SvgImage rainSVG = new SvgImage(getActivity(), R.id.pick_fav, R.raw.star);

        // pickup_pinlay.setVisibility(View.VISIBLE);
        // pickup_pin.setVisibility(View.GONE);

//        if (SessionSave.getSession("Lang", getActivity()).equals("ar"))
//            pickupp.setBackgroundResource(R.color.transparent);
//        else
//            pickupp.setBackgroundResource(R.color.transparent);

        // searchlay.setBackgroundResource(R.drawable.rect_home_bottom);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean SET_FOR_PICKUP = false;
        String result = "";
        try {
            double lat = 0.0, lng = 0.0;

            if (data != null) {
                Bundle res = data.getExtras();
                result = res.getString("param_result");
                lat = res.getDouble("lat");
                lng = res.getDouble("lng");
                SET_FOR_PICKUP = res.getBoolean("set_for_pickup");
                // Toast.makeText(getActivity(), String.valueOf(lat), Toast.LENGTH_SHORT).show();
                // currentlocTxt.setText(result);
            }
            if (SET_FOR_PICKUP && result != null && !result.trim().equals("")) {
                currentlocTxt = (result);
//                pickuplat = lat;
//                pickuplng = lng;
                LatLng p = new LatLng(lat, lng);
                setPickuplatlng(p);
//                BookTaxiNewFrag.P_latitude = lat;
//                BookTaxiNewFrag.P_longitude = lng;
                if (droplatlng == null)
                    BookTaxiNewFrag.movetoCurrentloc();
            } else if (!SET_FOR_PICKUP && result != null && !result.trim().equals("")) {
                droplocEdt = (result);
//                droplat = lat;
//                droplng = lng;
                LatLng p = new LatLng(lat, lng);
                setDroplatlng(p);
//                BookTaxiNewFrag.D_latitude = lat;
//                BookTaxiNewFrag.D_longitude = lng;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used to call the api after adding the favourite places
     */

//    private void addFav() {
//
//        final View r_view = View.inflate(getActivity(), R.layout.fav_list, null);
//        r_mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
//        r_mDialog.setContentView(r_view);
//        r_mDialog.setCancelable(true);
//        FontHelper.applyFont(getActivity(), r_mDialog.findViewById(R.id.favroot));
//        r_mDialog.show();
//        Colorchange.ChangeColor((ViewGroup) r_mDialog.findViewById(R.id.favroot), getActivity());
//        DrawableJava.draw_edittext_bg(r_mDialog.findViewById(R.id.favroot), CL.getColor(getActivity(), R.color.white), CL.getColor(getActivity(), R.color.header_bgcolor));
//        LinearLayout lay_fav_res1 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res1);
//        LinearLayout lay_fav_res2 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res2);
//        LinearLayout lay_fav_res3 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res3);
//        LinearLayout lay_fav_res4 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res4);
//        final LinearLayout other_details = (LinearLayout) r_mDialog.findViewById(R.id.other_details);
//        final EditText et_others = (EditText) r_mDialog.findViewById(R.id.et_others);
//        final TextView ok_others = (TextView) r_mDialog.findViewById(R.id.ok_others);
//        lay_fav_res1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                fav_place_type = "1";
//                set_fav();
//                r_mDialog.dismiss();
//            }
//        });
//        lay_fav_res2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                fav_place_type = "2";
//                set_fav();
//                r_mDialog.dismiss();
//            }
//        });
//        lay_fav_res3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                fav_place_type = "3";
//                set_fav();
//                r_mDialog.dismiss();
//            }
//        });
//        lay_fav_res4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                other_details.setVisibility(View.VISIBLE);
//                ok_others.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        try {
//                            if (!et_others.getText().toString().equals("")) {
//                                fav_place_type = et_others.getText().toString();
//                                other_details.setVisibility(View.GONE);
//                                et_others.setText("");
//                                String P_Address = getPickuplocTxt();
//                                //  P_Address =  getPickuplocTxt();
//                                if (P_Address.equals(""))
//                                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
//                                else {
//                                    JSONObject j = new JSONObject();
//                                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                                    j.put("p_favourite_place", P_Address);
//                                    j.put("p_fav_latitude", getPickuplat());
//                                    j.put("p_fav_longtitute", getPickuplng());
//                                    j.put("d_favourite_place", getDroplocTxt());
//                                    j.put("d_fav_latitude", getDroplat());
//                                    j.put("d_fav_longtitute", getDroplng());
//                                    j.put("fav_comments", "");
//                                    j.put("notes", "");
//                                    j.put("p_fav_locationtype", fav_place_type);
//                                    final String url = "type=add_favourite";
//                                    new addFavourite(url, j);
//                                }
//                            } else {
//                                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_other_details), "" + NC.getResources().getString(R.string.ok), "");
//                            }
//                            r_mDialog.dismiss();
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//
//
//    }
//
//    /**
//     * this method is used to call the api after setting the place
//     */
//
//    public void set_fav() {
//
//        try {
//            P_Address = getPickuplocTxt();
//            P_Address = TaxiUtil.pAddress;
//            if (P_Address.equals(""))
//                alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
//            else {
//                JSONObject j = new JSONObject();
//                j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                j.put("p_favourite_place", P_Address);
//                j.put("p_fav_latitude", getPickuplat());
//                j.put("p_fav_longtitute", getPickuplng());
//                j.put("d_favourite_place", getDroplocTxt());
//                j.put("d_fav_latitude", getDroplat());
//                j.put("d_fav_longtitute", getDroplng());
//                j.put("fav_comments", "");
//                j.put("notes", "");
//                j.put("p_fav_locationtype", fav_place_type);
//                final String url = "type=add_favourite";
//                new addFavourite(url, j);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * this class is used to set the place favouroite
//     */
//
//    public class addFavourite implements APIResult {
//        public addFavourite(final String url, JSONObject data) {
//            // TODO Auto-generated constructor stub
//            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
//        }
//
//        @Override
//        public void getResult(final boolean isSuccess, final String result) {
//
//            if (isSuccess) {
//                try {
//                    final JSONObject json = new JSONObject(result);
//                    if (json.getInt("status") == 1) {
//                        pick_fav.setImageResource(R.drawable.fav_focus);
//                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                    }
//                    if (json.getInt("status") == 2) {
//                        pick_fav.setImageResource(R.drawable.star);
//                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                    }
//                    if (json.getInt("status") == 3) {
//                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + json.getString("message"), "" + NC.getResources().getString(R.string.ok), "");
//                    }
//                } catch (final Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        CToast.ShowToast(getActivity(), result);
//                    }
//                });
//            }
//        }
//    }
    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();
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

    public void clearData() {
        pickuplatlng = null;
        droplatlng = null;
        pickuplocTxt = "";
        droplocTxt = "";
    }


    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write CAMERA permission is necessary to write event!!!");

                    alertBuilder.setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_CAMERA);

                        }
                    });


                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    //code for deny
                }
                break;
        }
    }

    public void lastknownlocation() {
        if (currentlocETxt.getText().toString().equals("")) {
            currentlocETxt.setText(SessionSave.getSession("current_address", getActivity()));
        } else {
            Toast.makeText(getActivity(), currentlocETxt.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}