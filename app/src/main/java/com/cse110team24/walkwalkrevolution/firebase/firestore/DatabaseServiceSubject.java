package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.List;
import java.util.Map;

public interface DatabaseServiceSubject extends Subject<DatabaseServiceObserver> {
    void notifyObserversTeamRetrieved(ITeam team);
    void notifyObserversFieldRetrieved(Object field);
    void notifyObserversUserData(Map<String, Object> userDataMap);
}
