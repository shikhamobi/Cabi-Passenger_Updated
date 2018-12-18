package com.cabipassenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.R;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

/**
 * This adapter used to show the trip history details into list view.
 */
//
public class TripHistoryAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater mInflater;

    // constructor
    public TripHistoryAdapter(Context con) {

        context = con;
        mInflater = LayoutInflater.from(context);
    }

    // Return list size.
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TaxiUtil.mTripHistorylist.size();
    }

    // It returns the item detail with select position.
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return TaxiUtil.mTripHistorylist.get(position);
    }

    // It returns the item id with select position.
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // Get the view for each row in the list used view holder.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.history_item, parent,false);
            FontHelper.applyFont(context, convertView.findViewById(R.id.historyitem_contain));
            holder.linear = (LinearLayout) convertView.findViewById(R.id.date_lay);
            holder.history_lay = (LinearLayout) convertView.findViewById(R.id.history_lay);
            holder.textView = (TextView) convertView.findViewById(R.id.historydate);
            holder.img_line = (View) convertView.findViewById(R.id.img_line);
            holder.fare = (TextView) convertView.findViewById(R.id.fareTxt);
            holder.name = (TextView) convertView.findViewById(R.id.driverNameTxt);
            holder.time = (TextView) convertView.findViewById(R.id.timeTxt);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Colorchange.ChangeColor((ViewGroup) convertView,context);

        try {
            if (TaxiUtil.mTripHistorylist.get(position).getDateFlag() == 0) {
                holder.linear.setVisibility(View.GONE);
            }
            else {
                holder.linear.setVisibility(View.VISIBLE);
                holder.textView.setText(TaxiUtil.mTripHistorylist.get(position).getCreatedate());
            }
            holder.fare.setText(SessionSave.getSession("Currency", context) + TaxiUtil.mTripHistorylist.get(position).getFare());
            holder.name.setText(TaxiUtil.mTripHistorylist.get(position).getPlace());
            holder.time.setText(TaxiUtil.mTripHistorylist.get(position).getTime());
            if (position == 0) {
                holder.img_line.setBackgroundResource(R.drawable.line2);
            }
            else if (position == (TaxiUtil.mTripHistorylist.size() - 1)) {
                holder.img_line.setBackgroundResource(R.drawable.line1);
            }
            if (TaxiUtil.mTripHistorylist.size() == 1) {
                holder.img_line.setBackgroundResource(R.drawable.line1);
            }
        }
        catch (Exception e) {
        }
        return convertView;
    }

    // View holder class member this contains in every row in list.
    public static class ViewHolder {
        public TextView textView;
        public TextView time, name, fare;
        public LinearLayout linear, history_lay;
        public View img_line;
    }
}
