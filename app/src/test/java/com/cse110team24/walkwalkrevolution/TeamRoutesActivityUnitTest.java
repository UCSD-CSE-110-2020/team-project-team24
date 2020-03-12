package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.annotation.LooperMode;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TeamRoutesActivityUnitTest extends TestInjection {
    private TeamsDatabaseServiceObserver observer;
    private ActivityScenario<TeamRoutesActivity> mActivityScenario;

    private RecyclerView teamRoutesRv;
    private Button firstFav;
    private Button secondFav;

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
                .putString(IUser.EMAIL_KEY, "email")
                .putString(IUser.TEAM_UID_KEY, "666")
                .putString(IUser.USER_NAME_KEY, "test")
                .commit();
        makeTeamRoutes();
    }

    @Test
    public void testTeamRoutes_verifyGetsTeamRoutes() {
        shadowOf(Looper.getMainLooper()).idle();
        answerWithTeamRoutes(null);
        mActivityScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        mActivityScenario.onActivity(activity -> {
            getTeamRoutesActivityUIElements(activity);
            Mockito.verify(dsf).createDatabaseService(DatabaseService.Service.TEAMS);
            Mockito.verify(teamsDatabaseService).register(any());
            Mockito.verify(teamsDatabaseService).getUserTeamRoutes(eq("666"), eq("test"), eq(10), any());
        });
    }

    @Test
    public void testTeamRoutes_verifyCorrectFavoriteDisplay() {
        answerWithTeamRoutes(null);
        mActivityScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        mActivityScenario.onActivity(activity -> {
            getTeamRoutesActivityUIElements(activity);
            assertEquals(activity.getDrawable(R.drawable.ic_star_border_black_24dp).getConstantState(), firstFav.getBackground().getConstantState());
            assertEquals(activity.getDrawable(R.drawable.ic_star_border_black_24dp).getConstantState(), secondFav.getBackground().getConstantState());
        });
    }

    @Test
    public void testTeamRoutes_verifyFavoritePersist() {
        answerWithTeamRoutes(null);
        mActivityScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        mActivityScenario.onActivity(activity -> {
            getTeamRoutesActivityUIElements(activity);
            firstFav.performClick();
            shadowOf(Looper.getMainLooper()).idle();
            assertEquals(activity.getDrawable(R.drawable.ic_star_yellow_24dp).getConstantState(), firstFav.getBackground().getConstantState());
        });
        mActivityScenario.close();

        //Persist on Relaunch
        mActivityScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        mActivityScenario.onActivity(activity -> {
            getTeamRoutesActivityUIElements(activity);
            shadowOf(Looper.getMainLooper()).idle();
            assertEquals(activity.getDrawable(R.drawable.ic_star_yellow_24dp).getConstantState(), firstFav.getBackground().getConstantState());
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
        View firstView = teamRoutesRv.getLayoutManager().findViewByPosition(0);
        View secondView = teamRoutesRv.getLayoutManager().findViewByPosition(1);
        firstFav = firstView.findViewById(R.id.btn_routes_favorite);
        secondFav = secondView.findViewById(R.id.btn_routes_favorite);
    }

    private void makeTeamRoutes() {
        Route firstRoute = new Route("Route One")
                .setRouteUid("1")
                .setCreatorDisplayName("Not the creator")
                .setFavorite(false);
        Route secondRoute = new Route("Route Two")
                .setRouteUid("2")
                .setCreatorDisplayName("Not the creator either")
                .setFavorite(true);

        testTeamRoutes = new ArrayList<>();
        testTeamRoutes.add(firstRoute);
        testTeamRoutes.add(secondRoute);
    }
}
