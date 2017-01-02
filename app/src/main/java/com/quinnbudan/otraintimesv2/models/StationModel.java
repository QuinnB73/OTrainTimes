package com.quinnbudan.otraintimesv2.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quinnbudan.otraintimesv2.interfaces.OCTranspoWebService;
import com.quinnbudan.otraintimesv2.interfaces.TaskDelegate;
import com.quinnbudan.otraintimesv2.tools.Stations;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by quinnbudan on 2016-12-29.
 */
public class StationModel {
    private static final String TAG = "StationModel";
    private static final String CUSTOM_API_URL = "http://api.quinnbudan.com/";
    private static final String OC_TRANSPO_URL = "https://api.octranspo1.com/v1.2/";
    private static final String APP_ID = "placeholder"; // provide your own APP_ID and API_KEY
    private static final String API_KEY = "placeholder";
    private static final String ROUTE_NO = "1"; // static route number for testing
    private static final String STOP_NO = "7659"; // static stop number for testing
    private static final String FORMAT = "json";
    private static final String DIR1_API_STRING = "Greenboro";
    private static final String DIR2_API_STRING = "Bayview";
    private static final String DIR1 = "dir1";
    private static final String DIR2 = "dir2";

    private String name;
    private String direction;
    private String stopNo;
    private String routeNo;
    private ArrayList<String> fullSchedule;
    private ArrayList<String> gpsSchedule;
    private Stations Stations = com.quinnbudan.otraintimesv2.tools.Stations.singletonInstance;
    private TaskDelegate delegate;

    public StationModel(String name, String direction, TaskDelegate delegate) {
        this.name = name;
        this.direction = direction;
        this.stopNo = Stations.getStopIDs().get(name);
        this.routeNo = Stations.getRouteNos().get(name);
        this.delegate = delegate;
        requestGpsSchedule();
    }

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }

    private void setFullSchedule(ArrayList<String> fullSchedule) {
        this.fullSchedule = fullSchedule;
    }

    private void setGpsSchedule(ArrayList<String> gpsSchedule) {
        this.gpsSchedule = gpsSchedule;
        Log.d(TAG, "NEW SCHED: " + gpsSchedule.toString());
    }

    public ArrayList<String> getFullSchedule() {
        return fullSchedule;
    }

    public ArrayList<String> getGpsSchedule() {
        return gpsSchedule;
    }

    /*
     * Refresh the schedule after it's been retrieved at least once
     */
    public void refreshGpsSchedule(){
        // TODO: do on an interval
        requestGpsSchedule();
    }

    /*
     * Uses Retrofit2 and OkHttp3 to make requests to the OC Transpo API for the GPS times
     */
    private void requestGpsSchedule(){
        final ArrayList<String> tmpGpsScheule = new ArrayList<>();
        // FOR VERBOSE LOGGING
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // setup retrofit 2, if we don't use verbose logging then don't set client
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(OC_TRANSPO_URL).client(client).
                addConverterFactory(GsonConverterFactory.create(gson)).build();
        OCTranspoWebService ocTranspoWebService = retrofit.create(OCTranspoWebService.class);
        Call<TranspoApiResponse> call = ocTranspoWebService.getGpsTimes(APP_ID, API_KEY, routeNo, stopNo, FORMAT);

        call.enqueue(new Callback<TranspoApiResponse>() { // is an async call
            @Override
            public void onResponse(Call<TranspoApiResponse> call, Response<TranspoApiResponse> response) {
                try {
                    Log.d(TAG, "RESPONSE: " + response.errorBody().string());
                } catch (Exception e) {
                    // do nothing, no errorBody
                }
                if (response.isSuccessful()) {
                    TranspoApiResponse apiResponse = response.body();
                    parseTimesFromResponse(apiResponse.getGetNextTripsForStopResult().getRoute().getRouteDirection(), tmpGpsScheule);
                    Log.d(TAG, "body: " + response.body().toString());
                    setGpsSchedule(tmpGpsScheule);
                    delegate.taskCompletionResult(tmpGpsScheule);
                }
            }

            @Override
            public void onFailure(Call<TranspoApiResponse> call, Throwable t) {
                Log.e(TAG, "FAILURE: " + t.getMessage());
            }
        });
    }

    /*
     * Gets passed in an ArrayList of RouteDirection objects and an retrieved the
     * "AdjustedScheduleTime" property from each Trip object, populates the
     * output parameter gpsSchedule with 3 GPS times in minutes (note that this
     * number 3 is always constant)
     */
    private void parseTimesFromResponse(ArrayList<RouteDirection> routeDirections,
                                        ArrayList<String> gpsSchedule){
        if (routeDirections != null) {
            for (int i = 0; i < routeDirections.size(); i++) {
                if (routeDirections.get(i).getRouteLablel().equals(DIR1_API_STRING) && direction.equals(DIR1)) {
                    ArrayList<Trip> tmpTrips = (ArrayList<Trip>) routeDirections.get(i).getTrips().getTrip();
                    if(tmpTrips != null) {
                        String time;
                        for (int j = 0; j < tmpTrips.size(); j++) {
                            time = tmpTrips.get(j).getAdjustedScheduleTime() + "m";
                            gpsSchedule.add(time);
                        }
                    }
                } else if (routeDirections.get(i).getRouteLablel().equals(DIR2_API_STRING) && direction.equals(DIR2)) {
                    ArrayList<Trip> tmpTrips = (ArrayList<Trip>) routeDirections.get(i).getTrips().getTrip();
                    if(tmpTrips != null) {
                        String time;
                        for (int j = 0; j < tmpTrips.size(); j++) {
                            time = tmpTrips.get(j).getAdjustedScheduleTime() + "m";
                            gpsSchedule.add(time);
                        }
                    }
                }
            }
        }
    }
}
