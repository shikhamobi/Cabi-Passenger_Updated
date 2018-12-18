package com.cabipassenger.data;

/**
 * Getter/setter class to hold the trip history details.
 */
//
public class TripHistoryData
{
	public String createdate, place, fare, time, tripid;
	public int dateFlag;

	public TripHistoryData(String _createdate, String _place, String _fare, String _time, int _dateFlag, String _tripid)
	{
		createdate = _createdate;
		place = _place;
		fare = _fare;
		time = _time;
		dateFlag = _dateFlag;
		tripid = _tripid;
	}

	public String getTripid()
	{
		return tripid;
	}

	public void setTripid(String tripid)
	{
		this.tripid = tripid;
	}

	public int getDateFlag()
	{
		return dateFlag;
	}

	public void setDateFlag(int dateFlag)
	{
		this.dateFlag = dateFlag;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getCreatedate()
	{
		return createdate;
	}

	public void setCreatedate(String createdate)
	{
		this.createdate = createdate;
	}

	public String getPlace()
	{
		return place;
	}

	public void setPlace(String place)
	{
		this.place = place;
	}

	public String getFare()
	{
		return fare;
	}

	public void setFare(String fare)
	{
		this.fare = fare;
	}
}
