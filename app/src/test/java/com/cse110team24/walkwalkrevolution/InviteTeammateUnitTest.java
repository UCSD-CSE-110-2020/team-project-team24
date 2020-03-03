package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.shadows.ShadowToast;


import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class InviteTeammateUnitTest extends TestInjection {

    private InviteTeamMemberActivity testActivity;

    private Button sendInviteBtn;
    private TextView inviteNameTv;
    private TextView inviteEmailTv;
    private static final String TOAST_MSG_NOT_GMAIL = "Please enter a valid gmail address";
    private static final String TOAST_MSG_NO_USERNAME = "please enter a name";
    private static final String TOAST_MSG_USER_NOT_EXIST = "Error sending invitation. User may not exist";

    ActivityScenario<InviteTeamMemberActivity> scenario;
    MessagingObserver observer;

    @Before
    public void setup() {
        super.setup();
        ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(IUser.EMAIL_KEY, "test@gmail.com")
                .putString(IUser.USER_NAME_KEY, "tester")
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);

    }

    private void getUIFields(Activity activity) {
        sendInviteBtn = activity.findViewById(R.id.btn_send_invite);
        inviteNameTv = activity.findViewById(R.id.field_enter_member_name);
        inviteEmailTv = activity.findViewById(R.id.field_enter_member_email);
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
    public void nonExistentUserEntered() {
        Mockito.doAnswer(invocation -> {
            observer = invocation.getArgument(0);
            return invocation;
        }).when(mMsg).register(any());

        Mockito.doAnswer(invocation -> {
            observer.onFailedInvitationSent(null);
            return null;
        }).when(mMsg).sendInvitation(Mockito.any());

        scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(mMsg).register(any());
            testActivity = activity;
            getUIFields(activity);

            inviteNameTv.setText("c");
            inviteEmailTv.setText("cheery@gmail.com");
            sendInviteBtn.performClick();

            Mockito.verify(mMsg).sendInvitation(any());
            assertEquals(TOAST_MSG_USER_NOT_EXIST, ShadowToast.getTextOfLatestToast());
        });
    }

}