package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.models.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RouteUnitTest {
    WalkStats stats;
    RouteEnvironment env;
    Route route;
    Calendar date = new GregorianCalendar(2020, 2,4 );

    @Before
    public void setUp() {
        route = new Route("Main Route");
        env = new RouteEnvironment();
        env.setRouteType(RouteEnvironment.RouteType.LOOP);
        env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        env.setTrailType(RouteEnvironment.TrailType.STREETS);
        env.setDifficulty(RouteEnvironment.Difficulty.HARD);

        stats = new WalkStats(1500, 1800, 1.8, date);
    }

    @Test
    public void testEqualAllInformationAndCompleted() {
        String start = "Gilman and Villa La Jolla";
        String notes = "In front of the parking structure";
        route.setEnvironment(env);
        route.setNotes(notes);
        route.setStartingLocation(start);
        route.setStats(stats);

        Route route2 = new Route("Main Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);
        route2.setStartingLocation(start);
        route2.setStats(stats);

        assertTrue(route.equals(route2));
    }

    @Test
    public void testEqualAllInformationAndNotCompleted() {
        String start = "Gilman and Villa La Jolla";
        String notes = "In front of the parking structure";
        route.setEnvironment(env);
        route.setNotes(notes);
        route.setStartingLocation(start);

        Route route2 = new Route("Main Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);
        route2.setStartingLocation(start);

        assertTrue(route.equals(route2));
    }

    @Test
    public void testEqualSomeInformationAndCompleted() {
        String notes = "In front of the parking structure";
        env.setSurfaceType(null);
        route.setEnvironment(env);
        route.setNotes(notes);
        route.setStats(stats);

        Route route2 = new Route("Main Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);
        route2.setStats(stats);

        assertTrue(route.equals(route2));
    }

    @Test
    public void testEqualSomeInformationAndNotCompleted() {
        String notes = "In front of the parking structure";
        env.setSurfaceType(null);
        route.setEnvironment(env);
        route.setNotes(notes);

        Route route2 = new Route("Main Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);

        assertTrue(route.equals(route2));
    }

    @Test
    public void testEqualNoInformationAndCompleted() {
        route.setStats(stats);
        Route route2 = new Route("Main Route");
        route2.setStats(stats);

        assertTrue(route.equals(route2));
    }

    @Test
    public void testEqualNoInformationAndNotCompleted() {
        Route route2 = new Route("Main Route");
        assertTrue(route.equals(route2));
    }

    @Test
    public void testNotEqualSameRouteDifferentStats() {
        String start = "Gilman and Villa La Jolla";
        String notes = "In front of the parking structure";
        route.setEnvironment(env);
        route.setNotes(notes);
        route.setStartingLocation(start);
        route.setStats(stats);

        Route route2 = new Route("Main Route");
        WalkStats stats2 = new WalkStats(2000, 600, 1.2, date);
        route2.setEnvironment(env);
        route2.setNotes(notes);
        route2.setStartingLocation(start);
        route2.setStats(stats2);

        assertFalse(route.equals(route2));
    }

    @Test
    public void testNotEqualDifferentRoutesBothIncomplete() {
        String start = "Gilman and Villa La Jolla";
        String notes = "In front of the parking structure";
        route.setEnvironment(env);
        route.setNotes(notes);
        route.setStartingLocation(start);

        Route route2 = new Route("Second Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);
        route2.setStartingLocation(start);

        assertFalse(route.equals(route2));
    }

    @Test
    public void testNotEqualCallingCompleteParamIncomplete() {
        String notes = "In front of the parking structure";
        env.setSurfaceType(null);
        route.setEnvironment(env);
        route.setNotes(notes);
        route.setStats(stats);

        Route route2 = new Route("Main Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);

        assertFalse(route.equals(route2));
    }

    @Test
    public void testNotEqualCallingIncompleteParamComplete() {
        String notes = "In front of the parking structure";
        env.setSurfaceType(null);
        route.setEnvironment(env);
        route.setNotes(notes);

        Route route2 = new Route("Main Route");
        route2.setEnvironment(env);
        route2.setNotes(notes);
        route2.setStats(stats);

        assertFalse(route.equals(route2));
    }

    @Test
    public void testNullParam() {
        Route route2 = null;
        assertFalse(route.equals(route2));
    }

    @Test
    public void testNotRouteInstance() {
        assertFalse(route.equals(date));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() throws IllegalArgumentException{
        Route nullName = new Route(null);
    }

    @Test
    public void testCompareToSameName() {
        Route route2 = new Route("Main Route");
        assertEquals(0, route.compareTo(route2));
    }

    @Test
    public void testCompareToCallingBeforeParam() {
        Route route2 = new Route("New Route");
        assertTrue(route.compareTo(route2) < 0);
    }

    @Test
    public void testCompareToParamBeforeCalling() {
        Route route2 = new Route("Breeze");
        assertTrue(route.compareTo(route2) > 0);
    }
}
