package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class RouteDetailsActivityUnitTest {

    private Intent intent;
    private WalkStats stats;
    private Route expectedRoute;
    private Calendar date = new GregorianCalendar(2020, 2, 14);
    Button startWalkBtn;
    TextView stepsField;
    TextView distanceField;
    TextView timeElapsedField;


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

    @Test
    public void testInfoIsTheSameAsStored() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(expectedRoute.getStats().getSteps(), Integer.parseInt(stepsField.getText().toString()));
            assertEquals(expectedRoute.getStats().getDistance(), Integer.parseInt(distanceField.getText().toString()));
            assertEquals(expectedRoute.getStats().getTimeElapsed(), Integer.parseInt(timeElapsedField.getText().toString()));
        });
    }
    private void getUIFields(RouteDetailsActivity activity) {

        startWalkBtn = activity.findViewById(R.id.btn_details_start_walk);
        stepsField = activity.findViewById(R.id.tv_details_recent_steps);
        distanceField = activity.findViewById(R.id.tv_details_recent_distance);
        timeElapsedField = activity.findViewById(R.id.tv_details_recent_time_elapsed);
    }
    /*
    @Test
    public void testInfoIsNotTheSameAsStore() {
        assertNotEquals(expectedRoute.getStats().getDistance(), 1.02);
    }

    @Test
    public void testSwitchToHomeScreenAfterStartWalk() {
        Button switchScreenBt = activity.findViewById(R.id.btn_details_start_walk);
        Button homeScreenStartBtn = activity.findViewById(R.id.btn_start_walk);
        Button homeScreenStopBtn = activity.findViewById(R.id.btn_stop_walk);
       Intent endWalkIntent = new Intent(ApplicationProvider.getApplicationContext(), HomeActivity.class);

        switchScreenBt.performClick();
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(endWalkIntent);
        scenario.onActivity(activity -> {
            assertEquals(homeScreenStartBtn.getVisibility(), View.INVISIBLE);
            assertEquals(homeScreenStopBtn.getVisibility(), View.VISIBLE);
        });

    }

  */
}
