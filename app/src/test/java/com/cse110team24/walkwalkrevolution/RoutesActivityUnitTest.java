package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.view.Menu;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RoutesActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private static final int FEET = 5;
    private static final float INCHES = 3f;
    private Intent intent;
    private long nextStepCount;
    private BottomNavigationView bottomNavigation;
   // private long timeElapsed;
   @Rule
   public final ActivityTestRule<RoutesActivity> activityTestRule =
           new ActivityTestRule<>(RoutesActivity.class);
    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeActivity.class)
                .putExtra(HomeActivity.FITNESS_SERVICE_KEY, TEST_SERVICE)
                .putExtra(HomeActivity.HEIGHT_FT_KEY, FEET)
                .putExtra(HomeActivity.HEIGHT_IN_KEY, INCHES);
        final RoutesActivity activity = activityTestRule.getActivity();
        bottomNavigation = activity.findViewById(R.id.bottom_navigation);
    }

    @Test
    public void menuNotNull () {
        final Menu menu = bottomNavigation.getMenu();
        assertNotNull("Menu should not be null", menu);
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