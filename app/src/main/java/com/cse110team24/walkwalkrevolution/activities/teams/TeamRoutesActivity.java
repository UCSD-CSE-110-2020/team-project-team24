package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 * Handles displaying the currently signed in user's teammates' routes.
 * <ol>
 *     <li>Sets up RecyclerViewAdapter and gets first batch of team routes, ordered by teammate name</li>
 *     <li>After a predetermined amount of scrolling or time, requests more routes from the database,
 *     starting after route last queried</li>
 * </ol>
 */
public class TeamRoutesActivity extends AppCompatActivity implements TeamsDatabaseServiceObserver {
    private static final String TAG = "WWR_TeamRoutesActivity";

    private TeamsDatabaseService mTeamsDb;
    private SharedPreferences mPreferences;
    private DocumentSnapshot mLastRouteDocSnapshot;

    private List<Route> mTeamRoutes = new ArrayList<>();
    private TeamRoutesRecyclerViewAdapter adapter;
    private RecyclerView mTeamRv;

    private IUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_routes);

        mPreferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        setUpDatabase();
        getCurrentUser();
        getUIElements();
        getTeamRoutes();
    }

    private void setUpDatabase() {
        mTeamsDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mTeamsDb.register(this);
    }

    private void getCurrentUser() {
        String teamUid = Utils.getString(mPreferences, IUser.TEAM_UID_KEY);
        String displayName = Utils.getString(mPreferences, IUser.USER_NAME_KEY);
        String email = Utils.getString(mPreferences, IUser.EMAIL_KEY);
        mCurrentUser = FirebaseUserAdapter.builder()
                .addDisplayName(displayName)
                .addEmail(email)
                .addTeamUid(teamUid)
                .build();
    }

    private void getTeamRoutes() {
        mTeamsDb.getUserTeamRoutes(mCurrentUser.teamUid(), mCurrentUser.getDisplayName(), 5, mLastRouteDocSnapshot);
    }

    private void getUIElements() {
        mTeamRv = findViewById(R.id.recycler_view_team_routes);
        adapter = new TeamRoutesRecyclerViewAdapter(this, mTeamRoutes, mPreferences);
        mTeamRv.setAdapter(adapter);
        mTeamRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {
        routes.forEach(route -> Log.d(TAG, "onRoutesRetrieved: route " + route));
        mTeamRoutes.addAll(routes);
        mLastRouteDocSnapshot = lastRoute;
        adapter.notifyDataSetChanged();
        // notify adapter data changed
    }

    @Override
    public void onTeamRetrieved(ITeam team) {
        // nada
    }
}
