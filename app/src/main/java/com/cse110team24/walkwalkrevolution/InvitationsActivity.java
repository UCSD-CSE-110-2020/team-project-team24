package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class InvitationsActivity extends AppCompatActivity implements InvitationsDatabaseServiceObserver {
    private static final String TAG = "InvitationsActivity";

    private InvitationsDatabaseService mIDb;
    private TeamDatabaseService mTDb;
    private UsersDatabaseService mUDb;
    private MessagingService mMessagingService;
    private AuthService mAuth;

    private SharedPreferences preferences;
    private IUser mCurrentUser;

    private List<Invitation> mInvitations = new ArrayList<>();
    private InvitationsListViewAdapter mAdapter;
    private ListView mInvitationsListView;
    private TextView mNoInvitationsTextView;

    private Invitation mCurrentSelectedInvitation;

    private Button acceptBtn;
    private Button declineBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        getUIElements();
        setButtonsOnClickListener();
        setUpServices();
        getPendingInvitations();
    }

    private void getUIElements() {
        mInvitationsListView = findViewById(R.id.invitationList);
        mAdapter = new InvitationsListViewAdapter(this, mInvitations);
        mInvitationsListView.setAdapter(mAdapter);
        mNoInvitationsTextView = findViewById(R.id.tv_no_invitations_prompt);
        mInvitationsListView.setOnItemClickListener((parent, view, position, id) -> {
            selectInvitation(parent, view, position, id);
        });
        acceptBtn = findViewById(R.id.buttonAccept);
        declineBtn = findViewById(R.id.buttonDecline);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setButtonsOnClickListener() {
        setAcceptButtonOnClickListener();
        setDeclineButtonOnClickListener();
    }

    private void setAcceptButtonOnClickListener() {
        acceptBtn.setOnClickListener(view -> {
            if (mCurrentSelectedInvitation == null) {
                Utils.showToast(this, "Please select an invitation", Toast.LENGTH_SHORT);
            } else if (mCurrentUser.teamUid() != null) {
                Utils.showToast(this, "You already have a team! You can only decline invitations", Toast.LENGTH_LONG);
            } else {
                mCurrentSelectedInvitation.setStatus(InvitationStatus.ACCEPTED);
                updateInvitations();
                String senderTeamUid = mCurrentSelectedInvitation.getTeamUid();
                mCurrentUser.updateTeamUid(senderTeamUid);
                mTDb.addUserToTeam(mCurrentUser, senderTeamUid);
                mUDb.updateUserTeamUidInDatabase(mCurrentUser, senderTeamUid);
                Utils.saveString(preferences, IUser.TEAM_UID_KEY, senderTeamUid);
                mMessagingService.subscribeToNotificationsTopic(senderTeamUid);
            }
        });
    }

    private void setDeclineButtonOnClickListener() {
        declineBtn.setOnClickListener(view -> {
            if (mCurrentSelectedInvitation == null) return;

            mCurrentSelectedInvitation.setStatus(InvitationStatus.DECLINED);
            updateInvitations();
        });
    }

    private void updateInvitations() {
        mIDb.updateInvitationForSendingUser(mCurrentSelectedInvitation);
        mIDb.updateInvitationForReceivingUser(mCurrentSelectedInvitation);
    }

    private void selectInvitation(AdapterView<?> parent, View view, int position, long id) {
        mCurrentSelectedInvitation = (Invitation) mAdapter.getItem(position);
        Log.d(TAG, "selectInvitation: item selected: " + mCurrentSelectedInvitation.fromName());
    }

    private void setUpServices() {
        mAuth = FirebaseApplicationWWR.getAuthServiceFactory().createAuthService();
        mIDb = (InvitationsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.INVITATIONS);
        mIDb.register(this);

        mTDb = (TeamDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);

        mUDb = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);

        mMessagingService = FirebaseApplicationWWR.getMessagingServiceFactory().createMessagingService(this, mIDb);
    }

    private void getPendingInvitations() {
        progressBar.setVisibility(View.VISIBLE);
        getCurrentUser();
        mIDb.getUserPendingInvitations(mCurrentUser);
    }

    private void getCurrentUser() {
        String displayName = preferences.getString(IUser.USER_NAME_KEY, null);
        String email = preferences.getString(IUser.EMAIL_KEY, null);
        String teamUid = preferences.getString(IUser.TEAM_UID_KEY, null);
        mCurrentUser = mAuth.getUser();
        mCurrentUser.setDisplayName(displayName);
        mCurrentUser.setEmail(email);
        mCurrentUser.updateTeamUid(teamUid);
    }

    // TODO: 3/3/20 display invitations and allow each one to be clickable
    @Override
    public void onUserPendingInvitations(List<Invitation> invitations) {
        Log.i(TAG, "onUserPendingInvitations: user's pending invitations retrieved");
        progressBar.setVisibility(View.GONE);
        mInvitations.addAll(invitations);
        mAdapter.notifyDataSetChanged();
        if (!invitations.isEmpty()) {
            mNoInvitationsTextView.setVisibility(View.GONE);
        }
    }
}
