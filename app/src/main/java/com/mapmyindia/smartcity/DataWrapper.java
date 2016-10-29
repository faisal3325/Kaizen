package com.mapmyindia.smartcity;

import android.os.Parcelable;

import com.mmi.apis.routing.Trip;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Faisal on 29-Oct-16.
 */
public class DataWrapper extends ArrayList<Parcelable> implements Serializable {

    private ArrayList<Trip> trips;

    public DataWrapper(ArrayList<Trip> data) {
        this.trips = data;
    }

    public ArrayList<Trip> getTrips() {
        return this.trips;
    }

}