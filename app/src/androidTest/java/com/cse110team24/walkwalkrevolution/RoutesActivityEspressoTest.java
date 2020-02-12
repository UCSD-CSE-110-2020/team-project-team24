package com.cse110team24.walkwalkrevolution;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;
import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RoutesActivityEspressoTest {


    /**
     * new activity test rule to forcibly remove app data
     * @param <T>
     * notes: see https://stackoverflow.com/questions/37597080/reset-app-state-between-instrumentationtestcase-runs
     */
    class RoutesActivityTestRule<T extends Activity> extends ActivityTestRule<T> {
        RoutesActivityTestRule(Class<T> activityClass) {
            super(activityClass);
        }

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext()
                    .getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE)
                    .edit()
                    .remove(HomeActivity.HEIGHT_FT_KEY)
                    .remove(HomeActivity.HEIGHT_IN_KEY)
                    .apply();
        }

    }

    private static final String TEST_SERVICE = "TEST_SERVICE";
    @Rule
    public RoutesActivityTestRule<HeightActivity> mActivityTestRule = new RoutesActivityTestRule<>(HeightActivity.class);
    private long nextStepCount;
    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomeActivity activity) {
                return new TestFitnessService(activity);
            }
        });
        SharedPreferences.Editor edit = mActivityTestRule.getActivity().getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE).edit();
        edit.putFloat(HomeActivity.HEIGHT_IN_KEY, -1f);
        edit.putInt(HomeActivity.HEIGHT_FT_KEY, -1);
        edit.commit();
        nextStepCount = 0;
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);

        Route routeUno = new Route("CSE Building");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,5,6);
        WalkStats stats = new WalkStats(1000, 90_000_000, 1.5,  calendar);
        Route routeDos = new Route("ECE Building")
                .setStartingLocation("ECE Makerspace")
                .setFavorite(true)
                .setStats(stats);
        calendar = Calendar.getInstance();
        calendar.set(2019, 1, 11);
        stats = new WalkStats(500, 90_000, 2.0, calendar);
        Route routTres = new Route("Center Hall")
                .setFavorite(false)
                .setStartingLocation("Tu madre")
                .setStats(stats);

        List<Route> routes = new ArrayList<>();
        routes.add(routeUno);
        routes.add(routeDos);
        routes.add(routTres);
        try {
            RoutesManager.writeList(routes, RoutesActivity.LIST_SAVE_FILE, InstrumentationRegistry.getInstrumentation().getTargetContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void routesActivityEspressoTest() {
        setup();

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_height_feet),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_height_remainder_inches),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("3"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_height_finish), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_routes_list), withContentDescription("Routes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_route_name), withText("CSE Building"), isDisplayed()));
        textView.check(matches(withText("CSE Building")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tv_route_name), withText("Center Hall"), isDisplayed()));
        textView2.check(matches(withText("Center Hall")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tv_route_name), withText("ECE Building"), isDisplayed()));
        textView3.check(matches(withText("ECE Building")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tv_routes_steps), withText("1000 steps"), isDisplayed()));
        textView4.check(matches(withText("1000 steps")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv_routes_distance), withText("1.50 mile(s)"), isDisplayed()));
        textView5.check(matches(withText("1.50 mile(s)")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.tv_routes_date_completed), withText("06/06"), isDisplayed()));
        textView6.check(matches(withText("06/06")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
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
            return new GoogleFitAdapter(null).getDistanceFromHeight(steps, heightFeet, heightRemainderInches);
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
