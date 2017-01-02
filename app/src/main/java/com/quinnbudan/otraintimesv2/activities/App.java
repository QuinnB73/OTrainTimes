package com.quinnbudan.otraintimesv2.activities;

import android.app.Application;
import android.content.Context;

/**
 * Created by quinnbudan on 2017-01-02.
 */
public class App extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
