package com.cse110team24.walkwalkrevolution;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SaveRouteActivityEspressoTest {

    /**
     * new activity test rule to forcibly remove app data
     * @param <T>
     * notes: see https://stackoverflow.com/questions/37597080/reset-app-state-between-instrumentationtestcase-runs
     */
    class SaveRouteActivityTestRule<T extends Activity> extends ActivityTestRule<T> {
        SaveRouteActivityTestRule(Class<T> activityClass) {
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
    public SaveRouteActivityTestRule<HeightActivity> mActivityTestRule = new SaveRouteActivityTestRule<>(HeightActivity.class);

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
    }

    @Test
    public void saveRouteActivityEspressoTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_height_feet), isDisplayed()));
        appCompatEditText.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_height_remainder_inches), isDisplayed()));
        appCompatEditText2.perform(replaceText("9"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_height_finish), withText("Finish"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_start_walk), withText("Start Walk"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_stop_walk), withText("Stop Walk"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_save_this_route), withText("Save this route?"), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_save_route_title), withText("Title: (this is REQUIRED)"), isDisplayed()));
        textView.check(matches(withText("Title: (this is REQUIRED)")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tv_save_route_starting_location), withText("Starting Location:"), isDisplayed()));
        textView2.check(matches(withText("Starting Location:")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tv_route_type), withText("Route Type:"), isDisplayed()));
        textView3.check(matches(withText("Route Type:")));

        ViewInteraction radioButton = onView(
                allOf(withId(R.id.radio_btn_loop), isDisplayed()));
        radioButton.check(matches(isDisplayed()));

        ViewInteraction radioButton2 = onView(
                allOf(withId(R.id.radio_btn_out_back), isDisplayed()));
        radioButton2.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tv_terrain_type), withText("Terrain Type:"), isDisplayed()));
        textView4.check(matches(withText("Terrain Type:")));

        ViewInteraction radioButton3 = onView(
                allOf(withId(R.id.rd_btn_flat), isDisplayed()));
        radioButton3.check(matches(isDisplayed()));

        ViewInteraction radioButton4 = onView(
                allOf(withId(R.id.rd_btn_hilly), isDisplayed()));
        radioButton4.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv_surface_type), withText("Surface Type:"), isDisplayed()));
        textView5.check(matches(withText("Surface Type:")));

        ViewInteraction radioButton5 = onView(
                allOf(withId(R.id.rd_btn_even), isDisplayed()));
        radioButton5.check(matches(isDisplayed()));

        ViewInteraction radioButton6 = onView(
                allOf(withId(R.id.rd_btn_uneven), isDisplayed()));
        radioButton6.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.tv_land_type), withText("Land Type:"), isDisplayed()));
        textView6.check(matches(withText("Land Type:")));

        ViewInteraction radioButton7 = onView(
                allOf(withId(R.id.rd_btn_street), isDisplayed()));
        radioButton7.check(matches(isDisplayed()));

        ViewInteraction radioButton8 = onView(
                allOf(withId(R.id.rd_btn_trail), isDisplayed()));
        radioButton8.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.tv_difficulty), withText("Difficulty:"), isDisplayed()));
        textView7.check(matches(withText("Difficulty:")));

        ViewInteraction radioButton9 = onView(
                allOf(withId(R.id.rd_btn_hard), isDisplayed()));
        radioButton9.check(matches(isDisplayed()));

        ViewInteraction radioButton10 = onView(
                allOf(withId(R.id.rd_btn_moderate), isDisplayed()));
        radioButton10.check(matches(isDisplayed()));

        ViewInteraction radioButton11 = onView(
                allOf(withId(R.id.rd_btn_easy), isDisplayed()));
        radioButton11.check(matches(isDisplayed()));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.tv_route_notes), withText("Notes"), isDisplayed()));
        textView8.check(matches(withText("Notes")));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_save_route_title), isDisplayed()));
        appCompatEditText3.perform(replaceText("Marian Bear"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_save_route_location), isDisplayed()));
        appCompatEditText4.perform(replaceText("My house"), closeSoftKeyboard());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.radio_btn_loop), withText("Loop"), isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.rd_btn_hilly), withText("Hilly"), isDisplayed()));
        appCompatRadioButton2.perform(click());

        ViewInteraction appCompatRadioButton3 = onView(
                allOf(withId(R.id.rd_btn_uneven), withText("Uneven"), isDisplayed()));
        appCompatRadioButton3.perform(click());

        ViewInteraction appCompatRadioButton4 = onView(
                allOf(withId(R.id.rd_btn_trail), withText("Trail"), isDisplayed()));
        appCompatRadioButton4.perform(click());

        ViewInteraction appCompatRadioButton5 = onView(
                allOf(withId(R.id.rd_btn_moderate), withText("Moderate"), isDisplayed()));
        appCompatRadioButton5.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_route_notes), isDisplayed()));
        appCompatEditText5.perform(replaceText("Nice"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.et_route_notes), withText("Nice"), isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btn_save_route), withText("SAVE"), isDisplayed()));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_routes_list), withContentDescription("Routes"), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.tv_route_name), withText("Marian Bear"), isDisplayed()));
        textView9.check(matches(withText("Marian Bear")));
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
