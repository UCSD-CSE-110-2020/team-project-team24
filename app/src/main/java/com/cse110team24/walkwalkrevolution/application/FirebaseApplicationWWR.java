package com.cse110team24.walkwalkrevolution.application;

import android.app.Application;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class FirebaseApplicationWWR extends Application implements ApplicationSubject {

    private static AuthServiceFactory authServiceFactory;
    private static DatabaseServiceFactory databaseServiceFactory;
    private static MessagingServiceFactory messagingServiceFactory;
    List<ApplicationObserver> observers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        authServiceFactory = new FirebaseAuthServiceFactory();
        databaseServiceFactory = new FirestoreDatabaseServiceFactory();
        messagingServiceFactory = new FirebaseMessagingServiceFactory();
    }

    public static AuthServiceFactory getAuthServiceFactory() {
        return authServiceFactory;
    }

    public static AuthServiceFactory setAuthServiceFactory(FirebaseAuthServiceFactory asf) {
        return authServiceFactory = asf;
    }

    public static DatabaseServiceFactory getDatabaseServiceFactory() {
        return databaseServiceFactory;
    }

    public static DatabaseServiceFactory setDatabaseServiceFactory(FirestoreDatabaseServiceFactory dsf) {
        return databaseServiceFactory = dsf;
    }

    public static MessagingServiceFactory getMessagingServiceFactory() {
        return messagingServiceFactory;
    }

    public static MessagingServiceFactory setMessagingServiceFactory(FirebaseMessagingServiceFactory msf) {
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
