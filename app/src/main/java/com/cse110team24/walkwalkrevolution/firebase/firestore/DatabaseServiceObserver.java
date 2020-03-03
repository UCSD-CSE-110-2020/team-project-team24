package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Map;
import java.util.Observer;

public interface DatabaseServiceObserver {
    void onTeamRetrieved(ITeam team);
    void onFieldRetrieved(Object field);
    void onUserData(Map<String, Object> userDataMap);
}
