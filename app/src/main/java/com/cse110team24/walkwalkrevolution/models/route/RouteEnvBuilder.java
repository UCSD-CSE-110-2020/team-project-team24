package com.cse110team24.walkwalkrevolution.models.route;

public class RouteEnvBuilder implements RouteEnvironmentBuilder {
    private RouteEnvironment mEnv;

    public RouteEnvBuilder() {
        mEnv = new RouteEnvironment();
    }

    @Override
    public RouteEnvBuilder addRouteType(RouteEnvironment.RouteType routeType) {
        mEnv.setRouteType(routeType);
        return this;
    }

    @Override
    public RouteEnvBuilder addTerrainType(RouteEnvironment.TerrainType terrainType) {
        mEnv.setTerrainType(terrainType);
        return this;
    }

    @Override
    public RouteEnvBuilder addSurfaceType(RouteEnvironment.SurfaceType surfaceType) {
        mEnv.setSurfaceType(surfaceType);
        return this;
    }

    @Override
    public RouteEnvBuilder addTrailType(RouteEnvironment.TrailType trailType) {
        mEnv.setTrailType(trailType);
        return this;
    }

    @Override
    public RouteEnvBuilder addDifficulty(RouteEnvironment.Difficulty difficulty) {
        mEnv.setDifficulty(difficulty);
        return this;
    }

    @Override
    public RouteEnvironment build() {
        return mEnv;
    }
}
