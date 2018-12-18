package com.cabipassenger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cabipassenger.data.SplitStatusData;
import com.cabipassenger.fragments.SplitFareStatusDialog;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.AppCacheImage;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.DownloadImageAndsavetoCache;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.RoundedImageView;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * this class is used to display the tripdetails when user book a taxi
 */
public class TripDetailsAct extends MainActivity {
    // Class members declarations.

    private TextView HeadTitle;
    private RoundedImageView driverImg;
    private TextView driverNameTxt;
    private TextView taxiNumberTxt;
    private TextView time;
    private TextView place, booking_txt;
    private TextView paymenttype;
    private TextView jobref;
    private TextView amount;
    private TextView waitingfare;

    private ImageView split_ref;
    private DisplayImageOptions options;
    private TextView dropplace;
    private ImageView ratingBar;

    private String Tripid;
    private Button bookingBtn;
    private TextView passnameTxt;
    private TextView droptimeTxt;
    private TextView distanceTxt;
    private TextView duartionTxt;
    private double mdistance;

    private RelativeLayout tripdetail_contain;
    private SplitFareStatusDialog splitFareDialog;

    /**
     * Set the layout to activity.
     */
    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        setLocale();
        return R.layout.tripdetaillay;
    }

    @Override
    public void priorChanges() {
        super.priorChanges();
        tripdetail_contain = (RelativeLayout) findViewById(R.id.tripdetail_contain);
        FontHelper.applyFont(this, tripdetail_contain);
    }

    /**
     * Initialize the views on layout
     */
    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0)), TripDetailsAct.this);
        TaxiUtil.sContext = this;
        TaxiUtil.mActivitylist.add(this);
        Intent i = getIntent();
        TaxiUtil.current_act = "TripDetailsAct";
        Tripid = i.getStringExtra("Tripid");
        findViewById(R.id.headlayout).setVisibility(View.VISIBLE);
