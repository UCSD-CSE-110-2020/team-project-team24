package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.activities.invitations.InvitationsActivity;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@RunWith(AndroidJUnit4.class)
public class InvitationsActivityUnitTest extends TestInjection {

    private ActivityScenario<InvitationsActivity> scenario;
    private SharedPreferences sp;
    private Button acceptBtn;
    private Button declineBtn;
    private ListView invitationsListView;
    private InvitationsDatabaseServiceObserver invitationsDbObserver;
    private Invitation invitation;
    private List<Invitation> mInvitations = new ArrayList<>();
    private static final String TOAST_SELECT_INVITATION = "Please select an invitation";
    private static final String TOAST_ALREADY_ON_TEAM = "You already have a team! You can only decline invitations";

    @Before
    public void setup() {
        super.setup();
        Mockito.when(mAuth.getUser()).thenReturn(aTestUser);
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, aTestUser.getEmail())
                .putString(IUser.USER_NAME_KEY, aTestUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);
        invitation = Invitation.builder()
                .addFromUser(testUser)
                .addToEmail("tester@gmail.com")
                .addToDisplayName("Ival")
                .addTeamUid(testUser.teamUid())
                .build();

        doAnswer(invocation -> {
            invitationsDbObserver = invocation.getArgument(0);
            return invocation;
        }).when(invitationsDatabaseService).register(any());

        mInvitations.add(invitation);

        doAnswer(invocation -> {
            invitationsDbObserver.onUserPendingInvitations(mInvitations);
            return null;
        }).when(invitationsDatabaseService).getUserPendingInvitations(aTestUser);

    }

    private void getUIFields(Activity activity) {
        acceptBtn = activity.findViewById(R.id.buttonAccept);
        declineBtn = activity.findViewById(R.id.buttonDecline);
        invitationsListView = activity.findViewById(R.id.invitationList);
    }

    @Test
    public void didNotSelectInvitation() {
        setup();
        scenario = ActivityScenario.launch(InvitationsActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            acceptBtn.performClick();
            assertEquals(TOAST_SELECT_INVITATION, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void approveInvitation() {
        setup();
        scenario = ActivityScenario.launch(InvitationsActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            invitationsListView.performItemClick(
                    invitationsListView.getAdapter().getView(0, null, null),
                    0,
                    invitationsListView.getAdapter().getItemId(0));
            acceptBtn.performClick();
            assertEquals(aTestUser.teamUid(), testUser.teamUid());
            assertEquals("welcome to " + invitation.fromName() + "'s team", ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void declineInvitation() {
        setup();
        scenario = ActivityScenario.launch(InvitationsActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            invitationsListView.performItemClick(
                    invitationsListView.getAdapter().getView(0, null, null),
                    0,
                    invitationsListView.getAdapter().getItemId(0));
            declineBtn.performClick();
            assertNull(aTestUser.teamUid());
        });
    }

    @Test
    public void alreadyOnATeam() {
        setup();
        aTestUser.updateTeamUid("333");
        sp.edit().putString(IUser.TEAM_UID_KEY, aTestUser.teamUid())
                .commit();
        scenario = ActivityScenario.launch(InvitationsActivity.class);
        scenario.onActivity(activity ->  {
            getUIFields(activity);
            invitationsListView.performItemClick(
                    invitationsListView.getAdapter().getView(0, null, null),
                    0,
                    invitationsListView.getAdapter().getItemId(0));
            acceptBtn.performClick();
            assertEquals(TOAST_ALREADY_ON_TEAM, ShadowToast.getTextOfLatestToast());
        });
    }
}