package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseSeviceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;

import java.util.ArrayList;
import java.util.List;

public class FirebaseFirestoreAdapterUsers implements UsersDatabaseService {

    List<UsersDatabaseSeviceObserver> observers = new ArrayList<>();
    @Override
    public void register(UsersDatabaseSeviceObserver usersDatabaseSeviceObserver) {
        observers.add(usersDatabaseSeviceObserver);
    }

    @Override
    public void deregister(UsersDatabaseSeviceObserver usersDatabaseSeviceObserver) {
        observers.remove(usersDatabaseSeviceObserver);
    }
}
