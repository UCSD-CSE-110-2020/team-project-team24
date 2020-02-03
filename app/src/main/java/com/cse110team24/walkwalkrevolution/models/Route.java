package com.cse110team24.walkwalkrevolution.models;

import java.io.Serializable;
import java.lang.Comparable;


public class Route implements Serializable, Comparable {

    private String title;

    private WalkStats stats;

    // OPTIONAL fields
    private String startingLocation;
    private RouteType routeType;
    private TerrainType terrainType;
    private SurfaceType surfaceType;
    private TrailType trailType;
    private Difficulty difficulty;
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

    @Override
    public int compareTo(Object o) {
        return 0;
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

    public Object[] gatherFields() {

        return null;
    }

}

enum RouteType {
    LOOP, OUT_AND_BACK;
}

enum TerrainType {
    FLAT, HILLY;
}

enum SurfaceType {
    EVEN, UNEVEN;
}

enum Difficulty {
    EASY, MODERATE, HARD;
}

enum TrailType {
    STREETS, TRAIL;
}
