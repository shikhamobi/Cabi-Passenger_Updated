package com.cabipassenger.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.adapter.PastBookingAdapter;
import com.cabipassenger.adapter.UpcomingAdapter;
import com.cabipassenger.data.apiData.ApiRequestData;
import com.cabipassenger.data.apiData.PastBookingResponse;
import com.cabipassenger.data.apiData.UpcomingResponse;
import com.cabipassenger.service.CoreClient;
import com.cabipassenger.service.ServiceGenerator;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.NetworkStatus;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by developer on 1/11/16.
 * Fragment which contains ongoing and upcoming list
 */
public class TripHistory extends Fragment {

    private static boolean UP_COMING = true;
    LinearLayout txt_up_coming_r, txt_past_booking_r;
    TextView txt_up_coming, txt_past_booking;
    RecyclerView history_recyclerView;
    private TextView no_data;
    int start = 0;
    private int limit = 10;
    private int preLast = -9;
    private LinearLayoutManager mLayoutManager;
    private List<UpcomingResponse.PastBooking> pastData = new ArrayList<>();
    private int prevLimt;
    private PastBookingAdapter past_booking_adapter;
    private Dialog mDialog;
    private View upcoming_underline, past_underline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trip_history_lay, container, false);
        Initialize(v);
        Colorchange.ChangeColor((ViewGroup)v,getActivity());

        return v;
    }

    public void Initialize(View v) {
        txt_past_booking = (TextView) v.findViewById(R.id.txt_past_booking);
        txt_up_coming = (TextView) v.findViewById(R.id.txt_up_coming);
        txt_up_coming_r = (LinearLayout) v.findViewById(R.id.txt_up_coming_r);
        txt_past_booking_r = (LinearLayout) v.findViewById(R.id.txt_past_booking_r);
        history_recyclerView = (RecyclerView) v.findViewById(R.id.history_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        history_recyclerView.setLayoutManager(mLayoutManager);
        no_data = (TextView) v.findViewById(R.id.nodataTxt);

        upcoming_underline = (View) v.findViewById(R.id.upcoming_underline);
        past_underline = (View) v.findViewById(R.id.past_underline);

        txt_up_coming_r.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //txt_up_coming.setBackgroundResource(R.drawable.book_select);
               // txt_past_booking.setBackgroundResource(R.drawable.book_unselect);

                upcoming_underline.setBackgroundColor(CL.getResources().getColor(getActivity(),R.color.button_accept));
                past_underline.setBackgroundColor(CL.getResources().getColor(getActivity(),R.color.textviewcolor_light));


                txt_past_booking.setTextColor(CL.getResources().getColor(getActivity(),R.color.textviewcolor_light));
                txt_up_coming.setTextColor(CL.getResources().getColor(getActivity(),R.color.button_accept));

                history_recyclerView.setAdapter(null);
                UP_COMING = true;
                callUpComingData();
            }
        });


        txt_past_booking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                past_underline.setBackgroundColor(CL.getResources().getColor(getActivity(),R.color.button_accept));
                upcoming_underline.setBackgroundColor(CL.getResources().getColor(getActivity(),R.color.textviewcolor_light));


                txt_past_booking.setTextColor(CL.getResources().getColor(getActivity(),R.color.button_accept));
                txt_up_coming.setTextColor(CL.getResources().getColor(getActivity(),R.color.textviewcolor_light));

                pastData.clear();
                history_recyclerView.setAdapter(null);
                past_booking_adapter=null;
                start = 0;
                limit = 10;
                UP_COMING = false;
                callPastBookingData();
            }
        });
        callUpComingData();


        history_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!UP_COMING)
                    if (dy > 0) //check for scroll down
                    {
                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                  //      Log.v("...", "Last Item Wow !" + visibleItemCount + "___" + pastVisiblesItems + "___" + totalItemCount);


//                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            // loading = false;
                           // Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            if (totalItemCount >= 10 && limit >= prevLimt && totalItemCount == limit) {
                              //  System.out.println("_*____*****_" + limit + "***" + start + "***" + totalItemCount);
                                if (start == 0)
                                    start = 11;
                                else
                                    start += 10;
                                prevLimt = limit;
                                limit += 10;
                                callPastBookingData();
                            }
                        }
                        // }
                    }
            }
        });
    }



    @Override
    public void onStop() {
        super.onStop();
        closeDialog();
    }

    /**
     * request server for upcoming list items
     */
    private void callUpComingData() {
        CoreClient client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
        ApiRequestData.UpcomingRequest request = new ApiRequestData.UpcomingRequest();
        request.setId(SessionSave.getSession("Id", getActivity()));
        request.setDeviceType("2");
        request.setLimit("100");
        request.setStart("0");//

        Call<UpcomingResponse> LoginResponse = client.callData(TaxiUtil.COMPANY_KEY,TaxiUtil.DYNAMIC_AUTH_KEY,request,SessionSave.getSession("Lang",getActivity()));
        showDialog();
        LoginResponse.enqueue(new Callback<UpcomingResponse>() {
            @Override
            public void onResponse(Call<UpcomingResponse> call, Response<UpcomingResponse> response) {
                if (getView() != null) {
                    closeDialog();
                    UpcomingResponse data = response.body();
                    if (data != null) {
                        if (data.status == 1) {
                            history_recyclerView.setAdapter(new UpcomingAdapter(getContext(), data.detail.pending_bookings));

                            if (data.detail.pending_bookings.size() == 0)
                                no_data.setVisibility(View.VISIBLE);
                            else
                                no_data.setVisibility(View.GONE);
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
            public void onFailure(Call<UpcomingResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), NC.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();

                // (getActivity() != null)
                //((MainActivity) getActivity()).closeProgressDialog();
            }
        });
    }

    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(getActivity())) {
                View view = View.inflate(getActivity(), R.layout.progress_bar, null);
                mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
                mDialog.setContentView(view);
                mDialog.setCancelable(false);
                mDialog.show();

                ImageView iv= (ImageView)mDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(getActivity())
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);
            } else {

            }
        } catch (Exception e) {

        }

    }

    public void closeDialog() {
        try {
            if (mDialog != null)
                if (mDialog.isShowing())
                    mDialog.dismiss();
        } catch (Exception e) {

        }
    }
