package com.cabipassenger.fragments;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cabipassenger.R;
import com.cabipassenger.features.ApproximateCalculation;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.splitfareDialog;
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

import java.util.ArrayList;
import java.util.Locale;

import static android.view.WindowManager.*;

/**
 * Created by developer on 4/1/16.
 * Dialogfragment to show the current status of the split fare with friends
 */
public class SplitFareDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    static ArrayList<String> phoneValueArr = new ArrayList<String>();
    static ArrayList<String> nameValueArr = new ArrayList<String>();
    AutoCompleteTextView friend_name1, friend_name2, friend_name3;
    ArrayAdapter<String> adapter;
    Spinner percentSpn, percentSpn1, percentSpn2, percentSpn3;
    ImageView percent_add_icon1, percent_add_icon2, percent_add_icon3;
    private View currentView;
    private Button btn_continue;
    private int friend1S, friend2S, friend3S;
    private double friend1S_a, friend2S_a, friend3S_a;
    TextView friend_name;
    double percentArray[] = {100, 50, 33.33, 25, 0};
    //  int percentArray_user[] = {25, 50, 75, 100};
    ArrayList<Integer> dynamicPercent = new ArrayList<>();
    ArrayList<Integer> dynamicPercent_U = new ArrayList<>();
    double app_fare;
    private TextView your_fare, your_fare1, your_fare2, your_fare3;
    private Button btn_cancel;
    private LinearLayout split_progress;
    private Dialog alertmDialog;
    private ArrayAdapter<Integer> P_adapter, S_adapter;
    private int prev_selected_item;
    private String phoneNo1;
    private String phoneNo2;
    Cursor c = null;
    private RoundedImageView primary_user_img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.split_fare, container, false);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // getDialog().setStyle( DialogFragment.STYLE_NORMAL, R.style.You_Dialog );
        FontHelper.applyFont(getActivity(), v);
     //   System.out.println("______onccc");
        friend_name = (TextView) v.findViewById(R.id.friend_name);
        friend_name1 = (AutoCompleteTextView) v.findViewById(R.id.friend_name1);
        friend_name2 = (AutoCompleteTextView) v.findViewById(R.id.friend_name2);
        friend_name3 = (AutoCompleteTextView) v.findViewById(R.id.friend_name3);
        friend_name.setText(SessionSave.getSession("ProfileName", getActivity()));
        primary_user_img = (RoundedImageView) v.findViewById(R.id.primary_user_img);
        percent_add_icon1 = (ImageView) v.findViewById(R.id.percent_add_icon1);
        percent_add_icon2 = (ImageView) v.findViewById(R.id.percent_add_icon2);
        percent_add_icon3 = (ImageView) v.findViewById(R.id.percent_add_icon3);
        your_fare = (TextView) v.findViewById(R.id.your_fare);
        your_fare1 = (TextView) v.findViewById(R.id.your_fare1);
        your_fare2 = (TextView) v.findViewById(R.id.your_fare2);
        your_fare3 = (TextView) v.findViewById(R.id.your_fare3);
        split_progress = (LinearLayout) v.findViewById(R.id.split_progress);
        btn_continue = (Button) v.findViewById(R.id.btn_continue);
        btn_cancel = (Button) v.findViewById(R.id.cancel);
        percentSpn = (Spinner) v.findViewById(R.id.percentSpn);
        percentSpn1 = (Spinner) v.findViewById(R.id.percentSpn1);
        percentSpn2 = (Spinner) v.findViewById(R.id.percentSpn2);
        percentSpn3 = (Spinner) v.findViewById(R.id.percentSpn3);
        percentSpn.setSelection(3);
        String.format(Locale.UK, "%.2f", app_fare);
        your_fare.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", app_fare));
        your_fare1.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", 0.0));
        your_fare2.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", 0.0));
        your_fare3.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", 0.0));
        // System.out.println("____________zzzz"+SessionSave.getSession("ProfileImage", getActivity()));
        Picasso.with(getActivity()).load(SessionSave.getSession("ProfileImage", getActivity())).into(primary_user_img);
        percentSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           //     System.out.println("_______Selected" + position + "___" + percentArray[position]);
                double value = app_fare * ((double) percentArray[position] / 100.00);
                your_fare.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", value));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        percentSpn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                prev_selected_item = percentSpn1.getSelectedItemPosition();
                return false;
            }
        });
        percentSpn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                prev_selected_item = percentSpn2.getSelectedItemPosition();
                return false;
            }
        });
        percentSpn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                prev_selected_item = percentSpn3.getSelectedItemPosition();
                return false;
            }
        });
        percentSpn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double value = app_fare * ((double) percentArray[position] / 100.00);

                your_fare1.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", value));
