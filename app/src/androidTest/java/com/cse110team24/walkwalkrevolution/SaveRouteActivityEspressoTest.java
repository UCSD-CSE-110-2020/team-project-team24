package com.cse110team24.walkwalkrevolution;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
        appCompatEditText2.perform(replaceText("9"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_height_finish), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_start_walk), withText("Start Walk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_stop_walk), withText("Stop Walk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_save_this_route), withText("Save this route?"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                17),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_save_route_title), withText("Title: (this is REQUIRED)"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Title: (this is REQUIRED)")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tv_save_route_starting_location), withText("Starting Location:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("Starting Location:")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tv_route_type), withText("Route Type:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                4),
                        isDisplayed()));
        textView3.check(matches(withText("Route Type:")));

        ViewInteraction radioButton = onView(
                allOf(withId(R.id.radio_btn_loop),
                        childAtPosition(
                                allOf(withId(R.id.radiogroup_route_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                5)),
                                0),
                        isDisplayed()));
        radioButton.check(matches(isDisplayed()));

        ViewInteraction radioButton2 = onView(
                allOf(withId(R.id.radio_btn_out_back),
                        childAtPosition(
                                allOf(withId(R.id.radiogroup_route_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                5)),
                                1),
                        isDisplayed()));
        radioButton2.check(matches(isDisplayed()));

        ViewInteraction radioButton3 = onView(
                allOf(withId(R.id.rd_btn_flat),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_terrain_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                7)),
                                0),
                        isDisplayed()));
        radioButton3.check(matches(isDisplayed()));

        ViewInteraction radioButton4 = onView(
                allOf(withId(R.id.rd_btn_hilly),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_terrain_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                7)),
                                1),
                        isDisplayed()));
        radioButton4.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tv_terrain_type), withText("Terrain Type:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                6),
                        isDisplayed()));
        textView4.check(matches(withText("Terrain Type:")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv_surface_type), withText("Surface Type:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                8),
                        isDisplayed()));
        textView5.check(matches(withText("Surface Type:")));

        ViewInteraction radioButton5 = onView(
                allOf(withId(R.id.rd_btn_even),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_surface_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                9)),
                                0),
                        isDisplayed()));
        radioButton5.check(matches(isDisplayed()));

        ViewInteraction radioButton6 = onView(
                allOf(withId(R.id.rd_btn_uneven),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_surface_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                9)),
                                1),
                        isDisplayed()));
        radioButton6.check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.tv_land_type), withText("Land Type:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                10),
                        isDisplayed()));
        textView6.check(matches(withText("Land Type:")));

        ViewInteraction radioButton7 = onView(
                allOf(withId(R.id.rd_btn_street),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_land_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                11)),
                                0),
                        isDisplayed()));
        radioButton7.check(matches(isDisplayed()));

        ViewInteraction radioButton8 = onView(
                allOf(withId(R.id.rd_btn_trail),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_land_type),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                11)),
                                1),
                        isDisplayed()));
        radioButton8.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.tv_difficulty), withText("Difficulty:"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                12),
                        isDisplayed()));
        textView7.check(matches(withText("Difficulty:")));

        ViewInteraction radioButton9 = onView(
                allOf(withId(R.id.rd_btn_hard),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_difficulty),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                13)),
                                0),
                        isDisplayed()));
        radioButton9.check(matches(isDisplayed()));

        ViewInteraction radioButton10 = onView(
                allOf(withId(R.id.rd_btn_moderate),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_difficulty),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                13)),
                                1),
                        isDisplayed()));
        radioButton10.check(matches(isDisplayed()));

        ViewInteraction radioButton11 = onView(
                allOf(withId(R.id.rd_btn_easy),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_difficulty),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                13)),
                                2),
                        isDisplayed()));
        radioButton11.check(matches(isDisplayed()));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.tv_route_notes), withText("Notes"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                14),
                        isDisplayed()));
        textView8.check(matches(withText("Notes")));

        ViewInteraction textView9 = onView(
                allOf(withText("Save your walk"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                0),
                        isDisplayed()));
        textView9.check(matches(withText("Save your walk")));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_save_route_title),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("Marian Bear"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_save_route_location),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText4.perform(scrollTo(), replaceText("My house"), closeSoftKeyboard());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.radio_btn_loop), withText("Loop"),
                        childAtPosition(
                                allOf(withId(R.id.radiogroup_route_type),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                5)),
                                0)));
        appCompatRadioButton.perform(scrollTo(), click());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.rd_btn_hilly), withText("Hilly"),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_terrain_type),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                7)),
                                1)));
        appCompatRadioButton2.perform(scrollTo(), click());

        ViewInteraction appCompatRadioButton3 = onView(
                allOf(withId(R.id.rd_btn_uneven), withText("Uneven"),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_surface_type),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                9)),
                                1)));
        appCompatRadioButton3.perform(scrollTo(), click());

        ViewInteraction appCompatRadioButton4 = onView(
                allOf(withId(R.id.rd_btn_trail), withText("Trail"),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_land_type),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                11)),
                                1)));
        appCompatRadioButton4.perform(scrollTo(), click());

        ViewInteraction appCompatRadioButton5 = onView(
                allOf(withId(R.id.rd_btn_moderate), withText("Moderate"),
                        childAtPosition(
                                allOf(withId(R.id.rd_group_difficulty),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                13)),
                                1)));
        appCompatRadioButton5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_route_notes),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                15)));
        appCompatEditText5.perform(scrollTo(), replaceText("Had a relaxing walk"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.et_route_notes), withText("Had a relaxing walk"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                15)));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btn_save_route), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                16)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_routes_list), withContentDescription("Routes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.tv_route_name), withText("Marian Bear"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        0),
                                0),
                        isDisplayed()));
        textView10.check(matches(withText("Marian Bear")));
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
