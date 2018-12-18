package com.cabipassenger.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.data.apiData.UpcomingResponse;
import com.cabipassenger.fragments.TripDetailNewFrag;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by developer on 1/11/16.
 * use to populate the past booking recyclerview
 */
public class PastBookingAdapter extends RecyclerView.Adapter<PastBookingAdapter.CustomViewHolder> {

    private final List<UpcomingResponse.PastBooking> data;
    private final Context mContext;


    public PastBookingAdapter(Context c, List<UpcomingResponse.PastBooking> data) {
        this.mContext = c;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = null;
        view = inflater.inflate(R.layout.past_booking_item, parent, false);
        Colorchange.ChangeColor((ViewGroup) view,mContext);

        return new CustomViewHolder(view);
    }

    /**
     * binds view to recyclerview
     * @param holder
     * @param position
     */

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        if (!data.get(position).profile_image.trim().equals("")) {
            Picasso.with(mContext).load(data.get(position).map_image).into(holder.map_image);
            Picasso.with(mContext).load(data.get(position).profile_image).resize(100,100).into(holder.driver_image);
        }
        holder.trip_time.setText(data.get(position).pickup_time);
        holder.trip_driver_name.setText(data.get(position).model_name);
        holder.trip_time.setText(data.get(position).pickup_time);
        if (data.get(position).payment_type.trim().equals("1")) {
            holder.trip_payment_type.setText(NC.getString(R.string.cash));
            holder.trip_payment_type.setTextColor(CL.getColor(mContext, R.color.pickupheadertext));

        } else if (data.get(position).payment_type.trim().equals("5")) {
            holder.trip_payment_type.setText(NC.getString(R.string.wallet));
            holder.trip_payment_type.setTextColor(CL.getColor(mContext, R.color.pickupheadertext));
        } else if((data.get(position).payment_type.trim().equals("2"))||(data.get(position).payment_type.trim().equals("3"))){
            holder.trip_payment_type.setText(NC.getString(R.string.card));
            holder.trip_payment_type.setTextColor(CL.getColor(mContext, R.color.paymentcard));
        }
        else
            holder.trip_payment_type.setVisibility(View.GONE);
        holder.trip_payment_amount.setText(SessionSave.getSession("Currency", mContext) + "" + data.get(position).fare);
        holder.trip_status.setText(NC.getString(R.string.completed));
//        holder.map_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Fragment ff = new TripDetailNewFrag();
//                Bundle b = new Bundle();
//                b.putString("trip_id", data.get(position).trip_id);
//                b.putString("title", data.get(position).pickup_time);
//                ff.setArguments(b);
//                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, ff).commit();
//            }
//        });


        if (data.get(position).travel_status.trim().equals("1")) {
            holder.trip_status.setText(NC.getString(R.string.completed));
            holder.map_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment ff = new TripDetailNewFrag();
                    Bundle b = new Bundle();
                    b.putString("trip_id", data.get(position).trip_id);
                    b.putString("title", data.get(position).pickup_time);
                    ff.setArguments(b);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, ff).commit();
                }
            });

        } else {
            holder.trip_status.setText(NC.getString(R.string.cancelled));
            holder.map_image.setOnClickListener(null);
        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView map_image, driver_image;
        TextView trip_time, trip_driver_name;
        TextView trip_payment_type, trip_payment_amount, trip_status;

        public CustomViewHolder(View v) {
            super(v);
            map_image = (ImageView) v.findViewById(R.id.map_image);
            driver_image = (ImageView) v.findViewById(R.id.driver_image);
            trip_time = (TextView) v.findViewById(R.id.trip_time);
            trip_driver_name = (TextView) v.findViewById(R.id.trip_driver_name);

            trip_payment_type = (TextView) v.findViewById(R.id.trip_payment_type);
            trip_payment_amount = (TextView) v.findViewById(R.id.trip_payment_amount);
            trip_status = (TextView) v.findViewById(R.id.trip_status);
            //super(view);
        }
    }
}
