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

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.shadows.ShadowToast;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class HomeActivityUnitTest extends TestInjection {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final int FEET = 5;
    private static final float INCHES = 3f;
    private static final String TOAST_MESSAGE = "Remember to set an end time for your walk!";

    private TextView stepsTv;
    private TextView distanceTv;
    private TextView latestStepsTv;
    private TextView latestDistanceTv;
    private TextView latestTimeTv;
    private TextView noWalkTodayTv;
    private Button startButton;
    private Button stopButton;

    private Intent intent;
    private long nextStepCount;
    private long timeElapsed;

    @Before
    public void setup() {
        super.setup();
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeActivity.class)
                    .putExtra(HomeActivity.FITNESS_SERVICE_KEY, TEST_SERVICE)
                    .putExtra(HomeActivity.HEIGHT_FT_KEY, FEET)
                    .putExtra(HomeActivity.HEIGHT_IN_KEY, INCHES);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        timeElapsed = 90_000;
    }

    @Test
    public void testHeightSaved() {
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
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
            getUIViews(activity);
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
            getUIViews(activity);
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
           getUIViews(activity);
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
            getUIViews(activity);
            startButton.performClick();
            stopButton.performClick();
            assertEquals(startButton.getVisibility(), View.VISIBLE);
            assertEquals(stopButton.getVisibility(), View.INVISIBLE);

            assertEquals(5842, (int) Integer.valueOf(latestStepsTv.getText().toString()));
            assertEquals(timeElapsedMinutes, Double.valueOf(latestTimeTv.getText().toString().split(" ")[0]), 0.1);
            assertEquals(expectedMiles, Double.valueOf(latestDistanceTv.getText().toString().split(" ")[0]), 0.1);
        });
    }

    @Test
    public void testDisableStopWalkWithToast() {
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIViews(activity);
            Intent mockIntent = getMockIntent();
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, mockIntent);
            startButton.performClick();
            assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MESSAGE);
            assertEquals(startButton.getVisibility(), View.INVISIBLE);
            assertEquals(stopButton.getVisibility(), View.VISIBLE);
            stopButton.performClick();
            assertEquals(startButton.getVisibility(), View.INVISIBLE);
            assertEquals(stopButton.getVisibility(), View.VISIBLE);
            assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MESSAGE);
        });
    }

    @Test
    public void testMockIncrementBeforeStarting() {
        nextStepCount = 1500;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIViews(activity);
            Intent mockIntent = getMockIntent();
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, mockIntent);
            assertEquals(1500, (int) Integer.valueOf(stepsTv.getText().toString()));
        });
    }

    @Test
    public void testMockedWalkStatsNoAddedSteps() {
        nextStepCount = 1500;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIViews(activity);
            Intent mockIntent = getMockIntent();
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, mockIntent);
            startButton.performClick();

            nextStepCount = 0;
            timeElapsed = 7_200_000;
            Intent endWalkIntent = new Intent(ApplicationProvider.getApplicationContext(), MockActivity.class)
                    .putExtra(MockActivity.ADDED_STEPS_KEY, 0)
                    .putExtra(MockActivity.INPUT_TIME_KEY, "9:20:00")
                    .putExtra(MockActivity.SETTING_START_TIME_KEY, false);
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, endWalkIntent);
            stopButton.performClick();
            assertEquals("0", latestStepsTv.getText().toString());
            assertEquals("0.00 mile(s)", latestDistanceTv.getText().toString());
            assertEquals("120.00 min.", latestTimeTv.getText().toString());
        });
    }

    @Test
    public void testMockedWalkStatsAddedSteps() {
        nextStepCount = 1500;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIViews(activity);
            Intent mockIntent = getMockIntent();
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, mockIntent);
            startButton.performClick();

            nextStepCount = 1500;
            timeElapsed = 7_200_000;
            Intent endWalkIntent = new Intent(ApplicationProvider.getApplicationContext(), MockActivity.class)
                    .putExtra(MockActivity.ADDED_STEPS_KEY, 1500)
                    .putExtra(MockActivity.INPUT_TIME_KEY, "9:20:00")
                    .putExtra(MockActivity.SETTING_START_TIME_KEY, false);
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, endWalkIntent);
            stopButton.performClick();
            assertEquals("1500", latestStepsTv.getText().toString());
            assertEquals("0.62 mile(s)", latestDistanceTv.getText().toString());
            assertEquals("120.00 min.", latestTimeTv.getText().toString());
        });
    }

    @Test
    public void testMockedWalkStatsNegativeWalkTime() {
        nextStepCount = 1500;
        ActivityScenario<HomeActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIViews(activity);
            Intent mockIntent = getMockIntent();
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, mockIntent);
            startButton.performClick();

            timeElapsed = 79_200_000;
            Intent endWalkIntent = new Intent(ApplicationProvider.getApplicationContext(), MockActivity.class)
                    .putExtra(MockActivity.ADDED_STEPS_KEY, 0)
                    .putExtra(MockActivity.INPUT_TIME_KEY, "5:20:00")
                    .putExtra(MockActivity.SETTING_START_TIME_KEY, false);
            activity.onActivityResult(MockActivity.REQUEST_CODE, Activity.RESULT_OK, endWalkIntent);
            stopButton.performClick();
            assertEquals("1320.00 min.", latestTimeTv.getText().toString());
        });
    }

    private void getUIViews(HomeActivity activity) {
        stepsTv = activity.findViewById(R.id.tv_daily_steps);
        distanceTv = activity.findViewById(R.id.tv_daily_distance);
        noWalkTodayTv = activity.findViewById(R.id.tv_no_recent_walk_prompt);
        latestDistanceTv = activity.findViewById(R.id.tv_recent_distance);
        latestStepsTv = activity.findViewById(R.id.tv_recent_steps);
        latestTimeTv = activity.findViewById(R.id.tv_recent_time_elapsed);
        startButton = activity.findViewById(R.id.btn_start_walk);
        stopButton = activity.findViewById(R.id.btn_stop_walk);
    }

    private Intent getMockIntent() {
        return new Intent(ApplicationProvider.getApplicationContext(), MockActivity.class)
                .putExtra(MockActivity.ADDED_STEPS_KEY, 1500)
                .putExtra(MockActivity.INPUT_TIME_KEY, "7:20:00")
                .putExtra(MockActivity.SETTING_START_TIME_KEY, true);
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
            activity.setLatestWalkStats(nextStepCount, timeElapsed);
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
