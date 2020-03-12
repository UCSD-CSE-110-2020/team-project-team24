package com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams;

import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;

import java.util.List;

/**
 * Listens only for changes to teamWalks.
 */
public interface TeamsTeamWalksObserver extends TeamsDatabaseServiceObserver {

    /**
     * Called by the TeamsDatabaseServiceSubject this observer is observing when the requested team walks
     * list data is ready to be read.
     * <p>See also: </p>
     * @param teamWalks the list of team walks queried by the subject
     */
    void onTeamWalksRetrieved(List<TeamWalk> teamWalks);
}
