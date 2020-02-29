package com.cse110team24.walkwalkrevolution.firebase.auth;

import android.app.Application;

public class AuthServiceApplication extends Application {

    private static AuthServiceFactory authServiceFactory;

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
}
