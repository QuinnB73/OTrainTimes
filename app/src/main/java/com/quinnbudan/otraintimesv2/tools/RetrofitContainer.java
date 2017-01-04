package com.quinnbudan.otraintimesv2.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quinnbudan.otraintimesv2.interfaces.OCTranspoWebService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by quinnbudan on 2017-01-04.
 */
public class RetrofitContainer {

    private OCTranspoWebService ocTranspoWebService;


    public RetrofitContainer(String ocTranspoURL){
        // setup retrofit 2, if we don't use verbose logging then don't set client
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ocTranspoURL).client(getClient()).
                addConverterFactory(GsonConverterFactory.create(gson)).build();
        ocTranspoWebService = retrofit.create(OCTranspoWebService.class);
    }

    public OkHttpClient getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return client;
    }

    public void setOcTranspoWebService(OCTranspoWebService ocTranspoWebService){
        this.ocTranspoWebService = ocTranspoWebService;
    }

    public OCTranspoWebService getOcTranspoWebService(){
        return ocTranspoWebService;
    }
}
