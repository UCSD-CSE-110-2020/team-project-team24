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
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.gms.tasks.Task;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InviteTeamMemberActivity extends AppCompatActivity implements MessagingObserver, UsersDatabaseServiceObserver {
    private static final String TAG = "WWR_InviteTeamMemberActivity";
    private EditText editTeammateNameInvite;
    private EditText editTeammateGmailInvite;
    private Button btnSendInvite;
    private ProgressBar progressBar;

    private SharedPreferences preferences;

    private AuthService authService;

    private UsersDatabaseService mUsersDB;

    private InvitationsDatabaseService mInvitationsDB;

    private TeamDatabaseService mTeamsDB;
    private MessagingService messagingService;

    private IUser mFrom;
    private Invitation mInvitation;
    private String mTeamUid;

    private String mToEmail;
    private String mToDisplayName;
    private boolean receiverExists = false;
    private boolean dataReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_invite_member);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        getUIFields();
        setUpServices();

        createFromUser();
        // check if there is a teamUid in the shared preferences file - if not, get the user's data
        if ( (mTeamUid = preferences.getString(IUser.TEAM_UID_KEY, null)) == null) {
            mUsersDB.getUserData(mFrom);
        } else {
            dataReady = true;
            mFrom.updateTeamUid(mTeamUid);
            btnSendInvite.setEnabled(true);
        }


        btnSendInvite.setOnClickListener(view -> {
            tryToSendInvitation(view);
        });
    }

    private void setUpServices() {
        authService = FirebaseApplicationWWR.getAuthServiceFactory().createAuthService();

        mUsersDB = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
        mUsersDB.register(this);

        mInvitationsDB = (InvitationsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.INVITATIONS);
        mTeamsDB = (TeamDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);

        messagingService = FirebaseApplicationWWR.getMessagingServiceFactory().createMessagingService(this, mInvitationsDB);
        messagingService.register(this);
    }

    private void tryToSendInvitation(View view) {

        // don't call send invitation if the receiver information is invalid
        mToEmail = editTeammateGmailInvite.getText().toString();
        mToDisplayName = editTeammateNameInvite.getText().toString();
        if (invalidEmail(mToEmail) || invalidName(mToDisplayName)) return;

        // dataReady indicates we have exhausted all places to read the teamUid from
        // this will be true if the button can be clicked (probably need to remove)
        if (dataReady) {
            progressBar.setVisibility(View.VISIBLE);
            mUsersDB.checkIfOtherUserExists(Utils.cleanEmail(mToEmail));
        }
    }

    // create the sending user's team in the database
    // only do this if the teamUid is null both in shared preferences and in the UID
    private void createTeam() {
        Log.i(TAG, "createTeamIfNull: user has no team. It has been created");
        // create the team
        mTeamUid = mTeamsDB.createTeamInDatabase(mFrom);

        // update the user's user document
        mFrom.updateTeamUid(mTeamUid);
        mUsersDB.updateUserTeamUidInDatabase(mFrom, mTeamUid);

        // subscribe to the topic
        messagingService.subscribeToNotificationsTopic(mTeamUid);
        saveTeamUidInPreferences();
        dataReady = true;
        uploadAllSavedRoutes(mTeamUid);
    }

    private void uploadAllSavedRoutes(String mTeamUid) {
        // todo get the list locally then upload them one by one
        try {
            List<Route> routes = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, this);
            routes.forEach(route -> mTeamsDB.updateRoute(mTeamUid, route));
            Log.i(TAG, "uploadAllSavedRoutes: uploaded all routes to database");
        } catch (IOException e) {
            Log.e(TAG, "uploadAllSavedRoutes: error reading list from file", e);
        }
    }

    // save the team UID into shared preferences for future use
    private void saveTeamUidInPreferences() {
        getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(IUser.TEAM_UID_KEY, mTeamUid)
                .apply();
    }

    // create an IUser representation of the sending user that contains their name, email, and
    // team Uid regardless of whether the teamUid exists
    private void createFromUser() {
        String displayName = preferences.getString(IUser.USER_NAME_KEY, null);
        String email = preferences.getString(IUser.EMAIL_KEY, null);
        if (displayName != null && email != null) {
            mFrom = authService.getUser();
            mFrom.setDisplayName(displayName);
            mFrom.setEmail(email);
        }
    }

    // create the Invitation with the sending user's IUser representation and the receiving user's
    // email and name, and includes the sender's teamUid (which must always exist)
    private Invitation createInvitation() {
        return Invitation.builder()
                .addFromUser(mFrom)
                .addToDisplayName(mToDisplayName)
                .addToEmail(mToEmail)
                .addTeamUid(mFrom.teamUid())
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
        btnSendInvite.setEnabled(false);
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
        handleInvitationResult("Error sending invitation");
    }

    private void handleInvitationResult(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
        if (userDataMap != null) {
            Log.i(TAG, "onUserData: user data retrieved");
            mTeamUid = (String) userDataMap.get("teamUid");
            mFrom.updateTeamUid(mTeamUid);
            // still check if the teamUid exists because they could have deleted app data
            dataReady = true;
            btnSendInvite.setEnabled(true);
        }
    }

    // allow the invitation and team to be created only if the receiver exists and doesn't have a team
    @Override
    public void onUserExists(IUser otherUser) {
        if (!mToDisplayName.equals(otherUser.getDisplayName())) {
            onUserDoesNotExist();
        }
        else if (otherUser.teamUid() != null) {
            handleInvitationResult("Cannot send invite. User already has a team.");
        } else if (mFrom != null) {
            // create team if it doesn't exist and invitation receiver exists
            if (mTeamUid == null) {
                createTeam();
            }
            mInvitation = createInvitation();
            Log.i(TAG, "sendInvite: sending invitation from " + mInvitation.fromName() + " to " + mInvitation.toName());
            messagingService.sendInvitation(mInvitation);
        }
    }

    @Override
    public void onUserDoesNotExist() {
        handleInvitationResult("A user with this name/email combination does not exist");
    }
}