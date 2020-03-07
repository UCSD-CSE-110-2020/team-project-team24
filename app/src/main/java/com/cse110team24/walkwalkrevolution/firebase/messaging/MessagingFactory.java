package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;

public interface MessagingFactory {
    Messaging createMessagingService(Activity activity, DatabaseService databaseService);
}
