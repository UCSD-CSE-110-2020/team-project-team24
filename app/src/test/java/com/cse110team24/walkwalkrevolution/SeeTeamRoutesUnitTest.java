package com.cse110team24.walkwalkrevolution;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class SeeTeamRoutesUnitTest extends TestInjection {
    private TeamsDatabaseServiceObserver observer;
    private ActivityScenario<TeamRoutesActivity> mActivityScenario;

    private RecyclerView teamRoutesRv;

    @Before
    public void setup() {
        getObserver();
    }

    @Test
    public void testTeamRoutes_verifyGetsTeamRoutes() {

        mActivityScenario = ActivityScenario.launch(TeamRoutesActivity.class);
        mActivityScenario.onActivity(activity -> {
            getTeamRoutesActivityUIElements(activity);
            
        });
    }


    private void getObserver() {
        Mockito.doAnswer(invocation -> {
            observer = invocation.getArgument(0);
            return null;
        }).when(teamsDatabaseService).register(any());
    }

    private void getTeamRoutesActivityUIElements(TeamRoutesActivity activity) {
        teamRoutesRv = activity.findViewById(R.id.recycler_view_team_routes);
    }
}
