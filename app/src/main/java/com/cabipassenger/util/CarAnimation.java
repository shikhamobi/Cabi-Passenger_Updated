package com.cabipassenger.util;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.cabipassenger.fragments.BookTaxiNewFrag;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * This class is used to animate the marker from one location to another location asynchronously with given time interval.
 * @author developer
 */
public class CarAnimation extends AsyncTask<String, String, String>
{
	Context mContext;
	Marker mMarker;
	LatLngInterpolator mLatLngInterpolator;
	private List<String> slatlng;
	int j, i, driverPosition, listLength, z = 0;
	private Runnable car;
	String StringArraylatlng[];
	String[] splitcomma, splitcomma1;
	public String driver_coordinatess, mdriver_id;

	public CarAnimation(Context context, Marker m, LatLngInterpolator latLngInterpolator, int k, List<String> latlnglist, String driver_id)
	{
		mContext = context;
		mMarker = m;
		mLatLngInterpolator = latLngInterpolator;
		slatlng = latlnglist;
		j = 1;
		i = 0;
		driverPosition = k;
		mdriver_id = driver_id;
	}

	public void call()
	{
		final Handler handler1 = new Handler(Looper.getMainLooper());
		handler1.post(car = new Runnable()
		{
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run()
			{
				if (j < slatlng.size())
				{
					if (i != slatlng.size())
					{
						splitcomma = slatlng.get(j).split(",");
						splitcomma1 = slatlng.get(i).split(",");
						final ValueAnimator valueAnimator = new ValueAnimator();
						valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
						{
							@SuppressLint("NewApi")
							@Override
							public void onAnimationUpdate(ValueAnimator animation)
							{
								try
								{
									float v = animation.getAnimatedFraction();
									if (BookTaxiNewFrag.z == 1)
									{
										handler1.removeCallbacks(car);
									}
									LatLng newPosition = mLatLngInterpolator.interpolate(v, new LatLng(Double.parseDouble(splitcomma1[0]), Double.parseDouble(splitcomma1[1])), new LatLng(Double.parseDouble(splitcomma[0]), Double.parseDouble(splitcomma[1])));
									mMarker.setPosition(newPosition);
								}
								catch (ArrayIndexOutOfBoundsException e)
								{
									// TODO: handle exception
									handler1.removeCallbacks(car);
									e.printStackTrace();
								}
							}
						});
						valueAnimator.setFloatValues(0, 1); // Ignored.
						valueAnimator.setDuration(4000);
						valueAnimator.start();
						try
						{
							slatlng.remove(i);
							TaxiUtil.mDriverdata.get(driverPosition).setList(slatlng);
							handler1.postDelayed(car, 4000);
						}
						catch (IndexOutOfBoundsException e)
						{
							// TODO: handle exception'
							handler1.removeCallbacks(car);
							e.printStackTrace();
						}
					}
					else
					{
						handler1.removeCallbacks(car);
					}
				}
				else
				{
					handler1.removeCallbacks(car);
				}
			}
		});
	}

	@Override
	protected String doInBackground(String... params)
	{
		call();
		return null;
		// TODO Auto-generated method stub
	}
}