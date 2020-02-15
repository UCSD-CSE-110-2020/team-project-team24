package com.cse110team24.walkwalkrevolution.models;

import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class WalkStats implements Serializable {
    private long steps;
    private long timeElapsed;
    private double distance;
    private Calendar dateCompleted;

    public WalkStats(long steps, long timeElapsed, double distance, Calendar dateCompleted) {
        this.steps = steps;
        this.timeElapsed = timeElapsed;
        this.distance = distance;
        this.dateCompleted = dateCompleted;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Calendar getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Calendar dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public double timeElapsedInMinutes() {
        return timeElapsed / 1000.0 / 60;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WalkStats) {
            WalkStats stats = (WalkStats) o;
            return steps == stats.steps && timeElapsed == stats.timeElapsed &&
                    distance == stats.distance && Objects.equals(dateCompleted, stats.dateCompleted);
        }

        return false;
    }

    // TODO: 2020-02-14 write tests for this
    @Override
    public String toString() {
        return "\ndistance: " + formattedDistance() +
                "\ntime: " + formattedTime() +
                "\ndate completed: " + dateCompleted.getTime();
    }

    // TODO: 2020-02-14 test
    public String formattedDistance() {
        return format(distance,"mile(s)");
    }

    // TODO: 2020-02-14 test
    public String formattedTime() {
        return format(timeElapsedInMinutes(), "min.");
    }

    private String format(double val, String suffix) {
        NumberFormat format = new DecimalFormat("#0.00");
        return String.format("%s %s", format.format(val), suffix);
    }
}
