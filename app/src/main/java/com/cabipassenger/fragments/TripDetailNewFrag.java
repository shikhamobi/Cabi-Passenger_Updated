package com.cabipassenger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.data.SplitStatusData;
import com.cabipassenger.data.apiData.ApiRequestData;
import com.cabipassenger.data.apiData.TripDetailResponse;
import com.cabipassenger.service.CoreClient;
import com.cabipassenger.service.ServiceGenerator;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.RoundedImageView;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by developer on 2/11/16.
 * class for trip detail page directed from trip history
 */
public class TripDetailNewFrag extends Fragment {
    private TextView details_trip_id, distance, dfare,
            vdfare, waiting, wcost, vWait, sub, tax, promo, total,
            wallet, cash, t_pickup_value, t_dropoff_text_value, min_fare, min_fare_per, min_total_fare, pay_type;
    private String trip_id;
    private ImageView trip_map_view;
    private RoundedImageView driverImg;
    private TextView fares;
    private ImageView driverRat;
    private LinearLayout tripdetails, help_lay;
    private BottomSheetBehavior<View> mBottomSheetBehavior;

    DecimalFormat df = new DecimalFormat("####0.00");


    private RelativeLayout searchlay;
    private FrameLayout
            pickup_pinlay;
    private ImageView
            drop_pin, pickup_pin, pick_fav, drop_close;
    private LinearLayout
            pickupp, lay_pick_fav, dropppp, drop_fav;
    private TextView
            currentlocTxt, droplocEdt;
    private Double pickuplat = 0.0;
    private Double pickuplng = 0.0;
    private Double droplat = 0.0;
    private String fav_place_type;
    private String P_Address;
    private String title = "";
    private LinearLayout loading;
    private TextView user;
    private TextView night_fare;
    private TextView evefare;
    private LinearLayout distance_lay;
    private LinearLayout minutes_layout;
    private LinearLayout waiting_lay;
    private LinearLayout evening_lay;
    private LinearLayout night_lay;
    private LinearLayout promo_lay;
    private View pickup_drop_Sep;
    private FrameLayout bottomFrameLay;
    private TextView tax_label;
    private ImageView split_ref;
    private SplitFareStatusDialog splitFareDialog;
    private TextView distance_fare_txt;
    private View eve_night_sep;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.include_details, container, false);

        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        loading = (LinearLayout) v.findViewById(R.id.loading);

        ImageView iv = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);
        tax_label = (TextView) v.findViewById(R.id.tax_label);
        t_pickup_value = (TextView) v.findViewById(R.id.currentlocTxt);
        bottomFrameLay = (FrameLayout) v.findViewById(R.id.bottomFrameLay);
        driverImg = (RoundedImageView) v.findViewById(R.id.driverImg);
        trip_map_view = (ImageView) v.findViewById(R.id.trip_map_view);
        driverRat = (ImageView) v.findViewById(R.id.rating);
        tripdetails = (LinearLayout) v.findViewById(R.id.tripdetails);
        help_lay = (LinearLayout) v.findViewById(R.id.help_lay);
        user = (TextView) v.findViewById(R.id.user);
        t_dropoff_text_value = (TextView) v.findViewById(R.id.droplocEdt);
        details_trip_id = (TextView) v.findViewById(R.id.details_trip_id);
        distance = (TextView) v.findViewById(R.id.dist);
        dfare = (TextView) v.findViewById(R.id.dfare);
        vdfare = (TextView) v.findViewById(R.id.vehicle_detail_fare);
        waiting = (TextView) v.findViewById(R.id.wait);
        wcost = (TextView) v.findViewById(R.id.wcost);
        vWait = (TextView) v.findViewById(R.id.vwcost);
        sub = (TextView) v.findViewById(R.id.sTotal);
        tax = (TextView) v.findViewById(R.id.tax);
        promo = (TextView) v.findViewById(R.id.promo);
        total = (TextView) v.findViewById(R.id.total);
        wallet = (TextView) v.findViewById(R.id.wallet);
        cash = (TextView) v.findViewById(R.id.cash);
        fares = (TextView) v.findViewById(R.id.fares);
        min_fare = (TextView) v.findViewById(R.id.min_fare);
        min_fare_per = (TextView) v.findViewById(R.id.min_fare_per);
        min_total_fare = (TextView) v.findViewById(R.id.min_total_fare);
        pay_type = (TextView) v.findViewById(R.id.pay_type);
        trip_id = getArguments().getString("trip_id");

        View bottomSheet = v.findViewById(R.id.tripdetails_scroll);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        TextView help_txt = (TextView) v.findViewById(R.id.help_txt);
        help_txt.setTextColor(CL.getResources().getColor(getActivity(), R.color.button_accept));
        help_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment ff = new HelpFrag();
                Bundle bb = new Bundle();
                bb.putString("trip_id", trip_id);
                bb.putString("title", title);
                ff.setArguments(bb);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, ff).addToBackStack(null).commit();
            }
        });
        callDetail();
        dropVisible(v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (getActivity() != null) {
                title = getArguments().getString("title");
                ((MainHomeFragmentActivity) getActivity()).setTitle_m(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * to make drop location visible
     *
     * @param v
     */
    public void dropVisible(View v) {
        pickup_pinlay = (FrameLayout
                ) v.findViewById(R.id.pickup_pinlay);
        drop_pin = (ImageView
                ) v.findViewById(R.id.drop_pin);
        pickupp = (LinearLayout
                ) v.findViewById(R.id.pickupp);
        pickup_pin = (ImageView
                ) v.findViewById(R.id.pickup_pin);
        currentlocTxt = (TextView
                ) v.findViewById(R.id.currentlocTxt);
        lay_pick_fav = (LinearLayout
                ) v.findViewById(R.id.lay_pick_fav);
        pick_fav = (ImageView
                ) v.findViewById(R.id.pick_fav);
        dropppp = (LinearLayout
                ) v.findViewById(R.id.dropppp);
        droplocEdt = (TextView
                ) v.findViewById(R.id.droplocEdt);
        drop_fav = (LinearLayout
                ) v.findViewById(R.id.drop_fav);
        drop_close = (ImageView
                ) v.findViewById(R.id.drop_close);
        evefare = (TextView) v.findViewById(R.id.evefare);
        eve_night_sep = (View) v.findViewById(R.id.eve_night_sep);
        distance_lay = (LinearLayout) v.findViewById(R.id.distance_lay);
        evening_lay = (LinearLayout) v.findViewById(R.id.evening_lay);
        night_lay = (LinearLayout) v.findViewById(R.id.night_lay);
        waiting_lay = (LinearLayout) v.findViewById(R.id.waiting_lay);
        distance_fare_txt = (TextView) v.findViewById(R.id.distance_fare_txt);
        promo_lay = (LinearLayout) v.findViewById(R.id.promo_lay);
        minutes_layout = (LinearLayout) v.findViewById(R.id.minutes_layout);
        night_fare = (TextView) v.findViewById(R.id.night_fare);
        pickup_drop_Sep = (View) v.findViewById(R.id.pickup_drop_Sep);
        searchlay = (RelativeLayout) v.findViewById(R.id.searchlay);
        dropppp.setVisibility(View.VISIBLE);
        pick_fav.setImageResource(R.drawable.star);
        pickup_pinlay.setVisibility(View.VISIBLE);
        pickup_pin.setVisibility(View.GONE);

        dropppp.setVisibility(View.VISIBLE);
        pick_fav.setImageResource(R.drawable.star);
        pick_fav.setVisibility(View.GONE);
        pickup_pinlay.setVisibility(View.VISIBLE);
        pickup_pin.setVisibility(View.GONE);
        pickup_drop_Sep.setVisibility(View.VISIBLE);
        // v.findViewById(R.id.pickUpTxt).setVisibility(View.INVISIBLE);
        if (SessionSave.getSession("Lang", getActivity()).equals("fa")||SessionSave.getSession("Lang", getActivity()).equals("ar"))
            pickupp.setBackgroundResource(R.color.transparent);
        else
            pickupp.setBackgroundResource(R.color.transparent);

        searchlay.setBackgroundResource(R.drawable.rect_home_bottom);
        split_ref = (ImageView) v.findViewById(R.id.split_ref);
        split_ref.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                splitFareDialog = new SplitFareStatusDialog();
                splitFareDialog.show(fm, "splitStatus");
            }

        });


//        if (SessionSave.getSession("Lang", getActivity()).equals("fa")||SessionSave.getSession("Lang", getActivity()).equals("ar"))
//            pickupp.setBackgroundResource(R.drawable.search_pickup_ar);
//        else
//            pickupp.setBackgroundResource(R.drawable.search_pickup);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.mybookings));
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");

        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).small_title(true);

        try {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainHomeFragmentActivity) getActivity()).small_title(false);
    }

    /**
     * to convert double values to 2 decimel string ex:12.6677896 to 12.66
     *
     * @param value  -->double value
     * @param places --> how many digits after decimel
     * @return round off string
     */
    public static String round(double value, int places) {
        return String.valueOf(value);
//        if (places < 0) throw new IllegalArgumentException();
//
//        long factor = (long) Math.pow(10, places);
//        value = value * factor;
//        long tmp = Math.round(value);
//        return String.valueOf((double) tmp / factor);
    }

    /**
     * request to fill trip detail fragment
     */
    private void callDetail() {
        CoreClient client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
        ApiRequestData.getTripDetailRequest request = new ApiRequestData.getTripDetailRequest();
        request.setTrip_id(trip_id);
        request.setPassenger_id(SessionSave.getSession("Id", getActivity()));

        Call<TripDetailResponse> LoginResponse = client.callData(TaxiUtil.COMPANY_KEY, TaxiUtil.DYNAMIC_AUTH_KEY, request, SessionSave.getSession("Lang", getActivity()));
        LoginResponse.enqueue(new Callback<TripDetailResponse>() {
            @Override
            public void onResponse(Call<TripDetailResponse> call, Response<TripDetailResponse> response) {
                if (getView() != null) {
                    TripDetailResponse data = response.body();
                    if (data != null) {
                        if (data.status == 1) {
                            loading.setVisibility(View.GONE);
                            bottomFrameLay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                            });
                            details_trip_id.setText(NC.getString(R.string.trip_id) + ": " + data.detail.trip_id);
                            user.setText(data.detail.driver_name);
                            fares.setText(SessionSave.getSession("Currency", getActivity()) + data.detail.amt);
                            Picasso.with(getActivity()).load(data.detail.driver_image).resize(100, 100).into(driverImg);
                          //  System.out.println("_________TTT" + data.detail.map_image);
                            Picasso.with(getActivity()).load(data.detail.map_image).into(trip_map_view);
                            t_dropoff_text_value.setText(data.detail.drop_location);
                            t_pickup_value.setText(data.detail.current_location);
                            distance.setText(data.detail.distance + " " + data.detail.metric);
                            dfare.setText(SessionSave.getSession("Currency", getActivity()) + data.detail.distance_fare_metric);
                            vdfare.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.distance_fare));
                            min_fare.setText(data.detail.trip_minutes + " " + NC.getString(R.string.mins));
                            min_fare_per.setText(SessionSave.getSession("Currency", getActivity()) + data.detail.fare_per_minute);
                            min_total_fare.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.minutes_fare));
                            waiting.setText(data.detail.waiting_time);
                            wcost.setText(SessionSave.getSession("Currency", getActivity()) + data.detail.waiting_fare_hour + "/" + NC.getString(R.string.hour));
                            vWait.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.waiting_fare));
                            sub.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.subtotal));
                            tax.setText(SessionSave.getSession("site_currency", getActivity()) + (data.detail.tax_fare));
                            tax_label.setText(NC.getString(R.string.Tax) + " " + data.detail.tax_percentage + "%");
                            promo.setText(data.detail.promocode_fare);
                            total.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.amt));
                            wallet.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.used_wallet_amount));
                            cash.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.actual_paid_amount));
                           // System.out.println("payment_type"+data.detail.payment_type_label);
                            pay_type.setText(data.detail.payment_type_label);
