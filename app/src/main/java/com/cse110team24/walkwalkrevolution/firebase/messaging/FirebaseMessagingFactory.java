package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;

public class FirebaseMessagingFactory implements MessagingFactory {

    @Override
    public Messaging createMessagingService(Activity activity, DatabaseService databaseService) {
        return new FirebaseMessagingAdapter(activity, databaseService);
    }
}
