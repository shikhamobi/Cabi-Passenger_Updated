package com.cabipassenger.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.adapter.FavouriteAdapter;
import com.cabipassenger.data.FavouriteData;
import com.cabipassenger.features.CToast;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.FragPopFront;
import com.cabipassenger.service.APIService_Retrofit_JSON_NoProgress;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * this class is used to favourite the trip
 */
public class FavouriteFrag extends Fragment  implements FragPopFront {

    // Class members declarations.
    private LinearLayout CancelBtn;
    private TextView HeadTitle;
    private TextView AddFavImg;
    private TextView EditFavImg;
    private TextView CancelTxt;
    private TextView nodataTxt;
    private ListView List;

    private LinearLayout rightlay;
    private LinearLayout favlay;
    TextView back_text;
    public Dialog mDialog;
    private ImageView favourite_loading;
    private Dialog alertmDialog;

    // Set the layout to activity.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favouritelay, container, false);
        priorChanges(v);
       // Colorchange.ChangeColor((ViewGroup)v,getActivity());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Colorchange.ChangeColor((ViewGroup)(PaymentOptionFrag.this.v),getActivity());
                Colorchange.ChangeColor((ViewGroup) (((ViewGroup) getActivity()
                        .findViewById(android.R.id.content)).getChildAt(0)), getActivity());
            }
        },100);
        return v;
    }

    public void priorChanges(View v) {
        // super.priorChanges();
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.frame_lay));
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        rightlay = (LinearLayout) v.findViewById(R.id.rightlay);
        rightlay.setVisibility(View.GONE);
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.menu_favourites));
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        Initialize(v);
    }

    // Initialize the views on layout
    public void Initialize(View v) {
        // TODO Auto-generated method stub
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.fav_lay2));
        favourite_loading = (ImageView) v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(favourite_loading);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);
        CancelBtn = (LinearLayout) v.findViewById(R.id.leftIconTxt);
        favlay = (LinearLayout) v.findViewById(R.id.editfavlay);
        nodataTxt = (TextView) v.findViewById(R.id.nodataTxt);
        favlay.setVisibility(View.VISIBLE);
        AddFavImg = (TextView) v.findViewById(R.id.addIconTxt);
        EditFavImg = (TextView) v.findViewById(R.id.editIconTxt);
        List = (ListView) v.findViewById(R.id.list);
        List.setDivider(null);
        // To add new favorite place.Move from this activity to edit favorite activity.
        AddFavImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  Intent i = new Intent(getActivity(), EditFavouriteAct.class);


            }
        });


    }
    /**
     *this method is used to moves to edit favourite fragment
     */

    public void addonClick() {
        EditFavouriteFrag ef = new EditFavouriteFrag();
        Bundle bundle = new Bundle();
        bundle.putBoolean("ADD", true);
        ef.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, ef).addToBackStack(null).commit();
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
    }


    /**
     *Default on resume method used to call the get_favourite_list to update the favorite place list in UI.
     */

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(true);
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.menu_favourites));

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        try {
            JSONObject j = new JSONObject();
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            if (TaxiUtil.isOnline(getActivity())) {
                new GetFavlist("type=get_favourite_list", j);
            } else {
                alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
/**
 * interface for update ui on back press from favourite driver fragment
 */
    @Override
    public void trigger_FragPopFront() {
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(true);
        ((MainHomeFragmentActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.menu_favourites));

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        try {
            JSONObject j = new JSONObject();
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
            if (TaxiUtil.isOnline(getActivity())) {
                new GetFavlist("type=get_favourite_list", j);
            } else {
                alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    /**
     * This class used to get the favorite list
     * <p>
     * <p>
     * This class used to get the favorite list
     * </p>
     *
     * @author developer
     */



    private class GetFavlist implements APIResult {
        private String Message;

        public GetFavlist(String url, JSONObject JO) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON_NoProgress(getActivity(), this, JO, false).execute(url);
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {


            // TODO Auto-generated method stub
            favourite_loading.setVisibility(View.GONE);
            if (TaxiUtil.mFavouritelist.size() != 0) {
                TaxiUtil.mFavouritelist.clear();
            }
            if (isSuccess) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        JSONArray jArry = json.getJSONArray("detail");
                        int length = jArry.length();
                        for (int i = 0; i < length; i++) {
                            FavouriteData data = new FavouriteData(jArry.getJSONObject(i).getString("p_favourite_id"),
                                    jArry.getJSONObject(i).getString("p_fav_latitude"), jArry.getJSONObject(i).getString("p_fav_longtitute"),
                                    jArry.getJSONObject(i).getString("p_favourite_place"), jArry.getJSONObject(i).getString("d_favourite_place"),
                                    jArry.getJSONObject(i).getString("d_fav_latitude"), jArry.getJSONObject(i).getString("d_fav_longtitute"),
                                    jArry.getJSONObject(i).getString("fav_comments"), jArry.getJSONObject(i).getString("notes"),
                                    jArry.getJSONObject(i).getString("fav_loction_type"),
                                    jArry.getJSONObject(i).getString("map_image"));
                            TaxiUtil.mFavouritelist.add(data);
                            nodataTxt.setVisibility(View.GONE);
                            List.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Message = json.getString("message");
                        nodataTxt.setVisibility(View.VISIBLE);
                        List.setVisibility(View.GONE);
                        nodataTxt.setText(Message);
                    }
                    if (TaxiUtil.mFavouritelist.size() != 0) {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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

    /**
     * This handler used to update the values into list using array adapter.
     */

    Handler mHandler = new Handler() {
        private FavouriteAdapter adapter;

        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    if (getView() != null) {
                        adapter = new FavouriteAdapter(getActivity());
                        List.setAdapter(adapter);
                    }
                    break;
            }
        }


    };

    // Slider menu used to move from one activity to another activity.


    @Override
    public void onDetach() {
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
        TaxiUtil.mActivitylist.remove(this);
        super.onDestroy();
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
}