package com.cabipassenger.util;

import android.location.Location;

/**
 * Class is to calculate the distance between two location in both KM and Miles.
 */
public class FindDistance
{
	public static double rad2deg(double rad)
	{
		return (rad * 180 / Math.PI);
	}

	public static double deg2rad(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2, String string)
	{
//		double theta = lon1 - lon2;
//		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//		dist = Math.acos(dist);
//		dist = rad2deg(dist);
//		dist = dist * 60 * 1.1515;
//		System.out.println("dddnaga"+lat1+"__"+lon1+"___"+lat2+"__"+lon2);
//		if (string.equals("m"))
//		{
//			dist = dist * 1.609344;
//		}


//		else if (string.equals("m"))
//		{
//			dist = dist * 0.8684;
//		}
		return distance(lat1, lon1, lat2, lon2,  string,new Location(""));
	}
	public static double distance(double lat1, double lon1, double lat2, double lon2, String string,Location location)
	{
//		double theta = lon1 - lon2;
//		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//		dist = Math.acos(dist);
//		dist = rad2deg(dist);
//		dist = dist * 60 * 1.1515;
//		System.out.println("dddnaga"+lat1+"__"+lon1+"___"+lat2+"__"+lon2);
	//	System.out.println("dddnaga"+lat1+"__"+lon1+"___"+lat2+"__"+lon2+"__"+string);
		float dist[]=new float[2];
		double distance;
		new Location("").distanceBetween(lat1,lon1,lat2,lon2,dist);
		distance=dist[0]/1000;
		if (string.equals("m"))
		{
			distance = distance / 1.609344;
		}

		//System.out.println("dddnaga"+lat1+"__"+lon1+"___"+lat2+"__"+lon2+distance);
//		else if (string.equals("m"))
//		{
//			dist = dist * 0.8684;
//		}
		return distance;
	}
}
