package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;

public class InviteTeamMemberActivity extends Activity implements MessagingObserver {

    private EditText editTeammateNameInvite;
    private EditText editTeammateGmailInvite;
    private Button btnSendInvite;

    private SharedPreferences preferences;

    private AuthService authService;
    private DatabaseService mDb;
    private MessagingService messagingService;

    private IUser from;
    private IUser to;

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
    }

    private void createFromUser() {
        String displayName = preferences.getString(LoginActivity.USER_NAME_KEY, null);
        String email = preferences.getString(LoginActivity.EMAIL_KEY, null);
        if (displayName != null && email != null) {
            from = authService.getUser();
            from.setDisplayName(displayName);
            from.setEmail(email);
        }
    }

    private void createToUser() {

    }

    private void getUIFields() {
        editTeammateGmailInvite = findViewById(R.id.field_enter_member_email);
        editTeammateNameInvite = findViewById(R.id.field_enter_member_name);
        btnSendInvite = findViewById(R.id.btn_send_invite);
    }

    @Override
    public void onInvitationSent(Invitation invitation) {

    }

    @Override
    public void onFailedInvitationSent(Task<?> task) {

    }
}
