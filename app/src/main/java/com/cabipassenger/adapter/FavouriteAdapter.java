package com.cabipassenger.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.fragments.BookTaxiNewFrag;
import com.cabipassenger.fragments.EditFavouriteFrag;
import com.cabipassenger.interfaces.DialogInterface;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;

import java.util.Calendar;

/**
 * This adapter used to populate the favorite details into list view.
 */
//
public class FavouriteAdapter extends BaseAdapter implements DialogInterface {
    private final Context context;
    private final LayoutInflater mInflater;
    private String FavId = "", fLat = "", fLong = "", fPlace = "", dplace = "", dlat = "", dlong = "", fav_comments = "", notes = "";
    private String Place_type;
    private String PickupTime;
    private int clickedPos;
    private int book_fav_Driver = 2;
    private Dialog alertmDialog;

    // constructor
    public FavouriteAdapter(Context favouritesAct) {
        // TODO Auto-generated constructor stub
        context = favouritesAct;
        mInflater = LayoutInflater.from(context);
        int hour;
        int minute;
        int seconds;
        final Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        seconds = cal.get(Calendar.SECOND);
        updateTime(hour, minute, seconds);
    }

    // Return list size.
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TaxiUtil.mFavouritelist.size();
    }

    // It returns the item detail with select position.
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return TaxiUtil.mFavouritelist.get(position);
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

            convertView = mInflater.inflate(R.layout.favourite_item, parent,false);
            mHolder = new ViewHolder();
            FontHelper.applyFont(context, convertView.findViewById(R.id.favitem_contain));
            mHolder.place_image = (ImageView) convertView.findViewById(R.id.place_image);
            mHolder.Place = (TextView) convertView.findViewById(R.id.placeTxt);
            mHolder.drop_placeTxt = (TextView) convertView.findViewById(R.id.drop_placeTxt);
            mHolder.img_lineup = convertView.findViewById(R.id.line1);
            mHolder.BookBtn = (TextView) convertView.findViewById(R.id.BookBtn);
            mHolder.placelay = (LinearLayout) convertView.findViewById(R.id.placelay);
            mHolder.drop_pin = (ImageView) convertView.findViewById(R.id.drop_pin);
            mHolder.fav_img = (ImageView) convertView.findViewById(R.id.fav_img);
            mHolder.favitem_contain=(LinearLayout) convertView.findViewById(R.id.favitem_contains);


            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        Colorchange.ChangeColor((ViewGroup) convertView,context);

        try {
//            if (position == 0) {
//                mHolder.img_lineup.setVisibility(View.VISIBLE);
//                mHolder.img_linebottom.setVisibility(View.GONE);
//            }
//            else if (position == (TaxiUtil.mFavouritelist.size() - 1)) {
//                mHolder.img_linebottom.setVisibility(View.VISIBLE);
//                mHolder.img_lineup.setVisibility(View.GONE);
//            }
//            else {
//                mHolder.img_lineup.setVisibility(View.VISIBLE);
//                mHolder.img_linebottom.setVisibility(View.GONE);
//            }
            mHolder.BookBtn.setText(NC.getString(R.string.book_now));
            Spannable pickupplace = new SpannableString("" + TaxiUtil.mFavouritelist.get(position).getPlace());
            pickupplace.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mHolder.Place.setText(pickupplace);
            Spannable dropplace = new SpannableString("" + TaxiUtil.mFavouritelist.get(position).getD_Place());
            dropplace.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (TaxiUtil.mFavouritelist.get(position).getD_Place().toString().trim().length() > 2)
                mHolder.drop_placeTxt.setVisibility(View.VISIBLE);


            mHolder.drop_placeTxt.setText(dropplace);
            if (TaxiUtil.mFavouritelist.get(position).getPlace_type().equals("1")) {
                mHolder.place_image.setImageResource(R.drawable.fav_home);
            } else if (TaxiUtil.mFavouritelist.get(position).getPlace_type().equals("2")) {
                mHolder.place_image.setImageResource(R.drawable.company);
            } else if (TaxiUtil.mFavouritelist.get(position).getPlace_type().equals("3")) {
                mHolder.place_image.setImageResource(R.drawable.fav_airport);
            } else {
                mHolder.place_image.setImageResource(R.drawable.fav_others);
            }

//            String msg = "http://maps.googleapis.com/maps/api/staticmap?center="
//                    +"11.01187"
//                    + ","
//                    + "76.8970224"
//                    + "&zoom=8&markers=color:blue%7Clabel:S%7C"
//                    +"11.01187"
//                    + ","
//                    + "76.8970224"
//                    + "&size=500x400&sensor=false"
//                    // String msg = "Location :" + " " + maplocation.maplocationname
//                    + "~~~~~" +"11.01187"
//                    + ","
//                    + "76.8970224";

            Glide.with(context).load(TaxiUtil.mFavouritelist.get(position).getMap_image().toString()).into(mHolder.fav_img);


            if (TaxiUtil.mFavouritelist.get(position).getD_Place().toString().length() < 2) {
                mHolder.drop_pin.setImageResource(R.drawable.dot);
                mHolder.drop_placeTxt.setVisibility(View.GONE);
            } else if (TaxiUtil.mFavouritelist.get(position).getD_Place().toString().length() > 2) {
                mHolder.drop_pin.setImageResource(R.drawable.location);
            }
            if (TaxiUtil.mFavouritelist.size() == 1) {
                // mHolder.img_linebottom.setVisibility(View.VISIBLE);
                //   mHolder.img_lineup.setVisibility(View.GONE);
            }
            // this listener helps call book the taxi from already stored favorite place.
            mHolder.BookBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (!SessionSave.getSession("trip_id", context).equals("")) {
                        Intent i = new Intent(context, MainHomeFragmentActivity.class);
                        Toast.makeText(context, NC.getString(R.string.Trip_in_progress), Toast.LENGTH_SHORT).show();
                        context.startActivity(i);
                    } else {
                        FavId = TaxiUtil.mFavouritelist.get(position).getFavouriteId();
                        fLat = TaxiUtil.mFavouritelist.get(position).getF_lat();
                        fLong = TaxiUtil.mFavouritelist.get(position).getF_lng();
                        fPlace = TaxiUtil.mFavouritelist.get(position).getPlace();
                        dplace = TaxiUtil.mFavouritelist.get(position).getD_Place();
                        dlat = TaxiUtil.mFavouritelist.get(position).getD_Lat();
                        dlong = TaxiUtil.mFavouritelist.get(position).getD_Long();
                        notes = TaxiUtil.mFavouritelist.get(position).getNotes();

                        BookTaxiNewFrag ef = new BookTaxiNewFrag();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("pickup_latitude", Double.parseDouble(fLat));
                        bundle.putDouble("pickup_longitude", Double.parseDouble(fLong));
                        if (!dlat.trim().equals("")) {
                            bundle.putDouble("drop_latitude", Double.parseDouble(dlat));
                            bundle.putDouble("drop_longitude", Double.parseDouble(dlong));
                            bundle.putString("drop_location", dplace);
                        } else {
                            bundle.putDouble("drop_latitude", 0.0);
                            bundle.putDouble("drop_longitude", 0.0);
                            bundle.putString("drop_location", null);
                        }
                        bundle.putString("pickup_location", fPlace);
                        bundle.putString("driver_notes", notes);
                        bundle.putBoolean("book_again", true);

                        ef.setArguments(bundle);
                        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }


                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, ef).commit();


