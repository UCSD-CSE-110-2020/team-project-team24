package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.util.Log;
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

import static com.cse110team24.walkwalkrevolution.RoutesActivity.TAG;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SaveRouteActivityUnitTest {
    private Intent intent;
    private WalkStats stats;
    private Route expectedRoute;
    private Route expectedFabRoute;
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), SaveRouteActivity.class);
        expectedRoute = new Route("Test Title")
                .setStartingLocation("")
                .setEnvironment(new RouteEnvironment())
                .setNotes("")
                .setStats(stats);
        expectedFabRoute = new Route("some route")
                .setNotes("")
                .setStartingLocation("")
                .setEnvironment(new RouteEnvironment());
    }

    @Test
    public void testNoTitleEntered() {
        intent.putExtra(SaveRouteActivity.WALK_STATS_KEY, stats);
        ActivityScenario<SaveRouteActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            doneButton.performClick();
            assertEquals(ShadowToast.getTextOfLatestToast(), "Title is required");
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
