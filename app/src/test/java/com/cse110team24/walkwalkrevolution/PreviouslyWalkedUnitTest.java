package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RoutesActivity;
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
import androidx.test.platform.app.InstrumentationRegistry;

import static com.cse110team24.walkwalkrevolution.HomeActivity.APP_PREF;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class PreviouslyWalkedUnitTest extends TestInjection {

    private Route routeUno, routeDos, routeTres;
    private TextView routesFirstCheckmark, routesSecondCheckmark, routesThirdCheckmark;
    private TeamsDatabaseServiceObserver observer;
    private ActivityScenario<TeamRoutesActivity> teamRoutesScenario;
    private ActivityScenario<RoutesActivity> routesScenario;
    private ActivityScenario<RouteDetailsActivity> routeDetailScenario;

    public static final String TEST_SAVE_FILE = ".WWR_test_save_file";

    private List<Route> routesList;
    private List<Route> teamRoutesList;
    private Context context;

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
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        routesList = getListOfRoutes(true);
        teamRoutesList = getListOfRoutes(false);
    }

    @Test
    public void testYourRouteCheckMark_youHaveStats()  {
        launchRoutesActivity();
        routesScenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(View.VISIBLE, routesSecondCheckmark.getVisibility());
            assertEquals(View.VISIBLE, routesThirdCheckmark.getVisibility());
        });

        Route route = routesList.get(1);
        launchRouteDetailsActivity(route, 2);
        routeDetailScenario.onActivity(activity -> {
            TextView checkMark = activity.findViewById(R.id.tv_previously_walked_checkmark);
            assertEquals(View.VISIBLE, checkMark.getVisibility());
        });
    }

    @Test
    public void testYourRouteNoCheckMark_youNoStats() {
        launchRoutesActivity();
        routesScenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(View.INVISIBLE, routesFirstCheckmark.getVisibility());
        });
    }

    @Test
    public void testTeamRouteCheckMark_teamAndYouHaveStats() {
        try {
            RoutesManager.writeSingle(routeDos, routeDos.getRouteUid(), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        launchTeamRoutesActivity();
        teamRoutesScenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).register(any());
            Mockito.verify(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
            getUIFields(activity);
            assertEquals(View.VISIBLE, routesThirdCheckmark.getVisibility());
        });

        Route route = teamRoutesList.get(1);
        launchRouteDetailsActivity(route, 2);
        routeDetailScenario.onActivity(activity -> {
            TextView checkMark = activity.findViewById(R.id.tv_previously_walked_checkmark);
            assertEquals(View.VISIBLE, checkMark.getVisibility());
        });
    }

    @Test
    public void testTeamRouteCheckMark_teamNoStatsYouHaveStats() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,5,6);
        WalkStats stats = new WalkStats(1000, 90_000_000, 1.5,  calendar);
        Route routeUnoCopy = new Route("CSE Building").setRouteUid("CSE").setStats(stats);
        try {
            RoutesManager.writeSingle(routeUnoCopy, routeUno.getRouteUid(), context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        launchTeamRoutesActivity();
        teamRoutesScenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).register(any());
            Mockito.verify(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
            getUIFields(activity);
            assertEquals(View.VISIBLE, routesFirstCheckmark.getVisibility());
        });

        Route route = teamRoutesList.get(0);
        launchRouteDetailsActivity(route, 0);
        routeDetailScenario.onActivity(activity -> {
            TextView checkMark = activity.findViewById(R.id.tv_previously_walked_checkmark);
            assertEquals(View.VISIBLE, checkMark.getVisibility());
        });
    }

    @Test
    public void testTeamRouteNoCheckMark_teamHasStatsYouNoStats() {
        launchTeamRoutesActivity();
        teamRoutesScenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).register(any());
            Mockito.verify(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
            getUIFields(activity);
            assertEquals(View.INVISIBLE, routesThirdCheckmark.getVisibility());
        });

        Route route = teamRoutesList.get(1);
        launchRouteDetailsActivity(route, 2);
        routeDetailScenario.onActivity(activity -> {
            TextView checkMark = activity.findViewById(R.id.tv_previously_walked_checkmark);
            TextView neverWalked = activity.findViewById(R.id.tv_details_never_walked);
            assertEquals(View.INVISIBLE, checkMark.getVisibility());
            assertEquals(View.VISIBLE, neverWalked.getVisibility());
        });
    }

    private void getUIFields(RoutesActivity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view);
        getCheckmarks(recyclerView);
    }

    private void getUIFields(TeamRoutesActivity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view_team_routes);
        getCheckmarks(recyclerView);
    }

    private void getCheckmarks(RecyclerView recyclerView) {
        View firstView = recyclerView.getLayoutManager().findViewByPosition(0);
        View secondView = recyclerView.getLayoutManager().findViewByPosition(1);
        View thirdView = recyclerView.getLayoutManager().findViewByPosition(2);

        routesFirstCheckmark = firstView.findViewById(R.id.tv_previously_walked_checkmark);
        routesSecondCheckmark = secondView.findViewById(R.id.tv_previously_walked_checkmark);
        routesThirdCheckmark = thirdView.findViewById(R.id.tv_previously_walked_checkmark);
    }

    private void launchRoutesActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RoutesActivity.class)
                .putExtra(RoutesActivity.SAVE_FILE_KEY, TEST_SAVE_FILE);
        try {
            RoutesManager.writeList(routesList, TEST_SAVE_FILE, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        routesScenario = ActivityScenario.launch(intent);
    }

    private void launchTeamRoutesActivity() {
        Mockito.doAnswer(invocation -> {
            ((TeamsRoutesObserver)observer).onRoutesRetrieved(teamRoutesList, null);
            return invocation;
        }).when(teamsDatabaseService).getUserTeamRoutes(anyString(), anyString(), anyInt(), any());
        teamRoutesScenario = ActivityScenario.launch(TeamRoutesActivity.class);
    }

    private void launchRouteDetailsActivity(Route route, int index) {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDetailsActivity.class)
                .putExtra(RouteDetailsActivity.ROUTE_KEY, route)
                .putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, index);
        routeDetailScenario = ActivityScenario.launch(intent);
    }

    private List<Route> getListOfRoutes(boolean getUserRoutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,5,6);
        WalkStats stats = new WalkStats(1000, 90_000_000, 1.5,  calendar);

        routeUno = new Route("CSE Building").setRouteUid("CSE");
        routeDos = new Route("ECE Building")
                .setRouteUid("ECE")
                .setStartingLocation("ECE Makerspace")
                .setFavorite(true)
                .setStats(stats);

        calendar = Calendar.getInstance();
        calendar.set(2019, 1, 11);
        stats = new WalkStats(500, 90_000, 2.0, calendar);
        routeTres = new Route("Center Hall")
                .setRouteUid("CENTRE")
                .setFavorite(false)
                .setStartingLocation("Tu madre")
                .setStats(stats);

        String ownerName = otherUser.getDisplayName();
        if(getUserRoutes) {
            ownerName = testUser.getDisplayName();
        }
        routeUno.setCreatorDisplayName(ownerName);
        routeDos.setCreatorDisplayName(ownerName);
        routeTres.setCreatorDisplayName(ownerName);

        List<Route> routes = new ArrayList<>();
        routes.add(routeUno);
        routes.add(routeDos);
        routes.add(routeTres);
        return routes;
    }

}
