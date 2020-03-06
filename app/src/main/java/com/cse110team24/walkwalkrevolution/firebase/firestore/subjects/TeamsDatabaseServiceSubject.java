package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.utils.Subject;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface TeamsDatabaseServiceSubject extends Subject<TeamsDatabaseServiceObserver> {
    void notifyObserversTeamRetrieved(ITeam team);
    void notifyObserversTeamRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute);
}