//                if (!isSpinnerValid())
//                    percentSpn1.setSelection(prev_selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        percentSpn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double value = app_fare * ((double) percentArray[position] / 100.00);
                your_fare2.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", value));
//                if (!isSpinnerValid())
//                    percentSpn2.setSelection(prev_selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        percentSpn3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double value = app_fare * ((double) percentArray[position] / 100.00);
                your_fare3.setText(NC.getString(R.string.ur_approx_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", value));
//                if (!isSpinnerValid())
//                    percentSpn3.setSelection(prev_selected_item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        percent_add_icon1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                friend_name1.setText("");
                friend_name1.setTag(0);
                spinAdapterChange();
                setSpinner();
            }
        });
        percent_add_icon2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                friend_name2.setText("");
                friend_name2.setTag(0);
                spinAdapterChange();
                setSpinner();
            }
        });
        percent_add_icon3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                friend_name3.setTag(0);
                spinAdapterChange();
                friend_name3.setText("");
                setSpinner();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /*
         * adapter = new ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, new ArrayList<String>());
		 */
        app_fare = ApproximateCalculation.aprrox_fare;

     //   System.out.println("Value: " + String.format(Locale.UK, "%.2f", app_fare));
        ((TextView) v.findViewById(R.id.get_friend)).setText(NC.getString(R.string.total_aprox_fare) + " " + SessionSave.getSession("Currency", getActivity()) + String.format(Locale.UK, "%.2f", app_fare));
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.contact_search, new ArrayList<String>());
        new AsyncTask_readContactData().execute();
        btn_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSubmitSplitValid()) {
                    ((splitfareDialog) getActivity()).onSplitSuccess(percentArray[percentSpn.getSelectedItemPosition()], friend1S, friend2S, friend3S, friend1S_a, friend2S_a, friend3S_a);
                    dismiss();
                } else {
                    //  alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.percentTotal), "" + NC.getResources().getString(R.string.ok), "");

                }
            }
        });


        return v;
    }

    /**
     * To validate whether the split is valid
     *
     * @return --->true if valid
     * --->else false on invalid
     */
    private boolean isSplitValid() {
        boolean valid = true;
        int friendTotal = 0;

        if ((Integer) friend_name1.getTag() != 0) {
            double amt = percentArray[percentSpn1.getSelectedItemPosition()];
            friendTotal += amt;
            friend1S = (Integer) friend_name1.getTag();
            friend1S_a = amt;
        }
        if ((Integer) friend_name2.getTag() != 0) {
            double amt = percentArray[percentSpn2.getSelectedItemPosition()];
            friendTotal += amt;
            friend2S = (Integer) friend_name2.getTag();
            friend2S_a = amt;
        }
        if ((Integer) friend_name3.getTag() != 0) {
            double amt = percentArray[percentSpn3.getSelectedItemPosition()];
            friendTotal += amt;
            friend3S = (Integer) friend_name3.getTag();
            friend3S_a = amt;
        }
        friendTotal += percentArray[percentSpn.getSelectedItemPosition()];
        if (!(friendTotal > 98 && friendTotal <= 100)) {
            valid = false;
            //Toast.makeText(getActivity(), "Split not valid", Toast.LENGTH_SHORT).show();
        }
        if (friend_name1.getError() != null || friend_name2.getError() != null || friend_name3.getError() != null) {
            valid = false;
            Toast.makeText(getActivity(), "Valid Friend Name", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    /**
     * this method is used to return the amount which splitted
     */

    private boolean isSubmitSplitValid() {
        boolean valid = true;
        double friendTotal = 0;
        if ((Integer) friend_name1.getTag() != 0) {
            double amt = percentArray[percentSpn1.getSelectedItemPosition()];
            friendTotal += amt;
            friend1S = (Integer) friend_name1.getTag();
            friend1S_a = amt;
        }
        if ((Integer) friend_name2.getTag() != 0) {
            double amt = percentArray[percentSpn2.getSelectedItemPosition()];
            friendTotal += amt;
            friend2S = (Integer) friend_name2.getTag();
            friend2S_a = amt;
        }
        if ((Integer) friend_name3.getTag() != 0) {
            double amt = percentArray[percentSpn3.getSelectedItemPosition()];
            friendTotal += amt;
            friend3S = (Integer) friend_name3.getTag();
            friend3S_a = amt;
        }
        double amt1 = percentArray[percentSpn1.getSelectedItemPosition()];
        double amt2 = percentArray[percentSpn2.getSelectedItemPosition()];
        double amt3 = percentArray[percentSpn3.getSelectedItemPosition()];
     //   System.out.println("_____**" + amt1 + "__" + amt2 + "__" + amt3);
//        if ((Integer) friend_name1.getTag() == 0 && amt1 > 0) {
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.Select_friend), "" + NC.getResources().getString(R.string.ok), "");
//            friend_name1.requestFocus();
//            valid = false;
//        } else if ((Integer) friend_name2.getTag() == 0 && amt2 > 0) {
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.Select_friend), "" + NC.getResources().getString(R.string.ok), "");
//            friend_name2.requestFocus();
//            valid = false;
//        } else if ((Integer) friend_name3.getTag() == 0 && amt3 > 0) {
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.Select_friend), "" + NC.getResources().getString(R.string.ok), "");
//            friend_name3.requestFocus();
//            valid = false;
//        }
//        if ((Integer) friend_name1.getTag() != 0 && amt1 <= 0) {
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.Select_percent) + " " + friend_name1.getText(), "" + NC.getResources().getString(R.string.ok), "");
////            friend_name1.requestFocus();
//            valid = false;
//        } else if ((Integer) friend_name2.getTag() != 0 && amt2 <= 0) {
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.Select_percent) + " " + friend_name2.getText(), "" + NC.getResources().getString(R.string.ok), "");
//            //friend_name2.requestFocus();
//            valid = false;
//        } else if ((Integer) friend_name3.getTag() != 0 && amt3 == 0) {
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.Select_percent) + " " + friend_name3.getText(), "" + NC.getResources().getString(R.string.ok), "");
////            friend_name3.requestFocus();
//            valid = false;
//        }
        // friendTotal = amt1 + amt2 + amt3;
        friendTotal += percentArray[percentSpn.getSelectedItemPosition()];
    //    System.out.println("__________" + friendTotal);
        if (!(friendTotal > 98 && friendTotal <= 100)) {
            valid = false;
            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.percentTotal), "" + NC.getResources().getString(R.string.ok), "");

            //Toast.makeText(getActivity(), "Split not valid", Toast.LENGTH_SHORT).show();
        }
