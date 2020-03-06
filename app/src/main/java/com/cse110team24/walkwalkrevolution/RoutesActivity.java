package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cse110team24.walkwalkrevolution.HomeActivity.APP_PREF;

public class RoutesActivity extends AppCompatActivity {
    public static final String TAG = "WWR_RoutesActivity";

    public static final String LIST_SAVE_FILE = ".WWR_route_list_data";
    public static final String SAVE_FILE_KEY = "save_file";
    public static final int REQUEST_CODE = 11;

    private RouteAdapter adapter;
    private RecyclerView rvRoutes;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;

    private UsersDatabaseService mUsersDbService;
    private TeamDatabaseService mTeamsDbService;

    private SharedPreferences preferences;

    private List<Route> routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        preferences = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getUIElements();
        setListeners();

        mUsersDbService = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
        mTeamsDbService = (TeamDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveListAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveListAsync();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForExistingSavedRoutes();
        configureRecyclerViewAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RouteDetailsActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            returnToHomeForWalk(data);
        } else if (requestCode == SaveRouteActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handleNewRoute(data);
        }

    }

    private void handleNewRoute(Intent data) {
        Route newRoute = (Route) data.getSerializableExtra(SaveRouteActivity.NEW_ROUTE_KEY);
        // upload the new route to db
        uploadRouteIfTeamExists(newRoute);
        routes.add(newRoute);
        Collections.sort(routes);
        adapter.notifyDataSetChanged();
        saveListAsync();
    }

    // only upload the route if the team already exists
    private void uploadRouteIfTeamExists(Route newRoute) {
        String teamUid = Utils.getString(preferences, IUser.TEAM_UID_KEY);
        if (teamUid != null) {
            mTeamsDbService.uploadRoute(teamUid, newRoute);
        }
    }

    private void returnToHomeForWalk(Intent data) {
        setResult(Activity.RESULT_OK, data);
        transitionWithAnimation();
    }

/*    public void launchGoToHomeActivity() {
        setResult(Activity.RESULT_CANCELED);
        transitionWithAnimation();
    }

 */

    private void transitionWithAnimation() {
        saveListAsync();
        Intent walkIntent = new Intent(getApplicationContext(), HomeActivity.class);
        walkIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(walkIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void getUIElements() {
        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        rvRoutes = findViewById(R.id.recycler_view);
    }

    private void setListeners() {
        setFabOnClickListener();
        setBottomNavItemSelectedListener();
    }

    private void setFabOnClickListener() {
        fab.setOnClickListener(view -> {
            launchSaveActivity();
        });
    }

    private void launchSaveActivity() {
        Intent intent = new Intent(this, SaveRouteActivity.class);
        startActivityForResult(intent, SaveRouteActivity.REQUEST_CODE);
    }

    private void setBottomNavItemSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.action_home) {
                Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }
            if(menuItem.getItemId() == R.id.action_team) {
                Intent myIntent = new Intent(getApplicationContext(), TeamActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }

            return true;
        });
    }

    private void checkForExistingSavedRoutes() {
        String filename = getSaveFilename();
        try {
            List<Route> tempList = RoutesManager.readList(filename, this);
            routes.clear();
            routes.addAll(tempList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "checkForExistingSavedRoutes: no routes found ", e);
        }
        Log.i(TAG, "checkForExistingSavedRoutes: list of saved routes found with size " + routes.size());
    }

    private String getSaveFilename() {
        String filename = getIntent().getStringExtra(SAVE_FILE_KEY);
        return (filename == null) ? LIST_SAVE_FILE : filename;
    }

    private void configureRecyclerViewAdapter() {
        Collections.sort(routes);
        adapter = new RouteAdapter(routes, this);
        rvRoutes.setAdapter(adapter);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void saveListAsync() {
        RoutesManager.AsyncTaskSaveRoutes saver = new RoutesManager.AsyncTaskSaveRoutes();
        saver.execute(routes, this);
    }
}
