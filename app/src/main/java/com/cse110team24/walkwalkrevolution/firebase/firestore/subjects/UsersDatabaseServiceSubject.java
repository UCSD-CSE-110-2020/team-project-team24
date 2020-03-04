package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseSeviceObserver;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.Map;

public interface UsersDatabaseServiceSubject extends Subject<UsersDatabaseSeviceObserver> {
    void notifyObserversUserData(Map<String, Object> userDataMap);

}
