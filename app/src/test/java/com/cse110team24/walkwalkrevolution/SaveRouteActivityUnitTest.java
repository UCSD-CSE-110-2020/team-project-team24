package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SaveRouteActivityUnitTest {
    private Intent intent;
    private WalkStats stats;
    private Route expectedRoute;
    private Calendar date = new GregorianCalendar(2020, 2, 14);

    private EditText editTextTitle;
    private EditText editTextLocation;
    private EditText editTextNotes;
    private RadioGroup routeTypeRdGroup;
    private RadioGroup terrainTypeRdGroup;
    private RadioGroup surfaceTypeRdGroup;
    private RadioGroup landTypeRdGroup;
    private RadioGroup difficultyTypeRdGroup;
    private Button doneButton;

    @Before
    public void setup() {
        stats = new WalkStats(1500, 1800000, 0.82, date);
        intent = new Intent(ApplicationProvider.getApplicationContext(), SaveRouteActivity.class)
                .putExtra(SaveRouteActivity.WALK_STATS_KEY, stats);
        expectedRoute = new Route("Test Title")
                .setStartingLocation("")
                .setEnvironment(new RouteEnvironment())
                .setNotes("")
                .setStats(stats);
    }

    @Test
    public void testNoTitleEntered() {
        ActivityScenario<SaveRouteActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            doneButton.performClick();
            List<Route> routes = null;
            try {
                routes = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, activity.getApplicationContext());
            }
            catch (IOException e) { e.printStackTrace(); }
            assertTrue(routes.isEmpty());
            assertEquals(ShadowToast.getTextOfLatestToast(), "Title is required");
        });
    }

    @Test
    public void testJustTitleEntered() {
        ActivityScenario<SaveRouteActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            editTextTitle.setText("Test Title");
            doneButton.performClick();
            Route actualRoute = null;
            try {
                actualRoute = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, activity.getApplicationContext())
                        .get(0);
            }
            catch (IOException e) { e.printStackTrace(); }
            assertEquals(expectedRoute, actualRoute);
        });
    }

    @Test
    public void testAllFieldsEntered() {
        ActivityScenario<SaveRouteActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);

            editTextTitle.setText("Test Title");
            editTextLocation.setText("Starting Location");
            editTextNotes.setText("Some Notes");
            routeTypeRdGroup.check(R.id.radio_btn_out_back);
            terrainTypeRdGroup.check(R.id.rd_btn_hilly);
            surfaceTypeRdGroup.check(R.id.rd_btn_uneven);
            landTypeRdGroup.check(R.id.rd_btn_trail);
            difficultyTypeRdGroup.check(R.id.rd_btn_moderate);

            doneButton.performClick();
            Route actualRoute = null;
            try {
                actualRoute = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, activity.getApplicationContext())
                        .get(0);
            }
            catch (IOException e) { e.printStackTrace(); }
            RouteEnvironment env = setRouteEnv(new RouteEnvironment(), RouteEnvironment.RouteType.OUT_AND_BACK,
                    RouteEnvironment.TerrainType.HILLY, RouteEnvironment.SurfaceType.UNEVEN,
                    RouteEnvironment.TrailType.TRAIL, RouteEnvironment.Difficulty.MODERATE);
            expectedRoute.setStartingLocation("Starting Location")
                    .setNotes("Some Notes")
                    .setEnvironment(env);
            assertEquals(expectedRoute, actualRoute);
        });
    }

    // Save To Empty List already tested in tests above

    @Test
    public void testSaveToPopulatedList() {
        ActivityScenario<SaveRouteActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);

            List<Route> routes = new ArrayList<Route>();
            routes.add(new Route("Before"));
            try {
                RoutesManager.writeList(routes, RoutesActivity.LIST_SAVE_FILE,
                        activity.getApplicationContext());
            }
            catch (IOException e) { e.printStackTrace(); }

            editTextTitle.setText("Test Title");
            doneButton.performClick();
            Route actualRoute = null;
            try {
                actualRoute = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, activity.getApplicationContext())
                        .get(1);
            }
            catch (IOException e) { e.printStackTrace(); }
            assertEquals(expectedRoute, actualRoute);
        });
    }

    private void getUIFields(SaveRouteActivity activity) {
        editTextTitle = activity.findViewById(R.id.et_save_route_title);
        editTextLocation = activity.findViewById(R.id.et_save_route_location);
        editTextNotes = activity.findViewById(R.id.et_route_notes);
        routeTypeRdGroup = activity.findViewById(R.id.radiogroup_route_type);
        terrainTypeRdGroup = activity.findViewById(R.id.rd_group_terrain_type);
        surfaceTypeRdGroup = activity.findViewById(R.id.rd_group_surface_type);
        landTypeRdGroup = activity.findViewById(R.id.rd_group_land_type);
        difficultyTypeRdGroup = activity.findViewById(R.id.rd_group_difficulty);
        doneButton = activity.findViewById(R.id.btn_save_route);
    }

    private RouteEnvironment setRouteEnv(RouteEnvironment env, RouteEnvironment.RouteType rt,
                                RouteEnvironment.TerrainType tet, RouteEnvironment.SurfaceType st,
                                RouteEnvironment.TrailType trt, RouteEnvironment.Difficulty d) {
        env.setRouteType(rt);
        env.setTerrainType(tet);
        env.setSurfaceType(st);
        env.setTrailType(trt);
        env.setDifficulty(d);

        return env;
    }
}
