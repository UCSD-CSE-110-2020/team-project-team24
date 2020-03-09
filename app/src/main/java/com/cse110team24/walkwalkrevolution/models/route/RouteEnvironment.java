package com.cse110team24.walkwalkrevolution.models.route;

import java.io.Serializable;

public class RouteEnvironment implements Serializable {
    public enum RouteType {
        LOOP, OUT_AND_BACK
    }

    public enum TerrainType {
        FLAT, HILLY
    }

    public enum SurfaceType {
        EVEN, UNEVEN
    }

    public enum Difficulty {
        EASY, MODERATE, HARD
    }

    public enum TrailType {
        STREETS, TRAIL
    }

    private RouteType mRouteType;
    private TerrainType mTerrainType;
    private SurfaceType mSurfaceType;
    private TrailType mTrailType;
    private Difficulty mDifficulty;

    public RouteType getRouteType() {
        return mRouteType;
    }

    public void setRouteType(RouteType routeType) {
        this.mRouteType = routeType;
    }

    public TerrainType getTerrainType() {
        return mTerrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.mTerrainType = terrainType;
    }

    public SurfaceType getSurfaceType() {
        return mSurfaceType;
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.mSurfaceType = surfaceType;
    }

    public TrailType getTrailType() {
        return mTrailType;
    }

    public void setTrailType(TrailType trailType) {
        this.mTrailType = trailType;
    }

    public Difficulty getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.mDifficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RouteEnvironment) {
            RouteEnvironment environment = (RouteEnvironment) o;
            return mRouteType == environment.mRouteType && mTerrainType == environment.mTerrainType &&
                    mSurfaceType == environment.mSurfaceType && mTrailType == environment.mTrailType &&
                    mDifficulty == environment.mDifficulty;
        }

        return false;
    }

    public static RouteEnvBuilder builder() {
        return new RouteEnvBuilder();
    }

}