package com.quinnbudan.otraintimesv2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quinnbudan on 2016-12-30.
 * OBJECT BEING MAPPED TO OC TRANSPO API RESPONSE
 */
public class RouteDirection {
    @SerializedName("RouteNo")
    @Expose
    private String routeNo;

    @SerializedName("RouteLabel")
    @Expose
    private String routeLablel;

    @SerializedName("Direction")
    @Expose
    private String direction;

    @SerializedName("Error")
    @Expose
    private String error;

    @SerializedName("RequestProcessingTime")
    @Expose
    private String requestProcessingTime;

    @SerializedName("Trips")
    @Expose
    private Trips trips;

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteLablel() {
        return routeLablel;
    }

    public void setRouteLablel(String routeLablel) {
        this.routeLablel = routeLablel;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequestProcessingTime() {
        return requestProcessingTime;
    }

    public void setRequestProcessingTime(String requestProcessingTime) {
        this.requestProcessingTime = requestProcessingTime;
    }

    public Trips getTrips() {
        return trips;
    }

    public void setTrips(Trips trips) {
        this.trips = trips;
    }
}
