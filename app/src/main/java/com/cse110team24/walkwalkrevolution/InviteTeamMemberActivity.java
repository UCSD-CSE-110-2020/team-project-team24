package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;
import androidx.appcompat.app.AppCompatActivity;

public class InviteTeamMemberActivity extends AppCompatActivity implements MessagingObserver{

    private EditText editTeammateNameInvite;
    private EditText editTeammateGmailInvite;
    private Button btnSendInvite;

    private SharedPreferences preferences;

    private AuthService authService;
    private DatabaseService mDb;
    private MessagingService messagingService;

    private IUser mFrom;
    private Invitation mInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_invite_member);
        preferences = getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE);
        authService = FirebaseApplicationWWR.getAuthServiceFactory().createAuthService();
        mDb = FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService();
        messagingService = FirebaseApplicationWWR.getMessagingServiceFactory().createMessagingService(this, mDb);
        getUIFields();
        createFromUser();
        btnSendInvite.setOnClickListener(view -> {
            sendInvite(view);
        });
    }

    private void sendInvite(View view) {
        createFromUser();
        createInvitation();
        messagingService.sendInvitation(mInvitation);
    }

    private void createFromUser() {
        String displayName = preferences.getString(LoginActivity.USER_NAME_KEY, null);
        String email = preferences.getString(LoginActivity.EMAIL_KEY, null);
        if (displayName != null && email != null) {
            mFrom = authService.getUser();
            mFrom.setDisplayName(displayName);
            mFrom.setEmail(email);
        }
    }

    private void createInvitation() {
        String toEmail = editTeammateGmailInvite.getText().toString();
        String toDisplayName = editTeammateNameInvite.getText().toString();
        mInvitation = Invitation.builder()
                .addFromUser(mFrom)
                .addToDisplayName(toEmail)
                .addToEmail(toDisplayName)
                .build();
    }

    private void getUIFields() {
        editTeammateGmailInvite = findViewById(R.id.field_enter_member_email);
        editTeammateNameInvite = findViewById(R.id.field_enter_member_name);
        btnSendInvite = findViewById(R.id.btn_send_invite);
    }

    @Override
    public void onInvitationSent(Invitation invitation) {
        Toast.makeText(this, "Invitation sent", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedInvitationSent(Task<?> task) {

    }
}
