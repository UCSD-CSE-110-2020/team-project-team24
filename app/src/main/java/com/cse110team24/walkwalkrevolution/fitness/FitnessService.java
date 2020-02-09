package com.cse110team24.walkwalkrevolution.fitness;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateDailyStepCount();
    void startRecording();
    void stopRecording();
    double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches);
}
