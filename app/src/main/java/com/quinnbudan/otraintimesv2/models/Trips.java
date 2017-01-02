package com.quinnbudan.otraintimesv2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by quinnbudan on 2016-12-30.
 * OBJECT BEING MAPPED TO OC TRANSPO API RESPONSE
 */
public class Trips {
    @SerializedName("Trip")
    @Expose
    private List<Trip> trip;

    public List<Trip> getTrip() {
        return trip;
    }

    public void setTrip(List<Trip> trip) {
        this.trip = trip;
    }
}
