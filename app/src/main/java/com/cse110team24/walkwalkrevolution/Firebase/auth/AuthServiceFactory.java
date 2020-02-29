package com.cse110team24.walkwalkrevolution.Firebase.auth;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceFactory {
    private static final String TAG = "[AuthServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public AuthService createAuthService(String key) {
        return blueprints.get(key).create();
    }

    public interface BluePrint {
        AuthService create();
    }
}
