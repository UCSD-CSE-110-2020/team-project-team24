package com.cse110team24.walkwalkrevolution;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class InviteTeammateUnitTest extends TestInjection {

    private LoginActivity testActivity;
    private Button sendInviteBtn;
    private TextView inviteNameTv;
    private TextView inviteEmailTv;

    @Before
    public void setup() {
        super.setup();

        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
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
}
