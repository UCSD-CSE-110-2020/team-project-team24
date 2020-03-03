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
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseSeviceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: 3/3/20 change to implement TeamsDatabaseServiceObserver and UsersDatabaseServiceObserver
public class InviteTeamMemberActivity extends AppCompatActivity implements MessagingObserver, UsersDatabaseSeviceObserver {
    private static final String TAG = "InviteTeamMemberActivity";
    private EditText editTeammateNameInvite;
    private EditText editTeammateGmailInvite;
    private Button btnSendInvite;
    private ProgressBar progressBar;

    private SharedPreferences preferences;

    private AuthService authService;

    // TODO: 3/3/20 change to TeamsDatabaseService and UsersDatabaseService
    private UsersDatabaseService mUsersDB;
    private InvitationsDatabaseService mInvitationsDB;
    private TeamDatabaseService mTeamsDB;
    private MessagingService messagingService;

    private IUser mFrom;
    private Invitation mInvitation;

    private String mTeamUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_invite_member);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        authService = FirebaseApplicationWWR.getAuthServiceFactory().createAuthService();

        getUIFields();
        createFromUser();
        setUpServices();
        btnSendInvite.setOnClickListener(view -> {
            sendInvite(view);
        });
    }

    private void setUpServices() {
        mUsersDB = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
        mUsersDB.register(this);

        mInvitationsDB = (InvitationsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.INVITATIONS);
        mTeamsDB = (TeamDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);

        messagingService = FirebaseApplicationWWR.getMessagingServiceFactory().createMessagingService(this, mInvitationsDB);
        messagingService.register(this);
    }

    private void sendInvite(View view) {
        mInvitation = createInvitation();
        if (mFrom != null && mInvitation != null) {
            progressBar.setVisibility(View.VISIBLE);
            Log.i(TAG, "sendInvite: sending invitation from " + mInvitation.fromName() + " to " + mInvitation.toName());
            mUsersDB.getUserData(mFrom);
            messagingService.sendInvitation(mInvitation);
        }
    }

    private void createTeamIfNull() {
        if (mTeamUid == null) {
            Log.i(TAG, "createTeamIfNull: user has no team. It has been created");
            ITeam team = new TeamAdapter(new ArrayList<>());
            team.addMember(mFrom);
            mTeamUid = mTeamsDB.createTeamInDatabase(mFrom);
            mUsersDB.setUserTeam(mFrom, mTeamUid);
        }

        messagingService.subscribeToNotificationsTopic(mTeamUid);
        saveTeamUid();
    }

    private void saveTeamUid() {
        getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(IUser.TEAM_UID_KEY, mTeamUid)
                .apply();
    }

    private void createFromUser() {
        String displayName = preferences.getString(IUser.USER_NAME_KEY, null);
        String email = preferences.getString(IUser.EMAIL_KEY, null);
        if (displayName != null && email != null) {
            mFrom = authService.getUser();
            mFrom.setDisplayName(displayName);
            mFrom.setEmail(email);
        }
    }

    private Invitation createInvitation() {
        String toEmail = editTeammateGmailInvite.getText().toString();
        String toDisplayName = editTeammateNameInvite.getText().toString();
        if (invalidEmail(toEmail) || invalidName(toDisplayName)) return null;
        return Invitation.builder()
                .addFromUser(mFrom)
                .addToDisplayName(toDisplayName)
                .addToEmail(toEmail)
                .build();
    }

    private boolean invalidEmail(String toEmail) {
        if(!Utils.isValidGmail(toEmail)) {
            Toast.makeText(this, "Please enter a valid gmail address", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean invalidName(String toName) {
        if (toName.isEmpty()) {
            Toast.makeText(this, "please enter a name", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
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
        editTeammateGmailInvite.setText("");
        editTeammateNameInvite.setText("");
    }

    @Override
    public void onFailedInvitationSent(Task<?> task) {
        handleInvitationResult("Error sending invitation. User may not exist");
    }

    private void handleInvitationResult(String message) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
        if (userDataMap != null) {
            mTeamUid = (String) userDataMap.get("teamUid");
            createTeamIfNull();
        }
    }
}