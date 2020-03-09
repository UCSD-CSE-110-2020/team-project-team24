package com.cse110team24.walkwalkrevolution.application;

import android.app.Application;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingFactory;

import java.util.ArrayList;
import java.util.List;

public class FirebaseApplicationWWR extends Application implements ApplicationSubject {
    private static final String TAG = "WWR_FirebaseApplicationWWR";
    private static AuthFactory authFactory;
    private static DatabaseServiceFactory databaseServiceFactory;
    private static MessagingFactory messagingFactory;
    List<ApplicationObserver> observers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: application created");
        authFactory = new FirebaseAuthFactory();
        databaseServiceFactory = new FirestoreDatabaseServiceFactory();
        messagingFactory = new FirebaseMessagingFactory();
    }

    public static AuthFactory getAuthFactory() {
        return authFactory;
    }

    public static AuthFactory setAuthServiceFactory(AuthFactory asf) {
//        Log.i(TAG, "setAuthServiceFactory: auth service factory set with class name " + asf.getClass().getSimpleName());
        return authFactory = asf;
    }

    public static DatabaseServiceFactory getDatabaseServiceFactory() {
        return databaseServiceFactory;
    }

    public static DatabaseServiceFactory setDatabaseServiceFactory(DatabaseServiceFactory dsf) {
//        Log.i(TAG, "setDatabaseServiceFactory: database service factory set with class name " + dsf.getClass().getSimpleName());
        return databaseServiceFactory = dsf;
    }

    public static MessagingFactory getMessagingFactory() {
        return messagingFactory;
    }

    public static MessagingFactory setMessagingServiceFactory(MessagingFactory msf) {
//        Log.i(TAG, "setMessagingServiceFactory: messaging service factory set with class name " + msf.getClass().getSimpleName());
        return messagingFactory = msf;
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
