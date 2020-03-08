package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class TeamRoutesActivity extends AppCompatActivity implements TeamsDatabaseServiceObserver {
    private static final String TAG = "WWR_TeamRoutesActivity";

    private TeamsDatabaseService mTeamsDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_routes);

        mTeamsDb = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mTeamsDb.register(this);
    }

    @Override
    public void onRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {
        routes.forEach(route -> Log.d(TAG, "onRoutesRetrieved: route " + route));
    }

    @Override
    public void onTeamRetrieved(ITeam team) {
        // nada
    }
}
