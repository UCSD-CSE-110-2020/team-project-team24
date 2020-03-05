package com.cse110team24.walkwalkrevolution;

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

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity implements TeamsDatabaseServiceObserver {
    private static final String TAG = "TeamActivity";
    private Button sendInviteBtn;
    private Button seeInvitationsBtn;
    private BottomNavigationView bottomNavigationView;

    private TeamDatabaseService mDb;

    SharedPreferences mPreferences;

    private ITeam mTeam = new TeamAdapter(new ArrayList<>());
    private String mTeamUid;

    private SharedPreferences preferences;

    //The following three fields are for fakeTesting() only, should delete afterwards.
    private ListView teammatesList;
    private ListviewAdapter listviewAdapter;
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
        getTeamUid();
        getUIFields();
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
            mDb.getUserTeam(mTeamUid, preferences.getString(IUser.USER_NAME_KEY, ""));
        }
    }

    private void setUpServices() {
        mDb = (TeamDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mDb.register(this);
    }

    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        seeInvitationsBtn = findViewById(R.id.btn_pending_invites);
        noTeamMessage = findViewById(R.id.text_no_teammates);
        teammatesList = findViewById(R.id.list_members_in_team);
        listviewAdapter = new ListviewAdapter(this, mTeam.getTeam());
        teammatesList.setAdapter(listviewAdapter);
    }

    private void setButtonClickListeners() {
        setInviteButtonOnClick();
        setBottomNavItemSelectedListener();
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
    private void launchInviteRouteActivity() {
        Intent intent = new Intent(this, InviteTeamMemberActivity.class);
        startActivity(intent);
    }

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
        ListviewAdapter listviewAdapter = new ListviewAdapter(this, users);
        teammatesList.setAdapter(listviewAdapter);

    }

    private void showNoTeamToast() {
        Toast.makeText(this, "You don't have a team. Try sending an invitation!", Toast.LENGTH_LONG).show();
    }
}
