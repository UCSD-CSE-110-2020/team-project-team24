package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;

import java.util.List;
import java.util.Map;

// TODO: 3/3/20 change to  InvitationsDatabaseServiceObserver
public class InvitationsActivity extends AppCompatActivity implements DatabaseServiceObserver {

    // TODO: 3/3/20 change to InvitationsDatabaseService
    private DatabaseService mDb;
    private MessagingService mMessagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        setUpServices();
    }

    private void setUpServices() {
        mDb = FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService();
        mMessagingService = FirebaseApplicationWWR.getMessagingServiceFactory().createMessagingService(this, mDb);
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

    @Override
    public void onUserPendingInvitations(List<Invitation> invitations) {

    }
}
