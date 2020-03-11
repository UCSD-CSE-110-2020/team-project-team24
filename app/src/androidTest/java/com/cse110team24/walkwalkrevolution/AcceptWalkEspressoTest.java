package com.cse110team24.walkwalkrevolution;


import android.content.Intent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.internal.inject.InstrumentationContext;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestUsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalkStatus;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AcceptWalkEspressoTest {

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup(){
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
        TestTeamsDatabaseService.testTeamRoutes.add(new Route.Builder("Catwalk").addCreatorDisplayName("Ass Face").build());

        TestTeamsDatabaseService.testTeam = new TeamAdapter(new ArrayList<>());

        TestTeamsDatabaseService.testTeamWalks = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = null;
        try {
            date = sdf.parse("12/10/2099 12:00 PM");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TeamWalk teamWalk = new TeamWalk(new Route.Builder("Catwalk").addCreatorDisplayName("Ass Face").build(), "Ass Face", new Timestamp(date));
        teamWalk.setStatus(TeamWalkStatus.PROPOSED);
        TestTeamsDatabaseService.testTeamWalks.add(teamWalk);

        TestTeamsDatabaseService.testTeamStatuses = new TreeMap<>();
        TestTeamsDatabaseService.testTeamStatuses.put("User 1", "declined the walk due to a scheduling conflict");
        TestTeamsDatabaseService.testTeamStatuses.put("User 2", "accepted the walk!");

    }

    @Test
    public void acceptWalkEspressoTest() {
        setup();
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enter_gmail_address), isDisplayed()));
        appCompatEditText.perform(replaceText("jose@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enter_password), isDisplayed()));
        appCompatEditText2.perform(replaceText("1234jam"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_height_feet), isDisplayed()));
        appCompatEditText4.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_height_remainder_inches), isDisplayed()));
        appCompatEditText5.perform(replaceText("7"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_height_finish), withText("Login"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_team), withContentDescription("Team"), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_scheduled_walks), withText("Scheduled and Proposed Walks"), isDisplayed()));
        appCompatButton2.perform(click());


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.schedule_propose_btn_accept), withText("Accept"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.schedule_propose_tv_proposed_by_display), withText("Ass Face"), isDisplayed()));
        textView2.check(matches(withText("Ass Face")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.schedule_propose_tv_walk_name_prompt), withText("Name of walk:"), isDisplayed()));
        textView3.check(matches(withText("Name of walk:")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.schedule_propose_tv_walk_name_display), withText("Catwalk"), isDisplayed()));
        textView4.check(matches(withText("Catwalk")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.schedule_propose_tv_starting_loc), withText("Starting location:"), isDisplayed()));
        textView5.check(matches(withText("Starting location:")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.schedule_propose_tv_starting_loc_display), withHint("(unspecified)"), isDisplayed()));
        textView6.check(matches(withHint("(unspecified)")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.schedule_propose_tv_walk_date), withText("Proposed Date and Time:"), isDisplayed()));
        textView7.check(matches(withText("Proposed Date and Time:")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.schedule_propose_tv_walk_date_display), withText("12/10/2099 at 12:00 PM"), isDisplayed()));
        textView8.check(matches(withText("12/10/2099 at 12:00 PM")));

        ViewInteraction button = onView(
                allOf(withId(R.id.schedule_propose_btn_accept), isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.schedule_propose_btn_decline_cant_come), isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.schedule_propose_btn_decline_not_interested), isDisplayed()));
        button3.check(matches(isDisplayed()));
    }
}
