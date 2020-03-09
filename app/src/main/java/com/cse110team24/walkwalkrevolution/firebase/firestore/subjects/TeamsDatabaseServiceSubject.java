package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamWalk;
import com.cse110team24.walkwalkrevolution.utils.Subject;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface TeamsDatabaseServiceSubject extends Subject<TeamsDatabaseServiceObserver> {
    /**
     * Notify this subject's observers that the requested team's teammates data is ready to read. 
     * See also: {@link TeamsDatabaseServiceObserver#onTeamRetrieved(ITeam)}.
     * @param team the team that was retrieved. Does not include the currently signed in user.
     */
    void notifyObserversTeamRetrieved(ITeam team);

    /**
     * Notify this subject's observers that the requested team's routes data is ready to be read.
     * <p>See also: {@link TeamsDatabaseServiceObserver#onRoutesRetrieved(List, DocumentSnapshot)}</p>
     * @param routes the list of routes that was retrieved. Does not include the currently signed in user's routes.
     * @param lastRoute the last route in the list's DocumentSnapshot.
     */
    void notifyObserversTeamRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute);

    /**
     * Notify this subject's observers that the requested team's walks data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsTeamWalksObserver#onTeamWalksRetrieved(List)}</p>
     * @param walks the list of TeamWalks that was retrieved.
     */
    void notifyObserversTeamWalksRetrieved(List<TeamWalk> walks);
}
