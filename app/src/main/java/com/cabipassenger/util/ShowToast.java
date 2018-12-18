package com.cabipassenger.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by developer on 7/2/17.
 */
public class ShowToast {

   static Toast toast;
    public static void center(Context c,String s){
        if(toast!=null)
            toast.cancel();
        toast = Toast.makeText(c,s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
    public static void top(Context c,String s){
        if(toast!=null)
            toast.cancel();
        toast = Toast.makeText(c,s, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 50);
        toast.show();

    }
}
