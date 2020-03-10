package com.cse110team24.walkwalkrevolution.activities.teams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.activities.invitations.InvitationsActivity;
import com.cse110team24.walkwalkrevolution.activities.invitations.InviteTeamMemberActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RoutesActivity;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeammatesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity implements TeamsTeammatesObserver {
    private static final String TAG = "WWR_TeamActivity";

    private Button sendInviteBtn;
    private Button seeInvitationsBtn;
    private Button seeTeammateRoutesBtn;
    private Button seeScheduledWalksBtn;
    private BottomNavigationView bottomNavigationView;

    private TeamsDatabaseService mDb;
    private UsersDatabaseService uDb;

    SharedPreferences mPreferences;

    private ITeam mTeam = new TeamAdapter(new ArrayList<>());
    private String mTeamUid;

    private SharedPreferences preferences;

    //The following three fields are for fakeTesting() only, should delete afterwards.
    private ListView teammatesList;
    private TeammatesListViewAdapter teammatesListViewAdapter;
    TextView noTeamMessage;
    public Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        setUpServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUIFields();
        getTeamUid();
        setButtonClickListeners();
        seeInvitationsBtn.setOnClickListener(view -> {
            launchInvitationsActivity(view);
        });
    }

    private void launchInvitationsActivity(View view) {
        Intent intent = new Intent(this, InvitationsActivity.class);
        startActivity(intent);
    }

    private void getTeamUid() {
        mPreferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        mTeamUid = mPreferences.getString(IUser.TEAM_UID_KEY, null);
        if (mTeamUid == null) {
            showNoTeamToast();
        } else {
            Log.d(TAG, "getTeamUid: team uid found, retrieving team");
            seeTeammateRoutesBtn.setEnabled(true);
            mDb.getUserTeam(mTeamUid, preferences.getString(IUser.USER_NAME_KEY, ""));
        }
    }

    private void setUpServices() {
        mDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mDb.register(this);

        uDb = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
    }

    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_team_activity_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        seeInvitationsBtn = findViewById(R.id.btn_team_activity_pending_invites);
        seeTeammateRoutesBtn = findViewById(R.id.btn_team_activity_see_teammate_routes);
        seeScheduledWalksBtn = findViewById(R.id.btn_scheduled_walks);
        noTeamMessage = findViewById(R.id.text_no_teammates);
        teammatesList = findViewById(R.id.list_members_in_team);
        teammatesListViewAdapter = new TeammatesListViewAdapter(this, mTeam.getTeam(), preferences);
        noTeamMessage.setVisibility(View.GONE);
        teammatesList.setAdapter(teammatesListViewAdapter);
    }

    private void setButtonClickListeners() {
        setInviteButtonOnClick();
        setBottomNavItemSelectedListener();
        setSeeTeamRoutesOnClick();
        setSeeScheduledWalksOnClick();
    }

    private void setInviteButtonOnClick() {
        sendInviteBtn.setOnClickListener(view -> {
            launchInviteRouteActivity();
        });
    }

    private void setBottomNavItemSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.action_home) {
                Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
            if(menuItem.getItemId() == R.id.action_routes_list) {
                Intent myIntent = new Intent(getApplicationContext(), RoutesActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }

            return true;
        });
    }

    private void setSeeTeamRoutesOnClick() {
        seeTeammateRoutesBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, TeamRoutesActivity.class));
        });
    }

    private void setSeeScheduledWalksOnClick() {
        seeScheduledWalksBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ScheduledProposedWalkActivity.class));
        });
    }

    private void launchInviteRouteActivity() {
        Intent intent = new Intent(this, InviteTeamMemberActivity.class);
        startActivity(intent);
    }

    // TODO: 3/8/20 stop initializing new adapter, notify data set changed instead
    @Override
    public void onTeamRetrieved(ITeam team) {
        mTeam = team;
        List<IUser> users = mTeam.getTeam();
        TextView noTeamMessage = findViewById(R.id.text_no_teammates);
        if(users.size() == 0) {
            noTeamMessage.setVisibility(View.VISIBLE);
        }else {
            noTeamMessage.setVisibility(View.GONE);
        }
        ListView teammatesList = findViewById(R.id.list_members_in_team);
        TeammatesListViewAdapter teammatesListViewAdapter = new TeammatesListViewAdapter(this, users, preferences);
        teammatesList.setAdapter(teammatesListViewAdapter);

    }

    private void showNoTeamToast() {
        Toast.makeText(this, "You don't have a team. Try sending an invitation!", Toast.LENGTH_LONG).show();
    }
}
