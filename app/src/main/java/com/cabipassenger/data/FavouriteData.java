package com.cabipassenger.data;

/**
 * Getter/setter class to hold the favorite details.
 */

public class FavouriteData
{
	public String FavouriteId;
	public String f_lat;
	public String f_lng;
	public String Place;
	public String d_Place;
	public String d_Lat;
	public String d_Long;
	public String comments;

	public String getMap_image() {
		return map_image;
	}

	public void setMap_image(String map_image) {
		this.map_image = map_image;
	}

	public String map_image;
	private String notes, place_type;

	public FavouriteData(String _FavouriteId, String _f_lat, String _f_lng, String _Place, String _d_place, String _d_lat, String _d_long, String _comments, String _notes, String _place_type,String map_image)
	{
		FavouriteId = _FavouriteId;
		f_lat = _f_lat;
		f_lng = _f_lng;
		Place = _Place;
		d_Place = _d_place;
		d_Lat = _d_lat;
		d_Long = _d_long;
		comments = _comments;
		notes = _notes;
		place_type = _place_type;
		this.map_image=map_image;
	}

	public String getPlace_type()
	{
		return place_type;
	}

	public void setPlace_type(String place_type)
	{
		this.place_type = place_type;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getD_Place()
	{
		return d_Place;
	}

	public void setD_Place(String d_Place)
	{
		this.d_Place = d_Place;
	}

	public String getD_Lat()
	{
		return d_Lat;
	}

	public void setD_Lat(String d_Lat)
	{
		this.d_Lat = d_Lat;
	}

	public String getD_Long()
	{
		return d_Long;
	}

	public void setD_Long(String d_Long)
	{
		this.d_Long = d_Long;
	}

	public String getFavouriteId()
	{
		return FavouriteId;
	}

	public void setFavouriteId(String favouriteId)
	{
		FavouriteId = favouriteId;
	}

	public String getF_lat()
	{
		return f_lat;
	}

	public void setF_lat(String f_lat)
	{
		this.f_lat = f_lat;
	}

	public String getF_lng()
	{
		return f_lng;
	}

	public void setF_lng(String f_lng)
	{
		this.f_lng = f_lng;
	}

	public String getPlace()
	{
		return Place;
	}

	public void setPlace(String place)
	{
		Place = place;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}
}
