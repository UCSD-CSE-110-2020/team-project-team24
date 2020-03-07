package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.teams.team.ITeam;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface TeamsDatabaseServiceObserver {
    /**
     * Called by the TeamsDatabaseServiceSubject this observer is observing when the requested teammates
     * list data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject#notifyObserversTeamRetrieved(ITeam)}</p>
     * @param team the team that was retrieved. Does not include the currently signed in user.
     */
    void onTeamRetrieved(ITeam team);

    /**
     * Called by the TeamsDatabaseServiceSubject this observer is observing when the requested team routes
     * data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject#notifyObserversTeamRoutesRetrieved(List, DocumentSnapshot)}</p>
     * @param routes the list of routes that was retrieved. Does not include the currently signed in user's routes.
     * @param lastRoute the last route in the list's DocumentSnapshot.
     */
    void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute);
}
