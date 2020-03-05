package com.cse110team24.walkwalkrevolution.application;

import android.app.Application;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class FirebaseApplicationWWR extends Application implements ApplicationSubject {
    private static final String TAG = "WWR_FirebaseApplicationWWR";
    private static AuthServiceFactory authServiceFactory;
    private static DatabaseServiceFactory databaseServiceFactory;
    private static MessagingServiceFactory messagingServiceFactory;
    List<ApplicationObserver> observers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: application created");
        authServiceFactory = new FirebaseAuthServiceFactory();
        databaseServiceFactory = new FirestoreDatabaseServiceFactory();
        messagingServiceFactory = new FirebaseMessagingServiceFactory();
    }

    public static AuthServiceFactory getAuthServiceFactory() {
        return authServiceFactory;
    }

    public static AuthServiceFactory setAuthServiceFactory(AuthServiceFactory asf) {
        Log.i(TAG, "setAuthServiceFactory: auth service factory set with class name " + asf.getClass().getSimpleName());
        return authServiceFactory = asf;
    }

    public static DatabaseServiceFactory getDatabaseServiceFactory() {
        return databaseServiceFactory;
    }

    public static DatabaseServiceFactory setDatabaseServiceFactory(DatabaseServiceFactory dsf) {
        Log.i(TAG, "setDatabaseServiceFactory: database service factory set with class name " + dsf.getClass().getSimpleName());
        return databaseServiceFactory = dsf;
    }

    public static MessagingServiceFactory getMessagingServiceFactory() {
        return messagingServiceFactory;
    }

    public static MessagingServiceFactory setMessagingServiceFactory(MessagingServiceFactory msf) {
        Log.i(TAG, "setMessagingServiceFactory: messaging service factory set with class name " + msf.getClass().getSimpleName());
        return messagingServiceFactory = msf;
    }

    @Override
    public void notifyObserversNewToken(String token) {
        observers.forEach(observer -> {
            observer.onNewToken(token);
        });
    }

    @Override
    public void register(ApplicationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deregister(ApplicationObserver observer) {
        observers.remove(observer);
    }
}
