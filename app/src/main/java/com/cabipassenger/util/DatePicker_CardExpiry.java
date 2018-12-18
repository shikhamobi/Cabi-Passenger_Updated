package com.cabipassenger.util;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.cabipassenger.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * this class is use to display common date pickers
 */
public class DatePicker_CardExpiry extends DialogFragment {
    Calendar cal;
    int curMonth;
    int curYear;

    DatePicker datePicker;
    TextView ok, cancel;
    TextView dialogtitle;
    int initMonth, initYear;

    public interface DateDialogInterface {
        void onSuccess(int month, int year);

        void failure(String inputText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.date_picker_dialog, container, false);
        try {
            if (getArguments() != null) {
                initMonth = getArguments().getInt("month");
                initYear = getArguments().getInt("year");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FontHelper.applyFont(getActivity(), v);
        dialogtitle = (TextView) v.findViewById(R.id.dialogtitle);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cal = Calendar.getInstance();
        curMonth = cal.get(Calendar.MONTH);
        curYear = cal.get(Calendar.YEAR);
        datePicker = (DatePicker) v.findViewById(R.id.datePicker1);
        if (initYear == 0) {
            initYear = curYear;
            initMonth = curMonth;
        }

        try {
            datePicker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
     //   System.out.println("_______2**"+initMonth+"__"+initYear);
        datePicker.init(initYear, initMonth-1, 2, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                FontHelper.overrideFonts(getActivity(), view);
                // int   year = datePicker.getYear();

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM");

                String formatedDate = sdf.format(new Date(year, month, day));
                dialogtitle.setText(formatedDate + " " + view.getYear());
            }
        });

        datePicker.setMinDate(cal.getTimeInMillis() - 1000);
        ok = (TextView) v.findViewById(R.id.ok);
        cancel = (TextView) v.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((DateDialogInterface) getActivity()).onSuccess(datePicker.getMonth() + 1, datePicker.getYear());

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((DateDialogInterface) getActivity()).failure("cancel");
            }
        });


        return v;
    }
}
