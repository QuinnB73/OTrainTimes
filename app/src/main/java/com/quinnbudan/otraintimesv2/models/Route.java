package com.quinnbudan.otraintimesv2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by quinnbudan on 2016-12-30.
 * OBJECT BEING MAPPED TO OC TRANSPO API RESPONSE
 */
public class Route {
    @SerializedName("RouteDirection")
    @Expose
    private ArrayList<RouteDirection> routeDirection;

    public ArrayList<RouteDirection> getRouteDirection() {
        return routeDirection;
    }

    public void setRouteDirection(ArrayList<RouteDirection> routeDirection) {
        this.routeDirection = routeDirection;
    }
}
