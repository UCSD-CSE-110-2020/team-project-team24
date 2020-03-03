package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ListViewAutoScrollHelper;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Map;

public class TeamActivity extends AppCompatActivity implements DatabaseServiceObserver {

    private Button sendInviteBtn;
    private BottomNavigationView bottomNavigationView;
    private DatabaseService mDb;

    SharedPreferences preferences;

    private ITeam mTeam;
    private String teamUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        mDb = FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService();
        mDb.register(this);
        getUIFields();
        setButtonClickListeners();
    }
    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

    }
    private void setButtonClickListeners() {
        setInviteButtonOnClick();
        // setBottomNavigationOnClick();
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

  /*  public void launchGotoHomeActivity() {
        setResult(Activity.RESULT_CANCELED);
        transitionWithAnimation();
    }
    public void launchGoToRoutesActivity() {
        setResult(Activity.RESULT_CANCELED);
        transitionWithAnimation();
    }

    private void transitionWithAnimation() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
    */

    // TODO: 3/2/20 update UI now that team is retrieved
    @Override
    public void onTeamRetrieved(ITeam team) {
        mTeam = team;
        List<IUser> users = mTeam.getTeam();
        ListView teammatesList = (ListView) findViewById(R.id.list_members_in_team);
        ListviewAdapter listviewAdapter = new ListviewAdapter(this, users);
        teammatesList.setAdapter(listviewAdapter);
        // start updating UI
    }

    @Override
    public void onFieldRetrieved(Object object) {

    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
    }
}
