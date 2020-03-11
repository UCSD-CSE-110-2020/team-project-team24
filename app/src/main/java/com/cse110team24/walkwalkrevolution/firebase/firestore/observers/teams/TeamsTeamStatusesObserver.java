package com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.SortedMap;

public interface TeamsTeamStatusesObserver extends TeamsDatabaseServiceObserver {

    /**
     * Called by the TeamsDatabaseServiceSubject this observer is observing when the requested team walks
     * statuses data is ready to be read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject#notifyObserversTeamWalkStatusesRetrieved(SortedMap)}</p>
     * @param statusData map of teammate status data. Key = teammate display name and value = {@link com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalkStatus}
     *                   in string form.
     */
    void onTeamWalkStatusesRetrieved(SortedMap<String, String> statusData);
}
