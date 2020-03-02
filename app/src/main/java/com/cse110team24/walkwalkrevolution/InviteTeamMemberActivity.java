package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.gms.tasks.Task;
import androidx.appcompat.app.AppCompatActivity;

public class InviteTeamMemberActivity extends AppCompatActivity implements MessagingObserver{
    private static final String TAG = "InviteTeamMemberActivity";
    private EditText editTeammateNameInvite;
    private EditText editTeammateGmailInvite;
    private Button btnSendInvite;
    private ProgressBar progressBar;

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
        messagingService.register(this);
        getUIFields();
        createFromUser();
        btnSendInvite.setOnClickListener(view -> {
            sendInvite(view);
        });
    }

    private void sendInvite(View view) {
        createFromUser();
        mInvitation = createInvitation();
        if (mFrom != null && mInvitation != null) {
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG, "sendInvite: sending invitation from " + mInvitation.fromName() + " to " + mInvitation.toName());
            messagingService.sendInvitation(mInvitation);
        }
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

    private Invitation createInvitation() {
        String toEmail = editTeammateGmailInvite.getText().toString();
        if(!Utils.isValidGmail(toEmail)) {
            Toast.makeText(this, "Please enter a valid gmail address", Toast.LENGTH_SHORT).show();
            return null;
        }
        String toDisplayName = editTeammateNameInvite.getText().toString();
        return Invitation.builder()
                .addFromUser(mFrom)
                .addToDisplayName(toDisplayName)
                .addToEmail(toEmail)
                .build();
    }

    private void getUIFields() {
        editTeammateGmailInvite = findViewById(R.id.field_enter_member_email);
        editTeammateNameInvite = findViewById(R.id.field_enter_member_name);
        btnSendInvite = findViewById(R.id.btn_send_invite);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onInvitationSent(Invitation invitation) {
        handleInvitationResult("Invitation sent");
    }

    @Override
    public void onFailedInvitationSent(Task<?> task) {
        handleInvitationResult("Error sending invitation");
    }

    private void handleInvitationResult(String message) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        editTeammateGmailInvite.setText("");
        editTeammateNameInvite.setText("");
    }
}
