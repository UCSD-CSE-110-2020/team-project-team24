package com.cse110team24.walkwalkrevolution.models.route;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.Objects;

public class Route implements Serializable, Comparable<Route> {
    private static final String TITLE_ERR = "A mTitle is required for a route.";

    private String mTitle;

    private WalkStats mStats;

    // OPTIONAL fields
    private String mStartingLocation;
    private RouteEnvironment mEnvironment;
    private boolean mIsFavorite;
    private String mNotes;

    private String mRouteUid;

    public Route(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException(TITLE_ERR);
        }
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public Route setTitle(String title) throws IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException(TITLE_ERR);
        }
        mTitle = title;
        return this;
    }

    public String getStartingLocation() {
        return mStartingLocation;
    }

    public Route setStartingLocation(String location) {
        mStartingLocation = location;
        return this;
    }

    public String getNotes() {
        return mNotes;
    }

    public Route setNotes(String notes) {
        mNotes = notes;
        return this;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public Route setFavorite(boolean favorite) {
        mIsFavorite = favorite;
        return this;
    }

    public WalkStats getStats() {
        return mStats;
    }

    public Route setStats(WalkStats stats) {
        mStats = stats;
        return this;
    }

    public RouteEnvironment getEnvironment() {
        return mEnvironment;
    }

    public Route setEnvironment(RouteEnvironment environment) {
        mEnvironment = environment;
        return this;
    }

    public Route setRouteUid(String routeUid) {
        mRouteUid = routeUid;
        return this;
    }

    public String getRouteUid() {
        return mRouteUid;
    }

    @Override
    public int compareTo(Route o) {
        return mTitle.compareTo(o.mTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Route) {
            Route route = (Route) o;
            boolean titleEquals = Objects.equals(mTitle, route.mTitle);
            boolean locEquals = Objects.equals(mStartingLocation, route.mStartingLocation);
            boolean envEquals = Objects.equals(mEnvironment, route.mEnvironment);
            boolean statsEquals = Objects.equals(mStats, route.mStats);
            boolean notesEquals = Objects.equals(mNotes, route.mNotes);

            return titleEquals && locEquals && envEquals
                    && statsEquals && notesEquals && mIsFavorite == route.mIsFavorite;
        }
        return false;
    }

    @Override
    public String toString() {
        return "\ntitle: " + mTitle +
                "\nstats: " + ((mStats == null) ? "none" : mStats);
    }

    public static class Builder implements RouteBuilder {
        private Route mToBuild;

        public Builder(String title) {
            mToBuild = new Route(title);
        }

        @Override
        public Builder addStartingLocation(String location) {
            mToBuild.setStartingLocation(location);
            return this;
        }

        @Override
        public Builder addRouteEnvironment(RouteEnvironment env) {
            mToBuild.setEnvironment(env);
            return this;
        }

        @Override
        public Builder addWalkStats(WalkStats stats) {
            mToBuild.setStats(stats);
            return this;
        }

        @Override
        public Builder addNotes(String notes) {
            mToBuild.setNotes(notes);
            return this;
        }

        @Override
        public Builder addFavStatus(boolean isFavorite) {
            mToBuild.setFavorite(isFavorite);
            return this;
        }

        @Override
        public Builder addRouteUid(String routeUid) {
            mToBuild.setRouteUid(routeUid);
            return this;
        }

        @Override
        public Route build() {
            return mToBuild;
        }
    }

}

