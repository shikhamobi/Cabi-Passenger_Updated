package com.cabipassenger.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.adapter.HelpAdapter;
import com.cabipassenger.data.apiData.HelpResponse;
import com.cabipassenger.service.CoreClient;
import com.cabipassenger.service.ServiceGenerator;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * this class is used to show whether the user face some inconvenience
 */

public class HelpFrag extends Fragment {
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
    private HelpAdapter adapter;
    private String trip_id, detail_title;

    // Set the layout to activity.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.helplay, container, false);

        Bundle b = this.getArguments();
        if (b.getString("trip_id") != null){
            trip_id = b.getString("trip_id");
               detail_title =b.getString("title");}
        priorChanges(v);
        Colorchange.ChangeColor((ViewGroup)v,getActivity());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getString(R.string.help));
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);

        //((MainHomeFragmentActivity) getActivity()).small_title(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainHomeFragmentActivity) getActivity()).small_title(false);
    }

    public void priorChanges(View v) {
        // super.priorChanges();
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.frame_lay));
        back_text = (TextView) v.findViewById(R.id.back_text);
        back_text.setVisibility(View.VISIBLE);
        rightlay = (LinearLayout) v.findViewById(R.id.rightlay);
        rightlay.setVisibility(View.GONE);
        TextView HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.menu_favourites));
        CancelTxt = (TextView) v.findViewById(R.id.leftIcon);
        CancelTxt.setVisibility(View.GONE);
        Initialize(v);
    }

    // Initialize the views on layout
    public void Initialize(View v) {
        // TODO Auto-generated method stub
        FontHelper.applyFont(getActivity(), v.findViewById(R.id.fav_lay2));
        favourite_loading = (ImageView) v.findViewById(R.id.favourite_loading);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(favourite_loading);
        Glide.with(getActivity())
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        //favourite_loading.setVisibility(View.GONE);
        CancelBtn = (LinearLayout) v.findViewById(R.id.leftIconTxt);

        favlay = (LinearLayout) v.findViewById(R.id.editfavlay);

        nodataTxt = (TextView) v.findViewById(R.id.nodataTxt);
        favlay.setVisibility(View.VISIBLE);
        AddFavImg = (TextView) v.findViewById(R.id.addIconTxt);
        EditFavImg = (TextView) v.findViewById(R.id.editIconTxt);
        List = (ListView) v.findViewById(R.id.list);
        List.setDivider(null);
        // To add new favorite place.Move from this activity to edit favorite activity.

//        for (int i = 0; i < 15; i++) {
//            HelpData data = new HelpData("1", "Help us", "");
//            TaxiUtil.mHelplist.add(data);
//            nodataTxt.setVisibility(View.GONE);
//            List.setVisibility(View.VISIBLE);
//        }
//
//        if (TaxiUtil.mHelplist.size() != 0) {
//            mHandler.sendEmptyMessage(0);
//        }


        AddFavImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  Intent i = new Intent(getActivity(), EditFavouriteAct.class);


            }
        });
        getHelp();
    }

    @Override
    public void onStop() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.onStop();
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");

        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).small_title(true);
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(detail_title);
    }
/**
 * this method is used to call the help api
 */

    private void getHelp() {
        CoreClient client = null;
        //   System.out.println("rrc_____get" + url_type + "___" + wholeURL + "___" + data);

        client = new ServiceGenerator(getActivity()).createService(CoreClient.class);
        Call<HelpResponse> coreResponse = null;
        coreResponse = client.helpContent(TaxiUtil.COMPANY_KEY,TaxiUtil.DYNAMIC_AUTH_KEY, SessionSave.getSession("Lang",getActivity()));

        coreResponse.enqueue(new Callback<HelpResponse>() {
            @Override
            public void onResponse(Call<HelpResponse> call, retrofit2.Response<HelpResponse> response) {
                HelpResponse data = null;
                data = response.body();
                favourite_loading.setVisibility(View.GONE);
               // System.out.println("____"+data);
                if (data != null) {
                    try {
                        if (getView() != null) {
                            favourite_loading.setVisibility(View.GONE);

                            adapter = new HelpAdapter(getActivity(), data, trip_id);
                            List.setAdapter(adapter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<HelpResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // This handler used to update the values into list using array adapter.
//    Handler mHandler = new Handler() {
//        private HelpAdapter adapter;
//
//        @Override
//        public void handleMessage(android.os.Message msg) {
//
//            switch (msg.what) {
//                case 0:
//                    adapter = new HelpAdapter(getActivity());
//                    List.setAdapter(adapter);
//                    break;
//            }
//        }
//
//
//    };
}
