package com.quinnbudan.otraintimesv2;

import android.content.Context;

import com.quinnbudan.otraintimesv2.activities.App;
import com.quinnbudan.otraintimesv2.interfaces.OCTranspoWebService;
import com.quinnbudan.otraintimesv2.interfaces.TaskDelegate;
import com.quinnbudan.otraintimesv2.models.StationModel;
import com.quinnbudan.otraintimesv2.models.TranspoApiResponse;
import com.quinnbudan.otraintimesv2.tools.RetrofitContainer;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import retrofit2.Call;


/**
 * Created by quinnbudan on 2017-01-04.
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class UnitTests {
    private static final String FAKE_APP_ID = "111111";
    private static final String FAKE_API_KEY = "000000";

    @Mock
    private Context mockContext;
    @Mock
    private TaskDelegate mockTaskDelegate;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStringsFromContext(){
        when(mockContext.getString(R.string.appID)).thenReturn(FAKE_APP_ID);
        when(mockContext.getString(R.string.apiKey)).thenReturn(FAKE_API_KEY);

        // Testing StationModel's correctness
        StationModel stationModel = new StationModel("Confederation", "dir1", mockTaskDelegate, mockContext);

        assertEquals(stationModel.getAppId(), FAKE_APP_ID);
        assertEquals(stationModel.getApiKey(), FAKE_API_KEY);
    }
}
