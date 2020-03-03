package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ListViewAutoScrollHelper;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.user.UserBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: 3/3/20 change to implement TeamsDatabaseServiceObserver
public class TeamActivity extends AppCompatActivity implements DatabaseServiceObserver {
    private static final String TAG = "TeamActivity";
    private Button sendInviteBtn;
    private Button seeInvitationsBtn;
    private BottomNavigationView bottomNavigationView;
    // TODO: 3/3/20 change to TeamsDatabaseService
    private DatabaseService mDb;

    SharedPreferences mPreferences;

    private ITeam mTeam;
    private String mTeamUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        setUpServices();
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
            mDb.getUserTeam(mTeamUid);
        }
    }

    private void setUpServices() {
        mDb = FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mDb.register(this);
    }
    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        seeInvitationsBtn = findViewById(R.id.btn_pending_invites);
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
        List<IUser> users = mTeam.getTeam();
        TextView noTeamMessage = findViewById(R.id.text_no_teammates);
        if(users.size() == 1) {
            noTeamMessage.setVisibility(View.VISIBLE);
        }else {
            noTeamMessage.setVisibility(View.GONE);
        }
        ListView teammatesList = findViewById(R.id.list_members_in_team);
        ListviewAdapter listviewAdapter = new ListviewAdapter(this, users);
        teammatesList.setAdapter(listviewAdapter);
    }

    /*private void fakeTesting() {
        List<IUser> users = new ArrayList<>();
        UserBuilder builder1 = new FirebaseUserAdapter.Builder();
        UserBuilder builder2 = new FirebaseUserAdapter.Builder();
        UserBuilder builder3 = new FirebaseUserAdapter.Builder();
        builder1.addDisplayName("Samuel Jackson");
        builder2.addDisplayName("Takahashi Rie");
        builder3.addDisplayName("Dennis");
        users.add(builder1.build());
        users.add(builder2.build());
        users.add(builder3.build());
        ListviewAdapter listviewAdapter = new ListviewAdapter(this, users);
        ListView teammatesList = (ListView) findViewById(R.id.list_members_in_team);
        teammatesList.setAdapter(listviewAdapter);
    }*/

    @Override
    public void onFieldRetrieved(Object object) {
    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
    }

    @Override
    public void onUserPendingInvitations(List<Invitation> invitations) {
    }


    private void showNoTeamToast() {
        Toast.makeText(this, "You don't have a team -^-. Try sending an invitation!", Toast.LENGTH_LONG).show();
    }
}
