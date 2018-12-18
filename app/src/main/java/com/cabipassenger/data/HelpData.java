package com.cabipassenger.data;

/**
 * Created by developer on 1/11/16.
 * Data's required for help api
 */

public class HelpData
{
    public String HelpId, helptext, f_lng, Place, d_Place, d_Lat, d_Long, comments;
    private String notes, place_type;

    public HelpData(String _HelpId, String _helptext, String _comments)
    {
        HelpId = _HelpId;
        helptext = _helptext;
        comments = _comments;

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

    public String getHelpId()
    {
        return HelpId;
    }

    public void setFavouriteId(String helpId)
    {
        HelpId = helpId;
    }

    public String gethelptext()
    {
        return helptext;
    }

    public void sethelptext(String helptext)
    {
        this.helptext = helptext;
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
