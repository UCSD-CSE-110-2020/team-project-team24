package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoogleFitAdapterUnitTest {
    private static double EQ_DELTA = 0.1;
    GoogleFitAdapter adapter;

    @Before
    public void setup() {
        adapter = new GoogleFitAdapter(null);
    }

    @Test
    public void testGetDistanceFromHeightMile() {
        double expectedMiles = 1.0;
        double actualMiles = adapter.getDistanceFromHeight(2435, 5, 3);
        assertEquals(expectedMiles, actualMiles, EQ_DELTA);
    }

    @Test
    public void testGetDistanceFromHeightLessThanMile() {
        double expectedMiles = 0.82;
        double actualMiles = adapter.getDistanceFromHeight(2000, 5, 3);
        assertEquals(expectedMiles, actualMiles, EQ_DELTA);
    }

    @Test
    public void testGetDistanceFromHeightMoreThanMile() {
        double expectedMiles = 2.39;
        double actualMiles = adapter.getDistanceFromHeight(5842, 5, 3);
        assertEquals(expectedMiles, actualMiles, EQ_DELTA);
    }

    @Test
    public void testGetDistanceFromHeightZeroCase() {
        double expectedMiles = 0.0;
        double actualMiles = adapter.getDistanceFromHeight(0, 5, 3);
        assertEquals(expectedMiles, actualMiles, EQ_DELTA);
    }
}