//
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.tripdetail));
        driverImg = (RoundedImageView) findViewById(R.id.driverImg);
        driverNameTxt = (TextView) findViewById(R.id.drivernameTxt);
        taxiNumberTxt = (TextView) findViewById(R.id.taxinoTxt);
        time = (TextView) findViewById(R.id.time);
        amount = (TextView) findViewById(R.id.amount);
        waitingfare = (TextView) findViewById(R.id.waiting);
        split_ref = (ImageView) findViewById(R.id.split_ref);
        place = (TextView) findViewById(R.id.place);
        booking_txt = (TextView) findViewById(R.id.booking_txt);
        dropplace = (TextView) findViewById(R.id.dropplace);
        paymenttype = (TextView) findViewById(R.id.payment);
        jobref = (TextView) findViewById(R.id.jobref);
        bookingBtn = (Button) findViewById(R.id.bookingBtn);
        passnameTxt = (TextView) findViewById(R.id.passnameTxt);
        droptimeTxt = (TextView) findViewById(R.id.droptimeTxt);
        distanceTxt = (TextView) findViewById(R.id.distanceTxt);
        duartionTxt = (TextView) findViewById(R.id.duartionTxt);
        ratingBar = (ImageView) findViewById(R.id.rating);

        options = new DisplayImageOptions.Builder().showImageOnLoading(null).showImageForEmptyUri(null).showImageOnFail(null).resetViewBeforeLoading(false).delayBeforeLoading(1000).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).displayer(new SimpleBitmapDisplayer()).handler(new Handler()).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);
        // API call to get the request trip detail.
        try {
            JSONObject j = new JSONObject();
            j.put("trip_id", Tripid);
            new TripDetail("type=get_trip_detail", j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        // This for close the activity.
//        CancelBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                finish();
//            }
//        });
        // This for move from this activity to search taxi page.
        bookingBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i;
                if (!SessionSave.getSession("trip_id", TripDetailsAct.this).equals("")) {
                    i = new Intent(TripDetailsAct.this, MainHomeFragmentActivity.class);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                    startActivity(i);
                    finish();
                } else {
                    i = new Intent(TripDetailsAct.this, MainHomeFragmentActivity.class);
                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                    startActivity(i);
                    finish();
                }
            }
        });
        split_ref.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                splitFareDialog = new SplitFareStatusDialog();
                splitFareDialog.show(fm, "splitStatus");
            }

        });

    }


    /**
     * This class for call the get_trip_detail API and process the response to update the UI.
     */
    private class TripDetail implements APIResult {
        public TripDetail(String string, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(TripDetailsAct.this, this, data, false).execute(string);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {



            // TODO Auto-generated method
            tripdetail_contain.setVisibility(View.VISIBLE);
            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        String driverImage = json.getJSONObject("detail").getString("driver_image");
                        if (!AppCacheImage.loadBitmap(driverImage, driverImg)) {

                            new DownloadImageAndsavetoCache(driverImg).execute(driverImage);
                            // ImageLoader.getInstance().displayImage(json.getJSONObject("detail").getString("driver_image"), driverImg, options);
                        }

                        String d_name = json.getJSONObject("detail").getString("driver_name");
                        d_name = Character.toUpperCase(d_name.charAt(0)) + d_name.substring(1);
                        driverNameTxt.setText(d_name);
                        passnameTxt.setText(d_name);
                        taxiNumberTxt.setText(json.getJSONObject("detail").getString("taxi_number"));
                        jobref.setText(json.getJSONObject("detail").getString("trip_id"));
                        paymenttype.setText(json.getJSONObject("detail").getString("payment_type"));
                        //booking_time
                        dropplace.setText(json.getJSONObject("detail").getString("drop_location"));
                        place.setText(json.getJSONObject("detail").getString("current_location"));
                        booking_txt.setText((json.getJSONObject("detail").getString("booking_time")));
                        //booking_txt.setText(date_conversion(json.getJSONObject("detail").getString("booking_time")));
                        int mdriverrating = 0;
                        if (!json.getJSONObject("detail").getString("rating").equalsIgnoreCase(""))
                            mdriverrating = Integer.parseInt(json.getJSONObject("detail").getString("driver_rating"));
                        //  droptimeTxt.setText(date_conversion(json.getJSONObject("detail").getString("drop_time")));
                        droptimeTxt.setText((json.getJSONObject("detail").getString("drop_time")));
                        ratingBar.setImageResource(0);
                        if (mdriverrating == 0) {
                            ratingBar.setImageResource(R.drawable.star6);
                        }
                        if (mdriverrating == 1) {
                            ratingBar.setImageResource(R.drawable.star1);
                        }
                        if (mdriverrating == 2) {
                            ratingBar.setImageResource(R.drawable.star2);
                        }
                        if (mdriverrating == 3) {
                            ratingBar.setImageResource(R.drawable.star3);
                        }
                        if (mdriverrating == 4) {
                            ratingBar.setImageResource(R.drawable.star4);
                        }
                        if (mdriverrating == 5) {
                            ratingBar.setImageResource(R.drawable.star5);
                        }
                        if (json.getJSONObject("detail").getString("distance").length() != 0)
                            mdistance = Double.parseDouble(json.getJSONObject("detail").getString("distance"));
                        distanceTxt.setText("" + String.format(Locale.UK, "%.2f", mdistance) + " " +
                                "" + json.getJSONObject("detail").getString("metric"));
                        duartionTxt.setText(json.getJSONObject("detail").getString("trip_duration"));
                        String t_amt = json.getJSONObject("detail").getString("amt");
                        String w_fare = json.getJSONObject("detail").getString("waiting_fare");

                        //waitingfare
                        if (t_amt.length() > 0) {
                            amount.setText(SessionSave.getSession("Currency", TripDetailsAct.this) + "" + String.format(Locale.UK, "%.2f", Double.parseDouble(t_amt)));
                        } else {
                            amount.setText(SessionSave.getSession("Currency", TripDetailsAct.this) + "" + 0);
                        }

                        if (w_fare.length() > 0) {
                            waitingfare.setText(SessionSave.getSession("Currency", TripDetailsAct.this) + "" + String.format(Locale.UK, "%.2f", Double.parseDouble(w_fare)));
                        } else {
                            waitingfare.setText(SessionSave.getSession("Currency", TripDetailsAct.this) + "" + 0);
                        }


                        // time.setText(date_conversion(json.getJSONObject("detail").getString("pickup_time")));
                        time.setText((json.getJSONObject("detail").getString("pickup_time")));
                        try {
                            if (json.getJSONObject("detail").has("isSplit_fare"))
                                if (json.getJSONObject("detail").getInt("isSplit_fare") == 1 ? true : false) {

                                    split_ref.setVisibility(View.VISIBLE);
                                    TaxiUtil.SPLIT_STATUS_ITEM.clear();
                                    JSONArray splitArray = json.getJSONObject("detail").getJSONArray("splitFareDetails");
                                    for (int i = 0; i < splitArray.length(); i++) {
                                        JSONObject SplitArrayData = splitArray.getJSONObject(i);
                                        SplitStatusData obj = new SplitStatusData(getApplicationContext());
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
                    }
                } catch (Exception e) {
                }
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(TripDetailsAct.this, getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
       // Toast.makeText(TripDetailsAct.this, "hiiiiiiiiii", Toast.LENGTH_SHORT).show();

        super.onBackPressed();
        this.finish();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        TaxiUtil.mActivitylist.remove(this);
        super.onDestroy();
    }

    // Slider menu used to move from one activity to another activity.
}