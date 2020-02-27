package com.cse110team24.walkwalkrevolution.models.route;

import com.cse110team24.walkwalkrevolution.models.Builder;

public interface IRouteBuilder extends Builder<Route> {

    IRouteBuilder addStartingLocation(String location);

    IRouteBuilder addRouteEnvironment(RouteEnvironment env);

    IRouteBuilder addWalkStats(WalkStats stats);

    IRouteBuilder addNotes(String notes);

    IRouteBuilder addFavStatus(boolean isFavorite);
}
