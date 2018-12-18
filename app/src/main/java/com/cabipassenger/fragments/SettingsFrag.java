package com.cabipassenger.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.TextView;

import com.cabipassenger.MainHomeFragmentActivity;
import com.cabipassenger.R;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.interfaces.FragPopFront;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.cabipassenger.util.Colorchange;
import com.cabipassenger.util.FontHelper;
import com.cabipassenger.util.NC;
import com.cabipassenger.util.SessionSave;
import com.cabipassenger.util.TaxiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * this class is used for settings
 */
public class SettingsFrag extends Fragment implements FragPopFront {
    private SwitchCompat spliton_off, favoritedriveron_off, skip_drop_on_off;
    private TextView favoritedriver;
    private TextView version;
    private String APP_VERSION = "";
    private int typeOfApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        Colorchange.ChangeColor((ViewGroup) v, getActivity());
        String host="";
        try {
            URL urls = new URL(TaxiUtil.API_BASE_URL);
             host = urls.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ((TextView) v.findViewById(R.id.baseUrl)).setText(host);
        FontHelper.applyFont(getActivity(), v);
        ((MainHomeFragmentActivity) getActivity()).setTitle_m(NC.getResources().getString(R.string.settings));
        v.findViewById(R.id.lang).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new LanguageFrag()).addToBackStack(null).commit();
            }
        });
        version = (TextView) v.findViewById(R.id.version);
        favoritedriver = (TextView) v.findViewById(R.id.favoritedriver);
        spliton_off = (SwitchCompat) v.findViewById(R.id.spliton_off);
        favoritedriveron_off = (SwitchCompat) v.findViewById(R.id.favoritedriverson_off);
        skip_drop_on_off = (SwitchCompat) v.findViewById(R.id.skip_drop_on_off);

      //  Log.e("preference ", String.valueOf(SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)));
        if (APP_VERSION.equals("")) {
            PackageInfo info = null;
            PackageManager manager = getActivity().getPackageManager();
            try {
                info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                APP_VERSION = info.versionName;
               // System.out.println("apppversion----" + APP_VERSION);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        version.setText(APP_VERSION);
        if (SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), false))
            spliton_off.setChecked(true);
        else
            spliton_off.setChecked(false);


        favoritedriveron_off.setChecked(SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), false));
        skip_drop_on_off.setChecked(SessionSave.getSession(TaxiUtil.isSkipFavOn, getActivity(), false));

        /*spliton_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SessionSave.saveSession(TaxiUtil.isSplitOn, isChecked, getActivity());
            }
        });*/

        favoritedriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //getFragmentManager().beginTransaction().add(R.id.mainFrag, new FavouriteDriverFrag()).commit();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new FavouriteDriverFrag()).addToBackStack(null).commit();

            }
        });

//        favoritedriveron_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked)
//                    SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, getActivity());
//                else
//                    SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, getActivity());
//
//            }
//        });

