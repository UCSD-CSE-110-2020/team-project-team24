package com.cse110team24.walkwalkrevolution;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class RoutesManagerUnitTest {
    private static final String TEST_FILE_LIST = ".WWR_storage_test_list";
    private static final String TEST_FILE_SINGLE = ".WWR_storage_test_single";
    private static final String TEST_FILE_LATEST = ".WWR_storage_test_latest";

    @Rule
    public ActivityScenarioRule<LoginActivity> scenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    private ActivityScenario<LoginActivity> scenario;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    @Test
    public void testRouteTitleOnlyList() {
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            List<Route> routes = new ArrayList<>();
            String title = "Test title";
            routes.add(new Route(title));
            try {
                RoutesManager.writeList(routes, TEST_FILE_LIST, context);
            } catch (IOException e) {
                fail();
            }

            try {
                List<Route> readRoutes = RoutesManager.readList(TEST_FILE_LIST, context);
                assertEquals(new Route(title), readRoutes.get(0));
            } catch (IOException e) {
                fail();
            }
        });
    }

    @Test
    public void testAllFieldsList() {
        scenario.onActivity(activity -> {
            Route route = getRoute();
            List<Route> routes = new ArrayList<>();

            Context context = activity.getApplicationContext();
            routes.add(route);
            try {
                RoutesManager.writeList(routes, TEST_FILE_LIST, context);
            } catch (IOException e) {
                fail();
            }

            try {
                List<Route> readRoutes = RoutesManager.readList(TEST_FILE_LIST, context);
                assertEquals(route, readRoutes.get(0));
            } catch (IOException e) {
                fail();
            }
        });
    }

    @Test
    public void testRouteTitleOnlySigle() {
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            String title = "Test title";
            Route route = new Route(title);
            try {
                RoutesManager.writeSingle(route, TEST_FILE_SINGLE, context);
            } catch (IOException e) {
                fail();
            }

            try {
                Route readRoute = RoutesManager.readSingle(TEST_FILE_SINGLE, context);
                assertEquals(new Route(title), readRoute);
            } catch (IOException e) {
                fail();
            }
        });
    }

    @Test
    public void testAllFieldsSingle() {
        scenario.onActivity(activity -> {
            Route route = getRoute();
            Context context = activity.getApplicationContext();
            try {
                RoutesManager.writeSingle(route, TEST_FILE_SINGLE, context);
            } catch (IOException e) {
                fail();
            }

            try {
                Route readRoute = RoutesManager.readSingle(TEST_FILE_SINGLE, context);
                assertEquals(route, readRoute);
            } catch (IOException e) {
                fail();
            }
        });
    }

    @Test
    public void testWriteLatestNullStats() {
        scenario.onActivity(activity -> {
            Route route = new Route("");
            try {
                RoutesManager.writeLatest(route, TEST_FILE_LATEST, activity.getApplicationContext());
                fail();
            } catch (IOException e) {
                fail();
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
        });
    }
    @Test
    public void testLatest() {
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            Route route = getRoute();
            try {
                RoutesManager.writeLatest(route, TEST_FILE_LATEST, context);
            } catch (Exception e) {
                fail();
            }

            try {
                assertEquals(route, RoutesManager.readLatest(TEST_FILE_LATEST, context));
            } catch (Exception e) {
                fail();
            }
        });
    }

    @Test
    public void testLatestAlreadyWritten() {
        scenario.onActivity(activity -> {
            Context context = activity.getApplicationContext();
            Route route = getRoute();
            try {
                RoutesManager.writeLatest(route, TEST_FILE_LATEST, context);
            } catch (Exception e) {
                fail();
            }

            route.getStats().setDistance(0.5);
            try {
                RoutesManager.writeLatest(route, TEST_FILE_LATEST, context);
            } catch (Exception e) {
                fail();
            }

            try {
                assertEquals(route, RoutesManager.readLatest(TEST_FILE_LATEST, context));
            } catch (Exception e) {
                fail();
            }

        });
    }

    private Route getRoute() {
        RouteEnvironment env = getEnvironment();
        WalkStats stats = new WalkStats(500, 100_000, 1.2, new GregorianCalendar());
        return new Route("title")
                .setStartingLocation("Test World")
                .setStats(stats)
                .setEnvironment(env)
                .setFavorite(true)
                .setNotes("Testing reading a route");
    }

    private RouteEnvironment getEnvironment() {
        RouteEnvironment environment = new RouteEnvironment();
        environment.setDifficulty(RouteEnvironment.Difficulty.HARD);
        environment.setRouteType(RouteEnvironment.RouteType.LOOP);
        environment.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        environment.setTerrainType(RouteEnvironment.TerrainType.FLAT);
        environment.setTrailType(RouteEnvironment.TrailType.TRAIL);
        return environment;
    }
}
