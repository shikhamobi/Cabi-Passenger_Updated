package com.cabipassenger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.adapter.PromoAdapter;
import com.cabipassenger.data.apiData.PromoDataList;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

/**
 * Created by developer on 18/8/17.
 */

public class PromoList extends Fragment {
    private RecyclerView list_view;
    PromoDataList dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.promo_list, container, false);
        list_view = (RecyclerView) v.findViewById(R.id.list_view);
        PromoDataList promoDataLists;
        if (!SessionSave.getSession(TaxiUtil.PROMO_LIST, getActivity()).trim().equals("")) {
            promoDataLists = TaxiUtil.fromJson(SessionSave.getSession(TaxiUtil.PROMO_LIST, getActivity()), PromoDataList.class);
         //   System.out.println("heelllll" + promoDataLists.promoDatas.size());

            dataList = new PromoDataList();
            for (int i = 0; i < promoDataLists.promoDatas.size(); i++) {
                System.out.println("__________PPO" + promoDataLists.promoDatas.get(i).getExpiry_date() + "__" + SessionSave.getSession("current_time_local", getActivity(), 0));
                if (promoDataLists.promoDatas.get(i).getExpiry_date() > SessionSave.getSession("current_time_local", getActivity(), 0)) {
                    dataList.promoDatas.add(promoDataLists.promoDatas.get(i));
                }
            }
            if(dataList.promoDatas.size()==0)
                SessionSave.saveSession(TaxiUtil.PROMO_LIST, "", getActivity());
            else
            SessionSave.saveSession(TaxiUtil.PROMO_LIST, TaxiUtil.toString(dataList), getActivity());
            list_view.setAdapter(new PromoAdapter(getActivity(), dataList));
            list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


        return v;
    }

    @Override
    public void onResume() {
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(false);
        ((MainHomeFragmentActivity) getActivity()).toolbar_title.setText(NC.getString(R.string.promo_code));
        super.onResume();
    }

    @Override
    public void onStop() {
        ((MainHomeFragmentActivity) getActivity()).toolbar_title.setText(NC.getString(R.string.menu_profile));
        ((MainHomeFragmentActivity) getActivity()).toolbar_title.setText(NC.getString(R.string.menu_profile));
        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        ((MainHomeFragmentActivity) getActivity()).toolbarRightIcon(true);
        super.onStop();
    }
}
