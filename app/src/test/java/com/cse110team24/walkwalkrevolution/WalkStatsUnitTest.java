package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.models.*;

import java.text.DecimalFormat;
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

    @Test
    public void testTimeElapsedInMinuteSingle() {
        stats.setTimeElapsed(60_000);
        assertEquals(1.0, stats.timeElapsedInMinutes(), 0.1);
    }

    @Test
    public void testTimeElapsedInMinutesMultiple() {
        stats.setTimeElapsed(90_000);
        assertEquals(1.5, stats.timeElapsedInMinutes(), 0.1);
    }

    @Test
    public void testTimeElapsedIinMinuteZero() {
        stats.setTimeElapsed(0);
        assertEquals(0.0, stats.timeElapsedInMinutes(), 0.1);
    }

    @Test
    public void testFormattedDistanceNotZero() {
        assertEquals("1.80 mile(s)", stats.formattedDistance());
    }
    @Test
    public void testFormattedDistanceZero() {
        Calendar dateCompleted = new GregorianCalendar(2020, 2, 3);
        WalkStats tests2 = new WalkStats(0, 1800000, 0, dateCompleted);
        assertEquals("0.00 mile(s)", tests2.formattedDistance());
    }
    @Test
    public void testFormattedTimeNotZero() {
       // float timeToMin =  Math.round(1800000/1000.0/60);
        double timeToMin =  (1800000/60000.0);
        assertEquals( String.format("%.2f", timeToMin) +" min.", stats.formattedTime());
    }

    @Test
    public void testToString() {
        double timeToMin =  (1800000/60000.0);
        String result = stats.toString();
        String cutOff = result.substring(0,result.indexOf("00:00:00")+8);
        assertEquals( "\ndistance: 1.80 mile(s)" +
                "\ntime: " + String.format("%.2f", timeToMin) +" min."
                +"\ndate completed: Tue Mar 03 00:00:00",cutOff);

    }
}
