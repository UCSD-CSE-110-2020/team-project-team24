package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.activities.teams.ScheduledProposedWalkActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamStatusesObserver;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(AndroidJUnit4.class)
public class ScheduleWalkActivityUnitTest extends TestInjection {
    SharedPreferences sp;
    Intent organizerIntent;
    TeamsDatabaseServiceObserver teamDbObserver;
    List<TeamWalk> walkList;
    TextView walkStatusTv;
    LinearLayout recipientButtons;
    LinearLayout organizerButtons;
    Button scheduleBtn;
    Button withdrawCancelBtn;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.TEAM_UID_KEY, testUser.teamUid())
                 .putString(IUser.EMAIL_KEY, testUser.getEmail())
                 .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                 .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        mockTeamDb();
        createTeamWalkList();
        organizerIntent = new Intent(ApplicationProvider.getApplicationContext(), ScheduledProposedWalkActivity.class);
    }

    @Test
    public void testScheduleWalkActivity_CorrectButtonDisplayAsOrganizer() {
        ActivityScenario<ScheduledProposedWalkActivity> scenario = ActivityScenario.launch(organizerIntent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            Mockito.verify(teamsDatabaseService).getLatestTeamWalksDescendingOrder("666", 1);
            Mockito.verify(teamsDatabaseService).register(any());

            assertEquals("Status: Proposed", walkStatusTv.getText().toString());
            assertEquals(View.GONE, recipientButtons.getVisibility());
            assertEquals(View.VISIBLE, organizerButtons.getVisibility());
            assertEquals("withdraw", withdrawCancelBtn.getText().toString());
        });
    }

    @Test
    public void testScheduleWalkActivity_ScheduleWalkAsOrganizer() {
        ActivityScenario<ScheduledProposedWalkActivity> scenario = ActivityScenario.launch(organizerIntent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            Mockito.verify(teamsDatabaseService).getLatestTeamWalksDescendingOrder("666", 1);
            Mockito.verify(teamsDatabaseService).register(any());

            scheduleBtn.performClick();
            assertEquals("Status: Scheduled", walkStatusTv.getText().toString());
            assertEquals(View.GONE, recipientButtons.getVisibility());
            assertEquals(View.VISIBLE, organizerButtons.getVisibility());
            assertEquals(View.GONE, scheduleBtn.getVisibility());
            assertEquals("cancel", withdrawCancelBtn.getText().toString());
        });
    }

    @Test
    public void testScheduleWalkActivity_WithdrawWalkAsOrganizer() {
        ActivityScenario<ScheduledProposedWalkActivity> scenario = ActivityScenario.launch(organizerIntent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            Mockito.verify(teamsDatabaseService).getLatestTeamWalksDescendingOrder("666", 1);
            Mockito.verify(teamsDatabaseService).register(any());

            withdrawCancelBtn.performClick();
            assertEquals("Status: Withdrawn", walkStatusTv.getText().toString());
            assertEquals(View.GONE, recipientButtons.getVisibility());
            assertEquals(View.GONE, organizerButtons.getVisibility());
        });
    }

    @Test
    public void testScheduleWalkActivity_CancelWalkAsOrganizer() {
        ActivityScenario<ScheduledProposedWalkActivity> scenario = ActivityScenario.launch(organizerIntent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            Mockito.verify(teamsDatabaseService).getLatestTeamWalksDescendingOrder("666", 1);
            Mockito.verify(teamsDatabaseService).register(any());

            scheduleBtn.performClick();
            withdrawCancelBtn.performClick();
            assertEquals("Status: Cancelled", walkStatusTv.getText().toString());
            assertEquals(View.GONE, recipientButtons.getVisibility());
            assertEquals(View.GONE, organizerButtons.getVisibility());
        });
    }

    private void mockTeamDb() {
        Mockito.doAnswer(invocation -> {
            teamDbObserver = invocation.getArgument(0);
            return invocation;
        }).when(teamsDatabaseService).register(any());

        Mockito.doAnswer(invocation -> {
            ((TeamsTeamWalksObserver) teamDbObserver).onTeamWalksRetrieved(walkList);
            return null;
        }).when(teamsDatabaseService).getLatestTeamWalksDescendingOrder("666", 1);

        Mockito.doAnswer(invocation -> {
            ((TeamsTeamStatusesObserver) teamDbObserver).onTeamWalkStatusesRetrieved(new TreeMap<>());
            return null;
        }).when(teamsDatabaseService).getTeammateStatusesForTeamWalk(any(), any());
    }

    private void getUIFields(Activity activity) {
        walkStatusTv = activity.findViewById(R.id.schedule_propose_tv_walk_status);
        recipientButtons = activity.findViewById(R.id.schedule_propose_linear_layout_status_buttons);
        organizerButtons = activity.findViewById(R.id.schedule_propose_linear_layout_decision_buttons);
        scheduleBtn = activity.findViewById(R.id.schedule_propose_btn_schedule);
        withdrawCancelBtn = activity.findViewById(R.id.schedule_propose_btn_withdraw);
    }

    private void createTeamWalkList() {
        walkList = new ArrayList<>();
        Route route = new Route("Test Route").setStartingLocation("Here");
        Timestamp timestamp = new Timestamp(new Date(2021, 4, 4));
        TeamWalk teamWalk = new TeamWalk(route, testUser.getDisplayName(), timestamp);
        walkList.add(teamWalk);
    }
}
