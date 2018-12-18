package com.cabipassenger.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;
import com.cabipassenger.data.MapWrapperLayout;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.DrawableJava;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.LocationSearchActivity;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.ShowToast;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * this class is used to edit favoutite trips
 */

public class
EditFavouriteFrag extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    // Class members declarations.
    private GoogleMap map;
    private double latitude;
    private double longitude;
    private Marker p_marker, d_marker;
    private String Address;
    private LinearLayout CancelBtn;
    private TextView DoneBtn;
    private TextView HeadTitle;
    private EditText CommentEdt;
    private TextView pickuplocTxt;
    private TextView droplocTxt, tv_place_type;
    String selectedlocation_type = "";
    private TextView CancelTxt, back_text;
    private String Id;
    private String Lat;
    private String Lng;
    private String Place = "";
    private String notes, Place_type = "";
    private boolean Add;
    private LatLng P_HAMBURG, D_HAMBURG;
    private String DPlace = "";
    private String Dlat;
    private String Dlong;
    private String Cmnts;

    private static Dialog mDialog;
    ImageView Location_correctImg;
    AutoCompleteTextView editTextAddress;
    TextView okbtn, cancelbtn;
    EditText ed_landmark;
    LinearLayout lay_landmark;
    public Object landmark;
    LinearLayout lay_fav_res1, lay_fav_res2, lay_fav_res3, lay_fav_res4, other_details;
    TextView ok_others;
    EditText et_others;
    String fav_place_type = "";
    Dialog r_mDialog;
    static JSONObject jObj = null;
    static JSONObject json;
    JSONArray contacts = null;
    ArrayList<String> names, parcel_type;
    private static final String TAG_RESULT = "predictions";
    String jsonurl = "";
    private boolean ischkAdd = false;
    private Button saveBtn;
    int layoutheight;
    public static String vaildAddress = "";
    private MapWrapperLayout mapWrapperLayout;
    private LinearLayout edit_progress;
    private paserdata parse;
    private String json_s;
    private Dialog alertmDialog;
    private Dialog mshowDialog;
    private GoogleApiClient mGoogleApiClient;
    private float zoom = 12f;

    // Set the layout to activity.


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.editfavouritelay, container, false);
        mapWrapperLayout = (MapWrapperLayout) v.findViewById(R.id.map_relative_layout);
        mapWrapperLayout.setVisibility(View.VISIBLE);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        priorChanges(v);
        Initialize(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        if (Add) {
            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.add_favourites));
            ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
            DoneBtn.setVisibility(View.GONE);
        } else {
            ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.edit_favourites));
            ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(true);
            DoneBtn.setVisibility(View.VISIBLE);
        }
    }

    public void priorChanges(View v) {

        FontHelper.applyFont(getActivity(), v.findViewById(R.id.editfav_main));
        edit_progress = (LinearLayout) v.findViewById(R.id.edit_progress);
        CancelBtn = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        saveBtn = (Button) v.findViewById(R.id.saveBtn);
        DoneBtn = (TextView) v.findViewById(R.id.rightIconTxt);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.edit_favourites));

        ImageView iv = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    // Initialize the views on layout
    @SuppressLint("NewApi")

    public void Initialize(View v) {
        // TODO Auto-generated method stub

        tv_place_type = (TextView) v.findViewById(R.id.place_type);

        //        DoneBtn.setText(NC.getResources().getString(R.string.save));
        //        DoneBtn.setBackground(NC.getResources().getDrawable(R.drawable.back_green_to_white));
        DoneBtn.setBackground(getResources().getDrawable(R.drawable.card_delete));
        DoneBtn.setVisibility(View.GONE);
        //  CommentEdt = (EditText) v.findViewById(R.id.commentEdt);
        pickuplocTxt = (TextView) v.findViewById(R.id.pickuplocTxt);
        droplocTxt = (TextView) v.findViewById(R.id.droplocTxt);
        droplocTxt.setHint(NC.getString(R.string.droploc));
        pickuplocTxt.setHint(NC.getString(R.string.picklocation));
        tv_place_type.setHint(NC.getString(R.string.choose_place));

        pickuplocTxt.setSelected(true);
        droplocTxt.setSelected(true);
        Bundle n = this.getArguments();
        if (n != null)
            Add = n.getBoolean("ADD", false);
        // Check if add/edit operation
        try {
            if (!Add) {
                ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.edit_favourites));
                ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(true);
                Id = n.getString("Id");
                Lat = n.getString("Lat");
                Lng = n.getString("Lng");
                Place = n.getString("Place");
                DPlace = n.getString("D_place");
                Dlat = n.getString("D_lat");
                Dlong = n.getString("D_long");
                Cmnts = n.getString("Cmt");
                Place_type = n.getString("Place_type");
                switch (Place_type) {
                    case "1":
                        tv_place_type.setText("" + NC.getResources().getString(R.string.home));
                        break;
                    case "2":
                        tv_place_type.setText("" + NC.getResources().getString(R.string.office));
                        break;
                    case "3":
                        tv_place_type.setText("" + NC.getResources().getString(R.string.airport));
                        break;
                    default:
                        tv_place_type.setText("" + Place_type);
                        break;
                }
                notes = n.getString("notes");
                DoneBtn.setVisibility(View.VISIBLE);


//                getFragmentManager().addOnBackStackChangedListener(
//                        new FragmentManager.OnBackStackChangedListener() {
//                            @Override
//                            public void onBackStackChanged() {
//                                // find your fragment and do updates
//
//                            }
//                        });


            } else {
                ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.add_favourites));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //   CommentEdt.setText(Cmnts);
        // Initialize the google map and place the two marker for pickup and drop location. Also shows the pickup and drop location in text views.

        // To cancel the ADD/EDIT operation and move tto list activity.
