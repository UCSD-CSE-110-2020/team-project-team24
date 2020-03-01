package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseMessagingAdapter implements MessagingService {
    private static String databaseUrl = "https://walkwalkrevolution.firebaseio.com";

    private FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingAdapter() {
        firebaseMessaging = FirebaseMessaging.getInstance();
    }

    private void provideCredentials() {
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setDatabaseUrl("https://walkwalkrevolution.firebaseio.com")
//                .build();
//
//        FirebaseApp.initializeApp(options);

    }

    @Override
    public void subscribeToNotificationsTopic(String documentKey) {

    }
}
