package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Map;

// TODO: 3/3/20 change to  InvitationsDatabaseServiceObserver
public class InvitationsActivity extends AppCompatActivity implements DatabaseServiceObserver {
    private static final String TAG = "InvitationsActivity";

    // TODO: 3/3/20 change to InvitationsDatabaseService
    private DatabaseService mDb;
    private MessagingService mMessagingService;
    private AuthService mAuth;

    private SharedPreferences preferences;
    private IUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        setUpServices();
        getPendingInvitations();
    }

    private void setUpServices() {
        mAuth = FirebaseApplicationWWR.getAuthServiceFactory().createAuthService();
        mDb = FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.INVITATIONS);
        mDb.register(this);
        mMessagingService = FirebaseApplicationWWR.getMessagingServiceFactory().createMessagingService(this, mDb);
    }

    private void getPendingInvitations() {
        getCurrentUser();
        mDb.getUserPendingInvitations(mCurrentUser);
        Toast.makeText(this, "Getting your invitations", Toast.LENGTH_SHORT).show();
    }

    private void getCurrentUser() {
        String displayName = preferences.getString(IUser.USER_NAME_KEY, null);
        String email = preferences.getString(IUser.EMAIL_KEY, null);
        if (displayName != null && email != null) {
            mCurrentUser = mAuth.getUser();
            mCurrentUser.setDisplayName(displayName);
            mCurrentUser.setEmail(email);
        }
    }

    /* ---------------------- will be removed when changed to specialized service interfaces ------------------------ */
    @Override
    public void onTeamRetrieved(ITeam team) {
    }

    @Override
    public void onFieldRetrieved(Object field) {
    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
    }
    /* ---------------------- will be removed when changed to specialized service interfaces ------------------------ */

    // TODO: 3/3/20 display invitations and allow each one to be clickable
    @Override
    public void onUserPendingInvitations(List<Invitation> invitations) {
        Toast.makeText(this, "retrieved your invitations successfully!", Toast.LENGTH_LONG).show();
    }
}
