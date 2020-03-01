package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseMessagingAdapter implements MessagingService {
    private static final String TAG = "FirebaseMessagingAdapter";
    private static final String databaseUrl = "https://walkwalkrevolution.firebaseio.com";

    private FirebaseMessaging mFirebaseMessaging;
    private Activity mActivity;

    public FirebaseMessagingAdapter(Activity activity) {
        mActivity = activity;
        FirebaseApp.initializeApp(activity);
        mFirebaseMessaging = FirebaseMessaging.getInstance();
    }

    private void provideCredentials() {
    }

    @Override
    public void subscribeToNotificationsTopic(String topic) {
        mFirebaseMessaging.subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "subscribeToNotificationsTopic: subscribed to " + topic);
                    } else {
                        Log.e(TAG, "subscribeToNotificationsTopic: error subscribing to topic " + topic, task.getException());
                    }
                });
    }
}
