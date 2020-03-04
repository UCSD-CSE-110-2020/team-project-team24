package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import java.util.Map;

public interface UsersDatabaseServiceObserver {
    void onUserData(Map<String, Object> userDataMap);
    void onUserExists();
    void onUserDoesNotExist();
}
