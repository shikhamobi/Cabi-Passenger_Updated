package com.cabipassenger.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;

/**
 * this class is used to store the values locally and used it whenever necessary
 */

public class SessionSave {

    public static void saveSession(String key, String value, Context context) {
        if (context != null) {
            Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static void saveSession(String key, Long value, Context context) {
        if (context != null) {
            Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static String getSession(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void saveSession(String key, boolean value, Context context) {
        Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getSession(String key, Context context, boolean a) {
        SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static long getSession(String key, Context context, long a) {
        SharedPreferences prefs = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE);
        return prefs.getLong(key, 0);
    }

    public static void clearSession(Context context) {
        Editor editor = context.getSharedPreferences("KEY", Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static void tutorialChk(boolean isAsk, Context con) {
        Editor editor = con.getSharedPreferences("ASK", con.MODE_PRIVATE).edit();
        editor.putBoolean("isAsk", isAsk);
        editor.commit();
    }

    public static boolean isAsk(Context con) {
        SharedPreferences sharedPreferences = con.getSharedPreferences("ASK", con.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isAsk", false);

    }


    public static boolean saveArray(String message, Context con) {
        SharedPreferences sp = con.getSharedPreferences("KEYARRAY", con.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
    /* sKey is an array */
        mEdit1.putString("Status_size", message);

//        for(int i=0;i<sKey.size();i++)
//        {
//            mEdit1.remove("Status_" + i);
//            mEdit1.putString("Status_" + i, sKey.get(i));
//        }

        return mEdit1.commit();
    }

    public static ArrayList<String> loadArray(Context con) {
        ArrayList<String> sKey = new ArrayList<>();
        SharedPreferences mSharedPreference1 = con.getSharedPreferences("KEYARRAY", con.MODE_PRIVATE);
        sKey.clear();
        int size = mSharedPreference1.getInt("Status_size", 0);

        for (int i = 0; i < size; i++) {
            sKey.add(mSharedPreference1.getString("Status_" + i, null));
        }

        return sKey;

    }
}
