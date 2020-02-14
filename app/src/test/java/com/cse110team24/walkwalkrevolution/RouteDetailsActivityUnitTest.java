package com.cse110team24.walkwalkrevolution;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import org.junit.Before;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RouteDetailsActivityUnitTest {
    private Intent intent;
    private WalkStats stats;
    private Route expectedRoute;
    private Calendar date = new GregorianCalendar(2020, 2, 14);


    @Before
    public void setup() {
        stats = new WalkStats(1500, 1800000, 0.82, date);
        expectedRoute = new Route("Test Title")
                .setStartingLocation("")
                .setEnvironment(new RouteEnvironment())
                .setNotes("")
                .setStats(stats);
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intent.putExtra(RouteDetailsActivity.ROUTE_KEY, expectedRoute);
        intent.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);
    }
}