/**
 * this method is used to call pastbooking api
 */

    private void callPastBookingData() {
        CoreClient client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
        ApiRequestData.PastBookingRequest request = new ApiRequestData.PastBookingRequest();
        request.setPassenger_id(SessionSave.getSession("Id", getActivity()));
        request.setDevice_type("2");
        request.setLimit("10");
        request.setStart(String.valueOf(start));
        request.setMonth("12");
        request.setYear("2016");
        // System.out.println("_*____****_"+limit+"***"+start);
        Call<PastBookingResponse> LoginResponse = client.callData(TaxiUtil.COMPANY_KEY,TaxiUtil.DYNAMIC_AUTH_KEY,request,SessionSave.getSession("Lang",getActivity()));
        showDialog();
        LoginResponse.enqueue(new Callback<PastBookingResponse>() {
            @Override
            public void onResponse(Call<PastBookingResponse> call, Response<PastBookingResponse> response) {
                if (getView() != null) {
                    closeDialog();
                    PastBookingResponse data = response.body();

                    if (data != null) {
                        if (data.status == 1) {
                            pastData.addAll(data.trip_details);
                            if (past_booking_adapter == null) {
                                past_booking_adapter = new PastBookingAdapter(getContext(), pastData);
                                history_recyclerView.setAdapter(past_booking_adapter);
                                if (data.trip_details.size() == 0)
                                    no_data.setVisibility(View.VISIBLE);
                                else
                                    no_data.setVisibility(View.GONE);
                            } else
                                past_booking_adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), NC.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<PastBookingResponse> call, Throwable t) {
                t.printStackTrace();
                closeDialog();
                Toast.makeText(getActivity(), NC.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();

                // (getActivity() != null)
                //((MainActivity) getActivity()).closeProgressDialog();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

      //  Log.d("onResume","onResume");

        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.mybookings));
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
    }
}
