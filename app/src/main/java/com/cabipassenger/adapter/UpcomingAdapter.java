package com.cabipassenger.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.data.apiData.UpcomingResponse;
import com.cabipassenger.fragments.OnGoingFrag;
import com.cabipassenger.fragments.ReasonListFrag;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.SessionSave;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by developer on 1/11/16.
 * use to populate the upcoming list in trip history page
 */
public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.CustomViewHolder> {

    private final List<UpcomingResponse.PastBooking> data;
    private final Context mContext;


    public UpcomingAdapter(Context c, List<UpcomingResponse.PastBooking> data) {
        this.mContext = c;
        this.data = data;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = null;
        view = inflater.inflate(R.layout.upcoming_list_item, parent, false);
        Colorchange.ChangeColor((ViewGroup) view,mContext);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        if (!data.get(position).profile_image.trim().equals("")) {
            Picasso.with(mContext).load(data.get(position).map_image).into(holder.map_image);
            Picasso.with(mContext).load(data.get(position).profile_image).into(holder.driver_image);
        }
        holder.trip_time.setText(data.get(position).pickup_time);
        holder.trip_driver_name.setText(data.get(position).model_name);
        if(data.get(position).travel_status!=null)
            if (!data.get(position).travel_status.trim().equals("0")) {
                holder.trip_track.setVisibility(View.VISIBLE);
                holder.trip_cancel.setVisibility(View.GONE);
                holder.trip_track.setTag(position);
                holder.trip_track.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        SessionSave.saveSession("trip_id", data.get((Integer) view.getTag()).passengers_log_id.trim(), mContext);
                        ((AppCompatActivity) mContext).getSupportFragmentManager()
                                .beginTransaction().add(R.id.mainFrag, new OnGoingFrag()).addToBackStack(null).commit();
                    }
                });
            } else {
                holder.trip_track.setVisibility(View.GONE);
                holder.trip_cancel.setVisibility(View.VISIBLE);
                holder.trip_cancel.setTag(position);
//                holder.trip_cancel.setBackgroundResource(R.drawable.cancelheader_bgcolor);
                holder.trip_cancel.setTextColor(CL.getColor(mContext,R.color.button_accept));
                holder.trip_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fm1 = ((AppCompatActivity)mContext).getSupportFragmentManager();

                        Bundle bb =new Bundle();
                        bb.putString("From","2");
                        bb.putString("trip_id",data.get((Integer) view.getTag()).passengers_log_id.trim());

                        ReasonListFrag rl  = new ReasonListFrag();
                        rl.setArguments(bb);
                        rl.show(fm1,"rl");

                    }
                });

            }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView map_image, driver_image;
        TextView trip_time, trip_driver_name, trip_track, trip_cancel;

        public CustomViewHolder(View v) {
            super(v);
            map_image = (ImageView) v.findViewById(R.id.map_image);
            driver_image = (ImageView) v.findViewById(R.id.driver_image);
            trip_time = (TextView) v.findViewById(R.id.trip_time);
            trip_driver_name = (TextView) v.findViewById(R.id.trip_driver_name);
            trip_track = (TextView) v.findViewById(R.id.trip_track);
            trip_cancel= (TextView) v.findViewById(R.id.trip_cancel);
            //super(view);
        }
    }
}
