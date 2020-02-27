package com.cse110team24.walkwalkrevolution.models;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.Objects;

public class Route implements Serializable, Comparable<Route> {
    private static final String TITLE_ERR = "A title is required for a route.";

    private String title;

    private WalkStats stats;

    // OPTIONAL fields
    private String startingLocation;
    private RouteEnvironment environment;
    private boolean isFavorite;
    private String notes;

    public Route(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException(TITLE_ERR);
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Route setTitle(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException(TITLE_ERR);
        }
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

    public Route setEnvironment(RouteEnvironment environment) {
        this.environment = environment;
        return this;
    }

    @Override
    public int compareTo(Route o) {
        return title.compareTo(o.title);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Route) {
            Route route = (Route) o;
            boolean titleEquals = Objects.equals(title, route.title);
            boolean locEquals = Objects.equals(startingLocation, route.startingLocation);
            boolean envEquals = Objects.equals(environment, route.environment);
            boolean statsEquals = Objects.equals(stats, route.stats);
            boolean notesEquals = Objects.equals(notes, route.notes);

            return titleEquals && locEquals && envEquals
                    && statsEquals && notesEquals && isFavorite == route.isFavorite;
        }
        return false;
    }

    @Override
    public String toString() {
        return "\ntitle: " + title +
                "\nstats: " + ((stats == null) ? "none" : stats);
    }

    public static class RouteBuilder implements Builder<Route> {
        private Route toBuild;

        public RouteBuilder(String title) {
            toBuild = new Route(title);
        }

        public RouteBuilder addRouteEnvironment(RouteEnvironment env) {
            toBuild.setEnvironment(env);
            return this;
        }

        public RouteBuilder addWalkStats(WalkStats stats) {
            toBuild.setStats(stats);
            return this;
        }

        public RouteBuilder addNotes(String notes) {
            toBuild.setNotes(notes);
            return this;
        }

        public RouteBuilder addFavStatus(boolean isFavorite) {
            toBuild.setFavorite(isFavorite);
            return this;
        }

        @Override
        public Route build() {
            return toBuild;
        }
    }

}

