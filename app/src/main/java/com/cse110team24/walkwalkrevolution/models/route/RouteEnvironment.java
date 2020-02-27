package com.cse110team24.walkwalkrevolution.models.route;

import java.io.Serializable;

public class RouteEnvironment implements Serializable {
    public enum RouteType {
        LOOP, OUT_AND_BACK;
    }

    public enum TerrainType {
        FLAT, HILLY;
    }

    public enum SurfaceType {
        EVEN, UNEVEN;
    }

    public enum Difficulty {
        EASY, MODERATE, HARD;
    }

    public enum TrailType {
        STREETS, TRAIL;
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

    public RouteEnvironmentBuilder builder() {
        return new RouteEnvironmentBuilder();
    }

    public static class RouteEnvironmentBuilder implements IRouteEnvironmentBuilder {
        private RouteEnvironment mEnv;

        public RouteEnvironmentBuilder() {
            mEnv = new RouteEnvironment();
        }

        @Override
        public RouteEnvironmentBuilder addRouteType(RouteType routeType) {
            mEnv.setRouteType(routeType);
            return this;
        }

        @Override
        public RouteEnvironmentBuilder addTerrainType(TerrainType terrainType) {
            mEnv.setTerrainType(terrainType);
            return this;
        }

        @Override
        public RouteEnvironmentBuilder addSurfaceType(SurfaceType surfaceType) {
            mEnv.setSurfaceType(surfaceType);
            return this;
        }

        @Override
        public RouteEnvironmentBuilder addTrailType(TrailType trailType) {
            mEnv.setTrailType(trailType);
            return this;
        }

        @Override
        public RouteEnvironmentBuilder addDifficulty(Difficulty difficulty) {
            mEnv.setDifficulty(difficulty);
            return this;
        }

        @Override
        public RouteEnvironment build() {
            return mEnv;
        }
    }

}