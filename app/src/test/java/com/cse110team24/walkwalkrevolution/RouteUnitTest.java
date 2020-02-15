package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.models.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RouteUnitTest {
    private WalkStats stats;
    private RouteEnvironment env;
    private Route route;
    private Calendar date = new GregorianCalendar(2020, 2,4 );

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

        assertEquals(route, route2);
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

        assertEquals(route, route2);
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

        assertEquals(route, route2);
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

        assertEquals(route, route2);
    }

    @Test
    public void testEqualNoInformationAndCompleted() {
        route.setStats(stats);
        Route route2 = new Route("Main Route");
        route2.setStats(stats);

        assertEquals(route, route2);
    }

    @Test
    public void testEqualNoInformationAndNotCompleted() {
        Route route2 = new Route("Main Route");
        assertEquals(route, route2);
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

        assertNotEquals(route, route2);
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

        assertNotEquals(route, route2);
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

        assertNotEquals(route, route2);
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

        assertNotEquals(route, route2);
    }

    @Test
    public void testNullParam() {
        Route route2 = null;
        assertNotEquals(route, route2);
    }

    @Test
    public void testNotRouteInstance() {
        assertNotEquals(route, date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() throws IllegalArgumentException{
        new Route(null);
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

    @Test
    public void testToStringNotEqualTitle() {
        Route route2 = new Route("Breeze");
        assertNotEquals(route.toString(), route2.toString());
    }
    @Test
    public void testToStringEqualTitle() {

        Route route2 = new Route("Main Route");
        assertEquals(route.toString(), route2.toString());
    }
    @Test
    public void testToStringEqualTitleDifferentStats() {
         WalkStats statsTester;
         statsTester = new WalkStats(1500,10000,1.20, date);
        WalkStats stats3 = new WalkStats(1000,5000,3.29, date);
        Route route2 = new Route("Main Route");
        route2.setStats(statsTester);
        route.setStats(stats3);
        assertNotEquals(route.toString(), route2.toString());


    }
}
