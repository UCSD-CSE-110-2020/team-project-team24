package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class InviteTeamMemberActivity extends Activity {

    private EditText editTeammateNameInvite;
    private EditText editTeammateGmailInvite;
    private Button btnSendInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_invite_member);

        getUIFields();
    }

    private void getUIFields() {
        editTeammateGmailInvite = findViewById(R.id.field_enter_member_email);
        editTeammateNameInvite = findViewById(R.id.field_enter_member_name);
        btnSendInvite = findViewById(R.id.btn_send_invite);
    }
}
