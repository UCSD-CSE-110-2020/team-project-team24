package com.cse110team24.walkwalkrevolution.models;

public class RouteEnvironment {
    private final static int TYPE_CT = 5;

    private RouteType routeType;
    private TerrainType terrainType;
    private SurfaceType surfaceType;
    private TrailType trailType;
    private Difficulty difficulty;

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public SurfaceType getSurfaceType() {
        return surfaceType;
    }

    public void setSurfaceType(SurfaceType surfaceType) {
        this.surfaceType = surfaceType;
    }

    public TrailType getTrailType() {
        return trailType;
    }

    public void setTrailType(TrailType trailType) {
        this.trailType = trailType;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
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