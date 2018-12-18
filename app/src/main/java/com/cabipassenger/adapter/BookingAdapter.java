///package com.Taximobility.adapter;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cabi.ContinousRequest;
//import com.cabi.MainHomeFragmentActivity;
//import com.cabi.R;
//import com.cabi.TripDetailsAct;
//import com.cabi.features.CToast;
//import com.cabi.fragments.BookTaxiNewFrag;
//import com.cabi.fragments.TripHistoryFrag;
//import com.cabi.interfaces.APIResult;
//import com.cabi.interfaces.DialogInterface;
//import com.cabi.service.APIService_Retrofit_JSON;
//import com.cabi.util.FontHelper;
//import com.cabi.util.SessionSave;
//import com.cabi.util.TaxiUtil;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
////This adapter used to show the booking details into list view.
//public class BookingAdapter extends BaseAdapter implements DialogInterface {
//    private final Context context;
//    private final LayoutInflater mInflater;
//    private final Typeface tf;
//    int hour;
//    int minute;
//    int sec;
//    private String PickupTime;
//    boolean isFare;
//    private int clickedPos;
//    private Dialog alertmDialog;
//    private int book_fav_Driver;
//    ArrayList<Boolean> statusAccepted = new ArrayList<>();
//    private Dialog mDialog;
//    private String sReason;
//    private Dialog cvv_Dialog;
//    private Dialog r_mDialog;
//    private String mTripid;
//
//    // constructor
//    public BookingAdapter(Context con) {
//
//        context = con;
//        mInflater = LayoutInflater.from(context);
//        final Calendar cal = Calendar.getInstance();
//        hour = cal.get(Calendar.HOUR_OF_DAY);
//        minute = cal.get(Calendar.MINUTE);
//        sec = cal.get(Calendar.SECOND);
//        updateTime(hour, minute, sec);
//        tf = Typeface.createFromAsset(context.getAssets(), FontHelper.FONT_TYPEFACE_Bold);
//        if (getCount() > 0)
//            if (TaxiUtil.bookinglist.get(0).get("fare").equals("no"))
//                isFare = false;
//            else
//                isFare = true;
//
//
//
//        for (int i = 0; i < getCount(); i++)
//            if (TaxiUtil.bookinglist.get(i).get("travel_status") != null){
//                if (TaxiUtil.bookinglist.get(i).get("travel_status").equals("0"))
//                    statusAccepted.add(false);
//                else
//                    statusAccepted.add(true);}
//            else
//                statusAccepted.add(true);
//
//
//    }
//
//    // Return list size.
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return TaxiUtil.bookinglist.size();
//    }
//
//    // It returns the item detail with select position.
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return TaxiUtil.bookinglist.get(position);
//    }
//
//    // It returns the item id with select position.
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (statusAccepted.get(position))
//            return 1;
//        else
//            return 0;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    // Get the view for each row in the list used view holder.
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        final ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            if (getItemViewType(position) == 1) {
//                convertView = mInflater.inflate(R.layout.booking_list_item, null);
//
//            } else {
//                convertView = mInflater.inflate(R.layout.booking_list_upcoming_item, null);
//
//            }
//
//            FontHelper.applyFont(context, convertView.findViewById(R.id.item_lay));
//
//            holder.drivernameTxt = (TextView) convertView.findViewById(R.id.drivernameTxt);
//            holder.picktime = (TextView) convertView.findViewById(R.id.picktime);
//            holder.picktimecol = (TextView) convertView.findViewById(R.id.picktimecol);
//            holder.picktimeTxt = (TextView) convertView.findViewById(R.id.picktimeTxt);
//            holder.picktimeImg = (ImageView) convertView.findViewById(R.id.picktimeImg);
//            holder.driverImg = (ImageView) convertView.findViewById(R.id.driverImg);
//            holder.pickloc = (TextView) convertView.findViewById(R.id.pickloc);
//            holder.pickloccol = (TextView) convertView.findViewById(R.id.pickloccol);
//            holder.picklocTxt = (TextView) convertView.findViewById(R.id.picklocTxt);
//            holder.picklocImg = (ImageView) convertView.findViewById(R.id.picklocImg);
//            holder.droploc = (TextView) convertView.findViewById(R.id.droploc);
//            holder.droploccol = (TextView) convertView.findViewById(R.id.droploccol);
//            holder.droplocTxt = (TextView) convertView.findViewById(R.id.droplocTxt);
//            holder.droplocImg = (ImageView) convertView.findViewById(R.id.droplocImg);
//            holder.item_lay = (LinearLayout) convertView.findViewById(R.id.item_lay);
//            holder.lay_status = (LinearLayout) convertView.findViewById(R.id.lay_status);
//
//            holder.status_txt = (TextView) convertView.findViewById(R.id.status_txt);
//
//            holder.amount = (TextView) convertView.findViewById(R.id.amount);
//            holder.taxi_model = (TextView) convertView.findViewById(R.id.taxi_model);
//            holder.taxi_no = (TextView) convertView.findViewById(R.id.taxi_no);
//            holder.picktimeTxt.setTypeface(tf);
//            holder.taxi_model.setTypeface(tf);
//            holder.taxi_no.setTypeface(tf);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        try {
//            //  holder.drivernameTxt.setText(Character.toUpperCase(TaxiUtil.bookinglist.get(position).get("drivername").charAt(0)) + TaxiUtil.bookinglist.get(position).get("drivername").substring(1));
//            // holder.picktimeTxt.setText(MainActivity.date_conversion(TaxiUtil.bookinglist.get(position).get("pickuptime")));
//            holder.picktimeTxt.setText((TaxiUtil.bookinglist.get(position).get("pickuptime")));
//            holder.picklocTxt.setText(TaxiUtil.bookinglist.get(position).get("pickup_location"));
//            holder.droplocTxt.setText(TaxiUtil.bookinglist.get(position).get("drop_location"));
//            try{
//            Picasso.with(context).load(TaxiUtil.bookinglist.get(position).get("img")).into(holder.driverImg);}catch(Exception e){
//
//            }
//            holder.taxi_model.setText(TaxiUtil.bookinglist.get(position).get("model_name"));
//            holder.taxi_no.setText(TaxiUtil.bookinglist.get(position).get("taxi_no"));
//            if (getItemViewType(position) == 1)
//                if (TaxiUtil.bookinglist.get(position).get("bookings_type").equalsIgnoreCase("2")) {
//                    holder.lay_bookagain.setVisibility(View.VISIBLE);
//                }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        //        // this listener helps call book the taxi from already stored booking list.
//        if (getItemViewType(position) == 1) {
//
//            holder.lay_bookagain = (LinearLayout) convertView.findViewById(R.id.lay_bookagain);
//            holder.bookaginTxt = (TextView) convertView.findViewById(R.id.bookaginTxt);
//            if (isFare)
//                holder.amount.setText(SessionSave.getSession("Currency", context) + " " + TaxiUtil.bookinglist.get(position).get("fare"));
//            else {
//                holder.amount.setVisibility(View.GONE);
//                holder.bookaginTxt.setVisibility(View.GONE);
//            }
//            holder.lay_bookagain.setOnClickListener(new OnClickListener() {
//
//
//                @Override
//                public void onClick(View v) {
//                    String pickup_latitude;
//                    String pickup_longitude;
//                    String drop_latitude;
//                    String drop_longitude;
//                    String pickup_location;
//                    String drop_location;
//                    String driver_notes;
//                    // TODO Auto-generated method stub
//                    if (!SessionSave.getSession("trip_id", context).equals("")) {
//                        Intent i = new Intent(context, MainHomeFragmentActivity.class);
//                        context.startActivity(i);
//                        Toast.makeText(context, context.getString(R.string.Trip_in_progress), Toast.LENGTH_SHORT).show();
//                    } else {
//
//                        pickup_latitude = TaxiUtil.bookinglist.get(position).get("pickup_latitude");
//                        pickup_longitude = TaxiUtil.bookinglist.get(position).get("pickup_longitude");
//                        drop_latitude = TaxiUtil.bookinglist.get(position).get("drop_latitude");
//                        drop_longitude = TaxiUtil.bookinglist.get(position).get("drop_longitude");
//                        pickup_location = TaxiUtil.bookinglist.get(position).get("pickup_location").replace("\n", " ");
//                        drop_location = TaxiUtil.bookinglist.get(position).get("drop_location").replace("\n", " ");
//                        driver_notes = TaxiUtil.bookinglist.get(position).get("notes");
//                        BookTaxiNewFrag ef = new BookTaxiNewFrag();
//                        Bundle bundle = new Bundle();
//                        bundle.putDouble("pickup_latitude", Double.parseDouble(pickup_latitude));
//                        bundle.putDouble("pickup_longitude", Double.parseDouble(pickup_longitude));
//                        bundle.putDouble("drop_latitude", Double.parseDouble(drop_latitude));
//                        bundle.putDouble("drop_longitude", Double.parseDouble(drop_longitude));
//                        bundle.putString("pickup_location", pickup_location);
//                        bundle.putString("drop_location", drop_location);
//                        bundle.putString("driver_notes", driver_notes);
//                        bundle.putBoolean("book_again", true);
//
//                        ef.setArguments(bundle);
//                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, ef).addToBackStack(null).commit();
//
//
////
//                    }
//                }
//            });
//            holder.item_lay.setOnClickListener(new OnClickListener() {
//                @SuppressLint("NewApi")
//                @Override
//                public void onClick(View v) {
//
//                    try {
//                        // TODO Auto-generated method stub
//                        holder.item_lay.invalidate();
//                        System.out.println("driver travel status" + TaxiUtil.bookinglist.get(position).get("bookings_type"));
//                        if (TaxiUtil.bookinglist.get(position).get("bookings_type").equalsIgnoreCase("2")) {
//
//                            Intent intent;
//                            intent = new Intent(context, TripDetailsAct.class);
//                            intent.putExtra("Tripid", TaxiUtil.bookinglist.get(position).get("passengers_log_id"));
//                            context.startActivity(intent);
//
//                        } else {
//                            SessionSave.saveSession("trip_id", TaxiUtil.bookinglist.get(position).get("passengers_log_id"), context);
//                            Intent intent;
//                            intent = new Intent(context, MainHomeFragmentActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            context.startActivity(intent);
//                            ((Activity) context).finish();
//                        }
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
//            holder.lay_cancel = (LinearLayout) convertView.findViewById(R.id.lay_cancel);
//            holder.lay_cancel.setTag(position);
//            holder.lay_cancel.setOnClickListener(new View.OnClickListener()
//            {
//
//                @Override
//                public void onClick(View v) {
//                    v.setClickable(false);
//                    int pos=Integer.parseInt(v.getTag().toString());
//                    mTripid=TaxiUtil.bookinglist.get(pos).get("passengers_log_id");
//                    callCancel();
//                    v.setClickable(true);
//                }
//            });
//
//        }
//        return convertView;
//    }
//
//    @Override
//    public void onSuccess(View view, Dialog dialog, String resultcode) {
//        book_fav_Driver = 1;
//        book_now(clickedPos);
//        dialog.dismiss();
//    }
//
//    @Override
//    public void onFailure(View view, Dialog dialog, String resultcode) {
//        book_fav_Driver = 2;
//        book_now(clickedPos);
//        dialog.dismiss();
//    }
//
//
//    public void callCancel() {
//        final View view = View.inflate(context, R.layout.netcon_lay, null);
//        mDialog = new Dialog(context, R.style.dialogwinddow);
//        mDialog.setContentView(view);
//        mDialog.setCancelable(true);
//        mDialog.show();
//        FontHelper.applyFont(context, mDialog.findViewById(R.id.alert_id));
//        final TextView title_text = (TextView) mDialog.findViewById(R.id.title_text);
//        final TextView message_text = (TextView) mDialog.findViewById(R.id.message_text);
//        final Button button_success = (Button) mDialog.findViewById(R.id.button_success);
//        final Button button_failure = (Button) mDialog.findViewById(R.id.button_failure);
//        title_text.setText("" + context.getResources().getString(R.string.message));
//        message_text.setText("" + context.getResources().getString(R.string.cancel_in_going_trip));
//        button_success.setText("" + context.getResources().getString(R.string.yes));
//        button_failure.setText("" + context.getResources().getString(R.string.no));
//        button_failure.setVisibility(View.VISIBLE);
//        button_success.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(final View v) {
//                // TODO
//                // Auto-generated
//                // method stub
//                mDialog.dismiss();
//                final View view = View.inflate(context, R.layout.reason_list, null);
//                r_mDialog = new Dialog(context, R.style.dialogwinddow);
//                r_mDialog.setContentView(view);
//                r_mDialog.setCancelable(true);
//                r_mDialog.show();
//                FontHelper.applyFont(context, r_mDialog.findViewById(R.id.reasonid));
//                final LinearLayout lay_reason_one = (LinearLayout) r_mDialog.findViewById(R.id.lay_reason_one);
//                final LinearLayout lay_reason_two = (LinearLayout) r_mDialog.findViewById(R.id.lay_reason_two);
//                final LinearLayout lay_reason_three = (LinearLayout) r_mDialog.findViewById(R.id.lay_reason_three);
//                final ImageView check1 = (ImageView) r_mDialog.findViewById(R.id.check1);
//                final ImageView check2 = (ImageView) r_mDialog.findViewById(R.id.check2);
//                final ImageView check3 = (ImageView) r_mDialog.findViewById(R.id.check3);
//                final TextView reason1 = (TextView) r_mDialog.findViewById(R.id.reason1);
//                final TextView reason2 = (TextView) r_mDialog.findViewById(R.id.reason2);
//                final TextView reason3 = (TextView) r_mDialog.findViewById(R.id.reason3);
//                lay_reason_one.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO
//                        // Auto-generated
//                        // method
//                        // stub
//                        check1.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_select));
//                        check2.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_unselect));
//                        check3.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_unselect));
//                        sReason = reason1.getText().toString();
//                        if (SessionSave.getSession("Credit_Card", context).equals("1"))
//                            getCVV();
//                        else {
//                            try {
//                                JSONObject j = new JSONObject();
//                                j.put("passenger_log_id", mTripid);
//                                j.put("travel_status", "4");
//                                j.put("remarks", sReason);
//                                j.put("pay_mod_id", "2");
//                                j.put("creditcard_cvv", "");
//                                r_mDialog.dismiss();
//                                new Cancel_afterTrip("type=cancel_trip", j);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//                lay_reason_two.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO
//                        // Auto-generated method stub
//                        check2.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_select));
//                        check3.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_unselect));
//                        check1.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_unselect));
//                        sReason = reason2.getText().toString();
//                        if (SessionSave.getSession("Credit_Card", context).equals("1"))
//                            getCVV();
//                        else {
//                            try {
//                                JSONObject j = new JSONObject();
//                                j.put("passenger_log_id", mTripid);
//                                j.put("travel_status", "4");
//                                j.put("remarks", sReason);
//                                j.put("pay_mod_id", "2");
//                                j.put("creditcard_cvv", "");
//                                new Cancel_afterTrip("type=cancel_trip", j);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//                lay_reason_three.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO
//                        // Auto-generated method stub
//                        check3.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_select));
//                        check2.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_unselect));
//                        check1.setImageDrawable(context.getResources().getDrawable(R.drawable.reason_unselect));
//                        sReason = reason3.getText().toString();
//                        if (SessionSave.getSession("Credit_Card", context).equals("1"))
//                            getCVV();
//                        else {
//                            try {
//                                JSONObject j = new JSONObject();
//                                j.put("passenger_log_id", mTripid);
//                                j.put("travel_status", "4");
//                                j.put("remarks", sReason);
//                                j.put("pay_mod_id", "2");
//                                j.put("creditcard_cvv", "");
//                                new Cancel_afterTrip("type=cancel_trip", j);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//                });
//            }
//        });
//        button_failure.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                // TODO
//                // Auto-generated
//                // method stub
//                mDialog.dismiss();
//            }
//        });
//
//
//    }
//
//    private class Cancel_afterTrip implements APIResult {
//
//        String alert_message = "";
//
//        public Cancel_afterTrip(String url, JSONObject data) {
//            // TODO Auto-generated constructor stub
//            System.out.println("_____"+url+"____"+data);
//            new APIService_Retrofit_JSON(context, this, data, false, TaxiUtil.APIBase_Path + "lang=" + SessionSave.getSession("Lang", context) + "&" + url).execute();
//            // new APIService_Volley_JSON(context, context, data, false).execute(url);
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, final String result) {
//
//            try {
//                if (isSuccess) {
//                    JSONObject json = new JSONObject(result);
//                    if (json.getInt("status") == 1) {
//                        SessionSave.saveSession("trip_id", "", context);
//                        SessionSave.saveSession("TaxiStatus", "", context);
//                        alert_message = json.getString("message") + "\n" + context.getResources().getString(R.string.canceled_amount) + " " + SessionSave.getSession("Currency", context) + json.getString("cancellation_amount") + "\n" + context.getResources().getString(R.string.canceled_from) + " " + json.getString("cancellation_from");
//
//                        TripHistoryFrag.txt_up_coming_r.performClick();
////                        Intent intent = new Intent(context, GetPassengerUpdate.class);
////                        context.stopService(intent);
////                        Intent i_gettaxi = new Intent(context, MainHomeFragmentActivity.class);
////                        i_gettaxi.putExtra("alert_message", alert_message);
////                        startActivity(i_gettaxi);
////                        context.finish();
//                    } else if (json.getInt("status") == 2) {
//                        SessionSave.saveSession("trip_id", "", context);
//                        SessionSave.saveSession("TaxiStatus", "", context);
//                        alert_message = json.getString("message");
////                        Intent intent = new Intent(context, GetPassengerUpdate.class);
////                        context.stopService(intent);
////                        Intent i_gettaxi = new Intent(context, MainHomeFragmentActivity.class);
////                        i_gettaxi.putExtra("alert_message", alert_message);
////                        startActivity(i_gettaxi);
////                        context.finish();
//                    } else if (json.getInt("status") == -1) {
//                        CToast.ShowToast(context, json.getString("message"));
////                        SessionSave.saveSession("TaxiStatus", "", context);
////                        SessionSave.saveSession("trip_id", "", context);
////                        alert_message = json.getString("message");
////                        Intent intent = new Intent(context, GetPassengerUpdate.class);
////                        context.stopService(intent);
////                        Intent i_gettaxi = new Intent(context, MainHomeFragmentActivity.class);
////                        i_gettaxi.putExtra("alert_message", alert_message);
////                        startActivity(i_gettaxi);
//                        //context.finish();
//                    } else if (json.getInt("status") == 3) {
//                        CToast.ShowToast(context, json.getString("message"));
//                    } else {
//                        CToast.ShowToast(context, json.getString("message"));
//                    }
//                } else {
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    //                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void getCVV() {
//
//        System.out.println("creditcard status" + SessionSave.getSession("Credit_Card", context));
//        try {
//            if (SessionSave.getSession("Credit_Card", context).equalsIgnoreCase("1")) {
//                if(r_mDialog!=null)
//                    r_mDialog.dismiss();
//                final View view = View.inflate(context, R.layout.forgot_popup, null);
//                cvv_Dialog = new Dialog(((Activity) context), R.style.NewDialog);
//                FontHelper.applyFont(context, view);
//                cvv_Dialog.setContentView(view);
//                cvv_Dialog.setCancelable(true);
//                cvv_Dialog.show();
//                final TextView title = (TextView) cvv_Dialog.findViewById(R.id.f_textview);
//                final EditText cvv = (EditText) cvv_Dialog.findViewById(R.id.forgotmail);
//                final Button OK = (Button) cvv_Dialog.findViewById(R.id.okbtn);
//                final Button Cancel = (Button) cvv_Dialog.findViewById(R.id.cancelbtn);
//                title.setText("" + context.getResources().getString(R.string.enter_the_cvv));
//                cvv.setHint("" + context.getResources().getString(R.string.enter_the_cvv));
//                cvv.setInputType(InputType.TYPE_CLASS_NUMBER);
//                InputFilter[] filterArray = new InputFilter[1];
//                filterArray[0] = new InputFilter.LengthFilter(4);
//                cvv.setFilters(filterArray);
//                OK.setOnClickListener(new OnClickListener() {
//                    private String sCvv;
//
//                    @Override
//                    public void onClick(final View v) {
//                        // TODO Auto-generated method stub
//                        try {
//                            sCvv = cvv.getText().toString();
//                            if (sCvv.trim().length() == 0 || sCvv.trim().length() > 4)
//                                alert_view(context, "" + context.getResources().getString(R.string.message), "" + context.getResources().getString(R.string.enter_the_valid_CVV), "" + context.getResources().getString(R.string.ok), "");
//                            else {
//                                Toast.makeText(context, mTripid, Toast.LENGTH_SHORT).show();
//                                JSONObject j = new JSONObject();
//                                j.put("passenger_log_id", mTripid);
//                                j.put("travel_status", "4");
//                                j.put("remarks", sReason);
//                                j.put("pay_mod_id", "2");
//                                j.put("creditcard_cvv", sCvv);
//                                new Cancel_afterTrip("type=cancel_trip", j);
//                                cvv.setText("");
//                                cvv_Dialog.dismiss();
//                            }
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                Cancel.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        // TODO Auto-generated method stub
//
//                        cvv.requestFocus();
//                       // cvv.onEditorAction(EditorInfo.IME_ACTION_DONE);
//
//                        if (cvv != null) {
//                            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(cvv.getWindowToken(), 0);
//                        }
//                        cvv_Dialog.dismiss();
//                    }
//                });
//            } else {
//                r_mDialog.dismiss();
//                JSONObject j = new JSONObject();
//                j.put("passenger_log_id", mTripid);
//                j.put("travel_status", "4");
//                j.put("remarks", sReason);
//                j.put("pay_mod_id", "2");
//                j.put("creditcard_cvv", "");
//                new Cancel_afterTrip("type=cancel_trip", j);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//
//    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
//        try {
//            final View view = View.inflate(mContext, R.layout.alert_view, null);
//            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
//            alertmDialog.setContentView(view);
//            alertmDialog.setCancelable(true);
//            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
//            alertmDialog.show();
//            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
//            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
//            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
//            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
//            button_failure.setVisibility(View.GONE);
//            title_text.setText(title);
//            message_text.setText(message);
//            button_success.setText(success_txt);
//            button_success.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//                    alertmDialog.dismiss();
//                }
//            });
//            button_failure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//                    alertmDialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    public void book_now(int position) {
//        String pickup_latitude;
//        String pickup_longitude;
//        String drop_latitude;
//        String drop_longitude;
//        String pickup_location;
//        String drop_location;
//        String driver_notes;
//        try {
//            pickup_latitude = TaxiUtil.bookinglist.get(position).get("pickup_latitude");
//            pickup_longitude = TaxiUtil.bookinglist.get(position).get("pickup_longitude");
//            drop_latitude = TaxiUtil.bookinglist.get(position).get("drop_latitude");
//            drop_longitude = TaxiUtil.bookinglist.get(position).get("drop_longitude");
//            pickup_location = TaxiUtil.bookinglist.get(position).get("pickup_location").replace("\n", " ");
//            drop_location = TaxiUtil.bookinglist.get(position).get("drop_location").replace("\n", " ");
//            driver_notes = TaxiUtil.bookinglist.get(position).get("notes");
//            JSONObject j = new JSONObject();
//            j.put("latitude", pickup_latitude);
//            j.put("longitude", pickup_longitude);
//            j.put("pickupplace", Uri.encode(pickup_location));
//            j.put("dropplace", Uri.encode(drop_location));
//            j.put("drop_latitude", drop_latitude);
//            j.put("drop_longitude", drop_longitude);
//            j.put("pickup_time", PickupTime);
//            j.put("motor_model", SessionSave.getSession("carModel", context));
//            j.put("cityname", "");
//            j.put("distance_away", "");
//            j.put("sub_logid", "");
//            j.put("fav_driver_booking_type", book_fav_Driver);
//            j.put("passenger_id", SessionSave.getSession("Id", context));
//            j.put("request_type", "1");
//            j.put("promo_code", "");
//            j.put("now_after", "0");
//            j.put("notes", driver_notes);
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
//
//    private void updateTime(int hours, final int mins, int sec) {
//
//
//        try {
//            String timeSet = "";
//            if (hours > 12) {
//                hours -= 12;
//                timeSet = "PM";
//            } else if (hours == 0) {
//                hours += 12;
//                timeSet = "AM";
//            } else if (hours == 12)
//                timeSet = "PM";
//            else
//                timeSet = "AM";
//            String minutes = "";
//            if (mins < 10)
//                minutes = "0" + mins;
//            else
//                minutes = String.valueOf(mins);
//            final String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(':').append(sec).append(" ").append(timeSet).toString();
//            PickupTime = aTime;
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    // View holder class member this contains in every row in list.
//    class ViewHolder {
//        TextView drivernameTxt;
//        TextView picktime;
//        TextView picktimecol;
//        TextView picktimeTxt;
//        ImageView picktimeImg;
//        TextView pickloc;
//        TextView pickloccol;
//        TextView picklocTxt;
//        ImageView picklocImg;
//        TextView droploc;
//        TextView droploccol;
//        TextView droplocTxt;
//        ImageView droplocImg;
//        LinearLayout item_lay;
//        TextView status_txt;
//        TextView bookaginTxt, amount, taxi_model, taxi_no;
//        LinearLayout lay_status, lay_bookagain, lay_cancel;
//        ImageView driverImg;
//    }
//
//    public class SearchTaxi implements APIResult {
//        String msg = "";
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
//            } else {
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//        }
//    }
//}
