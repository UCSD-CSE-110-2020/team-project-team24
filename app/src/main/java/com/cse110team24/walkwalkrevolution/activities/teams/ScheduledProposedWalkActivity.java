package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamWalksObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ScheduledProposedWalkActivity extends AppCompatActivity implements TeamsTeamWalksObserver {
    private static final String TAG = "WWR_ScheduledProposedWalkActivity";

    private TextView walkName;
    private TextView startingLocation;
    private TextView walkDate;
    private Button acceptBtn;
    private Button declineCannotMakeItBtn;
    private Button declineNotInterestedBtn;
    private Button scheduleWalkBtn;
    private Button withdrawWalkBtn;

    private TeamsDatabaseService mDb;
    private UsersDatabaseService uDb;

    SharedPreferences mPreferences;

    private String mTeamUid;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_proposed_walk);

        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        setUpServices();
        getUIFields();
        getTeamUid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDb.getLatestTeamWalksDescendingOrder(mTeamUid, 1);
    }

    private void setUpServices() {
        mDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mDb.register(this);

        uDb = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
    }

    private void getUIFields() {
        walkName = findViewById(R.id.schedule_propose_tv_walk_name_display);
        startingLocation = findViewById(R.id.schedule_propose_tv_starting_loc_display);
        walkDate = findViewById(R.id.schedule_propose_tv_walk_date_display);
        acceptBtn = findViewById(R.id.accept_btn);
        declineCannotMakeItBtn = findViewById(R.id.decline_cant_come_btn);
        declineNotInterestedBtn = findViewById(R.id.decline_not_interested_btn);
        //scheduleWalkBtn;
        //withdrawWalkBtn;
    }

    private void getTeamUid() {
        mPreferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        mTeamUid = mPreferences.getString(IUser.TEAM_UID_KEY, null);
        Log.d(TAG, "getTeamUid: team uid found, retrieving team");
        // TODO implement onTeamRetrieved
        mDb.getUserTeam(mTeamUid, preferences.getString(IUser.USER_NAME_KEY, ""));
    }

    @Override
    public void onTeamWalksRetrieved(List<TeamWalk> teamWalks) {
        Route proposedRoute = teamWalks.get(0).getProposedRoute();
        walkName.setText(proposedRoute.getTitle());
        startingLocation.setText(proposedRoute.getStartingLocation());
        walkDate.setText(teamWalks.get(0).getProposedDateAndTime().toString());
    }
}
