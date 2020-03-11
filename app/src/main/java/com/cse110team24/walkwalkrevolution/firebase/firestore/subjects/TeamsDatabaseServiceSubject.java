package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamWalksObserver;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.utils.Subject;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.SortedMap;

public interface TeamsDatabaseServiceSubject extends Subject<TeamsDatabaseServiceObserver> {
    /**
     * Notify this subject's observers of type {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeammatesObserver}
     * that the requested team's teammates data is ready to read.
     * See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeammatesObserver#onTeamRetrieved(ITeam)}.
     * @param team the team that was retrieved. Does not include the currently signed in user.
     */
    void notifyObserversTeamRetrieved(ITeam team);

    /**
     * Notify this subject's observers of type {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver}
     * that the requested team's routes data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver#onRoutesRetrieved(List, DocumentSnapshot)}</p>
     * @param routes the list of routes that was retrieved. Does not include the currently signed in user's routes.
     * @param lastRoute the last route in the list's DocumentSnapshot.
     */
    void notifyObserversTeamRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute);

    /**
     * Notify this subject's observers of type {@link TeamsTeamWalksObserver} that the requested
     * team's walks data is ready to be read.
     * <p>See also: {@link TeamsTeamWalksObserver#onTeamWalksRetrieved(List)}</p>
     * @param walks the list of TeamWalks that was retrieved.
     */
    void notifyObserversTeamWalksRetrieved(List<TeamWalk> walks);

    /**
     * Notify this subject's observers of type {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamStatusesObserver}
     * that the requested team walk statuses data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamStatusesObserver#onTeamWalkStatusesRetrieved(SortedMap)}</p>
     * @param statusData map of teammate status data. Key = teammate display name and value = {@link com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalkStatus}
     *                   in string form.
     */
    void notifyObserversTeamWalkStatusesRetrieved(SortedMap<String, String> statusData);
}