//        CancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                getActivity().finish();
//            }
//        });
        // This listener used to show the dialog to get the pickup location using google place API.
        pickuplocTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pickuplocTxt.setEnabled(false);
                selectedlocation_type = "Pick";
                Bundle b = new Bundle();
                b.putString("type", "P");
                Intent i = new Intent(getActivity(), LocationSearchActivity.class);
                i.putExtras(b);
                startActivityForResult(i, TaxiUtil.LocationResult);
                //  startActivityForResult(new Intent(getActivity(), LocationSearchActivity.class), TaxiUtil.LocationResult);
            }
        });
        // This listener used to show the dialog to get the drop location using google place API.
        droplocTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                droplocTxt.setEnabled(false);
                selectedlocation_type = "Drop";
                //  SearchLocationPopup();


                Bundle b = new Bundle();
                b.putString("type", "D");
                Intent i = new Intent(getActivity(), LocationSearchActivity.class);
                i.putExtras(b);
                startActivityForResult(i, TaxiUtil.LocationResult);
            }
        });
        // This listener used to show the dialog to get the favorite place type.
        tv_place_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final View r_view = View.inflate(getActivity(), R.layout.fav_list, null);
                r_mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                r_mDialog.setContentView(r_view);
                r_mDialog.setCancelable(true);
                r_mDialog.show();

                FontHelper.applyFont(getActivity(), r_mDialog.findViewById(R.id.favroot));
                Colorchange.ChangeColor((ViewGroup) r_mDialog.findViewById(R.id.favroot), getActivity());
                DrawableJava.draw_edittext_bg(r_mDialog.findViewById(R.id.favroot), CL.getColor(getActivity(), R.color.white), CL.getColor(getActivity(), R.color.header_bgcolor));
                lay_fav_res1 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res1);
                lay_fav_res2 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res2);
                lay_fav_res3 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res3);
                lay_fav_res4 = (LinearLayout) r_mDialog.findViewById(R.id.lay_fav_res4);
                other_details = (LinearLayout) r_mDialog.findViewById(R.id.other_details);
                et_others = (EditText) r_mDialog.findViewById(R.id.et_others);
                ok_others = (TextView) r_mDialog.findViewById(R.id.ok_others);
                lay_fav_res1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        fav_place_type = "1";
                        set_fav();
                        r_mDialog.dismiss();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(lay_fav_res1.getWindowToken(), 0);
                    }
                });
                lay_fav_res2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        fav_place_type = "2";
                        set_fav();
                        r_mDialog.dismiss();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(lay_fav_res2.getWindowToken(), 0);
                    }
                });
                lay_fav_res3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        fav_place_type = "3";
                        set_fav();
                        r_mDialog.dismiss();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(lay_fav_res3.getWindowToken(), 0);
                    }
                });
                lay_fav_res4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        other_details.setVisibility(View.VISIBLE);
                        ok_others.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                if (!et_others.getText().toString().equals("")) {
                                    fav_place_type = et_others.getText().toString();
                                    other_details.setVisibility(View.GONE);
                                    et_others.setText("");
                                    Place_type = fav_place_type;
                                    switch (fav_place_type) {
                                        case "1":
                                            tv_place_type.setText("" + NC.getResources().getString(R.string.home));
                                            break;
                                        case "2":
                                            tv_place_type.setText("" + NC.getResources().getString(R.string.office));
                                            break;
                                        case "3":
                                            tv_place_type.setText("" + NC.getResources().getString(R.string.airport));
                                            break;
                                        default:
                                            tv_place_type.setText("" + fav_place_type);
                                            break;
                                    }
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(ok_others.getWindowToken(), 0);
                                    r_mDialog.dismiss();
                                } else {
                                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_other_details), "" + NC.getResources().getString(R.string.ok), "");
                                }
                            }
                        });
                    }
                });
            }
        });

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        // After entering favorite place details perform the ADD/EDIT API call.to update into server.
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url;
                try {
                    JSONObject j = new JSONObject();
                    if (Place_type.equals("null") || Place_type.equalsIgnoreCase(""))
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_other_details), "" + NC.getResources().getString(R.string.ok), "");
                    else if (Place.length() == 0)
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_pickup_location), "" + NC.getResources().getString(R.string.ok), "");
                        //                    else if (DPlace.length() == 0)
                        //                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.select_the_drop_location), "" + NC.getResources().getString(R.string.ok), "");
                    else {
                        if (!Add) {
                            j.put("p_favourite_id", Id);
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("p_favourite_place", Place);
                            j.put("p_fav_latitude", Lat);
                            j.put("p_fav_longtitute", Lng);
                            j.put("d_favourite_place", DPlace);
                            j.put("d_fav_latitude", Dlat);
                            j.put("d_fav_longtitute", Dlong);
                            j.put("fav_comments", "");
                            j.put("notes", notes);
                            j.put("p_fav_locationtype", Place_type);
                            url = "type=edit_favourite";
                        } else {
                            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
                            j.put("p_favourite_place", Place);
                            j.put("p_fav_latitude", Lat);
                            j.put("p_fav_longtitute", Lng);
                            j.put("d_favourite_place", DPlace);
                            j.put("d_fav_latitude", Dlat);
                            j.put("d_fav_longtitute", Dlong);
                            j.put("fav_comments", "");
                            j.put("notes", notes);
                            j.put("p_fav_locationtype", Place_type);
                            url = "type=add_favourite";
                        }
                        new FavPlace(url, j);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        edit_progress.setVisibility(View.GONE);
    }

    public void deleteonClick() {
        try {
            String url;
            JSONObject j = new JSONObject();
            j.put("passenger_id", "" + SessionSave.getSession("Id", getActivity()));
            j.put("p_favourite_id", "" + Id);
            url = "type=delete_favourite";
            new DelFavPlace(url, j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            alertmDialog.show();

            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id), getActivity());
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

    /**
     * This function helps to get the favorite place type sting in different language with selected index.
     */

    public void set_fav() {

        Place_type = fav_place_type;
        switch (fav_place_type) {
            case "1":
                tv_place_type.setText("" + NC.getResources().getString(R.string.home));
                break;
            case "2":
                tv_place_type.setText("" + NC.getResources().getString(R.string.office));
                break;
            case "3":
                tv_place_type.setText("" + NC.getResources().getString(R.string.airport));
                break;
            default:
                tv_place_type.setText("" + fav_place_type);
                break;
        }
    }

    /**
     * get onactivity result values from LocationSearchActivity
     *
     * @param requestCode code that sent by this class to Location search activity
     * @param resultCode  code that receive from Location search activity to this class
     * @param data        data receive from this Location search activity which contain This function helps to get the favorite place type sting in different language with selected index.LatLng and location address
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickuplocTxt.setEnabled(true);
        droplocTxt.setEnabled(true);
        String result = "";
        try {
            double lat = 0.0, lng = 0.0;
            if (requestCode == TaxiUtil.LocationResult) {
                Bundle res = data.getExtras();
                result = res.getString("param_result");
                lat = res.getDouble("lat");
                lng = res.getDouble("lng");

            }
            if (selectedlocation_type.equals("Pick") && result != null && !result.trim().equals("")) {
                pickuplocTxt.setText("");
                pickuplocTxt.append(result);
                isMarqueed(Place, pickuplocTxt.getWidth(), pickuplocTxt);
                TaxiUtil.Latitude = lat;
                TaxiUtil.Longitude = lng;
                Place = result;
                Lat = String.valueOf(TaxiUtil.Latitude);
                Lng = String.valueOf(TaxiUtil.Longitude);
                vaildAddress = "";
                set_location(Place, TaxiUtil.Latitude, TaxiUtil.Longitude);
            } else if (selectedlocation_type.equals("Drop") && result != null && !result.trim().equals("")) {
                droplocTxt.setText(result);
                TaxiUtil.Latitude = lat;
                TaxiUtil.Longitude = lng;
                DPlace = result;
                Dlat = String.valueOf(TaxiUtil.Latitude);
                Dlong = String.valueOf(TaxiUtil.Longitude);
                vaildAddress = "";
                set_location(DPlace, TaxiUtil.Latitude, TaxiUtil.Longitude);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setPin();
    }

    private void setPin() {
        try {
            if (mGoogleApiClient.isConnected() && map != null) {
                if (Add) {
                    if (mGoogleApiClient.isConnected()) {
                        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                    P_HAMBURG = new LatLng(latitude, longitude);
                    //                D_HAMBURG = new LatLng(latitude + 0.0015, longitude + 0.0015);
                    HeadTitle.setText(NC.getResources().getString(R.string.add_favourites));
                    LatLng ll = new LatLng(latitude, longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(P_HAMBURG, zoom));
//                if (ll != null && map != null){
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 17f));
//                    System.out.println("___add__m" + ll + "__" + map);
//                }
                } else {
                    P_HAMBURG = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lng));
                    map.clear();
                    if (Dlat.length() > 0 && Dlong.length() > 0) {
                        D_HAMBURG = new LatLng(Double.parseDouble(Dlat), Double.parseDouble(Dlong));
                        d_marker = map.addMarker(new MarkerOptions().position(D_HAMBURG).title("" + NC.getResources().getString(R.string.fav_drop_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).draggable(true));
                        d_marker.setDraggable(false);
                        //getLocation(D_HAMBURG, 2, d_marker);
                    }
                    droplocTxt.setText("");
                    pickuplocTxt.setText("");
                    pickuplocTxt.append(Place);
                    isMarqueed(Place, pickuplocTxt.getWidth(), pickuplocTxt);
                    droplocTxt.append(DPlace);
                    HeadTitle.setText(NC.getResources().getString(R.string.edit_favourites));
                    p_marker = map.addMarker(new MarkerOptions().position(P_HAMBURG).title("" + NC.getResources().getString(R.string.fav_pick_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(true));
                    p_marker.setDraggable(false);
                    // getLocation(P_HAMBURG, 1, p_marker);
                    adjustBoundsAndMove();
                }
              //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(P_HAMBURG, zoom));

                map.setMyLocationEnabled(true);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.setOnMapClickListener(this);
                map.setOnMapLongClickListener(this);
                // map.setOnMarkerDragListener(this);
            }
        } catch (NullPointerException e) {
            // TODO: handle exception
            e.printStackTrace();
            getActivity().onBackPressed();
        }
    }


//    public void  onWindowFocusChanged(boolean hasFocus) {
//        // TODO Auto-generated method stub
//     //   super.onWindowFocusChanged(hasFocus);
////  System.out.println("...111Height..."+mainLayout.getMeasuredHeight());
//
//        isMarqueed("I am fine here. How r u", textView.getWidth(), textView);
//        isMarqueed("I am fine", textView.getWidth(), textView);
//    }


    private boolean isMarqueed(String text, int textWidth, TextView tv) {
        Paint testPaint = new Paint();
        testPaint.set(tv.getPaint());
        boolean isMarquee = true;
        if (textWidth > 0) {
            int availableWidth = (int) (textWidth - tv.getPaddingLeft() - tv.getPaddingRight() - testPaint.measureText(text));
           // System.out.println("...available width..." + availableWidth);
//        tv.setText(text);
            isMarquee = false;
        }
        return isMarquee;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mapWrapperLayout.init(map, getPixelsFromDp(getActivity(), 39 + 20), false, null);
        DoneBtn.post(new Runnable() {
            @Override
            public void run() {
                if (!Add)
                    layoutheight = DoneBtn.getHeight() + 40;
                else
                    layoutheight = DoneBtn.getHeight() + 80;
                map.setPadding(0, 0, 0, layoutheight);
            }
        });
        setPin();

    }

    /**
     * this class is used to set favourite place
     */

    private class FavPlace implements APIResult {
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

                        getActivity().onBackPressed();
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
                        CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    /**
     * This class used to delete the favorite place details
     * <p>
     * This class used to delete the favorite place details
     * </p>
     *
     * @author developer
     */
    private class DelFavPlace implements APIResult {
        private String Message;

        public DelFavPlace(String url, JSONObject data) {
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
                    if (json.getInt("status") == 1) {
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                        // Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
                        ShowToast.center(getActivity(), Message);
                        getActivity().onBackPressed();
                    } else {
                        ShowToast.center(getActivity(), Message);
                        //  Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
                        alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + Message, "" + NC.getResources().getString(R.string.ok), "");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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

    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub
    }

    // Using this default method, User able to drag the marker any where.whenever the marker position changes ,it automatically calculate the address.
    @Override
    public void onMarkerDragEnd(Marker marker) {
        // TODO Auto-generated method stub
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        LatLng position = marker.getPosition();
        Address = "";
        try {
            List<android.location.Address> addresses = geoCoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses.size() > 0) {
                for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                    Address += addresses.get(0).getAddressLine(index) + " ";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
        }
        String latlng = marker.getPosition().toString();
        String[] splitlat = latlng.split(":");
        String latandlong = splitlat[1];
        if (latandlong.contains("(") && latandlong.contains(")")) {
            latandlong = latandlong.replace("(", "");
            latandlong = latandlong.replace(")", "");
        }
        String[] individuallat = latandlong.split(",");
        latitude = Double.parseDouble(individuallat[0]);
        longitude = Double.parseDouble(individuallat[1]);
        LatLng HAMBURG = new LatLng(latitude, longitude);
        try {
            adjustBoundsAndMove();
            // map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, zoom));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (marker.equals(p_marker)) {
            Place = Address;
            Lat = "" + latitude;
            Lng = "" + longitude;
        }
        if (marker.equals(d_marker)) {
            DPlace = Address;
            Dlat = "" + latitude;
            Dlong = "" + longitude;
        }
        pickuplocTxt.setText("");
        droplocTxt.setText("");
        pickuplocTxt.append(Place);
        isMarqueed(Place, pickuplocTxt.getWidth(), pickuplocTxt);
        droplocTxt.append(DPlace);
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onDestroy() {

        if (mshowDialog != null && mshowDialog.isShowing()) {
            mshowDialog.dismiss();
            mshowDialog = null;

        }
        TaxiUtil.mActivitylist.remove(this);
        super.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        return false;
    }

    // Update the UI(markers and location details) with changed marker position.
    public void set_location(String location, Double lat, Double lng) {

        try {
            if (selectedlocation_type.equals("Pick")) {
                pickuplocTxt.setText("");
                pickuplocTxt.append(location);
                isMarqueed(Place, pickuplocTxt.getWidth(), pickuplocTxt);
                LatLng HAMBURG = new LatLng(lat, lng);
                if (p_marker != null)
                    p_marker.remove();
                p_marker = map.addMarker(new MarkerOptions().position(HAMBURG).title("" + NC.getResources().getString(R.string.fav_pick_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_green)).draggable(true));
                p_marker.setDraggable(false);
                adjustBoundsAndMove();
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    builder.include(HAMBURG);
//                LatLngBounds bounds = builder.build();
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, zoom));
            } else if (selectedlocation_type.equals("Drop")) {
                droplocTxt.setText("");
                droplocTxt.append(location);
                LatLng HAMBURG = new LatLng(lat, lng);
                if (d_marker != null)
                    d_marker.remove();
                d_marker = map.addMarker(new MarkerOptions().position(HAMBURG).title("" + NC.getResources().getString(R.string.fav_drop_location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_red)).draggable(true));
                p_marker.setDraggable(false);
                adjustBoundsAndMove();
            }


        } catch (Exception e) {
        }
    }

    private void adjustBoundsAndMove() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (p_marker != null && p_marker.getPosition() != null)
            builder.include(p_marker.getPosition());
        if (d_marker != null && d_marker.getPosition() != null)
            builder.include(d_marker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 500; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,200);
        map.moveCamera(cu);
    }

    // This popup helps to search for pickup and drop location using Google PlacefavAdd API.
    public void SearchLocationPopup() {

        final View view = View.inflate(getActivity(), R.layout.search_location_popup, null);
        mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
        mDialog.setContentView(view);
        FontHelper.applyFont(getActivity(), mDialog.findViewById(R.id.search_main));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        mDialog.setCancelable(true);
        if (!mDialog.isShowing())
            mDialog.show();
        pickuplocTxt.setEnabled(true);
        editTextAddress = (AutoCompleteTextView) mDialog.findViewById(R.id.autoCompleteTextView1);
        //        editTextAddress.setAdapter(new AutoCompleteAdapter(this));
        Location_correctImg = (ImageView) mDialog.findViewById(R.id.location_correctImg);
        okbtn = (TextView) mDialog.findViewById(R.id.okbtn);
        cancelbtn = (TextView) mDialog.findViewById(R.id.cancelbtn);
        cancelbtn.setVisibility(View.GONE);
        lay_landmark = (LinearLayout) mDialog.findViewById(R.id.lay_landmark);
        ed_landmark = (EditText) mDialog.findViewById(R.id.ed_landmark);
        lay_landmark.setVisibility(View.GONE);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!vaildAddress.equals("")) {
                        if (selectedlocation_type.equals("Pick") && ischkAdd) {
                            if (editTextAddress.getText().toString().trim().length() > 0) {
                                Place = editTextAddress.getText().toString();
                                Lat = String.valueOf(TaxiUtil.Latitude);
                                Lng = String.valueOf(TaxiUtil.Longitude);
                                vaildAddress = "";
                                set_location(Place, TaxiUtil.Latitude, TaxiUtil.Longitude);
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                                mDialog.dismiss();
                            }
                        } else if (selectedlocation_type.equals("Drop") && ischkAdd) {
                            if (editTextAddress.getText().toString().trim().length() > 0) {
                                DPlace = editTextAddress.getText().toString();
                                vaildAddress = "";
                                Dlat = String.valueOf(TaxiUtil.Latitude);
                                Dlong = String.valueOf(TaxiUtil.Longitude);
                                set_location(DPlace, TaxiUtil.Latitude, TaxiUtil.Longitude);
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                                mDialog.dismiss();
                            }
                        } else {
                            vaildAddress = "";
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                            mDialog.dismiss();
                            mDialog.dismiss();
                        }
                    } else {
                        CToast.ShowToast(getActivity(), "" + "Invaid Address");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vaildAddress = "";
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                mDialog.dismiss();
            }
        });
        editTextAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        if (!vaildAddress.equals("")) {
                            if (selectedlocation_type.equals("Pick") && ischkAdd) {
                                if (editTextAddress.getText().toString().trim().length() > 0) {
                                    vaildAddress = "";
                                    Place = editTextAddress.getText().toString();
                                    Lat = String.valueOf(TaxiUtil.Latitude);
                                    Lng = String.valueOf(TaxiUtil.Longitude);
                                    set_location(Place, TaxiUtil.Latitude, TaxiUtil.Longitude);
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                                    mDialog.dismiss();
                                }
                            } else if (selectedlocation_type.equals("Drop") && ischkAdd) {
                                if (editTextAddress.getText().toString().trim().length() > 0) {
                                    vaildAddress = "";
                                    DPlace = editTextAddress.getText().toString();
                                    Dlat = String.valueOf(TaxiUtil.Latitude);
                                    Dlong = String.valueOf(TaxiUtil.Longitude);
                                    set_location(DPlace, TaxiUtil.Latitude, TaxiUtil.Longitude);
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                                    mDialog.dismiss();
                                }
                            } else {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                                mDialog.dismiss();
                                mDialog.dismiss();
                                vaildAddress = "";
                            }
                        } else {
                            CToast.ShowToast(getActivity(), "" + "Invaid Address");
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        editTextAddress.addTextChangedListener(new TextWatcher() {
            String search_text[];

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                ischkAdd = false;
                search_text = editTextAddress.getText().toString().split(",");
                jsonurl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + search_text[0].replace(" ", "%20") + "&components=country:"
                        + SplashActivity.CURRENT_COUNTRY_CODE + "&radius=1000&sensor=true&key=AIzaSyBNauDRNOX6bhZWGWK09gjzyJ_bAq1sIKY&components=&language=" + SessionSave.getSession("Lang", getActivity());
                //jsonurl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + search_text[0].replace(" ", "%20") + "&radius=1000&sensor=true&key=" + SessionSave.getSession("android_google_api_key", getActivity()) + "&components=&language=" + SessionSave.getSession("Lang", getActivity());
              //  System.out.println("------" + jsonurl);

                if (search_text.length <= 3) {
                    names = new ArrayList<String>();
                    if (parse != null)
                        parse.cancel(true);
                    parse = new paserdata();
                    parse.execute();
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
    }

    private class AutoCompleteAdapter extends ArrayAdapter<Address> implements Filterable {
        private final LayoutInflater mInflater;
        private final Geocoder mGeocoder;
        private final StringBuilder mSb = new StringBuilder();
        private final Context context;

        public AutoCompleteAdapter(final Context context) {

            super(context, -1);
            mInflater = LayoutInflater.from(context);
            mGeocoder = new Geocoder(context, Locale.ENGLISH);
            this.context = context;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            final TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = (TextView) mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    editTextAddress.setText("");

                    editTextAddress.append(createFormattedAddressFromAddress(getItem(position)));
                    TaxiUtil.Address = editTextAddress.getText().toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            editTextAddress.dismissDropDown();
                        }
                    }, 1500);
                }
            });
            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
            tv.setText(createFormattedAddressFromAddress(getItem(position)));
            tv.setTextColor(context.getResources().getColor(R.color.header_bgcolor));
            return tv;
        }

        private String createFormattedAddressFromAddress(final Address address) {

            ischkAdd = true;
            mSb.setLength(0);
            final int addressLineSize = address.getMaxAddressLineIndex();
            for (int i = 0; i < addressLineSize; i++) {
                mSb.append(address.getAddressLine(i));
                if (i != addressLineSize - 1) {
                    mSb.append(", ");
                }
            }
            return mSb.toString();
        }

        @Override
        public Filter getFilter() {

            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(final CharSequence constraint) {

                    List<Address> addressList = null;
                    if (constraint != null) {
                        try {
                            addressList = mGeocoder.getFromLocationName((String) constraint, 5);
                        } catch (IOException e) {
                        }
                    }
                    if (addressList == null) {
                        addressList = new ArrayList<Address>();
                    }
                    final FilterResults filterResults = new FilterResults();
                    filterResults.values = addressList;
                    filterResults.count = addressList.size();
                    return filterResults;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(final CharSequence contraint, final FilterResults results) {

                    clear();
                    for (Address address : (List<Address>) results.values) {
                        add(address);
                    }
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }

                @Override
                public CharSequence convertResultToString(final Object resultValue) {

                    if (!resultValue.equals("")) {
                        TaxiUtil.Latitude = ((Address) resultValue).getLatitude();
                        TaxiUtil.Longitude = ((Address) resultValue).getLongitude();
                        if (editTextAddress.getText().toString().trim().length() > 0) {
                        }
                    }
                    return resultValue == null ? "" : ((Address) resultValue).getAddressLine(0);
                }
            };
            return myFilter;
        }
    }

    /**
     * this class is used to get address from autocomplete places api
     */

    public class paserdata extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                json = GetAddress(jsonurl.toString());
                if (json != null) {
                    names.clear();
                    contacts = json.getJSONArray(TAG_RESULT);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String description = c.getString("description");
                        names.add(description);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                ArrayAdapter<String> adp;
                adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        final TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(getResources().getColor(R.color.header_bgcolor));
                        text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                editTextAddress.setText("");
                                editTextAddress.append(text.getText().toString());
                                getLocationFromAddress(text.getText().toString());

                                editTextAddress.dismissDropDown();
                            }
                        });
                        return view;
                    }
                };
                editTextAddress.setAdapter(adp);
                adp.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    /**
     * this method is used to get lat lng from geocoder for given address
     *
     * @param strAddress this params has passed the given address
     */

    public void getLocationFromAddress(String strAddress) {

        ischkAdd = true;
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            Address location = address.get(0);
         //   System.out.println("Address lat:" + location.getLatitude() + "lng:" + location.getLongitude() + ischkAdd);
            location.getLatitude();
            location.getLongitude();
            if (!address.equals("")) {
                TaxiUtil.Latitude = location.getLatitude();
                TaxiUtil.Longitude = location.getLongitude();
                if (editTextAddress.getText().toString().trim().length() > 0) {
                    TaxiUtil.Address = editTextAddress.getText().toString();
                    vaildAddress = editTextAddress.getText().toString();
                }
            } else {
                TaxiUtil.Latitude = 0.0;
                TaxiUtil.Longitude = 0.0;
                TaxiUtil.Address = "";
            }
        } catch (Exception e) {
            // TODO: handle exception
            TaxiUtil.Latitude = 0.0;
            TaxiUtil.Longitude = 0.0;
            TaxiUtil.Address = "";
        }
    }

    /**
     * this method is used to pass the url as parameter
     */

    private JSONObject GetAddress(String Url) {

        try {
          //  System.out.println("address size:called" + Url + "****");

            java.net.URL url = new java.net.URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //  conn.setRequestMethod("POST");
//            byte[] postDataBytes = j.toString().getBytes("UTF-8");
//            conn.getOutputStream().write(postDataBytes);
            InputStream is = new BufferedInputStream(conn.getInputStream());


            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                json_s = sb.toString();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet httppost = new HttpGet(Url);
//            HttpResponse response = httpclient.execute(httppost);
//            String jsonResult = Utility.inputStreamToString(response.getEntity().getContent()).toString();
            JSONObject json = new JSONObject(json_s);
            return json;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }


    /**
     * Get the google map pixels from xml density independent pixel.
     */

    public static int getPixelsFromDp(final Context context, final float dp) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * To prevent window leakage error close all dialogs before activity stops.
     */

    @Override
    public void onStop() {

        if (alertmDialog != null)

            if (alertmDialog.isShowing())
                alertmDialog.dismiss();
        if (mDialog != null)
            if (mDialog.isShowing())
                mDialog.dismiss();
        if (r_mDialog != null)
            if (r_mDialog.isShowing())
                r_mDialog.dismiss();

        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(true);
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.menu_favourites));

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        super.onStop();
    }
}