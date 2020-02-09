package com.cse110team24.walkwalkrevolution;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecordWalkEspressoTest {

    /**
     * new activity test rule to forcibly remove app data
     * @param <T>
     * notes: see https://stackoverflow.com/questions/37597080/reset-app-state-between-instrumentationtestcase-runs
     */
    class RecordWalkTestRule<T extends Activity> extends ActivityTestRule<T> {
        RecordWalkTestRule(Class<T> activityClass) {
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

    @Rule
    public RecordWalkTestRule<HeightActivity> mActivityTestRule = new RecordWalkTestRule(HeightActivity.class);

    private static final String TEST_SERVICE = "TEST_SERVICE";
    private long nextStepCount;
    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomeActivity activity) {
                return new RecordWalkEspressoTest.TestFitnessService(activity);
            }
        });
        SharedPreferences.Editor edit = mActivityTestRule.getActivity().getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE).edit();
        edit.putFloat(HomeActivity.HEIGHT_IN_KEY, -1f);
        edit.putInt(HomeActivity.HEIGHT_FT_KEY, -1);
        edit.commit();
        nextStepCount = 0;
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE);
    }

    @Test
    public void recordWalkEspressoTest() {
        setup();

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.height_feet_et),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.height_remainder_inches_et),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("3"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.finish_btn), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.startWalkButton), withText("Start Walk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.stopWalkButton), withText("Stop Walk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.totalStepsCounter), withText("5"), isDisplayed()));
        textView.check(matches(withText("5")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.totalDistanceCounter), withText("0.00 mile(s)"), isDisplayed()));
        textView2.check(matches(withText("0.00 mile(s)")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.timeElapsedCounter), withText("0.12 min."), isDisplayed()));
        textView3.check(matches(withText("0.12 min.")));
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
            activity.setLatestWalkStats(5, 7200);
        }

        @Override
        public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
            return new GoogleFitAdapter(null).getDistanceFromHeight(steps, heightFeet, heightRemainderInches);
        }
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
}
