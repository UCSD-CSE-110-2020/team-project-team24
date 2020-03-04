package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;

public interface MessagingServiceFactory {
    MessagingService createMessagingService(Activity activity, DatabaseService databaseService);
}
