package com.cse110team24.walkwalkrevolution.fitness;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.HomeActivity;

public class MockFitAdapter implements FitnessService {
    private static final String TAG = "MockFitAdapter";
    public static final String MOCK_SERVICE_KEY = "Mock_Fit";
    private static final long ADD_MOCK_CONST = 500;

    private HomeActivity activity;
    private long updatedSteps;
    private long startSteps;
    private long startTime; 
    private long endTime; 

    public MockFitAdapter(HomeActivity activity) {
        this.activity = activity;
    }
 
    public void setStartTime(long startTime) {
        this.startTime = startTime;
        Log.d(TAG, "setStartTime: setting startTime to " + startTime);
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setDailySteps(long updatedSteps) {
        this.updatedSteps += updatedSteps;
    }

    @Override
    public int getRequestCode() {
        return 1;
    }

    @Override
    public void setup() {
    }

    @Override
    public void updateDailyStepCount() {
        activity.setDailyStats(updatedSteps);
    }

    @Override
    public void startRecording() {
        startSteps = updatedSteps;
    }

    @Override
    public void stopRecording() {
        long timeElapsed = endTime - startTime;
        activity.setLatestWalkStats(updatedSteps - startSteps, timeElapsed);
    }

    @Override
    public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
        return new GoogleFitAdapter(null).getDistanceFromHeight(steps, heightFeet, heightRemainderInches);
    }
}
