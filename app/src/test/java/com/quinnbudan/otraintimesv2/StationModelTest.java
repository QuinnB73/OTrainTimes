package com.quinnbudan.otraintimesv2;

import android.content.Context;
import android.util.Log;

import com.quinnbudan.otraintimesv2.interfaces.OCTranspoWebService;
import com.quinnbudan.otraintimesv2.interfaces.TaskDelegate;
import com.quinnbudan.otraintimesv2.models.GetNextTripsForStopResult;
import com.quinnbudan.otraintimesv2.models.Route;
import com.quinnbudan.otraintimesv2.models.RouteDirection;
import com.quinnbudan.otraintimesv2.models.StationModel;
import com.quinnbudan.otraintimesv2.models.TranspoApiResponse;
import com.quinnbudan.otraintimesv2.models.Trip;
import com.quinnbudan.otraintimesv2.models.Trips;
import com.quinnbudan.otraintimesv2.tools.RetrofitContainer;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by quinnbudan on 2017-01-04.
 */
@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class StationModelTest {
    private static final String FAKE_APP_ID = "111111";
    private static final String FAKE_API_KEY = "000000";
    private static final String CONFEDERATION_STOP_NO = "3063";
    private static final String CONFEDERATION_ROUTE_NO = "750";
    private static final String JSON_FORMAT = "json";
    private static final String CONFEDERATION_STOP_LABEL = "Confederation";
    private static final String DIRECTION_1 = "dir1";
    private static final String DIRECTION_2 = "dir2";
    private static final String LABEL_1 = "Confederation";
    private static final String LABEL_2 = "Greenboro";

    @Mock
    private Context mockContext;
    @Mock
    private TaskDelegate mockTaskDelegate;
    @Mock
    private RetrofitContainer mockRetrofitContainer;
    @Mock
    private OCTranspoWebService mockOCTranspoWebService;
    @Captor
    private ArgumentCaptor<TranspoApiResponse> transpoApiResponseArgumentCaptor;
    @Captor
    private ArgumentCaptor<Callback<TranspoApiResponse>> callbackArgumentCaptor;
    @Mock
    private Call<TranspoApiResponse> mockCall;
    @Mock
    private Callback<TranspoApiResponse> mockCallback;
    @Mock
    private Response<TranspoApiResponse> mockResponse;
    @Mock
    private TranspoApiResponse mockTranspoApiResponse;
    @Mock
    private Log mockLog;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test that the StationModel successfully gets the app id and api key strings from context
     */
    @Test
    public void testStringsFromContext(){

        when(mockContext.getString(R.string.appID)).thenReturn(FAKE_APP_ID);
        when(mockContext.getString(R.string.apiKey)).thenReturn(FAKE_API_KEY);

        StationModel stationModel = new StationModel(LABEL_1, "dir1", mockTaskDelegate, mockContext);

        assertEquals(stationModel.getAppId(), FAKE_APP_ID);
        assertEquals(stationModel.getApiKey(), FAKE_API_KEY);
    }

    /**
     * Test that the requestGpsSchedule successfully results in a valid schedule when
     * given valid input
     */
    @Test
    public void testRequestGpsScheduleSuccess() {
        when(mockContext.getString(R.string.appID)).thenReturn(FAKE_APP_ID);
        when(mockContext.getString(R.string.apiKey)).thenReturn(FAKE_API_KEY);
        when(mockRetrofitContainer.getOcTranspoWebService()).thenReturn(mockOCTranspoWebService);
        when(mockOCTranspoWebService.getGpsTimes(FAKE_APP_ID, FAKE_API_KEY, CONFEDERATION_ROUTE_NO,
                CONFEDERATION_STOP_NO, JSON_FORMAT)).thenReturn(mockCall);
        when(mockResponse.body()).thenReturn(buildTranspoApiResponse());
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.errorBody()).thenReturn(null);

        StationModel stationModel = new StationModel(LABEL_1, DIRECTION_1,
                mockTaskDelegate, mockContext, mockRetrofitContainer);

        verify(mockOCTranspoWebService.getGpsTimes(FAKE_APP_ID, FAKE_API_KEY, CONFEDERATION_ROUTE_NO,
                CONFEDERATION_STOP_NO, JSON_FORMAT), times(1)).enqueue(callbackArgumentCaptor.capture());

        callbackArgumentCaptor.getValue().onResponse(mockCall, mockResponse);
        assertEquals(buildExpectedGpsSchedule(), stationModel.getGpsSchedule());
    }

    /**
     * Builds TranspoApiResponse (uses a chain of builders)
     * @return TranspoApiResponse
     */
    private TranspoApiResponse buildTranspoApiResponse(){
        TranspoApiResponse transpoApiResponse = new TranspoApiResponse();
        transpoApiResponse.setGetNextTripsForStopResult(buildGetNextTripsForStopResult());
        return transpoApiResponse;
    }

    private GetNextTripsForStopResult buildGetNextTripsForStopResult(){
        GetNextTripsForStopResult getNextTripsForStopResult = new GetNextTripsForStopResult();
        getNextTripsForStopResult.setRoute(buildRoute());
        getNextTripsForStopResult.setStopNo(CONFEDERATION_STOP_NO);
        getNextTripsForStopResult.setStopLabel(CONFEDERATION_STOP_LABEL);
        return getNextTripsForStopResult;
    }

    private Route buildRoute(){
        Route route = new Route();
        RouteDirection dir1 = new RouteDirection();
        dir1.setDirection(DIRECTION_1);
        dir1.setRouteLablel(LABEL_1);
        dir1.setRouteNo(CONFEDERATION_ROUTE_NO);
        dir1.setTrips(buildTrips());
        RouteDirection dir2 = new RouteDirection();
        dir2.setDirection(DIRECTION_2);
        dir2.setRouteLablel(LABEL_2);
        dir2.setRouteNo(CONFEDERATION_ROUTE_NO);
        dir2.setTrips(buildTrips());

        ArrayList<RouteDirection> routeDirections = new ArrayList<>();
        routeDirections.add(dir1);
        routeDirections.add(dir2);

        route.setRouteDirection(routeDirections);
        return route;
    }

    private Trips buildTrips(){
        Trips trips = new Trips();
        Trip trip1 = new Trip();
        trip1.setAdjustedScheduleTime("5");
        Trip trip2 = new Trip();
        trip2.setAdjustedScheduleTime("20");
        Trip trip3 = new Trip();
        trip3.setAdjustedScheduleTime("35");
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip1);
        tripList.add(trip2);
        tripList.add(trip3);
        trips.setTrip(tripList);
        return trips;
    }

    /**
     * Builds the expected GPS schedule
     * @return times
     */
    private ArrayList<String> buildExpectedGpsSchedule(){
        String time1 = "5m";
        String time2 = "20m";
        String time3 = "35m";
        ArrayList<String> times = new ArrayList<>();
        times.add(time1);
        times.add(time2);
        times.add(time3);
        return times;
    }
}
