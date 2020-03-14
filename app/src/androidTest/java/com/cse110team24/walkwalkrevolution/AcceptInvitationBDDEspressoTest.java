package com.cse110team24.walkwalkrevolution;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestInvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestMessage;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService.testTeam;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestTeamsDatabaseService.testTeamUid;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 Given that I login successfully,
 and click "Team",
 When click I click "Pending Invites",
 Then I will see all my invitations,
 When I click on one of them,
 and accept,
 Then I will see my new list of teammates in Team Screen.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AcceptInvitationBDDEspressoTest {

    private List<IUser> listOfUsers;
    private List<Invitation> invitationList;
    private IUser amara_momoh;
    private IUser satta_momoh;

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE_KEY, activity -> new TestFitnessService(activity));
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE_KEY);
        TestAuth.isTestUserSignedIn = true;
        TestAuth.successUserSignedIn = true;
        TestMessage.invitationSentSuccess = true;

        //ME
        satta_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Amara Momoh")
                .addEmail("amara@gmail.com")
                .addUid("1")
                .build();

        TestAuth.testAuthUser = satta_momoh;

        listOfUsers = new ArrayList<IUser>();
        invitationList = new ArrayList<Invitation>();
        testTeam = new TeamAdapter(listOfUsers);
        testTeamUid = testTeam.getUid();

//PERSON INVITING ME
        amara_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Amara Momoh")
                .addEmail("ival@gmail.com")
                .addUid("2")
                .addTeamUid("666")
                .build();
        testTeam.addMember(amara_momoh);

        Invitation sentInvitation = new Invitation(amara_momoh);
        sentInvitation = Invitation.builder().addFromUser(amara_momoh).addTeamUid("666").build();

        invitationList.add(sentInvitation);

        IUser maria_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Maria Momoh")
                .addEmail("mars@gmail.com")
                .addUid("3")
                .addTeamUid("666")
                .build();
        testTeam.addMember(maria_momoh);

        TestInvitationsDatabaseService.testInvitationsList = invitationList;

    }
    @Test
    public void loginActivitySignInEspressoTest() {
        setup();

        assertThat(testTeamUid, nullValue());

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
                allOf(withId(R.id.btn_team_activity_pending_invites), withText("Pending Invitations"), isDisplayed()));
        appCompatButton2.perform(click());

        onData(anything()).inAdapterView((withId(R.id.invitationList))).atPosition(0).perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonAccept), withText("Accept"), isDisplayed()));
        appCompatButton3.perform(click());

        pressBack();

        onView(withId(R.id.text_no_teammates)).check(matches(not(isDisplayed())));

        testTeamUid = amara_momoh.teamUid();
        assertThat(testTeamUid, notNullValue());

    }
}
