package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.models.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WalkStatsUnitTest {
    WalkStats stats;

    @Before
    public void setUp() {
        Calendar dateCompleted = new GregorianCalendar(2020, 2, 3);
        stats = new WalkStats(1500, 1800000, 1.8, dateCompleted);
    }

    @Test
    public void testConstructorRegularUse() {
        Calendar dateCompleted = new GregorianCalendar(2020, 2, 3);
        WalkStats stat = new WalkStats(1500, 1800000, 1.8, dateCompleted);
        assertEquals(1500, stat.getSteps());
        assertEquals(1800000, stat.getTimeElapsed());
        assertEquals(1.8, stat.getDistance(), 0.001);
        assertEquals(dateCompleted, stat.getDateCompleted());
    }

    @Test
    public void testEqualsObjectsEqual() {
        Calendar date = new GregorianCalendar(2020, 2, 3);
        WalkStats secondStats = new WalkStats(1500, 1800000, 1.8, date);
        assertTrue(stats.equals(secondStats));
    }

    @Test
    public void testEqualsDifferByOne() {
        Calendar date = new GregorianCalendar(2020, 2, 4);
        WalkStats secondStats = new WalkStats(1500, 1800000, 1.8, date);
        assertFalse(stats.equals(secondStats));
    }

    @Test
    public void testEqualsObjectsCompletelyDifferent() {
        Calendar date = new GregorianCalendar(2020, 2, 8);
        WalkStats secondStats = new WalkStats(234, 345, 4.5, date);
        assertFalse(stats.equals(secondStats));
    }

    @Test
    public void testEqualsNullParam() {
        WalkStats nullStats = null;
        assertFalse(stats.equals(nullStats));
    }

    @Test
    public void testEqualsParamNotWalkStatsInstance() {
        Route route = new Route("test");
        assertFalse(stats.equals(route));
    }
}
