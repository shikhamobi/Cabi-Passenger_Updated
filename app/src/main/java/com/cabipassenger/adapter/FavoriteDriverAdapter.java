package com.cabipassenger.adapter;

;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.R;

import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.RoundedImageView;

import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developer on 7/10/16.
 * populate favourite driver listview
 */
public class FavoriteDriverAdapter extends BaseAdapter {
    private final Context context;
     LayoutInflater mInflater=null;
    private Dialog alertmDialog;

    // constructor
    public FavoriteDriverAdapter(Context favouritesAct) {
        // TODO Auto-generated constructor stub
        context = favouritesAct;
        try {
            mInflater = LayoutInflater.from(context);
        }catch (Exception e){

        }

    }

    // Return list size.
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TaxiUtil.mFavouriteDriverlist.size();
    }

    // It returns the item detail with select position.
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return TaxiUtil.mFavouriteDriverlist.get(position);
    }

    // It returns the item id with select position.
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // Get the view for each row in the list used view holder.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder mHolder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.favouritedriver_item, parent,false);
            mHolder = new ViewHolder();
            FontHelper.applyFont(context, convertView.findViewById(R.id.favdriveritem_contain));
            mHolder.img_profile = (RoundedImageView) convertView.findViewById(R.id.driverImg);
            mHolder.txt_name = (TextView) convertView.findViewById(R.id.driver_nameTxt);
            mHolder.txt_vechileno = (TextView) convertView.findViewById(R.id.driver_vechilenum);
            mHolder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
           // mHolder.txt_pno = (TextView) convertView.findViewById(R.id.driver_phoneTxt);
          
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        Colorchange.ChangeColor((ViewGroup) convertView,context);

        try {

           /* Log.e("Image ",TaxiUtil.mFavouriteDriverlist.get(position).profile_image);
            Log.e("Name ",TaxiUtil.mFavouriteDriverlist.get(position).name);
            Log.e("Email ",TaxiUtil.mFavouriteDriverlist.get(position).email);
            Log.e("phone ",TaxiUtil.mFavouriteDriverlist.get(position).phone);*/

            Picasso.with(context).load(TaxiUtil.mFavouriteDriverlist.get(position).profile_image).into(mHolder.img_profile);
            mHolder.txt_name.setText(TaxiUtil.mFavouriteDriverlist.get(position).name);
            mHolder.txt_vechileno.setText(TaxiUtil.mFavouriteDriverlist.get(position).taxino);


            mHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert_view(context, "" + NC.getResources().getString(R.string.message), context.getResources().getString(R.string.delete_fdrivers), "" + context.getResources().getString(R.string.ok), "" + context.getResources().getString(R.string.cancel),position);

                }
            });
            //mHolder.txt_email.setText(TaxiUtil.mFavouriteDriverlist.get(position).email);
            //mHolder.txt_pno.setText(TaxiUtil.mFavouriteDriverlist.get(position).phone);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    /**
     * calls api to delete the driver from favourite list
     */
    private class DelFavDriver implements APIResult {
        private String Message;
        int pos;

        public DelFavDriver(String url, JSONObject data,int _pos) {
            // TODO Auto-generated constructor stub
            this.pos = _pos;
            new APIService_Retrofit_JSON(context, this, data, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {
            // TODO Auto-generated method stub


            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);



                    Message = json.getString("message");

                    if (json.getInt("status") == 1) {

                        //remove deleted value from arraylist
                        TaxiUtil.mFavouriteDriverlist.remove(this.pos);
                        notifyDataSetChanged();

                    } else
                    {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, context.getString(R.string.server_con_error), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    /*public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt,final int position) {
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
            //button_failure.setVisibility(View.GONE);
            title_text.setVisibility(View.GONE);
            message_text.setText(message);
            button_failure.setText(failure_txt);
            button_failure.setVisibility(View.VISIBLE);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();

                    try {
                        String url;
                        JSONObject j = new JSONObject();
                        j.put("passenger_id", "" + SessionSave.getSession("Id", context));
                        j.put("driver_id", TaxiUtil.mFavouriteDriverlist.get(position).DriverId);
                        url = "type=unfavourite_driver";
                        new DelFavDriver(url, j,position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    }*/

    public  void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt,final int position)  {
        final View view = View.inflate(context, R.layout.netcon_lay, null);
        alertmDialog = new Dialog(context, R.style.dialogwinddow);
        alertmDialog.setContentView(view);
        alertmDialog.setCancelable(true);
        alertmDialog.show();
        FontHelper.applyFont(context, alertmDialog.findViewById(R.id.alert_id));
        final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
        final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
        message_text.setGravity(Gravity.CENTER);
        final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
        final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);

        title_text.setVisibility(View.GONE);
        message_text.setText("" + message);
        button_success.setText("" + NC.getResources().getString(R.string.yes));
        button_failure.setText("" + NC.getResources().getString(R.string.no));
        button_failure.setVisibility(View.VISIBLE);
        button_success.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                // TODO
                // Auto-generated
                // method stub
                alertmDialog.dismiss();

                try {
                    String url;
                    JSONObject j = new JSONObject();
                    j.put("passenger_id", "" + SessionSave.getSession("Id", context));
                    j.put("driver_id", TaxiUtil.mFavouriteDriverlist.get(position).DriverId);
                    url = "type=unfavourite_driver";
                    new DelFavDriver(url, j,position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        button_failure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO
                // Auto-generated
                // method stub
                alertmDialog.dismiss();
            }
        });


    }

    /**
     *   View holder class member this contains in every row in list.
     */

    class ViewHolder {
        public TextView txt_name,txt_vechileno;//txt_pno,txt_email;

        public RoundedImageView img_profile;

        public ImageView img_delete;
    }

}