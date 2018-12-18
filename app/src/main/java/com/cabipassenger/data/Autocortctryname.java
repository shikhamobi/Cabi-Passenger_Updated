package com.cabipassenger.data;

import android.content.Context;

//import com.cabi.util.FindContext;


/**
 * Created by developer on 20/8/16.
 * To align required space for each spinner item (eg: country code and name)
 */

public class Autocortctryname {

    public static String Autocortct(String countrycode, String countryname) {

        String coutryList;
        Context Name;
        countrycode = countrycode.trim();
        countryname = countryname.trim();

        int cSize = countrycode.length();
        coutryList = countrycode;
        int currlyPrecence=0;
        for (int i = 0; i < coutryList.length(); i++) {
            char temp = coutryList.charAt(i);

            if (temp=='(' || temp==')')
                currlyPrecence++;
        }
        cSize=cSize+currlyPrecence;
        if(currlyPrecence>0 && countrycode.length()>7){
            cSize=cSize+2;
        }
        else if(currlyPrecence>0 && countrycode.length()==7){
            cSize=cSize-1;
        }else if(countrycode.length()==3){
            cSize=cSize-1;
        }
        else if(countrycode.length()==2){
            cSize=cSize-2;
        }
        for(int i=0;i<16-cSize;i++){

            coutryList+=" ";
        }
        return coutryList+countryname;}
}
