package com.cabipassenger.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.SplashActivity;
import com.cabipassenger.features.ApproximateCalculation;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.route.Route;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FindDistance;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.LocationSearchActivity;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by developer on 4/30/16.
 * Class for FareEstimate fragment
 */
public class FareEstimateFrag extends Fragment {
    private TextView fare_abovedetails;
    private TextView fare_belowdetails;
    private TextView fare_basefare;
    private TextView fare_minimum_ppl;
    private TextView fare_minimum;
    private TextView fare_eta;
    private String car_model;
    private double E_time;
    private String selectedlocation_type = "";
    private TextView pickuplocTxt;
    private TextView droplocTxt;
    private String Place;
    private String Lat, Lng;
    private String DPlace;
    private String Dlat, Dlong;
    private String Pick, Drop;
    private Double approx_fare = 0.0;
    private int availablecarcount;
    NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
    DecimalFormat df = new DecimalFormat("####0.00");
    private ImageView car_model_img;
    private TextView model_name;
    private TextView belowmin;
    private TextView abovemin;
    private LinearLayout ll;
    String mileRkM = "";
    private double min_km=0.0;
    private double min_fare=0.0;

    @Override
    public void onDetach() {
        try {
            ((MainHomeFragmentActivity) getActivity()).homePage_title();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.taxi_fare_details, container, false);
        Bundle b = this.getArguments();
        Init(v);
        Colorchange.ChangeColor((ViewGroup)v,getActivity());

        if (SessionSave.getSession("Metric", getActivity()).equals("KM"))
            mileRkM = "KM";
        else
            mileRkM = "Miles";
        if (b != null) {
            car_model = b.getString("car");
            Pick = b.getString("pick");
            Drop = b.getString("drop");
            pickuplocTxt.setText(Pick);
            droplocTxt.setText(Drop);
            E_time = BookTaxiNewFrag.E_time;
            fare_eta.setText(E_time + " " + NC.getResources().getString(R.string.Mins));
            setValues();
            String car_select = BookTaxiNewFrag.selectModel;
            try {
                final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
               // Log.w("focus_image","focus_image"+car_select+"=="+car_select+array.getJSONObject(Integer.parseInt(car_select)-1).getString("focus_image"));
                Picasso.with(getActivity()).load(array.getJSONObject(Integer.parseInt(car_select)-1).getString("focus_image")).error(R.drawable.car2_unfocus).placeholder(R.drawable.car2_unfocus).into(car_model_img);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (car_select.equals("1"))
//                car_model_img.setImageResource(R.drawable.car11_focus);
//            else if (car_select.equals("2"))
//                car_model_img.setImageResource(R.drawable.car33_focus);
//            else if (car_select.equals("3"))
//                car_model_img.setImageResource(R.drawable.car22_focus);
//            else if (car_select.equals("4"))
//                car_model_img.setImageResource(R.drawable.car44_focus);

            if (Drop.trim().length() == 0) {
                droplocTxt.setEnabled(false);
                selectedlocation_type = "Drop";
                //  SearchLocationPopup();
                Bundle bb = new Bundle();
                bb.putString("type", "D");
                Intent i = new Intent(getActivity(), LocationSearchActivity.class);
                i.putExtras(bb);
                startActivityForResult(i, TaxiUtil.LocationResult);

                //  startActivityForResult(new Intent(getActivity(), LocationSearchActivity.class), TaxiUtil.LocationResult);

            } else {
                ll.setVisibility(View.VISIBLE);
                if (!SplashActivity.isBUISNESSKEY) {
                    double Driverdistance = 0.5 + FindDistance.distance(BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(),
                            BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng(),
                            SessionSave.getSession("Metric_type", getActivity()));
                    new Approximate_distance(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), false).execute();
                } else
                    new FindApproxDistance(getActivity(), BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(),
                            BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng(), false);
            }
            //  Toast.makeText(getActivity(), Pick, Toast.LENGTH_SHORT).show();
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getResources().getString(R.string.fare_estimate));

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);

    }
    /**
     * get onactivity result values from LocationSearchActivity
     * @param requestCode code that sent by this class to Location search activity
     * @param resultCode code that recevie from Location search activity to this class
     * @param data  data receive from this Location search activity which contain LatLng and location address
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
                if (data == null) {
                    ((MainHomeFragmentActivity) getActivity()).homePage();
                } else {
                    ll.setVisibility(View.VISIBLE);
                    Bundle res = data.getExtras();
                    result = res.getString("param_result");
                    lat = res.getDouble("lat");
                    lng = res.getDouble("lng");
                }
                // Toast.makeText(getActivity(), String.valueOf(lat), Toast.LENGTH_SHORT).show();
                // currentlocTxt.setText(result);
            }
            if (selectedlocation_type.equals("Pick") && result != null && !result.trim().equals("")) {
                pickuplocTxt.setText(result);
                TaxiUtil.Latitude = lat;
                TaxiUtil.Longitude = lng;
//                BookTaxiNewFrag.P_latitude = lat;
//                BookTaxiNewFrag.P_longitude = lng;
                Place = result;
                Lat = String.valueOf(TaxiUtil.Latitude);
                Lng = String.valueOf(TaxiUtil.Longitude);
                BookTaxiNewFrag.sf.setPickuplocTxt(result);
                BookTaxiNewFrag.sf.setPickuplatlng(new LatLng(lat,lng));

                BookTaxiNewFrag.movetoCurrentloc();
                //  fare_eta.setText("");
                fare_basefare.setText("");

                FindNearestlocal(lat, lng);
                // vaildAddress = "";
                // set_location(Place, TaxiUtil.Latitude, TaxiUtil.Longitude);
                if (!SplashActivity.isBUISNESSKEY) {
                    double Driverdistance = 0.5 + FindDistance.distance( lat, lng, BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng(),SessionSave.getSession("Metric_type", getActivity()));
                    new Approximate_distance(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), false).execute();
                } else
                    new FindApproxDistance(getActivity(), lat, lng, BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng(), false);
            } else if (selectedlocation_type.equals("Drop") && result != null && !result.trim().equals("")) {
                droplocTxt.setText(result);
                TaxiUtil.Latitude = lat;
                TaxiUtil.Longitude = lng;
//                BookTaxiNewFrag.D_latitude = lat;
//                BookTaxiNewFrag.D_longitude = lng;
                DPlace = result;
                Dlat = String.valueOf(TaxiUtil.Latitude);
                Dlong = String.valueOf(TaxiUtil.Longitude);
                BookTaxiNewFrag.sf.setDroplocTxt(result);
                BookTaxiNewFrag.sf.setDroplatlng(new LatLng(lat,lng));
                BookTaxiNewFrag.sf.dropVisible();
                //  fare_eta.setText("");
                fare_basefare.setText("");

                if (!SplashActivity.isBUISNESSKEY) {
                    double Driverdistance = 0.5 + FindDistance.distance(BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(), lat, lng, SessionSave.getSession("Metric_type", getActivity()));
                    new Approximate_distance(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), false).execute();
                } else
                    new FindApproxDistance(getActivity(), BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(), lat, lng, false);
                //  vaildAddress = "";
                // set_location(DPlace, TaxiUtil.Latitude, TaxiUtil.Longitude);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initiate view for fare estimate fragment
     * @param v-->root view of the fragment
     */
    private void Init(View v) {
        try {
            ll = (LinearLayout) v.findViewById(R.id.fare_id);
            FontHelper.applyFont(getActivity(), ll);
            v.findViewById(R.id.fare_id).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
            fare_abovedetails = (TextView) v.findViewById(R.id.fare_abovedetails);
            fare_belowdetails = (TextView) v.findViewById(R.id.fare_belowdetails);
            fare_basefare = (TextView) v.findViewById(R.id.fare_basefare);
            fare_minimum_ppl = (TextView) v.findViewById(R.id.fare_minimum_ppl);
            fare_minimum = (TextView) v.findViewById(R.id.fare_minimum);
            car_model_img = (ImageView) v.findViewById(R.id.car_model_img);
            model_name = (TextView) v.findViewById(R.id.model_name);
            fare_eta = (TextView) v.findViewById(R.id.fare_eta);
            belowmin = (TextView) v.findViewById(R.id.belowmin);
            abovemin = (TextView) v.findViewById(R.id.abovemin);
            pickuplocTxt = (TextView) v.findViewById(R.id.currentlocTxt);
            droplocTxt = (TextView) v.findViewById(R.id.droplocEdt);

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
                    // startActivityForResult(new Intent(getActivity(), LocationSearchActivity.class), TaxiUtil.LocationResult);
                }
            });
            // This listener used to show the dialog to get the drop location using google place API.
            droplocTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    droplocTxt.setEnabled(false);
                    selectedlocation_type = "Drop";
                    Bundle b = new Bundle();
                    b.putString("type", "D");
                    Intent i = new Intent(getActivity(), LocationSearchActivity.class);
                    i.putExtras(b);
                    startActivityForResult(i, TaxiUtil.LocationResult);

                    //  SearchLocationPopup();
                    // startActivityForResult(new Intent(getActivity(), LocationSearchActivity.class), TaxiUtil.LocationResult);
                }
            });


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * set values for the whole view
     * values for this class are get from session named as Server_Response
     */
    void setValues() {
        try {
            String Server_Response = SessionSave.getSession("Server_Response", getContext());
           // System.out.println("___" + Server_Response);
            final JSONObject json = new JSONObject(Server_Response);
            final JSONObject farejson = json.getJSONObject("fare_details");
            String base_fare = farejson.getString("base_fare");
           // min_fare = farejson.getString("min_fare");
            min_km = Double.parseDouble(farejson.getString("min_km"));
            min_fare =Double.parseDouble(farejson.getString("min_fare"));
            
            String model_size = farejson.getString("model_size");
            String below_km = farejson.getString("below_km");
            String above_km = farejson.getString("above_km");
            String below_above_km = farejson.getString("below_above_km");
            model_name.setText(farejson.getString("model_name"));

            if (car_model.equals(farejson.getString("model_id"))) {
                fare_minimum.setText(SessionSave.getSession("Currency", getActivity()) + " " + min_fare);
                fare_minimum_ppl.setText(model_size + " " + NC.getResources().getString(R.string.ppl));
//                fare_basefare.setText("" + SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare));


                fare_basefare.setText("" + SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare-5)+" - "+ SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare+5));

                fare_belowdetails.setText(SessionSave.getSession("Currency", getActivity()) + " " + below_km + "/" + mileRkM);
                fare_abovedetails.setText(SessionSave.getSession("Currency", getActivity()) + " " + above_km + "/" + mileRkM);
                belowmin.setText(getString(R.string.below) + " " + below_above_km + " " + SessionSave.getSession("Metric", getActivity()));
                abovemin.setText(getString(R.string.above) + " " + below_above_km + " " + SessionSave.getSession("Metric", getActivity()));
            } else {
                //   alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.no_taxi), "" + NC.getResources().getString(R.string.ok), "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Find nearest driver to the corresponding latlong of the passenger
     * @param P_latitude
     * @param P_longitude
     */
    public void FindNearestlocal(final double P_latitude, final double P_longitude) {
        // TODO Auto-generated method stub
        Marker m = null;
        try {
            if (TaxiUtil.mDriverdata.size() != 0) {
                double Driverdistance = 0;
                if (TaxiUtil.d_lat != 0)
                    availablecarcount = TaxiUtil.mDriverdata.size();
                if (TaxiUtil.mDriverdata.size() > 0) {
                    if (SplashActivity.isBUISNESSKEY)
                        new FindApproxDistance(getActivity(), P_latitude, P_longitude,
                                Double.parseDouble(TaxiUtil.mDriverdata.get(0).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(0).getLng()), true);
                    else
                        Driverdistance = 0.5 + FindDistance.distance(P_latitude, P_longitude, Double.parseDouble(TaxiUtil.mDriverdata.get(0).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(0).getLng()), SessionSave.getSession("Metric_type", getActivity()));


                }
                //  if (availablecarcount != 0) {
                if (!SplashActivity.isBUISNESSKEY)
                    new Approximate_distance(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), true).execute();
                // }
            } else {


             //   fare_eta.setText(0 + " " + getActivity().NC.getResources().getString(R.string.Mins));
                fare_eta.setText("-");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        try {
//            if (TaxiUtil.mDriverdata.size() != 0) {
//                double Driverdistance;
////                if (m != null) {
////                    m.remove();
////                }
//                availablecarcount = 0;
//                double NearestdriverArray[] = new double[TaxiUtil.mDriverdata.size()];
//                for (int i = 0; i < TaxiUtil.mDriverdata.size(); i++) {
//                    Driverdistance = 0.5 + FindDistance.distance(P_latitude, P_longitude, Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLat()), Double.parseDouble(TaxiUtil.mDriverdata.get(i).getLng()), SessionSave.getSession("Metric_type", getActivity()));
//                    NearestdriverArray[i] = Driverdistance;
//                    if (Driverdistance < Double.parseDouble(SessionSave.getSession("driver_around_miles", getActivity()))) {
//                        availablecarcount++;
//                    }
//                }
//                if (availablecarcount != 0) {
//                    Arrays.sort(NearestdriverArray);
//                    new Approximate_Time(NearestdriverArray[0], SessionSave.getSession("Metric_type", getActivity())).execute();
//                }
//            }
//            new FindApproxDistance(getActivity(), BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(),
//                    BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng());
//            System.gc();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


    }

    /**
     * class use to find the apporoximate distance between two latlong
     * used to find ETA from driver location to passenger
     * and
     * to find distance between pickup and drop location
     */
    public class Approximate_distance extends AsyncTask<Void, Void, Void> {
        double Ddistance;
        String Smetric;
        boolean isETA;

        Approximate_distance(double distance, String metric, boolean isETA) {

            Ddistance = distance;
            Smetric = metric;
            this.isETA = isETA;
        }

        @Override
        protected Void doInBackground(final Void... params) {

            try {
                calculatetime(Ddistance, Smetric, isETA);
            } catch (final Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            super.onPostExecute(result);
            // Approxi_time("yes");
        }
    }


    public void calculatetime(double distance, String metric, boolean isETA) {


        try {
            double distance_m = 0;
            if (distance != 0) {
                double timez;
                timez = distance / Double.parseDouble(BookTaxiNewFrag.speed);
                timez = timez * 3600; // time duration in seconds
                double minutes = Math.floor(timez / 60);
                timez -= minutes * 60;
                double seconds1 = Math.floor(timez);
                String timeString = (int) minutes + "." + (int) seconds1;
                //NSLOG(@"Minutes---->%f",minutes);

                //NSLOG(@"Seconds----->%f",seconds1);

                float minsfloatValue = Float.parseFloat(timeString);

                distance_m = Math.round(minsfloatValue * 100.0) / 100.0;

                //NSLOG(@"dis_cal---> %ld",(long)distance_m);
                if (distance_m <= 1) {
                    distance_m = 1;
                }
            }
            //System.out.println("naga---app" + distance_m + "__" + isETA + "__" + SplashActivity.isBUISNESSKEY);
            if (isETA) {
                E_time = distance_m;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        fare_eta.setText(E_time + " " + NC.getResources().getString(R.string.Mins));
                    }
                });

                if (!SplashActivity.isBUISNESSKEY) {
                    double Driverdistance = 0.5 + FindDistance.distance(BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(),
                            BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng(),
                            SessionSave.getSession("Metric_type", getActivity()));
                    new Approximate_distance(Driverdistance, SessionSave.getSession("Metric_type", getActivity()), false).execute();
                } else
                    new FindApproxDistance(getActivity(), BookTaxiNewFrag.sf.getPickuplat(), BookTaxiNewFrag.sf.getPickuplng(),
                            BookTaxiNewFrag.sf.getDroplat(), BookTaxiNewFrag.sf.getDroplng(), false);

            } else {
                // E_time = distance_m;
                if(distance<=min_km)
                    approx_fare= min_fare+5;
                else
                approx_fare = ApproximateCalculation.approxFare(getActivity(), (distance), distance_m);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        // System.out.println("naga---app" + approx_fare + "__" + "__" + SplashActivity.isBUISNESSKEY);
                        fare_basefare.setText("" + SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare-5)+" - "+ SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare+5));

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * use by Approximate_Time class in finding of approximate distance
     */
    public class FindApproxDistance implements APIResult {
        boolean isETA;

        public FindApproxDistance(Context c, double P_latitude, double P_longitude,
                                  double D_latitude, double D_longitude, boolean isETA) {
            this.isETA = isETA;
            ArrayList<LatLng> points = new ArrayList<>();
            LatLng pick = new LatLng(P_latitude, P_longitude);
            LatLng drop = new LatLng(D_latitude, D_longitude);
            points.add(pick);
            points.add(drop);
            String url = new Route().GetDistanceTime(c, points, "en", false);
          //  System.out.println("url___" + url);
            if (url != null && !url.equals(""))
                new APIService_Retrofit_JSON(c, this, null, true, url).execute();

            // new APIService_Volley_JSON(c, this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            if (isSuccess) {
                try {
                  //  System.out.println("result___" + result);
                    JSONObject obj = new JSONObject(result).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                    JSONObject ds = obj.getJSONObject("distance");
                    String dis = ds.getString("value");
                    JSONObject timee = obj.getJSONObject("duration");
                    String time = timee.getString("value");
                    Double approx_travel_time = Double.parseDouble(time) / 60;
                    Double approx_travel_dist = Double.parseDouble(dis) / 1000;

                    if (isETA){
                        E_time = approx_travel_time;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                fare_eta.setText(round(E_time,2) + " " + NC.getResources().getString(R.string.Mins));
                            }
                        });}

                    else {
                        if(approx_travel_dist<=min_km)
                            approx_fare= min_fare+5;
                        else
                        approx_fare = ApproximateCalculation.approxFare(getActivity(), approx_travel_dist, approx_travel_time);
                        fare_basefare.setText("" + SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare-5)+" - "+ SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare+5));

//                        fare_basefare.setText("" + SessionSave.getSession("Currency", getActivity()) + " " + String.format(Locale.UK, "%.2f", approx_fare));
                    }
                    //                    if (isBookAfter)
//                        Apicall_Book_After(dis, time);
//                    else {
//                        approxFare(c, Double.parseDouble(dis));
//                        FragmentManager fm = getChildFragmentManager();
//                        SplitFareDialog splitFareDialog = new SplitFareDialog();
//                        splitFareDialog.show(fm, "fragment_edit_name");
//                    }


                } catch (JSONException e) {
                    //Apicall_Book_After("", "");
                    e.printStackTrace();
                }


            }
        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}