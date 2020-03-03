package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.List;

public interface DatabaseServiceSubject extends Subject<DatabaseServiceObserver> {
    void notifyObserversTeamRetrieved(List<IUser> team);
    void notifyObserversFieldRetrieved(Object field);
}
