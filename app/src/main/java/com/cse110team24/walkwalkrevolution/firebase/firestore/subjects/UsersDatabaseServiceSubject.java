package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.Map;

public interface UsersDatabaseServiceSubject extends Subject<UsersDatabaseServiceObserver> {
    void notifyObserversUserData(Map<String, Object> userDataMap);

}
