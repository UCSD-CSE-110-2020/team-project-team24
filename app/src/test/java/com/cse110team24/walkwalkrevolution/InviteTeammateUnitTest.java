package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.ArgumentMatchers.eq;
import org.robolectric.shadows.ShadowToast;

import java.util.HashMap;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InviteTeammateUnitTest extends TestInjection {

    private Button sendInviteBtn;
    private TextView inviteNameTv;
    private TextView inviteEmailTv;
    private static final String TOAST_MSG_NOT_GMAIL = "Please enter a valid gmail address";
    private static final String TOAST_MSG_NO_USERNAME = "please enter a name";
    private static final String TOAST_MSG_USER_NOT_EXIST = "A user with this name/email combination does not exist";
    private static final String TOAST_MSG_OTHER_USER_HAS_TEAM = "Cannot send invite. User already has a team.";
    private static final String TOAST_MSG_INVITATION_SENT = "Invitation sent";

    ActivityScenario<InviteTeamMemberActivity> scenario;
    MessagingObserver messagingObserver;
    UsersDatabaseServiceObserver userDbObserver;
    SharedPreferences sp;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);

    }

    private void getUIFields(Activity activity) {
        sendInviteBtn = activity.findViewById(R.id.btn_send_invite);
        inviteNameTv = activity.findViewById(R.id.field_enter_member_name);
        inviteEmailTv = activity.findViewById(R.id.field_enter_member_email);
    }

    private void inviteUserWithName(String name) {
        inviteNameTv.setText(name);
        inviteEmailTv.setText("cheery@gmail.com");
        sendInviteBtn.performClick();
    }

    private void mockUserDbRegister() {
        Mockito.doAnswer(invocation -> {
            userDbObserver = invocation.getArgument(0);
            return invocation;
        }).when(usersDatabaseService).register(any());
    }

    private void mockOtherUserExists() {
        Mockito.doAnswer(invocation -> {
            userDbObserver.onUserExists(otherUser);
            return null;
        }).when(usersDatabaseService).checkIfOtherUserExists(Mockito.any());
    }

    @Test
    public void noInfoEntered() {
        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            sendInviteBtn.performClick();
            assertEquals(TOAST_MSG_NOT_GMAIL, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void emailNotEntered() {
        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            inviteNameTv.setText("Marian");
            sendInviteBtn.performClick();
            assertEquals(TOAST_MSG_NOT_GMAIL, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void nameNotEntered() {
        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            inviteEmailTv.setText("test@gmail.com");
            sendInviteBtn.performClick();
            assertEquals(TOAST_MSG_NO_USERNAME, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void nonExistentUserEmailEntered() {
        sp.edit().putString(IUser.TEAM_UID_KEY, testUser.teamUid()).commit();
        mockUserDbRegister();

        Mockito.doAnswer(invocation -> {
            userDbObserver.onUserDoesNotExist();
            return null;
        }).when(usersDatabaseService).checkIfOtherUserExists(Mockito.any());

        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(usersDatabaseService).register(any());
            getUIFields(activity);
            inviteUserWithName("c");

            Mockito.verify(usersDatabaseService).checkIfOtherUserExists(any());
            assertEquals(TOAST_MSG_USER_NOT_EXIST, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void nonExistentUserNameEntered() {
        sp.edit().putString(IUser.TEAM_UID_KEY, testUser.teamUid()).commit();
        mockUserDbRegister();
        mockOtherUserExists();

        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(usersDatabaseService).register(any());
            getUIFields(activity);
            inviteUserWithName("c");

            Mockito.verify(usersDatabaseService).checkIfOtherUserExists(any());
            assertEquals(TOAST_MSG_USER_NOT_EXIST, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void otherUserHasTeam() {
        sp.edit().putString(IUser.TEAM_UID_KEY, testUser.teamUid()).commit();
        otherUser.updateTeamUid("888");
        mockUserDbRegister();
        mockOtherUserExists();

        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(usersDatabaseService).register(any());
            getUIFields(activity);
            inviteUserWithName("cheery");

            Mockito.verify(usersDatabaseService).checkIfOtherUserExists(any());
            assertEquals(TOAST_MSG_OTHER_USER_HAS_TEAM, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void availableUserEntered() {
        sp.edit().putString(IUser.TEAM_UID_KEY, testUser.teamUid()).commit();
        mockUserDbRegister();
        mockOtherUserExists();

        Mockito.doAnswer(invocation -> {
            messagingObserver = invocation.getArgument(0);
            return invocation;
        }).when(mMsg).register(any());

        Mockito.doAnswer(invocation -> {
            messagingObserver.onInvitationSent(null);
            return null;
        }).when(mMsg).sendInvitation(Mockito.any());

        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(mMsg).register(any());
            Mockito.verify(usersDatabaseService).register(any());
            getUIFields(activity);
            inviteUserWithName("cheery");

            Mockito.verify(usersDatabaseService).checkIfOtherUserExists(any());
            Mockito.verify(mMsg).sendInvitation(Mockito.any());
            assertEquals(TOAST_MSG_INVITATION_SENT, ShadowToast.getTextOfLatestToast());
        });
    }

    @Test
    public void userWithoutTeamIDLocally() {
        mockUserDbRegister();
        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(usersDatabaseService).register(any());
            Mockito.verify(usersDatabaseService).getUserData(any());
            assertNotNull(testUser.teamUid()); // test if team id has been created
        });
    }

    @Test
    public void userWithoutTeamID() {
        testUser.updateTeamUid(null);
        mockUserDbRegister();
        mockOtherUserExists();

        Mockito.doAnswer(invocation -> {
            userDbObserver.onUserData(new HashMap<>());
            return null;
        }).when(usersDatabaseService).getUserData(any());

        Mockito.doReturn("666").when(teamsDatabaseService).createTeamInDatabase(any());

        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(usersDatabaseService).register(any());

            getUIFields(activity);
            inviteUserWithName("cheery");

            Mockito.verify(usersDatabaseService).checkIfOtherUserExists(any());
            Mockito.verify(teamsDatabaseService).createTeamInDatabase(any());
            assertNotNull(testUser.teamUid());
        });
    }

}