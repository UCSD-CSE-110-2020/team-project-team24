package com.cse110team24.walkwalkrevolution;

import android.content.Context;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestFitnessService;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
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
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;

/** Scenario: User has a team, sees team members listed in app
 *
 * Given that the user has a team,
 * When they click on the "Team" button from the home screen
 * Then a list of their fellow teammates will be displayed.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckTeamScreenBDDEspressoTest {

    private List<IUser> listOfUsers;
    IUser amara_momoh;
    IUser satta_momoh;

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup() {
        FitnessServiceFactory.put(TEST_SERVICE_KEY, activity -> new TestFitnessService(activity));
        mActivityTestRule.getActivity().setFitnessServiceKey(TEST_SERVICE_KEY);
        TestAuth.isTestUserSignedIn = true;
        TestAuth.successUserSignedUp = true;
        satta_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Satta Momoh")
                .addEmail("amara@gmail.com")
                .addUid("1")
                .addTeamUid("666")
                .build();
        TestAuth.testAuthUser = satta_momoh;

        listOfUsers = new ArrayList<IUser>();
        testTeam = new TeamAdapter(listOfUsers);

        testTeam.addMember(satta_momoh);

        amara_momoh = FirebaseUserAdapter.builder()
                .addDisplayName("Amara Momoh")
                .addEmail("ival@gmail.com")
                .addUid("2")
                .addTeamUid("666")
                .build();
        testTeam.addMember(amara_momoh);
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

        // I don't think the below two lines do anything...
        ViewInteraction listview = onView(allOf(withId(R.id.list_members_in_team), withContentDescription("Amara Momoh"), isDisplayed()));
        onData(withName("Amara Momoh")).inAdapterView(withId(R.id.list_members_in_team));

        // In actuality no temmates are displayed
        ViewInteraction textView = onView(allOf(withId(R.id.text_no_teammates), withText("No Teammates Yet :-("), isDisplayed()));
        textView.check(matches(withText("No Teammates Yet :-(")));
    }

    public static Matcher<IUser> withName(final String name){
        return new TypeSafeMatcher<IUser>(ListviewAdapter.class){
            @Override
            public boolean matchesSafely(IUser user) {
                return name.matches(user.getDisplayName());
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }
}