//        skip_drop_on_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                typeOfApi = 3;
//                callApi(isChecked);
//                //      Toast.makeText(getActivity(), String.valueOf(SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)), Toast.LENGTH_SHORT).show();
////                if (!SessionSave.getSession(TaxiUtil.isSkipFavOn, getActivity(), false)) {
////                    //  fav_add.setImageResource(R.drawable.on_btn);
////                    SessionSave.saveSession(TaxiUtil.isSkipFavOn, true, getActivity());
////                    ShowToast.center(getActivity(), getString(R.string.skip_fav_off));
////                    // Toast.makeText(getActivity(), getString(R.string.fav_driver_on), Toast.LENGTH_SHORT).show();
////                } else {
////                    // fav_add.setImageResource(R.drawable.off_btn);
////                    SessionSave.saveSession(TaxiUtil.isSkipFavOn, false, getActivity());
////                    ShowToast.center(getActivity(), getString(R.string.skip_fav_on));
////                    //Toast.makeText(getActivity(), getString(R.string.fav_driver_off), Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
//        favoritedriveron_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                typeOfApi = 2;
//                callApi(isChecked);
//                //      Toast.makeText(getActivity(), String.valueOf(SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), true)), Toast.LENGTH_SHORT).show();
////                if (!SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), false)) {
////                    //  fav_add.setImageResource(R.drawable.on_btn);
////                    SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, getActivity());
////                    ShowToast.center(getActivity(), NC.getString(R.string.fav_driver_on));
////                    // Toast.makeText(getActivity(), getString(R.string.fav_driver_on), Toast.LENGTH_SHORT).show();
////                } else {
////                    // fav_add.setImageResource(R.drawable.off_btn);
////                    SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, getActivity());
////                    ShowToast.center(getActivity(), NC.getString(R.string.fav_driver_off));
////
////                    //Toast.makeText(getActivity(), getString(R.string.fav_driver_off), Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
        skip_drop_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //SessionSave.saveSession(TaxiUtil.isSplitOn, isChecked, getActivity());
                typeOfApi = 3;
                callApi(isChecked);

            }
        });
        favoritedriveron_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //SessionSave.saveSession(TaxiUtil.isSplitOn, isChecked, getActivity());
                typeOfApi = 2;
                callApi(isChecked);

            }
        });
        spliton_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //SessionSave.saveSession(TaxiUtil.isSplitOn, isChecked, getActivity());
                typeOfApi = 1;
                callApi(isChecked);

            }
        });


        return v;
    }

    public void callApi(boolean isChecked) {
        JSONObject j = new JSONObject();
        try {
            j.put("passenger_id", SessionSave.getSession("Id", getActivity()));

            if (isChecked)
                j.put("value", "1");
            else
                j.put("value", "2");
            j.put("type", typeOfApi);

          //  Log.e("Json ", j.toString());

            new SaveSplitType("type=set_split_fare", j);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainHomeFragmentActivity) getActivity()).left_icon.setTag("backarrow");
        ((MainHomeFragmentActivity) getActivity()).left_icon.setImageResource(R.drawable.back);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Colorchange.ChangeColor((ViewGroup)(PaymentOptionFrag.this.v),getActivity());
                Colorchange.ChangeColor((ViewGroup) (((ViewGroup) getActivity()
                        .findViewById(android.R.id.content)).getChildAt(0)), getActivity());
            }
        }, 100);
    }

    /**
     * this show the favourite driver to user
     */

    @Override
    public void trigger_FragPopFront() {
        if (SessionSave.getSession(TaxiUtil.isFavDriverOn, getActivity(), false))
            favoritedriveron_off.setChecked(true);
        else
            favoritedriveron_off.setChecked(false);
    }

    /**
     * this class is used to set the split fare on off
     */

    public class SaveSplitType implements APIResult {


        public SaveSplitType(final String url, JSONObject data) {
            // TODO Auto-generated constructor stub
            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {

         //   System.out.println("SplitFareOption_____" + result);
          //  Log.e("Result ", result);
            try {
                final JSONObject json = new JSONObject(result);
                if (isSuccess) {
                    Toast.makeText(getActivity(), json.getString("message"),
                            Toast.LENGTH_SHORT).show();
                    if (json.getInt("status") == 1) {
                        if (typeOfApi == 1) {
                            SessionSave.saveSession(TaxiUtil.isSplitOn, true, getActivity());
                            spliton_off.setChecked(true);
                        } else if (typeOfApi == 2) {
                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, true, getActivity());
                            favoritedriveron_off.setChecked(true);
                        } else if (typeOfApi == 3) {
                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, true, getActivity());
                            skip_drop_on_off.setChecked(true);
                        }
                    } else {
                        if (typeOfApi == 1) {
                            SessionSave.saveSession(TaxiUtil.isSplitOn, false, getActivity());
                            spliton_off.setChecked(false);
                        } else if (typeOfApi == 2) {
                            SessionSave.saveSession(TaxiUtil.isFavDriverOn, false, getActivity());
                            favoritedriveron_off.setChecked(false);
                        } else if (typeOfApi == 3) {
                            SessionSave.saveSession(TaxiUtil.isSkipFavOn, false, getActivity());
                            skip_drop_on_off.setChecked(false);
                        }
//                        SessionSave.saveSession(TaxiUtil.isSplitOn, false, getActivity());
//                        spliton_off.setChecked(false);
                    }
                //    Log.e("SplitOn ", String.valueOf(SessionSave.getSession(TaxiUtil.isSplitOn, getActivity(), true)));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(getActivity(), json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
