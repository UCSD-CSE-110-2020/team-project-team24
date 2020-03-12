package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import static com.cse110team24.walkwalkrevolution.HomeActivity.APP_PREF;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class StatsOnAnyRouteUnitTest extends TestInjection {
    private TeamsDatabaseServiceObserver observer;
    private List<Route> teamRoutesList;
    private ActivityScenario<TeamRoutesActivity> teamRoutesScenario;
    SharedPreferences sp;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
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
    public void testCurrentUserCompletedTeammateRoute_currUserStatsAreDisplayed() {
        teamRoutesList.clear();
        setupTest();
        launchTeamRoutesActivity();
        teamRoutesScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        teamRoutesScenario.onActivity(activity -> {
            getRecyclerViewCells(activity);
            TestCase.assertEquals("666 steps", firstStepsTv.getText().toString());
        });
    }

    @Test
    public void testCurrentUserCompletedTeammateRoute_diffRouteHasCorrectStats() {
        teamRoutesList.clear();
        setupTest();
        launchTeamRoutesActivity();
        teamRoutesScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        teamRoutesScenario.onActivity(activity -> {
            getRecyclerViewCells(activity);
            TestCase.assertEquals("89 steps", secondStepsTv.getText().toString());
        });
    }

    private void setupTest() {
        setTestTeammateRoute();
        teamRoutesList.add(testTeamRouteCompletedByUser);
        teamRoutesList.add(testTeamRouteNotCompletedByUser);

        setCurrentUserRoute();
        saveTeamRouteForUser(testCurrentUserRoute);
    }

    Route testCurrentUserRoute;
    WalkStats testCurrentUserStats;
    private void setCurrentUserRoute() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, 1, 1);
        testCurrentUserStats = WalkStats.builder()
                .addSteps(666)
                .addDateCompleted(calendar)
                .addTimeElapsed(666)
                .addDistance(6.6)
                .build();
        testCurrentUserRoute = new Route.Builder("A Test Route")
                .addRouteUid("1")
                .addCreatorDisplayName("Teammate")
                .addWalkStats(testCurrentUserStats)
                .build();
    }

    WalkStats testTeammateStats;
    Route testTeamRouteCompletedByUser;
    Route testTeamRouteNotCompletedByUser;
    private void setTestTeammateRoute() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, 1, 1);
         testTeammateStats = WalkStats.builder()
                .addSteps(89)
                .addDateCompleted(calendar)
                .addTimeElapsed(89)
                .addDistance(5.5)
                .build();
         testTeamRouteCompletedByUser = new Route.Builder("A Test Route")
                .addRouteUid("1")
                .addCreatorDisplayName("Teammate")
                .addWalkStats(testTeammateStats)
                .build();

        testTeamRouteNotCompletedByUser = new Route.Builder("Other Route")
                .addRouteUid("6")
                .addCreatorDisplayName("Teammate")
                .addWalkStats(testTeammateStats)
                .build();
    }

    private void launchTeamRoutesActivity() {
        Mockito.doAnswer(invocation -> {
            ((TeamsRoutesObserver) observer).onRoutesRetrieved(teamRoutesList, null);
            return invocation;
        }).when(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
    }

    private void saveTeamRouteForUser(Route route) {
        try {
            RoutesManager.writeSingle(route, route.getRouteUid(),  ApplicationProvider.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextView firstStepsTv;
    private TextView secondStepsTv;
    private void getRecyclerViewCells(Activity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view_team_routes);
        View firstView = recyclerView.getLayoutManager().findViewByPosition(0);
        View secondView = recyclerView.getLayoutManager().findViewByPosition(1);

        firstStepsTv = firstView.findViewById(R.id.tv_routes_steps);
        secondStepsTv = secondView.findViewById(R.id.tv_routes_steps);
    }
}
