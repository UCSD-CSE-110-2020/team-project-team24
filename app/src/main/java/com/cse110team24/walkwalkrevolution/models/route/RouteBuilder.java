package com.cse110team24.walkwalkrevolution.models.route;

import com.cse110team24.walkwalkrevolution.utils.Builder;

public interface RouteBuilder extends Builder<Route> {

    RouteBuilder addStartingLocation(String location);

    RouteBuilder addRouteEnvironment(RouteEnvironment env);

    RouteBuilder addWalkStats(WalkStats stats);

    RouteBuilder addNotes(String notes);

    RouteBuilder addFavStatus(boolean isFavorite);
}
