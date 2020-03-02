package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;

public class MessagingServiceFactory {
    public MessagingService createMessagingService(Activity activity, DatabaseService databaseService) {
        return new FirebaseMessagingAdapter(activity, databaseService);
    }
}
