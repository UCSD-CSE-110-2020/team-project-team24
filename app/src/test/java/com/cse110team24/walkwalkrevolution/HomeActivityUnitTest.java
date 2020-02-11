package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

@RunWith(AndroidJUnit4.class)
public class HomeActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final int FEET = 5;
    private static final float INCHES = 3f;

    private Intent intent;
    private long nextStepCount;

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
        public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
            return new GoogleFitAdapter(activity).getDistanceFromHeight(steps, heightFeet, heightRemainderInches);
        }
    }
}
