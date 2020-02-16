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

@RunWith(AndroidJUnit4.class)
public class RouteDetailsActivityUnitTest {

    RouteEnvironment env = new RouteEnvironment();

    private Intent intent;
    private Intent intentIncomplete;
    private WalkStats stats;
    private Route expectedRoute;
    private Route incompleteExpectedRoute;
    private Calendar date = new GregorianCalendar(2020, 2, 14);
    private TextView startLocationText;
    private TextView routeTypeText;
    private TextView terrainTypeText;
    private TextView surfaceTypeText;
    private TextView landTypeText;
    private TextView difficultyText;
    private TextView detailsRecentStepsText;
    private TextView detailsRecentDistanceText;
    private TextView detailsRcentTimeElapsedText;
    private TextView notesText;
    private TextView neverWalkedBeforeText;
    private Button startWalkBtn;



    @Before
    public void setup() {

        env.setRouteType(RouteEnvironment.RouteType.LOOP);
        env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        env.setTrailType(RouteEnvironment.TrailType.TRAIL);
        env.setDifficulty(RouteEnvironment.Difficulty.MODERATE);

        stats = new WalkStats(1500, 1800000, 0.82, date);
        expectedRoute = new Route("Route has all the info")
                .setStartingLocation("")
                .setEnvironment(env)
                .setNotes("This team is AWESOME!")
                .setStats(stats);

        incompleteExpectedRoute = new Route("Route has no Info");

        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intent.putExtra(RouteDetailsActivity.ROUTE_KEY, expectedRoute);
        intent.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);

        intentIncomplete = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intentIncomplete.putExtra(RouteDetailsActivity.ROUTE_KEY, incompleteExpectedRoute);
        intentIncomplete.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);

    }

    @Test
    public void testInfoIsTheSameAsStored() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(expectedRoute.getStats().getSteps(),  Long.parseLong(detailsRecentStepsText.getText().toString()), 0.1);
            assertEquals(expectedRoute.getStats().getDistance(), Double.parseDouble((detailsRecentDistanceText.getText().toString().replace(" mile(s)", ""))), 0.1);
            assertEquals((double)(expectedRoute.getStats().getTimeElapsed()/60000), Double.parseDouble(detailsRcentTimeElapsedText.getText().toString().replace(" min.", "")), 0.1);
        });
    }
    @Test
    public void testNoteIsSet() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(expectedRoute.getNotes(), notesText.getText().toString());

        });
    }
    @Test
    public void testNoteIsNotSet() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentIncomplete);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(notesText.getVisibility(), View.GONE);
        });
    }
    @Test
    public void testEnvironmentVariablesAreSet() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(expectedRoute.getEnvironment().getRouteType().toString().toLowerCase(),
                    routeTypeText.getText().toString().toLowerCase());
            assertEquals(expectedRoute.getEnvironment().getTerrainType().toString().toLowerCase(),
                    terrainTypeText.getText().toString().toLowerCase());
            assertEquals(expectedRoute.getEnvironment().getSurfaceType().toString().toLowerCase(),
                    surfaceTypeText.getText().toString().toLowerCase());
            assertEquals(expectedRoute.getEnvironment().getTrailType().toString().toLowerCase(),
                    landTypeText.getText().toString().toLowerCase());
            assertEquals(expectedRoute.getEnvironment().getDifficulty().toString().toLowerCase(),
                    difficultyText.getText().toString().toLowerCase());
        });
    }



    private void getUIFields(RouteDetailsActivity activity) {

        startLocationText = activity.findViewById(R.id.tv_starting_loc);
        routeTypeText = activity.findViewById(R.id.tv_rte_type);
        terrainTypeText = activity.findViewById(R.id.tv_terr_type);
        surfaceTypeText = activity.findViewById(R.id.tv_srfce_type);
        landTypeText = activity.findViewById(R.id.tv_lnd_type);
        difficultyText = activity.findViewById(R.id.tv_diff);
        detailsRecentStepsText = activity.findViewById(R.id.tv_details_recent_steps);
        detailsRecentDistanceText = activity.findViewById(R.id.tv_details_recent_distance);
        detailsRcentTimeElapsedText = activity.findViewById(R.id.tv_details_recent_time_elapsed);
        neverWalkedBeforeText = activity.findViewById(R.id.tv_details_never_walked);
        notesText = activity.findViewById(R.id.tv_notes);
        startWalkBtn = activity.findViewById(R.id.btn_details_start_walk);


    }

}
