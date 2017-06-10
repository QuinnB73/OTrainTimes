package com.quinnbudan.otraintimesv2.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quinnbudan.otraintimesv2.R;
import com.quinnbudan.otraintimesv2.activities.App;
import com.quinnbudan.otraintimesv2.interfaces.OCTranspoWebService;
import com.quinnbudan.otraintimesv2.interfaces.TaskDelegate;
import com.quinnbudan.otraintimesv2.tools.RetrofitContainer;
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
    private static final String ROUTE_NO = "1"; // static route number for testing
    private static final String STOP_NO = "7659"; // static stop number for testing
    private static final String FORMAT = "json";
    private static final String DIR1_API_STRING = "Greenboro";
    private static final String DIR2_API_STRING = "Bayview";
    private static final String DIR1 = "dir1";
    private static final String DIR2 = "dir2";
    private static final long REFRESH_DELAY = 300000;

    private String appId;
    private String apiKey;
    private String name;
    private String direction;
    private String stopNo;
    private String routeNo;
    private ArrayList<String> fullSchedule;
    private ArrayList<String> gpsSchedule;
    private Stations Stations = com.quinnbudan.otraintimesv2.tools.Stations.singletonInstance;
    private TaskDelegate delegate;
    private RetrofitContainer retrofitContainer;
    private AutoRequesterTask autoRequesterTask;

    public StationModel(String name, String direction, TaskDelegate delegate) {
        this.name = name;
        this.direction = direction;
        this.stopNo = Stations.getStopIDs().get(name);
        this.routeNo = Stations.getRouteNos().get(name);
        this.delegate = delegate;
        retrofitContainer = new RetrofitContainer(OC_TRANSPO_URL);
        requestGpsSchedule();
        startAutoRequesterTask();
    }

    public StationModel(String name, String direction, TaskDelegate delegate, Context context){
        this.appId = context.getString(R.string.appID);
        this.apiKey = context.getString(R.string.apiKey);
        this.name = name;
        this.direction = direction;
        this.stopNo = Stations.getStopIDs().get(name);
        this.routeNo = Stations.getRouteNos().get(name);
        this.delegate = delegate;
        retrofitContainer = new RetrofitContainer(OC_TRANSPO_URL);
        requestGpsSchedule();
        startAutoRequesterTask();
    }

    public StationModel(String name, String direction, TaskDelegate delegate, Context context,
                        RetrofitContainer retrofitContainer){
        this.appId = context.getString(R.string.appID);
        this.apiKey = context.getString(R.string.apiKey);
        this.name = name;
        this.direction = direction;
        this.stopNo = Stations.getStopIDs().get(name);
        this.routeNo = Stations.getRouteNos().get(name);
        this.delegate = delegate;
        this.retrofitContainer = retrofitContainer;
        requestGpsSchedule();
        startAutoRequesterTask();
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

    public String getAppId(){
        return appId;
    }

    public String getApiKey(){
        return apiKey;
    }

    public void setRetrofitContainer(RetrofitContainer retrofitContainer){
        this.retrofitContainer = retrofitContainer;
    }

    public RetrofitContainer getRetrofitContainer(){
        return retrofitContainer;
    }

    /*
     * Refresh the schedule after it's been retrieved at least once
     */
    public void refreshGpsSchedule(){
        requestGpsSchedule();
    }

    /*
     * Uses Retrofit2 and OkHttp3 to make requests to the OC Transpo API for the GPS times
     */
    public void requestGpsSchedule(){
        final ArrayList<String> tmpGpsScheule = new ArrayList<>();

        OCTranspoWebService ocTranspoWebService = retrofitContainer.getOcTranspoWebService();
        Call<TranspoApiResponse> call = ocTranspoWebService.getGpsTimes(appId, apiKey, routeNo, stopNo, FORMAT);

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

    private void startAutoRequesterTask(){
        autoRequesterTask = new AutoRequesterTask();
        autoRequesterTask.start();
    }

    private class AutoRequesterTask implements Runnable {
        private Thread thread;
        private boolean isRunning;

        public void start(){
            if(!isRunning) {
                isRunning = true;
                thread = new Thread(this);
                thread.start();
            }
        }

        @Override
        public void run() {
            while(isRunning){
                try{
                    Thread.sleep(REFRESH_DELAY);
                    refreshGpsSchedule();
                } catch (Exception e){
                    Log.e(TAG+"b_thread", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        public void stop(){
            if(isRunning){
                isRunning = false;
            }
        }
    }
}
