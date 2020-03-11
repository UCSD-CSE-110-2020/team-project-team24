package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static com.cse110team24.walkwalkrevolution.HomeActivity.APP_PREF;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(AndroidJUnit4.class)
public class StatsOnAnyRouteUnitTest extends TestInjection {
    private TeamsDatabaseServiceObserver observer;
    private List<Route> teamRoutesList;
    private ActivityScenario<TeamRoutesActivity> teamRoutesScenario;

    @Before
    public void setup() {
        super.setup();
        SharedPreferences sp = ApplicationProvider.getApplicationContext().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.TEAM_UID_KEY, testUser.teamUid())
                .commit();

        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        Mockito.doAnswer(invocation -> {
            observer = invocation.getArgument(0);
            return invocation;
        }).when(teamsDatabaseService).register(any());
        teamRoutesList = new ArrayList<>();
    }

    @Test
    public void emtpyTest() {
        launchTeamRoutesActivity();
        teamRoutesScenario.onActivity(activity -> {

        });
    }

    private void launchTeamRoutesActivity() {
        Mockito.doAnswer(invocation -> {
            ((TeamsRoutesObserver)observer).onRoutesRetrieved(teamRoutesList, null);
            return invocation;
        }).when(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
        teamRoutesScenario = ActivityScenario.launch(TeamRoutesActivity.class);
    }
}