//                        if (BookTaxiNewFrag.availablecarcount > 0) {
//
//
//                            if (BookTaxiNewFrag.fav_driver_available > 0) {
//                                new Dialog_Common().setmCustomDialog(context, FavouriteAdapter.this, context.getResources().getString(R.string.message), BookTaxiNewFrag.fav_driver_message,
//                                        context.getResources().getString(R.string.ok),
//                                        context.getResources().getString(R.string.no_thanks), "1");
//                                clickedPos = position;
//                            } else
//                                bookNow(position);
//                        } else {
//                            alert_view(context, "" + context.getResources().getString(R.string.message), "" + context.getResources().getString(R.string.nodrivers), "" + context.getResources().getString(R.string.ok), "");
//                        }
                    }
                }
            });
            mHolder.favitem_contain.setTag(position);
            mHolder.placelay.setTag(position);
            mHolder.favitem_contain.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    TextView p = (TextView) view.findViewById(R.id.placeTxt);
                    TextView d = (TextView) view.findViewById(R.id.drop_placeTxt);

                    FavId = TaxiUtil.mFavouritelist.get(position).getFavouriteId();
                    fLat = TaxiUtil.mFavouritelist.get(position).getF_lat();
                    fLong = TaxiUtil.mFavouritelist.get(position).getF_lng();
                    fPlace = TaxiUtil.mFavouritelist.get(position).getPlace();
                    dplace = TaxiUtil.mFavouritelist.get(position).getD_Place();
                    dlat = TaxiUtil.mFavouritelist.get(position).getD_Lat();
                    dlong = TaxiUtil.mFavouritelist.get(position).getD_Long();
                    fav_comments = TaxiUtil.mFavouritelist.get(position).getComments();
                    notes = TaxiUtil.mFavouritelist.get(position).getNotes();
                    Place_type = TaxiUtil.mFavouritelist.get(position).getPlace_type();
                    EditFavouriteFrag ef = new EditFavouriteFrag();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("ADD", false);
                    bundle.putString("Id", FavId);
                    bundle.putString("Lat", fLat);
                    bundle.putString("Lng", fLong);
                    bundle.putString("Place", fPlace);
                    bundle.putString("D_place", dplace);
                    bundle.putString("D_lat", dlat);
                    bundle.putString("D_long", dlong);
                    bundle.putString("Cmt", fav_comments);
                    bundle.putString("notes", notes);
                    bundle.putString("Place_type", Place_type);
                    ef.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, ef).addToBackStack(null).commit();
                    ((MainHomeFragmentActivity) context).toolbarRightIcon(false);
                }
            });
            // this listener helps edit the already stored favorite place from the list.
            mHolder.placelay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TextView p = (TextView) v.findViewById(R.id.placeTxt);
                    TextView d = (TextView) v.findViewById(R.id.drop_placeTxt);
                    int position = (int) v.getTag();
                    FavId = TaxiUtil.mFavouritelist.get(position).getFavouriteId();
                    fLat = TaxiUtil.mFavouritelist.get(position).getF_lat();
                    fLong = TaxiUtil.mFavouritelist.get(position).getF_lng();
                    fPlace = TaxiUtil.mFavouritelist.get(position).getPlace();
                    dplace = TaxiUtil.mFavouritelist.get(position).getD_Place();
                    dlat = TaxiUtil.mFavouritelist.get(position).getD_Lat();
                    dlong = TaxiUtil.mFavouritelist.get(position).getD_Long();
                    fav_comments = TaxiUtil.mFavouritelist.get(position).getComments();
                    notes = TaxiUtil.mFavouritelist.get(position).getNotes();
                    Place_type = TaxiUtil.mFavouritelist.get(position).getPlace_type();
                    EditFavouriteFrag ef = new EditFavouriteFrag();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("ADD", false);
                    bundle.putString("Id", FavId);
                    bundle.putString("Lat", fLat);
                    bundle.putString("Lng", fLong);
                    bundle.putString("Place", fPlace);
                    bundle.putString("D_place", dplace);
                    bundle.putString("D_lat", dlat);
                    bundle.putString("D_long", dlong);
                    bundle.putString("Cmt", fav_comments);
                    bundle.putString("notes", notes);
                    bundle.putString("Place_type", Place_type);
                    ef.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, ef).addToBackStack(null).commit();
                    ((MainHomeFragmentActivity) context).toolbarRightIcon(false);
