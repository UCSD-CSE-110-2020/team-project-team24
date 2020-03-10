package com.cse110team24.walkwalkrevolution;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.espresso.ViewInteraction;
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
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService.testTeam;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ProposeWalkFromPersonalRoutesEspressoTest {

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);
    //public MockActivityTestRule<InviteTeamToWalkActivity> mActivityInviteTeamToWalk = new MockActivityTestRule<>(InviteTeamToWalkActivity.class);

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
    public void proposeWalkFromPersonalRoutes() {
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
                allOf(withId(R.id.action_routes_list), withContentDescription("Routes"), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction activityRoutesMenu = onView(
                allOf(withId(R.id.action_create_walk), withContentDescription("Create Route"), isDisplayed()));
        activityRoutesMenu.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.et_save_route_title), isDisplayed()));
        appCompatEditText6.perform(replaceText("Lala Land"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_save_route), withText("SAVE"), isDisplayed()));

        onView(withId(R.id.scroll_view_save))
                .perform(swipeUp());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        appCompatButton2.perform(click());
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}

        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));

        ViewInteraction activityDetailsMenu = onView(
                allOf(withId(R.id.action_propose_walk), withContentDescription("Propose to Team"), isDisplayed()));
        activityDetailsMenu.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.et_proposed_day_invite_team_to_walk_activity), isDisplayed()));
        appCompatEditText7.perform(replaceText("03/13/2099"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.et_proposed_time_invite_team_to_walk_activity), isDisplayed()));
        appCompatEditText8.perform(replaceText("11:59"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_send_invitation_to_team), withText("Send to Team"), isDisplayed()));
        appCompatButton3.perform(click());

        //Making sure that toast still exists.
        /*try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Invitation sent")).
                inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));*/

    }
}
