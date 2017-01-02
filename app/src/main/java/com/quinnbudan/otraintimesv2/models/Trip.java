package com.quinnbudan.otraintimesv2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quinnbudan on 2016-12-30.
 * OBJECT BEING MAPPED TO OC TRANSPO API RESPONSE
 */
public class Trip {
    @SerializedName("TripDestination")
    @Expose
    private String tripDestination;

    @SerializedName("TripStartTime")
    @Expose
    private String tripStartTime;

    @SerializedName("AdjustedScheduleTime")
    @Expose
    private String adjustedScheduleTime;

    @SerializedName("LastTripOfSchedule")
    @Expose
    private Boolean lastTripOfSchedule;

    @SerializedName("BusType")
    @Expose
    private String busType;

    @SerializedName("Latitude")
    @Expose
    private String latitude;

    @SerializedName("Longitude")
    @Expose
    private String longitude;

    @SerializedName("GPSSpeed")
    @Expose
    private String gpsSpeed;

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(String tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public String getAdjustedScheduleTime() {
        return adjustedScheduleTime;
    }

    public void setAdjustedScheduleTime(String adjustedScheduleTime) {
        this.adjustedScheduleTime = adjustedScheduleTime;
    }

    public Boolean getLastTripOfSchedule() {
        return lastTripOfSchedule;
    }

    public void setLastTripOfSchedule(Boolean lastTripOfSchedule) {
        this.lastTripOfSchedule = lastTripOfSchedule;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGpsSpeed() {
        return gpsSpeed;
    }

    public void setGpsSpeed(String gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }
}
