package com.cse110team24.walkwalkrevolution;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.mockedservices.MockActivityTestRule;
import com.cse110team24.walkwalkrevolution.mockedservices.TestAuth;
import com.cse110team24.walkwalkrevolution.mockedservices.TestUsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityUIEspressoTest {

    @Rule
    public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);

    @Before
    public void setup() {

        // todo you're mocking what the next of each of these will be
        TestAuth.isTestUserSignedIn = false;
        TestAuth.successUserSignedIn = true;
        TestAuth.testAuthUser = FirebaseUserAdapter.builder()
                .addDisplayName("Test")
                .addEmail("test@gmail.com")
                .addTeamUid("666")
                .addUid("1")
                .build();
    }

    // TODO: 3/5/20 apparently assertions for EditText types suck. They will most likely fail.
    @Test
    public void loginActivityUIEspressoTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.btn_height_finish), isDisplayed()));
            button.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.sign_up_tv), withText("Don't have an account? Sign up here"), isDisplayed()));
        textView.check(matches(withText("Don't have an account? Sign up here")));

    }
}
