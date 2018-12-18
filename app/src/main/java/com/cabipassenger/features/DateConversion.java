//package com.Taximobility.features;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
///**
// * Created by developer on 4/25/16.
// */
//public class DateConversion {
//
//    private static Calendar calendar;
//    private static String returnString="";
//
//    public static String date_conversion(String date_time) {
//        try {
//            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            calendar = Calendar.getInstance(Locale.UK);
//            java.util.Date yourDate = parser.parse(date_time);
//            calendar.setTime(yourDate);
//            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
//            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
//            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + timeformat);
//        } catch (Exception e) {
//            e.printStackTrace();
//            calendar = Calendar.getInstance(Locale.UK);
//            String month = monthFullName(calendar.get(Calendar.MONTH) + 1);
//            String dayName = daytwodigit(calendar.get(Calendar.DAY_OF_MONTH) + 1);
//            String timeformat = timetwodigit(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//            returnString = (dayName + "-" + month + "-" + calendar.get(Calendar.YEAR) + " " + timeformat);
//        }
//        return returnString;
//    }
//
//
//
//    public static String monthFullName(int monthnumber) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, monthnumber - 1);
//        SimpleDateFormat sf = new SimpleDateFormat("MMM");
//        sf.setCalendar(cal);
//        String monthName = sf.format(cal.getTime());
//        return monthName;
//    }
//
//    /**
//     * This is method to format month value (dd) eg:(21)
//     */
//    public static String daytwodigit(int daynumber) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_MONTH, daynumber - 1);
//        SimpleDateFormat sf = new SimpleDateFormat("dd");
//        sf.setCalendar(cal);
//        String day2digit = sf.format(cal.getTime());
//        return day2digit;
//    }
//
//    /**
//     * This is method to format month value (hh:mm:ss a) eg:(11:15:34 AM)
//     */
//    private static String timetwodigit(int year, int month, int day, int hr, int min, int sec) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(year, month, day, hr, min, sec);
//        SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss a");
//        sf.setCalendar(cal);
//        String time2digit = sf.format(cal.getTime());
//        return time2digit.toUpperCase();
//    }
//}