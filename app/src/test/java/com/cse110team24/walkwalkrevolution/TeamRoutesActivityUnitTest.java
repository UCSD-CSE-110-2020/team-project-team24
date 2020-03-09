package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class TeamRoutesActivityUnitTest extends TestInjection {
    private TeamsDatabaseServiceObserver observer;
    private ActivityScenario<TeamRoutesActivity> mActivityScenario;

    private RecyclerView teamRoutesRv;

    private List<Route> testTeamRoutes;

    private SharedPreferences mPreferences;

    @Before
    public void setup() {
        super.setup();
        getObserver();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        mPreferences = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        mPreferences
                .edit()
                .putString(IUser.TEAM_UID_KEY, "666")
                .putString(IUser.USER_NAME_KEY, "test")
                .commit();
        testTeamRoutes = new ArrayList<>();
    }

    @Test
    public void testTeamRoutes_verifyGetsTeamRoutes() {
        setup();
        answerWithTeamRoutes(null);
        mActivityScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        mActivityScenario.onActivity(activity -> {
            getTeamRoutesActivityUIElements(activity);
            Mockito.verify(dsf).createDatabaseService(DatabaseService.Service.TEAMS);
            Mockito.verify(teamsDatabaseService).register(any());
            Mockito.verify(teamsDatabaseService).getUserTeamRoutes(eq("666"), eq("test"), eq(10), any());
        });
    }

    private void answerWithTeamRoutes(DocumentSnapshot doc) {
        printIfNull("teams database service", teamsDatabaseService);
        Mockito.doAnswer(invocation -> {
            ((TeamsRoutesObserver )observer).onRoutesRetrieved(testTeamRoutes, doc);
            return invocation;
        }).when(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
    }

    private void printIfNull(String tag, Object o) {
        System.out.print("WWR_SeeTeamRoutesUnitTest: " + tag + " is ");
        if (o == null) {
            System.out.println("null");
        } else {
            System.out.println("not null");
        }
    }

    private void getObserver() {
        Mockito.doAnswer(invocation -> {
            observer = invocation.getArgument(0);
            return invocation;
        }).when(teamsDatabaseService).register(any());
    }

    private void getTeamRoutesActivityUIElements(TeamRoutesActivity activity) {
        teamRoutesRv = activity.findViewById(R.id.recycler_view_team_routes);
    }
}
