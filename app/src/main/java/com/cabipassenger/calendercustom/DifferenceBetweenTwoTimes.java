package com.cabipassenger.calendercustom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DifferenceBetweenTwoTimes {
    public static long[] calculate(String date_from, String date_to,
                                   boolean onlypos) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date aDate = null, bDate = null;
        try {
            aDate = formatter.parse(date_from);
            bDate = formatter.parse(date_to);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long diff;
        if (onlypos)
            diff = getDifferenceDayspos(aDate, bDate);
        else
            diff = getDifferenceDays(aDate, bDate);

        long result[] = new long[4];
        result[3] = (diff / 1000 % 60);
        result[2] = (diff / (60 * 1000) % 60);
        result[1] = diff / (60 * 60 * 1000) % 24;
        result[0] = diff / (24 * 60 * 60 * 1000);


        return result;
    }

    private static void stringToDate() {
        // TODO Auto-generated method stub

    }

    @SuppressLint("NewApi")
    public static long getDifferenceDays(Date d1, Date d2) {

        long diff = d2.getTime() - d1.getTime();
        return diff;

    }

    public static long getDifferenceDayspos(Date d1, Date d2) {

        long diff = d2.getTime() - d1.getTime();
        if (diff > 0)
            return diff;
        else
            return 0;
    }

    public static int getDifferenceinDays(String date_from, String date_to) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date aDate = null, bDate = null;
        try {
            aDate = formatter.parse(date_from);
            bDate = formatter.parse(date_to);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long diff = aDate.getTime() - bDate.getTime();

        long result = (diff / (60 * 60 * 1000)) / 24;
        if (diff > 0 && result == 0)
            return 1;
        else
            return (int) result;
    }
}
