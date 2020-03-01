package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class FirebaseMessagingAdapter implements MessagingService {
    private static final String TAG = "FirebaseMessagingAdapter";
    private static final String databaseUrl = "https://walkwalkrevolution.firebaseio.com";

    private FirebaseMessaging mFirebaseMessaging;
    private Activity mActivity;

    private DatabaseService db;
    private List<MessagingObserver> observers = new ArrayList<>();

    public FirebaseMessagingAdapter(Activity activity, DatabaseService databaseService) {
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

    @Override
    public void sendInvitation(Invitation invitation) {
        db.addInvitationForReceivingUser(invitation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "sendInvitation: invitation sent successfully");
                notifyObserversInvitationSent(invitation);
            } else {
                Log.e(TAG, "sendInvitation: failed to send invitation", task.getException());
                notifyObserversInvitationFailed(task);
            }
        });
    }

    private void notifyObserversInvitationSent(Invitation invitation) {
        observers.forEach(observer -> {
            observer.onInvitationSent(invitation);
        });
    }

    private void notifyObserversInvitationFailed(Task<?> task) {
        observers.forEach(observer -> {
            observer.onFailedInvitationSent(task);
        });
    }

    @Override
    public void register(MessagingObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deregister(MessagingObserver observer) {
        observers.remove(observer);
    }
}
