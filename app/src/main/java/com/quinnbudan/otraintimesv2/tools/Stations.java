package com.quinnbudan.otraintimesv2.tools;

import java.util.HashMap;

/**
 * Created by quinnbudan on 2016-12-29.
 */
public class Stations {
    public static final Stations singletonInstance = new Stations();
    private static final String[] stationNames = {"Greenboro", "Confederation", "Carleton", "Carling", "Bayview"};
    private static final String[] staticStopIDs = {"3037", "3063", "3062", "3061", "3060"};
    private static final String   staticRouteNo = "750"; // INTERNAL ROUTE NUMBER USED FOR O-TRAIN-TRILLIUM-LINE
    private HashMap<String, String> stopIDs = null;
    private HashMap<String, String> routeNos = null;

    private Stations(){
    }

    /*
     * Initializes the HashMap that maps names to stop IDs
     */
    private void initStopIds(){
        stopIDs = new HashMap<>();
        for(int i = 0; i < stationNames.length; i++){
            stopIDs.put(stationNames[i], staticStopIDs[i]);
        }
    }

    /*
     * Initializes the HashMap that maps names to the static route number 750, which is used
     * internally by OC Transpo to denote the OTrain
     */
    private void initRouteNos(){
        routeNos = new HashMap<>();
        for(int i = 0; i < stationNames.length; i++){
            routeNos.put(stationNames[i], staticRouteNo);
        }
    }

    public HashMap<String, String> getStopIDs(){
        if(stopIDs == null){
            initStopIds();
        }
        return stopIDs;
    }

    public HashMap<String, String> getRouteNos(){
        if(routeNos == null){
            initRouteNos();
        }
        return routeNos;
    }

    public String[] getStationNames(){
        return stationNames;
    }
}
