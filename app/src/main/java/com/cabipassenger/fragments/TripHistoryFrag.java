//package com.Taximobility.fragments;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cabi.R;
//import com.cabi.TripDetailsAct;
//import com.cabi.adapter.BookingAdapter;
//import com.cabi.adapter.TripHistoryAdapter;
//import com.cabi.calendercustom.DateClickListner;
//import com.cabi.calendercustom.DifferenceBetweenTwoTimes;
//import com.cabi.features.CToast;
//import com.cabi.interfaces.APIResult;
//import com.cabi.service.APIService_Retrofit_JSON;
//import com.cabi.service.APIService_Retrofit_JSON_NoProgress;
//import com.cabi.util.FontHelper;
//import com.cabi.util.SessionSave;
//import com.cabi.util.TaxiUtil;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Locale;
//
///**
// * Created by developer on 4/25/16.
// */
//
//public class TripHistoryFrag extends Fragment implements DateClickListner {
//    // Class members declarations.
//    public GregorianCalendar month, itemmonth;
//    public Handler handler;
//    public ArrayList<String> items;
//    ArrayList<String> event;
//    ArrayList<String> date;
//    ArrayList<String> desc;
//    private LinearLayout SlideImg;
//    private TextView HeadTitle;
//    private TextView TodayBtn;
//    private TextView MonthBtn;
//    private TextView SummaryBtn;
//    private SimpleDateFormat df;
//    private static int Summary = 0;
//    private static int current_month = 0;
//    private boolean isMonthwise;
//    private String curentDateString;
//    private Date selected_time;
//    private LinearLayout SummaryLay;
//    private LinearLayout CalendarLay;
//    @SuppressWarnings("unused")
//    private LinearLayout bottomlay, calender_layout, on_up_layout;
//    private TextView CancelTxt, leftIcon;
//    private TextView nodataTxt, on_nodataTxt;
//    private ListView Summarylist;
//    private SimpleDateFormat df_ym;
//    @SuppressWarnings("unused")
//    private TextView Title_month1;
//
//    private RelativeLayout headerMonth;
//    private boolean misMonthwise;
//    private boolean isBack;
//    Locale locale;
//    TextView txt_on_going, txt_up_coming, txt_past_booking;
//    LinearLayout txt_on_going_r, txt_past_booking_r;
//    public static LinearLayout txt_up_coming_r;
//    private ListView bookinglist, upcominglist, ongoinglist;
//    Dialog mDialog;
//    private int bookings_type = 1;
//    TextView donebtn, title1;
//    RelativeLayout previous1, next1;
//    private boolean issummary = true;
//    private int preLast = -9;
//    private int monthStart;
//    private static int monthLimit = 10;
//    private static int currentMonth, currentYear;
//    private Dialog alertmDialog;
//    private Dialog mshowDialog;
//    private String listType="O";
//    private FrameLayout listGroup;
//
//    // set the layout to activity.
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.trip_history_lay, container, false);
//        priorChanges(v);
//        return v;
//    }
//
//
//    public void priorChanges(View v) {
//
//        FontHelper.applyFont(getActivity(), v.findViewById(R.id.triphistory_contain));
//        SlideImg = (LinearLayout) v.findViewById(R.id.leftIconTxt);
//        bottomlay = (LinearLayout) v.findViewById(R.id.bottomlay);
//        calender_layout = (LinearLayout) v.findViewById(R.id.calender_layout);
//        on_up_layout = (LinearLayout) v.findViewById(R.id.on_up_layout);
//        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
//        donebtn = (TextView) v.findViewById(R.id.rightIconTxt);
//        donebtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.book_refresh, 0);
//        donebtn.setCompoundDrawablePadding(5);
//        HeadTitle.setText(getResources().getString(R.string.menu_trip_history));
//        Initialize(v);
//    }
//
//    // Initialize the views on layout
//    public void Initialize(View v) {
//        // TODO Auto-generated method stub
//
//        locale = new Locale("" + SessionSave.getSession("Lang", getActivity()), "" + SessionSave.getSession("Lang_Country", getActivity()));
//        Locale.setDefault(locale);
//
//
//        CancelTxt = (TextView) v.findViewById(R.id.back_text);
//        nodataTxt = (TextView) v.findViewById(R.id.nodataTxt);
//        on_nodataTxt = (TextView) v.findViewById(R.id.on_nodataTxt);
//        leftIcon = (TextView) v.findViewById(R.id.leftIcon);
//        CancelTxt.setVisibility(View.VISIBLE);
//        SummaryLay = (LinearLayout) v.findViewById(R.id.summarylay);
//        CalendarLay = (LinearLayout) v.findViewById(R.id.calendarlay);
//        Summarylist = (ListView) v.findViewById(R.id.summarylist);
//        Summarylist.setSelector(R.drawable.list_selector);
////        CalenderFragment.SCROLLING_TYPE = CalenderFragment.SCROLLING_TYPE_BACKWARD;
//
//        //  getSupportFragmentManager().beginTransaction().add(R.id.calendarlay, new CalenderFragment()).commit();
//        month = (GregorianCalendar) GregorianCalendar.getInstance();
//        itemmonth = (GregorianCalendar) month.clone();
//        Title_month1 = (TextView) v.findViewById(R.id.title_month1);
//        TodayBtn = (TextView) v.findViewById(R.id.todayBtn);
//        MonthBtn = (TextView) v.findViewById(R.id.monthBtn);
//        MonthBtn.setBackgroundResource(R.drawable.draw_button_select);
//        SummaryBtn = (TextView) v.findViewById(R.id.summaryBtn);
//        TodayBtn.setText("" + getResources().getString(R.string.today));
//        MonthBtn.setText("" + getResources().getString(R.string.month));
//        SummaryBtn.setText("" + getResources().getString(R.string.summary));
//        txt_on_going = (TextView) v.findViewById(R.id.txt_on_going);
//        txt_up_coming = (TextView) v.findViewById(R.id.txt_up_coming);
//        txt_past_booking = (TextView) v.findViewById(R.id.txt_past_booking);
//        txt_on_going_r = (LinearLayout) v.findViewById(R.id.txt_on_going_r);
//        txt_up_coming_r = (LinearLayout) v.findViewById(R.id.txt_up_coming_r);
//        txt_past_booking_r = (LinearLayout) v.findViewById(R.id.txt_past_booking_r);
//        bookinglist = (ListView) v.findViewById(R.id.bookinglist);
//        upcominglist = (ListView) v.findViewById(R.id.upcomingList);
//        ongoinglist = (ListView) v.findViewById(R.id.ongoingList);
//
//        nodataTxt = (TextView) v.findViewById(R.id.nodataTxt);
//        listGroup = (FrameLayout) v.findViewById(R.id.listGroup);
//        headerMonth = (RelativeLayout) v.findViewById(R.id.headerMonth);
//        title1 = (TextView) v.findViewById(R.id.title_month1);
//        previous1 = (RelativeLayout) v.findViewById(R.id.previous1);
//        next1 = (RelativeLayout) v.findViewById(R.id.next1);
//        items = new ArrayList<String>();
//
//
////        handler = new Handler();
////        handler.post(calendarUpdater);
//        df = new SimpleDateFormat("yyyy-MM-dd", locale);
//        df_ym = new SimpleDateFormat("yyyy-MM", locale);
//        try {
//            String str = "" + android.text.format.DateFormat.format("MMMM-yyyy", month);
//            String strmonth = "" + android.text.format.DateFormat.format("M", month);
//            String montharry[] = getResources().getStringArray(R.array.monthary);
//            str = str.toLowerCase();
//            str = str.substring(0, 1).toUpperCase() + str.substring(1);
//            String arry[] = str.split("-");
//            str = montharry[(Integer.parseInt(strmonth) - 1)] + "-" + arry[1];
//            title1.setText(str);
//            initial();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        // Action performed when previous month selected in listview.
//
//        // Action performed when previous month selected in calenderview.
//
//        // Action performed when next month selected in listview.
//        // Action performed when next month selected in calenderview.
//        // To refresh the history page
//        donebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                initial();
//            }
//        });
//        // Action performed when ongoing tap on clicked.
//        txt_on_going_r.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                bottomlay.setVisibility(View.GONE);
//                headerMonth.setVisibility(View.GONE);
//                Summarylist.setVisibility(View.GONE);
//                CalendarLay.setVisibility(View.GONE);
//                on_up_layout.setVisibility(View.VISIBLE);
//                bookings_type = 0;
//                nodataTxt.setVisibility(View.GONE);
//                txt_on_going.setBackgroundResource(R.drawable.book_select);
//                txt_up_coming.setBackgroundResource(R.drawable.book_unselect);
//                txt_past_booking.setBackgroundResource(R.drawable.book_unselect);
//                txt_on_going.setTextColor(Color.WHITE);
//                txt_up_coming.setTextColor(getResources().getColor(R.color.dark_gray));
//                txt_past_booking.setTextColor(getResources().getColor(R.color.dark_gray));
//                issummary = false;
//                listType = "O";
//                TaxiUtil.bookinglist.clear();
//                apirequest();
//            }
//        });
//        // Action performed when upcoming tap on clicked.
//        txt_up_coming_r.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                bottomlay.setVisibility(View.GONE);
//                headerMonth.setVisibility(View.GONE);
//                CalendarLay.setVisibility(View.GONE);
//                Summarylist.setVisibility(View.GONE);
//                on_up_layout.setVisibility(View.VISIBLE);
//                bookings_type = 1;
//                nodataTxt.setVisibility(View.GONE);
//                txt_on_going.setTextColor(getResources().getColor(R.color.dark_gray));
//                txt_up_coming.setTextColor(Color.WHITE);
//                txt_past_booking.setTextColor(getResources().getColor(R.color.dark_gray));
//                txt_on_going.setBackgroundResource(R.drawable.book_unselect);
//                txt_past_booking.setBackgroundResource(R.drawable.book_unselect);
//                txt_up_coming.setBackgroundResource(R.drawable.book_select);
//                issummary = false;
//                TaxiUtil.bookinglist.clear();
//                listType = "U";
//                apirequest();
//            }
//        });
//        // Action performed when past booking tap on clicked.
////        txt_past_booking.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                // TODO Auto-generated method stub
////                bottomlay.setVisibility(View.VISIBLE);
////                headerMonth.setVisibility(View.VISIBLE);
////                CalendarLay.setVisibility(View.VISIBLE);
////                Summarylist.setVisibility(View.GONE);
////                on_up_layout.setVisibility(View.GONE);
////                txt_on_going.setBackgroundResource(R.drawable.book_unselect);
////                txt_up_coming.setBackgroundResource(R.drawable.book_unselect);
////                txt_past_booking.setBackgroundResource(R.drawable.book_select);
////                if (SummaryLay.isShown()) {
////                    mHandler.sendEmptyMessage(1);
////                }
////                SummaryBtn.setTextColor(getResources().getColor(R.color.black));
////                MonthBtn.setTextColor(getResources().getColor(R.color.white));
////                TodayBtn.setTextColor(getResources().getColor(R.color.black));
////                SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
////                TodayBtn.setBackgroundResource(R.drawable.draw_button_unselect);
////                MonthBtn.setBackgroundResource(R.drawable.draw_button_select);
////                issummary = true;
////            }
////        });
//        // Action performed when back button on clicked.
//        SlideImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (isBack) {
//                    SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                    TodayBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                    MonthBtn.setBackgroundResource(R.drawable.draw_button_select);
//                    SummaryBtn.setTextColor(getResources().getColor(R.color.black));
//                    MonthBtn.setTextColor(getResources().getColor(R.color.white));
//                    TodayBtn.setTextColor(getResources().getColor(R.color.black));
//                    CalendarLay.setVisibility(View.VISIBLE);
//                    SummaryLay.setVisibility(View.GONE);
//                    Summarylist.setVisibility(View.GONE);
//                    isBack = false;
//                } else {
//                    //  menu.toggle();
//                }
//            }
//        });
//        // Action perform when summary button clicked.
//        bookinglist.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                final int lastItem = firstVisibleItem + visibleItemCount;
//                if (lastItem == totalItemCount) {
//                    Log.d("----", String.valueOf(preLast) + String.valueOf(lastItem));
//                    if (preLast != lastItem) { //to avoid multiple calls for last item
//                        Log.d("----------", String.valueOf(preLast) + String.valueOf(lastItem));
//                        preLast = lastItem;
//                        if (isMonthwise) {
//                            //         Toast.makeText(getActivity(), "Fetching...", Toast.LENGTH_SHORT).show();
//                            try {
//                                String cMonth = "0";
//                                if (currentMonth <= 9)
//                                    cMonth = "0" + currentMonth;
//                                else
//                                    cMonth = String.valueOf(currentMonth);
//                                if (monthLimit == 0)
//                                    monthLimit = 1;
//                                monthStart += 10;
//                                monthLimit += 10;
//                                JSONObject j = new JSONObject();
//                                j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                                j.put("start", monthStart);
//                                j.put("limit", monthLimit);
//                                if (!SessionSave.getSession("Lang", getActivity()).equals("ar")) {
//                                    j.put("month", cMonth);
//                                    j.put("year", currentYear);
//                                } else {
//                                    j.put("month", FontHelper.convertfromArabic(cMonth));
//                                    j.put("year", FontHelper.convertfromArabic(String.valueOf(currentYear)));
//                                }
//                                j.put("month", cMonth);
//                                j.put("year", currentYear);
//                                j.put("device_type", "2");
//                                String url = "type=completed_journey_monthwise";
//                                // String url = "type=booking_list";
//                                System.out.println("--------send" + j.toString());
//                                isMonthwise = true;
//                                if (TaxiUtil.isOnline(getActivity())) {
//                                    new getMonthHistory(url, j, isMonthwise);
//                                } else {
//                                    alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        });
//        txt_past_booking_r.setOnClickListener(new View.OnClickListener() {
//            private Date a_time;
//            private String curDate;
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                txt_on_going.setBackgroundResource(R.drawable.book_unselect);
//                txt_up_coming.setBackgroundResource(R.drawable.book_unselect);
//                txt_past_booking.setBackgroundResource(R.drawable.book_select);
//                txt_on_going.setTextColor(getResources().getColor(R.color.dark_gray));
//                txt_up_coming.setTextColor(getResources().getColor(R.color.dark_gray));
//                txt_past_booking.setTextColor(Color.WHITE);
//                Calendar c = Calendar.getInstance();
//                a_time = c.getTime();
//                curDate = "" + android.text.format.DateFormat.format("yyyy-MM", a_time);
//                try {
//                    a_time = df_ym.parse("" + android.text.format.DateFormat.format("yyyy-MM", month));
//                    Summary = compareDate(a_time, df_ym.parse(curDate));
//                } catch (ParseException e) {
//                }
//                String s = "" + android.text.format.DateFormat.format("MM-yyyy", month);
//                String[] m = s.split("-");
//                currentMonth = Integer.parseInt(m[0]);
//                currentYear = Integer.parseInt(m[1]);
//                monthStart = 0;
//                try {
//                    JSONObject j = new JSONObject();
//                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                    j.put("start", "0");
//                    j.put("limit", monthLimit);
//                    if (!SessionSave.getSession("Lang", getActivity()).equals("ar")) {
//                        j.put("month", m[0]);
//                        j.put("year", m[1]);
//                    } else {
//                        j.put("month", FontHelper.convertfromArabic(m[0]));
//                        j.put("year", FontHelper.convertfromArabic(m[1]));
//                    }
////                    j.put("month", m[0]);
//                    j.put("year", m[1]);
//                    j.put("device_type", "2");
//                    String url = "type=completed_journey_monthwise";
//                    TaxiUtil.bookinglist.clear();
//                    TaxiUtil.HSbookinglist.clear();
//                    listType = "P";
//                    // new GetBookinglist(url, j);
//                    isMonthwise = true;
//                    new getMonthHistory(url, j, isMonthwise);
////                    if (Summary <= 0) {
////                        headerMonth.setVisibility(View.VISIBLE);
////                        SummaryBtn.setBackgroundResource(R.drawable.draw_button_select);
////                        TodayBtn.setBackgroundResource(R.drawable.draw_button_unselect);
////                        MonthBtn.setBackgroundResource(R.drawable.draw_button_unselect);
////                        MonthBtn.setTextColor(getResources().getColor(R.color.black));
////                        SummaryBtn.setTextColor(getResources().getColor(R.color.white));
////                        TodayBtn.setTextColor(getResources().getColor(R.color.black));
////                        if (TaxiUtil.isOnline(getActivity())) {
////                            TaxiUtil.bookinglist.clear();
////                            new getMonthHistory(url, j, isMonthwise);
////                        } else {
////                            alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
////                        }
////                    } else if (Summary == 1) {
////                        SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
////                        TodayBtn.setBackgroundResource(R.drawable.draw_button_unselect);
////                        MonthBtn.setBackgroundResource(R.drawable.draw_button_select);
////                        SummaryBtn.setTextColor(getResources().getColor(R.color.black));
////                        MonthBtn.setTextColor(getResources().getColor(R.color.white));
////                        TodayBtn.setTextColor(getResources().getColor(R.color.black));
////                        // CToast.ShowToast(getActivity(), "" + getResources().getString(R.string.date_greater_current_date));
////                        alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.date_greater_current_date), "" + getResources().getString(R.string.ok), "");
////                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        });
//        // Action perform when today button clicked.
//        TodayBtn.setOnClickListener(new View.OnClickListener() {
//            private CharSequence year;
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                try {
//                    SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                    TodayBtn.setBackgroundResource(R.drawable.draw_button_select);
//                    MonthBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                    SummaryBtn.setTextColor(getResources().getColor(R.color.black));
//                    TodayBtn.setTextColor(getResources().getColor(R.color.white));
//                    MonthBtn.setTextColor(getResources().getColor(R.color.black));
//                    Calendar c = Calendar.getInstance();
//                    year = android.text.format.DateFormat.format("yyyy-MM-dd", c.getTime());
//                    String s = "" + android.text.format.DateFormat.format("MM-yyyy", month);
//                    s.split("-");
//                    JSONObject j = new JSONObject();
//                    j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                    j.put("start", "0");
//                    j.put("limit", "15");
//                    if (!SessionSave.getSession("Lang", getActivity()).equals("en"))
//                        j.put("date", FontHelper.convertfromArabic((android.text.format.DateFormat.format("yyyy-MM-dd", c.getTime())).toString()));
//
//                    else
//                        j.put("date", (android.text.format.DateFormat.format("yyyy-MM-dd", c.getTime())).toString());
//                    j.put("device_type", "2");
//                    String url = "type=completed_journey_datewise";
//                    isMonthwise = false;
//                    if (TaxiUtil.isOnline(getActivity())) {
//                        new getMonthHistory(url, j, isMonthwise);
//                    } else {
//                        alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        });
//        // Action perform when month button clicked.
//        MonthBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if (SummaryLay.isShown()) {
//                    mHandler.sendEmptyMessage(1);
//                }
//                SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                TodayBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                MonthBtn.setBackgroundResource(R.drawable.draw_button_select);
//                SummaryBtn.setTextColor(getResources().getColor(R.color.black));
//                MonthBtn.setTextColor(getResources().getColor(R.color.white));
//                TodayBtn.setTextColor(getResources().getColor(R.color.black));
//            }
//        });
//        // Action perform when a item click on calendar gridview
//        try {
//            bookinglist.setSelector(R.color.white);
//            bookinglist.setBackgroundColor(getResources().getColor(R.color.white));
//            Summarylist.setSelector(R.color.white);
//            Summarylist.setBackgroundColor(getResources().getColor(R.color.white));
//            bookinglist.deferNotifyDataSetChanged();
//            Summarylist.deferNotifyDataSetChanged();
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            bookinglist.setSelector(R.color.white);
//            bookinglist.setBackgroundColor(getResources().getColor(R.color.white));
//            Summarylist.setSelector(R.color.white);
//            Summarylist.setBackgroundColor(getResources().getColor(R.color.white));
//            bookinglist.deferNotifyDataSetChanged();
//            Summarylist.deferNotifyDataSetChanged();
//        } catch (Exception e) {
//
//        }
//    }
//
//    // To set the UI content when the activity loaded
//    public void initial() {
//
//        leftIcon.setVisibility(View.GONE);
//        bottomlay.setVisibility(View.GONE);
//        headerMonth.setVisibility(View.GONE);
//        Summarylist.setVisibility(View.GONE);
//        CalendarLay.setVisibility(View.GONE);
//        on_up_layout.setVisibility(View.VISIBLE);
//        bookings_type = 0;
//        nodataTxt.setVisibility(View.GONE);
//        txt_on_going.setBackgroundResource(R.drawable.book_select);
//        txt_up_coming.setBackgroundResource(R.drawable.book_unselect);
//        txt_past_booking.setBackgroundResource(R.drawable.book_unselect);
//        apirequest();
//    }
//
//    // Common function for booking list API call.
//    private void apirequest() {
//
//        try {
//            JSONObject j = new JSONObject();
//            j.put("id", SessionSave.getSession("Id", getActivity()));
//            j.put("start", "0");
//            j.put("limit", "100");
//            j.put("device_type", "2");
//            String url = "type=booking_list";
//            if (TaxiUtil.isOnline(getActivity())) {
//                new GetBookinglist(url, j);
//            } else {
//                alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    // Used to compare the two date.
//    protected int compareDate(Date selected_time2, Date parse) {
//        // TODO Auto-generated method stub
//        return selected_time2.compareTo(parse);
//    }
//
//    // To set the next month values on UI.
////    protected void setNextMonth() {
////
////        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
////            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
////        } else {
////            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
////        }
////    }
////
////    // To set the previous month values on UI.
////    protected void setPreviousMonth() {
////
////        if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
////            month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
////        } else {
////            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
////        }
////    }
//
//    // To refresh calendar and update the UI based on selected date.
////    public void refreshCalendar() {
////
////        TextView title1 = (TextView) v.findViewById(R.id.title_month1);
////        handler.post(calendarUpdater); // generate some calendar items
////        String str = "" + android.text.format.DateFormat.format("MMMM-yyyy", month);
////        String strmonth = "" + android.text.format.DateFormat.format("M", month);
////        String montharry[] = getResources().getStringArray(R.array.monthary);
////        str = str.toLowerCase();
////        str = str.substring(0, 1).toUpperCase() + str.substring(1);
////        String arry[] = str.split("-");
////        str = montharry[(Integer.parseInt(strmonth) - 1)] + "-" + arry[1];
////        title1.setText(str);
////    }
//
//    // To refresh calendar and update the UI.
////    public Runnable calendarUpdater = new Runnable() {
////        @Override
////        public void run() {
////
////            items.clear();
////            event = Utility.readCalendarEvent(getActivity());
////            for (int i = 0; i < Utility.startDates.size(); i++) {
////                itemmonth.add(GregorianCalendar.DATE, 1);
////                items.add(Utility.startDates.get(i).toString());
////            }
////        }
////    };
//    // To update the Listview from API result and move to details view while item picked from list.
//    Handler mHandler = new Handler() {
//        private TripHistoryAdapter trip_adapter;
//
//        @Override
//        public void handleMessage(android.os.Message msg) {
//
//            switch (msg.what) {
//                case 0:
//                    trip_adapter = new TripHistoryAdapter(getActivity());
//                    Summarylist.setAdapter(trip_adapter);
//                    Summarylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
//                            // TODO Auto-generated method stub
//                            showLoading(getActivity());
//                            Intent i = new Intent(getActivity(), TripDetailsAct.class);
//                            i.putExtra("Tripid", TaxiUtil.mTripHistorylist.get(pos).getTripid());
//                            startActivity(i);
//                        }
//                    });
//                    break;
//                case 1:
//                    SummaryLay.setVisibility(View.GONE);
//                    CalendarLay.setVisibility(View.VISIBLE);
//                    SlideImg.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    break;
//            }
//        }
//
//        ;
//    };
//
//    public void showLoading(Context context) {
//        View view = View.inflate(context, R.layout.progress_bar, null);
//        mshowDialog = new Dialog(context, R.style.dialogwinddow);
//        mshowDialog.setContentView(view);
//        mshowDialog.setCancelable(false);
//        mshowDialog.show();
//    }
//    // Slider menu used to move from one activity to another activity.
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//            if (SummaryLay.isShown()) {
//                mHandler.sendEmptyMessage(1);
//                SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                TodayBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                MonthBtn.setBackgroundResource(R.drawable.draw_button_select);
//                SummaryBtn.setTextColor(getResources().getColor(R.color.black));
//                MonthBtn.setTextColor(getResources().getColor(R.color.white));
//                TodayBtn.setTextColor(getResources().getColor(R.color.black));
//                return true;
//            }
//        }
//        return super.getActivity().onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
//        TaxiUtil.mActivitylist.remove(getActivity());
//        if (mshowDialog != null && mshowDialog.isShowing()) {
//            mshowDialog.dismiss();
//            mshowDialog = null;
//
//        }
//        super.onDestroy();
//    }
//
//    public String parseDateToddMMyyyy(String time) {
//        String outputPattern = "yyyy-MM-dd";
//        String inputPattern = "dd-MM-yyyy";
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
//
//        Date date = null;
//        String str = null;
//
//        try {
//            date = inputFormat.parse(time);
//            str = outputFormat.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//    @Override
//    public void DateClick(String date, String status) {
//
//        String formattedDate = parseDateToddMMyyyy(date);
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String formattedDated = df.format(calendar.getTime());
//        int a = DifferenceBetweenTwoTimes.getDifferenceinDays(date.replace("-", "/") + " 0:0:0", formattedDated);
//        if (a > 0) {
//            alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.date_greater_current_date), "" + getResources().getString(R.string.ok), "");
//            // CToast.ShowToast(getActivity(), "" + getResources().getString(R.string.date_greater_current_date));
//        } else {
//            try {
//                JSONObject j = new JSONObject();
//                j.put("passenger_id", SessionSave.getSession("Id", getActivity()));
//                j.put("start", "0");
//                j.put("limit", "15");
//                j.put("date", formattedDate);
//                j.put("device_type", "2");
//                String url = "type=completed_journey_datewise";
//                isMonthwise = false;
//                if (TaxiUtil.isOnline(getActivity())) {
//                    new getMonthHistory(url, j, isMonthwise);
//                } else {
//                    alert_view(getActivity(), "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_internet_connection), "" + getResources().getString(R.string.ok), "");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
//        try {
//            final View view = View.inflate(mContext, R.layout.alert_view, null);
//            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
//            alertmDialog.setContentView(view);
//            alertmDialog.setCancelable(true);
//            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
//            alertmDialog.show();
//            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
//            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
//            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
//            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
//            button_failure.setVisibility(View.GONE);
//            title_text.setText(title);
//            message_text.setText(message);
//            button_success.setText(success_txt);
//            button_success.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//                    alertmDialog.dismiss();
//                }
//            });
//            button_failure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    // TODO Auto-generated method stub
//                    alertmDialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * getActivity() class helps to call the Booking list API and parse the response for both ongoing and upcoming bookings.
//     */
//    private class GetBookinglist implements APIResult {
//        String passengers_log_id = "";
//        String pickup_location = "";
//        String drop_location = "";
//        String pickuptime = "";
//        String travel_status = "";
//        String passenger_name = "";
//        String driver_id = "";
//        String drivername = "";
//        String message = "";
//        JSONArray bookingary;
//        String pickup_latitude = "";
//        String pickup_longitude = "";
//        String drop_latitude = "";
//        String drop_longitude = "";
//        String notes = "";
//
//        public GetBookinglist(String url, JSONObject data) {
//            // TODO Auto-generated constructor stub
//            new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, final String result) {
//            // TODO Auto-generated method stub
//            try {
//                if (isSuccess) {
//                    TaxiUtil.bookinglist.clear();
//                    TaxiUtil.HSbookinglist.clear();
//                    SessionSave.saveSession("Server_bookinglist", "", getActivity());
//                    JSONObject json = new JSONObject(result);
//                    SessionSave.saveSession("Server_bookinglist", result, getActivity());
//                    HashSet<HashMap<String, String>> hs;
//                    if (json.getInt("status") == 1) {
//                        if (bookings_type == 1) {
//                            bookingary = json.getJSONObject("detail").getJSONArray("pending_bookings");
//                            for (int i = 0; i < bookingary.length(); i++) {
//                                if (bookingary.getJSONObject(i).getString("travel_status").equals("9") || bookingary.getJSONObject(i).getString("travel_status").equals("3") || bookingary.getJSONObject(i).getString("travel_status").equals("0")) {
//                                    HashMap<String, String> map = new HashMap<>();
//                                    passengers_log_id = bookingary.getJSONObject(i).getString("passengers_log_id");
//                                    pickup_location = bookingary.getJSONObject(i).getString("pickup_location");
//                                    drop_location = bookingary.getJSONObject(i).getString("drop_location");
//                                    pickuptime = bookingary.getJSONObject(i).getString("pickuptime");
//                                    travel_status = bookingary.getJSONObject(i).getString("travel_status");
//                                    passenger_name = bookingary.getJSONObject(i).getString("passenger_name");
//                                    drivername = bookingary.getJSONObject(i).getString("drivername");
//                                    driver_id = bookingary.getJSONObject(i).getString("driver_id");
//                                    pickup_latitude = bookingary.getJSONObject(i).getString("pickup_latitude");
//                                    pickup_longitude = bookingary.getJSONObject(i).getString("pickup_longitude");
//                                    drop_latitude = bookingary.getJSONObject(i).getString("drop_latitude");
//                                    drop_longitude = bookingary.getJSONObject(i).getString("drop_longitude");
//                                    notes = bookingary.getJSONObject(i).getString("notes_driver");
//                                    map.put("passengers_log_id", passengers_log_id);
//                                    map.put("pickup_location", pickup_location);
//                                    map.put("drop_location", drop_location);
//                                    map.put("pickuptime", pickuptime);
//                                    map.put("travel_status", travel_status);
//                                    map.put("passenger_name", passenger_name);
//                                    map.put("driver_id", driver_id);
//                                    map.put("drivername", drivername);
//                                    map.put("bookings_type", "" + bookings_type);
//                                    map.put("pickup_latitude", pickup_latitude);
//                                    map.put("pickup_longitude", pickup_longitude);
//                                    map.put("drop_latitude", drop_latitude);
//                                    map.put("drop_longitude", drop_longitude);
//                                    map.put("notes", notes);
//                                    //   map.put("fare", bookingary.getJSONObject(i).getString("fare"));
//                                    map.put("fare", "no");
//                                    map.put("img", bookingary.getJSONObject(i).getString("profile_image"));
//                                    map.put("model_name", bookingary.getJSONObject(i).getString("model_name"));
//                                    map.put("taxi_no", bookingary.getJSONObject(i).getString("taxi_no"));
//                                    TaxiUtil.bookinglist.add(map);
//                                }
//                            }
//                        } else if (bookings_type == 0) {
//                            bookingary = json.getJSONObject("detail").getJSONArray("pending_bookings");
//
//                            for (int i = 0; i < bookingary.length(); i++) {
//                                if (bookingary.getJSONObject(i).getString("travel_status").equals("2") || bookingary.getJSONObject(i).getString("travel_status").equals("5")) {
//                                    HashMap<String, String> map = new HashMap<>();
//                                    passengers_log_id = bookingary.getJSONObject(i).getString("passengers_log_id");
//                                    pickup_location = bookingary.getJSONObject(i).getString("pickup_location");
//                                    drop_location = bookingary.getJSONObject(i).getString("drop_location");
//                                    pickuptime = bookingary.getJSONObject(i).getString("pickuptime");
//                                    travel_status = bookingary.getJSONObject(i).getString("travel_status");
//                                    passenger_name = bookingary.getJSONObject(i).getString("passenger_name");
//                                    drivername = bookingary.getJSONObject(i).getString("drivername");
//                                    driver_id = bookingary.getJSONObject(i).getString("driver_id");
//                                    pickup_latitude = bookingary.getJSONObject(i).getString("pickup_latitude");
//                                    pickup_longitude = bookingary.getJSONObject(i).getString("pickup_longitude");
//                                    drop_latitude = bookingary.getJSONObject(i).getString("drop_latitude");
//                                    drop_longitude = bookingary.getJSONObject(i).getString("drop_longitude");
//                                    notes = bookingary.getJSONObject(i).getString("notes_driver");
//                                    map.put("passengers_log_id", passengers_log_id);
//                                    map.put("pickup_location", pickup_location);
//                                    map.put("drop_location", drop_location);
//                                    map.put("pickuptime", pickuptime);
//                                    map.put("travel_status", travel_status);
//                                    map.put("passenger_name", passenger_name);
//                                    map.put("driver_id", driver_id);
//                                    map.put("drivername", drivername);
//                                    map.put("bookings_type", "" + bookings_type);
//                                    map.put("pickup_latitude", pickup_latitude);
//                                    map.put("pickup_longitude", pickup_longitude);
//                                    map.put("drop_latitude", drop_latitude);
//                                    map.put("drop_longitude", drop_longitude);
//                                    map.put("notes", notes);
//                                    if (bookingary.getJSONObject(i).has("fare"))
//                                        map.put("fare", bookingary.getJSONObject(i).getString("fare"));
//                                    else
//                                        map.put("fare", "no");
//                                    map.put("img", bookingary.getJSONObject(i).getString("profile_image"));
//                                    map.put("model_name", bookingary.getJSONObject(i).getString("model_name"));
//                                    map.put("taxi_no", bookingary.getJSONObject(i).getString("taxi_no"));
//                                    TaxiUtil.bookinglist.add(map);
//                                }
//                            }
//                        } else if (bookings_type == 4) {
//                            bookingary = json.getJSONObject("detail").getJSONArray("past_bookings");
//                            for (int i = 0; i < bookingary.length(); i++) {
//
//                                HashMap<String, String> map = new HashMap<>();
//                                passengers_log_id = bookingary.getJSONObject(i).getString("passengers_log_id");
//                                pickup_location = bookingary.getJSONObject(i).getString("pickup_location");
//                                drop_location = bookingary.getJSONObject(i).getString("drop_location");
//                                pickuptime = bookingary.getJSONObject(i).getString("pickuptime");
//                                travel_status = bookingary.getJSONObject(i).getString("travel_status");
//                                passenger_name = bookingary.getJSONObject(i).getString("passenger_name");
//                                drivername = bookingary.getJSONObject(i).getString("drivername");
//                                driver_id = bookingary.getJSONObject(i).getString("driver_id");
//                                pickup_latitude = bookingary.getJSONObject(i).getString("pickup_latitude");
//                                pickup_longitude = bookingary.getJSONObject(i).getString("pickup_longitude");
//                                drop_latitude = bookingary.getJSONObject(i).getString("drop_latitude");
//                                drop_longitude = bookingary.getJSONObject(i).getString("drop_longitude");
//                                notes = bookingary.getJSONObject(i).getString("notes_driver");
//                                map.put("passengers_log_id", passengers_log_id);
//                                map.put("pickup_location", pickup_location);
//                                map.put("drop_location", drop_location);
//                                map.put("pickuptime", pickuptime);
//                                map.put("travel_status", travel_status);
//                                map.put("passenger_name", passenger_name);
//                                map.put("driver_id", driver_id);
//                                map.put("drivername", drivername);
//                                map.put("bookings_type", "" + bookings_type);
//                                map.put("pickup_latitude", pickup_latitude);
//                                map.put("pickup_longitude", pickup_longitude);
//                                map.put("drop_latitude", drop_latitude);
//                                map.put("drop_longitude", drop_longitude);
//                                map.put("notes", notes);
//                                //map.put("fare", bookingary.getJSONObject(i).getString("fare"));
//                                map.put("fare", bookingary.getJSONObject(i).getString("fare"));
////                                map.put("fare", "11.0" +
////                                        "");
//                                map.put("img", bookingary.getJSONObject(i).getString("profile_image"));
//                                map.put("model_name", bookingary.getJSONObject(i).getString("model_name"));
//                                map.put("taxi_no", bookingary.getJSONObject(i).getString("taxi_no"));
//                                TaxiUtil.bookinglist.add(map);
//
//                            }
//                        }
//                        hs = new HashSet<HashMap<String, String>>(TaxiUtil.bookinglist);
//                        TaxiUtil.HSbookinglist.addAll(hs);
//                    } else {
//                        message = json.getString("message");
//                    }
//                } else {
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            CToast.ShowToast(getActivity(), result);
//                        }
//                    });
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//
//            System.out.println("ttttttt______"+listType);
//            if (TaxiUtil.bookinglist.size() > 0) {
//                BookingAdapter adapter = new BookingAdapter(getActivity());
//                if (listType.equalsIgnoreCase("O")) {
//                    ongoinglist.setAdapter(adapter);
//                    ongoinglist.setVisibility(View.VISIBLE);
//                    upcominglist.setVisibility(View.GONE);
//                    bookinglist.setVisibility(View.GONE);
//                } else if (listType.equalsIgnoreCase("U")) {
//                    upcominglist.setAdapter(adapter);
//                    upcominglist.setVisibility(View.VISIBLE);
//                    ongoinglist.setVisibility(View.GONE);
//                    bookinglist.setVisibility(View.GONE);
//                } else {
//                    bookinglist.setAdapter(adapter);
//                    bookinglist.setVisibility(View.VISIBLE);
//                    ongoinglist.setVisibility(View.GONE);
//                    upcominglist.setVisibility(View.GONE);
//                }
//                listGroup.setVisibility(View.VISIBLE);
//                on_nodataTxt.setVisibility(View.GONE);
//            } else {
//                bookinglist.setVisibility(View.GONE);
//                listGroup.setVisibility(View.GONE);
//                on_nodataTxt.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    /**
//     * getActivity() class helps to call the getMonthHistory API and parse the response.
//     */
//    private class getMonthHistory implements APIResult {
//        public getMonthHistory(String url, JSONObject data, boolean isMonthwise) {
//            // TODO Auto-generated constructor stub
//            if (isMonthwise && monthStart > 0)
//                new APIService_Retrofit_JSON_NoProgress(getActivity(), this, data, false).execute(url);
//            else
//                new APIService_Retrofit_JSON(getActivity(), this, data, false).execute(url);
//            misMonthwise = isMonthwise;
//        }
//
//        @Override
//        public void getResult(boolean isSuccess, final String result) {
//            // TODO Auto-generated method stub
//            if (isSuccess) {
//                if (isMonthwise) {
//                    System.out.println("-------- no need to clear" + result);
//                } else {
//                    System.out.println("-------- need to clear" + result);
//                    TaxiUtil.bookinglist.clear();
//                }
////                if (TaxiUtil.bookinglist.size() != 0) {
////                    TaxiUtil.bookinglist.clear();
////                }
//                try {
//                    JSONObject json = new JSONObject(result);
//                    if (json.getInt("status") == 1) {
//                        SessionSave.saveSession("Currency", json.getString("site_currency"), getActivity());
//                        JSONArray arry = json.getJSONArray("detail");
//                        int length = arry.length();
//                        for (int i = 0; i < length; i++) {
//                            JSONArray jarry = arry.getJSONObject(i).getJSONArray("trip_details");
//                            int l = jarry.length();
//                            for (int k = 0; k < l; k++) {
//                                HashMap<String, String> map = new HashMap<>();
//                                map.put("passengers_log_id", jarry.getJSONObject(k).getString("trip_id"));
//                                map.put("drop_location", jarry.getJSONObject(k).getString("drop_location"));
//                                map.put("pickup_location", jarry.getJSONObject(k).getString("place"));
//                                map.put("pickuptime", jarry.getJSONObject(k).getString("pickup_time"));
//                                map.put("drivername", jarry.getJSONObject(k).getString("drivername"));
//                                map.put("bookings_type", "2");
//                                map.put("pickup_latitude", jarry.getJSONObject(k).getString("pickup_latitude"));
//                                map.put("pickup_longitude", jarry.getJSONObject(k).getString("pickup_longitude"));
//                                map.put("drop_latitude", jarry.getJSONObject(k).getString("drop_latitude"));
//                                map.put("drop_longitude", jarry.getJSONObject(k).getString("drop_longitude"));
//                                map.put("notes", jarry.getJSONObject(k).getString("notes_driver"));
//                                map.put("fare", jarry.getJSONObject(k).getString("fare"));
//                                map.put("img", jarry.getJSONObject(k).getString("profile_image"));
//                                map.put("model_name", jarry.getJSONObject(k).getString("model_name"));
//                                map.put("taxi_no", jarry.getJSONObject(k).getString("taxi_no"));
//
//                                TaxiUtil.bookinglist.add(map);
//                            }
//                        }
//
//                        System.out.println("tttttttrr______"+listType);
//                        if (TaxiUtil.bookinglist.size() != 0) {
//                            if (misMonthwise) {
//                                headerMonth.setVisibility(View.VISIBLE);
//                            } else {
//                                headerMonth.setVisibility(View.GONE);
//                            }
////                            SummaryLay.setVisibility(View.VISIBLE);
////                            bookinglist.setVisibility(View.VISIBLE);
////                            CalendarLay.setVisibility(View.GONE);
//                            BookingAdapter adapter1 = new BookingAdapter(getActivity());
//                            int prevselec = bookinglist.getLastVisiblePosition();
//                            if (listType.equalsIgnoreCase("O")) {
//                                ongoinglist.setAdapter(adapter1);
//                                ongoinglist.setVisibility(View.VISIBLE);
//                                upcominglist.setVisibility(View.GONE);
//                                bookinglist.setVisibility(View.GONE);
//                            } else if (listType.equalsIgnoreCase("U")) {
//                                upcominglist.setAdapter(adapter1);
//                                upcominglist.setVisibility(View.VISIBLE);
//                                ongoinglist.setVisibility(View.GONE);
//                                bookinglist.setVisibility(View.GONE);
//                            } else {
//                                bookinglist.setAdapter(adapter1);
//                                bookinglist.setVisibility(View.VISIBLE);
//                                ongoinglist.setVisibility(View.GONE);
//                                upcominglist.setVisibility(View.GONE);
//                                System.out.println("tttttttrr______" + listType);
//                            }
//                            listGroup.setVisibility(View.VISIBLE);
//                            on_nodataTxt.setVisibility(View.GONE);
//                            if (isMonthwise) {
//                                bookinglist.setSelection(prevselec);
//                            }
//                            nodataTxt.setVisibility(View.GONE);
//                            isBack = true;
//                        }
//                    } else {
//                        isBack = false;
//                        if (misMonthwise) {
//                            if (json.getInt("status") == 0 && monthStart <= 0) {
//                                isBack = true;
//                                TaxiUtil.mTripHistorylist.clear();
//                                SummaryLay.setVisibility(View.VISIBLE);
//                                bookinglist.setVisibility(View.GONE);
//                                nodataTxt.setText("" + json.getString("message"));
//                                nodataTxt.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            CToast.ShowToast(getActivity(), json.getString("message"));
//                            SummaryBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                            TodayBtn.setBackgroundResource(R.drawable.draw_button_select);
//                            MonthBtn.setBackgroundResource(R.drawable.draw_button_unselect);
//                            SummaryBtn.setTextColor(getResources().getColor(R.color.black));
//                            TodayBtn.setTextColor(getResources().getColor(R.color.white));
//                            MonthBtn.setTextColor(getResources().getColor(R.color.black));
//                            CalendarLay.setVisibility(View.VISIBLE);
//                            SummaryLay.setVisibility(View.GONE);
//                            bookinglist.setVisibility(View.GONE);
//                        }
//                    }
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                    System.out.println("-------- ex" + e);
//                }
//            } else {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        CToast.ShowToast(getActivity(), result);
//                    }
//                });
//            }
//        }
//    }
//}