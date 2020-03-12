package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteRecyclerViewAdapter;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RoutesActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.SaveRouteActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class RoutesActivityUnitTest extends TestInjection {
    public static final String TEST_SAVE_FILE = ".WWR_route_list_data";

    SharedPreferences sp;

    private BottomNavigationView bottomNavigation;
    private RouteRecyclerViewAdapter adapter;
    private View secondBtn;
    private Button thirdBtn;
    private TextView firstTv;
    private TextView thirdTv;
    private FloatingActionButton fab;

    Intent intent;
    ActivityScenario<RoutesActivity> scenario;

    @Before
    public void setup() {
        super.setup();
        try {
            RoutesManager.writeList(getListOfRoutes(), TEST_SAVE_FILE, InstrumentationRegistry.getInstrumentation().getTargetContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent = new Intent(ApplicationProvider.getApplicationContext(), RoutesActivity.class)
                .putExtra(RoutesActivity.SAVE_FILE_KEY, TEST_SAVE_FILE);
        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void menuNotNull () {
        scenario.onActivity(activity -> {
            getUIFields(activity);
            Menu menu = bottomNavigation.getMenu();
            assertNotNull("Menu should not be null", menu);
        });
    }

    @Test
    public void testCorrectNumberOfItemsLoaded() {
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(3, adapter.getItemCount());
        });
    }

    @Test
    public void testFavBtnsCorrectIcon() {
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(activity.getDrawable(R.drawable.ic_star_border_black_24dp).getConstantState(), secondBtn.getBackground().getConstantState());
            assertEquals(activity.getDrawable(R.drawable.ic_star_yellow_24dp).getConstantState(), thirdBtn.getBackground().getConstantState());
        });
    }

    @Test
    public void testFavoritePersist() {
        scenario.onActivity(activity -> {
            getUIFields(activity);
            secondBtn.performClick();
            shadowOf(Looper.getMainLooper()).idle();
            assertEquals(activity.getDrawable(R.drawable.ic_star_yellow_24dp).getConstantState(), secondBtn.getBackground().getConstantState());
        });
        scenario.close();

        // Persist on Relaunch
        scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(activity.getDrawable(R.drawable.ic_star_yellow_24dp).getConstantState(), secondBtn.getBackground().getConstantState());
        });
    }

    @Test
    public void testCorrectItemOrder() {
        scenario.onActivity(activity -> {
           getUIFields(activity);

           assertEquals("CSE Building", firstTv.getText().toString());
           assertEquals("ECE Building", thirdTv.getText().toString());
        });
    }

    private void getUIFields(RoutesActivity activity) {
        bottomNavigation = activity.findViewById(R.id.bottom_navigation);

        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view);
        adapter = (RouteRecyclerViewAdapter) recyclerView.getAdapter();

        View firstView = recyclerView.getLayoutManager().findViewByPosition(0);
        View secondView = recyclerView.getLayoutManager().findViewByPosition(1);
        View thirdView = recyclerView.getLayoutManager().findViewByPosition(2);

        firstTv = firstView.findViewById(R.id.tv_route_name);
        thirdTv = thirdView.findViewById(R.id.tv_route_name);

        secondBtn = secondView.findViewById(R.id.btn_routes_favorite);
        thirdBtn = thirdView.findViewById(R.id.btn_routes_favorite);
    }

    private List<Route> getListOfRoutes() {
        String creatorName = "hola";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,5,6);
        WalkStats stats = new WalkStats(1000, 90_000_000, 1.5,  calendar);

        Route routeUno = new Route("CSE Building").setCreatorDisplayName(creatorName).setRouteUid("CSE");
        Route routeDos = new Route("ECE Building")
                .setRouteUid("ECE")
                .setStartingLocation("ECE Makerspace")
                .setFavorite(true)
                .setCreatorDisplayName(creatorName)
                .setStats(stats);

        calendar = Calendar.getInstance();
        calendar.set(2019, 1, 11);
        stats = new WalkStats(500, 90_000, 2.0, calendar);
        Route routTres = new Route("Center Hall")
                .setRouteUid("CENTRE")
                .setFavorite(false)
                .setStartingLocation("Tu madre")
                .setCreatorDisplayName(creatorName)
                .setStats(stats);

        List<Route> routes = new ArrayList<>();
        routes.add(routeUno);
        routes.add(routeDos);
        routes.add(routTres);
        saveCreatorName(creatorName);
        return routes;
    }

    private void saveCreatorName(String creatorName) {
        SharedPreferences sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.USER_NAME_KEY, creatorName)
                .commit();
    }
}