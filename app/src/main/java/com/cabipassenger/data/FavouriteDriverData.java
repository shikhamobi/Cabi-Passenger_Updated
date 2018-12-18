package com.cabipassenger.data;

/**
 * Created by developer on 7/10/16.
 * Getter/setter class to hold the Favourite Driver Data.
 */
public class FavouriteDriverData {


    public String DriverId, name, email, phone, profile_image,taxino;

    public FavouriteDriverData(String _DriverId, String _name, String _email, String _phone, String _profile_image,String _taxino)
    {
        DriverId = _DriverId;
        name = _name;
        email = _email;
        phone = _phone;
        profile_image = _profile_image;
        taxino = _taxino;
    }

    public String getFavorite_drivername()
    {
        return name;
    }

    public void setFavorite_drivername(String _name)
    {
        this.name = _name;
    }

    public String getFavorite_driveremail()
    {
        return email;
    }

    public void setFavorite_driveremail(String _email)
    {
        this.email = _email;
    }

    public String getFavorite_driverphone()
    {
        return phone;
    }

    public void setFavorite_driverphone(String _phone)
    {
        this.phone = _phone;
    }

    public String getFavorite_driverimage()
    {
        return profile_image;
    }

    public void setFavorite_driverimage(String _profile_image)
    {
        this.profile_image = _profile_image;
    }

    public String getFavorite_taxino()
    {
        return taxino;
    }

    public void setFavorite_taxino(String _taxino)
    {
        this.taxino = _taxino;
    }


}
