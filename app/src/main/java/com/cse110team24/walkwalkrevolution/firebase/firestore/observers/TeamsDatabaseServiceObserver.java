package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface TeamsDatabaseServiceObserver {
    void onTeamRetrieved(ITeam team);
    void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute);
}
