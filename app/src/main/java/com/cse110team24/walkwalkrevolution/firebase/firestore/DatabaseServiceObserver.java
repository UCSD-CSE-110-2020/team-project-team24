package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Observer;

public interface DatabaseServiceObserver {
    void onTeamRetrieved(List<IUser> team);
    void onFieldRetrieved(Object field);
}
