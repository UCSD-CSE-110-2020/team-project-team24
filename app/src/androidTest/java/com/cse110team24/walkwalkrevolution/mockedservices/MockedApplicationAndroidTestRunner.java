package com.cse110team24.walkwalkrevolution.mockedservices;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;


/** TODO - add  testInstrumentationRunner 'com.cse110team24.walkwalkrevolution.mockedservices.MockedApplicationAndroidTestRunner'
 * to the gradle file
 * custom runner so we can control the instance of FirebaseApplicationWWR that is created
 *
 *
 */
public class MockedApplicationAndroidTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return super.newApplication(cl, FirebaseApplicationWWR.class.getName(), context);
    }
}