//
                            if (data.detail.payment_type_label.trim().equalsIgnoreCase(NC.getString(R.string.cash).trim())) {
                                // payment_type_c.setText(NC.getString(R.string.cash));
                                pay_type.setTextColor(getResources().getColor(R.color.pastbookingcashtext));

                            } else if (data.detail.payment_type_label.trim().equalsIgnoreCase(NC.getString(R.string.wallet).trim())) {
                                //  payment_type_c.setText(NC.getString(R.string.wallet));
                                pay_type.setTextColor(getResources().getColor(R.color.pastbookingcashtext));
                            } else {
                                // payment_type_c.setText(NC.getString(R.string.card));
                                pay_type.setTextColor(getResources().getColor(R.color.paymentcard));
                            }
                            distance_fare_txt.setText(NC.getString(R.string.dist_fare) + "(1/" + data.detail.metric + ")");
                            //Changes
                            night_fare.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.nightfare));
                            evefare.setText(SessionSave.getSession("Currency", getActivity()) + (data.detail.eveningfare));

                            if (Double.parseDouble(data.detail.nightfare) <= 0.0) {
                                night_lay.setVisibility(View.GONE);
                            }

                            if (Double.parseDouble(data.detail.eveningfare) <= 0.0) {
                                evening_lay.setVisibility(View.GONE);
                            }
                            if (Double.parseDouble(data.detail.nightfare) <= 0.0 && Double.parseDouble(data.detail.eveningfare) <= 0.0) {
                                eve_night_sep.setVisibility(View.GONE);
                            }
                            if (data.detail.promocode_fare.equals("0")) {
                                promo_lay.setVisibility(View.GONE);
                            }


                            if (data.detail.fare_calculation_type.equals("1")) {
                                minutes_layout.setVisibility(View.GONE);

                            } else if (data.detail.fare_calculation_type.equals("2")) {
                                distance_lay.setVisibility(View.GONE);

                            }


