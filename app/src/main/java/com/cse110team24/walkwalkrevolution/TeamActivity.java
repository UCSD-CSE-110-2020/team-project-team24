package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class TeamActivity extends AppCompatActivity implements DatabaseServiceObserver {
    private static final String TAG = "TeamActivity";
    private Button sendInviteBtn;
    private BottomNavigationView bottomNavigationView;
    private DatabaseService mDb;

    SharedPreferences mPreferences;

    private ITeam mTeam;
    private String mTeamUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        setUpServices();
        getTeamUid();
        getUIFields();
        setButtonClickListeners();
    }

    private void getTeamUid() {
        mPreferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        mTeamUid = mPreferences.getString(IUser.TEAM_UID_KEY, null);
        if (mTeamUid == null) {
            showNoTeamToast();
        } else {
            Log.d(TAG, "getTeamUid: team uid found, retrieving team");
            mDb.getUserTeam(mTeamUid);
        }
    }

    private void setUpServices() {
        mDb = FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService();
        mDb.register(this);
    }
    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

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

    // TODO: 3/2/20 update UI now that team is retrieved
    @Override
    public void onTeamRetrieved(ITeam team) {
        mTeam = team;
        // start updating UI

    }

    @Override
    public void onFieldRetrieved(Object object) {

    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
    }


    private void showNoTeamToast() {
        Toast.makeText(this, "You don't have a team -^-. Try sending an invitation!", Toast.LENGTH_LONG).show();
    }
}
