package com.cse110team24.walkwalkrevolution.models.route;

import com.cse110team24.walkwalkrevolution.utils.Builder;

import java.util.Map;

public interface RouteBuilder extends Builder<Route> {

    RouteBuilder addStartingLocation(String location);

    RouteBuilder addRouteEnvironment(RouteEnvironment env);

    RouteBuilder addWalkStats(WalkStats stats);

    RouteBuilder addNotes(String notes);

    RouteBuilder addFavStatus(boolean isFavorite);

    RouteBuilder addRouteUid(String routeUid);

    RouteBuilder addCreatorDisplayName(String name);

    RouteBuilder addFieldsFromMap(Map<String, Object> data);
}
