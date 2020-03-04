package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.Map;

public interface UsersDatabaseServiceObserver {
    void onUserData(Map<String, Object> userDataMap);
    void onUserExists(IUser otherUser);
    void onUserDoesNotExist();
}
