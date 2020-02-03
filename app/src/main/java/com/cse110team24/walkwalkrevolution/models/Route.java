package com.cse110team24.walkwalkrevolution.models;

import java.io.Serializable;
import java.lang.Comparable;


public class Route implements Serializable, Comparable<Route> {
    private String title;

    private WalkStats stats;

    // OPTIONAL fields
    private String startingLocation;
    private RouteEnvironment environment;
    private boolean isFavorite;
    private String notes;


    public Route(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Route setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public Route setStartingLocation(String location) {
        startingLocation = location;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public Route setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Route setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public WalkStats getStats() {
        return stats;
    }

    public Route setStats(WalkStats stats) {
        this.stats = stats;
        return this;
    }

    public RouteEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public int compareTo(Route o) {
        return title.compareTo(o.title);
    }

}

