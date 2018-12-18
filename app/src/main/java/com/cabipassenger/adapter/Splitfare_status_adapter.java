package com.cabipassenger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.data.SplitStatusData;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by developer on 6/6/16.
 * use to populate the split fare status listdialog in ongoing page
 */

public class Splitfare_status_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<SplitStatusData> data;



    public Splitfare_status_adapter(Context c) {
        this.mContext = c;
        this.data = TaxiUtil.SPLIT_STATUS_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.split_status_item, parent, false);
        Colorchange.ChangeColor((ViewGroup) view,mContext);

        FontHelper.applyFont(mContext, view);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==1)
            this.data = TaxiUtil.SPLIT_STATUS_ITEM;
        CustomViewHolder vHolder = (CustomViewHolder) holder;
        //   System.out.println("nagasearch--po" + data.get(position).getAdd1() + "_____" + position);
        vHolder.user_name.setText(data.get(position).getName());
        vHolder.status.setText(data.get(position).getStatus_text());
        vHolder.status.setTextColor(data.get(position).getStatus_color());
        Picasso.with(mContext).load(data.get(position).getImage()).into(vHolder.user_image);
        if(data.get(position).getFare_perc()!=null)
        {
            vHolder.farelay.setVisibility(View.VISIBLE);
            vHolder.fare.setText(SessionSave.getSession("Currency", mContext) + " " + data.get(position).getSplitfare());
            vHolder.fare_prec.setText(data.get(position).getFare_perc()+"%");
            vHolder.wallet_amt.setText(SessionSave.getSession("Currency", mContext) + " " + data.get(position).getWallet());
            vHolder.wallet_lay.setVisibility(View.VISIBLE);
        }

        if(position==4)
            vHolder.view1.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, status;
        ImageView user_image;
        View view1;
        TextView fare_prec,fare,wallet_amt;
        LinearLayout farelay,wallet_lay;

        public CustomViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.user_name);
            status = (TextView) view.findViewById(R.id.status);
            wallet_lay = (LinearLayout) view.findViewById(R.id.wallet_lay);
            fare_prec = (TextView) view.findViewById(R.id.fare_prec);
            fare = (TextView) view.findViewById(R.id.fare);
            farelay = (LinearLayout) view.findViewById(R.id.farelay);
            user_image = (ImageView) view.findViewById(R.id.user_image);
            view1=view.findViewById(R.id.view);
            wallet_amt= (TextView) view.findViewById(R.id.wallet_amt);

        }
    }

}
