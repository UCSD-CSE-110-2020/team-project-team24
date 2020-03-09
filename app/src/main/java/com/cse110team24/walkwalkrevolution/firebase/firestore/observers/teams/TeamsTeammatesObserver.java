package com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;

/**
 * Listens only for changes of team.
 */
public interface TeamsTeammatesObserver extends TeamsDatabaseServiceObserver {
    /**
     * Called by the TeamsDatabaseServiceSubject this observer is observing when the requested teammates
     * list data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject#notifyObserversTeamRetrieved(ITeam)}</p>
     * @param team the team that was retrieved. Does not include the currently signed in user.
     */
    void onTeamRetrieved(ITeam team);
}
