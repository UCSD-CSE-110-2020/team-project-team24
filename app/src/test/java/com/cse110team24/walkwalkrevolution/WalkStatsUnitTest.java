package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.models.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WalkStatsUnitTest {
    private WalkStats stats;

    @Before
    public void setUp() {
        Calendar dateCompleted = new GregorianCalendar(2020, 2, 3);
        stats = new WalkStats(1500, 1800000, 1.8, dateCompleted);
    }

    @Test
    public void testConstructorRegularUse() {
        Calendar dateCompleted = new GregorianCalendar(2020, 2, 3);
        assertEquals(1500, stats.getSteps());
        assertEquals(1800000, stats.getTimeElapsed());
        assertEquals(1.8, stats.getDistance(), 0.001);
        assertEquals(dateCompleted, stats.getDateCompleted());
    }

    @Test
    public void testEqualsObjectsEqual() {
        Calendar date = new GregorianCalendar(2020, 2, 3);
        WalkStats secondStats = new WalkStats(1500, 1800000, 1.8, date);
        assertEquals(stats, secondStats);
    }

    @Test
    public void testEqualsDifferByOne() {
        Calendar date = new GregorianCalendar(2020, 2, 4);
        WalkStats secondStats = new WalkStats(1500, 1800000, 1.8, date);
        assertNotEquals(stats, secondStats);
    }

    @Test
    public void testEqualsObjectsCompletelyDifferent() {
        Calendar date = new GregorianCalendar(2020, 2, 8);
        WalkStats secondStats = new WalkStats(234, 345, 4.5, date);
        assertNotEquals(stats, secondStats);
    }

    @Test
    public void testEqualsNullParam() {
        WalkStats nullStats = null;
        assertNotEquals(stats, nullStats);
    }

    @Test
    public void testEqualsParamNotWalkStatsInstance() {
        Route route = new Route("test");
        assertNotEquals(stats, route);
    }
}
