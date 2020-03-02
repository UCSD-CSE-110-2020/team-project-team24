package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingServiceFactory;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.ArrayList;
import java.util.List;

public class MockActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

    public static class Runner extends AndroidJUnitRunner {
        @Override
        public Application newApplication(ClassLoader cl, String className, Context context) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
            return super.newApplication(cl, FirebaseApplicationWWR.class.getName(), context);
        }
    }

    protected AuthServiceFactory asf;
    protected DatabaseServiceFactory dsf;
    protected TestInjection testInjection;

    MockActivityTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected Intent getActivityIntent() {

        return super.getActivityIntent();
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        testInjection = new TestInjection();
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE)
                .edit()
                .remove(HomeActivity.HEIGHT_FT_KEY)
                .remove(HomeActivity.HEIGHT_IN_KEY)
                .apply();
    }


}
