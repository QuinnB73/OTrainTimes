package com.quinnbudan.otraintimesv2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quinnbudan on 2016-12-30.
 * OBJECT BEING MAPPED TO OC TRANSPO API RESPONSE
 */
public class GetNextTripsForStopResult {
    @SerializedName("StopNo")
    @Expose
    private String stopNo;

    @SerializedName("StopLabel")
    @Expose
    private String stopLabel;

    @SerializedName("Error")
    @Expose
    private String error;

    @SerializedName("Route")
    @Expose
    private Route route;

    public String getStopNo() {
        return stopNo;
    }

    public void setStopNo(String stopNo) {
        this.stopNo = stopNo;
    }

    public String getStopLabel() {
        return stopLabel;
    }

    public void setStopLabel(String stopLabel) {
        this.stopLabel = stopLabel;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
