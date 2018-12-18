package com.cabipassenger.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.util.AppCacheImage;
import com.cabipassenger.util.CL;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.DownloadImageAndsavetoCache;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * this class is used to show fare details
 */

public class FareFrag extends Fragment {
    // Class members declarations.

    private LinearLayout CancelBtn;
    private TextView HeadTitle;

    private TextView fare_detail;

    private String carModel = "1";

    private TextView basefareTxt;
    private TextView minfareTxt;
    private TextView belowfareTxt;
    private TextView abovefareTxt;
    private TextView nightfareTxt;
    private TextView cancelfareTxt;
    private TextView evefareTxt;
    private TextView waitingfareTxt;
    private TextView basekm;
    private TextView minkm;
    private TextView belowkm;
    private TextView abovekm;
    private TextView nighttime, evefare, leftIcon, back_text;
    View view_car1, view_car2, view_car3;
    private LinearLayout farecontain;
    private LinearLayout fare_progress;
    private Dialog mshowDialog;
    private LinearLayout id_all, id_alls;
    private LinearLayout firstCar;
    private LinearLayout parent, eve, night;
    private TextView minutefareTxt;
    private LinearLayout above_lay, below_lay, fare_per_km_lay;
    private TextView far_per_km, far_per_km_txt;

    // Set the layout to activity.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fare_lay, container, false);
        priorChanges(v);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        return v;
    }

    public void priorChanges(View v) {
        farecontain = (LinearLayout) v.findViewById(R.id.farecontain);
        fare_progress = (LinearLayout) v.findViewById(R.id.fare_progress);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        above_lay = (LinearLayout) v.findViewById(R.id.above_lay);
        below_lay = (LinearLayout) v.findViewById(R.id.below_lay);
        fare_per_km_lay = (LinearLayout) v.findViewById(R.id.fare_per_km_lay);
        far_per_km = (TextView) v.findViewById(R.id.far_per_km);
        far_per_km_txt = (TextView) v.findViewById(R.id.far_per_km_txt);
        HeadTitle.setText("" + NC.getResources().getString(R.string.fare_txt));
        leftIcon = (TextView) v.findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        FontHelper.applyFont(getActivity(), farecontain);
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.headlayout));

        ImageView iv = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        Initialize(v);

    }

    // Initialize the views on layout
    public void Initialize(View v) {
        // TODO Auto-generated method stub

        TaxiUtil.current_act = "FareAct";

        FontHelper.applyFont(getActivity(), v.findViewById(R.id.farecontain));
        CancelBtn = (LinearLayout) v.findViewById(R.id.leftIconTxt);

        fare_detail = (TextView) v.findViewById(R.id.fare_detail_txt);

        basefareTxt = (TextView) v.findViewById(R.id.basefareTxt);
        minfareTxt = (TextView) v.findViewById(R.id.minfareTxt);
        belowfareTxt = (TextView) v.findViewById(R.id.belowfareTxt);
        abovefareTxt = (TextView) v.findViewById(R.id.abovefareTxt);
        nightfareTxt = (TextView) v.findViewById(R.id.nightfareTxt);
        eve = (LinearLayout) v.findViewById(R.id.eve);
        night = (LinearLayout) v.findViewById(R.id.night);
        cancelfareTxt = (TextView) v.findViewById(R.id.cancelfareTxt);
        id_all = (LinearLayout) v.findViewById(R.id.id_all);
        id_alls = (LinearLayout) v.findViewById(R.id.id_alls);
        evefareTxt = (TextView) v.findViewById(R.id.evefareTxt);
        waitingfareTxt = (TextView) v.findViewById(R.id.waitingfareTxt);
        minutefareTxt = (TextView) v.findViewById(R.id.minutefareTxt);
        evefare = (TextView) v.findViewById(R.id.evefare);
        basekm = (TextView) v.findViewById(R.id.basekm);
        minkm = (TextView) v.findViewById(R.id.minkm);
        belowkm = (TextView) v.findViewById(R.id.belowkm);
        abovekm = (TextView) v.findViewById(R.id.abovekm);
        nighttime = (TextView) v.findViewById(R.id.nightchrgtime);
        // Call this method to set the car model  API result.
        final String url = "type=getmodel_fare_details";
        new FareUpdate(url);
        setcarModel();
//        select_defaultmodel();
        // To show the Slider menu for move from one activity to another activity.

        // Update the while pick the 1st car model.
//        lay_model_one.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                try {
//                    final JSONArray array = new JSONArray(SessionSave.getSession("model_details_update", getActivity()));
//                    System.out.println("model_details" + SessionSave.getSession("model_details_update", getActivity()));
//                    carModel = array.getJSONObject(0).getString("model_id");
//                    lay_cir_car1.setBackgroundResource(R.drawable.circle_focus);
//
//                    txt_car4.setTextColor(Color.LTGRAY);
//                    txt_dra_car1.setImageResource(R.drawable.fare_car1_focus);
//                    txt_dra_car2.setImageResource(R.drawable.fare_car2_unfocus);
//                    txt_dra_car3.setImageResource(R.drawable.fare_car3_unfocus);
//                    txt_dra_car4.setImageResource(R.drawable.fare_car4_unfocus);
//                    carModel = array.getJSONObject(0).getString("model_id");
//                    fare_detail.setText(txt_car1.getText().toString() + " " + NC.getResources().getString(R.string.fare_txt) + " " + NC.getResources().getString(R.string.details));
//                    select_model(carModel);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        });
        // Update the while pick the 2nd car model.

    }

    /**
     * set car model that listed in back end with its fare list
     */

    public void setcarModel() {

        try {
            final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            if (array.length() <= 4) {
                id_alls.setVisibility(View.GONE);
                for (int n = 0; n < array.length(); n++) {
                    int i = 0;
                    //To reverse the car model array
                    if (SessionSave.getSession("Lang", getContext()).equals("ar") || SessionSave.getSession("Lang", getActivity()).equals("fa"))
                        i = array.length() - (n + 1);
                    else
                        i = n;
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_all, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);
                    carlay.setLayoutParams(params);
                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + array.getJSONObject(i).getString("model_name"));

                        if (!AppCacheImage.loadBitmap(array.getJSONObject(i).getString("unfocus_image"), (ImageView) v.findViewById(R.id.txt_dra_car1))) {
                         //   System.out.println("Image... not avail in cache");
                            new DownloadImageAndsavetoCache((ImageView) v.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(i).getString("unfocus_image"));

                        }

                        /*if (i == 1)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car3_unfocus);
                        if (i == 2)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car2_unfocus);
                        if (i == 3)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car4_unfocus);*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    carlay.setTag(i);
                    carlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int pos = Integer.parseInt(v.getTag().toString());
                            select_model(v, pos, 1);
                        }
                    });

                    id_all.addView(v);
                    if (i == 0) {
                        firstCar = carlay;
                    }
                    if (i < (array.length() - 1)) {
                        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(
                                1, ViewGroup.LayoutParams.MATCH_PARENT);
                        View vv = new View(getActivity());
                        vv.setLayoutParams(paramsv);
                        //  vv.setBackgroundColor(CL.getColor(getActivity(), R.color.line_grey));

                        id_all.addView(vv);
                    }
                }
            } else {
                id_all.setVisibility(View.GONE);
                for (int n = 0; n < array.length(); n++) {
                    int i = 0;
                    //To reverse the car model array
                    if (SessionSave.getSession("Lang", getContext()).equals("ar") || SessionSave.getSession("Lang", getActivity()).equals("fa"))
                        i = array.length() - (n + 1);
                    else
                        i = n;
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_alls, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);
                    ViewGroup.LayoutParams lp = carlay.getLayoutParams();
                    if (lp instanceof ViewGroup.MarginLayoutParams) {
                        ((ViewGroup.MarginLayoutParams) lp).rightMargin = 17;
                        ((ViewGroup.MarginLayoutParams) lp).leftMargin = 17;
                    }
                    carlay.setTag(i);
                    carlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int pos = Integer.parseInt(v.getTag().toString());
                            select_model(v, pos, 0);
                        }
                    });
                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + array.getJSONObject(i).getString("model_name"));

                        if (!AppCacheImage.loadBitmap(array.getJSONObject(i).getString("unfocus_image"), (ImageView) v.findViewById(R.id.txt_dra_car1))) {
                        //    System.out.println("Image... not avail in cache");
                            new DownloadImageAndsavetoCache((ImageView) v.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(i).getString("unfocus_image"));

                        }

                        /*if (i == 1)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car3_unfocus);
                        if (i == 2)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car2_unfocus);
                        if (i == 3)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car4_unfocus);*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    id_alls.addView(v);
                    if (i == 0) {
                        firstCar = carlay;
                    }
                    if (i < (array.length() - 1)) {
                        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(
                                1, ViewGroup.LayoutParams.MATCH_PARENT);
                        View vv = new View(getActivity());
                        vv.setLayoutParams(paramsv);
                        //     vv.setBackgroundColor(CL.getColor(getActivity(), R.color.line_grey));

                        id_alls.addView(vv);
                    }
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
          //  System.out.println("Exception cirlce" + e);
            e.printStackTrace();
        }
    }

    /**
     * this class is used to set the car click position
     */

    private void carlay_click(View v, int type) {

        try {
            final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            int pos = Integer.parseInt(v.getTag().toString());
          //  System.out.println("_____________OO" + pos);
            int carPos = pos;
            if (SessionSave.getSession("Lang", getContext()).equals("ar") || SessionSave.getSession("Lang", getActivity()).equals("fa"))
                pos = array.length() - (pos + 1);
            carModel = array.getJSONObject(carPos).getString("model_id");
            // selectModel = String.valueOf(pos + 1);
            //SessionSave.saveSession("carModel", "" + carModel, getActivity());
            int viewCount = 0;
            if (type == 1)
                parent = id_all;
            else
                parent = id_alls;
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) instanceof ViewGroup) {
                    ViewGroup vv = (ViewGroup) parent.getChildAt(i);
                    if (i != (pos + viewCount)) {
                        vv.findViewById(R.id.lay_cir_car1).setBackgroundResource(R.drawable.circle_unfocus);
                        ((TextView) vv.findViewById(R.id.txt_model1)).setTextColor(CL.getColor(getActivity(), R.color.textviewcolor_light));


                       /* if ((i - viewCount) == 1)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car3_unfocus);
                        else if ((i - viewCount) == 2)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car2_unfocus);
                        else if ((i - viewCount) == 3)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car4_unfocus);
                        else if ((i - viewCount) == 0)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car1_unfocus);*/

                        if ((i - viewCount) == 1) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(1).getString("unfocus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                              //  System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(1).getString("unfocus_image"));
                            }
                        } else if ((i - viewCount) == 2) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(2).getString("unfocus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                               // System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(2).getString("unfocus_image"));
                            }
                        } else if ((i - viewCount) == 3) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(3).getString("unfocus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                              //  System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(3).getString("unfocus_image"));
                            }
                        } else if ((i - viewCount) == 0) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(0).getString("unfocus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                               // System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(0).getString("unfocus_image"));
                            }
                        }
                        //  ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car1_unfocus);
                        (vv.findViewById(R.id.dotsProgressBar1)).setVisibility(View.GONE);
                    } else {
                        //vv.findViewById(R.id.lay_cir_car1).setBackgroundResource(R.drawable.circle_focus);
                        //LinearLayout circlfocus =  (LinearLayout)vv.findViewById(R.id.lay_cir_car1);
                        //Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "circle_focus.png").error(R.drawable.circle_focus).into(circlfocus);
                        //  Log.e("circlefocus__", SessionSave.getSession("image_path", getActivity()) + "circle_focus.png");
                        final LinearLayout temp = (LinearLayout) vv;
                        Glide.with(this).load(SessionSave.getSession("image_path", getActivity()) + "circle_focus.png").asBitmap().into(new SimpleTarget<Bitmap>(81, 81) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable drawable = new BitmapDrawable(resource);
                                temp.findViewById(R.id.lay_cir_car1).setBackground(drawable);

                            }
                        });

                        ((TextView) vv.findViewById(R.id.txt_model1)).setTextColor(CL.getColor(getActivity(), R.color.button_accept));
