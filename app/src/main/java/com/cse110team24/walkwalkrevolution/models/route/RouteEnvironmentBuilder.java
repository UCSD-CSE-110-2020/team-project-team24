package com.cse110team24.walkwalkrevolution.models.route;

import com.cse110team24.walkwalkrevolution.models.Builder;

public interface RouteEnvironmentBuilder extends Builder<RouteEnvironment> {
    RouteEnvironmentBuilder addRouteType(RouteEnvironment.RouteType routeType);

    RouteEnvironmentBuilder addTerrainType(RouteEnvironment.TerrainType terrainType);

    RouteEnvironmentBuilder addSurfaceType(RouteEnvironment.SurfaceType surfaceType);

    RouteEnvironmentBuilder addTrailType(RouteEnvironment.TrailType trailType);

    RouteEnvironmentBuilder addDifficulty(RouteEnvironment.Difficulty difficulty);
}
