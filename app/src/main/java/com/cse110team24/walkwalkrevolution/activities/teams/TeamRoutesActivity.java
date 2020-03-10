package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteRecyclerViewAdapter;
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
import java.util.Collections;
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
    public static final int REQUEST_CODE = 5120;

    private TeamsDatabaseService mTeamsDb;
    private SharedPreferences mPreferences;
    private DocumentSnapshot mLastRouteDocSnapshot;

    private List<Route> mTeamRoutes = new ArrayList<>();
    private RouteRecyclerViewAdapter adapter;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RouteDetailsActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: returning to team activity to record walk");
            returnToTeamActivityForWalk(data);
        }
    }

    private void returnToTeamActivityForWalk(Intent data) {
        setResult(Activity.RESULT_OK, data);
        finish();
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
        mTeamsDb.getUserTeamRoutes(mCurrentUser.teamUid(), mCurrentUser.getDisplayName(), 10, mLastRouteDocSnapshot);
    }

    private void getUIElements() {
        mTeamRv = findViewById(R.id.recycler_view_team_routes);
        setScrollListener(mTeamRv);
        adapter = new RouteRecyclerViewAdapter(this, mTeamRoutes, mPreferences);
        mTeamRv.setAdapter(adapter);
        mTeamRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setScrollListener(RecyclerView view) {
//        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//             @Override
//             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                 super.onScrollStateChanged(recyclerView, newState);
//             }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 100 && Utils.checkNotNull(mLastRouteDocSnapshot)) {
//                    getTeamRoutes();
//                }
//            }
//
//        });
    }

    @Override
    public void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {
        routes.forEach(route -> Log.d(TAG, "onRoutesRetrieved: route " + route));
        mTeamRoutes.addAll(routes);
        Collections.sort(mTeamRoutes);
        mLastRouteDocSnapshot = lastRoute;
        adapter.notifyDataSetChanged();

        if (Utils.checkNotNull(lastRoute)) {
            getTeamRoutes();
        }
    }

    @Override
    public void onTeamRetrieved(ITeam team) {
        // nada
    }
}
