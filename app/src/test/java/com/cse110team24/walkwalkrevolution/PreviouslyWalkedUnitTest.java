package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RoutesActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PreviouslyWalkedUnitTest extends TestInjection {

    TextView routesFirstCheckmark, routesSecondCheckmark, routesThirdCheckmark;

    private ActivityScenario<TeamRoutesActivity> teamRoutesScenario;
    private ActivityScenario<RoutesActivity> routesScenario;
    private ActivityScenario<RouteDetailsActivity> routeDetailScenario;

    public static final String TEST_SAVE_FILE = ".WWR_test_save_file";

    @Before
    public void setup() {
        super.setup();

    }

    @Test
    public void testYourWalkCheckmark_youHaveStats()  {
//        launchRoutesActivity();
//        routesScenario.onActivity(activity -> {
//            getUIFields(activity);
//            assertEquals(View.VISIBLE, routesFirstCheckmark.getVisibility());
//        });
    }

    private void getUIFields(RoutesActivity activity) {
        RecyclerView recyclerView = activity.findViewById(R.id.recycler_view);

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
            RoutesManager.writeList(getListOfRoutes(), TEST_SAVE_FILE, InstrumentationRegistry.getInstrumentation().getTargetContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        routesScenario = ActivityScenario.launch(intent);
    }

    private List<Route> getListOfRoutes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,5,6);
        WalkStats stats = new WalkStats(1000, 90_000_000, 1.5,  calendar);

        Route routeUno = new Route("CSE Building").setCreatorDisplayName("hola").setRouteUid("CSE");
        Route routeDos = new Route("ECE Building")
                .setRouteUid("ECE")
                .setStartingLocation("ECE Makerspace")
                .setFavorite(true)
                .setCreatorDisplayName("hola")
                .setStats(stats);

        calendar = Calendar.getInstance();
        calendar.set(2019, 1, 11);
        stats = new WalkStats(500, 90_000, 2.0, calendar);
        Route routTres = new Route("Center Hall")
                .setRouteUid("CENTRE")
                .setFavorite(false)
                .setStartingLocation("Tu madre")
                .setCreatorDisplayName("hola")
                .setStats(stats);

        List<Route> routes = new ArrayList<>();
        routes.add(routeUno);
        routes.add(routeDos);
        routes.add(routTres);
        return routes;
    }

}
