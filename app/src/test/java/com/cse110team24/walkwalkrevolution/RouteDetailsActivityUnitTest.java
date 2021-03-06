package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.RouteBuilder;
import com.cse110team24.walkwalkrevolution.models.route.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RouteDetailsActivityUnitTest {

    RouteEnvironment env = new RouteEnvironment();
    RouteEnvironment semiEnv = new RouteEnvironment();

    private Intent intent;
    private Intent intentIncomplete;
    private Intent intentSemiComplete;
    private Intent intentTeammate;
    private WalkStats stats;
    private Route expectedRoute;
    private Route incompleteExpectedRoute;
    private Route semiCompleteExpectedRoute;
    private Route teammateRoute;
    private Calendar date = new GregorianCalendar(2020, 2, 14);
    private TextView startLocationText;
    private TextView routeTypeText;
    private TextView terrainTypeText;
    private TextView surfaceTypeText;
    private TextView landTypeText;
    private TextView difficultyText;
    private TextView mostRecentWalkPrompt;
    private TextView detailsRecentStepsText;
    private TextView detailsRecentDistanceText;
    private TextView detailsRcentTimeElapsedText;
    private TextView notesText;
    private TextView neverWalkedBeforeText;
    private Button startWalkBtn;



    @Before
    public void setup() {
        setEnvironment();
        setPartialEnv();
        stats = new WalkStats(1500, 1800000, 0.82, date);
        makeExpectedRoute();
        incompleteExpectedRoute = new Route("Route has no Info").setRouteUid("NO INFO");
        makePartialRoute();
        makeTeammateRoute();
        createCompleteIntent();
        createIncompleteIntent();
        createSemiCompleteIntent();
        createTeammateRouteIntent();
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
    public void testStatsNeverRunBefore() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentIncomplete);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(detailsRecentStepsText.getVisibility(), View.GONE);
            assertEquals(detailsRecentDistanceText.getVisibility(), View.GONE);
            assertEquals(detailsRcentTimeElapsedText.getVisibility(), View.GONE);
            assertEquals(neverWalkedBeforeText.getVisibility(), View.VISIBLE);
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
    @Test
    public void testEnvironmentVariablesNotSet() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentIncomplete);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(routeTypeText.getVisibility(), View.GONE);
            assertEquals(terrainTypeText.getVisibility(), View.GONE);
            assertEquals(surfaceTypeText.getVisibility(), View.GONE);
            assertEquals(landTypeText.getVisibility(), View.GONE);
            assertEquals(difficultyText.getVisibility(), View.GONE);
        });
    }

    @Test
    public void testEnvironmentVariablesSemiCompleted() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentSemiComplete);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(routeTypeText.getVisibility(), View.GONE);
            assertEquals(terrainTypeText.getVisibility(), View.GONE);
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
        mostRecentWalkPrompt = activity.findViewById(R.id.tv_details_recent_walk_prompt);
        detailsRecentStepsText = activity.findViewById(R.id.tv_details_recent_steps);
        detailsRecentDistanceText = activity.findViewById(R.id.tv_details_recent_distance);
        detailsRcentTimeElapsedText = activity.findViewById(R.id.tv_details_recent_time_elapsed);
        neverWalkedBeforeText = activity.findViewById(R.id.tv_details_never_walked);
        notesText = activity.findViewById(R.id.tv_notes);
        startWalkBtn = activity.findViewById(R.id.btn_details_start_walk);
    }

    @Test
    public void testStartingLocationSet() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(expectedRoute.getStartingLocation().toString(), startLocationText.getText().toString());

        });
    }
    @Test
    public void testStartingLocationNotSet() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentIncomplete);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(startLocationText.getVisibility(), View.GONE);
        });
    }

    @Test
    public void testTeamRoutes_verifyStatsSubstitution_TeammateGoneOnWalk() {
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentTeammate);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(neverWalkedBeforeText.getVisibility(), View.VISIBLE);
            assertEquals(mostRecentWalkPrompt.getText().toString(), "Teammate's Most Recent Walk");
        });
    }

    @Test
    public void testTeamRoutes_verifyStatsSubstitution_TeammateNotGoneOnWalk() {
        teammateRoute.setStats(null);
        ActivityScenario<RouteDetailsActivity> scenario = ActivityScenario.launch(intentTeammate);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(neverWalkedBeforeText.getVisibility(), View.VISIBLE);
            assertEquals(mostRecentWalkPrompt.getVisibility(), View.GONE);
        });
    }

    private void setEnvironment() {
        env.setRouteType(RouteEnvironment.RouteType.LOOP);
        env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        env.setTrailType(RouteEnvironment.TrailType.TRAIL);
        env.setDifficulty(RouteEnvironment.Difficulty.MODERATE);
    }

    private void setPartialEnv() {
        semiEnv.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        semiEnv.setTrailType(RouteEnvironment.TrailType.TRAIL);
        semiEnv.setDifficulty(RouteEnvironment.Difficulty.MODERATE);
    }

    private void makeExpectedRoute() {
        expectedRoute = new Route("Route has all the info")
                .setRouteUid("ALL INFO")
                .setStartingLocation("THIS IS AT UCSD")
                .setEnvironment(env)
                .setNotes("This team is AWESOME!")
                .setStats(stats);
    }

    private void makePartialRoute() {
        semiCompleteExpectedRoute = new Route("This route has some info")
                .setRouteUid("SOME INFO")
                .setStartingLocation(" This is idk where")
                .setEnvironment(semiEnv)
                .setStats(stats);
    }

    private void makeTeammateRoute() {
        teammateRoute = new Route("Teammate Route")
                .setRouteUid("teamRouteUid")
                .setStartingLocation("here")
                .setEnvironment(env)
                .setStats(stats)
                .setCreatorDisplayName("Steve");
    }

    private void createCompleteIntent() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intent.putExtra(RouteDetailsActivity.ROUTE_KEY, expectedRoute);
        intent.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);
    }

    private void createIncompleteIntent() {
        intentIncomplete = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intentIncomplete.putExtra(RouteDetailsActivity.ROUTE_KEY, incompleteExpectedRoute);
        intentIncomplete.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);
    }

    private void createSemiCompleteIntent() {
        intentSemiComplete = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intentSemiComplete.putExtra(RouteDetailsActivity.ROUTE_KEY, semiCompleteExpectedRoute);
        intentSemiComplete.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);
    }

    private void createTeammateRouteIntent() {
        intentTeammate = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class);
        intentTeammate.putExtra(RouteDetailsActivity.ROUTE_KEY, teammateRoute);
        intentTeammate.putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, 0);
    }
}
