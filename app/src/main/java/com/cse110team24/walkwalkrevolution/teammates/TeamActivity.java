package com.cse110team24.walkwalkrevolution.teammates;

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
import com.cse110team24.walkwalkrevolution.invitations.InvitationsActivity;
import com.cse110team24.walkwalkrevolution.invitations.InviteTeamMemberActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.RoutesActivity;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamActivity extends AppCompatActivity implements TeamsDatabaseServiceObserver, UsersDatabaseServiceObserver {
    private static final String TAG = "WWR_TeamActivity";
    private Button sendInviteBtn;
    private Button seeInvitationsBtn;
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
            IUser currUser = FirebaseUserAdapter.builder()
                    .addEmail(Utils.getString(preferences, IUser.EMAIL_KEY))
                    .build();
            uDb.getUserData(currUser);
        } else {
            Log.d(TAG, "getTeamUid: team uid found, retrieving team");
            mDb.getUserTeam(mTeamUid, preferences.getString(IUser.USER_NAME_KEY, ""));
        }
    }

    private void setUpServices() {
        mDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mDb.register(this);

        uDb = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
        uDb.register(this);
    }

    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        seeInvitationsBtn = findViewById(R.id.btn_pending_invites);
        noTeamMessage = findViewById(R.id.text_no_teammates);
        teammatesList = findViewById(R.id.list_members_in_team);
        teammatesListViewAdapter = new TeammatesListViewAdapter(this, mTeam.getTeam());
        noTeamMessage.setVisibility(View.GONE);
        teammatesList.setAdapter(teammatesListViewAdapter);
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
        TeammatesListViewAdapter teammatesListViewAdapter = new TeammatesListViewAdapter(this, users);
        teammatesList.setAdapter(teammatesListViewAdapter);

    }

    @Override
    public void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {
        // TODO: 3/6/20 ?
    }

    private void showNoTeamToast() {
        Toast.makeText(this, "You don't have a team. Try sending an invitation!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
        if (userDataMap != null) {
            mTeamUid = (String) userDataMap.get(IUser.TEAM_UID_KEY);
            Utils.saveString(preferences, IUser.TEAM_UID_KEY, mTeamUid);
            if (mTeamUid == null) {
                showNoTeamToast();
            } else {
                getTeamUid();
            }
        }
    }

    @Override
    public void onUserExists(IUser otherUser) {

    }

    @Override
    public void onUserDoesNotExist() {

    }
}
