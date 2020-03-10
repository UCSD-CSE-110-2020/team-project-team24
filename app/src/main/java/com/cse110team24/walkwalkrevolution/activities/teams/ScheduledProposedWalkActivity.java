package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeamWalksObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeammateStatus;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.util.List;

public class ScheduledProposedWalkActivity extends AppCompatActivity implements TeamsTeamWalksObserver {
    private static final String TAG = "WWR_ScheduledProposedWalkActivity";

    private Button acceptBtn;
    private Button declineCannotMakeItBtn;
    private Button declineNotInterestedBtn;
    private Button scheduleWalkBtn;
    private Button withdrawWalkBtn;

    private TeamsDatabaseService mDb;
    private UsersDatabaseService uDb;
    private TeamWalk mCurrentTeamWalk;
    private IUser mCurrentUser;
    private TeammateStatus mCurrentUserStatus;

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
        getCurrentUser();
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
    }

    private void getTeamUid() {
        mPreferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        mTeamUid = mPreferences.getString(IUser.TEAM_UID_KEY, null);
        Log.d(TAG, "getTeamUid: team uid found, retrieving team");
        // TODO implement onTeamRetrieved
        mDb.getUserTeam(mTeamUid, preferences.getString(IUser.USER_NAME_KEY, ""));
    }

    private void getCurrentUser() {
        getTeamUid();
        mCurrentUser = FirebaseUserAdapter.builder()
                .addDisplayName(mPreferences.getString(IUser.USER_NAME_KEY, ""))
                .addTeamUid(mTeamUid)
                .addEmail(mPreferences.getString(IUser.EMAIL_KEY, ""))
                .build();
    }

    @Override
    public void onTeamWalksRetrieved(List<TeamWalk> teamWalks) {
        if(teamWalks.size() == 0) {
            findViewById(R.id.no_proposed_or_scheduled_walks_tv).setVisibility(View.VISIBLE);
            return;
        }

        mCurrentTeamWalk = teamWalks.get(0);
        displayAppropriateUIViewsForUser();
    }

    private void displayAppropriateUIViewsForUser() {
        displayCommonUIViews();

        // current user proposed the walk
        if (mCurrentTeamWalk.getProposedBy().equals(mCurrentUser.getDisplayName())) {
            Log.i(TAG, "displayAppropriateUIViewsForUser: user proposed route");
            displayProposerUIViews();
        } else {
            Log.i(TAG, "displayAppropriateUIViewsForUser: user was invited to route");
            displayTeammateUIViews();
        }
    }

    private void displayProposerUIViews() {

    }

    private void displayTeammateUIViews() {
        findViewById(R.id.schedule_propose_linear_layout_status_buttons).setVisibility(View.VISIBLE);
        addClickListenersTeammateButtons();
        displayProposedByViews();
    }

    private void addClickListenersTeammateButtons() {
        mCurrentUserStatus = TeammateStatus.get(mPreferences.getString(IUser.STATUS_TEAM_WALK, ""));
        highLightCurrentStatusButton();

        acceptBtn = findViewById(R.id.schedule_propose_btn_accept);
        acceptBtn.setOnClickListener(v -> acceptBtnOnClickListener());

        declineCannotMakeItBtn = findViewById(R.id.schedule_propose_btn_decline_cant_come);
        declineCannotMakeItBtn.setOnClickListener(v -> declineReasonTimeBtnOnClickListener());

        declineNotInterestedBtn = findViewById(R.id.schedule_propose_btn_decline_not_interested);
        declineNotInterestedBtn.setOnClickListener(v -> declineReasonBadBtnOnClickListener());
    }

    private void highLightCurrentStatusButton() {
        if(mCurrentUserStatus == null)
            return;

        switch (mCurrentUserStatus) {
            case ACCEPTED:
                // TODO: 3/10/20 highlight accepted button
                break;
            case DECLINED_NOT_GOOD:
                // TODO: 3/10/20 highlight declined not good reason button
                break;

            case DECLINED_SCHEDULING_CONFLICT:
                // TODO: 3/10/20 highlight declined bad time button
                break;
        }
    }

    private void updateStatus(TeammateStatus newStatus) {
        if (mCurrentUserStatus == newStatus) {
            Utils.showToast(this, "Please pick a new status", Toast.LENGTH_SHORT);
        } else {
            mPreferences.edit().putString(IUser.STATUS_TEAM_WALK, newStatus.getReason()).apply();
            mDb.changeTeammateStatusForLatestWalk(mCurrentUser, mCurrentTeamWalk, newStatus);
            highLightCurrentStatusButton();
        }
    }

    private void acceptBtnOnClickListener() {
        updateStatus(TeammateStatus.ACCEPTED);
    }

    private void declineReasonTimeBtnOnClickListener() {
        updateStatus(TeammateStatus.DECLINED_SCHEDULING_CONFLICT);
    }

    private void declineReasonBadBtnOnClickListener() {
        updateStatus(TeammateStatus.DECLINED_NOT_GOOD);
    }

    private void displayProposedByViews() {
        findViewById(R.id.schedule_propose_tv_proposed_by_prompt).setVisibility(View.VISIBLE);
        TextView proposedByDisplayTv = findViewById(R.id.schedule_propose_tv_proposed_by_display);
        proposedByDisplayTv.setVisibility(View.VISIBLE);
        proposedByDisplayTv.setText(mCurrentTeamWalk.getProposedBy());
    }

    private void displayCommonUIViews() {
        Route proposedRoute = mCurrentTeamWalk.getProposedRoute();
        displayRouteDetails(proposedRoute);
        displayProposedDateAndTime();
    }

    private void displayRouteDetails(Route proposedRoute) {
        displayRouteName(proposedRoute);
        displayRouteStartingLocation(proposedRoute);
    }

    private void displayRouteName(Route proposedRoute) {
        findViewById(R.id.schedule_propose_tv_walk_name_prompt).setVisibility(View.VISIBLE);
        TextView walkName = findViewById(R.id.schedule_propose_tv_walk_name_display);
        walkName.setVisibility(View.VISIBLE);
        walkName.setText(proposedRoute.getTitle());
    }

    private void displayRouteStartingLocation(Route proposedRoute) {
        findViewById(R.id.schedule_propose_tv_starting_loc).setVisibility(View.VISIBLE);
        TextView startingLocation = findViewById(R.id.schedule_propose_tv_starting_loc_display);
        startingLocation.setVisibility(View.VISIBLE);
        startingLocation.setText(proposedRoute.getStartingLocation());
    }

    private void displayProposedDateAndTime() {
        findViewById(R.id.schedule_propose_tv_walk_date).setVisibility(View.VISIBLE);
        TextView walkDate = findViewById(R.id.schedule_propose_tv_walk_date_display);
        walkDate.setVisibility(View.VISIBLE);
        String formattedDateAndTime = Utils.formatDateIntoReadableString("MM/dd/yyyy 'at' HH:mm a", mCurrentTeamWalk.getProposedDateAndTime().toDate());
        walkDate.setText(formattedDateAndTime);
    }

}
