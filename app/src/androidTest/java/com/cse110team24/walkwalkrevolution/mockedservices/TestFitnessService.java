package com.cse110team24.walkwalkrevolution.mockedservices;

import android.app.Activity;
import android.content.Context;

import androidx.test.rule.ActivityTestRule;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

/** TODO
 * inject this service like this inside the @setup method:
 * FitnessServiceFactory.put(TEST_SERVICE, activity -> new TestFitnessService(activity));
 */

public class TestFitnessService implements FitnessService {

    public static final String TAG = "[TestFitnessService]: ";
    public static final String TEST_SERVICE_KEY = "TEST_SERVICE";


    public HomeActivity activity;

    // set these whenever you need to
    public static long nextStepCount;
    public static long startTime;
    public static long endTime;
    public static long toAdd;
    public static long beforeRecordingSteps;
    public static long endRecordingSteps;
    public static long timeElapsedMillis;


    public TestFitnessService(HomeActivity activity) {
        this.activity = activity;
    }
    @Override
    public int getRequestCode() {
        return 0;
    }
    @Override
    public void setup() {
        System.out.println(TAG + "setup");
    }
    @Override
    public void updateDailyStepCount() {
        System.out.println(TAG + "updateStepCount");
        activity.setDailyStats(nextStepCount);
    }

    @Override
    public void startRecording() {
    }

    @Override
    public void stopRecording() {
        activity.setLatestWalkStats(endRecordingSteps, timeElapsedMillis);
    }

    @Override
    public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
        return new GoogleFitAdapter(null).getDistanceFromHeight(steps, heightFeet, heightRemainderInches);
    }

    @Override
    public void setStartRecordingTime(long startTime) {
        startTime = 0;
    }

    @Override
    public void setEndRecordingTime(long startTime) {
        
    }

    @Override
    public void setStepsToAdd(long stepsToAdd) {

    }
}
