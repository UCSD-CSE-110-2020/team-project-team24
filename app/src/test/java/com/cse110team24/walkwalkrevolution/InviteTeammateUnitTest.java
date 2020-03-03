package com.cse110team24.walkwalkrevolution;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InviteTeammateUnitTest extends TestInjection {

    private InviteTeamMemberActivity testActivity;
    private Button sendInviteBtn;
    private TextView inviteNameTv;
    private TextView inviteEmailTv;
    private static final String TOAST_MSG_NOT_GMAIL = "Please enter a valid gmail address";

    @Before
    public void setup() {
        super.setup();

        ActivityScenario<InviteTeamMemberActivity> scenario = ActivityScenario.launch(InviteTeamMemberActivity.class);
        scenario.onActivity(activity -> {
            testActivity = activity;
            sendInviteBtn = testActivity.findViewById(R.id.btn_send_invite);
            inviteNameTv = testActivity.findViewById(R.id.field_enter_member_name);
            inviteEmailTv = testActivity.findViewById(R.id.field_enter_member_email);
        });
    }

    @Test
    public void emptyTest() {
        // TODO: change the name and fill out this test
    }

    @Test
    public void checkNameEntered() {
        inviteNameTv.setText("Marian");
        sendInviteBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NOT_GMAIL);

    }

}