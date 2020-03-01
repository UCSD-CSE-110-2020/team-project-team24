package com.cse110team24.walkwalkrevolution.application;

import android.app.Application;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.observer.Subject;

import java.util.ArrayList;
import java.util.List;

public class FirebaseApplicationWWR extends Application implements ApplicationSubject {

    private static AuthServiceFactory authServiceFactory;
    List<ApplicationObserver> observers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        authServiceFactory = new AuthServiceFactory();
    }

    public static AuthServiceFactory getAuthServiceFactory() {
        return authServiceFactory;
    }

    public static AuthServiceFactory setAuthServiceFactory(AuthServiceFactory asf) {
        return authServiceFactory = asf;
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
