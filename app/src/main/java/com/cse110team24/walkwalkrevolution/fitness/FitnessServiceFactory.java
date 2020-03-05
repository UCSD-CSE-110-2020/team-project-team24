package com.cse110team24.walkwalkrevolution.fitness;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.HomeActivity;

import java.util.HashMap;
import java.util.Map;

public class FitnessServiceFactory {
    private static final String TAG = "[WWR_FitnessServiceFactory]";

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, HomeActivity activity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        return blueprints.get(key).create(activity);
    }

    public interface BluePrint {
        FitnessService create(HomeActivity activity);
    }
}
