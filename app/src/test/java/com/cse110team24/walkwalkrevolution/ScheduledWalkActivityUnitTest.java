package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.activities.teams.ScheduledProposedWalkActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamWalksObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class ScheduledWalkActivityUnitTest extends TestInjection {

    ActivityScenario<ScheduledProposedWalkActivity> scenario;
    SharedPreferences sp;
    TextView location_btn;
    TeamsTeamWalksObserver observer;
    List<TeamWalk> listWithLoc;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        Route routeWithLoc = new Route.Builder("route with location")
                        .addStartingLocation("Center Hall")
                        .build();
        listWithLoc = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date date = null;
        try {
            date = sdf.parse("12/10/2099 12:00 PM");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date);
        TeamWalk teamWalk = new TeamWalk.Builder()
                                .addProposedBy("user")
                                .addProposedRoute(routeWithLoc)
                                .addProposedDateAndTime(timestamp)
                                .build();
        listWithLoc.add(teamWalk);
    }

    private void getUIFields(Activity activity) {
        location_btn = activity.findViewById(R.id.schedule_propose_tv_starting_loc_display);
    }

    private void registerObserver() {
        Mockito.doAnswer(invocation -> {
            observer = invocation.getArgument(0);
            return invocation;
        }).when(teamsDatabaseService).register(any());
    }

    @Test
    public void launchGoogleMapsWithLocation() {
        registerObserver();
        Mockito.doAnswer(invocation -> {
            observer.onTeamWalksRetrieved(listWithLoc);
            return null;
        }).when(teamsDatabaseService).getLatestTeamWalksDescendingOrder(any(), eq(1));

        scenario = ActivityScenario.launch(ScheduledProposedWalkActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).register(any());
            Mockito.verify(teamsDatabaseService).getLatestTeamWalksDescendingOrder(any(), eq(1));
            getUIFields(activity);
            location_btn.performClick();

            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();

            assertEquals(actualIntent.getAction(), Intent.ACTION_VIEW);
            assertEquals(actualIntent.getData().toString(), "http://maps.google.co.in/maps?q=" + location_btn.getText().toString());
        });
    }

}