package com.cabipassenger.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.data.apiData.PromoDataList;
import com.cabipassenger.util.Colorchange;

/**
 * Created by developer on 18/8/17.
 */

public class PromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    PromoDataList data;

    public interface LocationListItemClick {
        public void onClick(int pos);
    }

    public PromoAdapter(Context c, PromoDataList data) {
        this.mContext = c;
        this.data = data;
        //  this.data = TaxiUtil.SEARCH_LIST_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.promo_list_item, parent, false);
        Colorchange.ChangeColor((ViewGroup) view, mContext);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder vHolder = (CustomViewHolder) holder;
        vHolder.header.setText(data.promoDatas.get(position).getStatus());
        vHolder.message.setText(data.promoDatas.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return data.promoDatas.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView header, message;
        LinearLayout taxi__search_listitem;

        public CustomViewHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.header);
            message = (TextView) view.findViewById(R.id.message);
//            taxi__search_listitem= (LinearLayout) view.findViewById(R.id.taxi__search_listitem);
//            taxi__search_listitem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //   Toast.makeText(mContext, data.get(getAdapterPosition()).getAdd1()+"____"+String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                    ((LocationListItemClick)mContext).onClick(getAdapterPosition());
//
//                }
//            });
        }
    }

}