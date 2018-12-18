//package com.Taximobility.calendercustom;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.cabi.R;
//
//import java.text.DateFormatSymbols;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//@SuppressLint("NewApi")
//public class MainActivity extends AppCompatActivity {
//
//	ImageView prev, next;
//	ViewPager pager;
//	// MyPageAdapter pageAdapter;
//	private static int TOTAL_MONTH;
//	public static String START_MONTH, START_YEAR, END_MONTH, END_YEAR;
//	public static int iYear = 1999;
//	public static int iMonth = Calendar.FEBRUARY;
//	public static TextView calendertitle;
//	public int forfirst;
//	private static boolean BY_BUTTON;
//	private String scrolled = "positive";
//	public int currentposition;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		prev = (ImageView) findViewById(R.id.prev);
//		next = (ImageView) findViewById(R.id.next);
//		TOTAL_MONTH = getMonthDiff("20/09/2000", "24/02/2016");
//		datedetails("20/09/2000", "24/02/2016");
//		iYear = Integer.parseInt(START_YEAR.trim());
//		iMonth = Integer.parseInt(START_MONTH);
//		System.out.println("Total" + "***" + START_MONTH + "*" + END_YEAR + "&"
//				+ maxdate() + Calendar.FEBRUARY + Calendar.JANUARY
//				+ Calendar.AUGUST);
//		iMonth = 2;
//		iYear = 2016;
//		FragmentTransaction fragtrans = getSupportFragmentManager()
//				.beginTransaction();
//		Bundle bundle = new Bundle();
//		bundle.putInt("month", iMonth);
//		bundle.putInt("year", iYear);
//		// set Fragmentclass Arguments
//		Fragment f = new CalenderFragment();
//		f.setArguments(bundle);
//		fragtrans.add(R.id.viewpager, f, "");
//		fragtrans.commit();
//		calendertitle = (TextView) findViewById(R.id.calendertitle);
//		calendertitle.setText(String.valueOf(new DateFormatSymbols()
//				.getMonths()[iMonth - 1]) + "-" + String.valueOf(iYear));
//
//		prev.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				onPreviousCliked();
//			}
//		});
//		next.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				onNextClicked();
//
//			}
//		});
//
//	}
//
//	protected void onNextClicked() {
//		// TODO Auto-generated method stub
//		boolean isExcecuted = false;
//		int tempmonth = iMonth;
//		int tempyear = iYear;
//		if (iMonth < 12) {
//			iMonth++;
//		} else if (iMonth == 12) {
//			System.out.println("iiiii" + iMonth + "d" + iYear + 1);
//			iYear = iYear + 1;
//			iMonth = 1;
//			System.out.println("iiiii" + iMonth + "d" + iYear);
//		}
//		if (iYear <= Integer.parseInt(END_YEAR)) {
//			if (iYear == Integer.parseInt(END_YEAR)) {
//				if (iMonth <= Integer.parseInt(END_MONTH)) {
//					isExcecuted = true;
//					calendertitle
//							.setText(String.valueOf(new DateFormatSymbols()
//									.getMonths()[iMonth - 1])
//									+ "-"
//									+ String.valueOf(iYear));
//					Bundle bundle = new Bundle();
//					bundle.putInt("month", iMonth);
//					bundle.putInt("year", iYear);
//					// set Fragmentclass Arguments
//					Fragment f = new CalenderFragment();
//					f.setArguments(bundle);
//					// pager.setCurrentItem(1);
//					FragmentTransaction fragtrans = getSupportFragmentManager()
//							.beginTransaction();
//					fragtrans.replace(R.id.viewpager, f, "");
//					fragtrans.commit();
//				}
//			} else {
//				isExcecuted = true;
//				calendertitle
//						.setText(String.valueOf(new DateFormatSymbols()
//								.getMonths()[iMonth - 1])
//								+ "-"
//								+ String.valueOf(iYear));
//
//				FragmentTransaction fragtrans = getSupportFragmentManager()
//						.beginTransaction();
//				Bundle bundle = new Bundle();
//				bundle.putInt("month", iMonth);
//				bundle.putInt("year", iYear);
//				// set Fragmentclass Arguments
//				Fragment f = new CalenderFragment();
//				f.setArguments(bundle);
//				fragtrans.replace(R.id.viewpager, f, "");
//				fragtrans.commit();
//			}
//
//		}
//
//		if (!isExcecuted) {
//			iMonth = tempmonth;
//			iYear = tempyear;
//		}
//
//	}
//
//	protected void onPreviousCliked() {
//		// TODO Auto-generated method stub
//		boolean isExcecuted = false;
//		int tempmonth = iMonth;
//		int tempyear = iYear;
//		if (iMonth > 1) {
//			iMonth--;
//		} else if (iMonth == 1) {
//			System.out.println("iiiii" + iMonth + "d" + iYear);
//			iYear = iYear - 1;
//			iMonth = 12;
//			System.out.println("iiiii" + iMonth + "d" + iYear);
//		}
//		if (iYear >= Integer.parseInt(START_YEAR)) {
//			if (iYear == Integer.parseInt(START_YEAR)) {
//				if (iMonth >= Integer.parseInt(START_MONTH)) {
//					calendertitle
//							.setText(String.valueOf(new DateFormatSymbols()
//									.getMonths()[iMonth - 1])
//									+ "-"
//									+ String.valueOf(iYear));
//
//					FragmentTransaction fragtrans = getSupportFragmentManager()
//							.beginTransaction();
//					Bundle bundle = new Bundle();
//					bundle.putInt("month", iMonth);
//					bundle.putInt("year", iYear);
//					// set Fragmentclass Arguments
//					Fragment f = new CalenderFragment();
//					f.setArguments(bundle);
//					fragtrans.replace(R.id.viewpager, f, "");
//					fragtrans.commit();
//					isExcecuted = true;
//				}
//			} else {
//				calendertitle
//						.setText(String.valueOf(new DateFormatSymbols()
//								.getMonths()[iMonth - 1])
//								+ "-"
//								+ String.valueOf(iYear));
//
//				FragmentTransaction fragtrans = getSupportFragmentManager()
//						.beginTransaction();
//				Bundle bundle = new Bundle();
//				bundle.putInt("month", iMonth);
//				bundle.putInt("year", iYear);
//				// set Fragmentclass Arguments
//				Fragment f = new CalenderFragment();
//				f.setArguments(bundle);
//				fragtrans.replace(R.id.viewpager, f, "");
//				fragtrans.commit();
//				isExcecuted = true;
//			}
//
//		}
//		if (!isExcecuted) {
//			iMonth = tempmonth;
//			iYear = tempyear;
//		}
//	}
//
//	private void datedetails(String start, String end) {
//		// TODO Auto-generated method stub
//		String[] starta = start.split("/");
//		String[] enda = end.split("/");
//
//		START_MONTH = starta[1];
//		START_YEAR = starta[2];
//		END_MONTH = enda[1];
//		END_YEAR = enda[2];
//	}
//
//	static int maxdate() {
//
//		int iDay = 1;
//
//		// Create a calendar object and set year and month
//		Calendar mycal = new GregorianCalendar(iYear, iMonth - 1, iDay);
//
//		// Get the number of days in that month
//		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
//		return daysInMonth;
//	}
//
//	static int maxdatec(int month, int year) {
//
//		int iDay = 1;
//
//		// Create a calendar object and set year and month
//		Calendar mycal = new GregorianCalendar(year, month - 1, iDay);
//
//		// Get the number of days in that month
//		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
//		System.out.println("nagaga" + month + "--" + year + "--" + daysInMonth);
//		return daysInMonth;
//	}
//
//	static int startingDate() {
//		System.out.println("startcheck" + iMonth);
//
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.DATE, 1);
//		cal.set(Calendar.MONTH, iMonth - 1);
//		cal.set(Calendar.YEAR, iYear);
//
//		System.out.println("fdfddfdf" + iMonth + iYear);
//		System.out.println("fdfddfdf" + cal.get(Calendar.DAY_OF_WEEK));
//
//		return (cal.get(Calendar.DAY_OF_WEEK) - 1);
//
//	}
//
//	static int startingDatec(int month, int year) {
//		System.out.println("startcheck" + month);
//
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.DATE, 1);
//		cal.set(Calendar.MONTH, month - 1);
//		cal.set(Calendar.YEAR, year);
//
//		System.out.println("fdfddfdf" + month + year);
//		System.out.println("fdfddfdf" + cal.get(Calendar.DAY_OF_WEEK));
//		System.out.println("nagaga" + month + "--" + year + "--"
//				+ (cal.get(Calendar.DAY_OF_WEEK) - 1));
//		return (cal.get(Calendar.DAY_OF_WEEK) - 1);
//
//	}
//
//	private int getMonthDiff(String startDates, String endDates) {
//		Date startDate = null, endDate = null;
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//		try {
//
//			startDate = formatter.parse(startDates);
//			endDate = formatter.parse(endDates);
//			System.out.println("dddd" + startDate);
//			System.out.println("dddde" + formatter.format(startDate));
//
//		} catch (ParseException e) {
//			System.out.println("error" + e);
//		}
//
//		Calendar startCalendar = new GregorianCalendar();
//		startCalendar.setTime(startDate);
//		Calendar endCalendar = new GregorianCalendar();
//		endCalendar.setTime(endDate);
//		int diffYear = endCalendar.get(Calendar.YEAR)
//				- startCalendar.get(Calendar.YEAR);
//		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH)
//				- startCalendar.get(Calendar.MONTH);
//		return diffMonth;
//	}
//}
