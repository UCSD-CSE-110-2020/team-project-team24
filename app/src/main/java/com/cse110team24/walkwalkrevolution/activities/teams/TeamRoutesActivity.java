package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

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
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamRoutesActivity extends AppCompatActivity implements TeamsDatabaseServiceObserver {
    private static final String TAG = "WWR_TeamRoutesActivity";

    private TeamsDatabaseService mTeamsDb;
    private SharedPreferences mPreferences;
    private List<Route> mTeamRoutes = new ArrayList<>();
    private DocumentSnapshot mLastRouteDocSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_routes);

        mPreferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        setUpDatabase();
        getTeamRoutes();
    }

    private void setUpDatabase() {
        mTeamsDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mTeamsDb.register(this);
    }

    private void getTeamRoutes() {
        String name = Utils.getString(mPreferences, IUser.USER_NAME_KEY);
        String teamUid = Utils.getString(mPreferences, IUser.TEAM_UID_KEY);
        mTeamsDb.getUserTeamRoutes(teamUid, name, 5, mLastRouteDocSnapshot);
    }

    @Override
    public void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {
        routes.forEach(route -> Log.d(TAG, "onRoutesRetrieved: route " + route));
        mTeamRoutes.addAll(routes);
        mLastRouteDocSnapshot = lastRoute;
        // notify adapter data changed
    }

    @Override
    public void onTeamRetrieved(ITeam team) {
        // nada
    }
}
