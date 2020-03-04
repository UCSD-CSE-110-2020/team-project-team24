package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;

public interface TeamsDatabaseServiceObserver {
    void onTeamRetrieved(ITeam team);
}
