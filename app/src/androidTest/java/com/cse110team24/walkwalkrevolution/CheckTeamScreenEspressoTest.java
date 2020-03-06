package com.cse110team24.walkwalkrevolution;


import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.mockedservices.TestMessage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService.testTeam;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckTeamScreenEspressoTest implements TeamsDatabaseServiceObserver {

    private List<IUser> listOfUsers;
//    ITeam teamList;

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE_KEY, activity -> new TestFitnessService(activity));
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE_KEY);
        TestAuth.isTestUserSignedIn = true;
        TestAuth.successUserSignedUp = true;
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        IUser satta_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Satta Momoh")
                .addEmail("amara@gmail.com")
                .addUid("1")
                .addTeamUid("666")
                .build();
       TestAuth.testAuthUser = satta_momoh;
        listOfUsers = new ArrayList<IUser>();
        testTeam = new TeamAdapter(listOfUsers);
//        List<IUser> team = testTeam.getTeam();
        testTeam.addMember(satta_momoh);
       TestDatabaseServiceFactory testDatabaseServiceFactory = new TestDatabaseServiceFactory();
       TestTeamsDatabaseService testTeamsDatabaseService = (TestTeamsDatabaseService) testDatabaseServiceFactory.createDatabaseService(DatabaseService.Service.TEAMS);
//       String teamID = testTeamsDatabaseService.createTeamInDatabase(satta_momoh);
       IUser amara_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Amara Momoh")
                .addEmail("ival@gmail.com")
                .addUid("2")
                .addTeamUid("666")
                .build();
        testTeam.addMember(amara_momoh);
        testTeamsDatabaseService.register(this);
       // Intent intent = new Intent(appContext, TeamActivity.class);

        testTeamsDatabaseService.mObserver.onTeamRetrieved(testTeam);
       // testTeamsDatabaseService.getUserTeam("666", "Satta Momoh");


//        testTeamsDatabaseService.addUserToTeam(amara_momoh, teamID);
//        TestTeamsDatabaseService.testTeam.addMember(satta_momoh);
//        TestTeamsDatabaseService.testTeam.addMember(amara_momoh);
    }

    @Test
    public void checkTeamScreenEspressoTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.sign_up_tv), withText("Don't have an account? Sign up here"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enter_gmail_address), isDisplayed()));
        appCompatEditText.perform(replaceText("amara@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enter_password), isDisplayed()));
        appCompatEditText2.perform(replaceText("pretzel"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.enter_username), isDisplayed()));
        appCompatEditText3.perform(replaceText("Satta Momoh"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_height_feet), isDisplayed()));
        appCompatEditText4.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_height_remainder_inches), isDisplayed()));
        appCompatEditText5.perform(replaceText("4"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_height_finish), withText("Sign Up"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.action_team), withContentDescription("Team"), isDisplayed()));
        bottomNavigationItemView.perform(click());

//        ViewInteraction listview = onView(withId(R.id.list_members_in_team)).check(matches(isDisplayed()));
        ViewInteraction listview = onView(allOf(withId(R.id.list_members_in_team), withContentDescription("Amara Momoh"), isDisplayed()));

    }

    @Override
    public void onTeamRetrieved(ITeam team) {

    }
}
