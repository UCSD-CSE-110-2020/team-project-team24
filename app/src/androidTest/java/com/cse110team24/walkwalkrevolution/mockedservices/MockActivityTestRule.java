package com.cse110team24.walkwalkrevolution.mockedservices;

import android.app.Activity;
import android.content.Context;

import androidx.test.rule.ActivityTestRule;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.models.user.IUser;


/** TODO
 * new activity test rule to forcibly remove app data
 * Use this rule in your tests
 * use it like this [this will the instance variable for the testing class] :
 *      [@rule] - no brackets
 *      public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);
 * if you need to change what happens before the activity is launched, feel free to extend this class
 *
 */
public class  MockActivityTestRule<LoginActivity extends Activity> extends ActivityTestRule<LoginActivity> {

    FirebaseApplicationWWR testApplicationWWR;
    public MockActivityTestRule(Class<LoginActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE)
                .edit()
                .remove(HomeActivity.HEIGHT_FT_KEY)
                .remove(HomeActivity.HEIGHT_IN_KEY)
                .remove(IUser.TEAM_UID_KEY)
                .remove(IUser.USER_NAME_KEY)
                .remove(IUser.EMAIL_KEY)
                .apply();
        FirebaseApplicationWWR.setAuthServiceFactory(new TestAuth.TestAuthFactory());
        FirebaseApplicationWWR.setDatabaseServiceFactory(new TestDatabaseServiceFactory());
        FirebaseApplicationWWR.setMessagingServiceFactory(new TestMessage.TestMessagingFactory());
    }

}
