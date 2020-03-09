package com.cse110team24.walkwalkrevolution;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.mockedservices.TestMessage;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService.TEST_SERVICE_KEY;
import static org.hamcrest.Matchers.allOf;

/**
Given that I have logged into my WWR account with a valid email address
And I want to invite a team member with a valid email address and the WWR app
When I click the “Team” button on the Home screen,
And click the “+” button on the team screen,
When I am prompted to enter the name and gmail address of the person I want to join my team,
And I click invite,
Then the sent invitation will be sent successfully.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class InviteTeamMemberBDDEspressoTest {
    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE_KEY, activity -> new TestFitnessService(activity));
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE_KEY);
        TestAuth.isTestUserSignedIn = true;
        TestAuth.successUserSignedIn = true;
        TestMessage.invitationSentSuccess = true;
        TestAuth.testAuthUser = FirebaseUserAdapter.builder()
                .addDisplayName("Amara Momoh")
                .addEmail("amara@gmail.com")
                .addUid("1")
                .addTeamUid("666")
                .build();

    }
    @Test
    public void loginActivitySignInEspressoTest() {
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
                allOf(withId(R.id.btn_team_activity_invite_team_members), withText("Invite New Teammate"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.field_enter_member_name), isDisplayed()));
        appCompatEditText6.perform(replaceText("Satta Momoh"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.field_enter_member_email), isDisplayed()));
        appCompatEditText7.perform(replaceText("amara@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_send_invite), withText("Send Invitation"), isDisplayed()));
        appCompatButton3.perform(click());

    }
}