//        if (friend_name1.getError() != null || friend_name2.getError() != null || friend_name3.getError() != null) {
//            valid = false;
//            Toast.makeText(getActivity(), "Valid Friend Name", Toast.LENGTH_SHORT).show();
//        }
        return valid;
    }

    /**
     * this method is used for spinner values
     */

    private boolean isSpinnerValid() {
        boolean valid = true;
        double friendTotal = 0;

        double amt1 = percentArray[percentSpn1.getSelectedItemPosition()];
        double amt2 = percentArray[percentSpn2.getSelectedItemPosition()];
        double amt3 = percentArray[percentSpn3.getSelectedItemPosition()];

        friendTotal = amt1 + amt2 + amt3;
        friendTotal += percentArray[percentSpn.getSelectedItemPosition()];
        if (!(friendTotal > 98 && friendTotal <= 100)) {
            valid = false;
            //Toast.makeText(getActivity(), "Split not valid", Toast.LENGTH_SHORT).show();
            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), NC.getResources().getString(R.string.percentTotal), "" + NC.getResources().getString(R.string.ok), "");

        }
        if (friend_name1.getError() != null || friend_name2.getError() != null || friend_name3.getError() != null) {
            valid = false;
            Toast.makeText(getActivity(), "Valid Friend Name", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch (parent.getId()) {
//            case R.id.friend_name1:
//                if (!isSplitValid()) {
//
//                    JSONObject j = new JSONObject();
//                    try {
//                        j.put("phone", phoneValueArr.get(position));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    final String url = "type=passenger_mobile_check";
//                    new IsPassengerAvailable(url, j);
//                } else {
//                    alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + "Split not valid", "" + NC.getResources().getString(R.string.ok), "");
//                }
//                break;
//
//        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = ((TextView) view);
        //if (!isSpinnerValid()) {

        String phno = tv.getText().toString();
        String phnoA[] = phno.split("\\(");
        JSONObject j = new JSONObject();
        try {
            String ph_num = phnoA[1].substring(0, phnoA[1].length() - 1);
        //    System.out.println("_____SSSS" + phnoA[1] + "__" + ph_num + "___" + isAlpha(ph_num.replaceAll("\\s", "")));

            if (isAlpha(ph_num.replaceAll("\\s", ""))) {
                if(phoneValueArr.contains(phnoA[0]))
                ph_num = phnoA[0];
            }else if(!phoneValueArr.contains(ph_num)){
                if(phoneValueArr.contains(phnoA[0]))
                    ph_num = phnoA[0];
            }
          //  System.out.println("_____SSSS" + phnoA[1] + "__" + ph_num + "___" + phno);
            j.put("phone", ph_num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = "type=passenger_mobile_check";
        new IsPassengerAvailable(url, j);
//        } else {
//            ((EditText) currentView).setText("");
//            currentView.setTag(0);
//            alert_view(getActivity(), "" + NC.getResources().getString(R.string.message), "" + "Split not valid", "" + NC.getResources().getString(R.string.ok), "");
//        }
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (Character.isLetter(c)) {
                return true;
            }
        }

        return false;
    }

//    private void readContactData() {
//        try {
//
//            ContentResolver cr = getActivity().getContentResolver();
//            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                    null, null, null, null);
//            int iii=0;
//            if (cur != null && cur.getCount() > 0) {
//                while (cur.moveToNext()) {
//                    if (Integer.parseInt(cur.NC.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//iii=iii+1;
//                        System.out.println("______hiii"+iii);
//                        String id = cur.NC.getString(cur.getColumnIndex(
//                                ContactsContract.Contacts._ID));
//                        String name = cur.NC.getString(cur.getColumnIndex(
//                                ContactsContract.Contacts.DISPLAY_NAME));
//                        Cursor pCur = cr.query(
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//                                        + " = ?", new String[] { id }, null);
//                        System.out.println("______hiii"+name);
//                        int i = 0;
//                        int pCount = pCur.getCount();
//                        String[] phoneNum = new String[pCount];
//                        String[] phoneType = new String[pCount];
//
//                        while (pCur != null && pCur.moveToNext()) {
//
//                            phoneNum[i] = pCur.NC.getString(pCur.getColumnIndex(
//                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            if(name.contains("6")){
//                                Log.d("phoneNum","phoneNum"+phoneNum[i]);
//                            }
//                            phoneType[i] = pCur.NC.getString(pCur.getColumnIndex(
//                                    ContactsContract.CommonDataKinds.Phone.TYPE));
//                            if(phoneNum[i].toString().trim().replace("-", "").length() > 5) {
//                                if (phoneNum[i].toString().trim().startsWith("+1")) {
//                                    phoneNum[i] = phoneNum[i].toString().trim().toString().substring(2);
//                                }
//                                if (phoneNum[i].toString().trim().contains("(")) {
//                                    phoneNum[i] = phoneNum[i].toString().trim().replace("(", "");
//
//                                }
//                                if (phoneNum[i].contains(")")) {
//                                    phoneNum[i] = phoneNum[i].toString().trim().replace(")", "");
//                                }
//                                if (phoneNum[i].contains("-")) {
//                                    phoneNum[i] = phoneNum[i].toString().trim().replace("-", "");
//                                }
//                                if (phoneNum[i].contains(" ")) {
//                                    phoneNum[i] = phoneNum[i].toString().trim().replace(" ", "");
//                                }
//
//                                adapter.add(name.toString() + "(" + phoneNum[i].toString().trim().replace("-", "") + ")");
//                                adapter.add(phoneNum[i].toString().trim().replace("-", "") + "(" + name.toString() + ")");
//                                phoneValueArr.add(phoneNum[i].toString().trim().replace("-", ""));
//                                phoneValueArr.add("");
//                                nameValueArr.add(name.toString());
//                                nameValueArr.add("");
//                            }
//
//                            i++;
//                        }
//                    }
//
//                }
//
//            }
//
//
//
//
//
//        } catch (Exception e) {
//            Log.i("AutocompleteContacts", "Exception : " + e);
//            e.printStackTrace();
//        }
//
//    }

    /**
     * this method is used to read contact from phone book
     */

    private void readContactData() {

        try {


            // ContentResolver cr = getActivity().getContentResolver();
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                    + " COLLATE LOCALIZED ASC";
//            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                    null, null, null, sortOrder);


            String[] PROJECTION = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            // String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
            // + " COLLATE LOCALIZED ASC";
            String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER
                    + " = ?";
            String[] selectionArgs = {String.valueOf(1)};

            c = getActivity().managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION, selection, selectionArgs, sortOrder);
            int i = 0;

            while (c.moveToNext()) {
                int j = i++;
                // Log.d("moveToNext", "moveToNext" + cur.getCount()
                // + "==" + j);
                String name = c.getString(c.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                String phoneNo = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

//                if (Utils.phoneNo == phoneNo) {
//
//                } else {
                if (phoneNo != null) {
                    phoneNo1 = phoneNo.replace("-", "");
                    phoneNo2 = phoneNo1;
                    phoneNo2 = phoneNo2.replaceAll("\\s", "");
                    phoneNo2=phoneNo2.replace(SessionSave.getSession("country_code",getActivity()),"");
//                    if (phoneNo2.length() > 10)
//                        phoneNo2 = phoneNo2.substring(phoneNo2.length() - 10, phoneNo2.length());
                //   System.out.println("___BFAFAFA__" + phoneNo1 + "__" + phoneNo2);
                    if (phoneNo2.length() > 6) {
                        String f_Firstname = name.substring(0, 1).toUpperCase() + name.substring(1);
                        adapter.add(f_Firstname.toString() + "(" + phoneNo2.toString().trim().replace("-", "") + ")");
                        adapter.add(phoneNo2.toString().trim().replace("-", "") + "(" + name.toString() + ")");
                        phoneValueArr.add(phoneNo2.toString());
                        phoneValueArr.add("");

                        nameValueArr.add(f_Firstname);
                        nameValueArr.add("");
                    }
                }


            }


        } catch (Exception e) {
         //   Log.i("AutocompleteContacts", "Exception : " + e);
            e.printStackTrace();
        } finally {
            try {
//                if (!c.isClosed()) {
//                    c.close();
//                }
                c = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        LayoutParams attributes = window.getAttributes();
        //must setBackgroundDrawable(TRANSPARENT) in onActivityCreated()
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (true) {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        EditText e = ((EditText) v);
        e.setText("");

        currentView = v;
        currentView.setTag(0);

        return false;
    }

    /**
     * this class is used to call the readcintact
     */

    public class AsyncTask_readContactData extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                readContactData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  Toast.makeText(getActivity(), "add" + String.valueOf(adapter.getCount()), Toast.LENGTH_SHORT).show();
            friend_name1.setThreshold(2);
            friend_name1.setTag(0);
            friend_name2.setTag(0);
            friend_name3.setTag(0);
            friend_name1.setAdapter(adapter);
            friend_name1.setOnItemClickListener(SplitFareDialog.this);
            friend_name2.setThreshold(2);
            friend_name2.setAdapter(adapter);
            friend_name2.setOnItemClickListener(SplitFareDialog.this);
            friend_name3.setThreshold(2);
            friend_name3.setAdapter(adapter);
            friend_name3.setOnItemClickListener(SplitFareDialog.this);
            friend_name1.setOnTouchListener(SplitFareDialog.this);
            friend_name2.setOnTouchListener(SplitFareDialog.this);
            friend_name3.setOnTouchListener(SplitFareDialog.this);
            friend_name1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {


                    if (!hasFocus && !friend_name1.getText().toString().trim().equals("")) {
                        ArrayList<String> results = new ArrayList<String>();
                        for (int i = 0; i < adapter.getCount(); i++)
                            results.add(adapter.getItem(i));
                        if (results.size() == 0 ||
                                results.indexOf(friend_name1.getText().toString()) == -1) {
                            friend_name1.setError("Invalid username.");
                        }
                    } else
                        friend_name1.setError(null);

                }
            });
            friend_name2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !friend_name2.getText().toString().trim().equals("")) {
                        ArrayList<String> results = new ArrayList<String>();
                        for (int i = 0; i < adapter.getCount(); i++)
                            results.add(adapter.getItem(i));

                        if (results.size() == 0 ||
                                results.indexOf(friend_name2.getText().toString()) == -1) {
                            friend_name2.setError("Invalid username.");
                        }

                    } else
                        friend_name2.setError(null);
                }
            });
            friend_name3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !friend_name3.getText().toString().trim().equals("")) {
                        ArrayList<String> results = new ArrayList<String>();
                        for (int i = 0; i < adapter.getCount(); i++)
                            results.add(adapter.getItem(i));

                        if (results.size() == 0 ||
                                results.indexOf(friend_name3.getText().toString()) == -1) {
                            friend_name3.setError("Invalid username.");
                        }
                        ;
                    } else {
                        friend_name3.setError(null);
                    }
                }
            });
            split_progress.setVisibility(View.GONE);
            percentSpn1.setSelection(0);
            percentSpn2.setSelection(0);
            percentSpn3.setSelection(0);
            setSpinner();

        }
    }

    @Override
    public void onPause() {
    //    System.out.println("____________onpause");

//                if(c!=null)
//                    c.close();
        dismiss();
        super.onPause();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    /**
     * this class is used to check whether the another user exist or not
     */

    public class IsPassengerAvailable implements APIResult {
        private String data;
        private String url;

        public IsPassengerAvailable(final String url, JSONObject data) {
            new APIService_Retrofit_JSON(getActivity(), this, data, false, TaxiUtil.API_BASE_URL + TaxiUtil.COMPANY_KEY + "/?" + "lang=" + SessionSave.getSession("Lang", getActivity()) + "&" + url).execute();
            this.url = url;
            this.data = data.toString();
        }


        @Override
        public void getResult(boolean isSuccess, String result) {
            if (isSuccess) {
                try {
                 //   System.out.println("_____" + result);
                    JSONObject jobj = new JSONObject(result);
                    if (jobj.getInt("status") != 1) {
                        Toast.makeText(getActivity(), jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        ((EditText) currentView).setText("");
                        currentView.setTag(0);

                    } else {
                        int id = jobj.getJSONArray("detail").getJSONObject(0).getInt("id");
                        if (!userExist(id)) {
                            currentView.setTag(id);
                            setSpinner();
                        } else {
                            Toast.makeText(getActivity(), "user exist", Toast.LENGTH_SHORT).show();
                            ((EditText) currentView).setText("");
                            currentView.setTag(0);
                        }
                        spinAdapterChange();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

        private boolean userExist(int ids) {


            int[] id = null;
            try {
                id = new int[4];
                id[0] = Integer.parseInt(SessionSave.getSession("Id", getActivity()));
                id[1] = (Integer) friend_name1.getTag();
                id[2] = (Integer) friend_name2.getTag();
                id[3] = (Integer) friend_name3.getTag();
            } catch (Exception e) {
                e.printStackTrace();

            }
            boolean exist = false;
            for (int i = 0; i < 4; i++) {
                if (id[i] != 0)
                    if (ids == (id[i])) {
                        exist = true;
                    }
            }
            return exist;
        }
    }

    private void setSpinner() {

        int percent = 0;
        if ((Integer) friend_name1.getTag() != 0)
            percent += 1;
        if ((Integer) friend_name2.getTag() != 0)
            percent += 1;
        if ((Integer) friend_name3.getTag() != 0)
            percent += 1;

        percentSpn1.setSelection(0, true);
        percentSpn2.setSelection(0, true);
        percentSpn3.setSelection(0, true);
        if ((Integer) friend_name1.getTag() != 0)
            percentSpn1.setSelection(percent, true);
        if ((Integer) friend_name2.getTag() != 0)
            percentSpn2.setSelection(percent, true);
        if ((Integer) friend_name3.getTag() != 0)
            percentSpn3.setSelection(percent, true);
        percentSpn.setSelection(percent, true);
      //  System.out.println("________Spin" + percent);


    }

    @Override
    public void onAttach(Context context) {
    //    System.out.println("attach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
     //   System.out.println("deattach");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    //    System.out.println("save");
    }

    @Override
    public void onDestroyView() {
      //  System.out.println("desttadttach");
        super.onDestroyView();
    }

    private void spinAdapterChange() {
//        int totalPerson = 1;
//        totalPerson = (friend_name1.getTag() != 0) ? totalPerson + 1 : totalPerson;
//        totalPerson = (friend_name2.getTag() != 0) ? totalPerson + 1 : totalPerson;
//        totalPerson = (friend_name3.getTag() != 0) ? totalPerson + 1 : totalPerson;
//        Toast.makeText(getActivity(), String.valueOf(totalPerson), Toast.LENGTH_SHORT).show();
//        int totalPersonC=5-totalPerson;
//        dynamicPercent_U.clear();
//        dynamicPercent.clear();
//        for (int i = 0; i < totalPersonC; i++)
//            dynamicPercent_U.add(percentArray_user[i]);
//        for (int i = 0; i < totalPersonC; i++){
//            if(percentArray.length>totalPersonC)
//            dynamicPercent.add(percentArray[i]);}
//        P_adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, dynamicPercent_U);
//        S_adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, dynamicPercent);
//        percentSpn.setAdapter(P_adapter);
//        percentSpn1.setAdapter(S_adapter);
//        percentSpn2.setAdapter(S_adapter);
//        percentSpn3.setAdapter(S_adapter);
//        if (totalPerson == 3) {
//
//        }
    }


    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            if (alertmDialog != null)
                if (alertmDialog.isShowing())
                    alertmDialog.dismiss();
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