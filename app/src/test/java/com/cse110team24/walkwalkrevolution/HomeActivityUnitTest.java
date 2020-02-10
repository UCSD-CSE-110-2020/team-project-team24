package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class HomeActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final int FEET = 5;
    private static final float INCHES = 3f;

    private TextView latestStepsTv;
    private TextView latestDistanceTv;
    private TextView latestTimeTv;
    private TextView noWalkTodayTv;
    private Button startButton;
    private Button stopButton;

    private Intent intent;
    private long nextStepCount;
    private long endStepCount;
    private long startTime;
    private long endTime;

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeActivity.class)
                    .putExtra(HomeActivity.FITNESS_SERVICE_KEY, TEST_SERVICE)
                    .putExtra(HomeActivity.HEIGHT_FT_KEY, FEET)
                    .putExtra(HomeActivity.HEIGHT_IN_KEY, INCHES);
    }

    @Test
    public void testHeightSaved() {
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE);
            assertEquals(5, preferences.getInt(HomeActivity.HEIGHT_FT_KEY, -1));
            assertEquals(3, preferences.getFloat(HomeActivity.HEIGHT_IN_KEY, -1), 0.1);
        });
    }

    @Test
    public void testDailyStats() {
        nextStepCount = 5842;
        double expectedMiles = 2.39;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView stepsTv = activity.findViewById(R.id.dailyStepsText);
            TextView distanceTv = activity.findViewById(R.id.dailyDistanceText);
            activity.onActivityResult(0, Activity.RESULT_OK, null);
            assertEquals(String.valueOf(nextStepCount), stepsTv.getText().toString());
            assertEquals(expectedMiles, Double.valueOf(distanceTv.getText().toString()), 0.1);
        });
    }

    @Test
    public void testNoLatestWalk() {
        nextStepCount = 5842;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getLatestUIViews(activity);
            assertEquals(noWalkTodayTv.getVisibility(), View.VISIBLE);
            assertTrue(latestDistanceTv.getText().toString().isEmpty());
            assertTrue(latestStepsTv.getText().toString().isEmpty());
            assertTrue(latestTimeTv.getText().toString().isEmpty());
        });
    }

    @Test
    public void testOnGoingLatestWalk() {
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
           getLatestUIViews(activity);
            assertEquals(startButton.getVisibility(), View.VISIBLE);
            assertEquals(stopButton.getVisibility(), View.INVISIBLE);
           startButton.performClick();
            assertEquals(startButton.getVisibility(), View.INVISIBLE);
            assertEquals(stopButton.getVisibility(), View.VISIBLE);
        });
    }

    @Test
    public void testCompletedWalk() {
        nextStepCount = 5842;
        double timeElapsedMinutes = 1.5;
        double expectedMiles = 2.39;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getLatestUIViews(activity);
            startButton.performClick();
            stopButton.performClick();
            assertEquals(startButton.getVisibility(), View.VISIBLE);
            assertEquals(stopButton.getVisibility(), View.INVISIBLE);

            assertEquals(5842, (int) Integer.valueOf(latestStepsTv.getText().toString()));
            assertEquals(timeElapsedMinutes, Double.valueOf(latestTimeTv.getText().toString().split(" ")[0]), 0.1);
            assertEquals(expectedMiles, Double.valueOf(latestDistanceTv.getText().toString().split(" ")[0]), 0.1);
        });
    }

    private void getLatestUIViews(HomeActivity activity) {
        noWalkTodayTv = activity.findViewById(R.id.noWalkToday);
        latestDistanceTv = activity.findViewById(R.id.totalDistanceCounter);
        latestStepsTv = activity.findViewById(R.id.totalStepsCounter);
        latestTimeTv = activity.findViewById(R.id.timeElapsedCounter);
        startButton = activity.findViewById(R.id.startWalkButton);
        stopButton = activity.findViewById(R.id.stopWalkButton);
    }


    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private HomeActivity activity;

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
            activity.setLatestWalkStats(nextStepCount, 90_000);
        }

        @Override
        public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
            return new GoogleFitAdapter(activity).getDistanceFromHeight(steps, heightFeet, heightRemainderInches);
        }

        @Override
        public void setStartRecordingTime(long startTime) {

        }

        @Override
        public void setEndRecordingTime(long startTime) {

        }

        @Override
        public void setStepsToAdd(long stepsToAdd) {

        }
    }
}
