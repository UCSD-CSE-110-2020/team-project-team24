package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.SaveRouteActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(AndroidJUnit4.class)
public class ScheduledWalkActivityUnitTest extends TestInjection {

    ActivityScenario<ScheduledWalkActivity> scenario;
    SharedPreferences sp;
    Button location_btn;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
    }

    private void getUIFields(Activity activity) {
        location_btn = activity.findViewById(R.id.btn_location);
    }

    @Test
    public void launchGoogleMaps() {
        scenario = ActivityScenario.launch(ScheduledWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            location_btn.performClick();

            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();

            assertEquals(actualIntent.getAction(), Intent.ACTION_VIEW);
            assertEquals(actualIntent.getData().toString(), "http://maps.google.co.in/maps?q=" + activity.getStartingLoc());
        });
    }

}