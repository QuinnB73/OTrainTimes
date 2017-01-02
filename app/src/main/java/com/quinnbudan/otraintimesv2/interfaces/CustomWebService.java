package com.quinnbudan.otraintimesv2.interfaces;

import com.quinnbudan.otraintimesv2.models.StationModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by quinnbudan on 2016-12-29.
 */
public interface CustomWebService {
    /*
        Web service created to make the requests to the custom web server in place of the
        OC Transpo API
        NOT YET IMPLEMENTED
     */
    @Headers("Content-Type: application/json")
    @GET("/api.json")
    Call<StationModel> getFullSchedule();
}