//                    Intent i = new Intent(context, EditFavouriteAct.class);
//                    i.putExtra("ADD", false);
//                    i.putExtra("Id", FavId);
//                    i.putExtra("Lat", fLat);
//                    i.putExtra("Lng", fLong);
//                    i.putExtra("Place", fPlace);
//                    i.putExtra("D_place", dplace);
//                    i.putExtra("D_lat", dlat);
//                    i.putExtra("D_long", dlong);
//                    i.putExtra("Cmt", fav_comments);
//                    i.putExtra("notes", notes);
//                    i.putExtra("Place_type", Place_type);
//                    context.startActivity(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

//    private void bookNow(int position) {
//        try {
//            FavId = TaxiUtil.mFavouritelist.get(position).getFavouriteId();
//            fLat = TaxiUtil.mFavouritelist.get(position).getF_lat();
//            fLong = TaxiUtil.mFavouritelist.get(position).getF_lng();
//            fPlace = TaxiUtil.mFavouritelist.get(position).getPlace();
//            dplace = TaxiUtil.mFavouritelist.get(position).getD_Place();
//            dlat = TaxiUtil.mFavouritelist.get(position).getD_Lat();
//            dlong = TaxiUtil.mFavouritelist.get(position).getD_Long();
//            fav_comments = TaxiUtil.mFavouritelist.get(position).getComments();
//            notes = TaxiUtil.mFavouritelist.get(position).getNotes();
//            Place_type = TaxiUtil.mFavouritelist.get(position).getPlace_type();
//            TaxiUtil.FromFav = true;
//            if (fLat != null && fLat != "") {
//                TaxiUtil.p_lat = Double.parseDouble(fLat);
//            }
//            if (fLong != null && fLong != "") {
//                TaxiUtil.p_lng = Double.parseDouble(fLong);
//            }
//            if (dlat != null && dlat != "" && dlat.length() > 0) {
//                TaxiUtil.d_lat = Double.parseDouble(dlat);
//            }
//            if (dlong != null && dlong != "" && dlong.length() > 0) {
//                TaxiUtil.d_lng = Double.parseDouble(dlong);
//            }
//            if (notes == null)
//                notes = "";
//            TaxiUtil.p_place = fPlace;
//            TaxiUtil.d_place = dplace;
//            JSONObject j = new JSONObject();
//            j.put("latitude", fLat);
//            j.put("longitude", fLong);
//            j.put("pickupplace", Uri.encode(fPlace));
//            j.put("dropplace", Uri.encode(dplace));
//            j.put("pickupplace", fPlace);
//            j.put("dropplace", dplace);
//            j.put("drop_latitude", dlat);
//            j.put("drop_longitude", dlong);
//            j.put("pickup_time", PickupTime);
//            j.put("fav_driver_booking_type", book_fav_Driver);
//            j.put("motor_model", SessionSave.getSession("carModel", context));
//            j.put("cityname", "");
//            j.put("distance_away", "");
//            j.put("sub_logid", "");
//            j.put("passenger_id", SessionSave.getSession("Id", context));
//            j.put("request_type", "1");
//            j.put("promo_code", "");
//            j.put("now_after", "0");
//            j.put("notes", notes);
//            j.put("friend_id2", "0");
//            j.put("friend_percentage2", "0");
//            j.put("friend_id3", "0");
//            j.put("friend_percentage3", "0");
//            j.put("friend_id4", "0");
//            j.put("friend_percentage4", "0");
//            j.put("friend_id1", SessionSave.getSession("Id", context));
//            j.put("friend_percentage1", "100");
//            final String url = "type=savebooking";
//            new SearchTaxi(url, j);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onSuccess(View view, Dialog dialog, String resultcode) {
        dialog.dismiss();
        book_fav_Driver = 1;
        //bookNow(clickedPos);

    }

    @Override
    public void onFailure(View view, Dialog dialog, String resultcode) {
        book_fav_Driver = 2;
       // bookNow(clickedPos);
        dialog.dismiss();
    }

    // View holder class member this contains in every row in list.
    class ViewHolder {
        public TextView Place;
        public View img_lineup;
        public View img_linebottom;
        public TextView BookBtn;
        public ImageView place_image, drop_pin;
        private LinearLayout placelay,favitem_contain;
        private TextView drop_placeTxt;
        public ImageView fav_img;
    }


    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
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
     * Is used to convert the values into hour and mins format.
     * @param hours
     * @param mins
     * @param sec
     */

    private void updateTime(int hours, final int mins, final int sec) {

        try {
            String timeSet = "";
            if (hours > 12) {
                hours -= 12;
                timeSet = "PM";
            } else if (hours == 0) {
                hours += 12;
                timeSet = "AM";
            } else if (hours == 12)
                timeSet = "PM";
            else
                timeSet = "AM";
            String minutes = "";
            if (mins < 10)
                minutes = "0" + mins;
            else
                minutes = String.valueOf(mins);
            final String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(':').append(sec).append(" ").append(timeSet).toString();
            PickupTime = aTime;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * This class helps to call the request taxi API and process the response.
     */
    //
//    public class SearchTaxi implements APIResult {
//        String msg = "";
//        Dialog mDialog1;
//
//        public SearchTaxi(final String url, JSONObject data) {
//            // TODO Auto-generated constructor stub
//            new APIService_Retrofit_JSON(context, this, data, false).execute(url);
//        }
//
//        @Override
//        public void getResult(final boolean isSuccess, final String result) {
//
//            if (isSuccess) {
//                try {
//                    final JSONObject json = new JSONObject(result);
//                    if (json.getInt("status") == 1) {
//                        SessionSave.saveSession("trip_id", json.getJSONObject("detail").getString("passenger_tripid"), context);
//                        SessionSave.saveSession("Pass_Tripid", json.getJSONObject("detail").getString("passenger_tripid"), context);
//                        SessionSave.saveSession("request_time", json.getJSONObject("detail").getString("total_request_time"), context);
//                        Toast.makeText(context, "" + json.getString("message"), Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(context, ContinousRequest.class);
//                        context.startActivity(i);
//                    } else if (json.getInt("status") == 2 || json.getInt("status") == 5 || json.getInt("status") == 6 || json.getInt("status") == 3)
//                        Toast.makeText(context, "" + json.getString("message"), Toast.LENGTH_SHORT).show();
//                } catch (final Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
