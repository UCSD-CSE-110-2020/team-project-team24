package com.cse110team24.walkwalkrevolution;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cse110team24.walkwalkrevolution.activities.teams.InviteTeamToWalkActivity;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestInvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestMessage;
import com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestUsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalkStatus;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.Timestamp;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.CursorMatchers.withRowString;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService.testTeam;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ScheduleWalkFromProposedWalkEspressoTest {
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
        TestTeamsDatabaseService.testTeamRoutes.add(new Route.Builder("Title").addCreatorDisplayName("Emulator User").build());

        TestTeamsDatabaseService.testTeam = new TeamAdapter(new ArrayList<>());

        IUser userOne = FirebaseUserAdapter.builder()
                .addDisplayName("User 1")
                .addEmail("testOne@gmail.com")
                .addTeamUid("666")
                .addUid("1")
                .build();

        IUser userTwo = FirebaseUserAdapter.builder()
                .addDisplayName("User 2")
                .addEmail("testOne@gmail.com")
                .addTeamUid("666")
                .addUid("1")
                .build();

        TestTeamsDatabaseService.testTeam.addMember(userOne);
        TestTeamsDatabaseService.testTeam.addMember(userTwo);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = null;
        try {
            date = sdf.parse("04/20/2020 4:20 PM");
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, 3, 20, 16, 20);
            date = calendar.getTime();
            e.printStackTrace();
        }

        TestTeamsDatabaseService.testTeamStatuses = new TreeMap<>();
        TestTeamsDatabaseService.testTeamStatuses.put("User 1", "accepted the walk!");
        TestTeamsDatabaseService.testTeamStatuses.put("User 2", "accepted the walk!");

        TestTeamsDatabaseService.testTeamWalks = new ArrayList<>();

        TestTeamsDatabaseService.testTeamWalks.add(TeamWalk.builder()
                .addProposedRoute(new Route.Builder("Title").addCreatorDisplayName("Emulator User").build())
                .addProposedBy("Emulator User")
                .addProposedDateAndTime(new Timestamp(date))
                .build());
    }

    @Test
    public void scheduleWalkFromProposedWalk() {
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

        ViewInteraction appCompatButton1 = onView(
                allOf(withId(R.id.btn_scheduled_walks), withText("Scheduled and Proposed Walks"), isDisplayed()));
        appCompatButton1.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.schedule_propose_btn_schedule), withText("SCHEDULE"), isDisplayed()));
        appCompatButton2.perform(click());

        pressBack();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_scheduled_walks), withText("Scheduled and Proposed Walks"), isDisplayed()));
        appCompatButton3.perform(click());

        onView(withId(R.id.schedule_propose_tv_walk_date)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.schedule_propose_linear_layout_status_buttons)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withId(R.id.list_members_with_status)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}

