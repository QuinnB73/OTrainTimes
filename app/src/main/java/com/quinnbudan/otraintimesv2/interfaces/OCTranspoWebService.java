package com.quinnbudan.otraintimesv2.interfaces;

import com.quinnbudan.otraintimesv2.models.StationModel;
import com.quinnbudan.otraintimesv2.models.TranspoApiResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by quinnbudan on 2016-12-29.
 */
public interface OCTranspoWebService {
    /*
        Makes a request to the OC Transpo API for the GPS times in minutes
     */
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("GetNextTripsForStop")
    Call<TranspoApiResponse> getGpsTimes(@Field("appID") String appId,
                                         @Field("apiKey") String apiKey,
                                         @Field("routeNo") String routeNo,
                                         @Field("stopNo") String stopNo,
                                         @Field("format") String format);
}