//                            if(Double.parseDouble(data.detail.waiting_fare)<=0.0){
//                                waiting_lay.setVisibility(View.GONE);
//                            }
//                            if(Double.parseDouble(data.detail.minutes_fare)<=0.0){
//                            }
//                            if(Double.parseDouble(data.detail.distance_fare)<=0.0){
//                            }

                            if (data.detail.payment_type.equals("1"))
                                pay_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, 0, 0);
                            else if (data.detail.payment_type.equals("5")) {
                                pay_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, 0, 0);
                                pay_type.setVisibility(View.GONE);
                                cash.setVisibility(View.GONE);
                            } else
                                pay_type.setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, 0, 0);
                         //   pay_type.setText(data.detail.payment_type_label);
                            int driver_rating = Integer.parseInt(data.detail.rating);


                            if (driver_rating == 0)
                                driverRat.setImageResource(R.drawable.star6);
                            else if (driver_rating == 1)
                                driverRat.setImageResource(R.drawable.star1);
                            else if (driver_rating == 2)
                                driverRat.setImageResource(R.drawable.star2);
                            else if (driver_rating == 3)
                                driverRat.setImageResource(R.drawable.star3);
                            else if (driver_rating == 4)
                                driverRat.setImageResource(R.drawable.star4);
                            else if (driver_rating == 5)
                                driverRat.setImageResource(R.drawable.star5);
                            try {
                            //    System.out.println("_______________ddddd" + data.detail.isSplit_fare);
                                if (data.detail.isSplit_fare != null)
                                    if (data.detail.isSplit_fare == 1 ? true : false) {
                                        //Toast.makeText(getActivity(), "hiiiii", Toast.LENGTH_SHORT).show();
                                        split_ref.setVisibility(View.VISIBLE);
                                        TaxiUtil.SPLIT_STATUS_ITEM.clear();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(data.detail.splitFareDetails);
                                     //   System.out.println("_______________ddddd" + json.toString());
                                        JSONArray splitArray = new JSONArray(json);
                                        //JSONArray splitArray = jsonn.getJSONObject("detail").getJSONArray("splitFareDetails");
                                        for (int i = 0; i < splitArray.length(); i++) {
                                       //     System.out.println("_______________ddddd" + i);
                                            JSONObject SplitArrayData = splitArray.getJSONObject(i);
                                            SplitStatusData obj = new SplitStatusData(getActivity());
                                            obj.setImage(SplitArrayData.getString("profile_image"));
                                            obj.setName(SplitArrayData.getString("firstname"));
                                            obj.setStatus(SplitArrayData.getString("approve_status"));
                                            obj.setFare_perc(SplitArrayData.getString("fare_percentage"));
                                            obj.setSplitfare(SplitArrayData.getString("splitted_fare"));
                                            obj.setWallet(SplitArrayData.getString("used_wallet_amount"));
                                            TaxiUtil.SPLIT_STATUS_ITEM.add(obj);
                                        }

                                    }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), NC.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                    //((MainActivity) getActivity()).closeProgressDialog();
                }


            }

            @Override
            public void onFailure(Call<TripDetailResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), NC.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();

                // (getActivity() != null)
                //((MainActivity) getActivity()).closeProgressDialog();
            }
        });
    }

}