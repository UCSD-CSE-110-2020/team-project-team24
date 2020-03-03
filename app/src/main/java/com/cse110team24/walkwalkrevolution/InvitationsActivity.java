package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class InvitationsActivity extends AppCompatActivity implements InvitationsDatabaseServiceObserver {
    private static final String TAG = "InvitationsActivity";

    private InvitationsDatabaseService mDb;
    private MessagingService mMessagingService;
    private AuthService mAuth;

    private SharedPreferences preferences;
    private IUser mCurrentUser;

    private List<Invitation> mInvitations = new ArrayList<>();
    private InvitationsListViewAdapter mAdapter;
    private ListView mInvitationsListView;
    private TextView mNoInvitationsTextView;

    private Invitation mCurrentSelectedInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        getUIElements();
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
    }

    private void selectInvitation(AdapterView<?> parent, View view, int position, long id) {
        mCurrentSelectedInvitation = (Invitation) mAdapter.getItem(position);
        Log.d(TAG, "selectInvitation: item selected: " + mCurrentSelectedInvitation.fromName());
    }

    private void setUpServices() {
        mAuth = FirebaseApplicationWWR.getAuthServiceFactory().createAuthService();
        mDb = (InvitationsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.INVITATIONS);
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

    // TODO: 3/3/20 display invitations and allow each one to be clickable
    @Override
    public void onUserPendingInvitations(List<Invitation> invitations) {
        Toast.makeText(this, "retrieved your invitations successfully!", Toast.LENGTH_LONG).show();
        mInvitations.addAll(invitations);
        mAdapter.notifyDataSetChanged();
        if (!invitations.isEmpty()) {
            mNoInvitationsTextView.setVisibility(View.GONE);
        }
    }
}
