package com.quinnbudan.otraintimesv2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by quinnbudan on 2016-12-30.
 * OBJECT BEING MAPPED TO OC TRANSPO API RESPONSE
 */
public class TranspoApiResponse {
    @SerializedName("GetNextTripsForStopResult")
    @Expose
    private GetNextTripsForStopResult getNextTripsForStopResult;

    public GetNextTripsForStopResult getGetNextTripsForStopResult() {
        return getNextTripsForStopResult;
    }

    public void setGetNextTripsForStopResult(GetNextTripsForStopResult getNextTripsForStopResult) {
        this.getNextTripsForStopResult = getNextTripsForStopResult;
    }
}
