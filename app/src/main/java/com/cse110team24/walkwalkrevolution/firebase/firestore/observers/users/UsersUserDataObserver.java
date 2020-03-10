package com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users;

import java.util.Map;

/**
 * Listens only when user data is ready.
 */
public interface UsersUserDataObserver extends UsersDatabaseServiceObserver {

    /**
     * Called by the UsersDatabaseServiceSubject this observer is observing when requested user data is ready
     * to read.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.UsersDatabaseServiceSubject#notifyObserversUserData(Map)}</p>
     * @param userDataMap the user data being requested
     */
    void onUserData(Map<String, Object> userDataMap);
}
