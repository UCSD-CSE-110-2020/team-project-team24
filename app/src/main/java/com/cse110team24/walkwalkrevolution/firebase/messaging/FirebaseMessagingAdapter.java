package com.cse110team24.walkwalkrevolution.firebase.messaging;

import android.app.Activity;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.invitations.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.invitations.invitation.InvitationStatus;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 * This type's Messaging provider is FirebaseMessaging.
 */
public class FirebaseMessagingAdapter implements MessagingService {
    private static final String TAG = "WWR_FirebaseMessagingAdapter";
    private static final String databaseUrl = "https://walkwalkrevolution.firebaseio.com";

    private FirebaseMessaging mFirebaseMessaging;
    private Activity mActivity;

    private DatabaseService mDb;
    private List<MessagingObserver> observers = new ArrayList<>();

    public FirebaseMessagingAdapter(Activity activity, DatabaseService databaseService) {
        mActivity = activity;
        FirebaseApp.initializeApp(activity);
        mFirebaseMessaging = FirebaseMessaging.getInstance();
        mDb = databaseService;
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
        InvitationsDatabaseService db = (InvitationsDatabaseService) mDb;
        db.addInvitationForReceivingUser(invitation).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "sendInvitation: invitation sent successfully");
                db.addInvitationForSendingUser(invitation);
                notifyObserversInvitationSent(invitation);
            } else {
                Log.e(TAG, "sendInvitation: error sending invitation", task.getException());
                notifyObserversFailedInvitationSent(task);
            }
        });
    }

    // TODO: 3/3/20 change status
    @Override
    public void updateInvitationStatus(Invitation invitation, InvitationStatus status) {
    }

    @Override
    public void notifyObserversInvitationSent(Invitation invitation) {
        observers.forEach(observer -> {
            observer.onInvitationSent(invitation);
        });
    }

    @Override
    public void notifyObserversFailedInvitationSent(Task<?> task) {
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
