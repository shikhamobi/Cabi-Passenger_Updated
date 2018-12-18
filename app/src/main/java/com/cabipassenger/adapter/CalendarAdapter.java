/*
package com.Taximobility.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Taximobility.R;
import com.Taximobility.util.FontHelper;

public class CalendarAdapter extends BaseAdapter
{
	private final Context mContext;
	private final java.util.Calendar month;
	public GregorianCalendar pmonth; // calendar instance for previous month
	*/
/**
	 * calendar instance for previous month for getting complete view
	 *//*

	public GregorianCalendar pmonthmaxset;
	private final GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;
	private ArrayList<String> items;
	public static List<String> dayString;
	private View previousView;
	private View curentview;

	public CalendarAdapter(Context c, GregorianCalendar monthCalendar)
	{
		CalendarAdapter.dayString = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
	}

	public void setItems(ArrayList<String> items)
	{
		for (int i = 0; i != items.size(); i++)
		{
			if (items.get(i).length() == 1)
			{
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	@Override
	public int getCount()
	{
		return dayString.size();
	}

	@Override
	public Object getItem(int position)
	{
		return dayString.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		TextView dayView;
		// LinearLayout linear;
		if (convertView == null)
		{ // if it's not recycled, initialize some
			// attributes
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);
			FontHelper.applyFont(mContext, v.findViewById(R.id.cal_linear), "DroidSans.ttf");
		}
		// linear = (LinearLayout)v.findViewById(R.id.cal_linear);
		dayView = (TextView) v.findViewById(R.id.date);
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay))
		{
			// setting offdays to white color.
			dayView.setTextColor(Color.BLACK);
			dayView.setClickable(false);
			dayView.setFocusable(false);
			dayView.setBackgroundResource(R.drawable.calendar_cell);
		}
		else if ((Integer.parseInt(gridvalue) < 7) && (position > 28))
		{
			dayView.setTextColor(Color.BLACK);
			dayView.setClickable(false);
			dayView.setFocusable(false);
			dayView.setBackgroundResource(R.drawable.calendar_cell);
		}
		else if (dayString.get(position).equals(curentDateString))
		{
			dayView.setTextColor(Color.WHITE);
			dayView.setBackgroundResource(android.R.color.transparent);
		}
		else
		{
			// setting curent month's days in blue color.
			dayView.setTextColor(Color.BLACK);
			dayView.setBackgroundResource(android.R.color.transparent);
		}
		if (dayString.get(position).equals(curentDateString))
		{
			curentview = v;
			setSelected(v);
			previousView = v;
		}
		else
		{
			v.setBackgroundResource(R.drawable.list_item_background);
		}
		dayView.setText(gridvalue);
		// create date string for comparison
		String date = dayString.get(position);
		if (date.length() == 1)
		{
			date = "0" + date;
		}
		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
		if (monthStr.length() == 1)
		{
			monthStr = "0" + monthStr;
		}
		// show icon if date is not empty and it exists in the items array
		ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
		if (date.length() > 0 && items != null && items.contains(date))
		{
			iw.setVisibility(View.VISIBLE);
		}
		else
		{
			iw.setVisibility(View.INVISIBLE);
		}
		return v;
	}

	// This method used update the selected in calendar view.
	public View setSelected(View view)
	{
		if (curentview != null)
		{
			setSelected_c(curentview);
		}
		if (previousView != null)
		{
			previousView.setBackgroundResource(R.drawable.list_item_background);
		}
		previousView = view;
		view.setBackgroundResource(R.drawable.calendar_header_bgcolor_orange);
		if (curentview == view)
		{
			curentview.setBackgroundResource(R.drawable.calendar_header_bgcolor_orange);
		}
		return view;
	}

	// This method used update the current date in calendar view.
	public View setSelected_c(View view)
	{
		view.setBackgroundResource(R.drawable.calendar_cel_selectl);
		return view;
	}

	// this method to update the dates in calendar view.
	public void refreshDays()
	{
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		*/
/**
		 * Calendar instance for getting a complete gridview including the three month's (previous,current,next) dates.
		 *//*

		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		*/
/**
		 * setting the start date as previous month's required date.
		 *//*

		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);
		*/
/**
		 * filling calendar gridview.
		 *//*

		for (int n = 0; n < mnthlength; n++)
		{
			curentview = null;
			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);
		}
	}

	// this method returns the exact month of the day
	private int getMaxP()
	{
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH))
		{
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		}
		else
		{
			pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		return maxP;
	}
}*/
