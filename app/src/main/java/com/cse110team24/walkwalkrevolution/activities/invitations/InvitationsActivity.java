package com.cse110team24.walkwalkrevolution.activities.invitations;


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

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RoutesActivity;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.Auth;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.Messaging;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvitationsActivity extends AppCompatActivity implements InvitationsDatabaseServiceObserver {
    private static final String TAG = "WWR_InvitationsActivity";

    private InvitationsDatabaseService mIDb;
    private TeamsDatabaseService mTDb;
    private UsersDatabaseService mUDb;
    private Messaging mMessaging;
    private Auth mAuth;

    private SharedPreferences preferences;
    private IUser mCurrentUser;


    private List<Invitation> mInvitations = new ArrayList<>();
    private InvitationsListViewAdapter mAdapter;
    private ListView mInvitationsListView;
    private TextView mNoInvitationsTextView;
    private TextView mSentYouInvitationsTextView;

    private Invitation mCurrentSelectedInvitation;

    private Button acceptBtn;
    private Button declineBtn;
    private ProgressBar progressBar;

    private int selectedIdx = -1;

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
                uploadAllSavedRoutes(senderTeamUid);
                updateTeamUidLocallyAndDatabase(senderTeamUid);
                mTDb.addUserToTeam(mCurrentUser, senderTeamUid);
                mMessaging.subscribeToNotificationsTopic(senderTeamUid);
                Utils.showToast(this, "welcome to " + mCurrentSelectedInvitation.fromName() + "'s team", Toast.LENGTH_LONG);
                dismissSelectedInvitation();
            }
        });
    }

    private void uploadAllSavedRoutes(String mTeamUid) {
        // todo get the list locally then upload them one by one
        try {
            List<Route> routes = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, this);
            routes.forEach(route -> mTDb.updateRoute(mTeamUid, route));
            Log.i(TAG, "uploadAllSavedRoutes: uploaded all routes to database");

            RoutesManager.AsyncTaskSaveRoutes saver = new RoutesManager.AsyncTaskSaveRoutes();
            saver.execute(routes, this);
        } catch (IOException e) {
            Log.e(TAG, "uploadAllSavedRoutes: error reading list from file", e);
        }
    }

    private void updateTeamUidLocallyAndDatabase(String senderTeamUid) {
        mCurrentUser.updateTeamUid(senderTeamUid);
        mUDb.updateUserTeamUidInDatabase(mCurrentUser, senderTeamUid);
        Utils.saveString(preferences, IUser.TEAM_UID_KEY, senderTeamUid);
    }

    private void dismissSelectedInvitation() {
        mInvitations.remove(selectedIdx);
        mInvitationsListView.setSelector(android.R.color.background_light);
        if (mInvitations.size() == 0) {
            mNoInvitationsTextView.setVisibility(View.VISIBLE);
        }
        mCurrentSelectedInvitation = null;
        mAdapter.notifyDataSetChanged();
    }

    private void setDeclineButtonOnClickListener() {
        declineBtn.setOnClickListener(view -> {
            if (mCurrentSelectedInvitation == null) {
                Utils.showToast(this, "Please select an invitation", Toast.LENGTH_SHORT);
            } else {
                mCurrentSelectedInvitation.setStatus(InvitationStatus.DECLINED);
                updateInvitations();
                dismissSelectedInvitation();
            }
        });
    }

    private void updateInvitations() {
        mIDb.updateInvitationForSendingUser(mCurrentSelectedInvitation);
        mIDb.updateInvitationForReceivingUser(mCurrentSelectedInvitation);
    }

    private void selectInvitation(AdapterView<?> parent, View view, int position, long id) {
        mInvitationsListView.setSelector(android.R.color.holo_red_light);
        selectedIdx = position;
        mCurrentSelectedInvitation = (Invitation) mAdapter.getItem(position);
        Log.d(TAG, "selectInvitation: item selected: " + mCurrentSelectedInvitation.fromName());
    }

    private void setUpServices() {
        mAuth = FirebaseApplicationWWR.getAuthFactory().createAuthService();
        mIDb = (InvitationsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.INVITATIONS);
        mIDb.register(this);

        mTDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);

        mUDb = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);

        mMessaging = FirebaseApplicationWWR.getMessagingFactory().createMessagingService(this, mIDb);
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
        } else {
            mNoInvitationsTextView.setVisibility(View.VISIBLE);
        }
    }
}
