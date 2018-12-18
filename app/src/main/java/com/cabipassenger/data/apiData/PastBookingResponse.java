package com.cabipassenger.data.apiData;

/**
 * Created by developer on 1/11/16.
 * Contains data for past booking response
 */


import java.util.ArrayList;
import java.util.List;

public class PastBookingResponse {

    public String message;
    public Integer status;
    public String siteCurrency;
    public List<UpcomingResponse.PastBooking> trip_details = new ArrayList<>();

}

