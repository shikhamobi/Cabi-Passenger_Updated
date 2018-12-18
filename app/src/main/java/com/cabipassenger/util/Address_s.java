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
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 *     This class for convert the given lat and lng value into address asynchronously using GEOCODER <br>
 *     If GEOCODER fails to get the address then it will redirect to GOOGLE PLACE API and convert the address.</br>
 *     @author developer
 */
public class Address_s extends AsyncTask<String, String, String> {
    public Context mContext;
    LatLng mPosition;
    String Address = "";
    Geocoder geocoder;
    private double latitude;
    private double longitude;
    List<android.location.Address> addresses = null;

    public Address_s(Context context, LatLng position) {

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
          //  System.out.println("address size11:" + latitude + "%$#" + longitude);

            if (Geocoder.isPresent()) {
              //  System.out.println("address size:" + latitude + "%$#" + longitude);
                addresses = geocoder.getFromLocation(latitude, longitude, 3);
               // System.out.println("address size:" + addresses.size());
                if (addresses.size() == 0) {
                    new convertLatLngtoAddressApi(mContext, latitude, longitude);
                } else {
                    for (int i = 0; i < addresses.size(); i++) {
                        Address += addresses.get(0).getAddressLine(i) + ", ";
                    }
                    if (Address.length() > 0)
                        Address = Address.substring(0, Address.length() - 2);
                }
            } else {
                if (NetworkStatus.isOnline(mContext))
                    new convertLatLngtoAddressApi(mContext, latitude, longitude);
                else {
                    BookTaxiNewFrag.setfetch_address();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (NetworkStatus.isOnline(mContext))
                new convertLatLngtoAddressApi(mContext, latitude, longitude);
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
        TaxiUtil.pAddress = "" + Address;
        if (Address.length() != 0) {
           // System.out.println("____SDD____2"+BookTaxiNewFrag.sf.getPickuplocTxt());
            BookTaxiNewFrag.sf.setPickuplocTxt(Address);
            BookTaxiNewFrag.sf.setPickuplatlng(new LatLng(latitude,longitude));
        } else {
            BookTaxiNewFrag.setfetch_address();
        }
        result = null;

    }

/**
 * this class is used to GOOGLE PLACE API and convert the address
 */

    public class convertLatLngtoAddressApi implements APIResult {
        public convertLatLngtoAddressApi(Context c, double lati, double longi) {
            String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
            new APIService_Retrofit_JSON(c, this, null, true, googleMapUrl).execute();
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
            setLocation(result.toString());
        }
    }


    public void setLocation(String inputJson) {

        try {
            JSONObject object = new JSONObject("" + inputJson);
            JSONArray array = object.getJSONArray("results");
            object = array.getJSONObject(0);
           // System.out.println("____SDD____3"+BookTaxiNewFrag.sf.getPickuplocTxt());
            BookTaxiNewFrag.sf.setPickuplocTxt(object.getString("formatted_address"));
            BookTaxiNewFrag.sf.setPickuplatlng(new LatLng(latitude,longitude));
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mContext != null)
                if (!NetworkStatus.isOnline(mContext))
                    Toast.makeText(mContext, mContext.getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, mContext.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            BookTaxiNewFrag.setfetch_address();
        }
    }
}