//                        ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car11_focus);
                        /*if ((i - viewCount) == 1)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car33_focus);
                        else if ((i - viewCount) == 2)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car22_focus);
                        else if ((i - viewCount) == 3)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car44_focus);
                        else if ((i - viewCount) == 0)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car11_focus);*/
                        if ((i - viewCount) == 1) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(1).getString("focus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                             //   System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(1).getString("focus_image"));

                            }
                        } else if ((i - viewCount) == 2) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(2).getString("focus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                              //  System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(2).getString("focus_image"));

                            }
                        } else if ((i - viewCount) == 3) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(3).getString("focus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                              //  System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(3).getString("focus_image"));

                            }
                        } else if ((i - viewCount) == 0) {
                            if (!AppCacheImage.loadBitmap(array.getJSONObject(0).getString("focus_image"), (ImageView) vv.findViewById(R.id.txt_dra_car1))) {
                              //  System.out.println("Image... not avail in cache");
                                new DownloadImageAndsavetoCache((ImageView) vv.findViewById(R.id.txt_dra_car1)).execute(array.getJSONObject(0).getString("focus_image"));

                            }
                        }


                    }
                } else {
                    viewCount = viewCount + 1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


   /* public void setcarModel() {

        try {
            final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            if (array.length() <= 3) {
                id_alls.setVisibility(View.GONE);
                for (int i = 0; i < array.length(); i++) {
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_all, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.weight = 1.0f;
                    LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);
                    carlay.setLayoutParams(params);
                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + array.getJSONObject(i).getString("model_name"));
                        if (i == 1)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car3_unfocus);
                        if (i == 2)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car2_unfocus);
                        if (i == 3)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car4_unfocus);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    carlay.setTag(i);
                    carlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int pos = Integer.parseInt(v.getTag().toString());
                            select_model(v, pos, 1);
                        }
                    });

                    id_all.addView(v);
                    if (i == 0) {
                        firstCar = carlay;
                    }
                    if (i < (array.length() - 1)) {
                        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(
                                1, ViewGroup.LayoutParams.MATCH_PARENT);
                        View vv = new View(getActivity());
                        vv.setLayoutParams(paramsv);
                        vv.setBackgroundColor(CL.getColor(getActivity(), R.color.line_grey));

                        id_all.addView(vv);
                    }
                }
            } else {
                id_all.setVisibility(View.GONE);
                for (int i = 0; i < array.length(); i++) {
                    View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_lay_car, id_alls, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final LinearLayout carlay = (LinearLayout) v.findViewById(R.id.lay_model_one);
                    ViewGroup.LayoutParams lp = carlay.getLayoutParams();
                    if (lp instanceof ViewGroup.MarginLayoutParams) {
                        ((ViewGroup.MarginLayoutParams) lp).rightMargin = 17;
                        ((ViewGroup.MarginLayoutParams) lp).leftMargin = 17;
                    }
                    carlay.setTag(i);
                    carlay.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int pos = Integer.parseInt(v.getTag().toString());
                            select_model(v, pos, 0);
                        }
                    });
                    try {
                        ((TextView) v.findViewById(R.id.txt_model1)).setText("" + array.getJSONObject(i).getString("model_name"));
                        if (i == 1)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car3_unfocus);
                        if (i == 2)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car2_unfocus);
                        if (i == 3)
                            ((ImageView) v.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car4_unfocus);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    id_alls.addView(v);
                    if (i == 0) {
                        firstCar = carlay;
                    }
                    if (i < (array.length() - 1)) {
                        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(
                                1, ViewGroup.LayoutParams.MATCH_PARENT);
                        View vv = new View(getActivity());
                        vv.setLayoutParams(paramsv);
                        vv.setBackgroundColor(CL.getColor(getActivity(), R.color.line_grey));

                        id_alls.addView(vv);
                    }
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception cirlce" + e);
            e.printStackTrace();
        }
    }


    private void carlay_click(View v, int type) {
        int pos = Integer.parseInt(v.getTag().toString());
        try {
            final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
            carModel = array.getJSONObject(pos).getString("model_id");
            // selectModel = String.valueOf(pos + 1);
            //SessionSave.saveSession("carModel", "" + carModel, getActivity());
            int viewCount = 0;
            if (type == 1)
                parent = id_all;
            else
                parent = id_alls;
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) instanceof ViewGroup) {
                    ViewGroup vv = (ViewGroup) parent.getChildAt(i);
                    if (i != (pos + viewCount)) {
                        vv.findViewById(R.id.lay_cir_car1).setBackgroundResource(R.drawable.circle_unfocus);
                        ((TextView) vv.findViewById(R.id.txt_model1)).setTextColor(CL.getColor(getActivity(), R.color.line_grey));
                        if ((i - viewCount) == 1)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car3_unfocus);
                        else if ((i - viewCount) == 2)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car2_unfocus);
                        else if ((i - viewCount) == 3)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car4_unfocus);
                        else if ((i - viewCount) == 0)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car1_unfocus);
                        //  ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car1_unfocus);
                        (vv.findViewById(R.id.dotsProgressBar1)).setVisibility(View.GONE);
                    } else {
                        vv.findViewById(R.id.lay_cir_car1).setBackgroundResource(R.drawable.circle_focus);
                        ((TextView) vv.findViewById(R.id.txt_model1)).setTextColor(Color.WHITE);
//                        ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car11_focus);
                        if ((i - viewCount) == 1)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car33_focus);
                        else if ((i - viewCount) == 2)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car22_focus);
                        else if ((i - viewCount) == 3)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car44_focus);
                        else if ((i - viewCount) == 0)
                            ((ImageView) vv.findViewById(R.id.txt_dra_car1)).setImageResource(R.drawable.car11_focus);


                    }
                } else {
                    viewCount = viewCount + 1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    /**
     * This method used parse the details about the selected car model.
     */


    private void select_model(View v, int pos, int type) {
        carlay_click(v, type);
        try {
            final JSONArray array = new JSONArray(SessionSave.getSession("model_details_update", getActivity()));
            carModel = array.getJSONObject(pos).getString("model_id");
            fare_detail.setText(((TextView) v.findViewById(R.id.txt_model1)).getText().toString() + " - " + NC.getResources().getString(R.string.fare_txt) + " " + NC.getResources().getString(R.string.details));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String base_fare = "";
        String min_fare = "";
        String min_km = "";
        String below_above_km = "";
        String below_km = "";
        String above_km = "";
        String night_charge = "";
        String night_timing_from = "";
        String night_timing_to = "";
        String cancel_fare = "";
        String even_charge = "";
        String waiting_charge = "";
        String minute_fare = "";
        String even_timing_from = "";
        String even_timing_to = "";
        try {
            String model = SessionSave.getSession("model_details_update", getActivity());
           // System.out.println("model_details" + model);
            JSONArray model_array = new JSONArray(model);
            if (model != null) {
                for (int i = 0; i < model_array.length(); i++) {
                    if (carModel.equalsIgnoreCase(model_array.getJSONObject(i).getString("model_id"))) {
                        model_array.getJSONObject(i).getString("model_name");
                        base_fare = model_array.getJSONObject(i).getString("base_fare");
                        min_fare = model_array.getJSONObject(i).getString("min_fare");
                        min_km = model_array.getJSONObject(i).getString("min_km");
                        below_above_km = model_array.getJSONObject(i).getString("below_above_km");
                        below_km = model_array.getJSONObject(i).getString("below_km");
                        above_km = model_array.getJSONObject(i).getString("above_km");
                        night_charge = model_array.getJSONObject(i).getString("night_fare");
                        night_timing_from = model_array.getJSONObject(i).getString("night_timing_from");
                        night_timing_to = model_array.getJSONObject(i).getString("night_timing_to");
                        cancel_fare = model_array.getJSONObject(i).getString("cancellation_fare");
                        even_charge = model_array.getJSONObject(i).getString("evening_fare");
                        waiting_charge = model_array.getJSONObject(i).getString("waiting_fare");
                        minute_fare = model_array.getJSONObject(i).getString("minutes_fare");
                        even_timing_from = model_array.getJSONObject(i).getString("evening_timing_from");
                        even_timing_to = model_array.getJSONObject(i).getString("evening_timing_to");
                        boolean km_wise_fare = Integer.parseInt(model_array.getJSONObject(i).getString("km_wise_fare")) == 1 ? true : false;
                        if (km_wise_fare) {
                            below_lay.setVisibility(View.GONE);
                            above_lay.setVisibility(View.GONE);
                            fare_per_km_lay.setVisibility(View.VISIBLE);
                            far_per_km.setText(getString(R.string.fare_per_km)+" " + SessionSave.getSession("Metric", getActivity()));
                            far_per_km_txt.setText(SessionSave.getSession("Currency", getActivity()) + model_array.getJSONObject(i).getString("additional_fare_per_km"));
                        } else {
                            below_lay.setVisibility(View.VISIBLE);
                            above_lay.setVisibility(View.VISIBLE);
                            fare_per_km_lay.setVisibility(View.GONE);
                            belowkm.setText("" + NC.getResources().getString(R.string.below) + " " + below_above_km + " " + SessionSave.getSession("Metric", getActivity()));
                            abovekm.setText("" + NC.getResources().getString(R.string.above) + " " + below_above_km + " " + SessionSave.getSession("Metric", getActivity()));
                        }
                        minkm.setText("" + NC.getResources().getString(R.string.minimum_fare) + " (" + min_km + " " + SessionSave.getSession("Metric", getActivity()) + ")");
                        basefareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(base_fare)));
                        minfareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(min_fare)));
                        belowfareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(below_km)));
                        abovefareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(above_km)));
                        cancelfareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(cancel_fare)));
                        waitingfareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(waiting_charge)));
                        minutefareTxt.setText(SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", Float.parseFloat(minute_fare)));
                        if (night_charge.trim().equals("0"))
                            night.setVisibility(View.GONE);
                        else
                            night.setVisibility(View.VISIBLE);
                        if (even_charge.trim().equals("0"))
                            eve.setVisibility(View.GONE);
                        else
                            eve.setVisibility(View.VISIBLE);
//                        if(SessionSave.getSession("Lang", CardRegisterAct.this).equals("ar")||SessionSave.getSession("Lang",getActivity()).equals("fa")){
//                        nightfareTxt.setText("%" + night_charge + "");
//                        evefareTxt.setText("%" + even_charge + "");}
//                        else{
                        nightfareTxt.setText("" + night_charge + "%");
                        evefareTxt.setText("" + even_charge + "%");
                        // }
                        if (night_charge.equalsIgnoreCase("") || night_charge.equalsIgnoreCase("0"))
                            nighttime.setText("" + NC.getResources().getString(R.string.nit_fare));
                        else
                            nighttime.setText("" + NC.getResources().getString(R.string.nit_fare) + "\n(" + night_timing_from + " " + NC.getResources().getString(R.string.nit_fare_to) + " " + night_timing_to + ")");
                        if (even_charge.equalsIgnoreCase("") || even_charge.equalsIgnoreCase("0"))
                            evefare.setText("" + NC.getResources().getString(R.string.even_charge));
                        else
                            evefare.setText("" + NC.getResources().getString(R.string.even_charge) + "\n(" + even_timing_from + " " + NC.getResources().getString(R.string.nit_fare_to) + " " + even_timing_to + ")");
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // Update selected and unselected car model UI based on model change.
//    private void setcarModel() {
//
//        try {
//            final JSONArray array = new JSONArray(SessionSave.getSession("model_details", getActivity()));
//            switch (array.length()) {
//                case 1:
//                    carModel = array.getJSONObject(0).getString("model_id");
//                    txt_car1.setText("" + array.getJSONObject(0).getString("model_name"));
//                    lay_model_one.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    txt_car1.setText("" + array.getJSONObject(0).getString("model_name"));
//                    lay_model_one.setVisibility(View.VISIBLE);
//                    txt_car2.setText("" + array.getJSONObject(1).getString("model_name"));
//                    lay_model_two.setVisibility(View.VISIBLE);
//                    view_car1.setVisibility(View.VISIBLE);
//                    break;
//                case 3:
//                    txt_car1.setText("" + array.getJSONObject(0).getString("model_name"));
//                    lay_model_one.setVisibility(View.VISIBLE);
//                    txt_car2.setText("" + array.getJSONObject(1).getString("model_name"));
//                    lay_model_two.setVisibility(View.VISIBLE);
//                    txt_car3.setText("" + array.getJSONObject(2).getString("model_name"));
//                    lay_model_three.setVisibility(View.VISIBLE);
//                    view_car1.setVisibility(View.VISIBLE);
//                    view_car2.setVisibility(View.VISIBLE);
//                    break;
//                case 4:
//                    txt_car1.setText("" + array.getJSONObject(0).getString("model_name"));
//                    lay_model_one.setVisibility(View.VISIBLE);
//                    txt_car2.setText("" + array.getJSONObject(1).getString("model_name"));
//                    lay_model_two.setVisibility(View.VISIBLE);
//                    txt_car3.setText("" + array.getJSONObject(2).getString("model_name"));
//                    lay_model_three.setVisibility(View.VISIBLE);
//                    txt_car4.setText("" + array.getJSONObject(3).getString("model_name"));
//                    lay_model_four.setVisibility(View.VISIBLE);
//                    view_car1.setVisibility(View.VISIBLE);
//                    view_car2.setVisibility(View.VISIBLE);
//                    view_car3.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    break;
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    // Initially set the 1st model fare details and UI.
//    private void select_defaultmodel() {
//
//        try {
//            setcarModel();
//            final JSONArray array = new JSONArray(SessionSave.getSession("model_details_update", getActivity()));
//            carModel = array.getJSONObject(0).getString("model_id");
//            lay_cir_car1.setBackgroundResource(R.drawable.circle_focus);
//            lay_cir_car2.setBackgroundResource(R.drawable.circle_unfocus);
//            lay_cir_car3.setBackgroundResource(R.drawable.circle_unfocus);
//            lay_cir_car4.setBackgroundResource(R.drawable.circle_unfocus);
//            txt_dra_car1.setImageResource(R.drawable.fare_car1_focus);
//            txt_dra_car2.setImageResource(R.drawable.fare_car2_unfocus);
//            txt_dra_car3.setImageResource(R.drawable.fare_car3_unfocus);
//            txt_dra_car4.setImageResource(R.drawable.fare_car4_unfocus);
//            txt_car1.setTextColor(Color.WHITE);
//            txt_car2.setTextColor(Color.LTGRAY);
//            txt_car3.setTextColor(Color.LTGRAY);
//            txt_car4.setTextColor(Color.LTGRAY);
//            fare_detail.setText(txt_car1.getText().toString() + " " + NC.getResources().getString(R.string.fare_txt) + " " + NC.getResources().getString(R.string.details));
//
//            select_model(carModel);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    /**
     * Slider menu used to move from one activity to another activity.
     */


    private class FareUpdate implements APIResult {
        public FareUpdate(final String url) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON_NoProgress(getActivity(), this, "", true).execute(url);
            fare_progress.setVisibility(View.VISIBLE);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            farecontain.setVisibility(View.VISIBLE);
            fare_progress.setVisibility(View.GONE);
           // System.out.println("fare_model" + result);
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        final JSONObject array = json.getJSONObject("detail");
                        SessionSave.saveSession("model_details_update", "" + array.getJSONArray("model_details"), getActivity());
                        if (firstCar != null)
                            firstCar.performClick();
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                   // Log.e("tag", "errrror :" + e.getMessage());
                    e.printStackTrace();
                } catch (final NullPointerException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(getActivity(), getString(R.string.server_con_error));
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (mshowDialog != null && mshowDialog.isShowing()) {
                mshowDialog.dismiss();
                mshowDialog = null;

            }
            TaxiUtil.mActivitylist.remove(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);

    }
}
