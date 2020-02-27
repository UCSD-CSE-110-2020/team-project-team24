package com.cse110team24.walkwalkrevolution.models.route;

import com.cse110team24.walkwalkrevolution.models.Builder;

public interface IRouteEnvironmentBuilder extends Builder<RouteEnvironment> {
    IRouteEnvironmentBuilder addRouteType(RouteEnvironment.RouteType routeType);

    IRouteEnvironmentBuilder addTerrainType(RouteEnvironment.TerrainType terrainType);

    IRouteEnvironmentBuilder addSurfaceType(RouteEnvironment.SurfaceType surfaceType);

    IRouteEnvironmentBuilder addTrailType(RouteEnvironment.TrailType trailType);

    IRouteEnvironmentBuilder addDifficulty(RouteEnvironment.Difficulty difficulty);
}
