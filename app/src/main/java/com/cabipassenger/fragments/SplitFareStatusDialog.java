package com.cabipassenger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;

import com.cabipassenger.R;
import com.cabipassenger.adapter.Splitfare_status_adapter;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;

/**
 * Created by developer on 6/6/16.
 * To show the current status of the split for dialog
 */
public class SplitFareStatusDialog extends DialogFragment {
  public static RecyclerView rv;
    LinearLayout done;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.splitfare_status, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        FontHelper.applyFont(getActivity(), v);
        Colorchange.ChangeColor((ViewGroup)v,getActivity());
        init(v);
        return v;
    }

    private void init(View v) {
        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        done = (LinearLayout) v.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Splitfare_status_adapter adap = new Splitfare_status_adapter(getActivity());
        rv.setAdapter(adap);
        rv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calculeRecyclerViewFullHeight();
            }
        });

    }

    /**
     * Calculate the dimension of the recycler view
     */
    protected void calculeRecyclerViewFullHeight() {
        int height = 0;
        for (int idx = 0; idx < rv.getChildCount(); idx++ ) {
            View v = rv.getChildAt(idx);
            height += v.getHeight();
        }
        ViewGroup.LayoutParams params = rv.getLayoutParams();
        params.height = height;
       rv.setLayoutParams(params);
    }
}
