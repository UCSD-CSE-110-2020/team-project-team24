package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.models.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RouteEnvironmentUnitTest {
    RouteEnvironment env = new RouteEnvironment();

    @Before
    public void setUp() {
        env.setRouteType(RouteEnvironment.RouteType.LOOP);
        env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        env.setTrailType(RouteEnvironment.TrailType.TRAIL);
        env.setDifficulty(RouteEnvironment.Difficulty.MODERATE);
    }

    @Test
    public void testEqualsObjectsEqual() {
        RouteEnvironment env2 = new RouteEnvironment();
        env2.setRouteType(RouteEnvironment.RouteType.LOOP);
        env2.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        env2.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        env2.setTrailType(RouteEnvironment.TrailType.TRAIL);
        env2.setDifficulty(RouteEnvironment.Difficulty.MODERATE);
        assertTrue(env.equals(env2));
    }

    @Test
    public void testEqualsDifferByOne() {
        RouteEnvironment env2 = new RouteEnvironment();
        env2.setRouteType(RouteEnvironment.RouteType.LOOP);
        env2.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        env2.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        env2.setTrailType(RouteEnvironment.TrailType.TRAIL);
        env2.setDifficulty(RouteEnvironment.Difficulty.EASY);
        assertFalse(env.equals(env2));
    }

    @Test
    public void testEqualsAllDifferent() {
        RouteEnvironment env2 = new RouteEnvironment();
        env2.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
        env2.setTerrainType(RouteEnvironment.TerrainType.FLAT);
        env2.setSurfaceType(RouteEnvironment.SurfaceType.UNEVEN);
        env2.setTrailType(RouteEnvironment.TrailType.STREETS);
        env2.setDifficulty(RouteEnvironment.Difficulty.HARD);
        assertFalse(env.equals(env2));
    }

    @Test
    public void testEqualsNullParam() {
        RouteEnvironment env2 = null;
        assertFalse(env.equals(env2));
    }

    @Test
    public void testEqualsNotRouteEnvironment() {
        Route notEnv = new Route("Not an environment");
        assertFalse(env.equals(notEnv));
    }

    @Test
    public void testSomeFieldsNull() {
        RouteEnvironment env3 = new RouteEnvironment();
        env3.setDifficulty(RouteEnvironment.Difficulty.MODERATE);
        env3.setTrailType(RouteEnvironment.TrailType.TRAIL);

        env.setSurfaceType(null);
        env.setTerrainType(null);
        env.setRouteType(null);

        assertTrue(env.equals(env3));
    }
}
