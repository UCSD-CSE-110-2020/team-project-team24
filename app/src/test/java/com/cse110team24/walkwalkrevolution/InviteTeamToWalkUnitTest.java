package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import com.cse110team24.walkwalkrevolution.activities.teams.InviteTeamToWalkActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.shadows.ShadowToast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class InviteTeamToWalkUnitTest extends TestInjection {

    private EditText dateEt;
    private EditText timeEt;
    private Button sendBtn;

    private ActivityScenario<InviteTeamToWalkActivity> scenario;
    private static final String FAKE_DATE = "03/09/2050";
    private static final String PAST_DATE = "01/01/1990";
    private static final String FAKE_TIME = "08:00";
    private static final String RANDOM_TEXT = "random text";
    private static final String TOAST_NO_DATE = "Please enter a date";
    private static final String TOAST_NO_TIME = "Please enter a time";
    private static final String TOAST_INVALID = "Please enter a valid date and time.";
    private static final String TOAST_PAST_TIME = "Please select a date in the future.";
    private static final String TOAST_INVITATION_SENT = "Invitation sent";

    @Before
    public void setup() {
        super.setup();
        Mockito.when(mAuth.getUser()).thenReturn(testUser);
        SharedPreferences sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .putString(IUser.TEAM_UID_KEY, testUser.teamUid())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);
        Mockito.when(teamsDatabaseService.updateCurrentTeamWalk(any())).thenReturn(RANDOM_TEXT);
    }

    private void getUIFields(Activity activity) {
        dateEt = activity.findViewById(R.id.et_proposed_day_invite_team_to_walk_activity);
        timeEt = activity.findViewById(R.id.et_proposed_time_invite_team_to_walk_activity);
        sendBtn = activity.findViewById(R.id.btn_send_invitation_to_team);
    }

    @Test
    public void nothingEntered() {
        scenario = ActivityScenario.launch(InviteTeamToWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            sendBtn.performClick();
            assertEquals(TOAST_NO_DATE, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void onlyDateEntered() {
        scenario = ActivityScenario.launch(InviteTeamToWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            dateEt.setText(FAKE_DATE);
            sendBtn.performClick();
            assertEquals(TOAST_NO_TIME, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void onlyTimeEntered() {
        scenario = ActivityScenario.launch(InviteTeamToWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            timeEt.setText(FAKE_TIME);
            sendBtn.performClick();
            assertEquals(TOAST_NO_DATE, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void invalidDateEntered() {
        scenario = ActivityScenario.launch(InviteTeamToWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            dateEt.setText(RANDOM_TEXT);
            timeEt.setText(FAKE_TIME);
            sendBtn.performClick();
            assertEquals(TOAST_INVALID, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void invalidTimeEntered() {
        scenario = ActivityScenario.launch(InviteTeamToWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            dateEt.setText(FAKE_DATE);
            timeEt.setText(RANDOM_TEXT);
            sendBtn.performClick();
            assertEquals(TOAST_INVALID, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void pastTimeEntered() {
        scenario = ActivityScenario.launch(InviteTeamToWalkActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            dateEt.setText(PAST_DATE);
            timeEt.setText(FAKE_TIME);
            sendBtn.performClick();
            assertEquals(TOAST_PAST_TIME, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void inviteTeamToWalk() {
        Route route = new Route.Builder(RANDOM_TEXT).build();
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), InviteTeamToWalkActivity.class)
                            .putExtra(IUser.USER_NAME_KEY, testUser.getDisplayName())
                            .putExtra(RouteDetailsActivity.ROUTE_KEY, route);
        scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            dateEt.setText(FAKE_DATE);
            timeEt.setText(FAKE_TIME);
            sendBtn.performClick();
            Mockito.verify(teamsDatabaseService).updateCurrentTeamWalk(any());
            assertEquals(TOAST_INVITATION_SENT, ShadowToast.getTextOfLatestToast());
        });
    }
}
