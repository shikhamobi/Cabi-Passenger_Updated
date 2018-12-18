package com.cabipassenger.util;

import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cabipassenger.R;
import com.cabipassenger.fragments.BookTaxiNewFrag;
import com.cabipassenger.interfaces.APIResult;
import com.cabipassenger.service.APIService_Retrofit_JSON;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by developer on 1/12/17.
 */

public class LatLongToAddress extends AsyncTask<String, String, String> {
    private static  boolean GEOCODE_EXPIRY = false;
    public Context mContext;
    LatLng mPosition;
    String Address = "";
    Geocoder geocoder;
    private double latitude;
    private double longitude;
    List<android.location.Address> addresses = null;

    public LatLongToAddress(Context context, LatLng position) {

        mContext = context;
        mPosition = position;
        latitude = mPosition.latitude;
        longitude = mPosition.longitude;
        geocoder = new Geocoder(context, Locale.getDefault());
    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        SessionSave.saveSession("notes", "", mContext);
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        try {
           // System.out.println("address size11:" + latitude + "%$#" + longitude);

            if (Geocoder.isPresent()) {
            //    System.out.println("address size:!" + latitude + "%$#" + longitude);
                addresses = geocoder.getFromLocation(latitude, longitude, 3);

             //   System.out.println("address size:@" + addresses.size());
                if (addresses.size() == 0) {
                    new LatLongToAddress.convertLatLngtoAddressApi(mContext, latitude, longitude);
                } else {
                //    System.out.println("address size:@@" + addresses.size());
                    for (int i = 0; i < addresses.size(); i++) {
                        Address += addresses.get(0).getAddressLine(i) + ", ";
                    }
                    if (Address.length() > 0)
                        Address = Address.substring(0, Address.length() - 2);
                }
            } else {
              //  System.out.println("address size:@V" + addresses.size());
                if (NetworkStatus.isOnline(mContext))
                    new LatLongToAddress.convertLatLngtoAddressApi(mContext, latitude, longitude);
                else {
                    BookTaxiNewFrag.setfetch_address();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (NetworkStatus.isOnline(mContext))
                new LatLongToAddress.convertLatLngtoAddressApi(mContext, latitude, longitude);
            else {
                BookTaxiNewFrag.setfetch_address();
            }
        }
        return Address;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (mContext != null) {
            //closeDialog();
            TaxiUtil.pAddress = "" + Address;
            if (Address.length() != 0) {

            } else
            if(!GEOCODE_EXPIRY){

            }
            BookTaxiNewFrag.setfetch_address();
        }
        result = null;

    }

    /**
     * this class is used to GOOGLE PLACE API and convert the address
     */
    public class convertLatLngtoAddressApi implements APIResult {
        public convertLatLngtoAddressApi(Context c, double lati, double longi) {
            String googleMapUrl;
            if (GEOCODE_EXPIRY) {
                googleMapUrl = SessionSave.getSession("base_url", mContext) + "?type=google_geocoding";
                try {
                    JSONObject j = new JSONObject();
                    j.put("origin", lati + "," + longi);
                    j.put("type", "2");
                    new APIService_Retrofit_JSON(c, this, j, false, googleMapUrl, false).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else{
                googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                new APIService_Retrofit_JSON(c, this, null, true, googleMapUrl, true).execute();}
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
          //  System.out.println("____SDD____3d_" + result + "SSS" + isSuccess);
            if (result != null && !result.toString().trim().equalsIgnoreCase("")) {
                setLocation(result.toString());
            }
            //else
               // errorInGettingAddress("Can't get address due to result2: "+result);
        }
    }


    public void setLocation(String inputJson) {

        try {
            if (mContext != null && inputJson!=null) {
                JSONObject object = new JSONObject("" + inputJson);
                JSONArray array = object.getJSONArray("results");
                object = array.getJSONObject(0);
                BookTaxiNewFrag.sf.setPickuplocTxt(object.getString("formatted_address"));
                BookTaxiNewFrag.sf.setPickuplatlng(new LatLng(latitude, longitude));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            GEOCODE_EXPIRY=true;
            if (mContext != null) {
                if (!NetworkStatus.isOnline(mContext))
                    Toast.makeText(mContext, mContext.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                BookTaxiNewFrag.setfetch_address();
            }
        }
    }
}