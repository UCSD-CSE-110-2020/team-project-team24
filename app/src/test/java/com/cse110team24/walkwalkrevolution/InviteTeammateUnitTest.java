package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
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

    private static final String INVALID_EMAIL = "Please enter a valid email address";

    @Before
    public void setup() {
        super.setup();

        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);

        ActivityScenario<InviteTeamMemberActivity> scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);

        scenario.onActivity(activity -> {
            testActivity = activity;

            sendInviteBtn = testActivity.findViewById(R.id.btn_send_invite);
            inviteNameTv = testActivity.findViewById(R.id.field_enter_member_name);
            inviteEmailTv = testActivity.findViewById(R.id.field_enter_member_email);
        });
    }

    @Test
    public void noInfoInvitation() {
//        sendInviteBtn.performClick();
//        assertEquals(ShadowToast.getTextOfLatestToast(), INVALID_EMAIL);

    }

    @Test
    public void emailNotEntered() {
        inviteNameTv.setText("Marian");
        sendInviteBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NOT_GMAIL);
    }

    @Test
    public void nameNotEntered() {
        inviteEmailTv.setText("test@gmail.com");
        sendInviteBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NO_USERNAME);
    }

}