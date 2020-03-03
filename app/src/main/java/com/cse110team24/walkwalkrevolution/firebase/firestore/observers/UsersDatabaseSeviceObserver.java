package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import java.util.Map;

public interface UsersDatabaseSeviceObserver {
    void onUserData(Map<String, Object> userDataMap);
}
