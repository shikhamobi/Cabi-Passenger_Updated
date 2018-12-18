package com.cabipassenger.interfaces;

/**
 * Created by developer on 14/3/17.
 */
public interface PickupDropSet {


    public void pickUpSet(double latitude, double longtitue);

    public void dropSet(double latitude, double longtitue);

    void requestPickupAddress();
}
