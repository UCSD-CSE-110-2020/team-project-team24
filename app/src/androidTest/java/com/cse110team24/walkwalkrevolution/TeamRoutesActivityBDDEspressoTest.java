package com.cse110team24.walkwalkrevolution;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthAdapter;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestUsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static org.hamcrest.Matchers.allOf;

/**
 Given that I login successfully,
 and click "Team",
 When click I click "See Teammate Routes ",
 Then I will see all my teammates' routes ordered by name of route,
 and each route will have my teammate's initials with a unique color
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TeamRoutesActivityBDDEspressoTest {

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE_KEY, activity -> new TestFitnessService(activity));
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE_KEY);
        TestAuth.isTestUserSignedIn = true;
        TestAuth.successUserSignedIn = true;

        TestUsersDatabaseService.testCurrentUserData = new HashMap<>();
        TestUsersDatabaseService.testCurrentUserData.put("displayName", "Emulator User");
        TestUsersDatabaseService.testCurrentUserData.put("email", "emulator@gmail.com");
        TestUsersDatabaseService.testCurrentUserData.put("teamUid", "666");
        TestAuth.testAuthUser = FirebaseUserAdapter.builder()
                .addDisplayName("Emulator User")
                .addEmail("emulator@gmail.com")
                .addTeamUid("666")
                .build();

        TestTeamsDatabaseService.testTeamRoutes = new ArrayList<>();
        TestTeamsDatabaseService.testTeamRoutes.add(new Route.Builder("Title").addCreatorDisplayName("Ass Face").build());

        TestTeamsDatabaseService.testTeam = new TeamAdapter(new ArrayList<>());
    }

    @Test
    public void teamRoutesActivityBDDEspressoTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enter_gmail_address),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("emulator@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enter_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("testpw"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.enter_password), withText("testpw"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_height_feet),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_height_remainder_inches),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("3"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_height_finish), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_team), withContentDescription("Team"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_team_activity_see_teammate_routes), withText("See Teammate Routes"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_team_routes_initials), withText("AF"), isDisplayed()));
        textView.check(matches(withText("AF")));
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
