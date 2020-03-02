package com.cse110team24.walkwalkrevolution.application;

import android.app.Application;

import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class FirebaseApplicationWWR extends Application implements ApplicationSubject {

    private static FirebaseAuthServiceFactory authServiceFactory;
    private static FirestoreDatabaseServiceFactory databaseServiceFactory;
    private static FirebaseMessagingServiceFactory messagingServiceFactory;
    List<ApplicationObserver> observers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        authServiceFactory = new FirebaseAuthServiceFactory();
        databaseServiceFactory = new FirestoreDatabaseServiceFactory();
        messagingServiceFactory = new FirebaseMessagingServiceFactory();
    }

    public static FirebaseAuthServiceFactory getAuthServiceFactory() {
        return authServiceFactory;
    }

    public static FirebaseAuthServiceFactory setAuthServiceFactory(FirebaseAuthServiceFactory asf) {
        return authServiceFactory = asf;
    }

    public static FirestoreDatabaseServiceFactory getDatabaseServiceFactory() {
        return databaseServiceFactory;
    }

    public static FirestoreDatabaseServiceFactory setDatabaseServiceFactory(FirestoreDatabaseServiceFactory dsf) {
        return databaseServiceFactory = dsf;
    }

    public static FirebaseMessagingServiceFactory getMessagingServiceFactory() {
        return messagingServiceFactory;
    }

    public static FirebaseMessagingServiceFactory setMessagingServiceFactory(FirebaseMessagingServiceFactory msf) {
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
