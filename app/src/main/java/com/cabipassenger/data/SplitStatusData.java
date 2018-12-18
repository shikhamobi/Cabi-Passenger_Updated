package com.cabipassenger.data;

import android.content.Context;
import android.graphics.Color;

import com.cabipassenger.R;
import com.cabipassenger.util.NC;

/**
 * Created by developer on 6/6/16.
 * This class is used to get Split status data from api
 */
public class SplitStatusData {
    String image;
    String name;
    String status;
    String status_text;
    Context c;

    public String getSplitfare() {
        return splitfare;
    }

    public void setSplitfare(String splitfare) {
        this.splitfare = splitfare;
    }

    public String getFare_perc() {
        return fare_perc;
    }

    public void setFare_perc(String fare_perc) {
        this.fare_perc = fare_perc;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    String splitfare,fare_perc,wallet;

    public SplitStatusData(Context c) {
        this.c = c;
    }

    public int getStatus_color() {
        return status_color;
    }

    int status_color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (status.equalsIgnoreCase("I")) {
            status_color = Color.BLUE;
            setStatus_text(NC.getString(R.string.inprogress));
        } else if (status.equalsIgnoreCase("A")) {
            status_color = Color.GREEN;
            setStatus_text(NC.getString(R.string.accepted));
        } else if (status.equalsIgnoreCase("D")) {
            status_color = Color.RED;
            setStatus_text(NC.getString(R.string.rejected));
        }
    }


    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }
}
