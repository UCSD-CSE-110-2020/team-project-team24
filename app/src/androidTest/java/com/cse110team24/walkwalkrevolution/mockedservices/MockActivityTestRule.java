package com.cse110team24.walkwalkrevolution.mockedservices;

import android.app.Activity;
import android.content.Context;

import androidx.test.rule.ActivityTestRule;

import com.cse110team24.walkwalkrevolution.HomeActivity;


/** TODO
 * new activity test rule to forcibly remove app data
 * Use this rule in your tests
 * use it like this [this will the instance variable for the testing class] :
 *      public MockActivityTestRule<LoginActivity> mActivityTestRule = new MockActivityTestRule<>(LoginActivity.class);
 * @param <T> the activity that first launches (LoginActivity.class)
 * if you need to change what happens before the activity is launched, feel free to extend this class
 *
 */
public class  MockActivityTestRule<T extends Activity> extends ActivityTestRule<T> {
    MockActivityTestRule(Class<T> activityClass) {
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
                .apply();
    }

}
