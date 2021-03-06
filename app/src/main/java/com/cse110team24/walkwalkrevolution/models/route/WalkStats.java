package com.cse110team24.walkwalkrevolution.models.route;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.text.NumberFormat;
import java.text.DecimalFormat;

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

    private WalkStats() {
        dateCompleted = Calendar.getInstance();
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

    @Override
    public String toString() {
        return "\ndistance: " + formattedDistance() +
                "\ntime: " + formattedTime() +
                "\ndate completed: " + dateCompleted.getTime();
    }


    public String formattedDistance() {
        return format(distance,"mile(s)");
    }


    public String formattedTime() {
        return format(timeElapsedInMinutes(), "min.");
    }

    public String formattedDate() {
        Date date = dateCompleted.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.US);
        return sdf.format(date);
    }

    private String format(double val, String suffix) {
        NumberFormat format = new DecimalFormat("#0.00");
        return String.format("%s %s", format.format(val), suffix);
    }

    public Map<String, Object> statsData() {
        Map<String, Object> data = new HashMap<>();
        data.put("steps", steps);
        data.put("elapsedTimeMillis", timeElapsed);
        data.put("distance", distance);
        data.put("date", dateCompleted.getTime());
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        WalkStats mWalkStats = new WalkStats();

        public Builder addDateCompleted(Calendar calendar) {
            mWalkStats.setDateCompleted(calendar);
            return this;
        }

        public Builder addDistance(double distance) {
            mWalkStats.setDistance(distance);
            return this;
        }

        public Builder addTimeElapsed(long timeElapsed) {
            mWalkStats.setTimeElapsed(timeElapsed);
            return this;
        }

        public Builder addSteps(long steps) {
            mWalkStats.setSteps(steps);
            return this;
        }

        public WalkStats build() {
            return mWalkStats;
        }
    }

}
