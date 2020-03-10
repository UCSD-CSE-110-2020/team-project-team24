package com.cse110team24.walkwalkrevolution.activities.userroutes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.teams.TeamRoutesActivity;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.activities.teams.TeamActivity;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cse110team24.walkwalkrevolution.HomeActivity.APP_PREF;

/**
 * Handles displaying and saving user's routes.
 * <p>Integrates {@link TeamsDatabaseService}</p>
 *
 * <ol>
 *     <li>Saves and displays user's list of routes locally</li>
 *     <li>If a user has a team, uploads newly added routes to user's team in database</li>
 * </ol>
 */
public class RoutesActivity extends AppCompatActivity {
    public static final String TAG = "WWR_RoutesActivity";

    public static final String LIST_SAVE_FILE = ".WWR_route_list_data";
    public static final String SAVE_FILE_KEY = "save_file";
    public static final int REQUEST_CODE = 11;

    private RouteRecyclerViewAdapter adapter;
    private RecyclerView rvRoutes;
    private BottomNavigationView bottomNavigationView;

    private TeamsDatabaseService mTeamsDbService;

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

        mTeamsDbService = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_routes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 3/8/20 detect click
        switch (item.getItemId()) {
            case R.id.action_create_walk:
                Log.i(TAG, "onOptionsItemSelected: clicked on add walk");
                launchSaveActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void transitionWithAnimation() {
        saveListAsync();
        Intent walkIntent = new Intent(getApplicationContext(), HomeActivity.class);
        walkIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(walkIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void getUIElements() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        rvRoutes = findViewById(R.id.recycler_view);
    }

    private void setListeners() {
        setBottomNavItemSelectedListener();
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
        adapter = new RouteRecyclerViewAdapter(this, routes, preferences);
        rvRoutes.setAdapter(adapter);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void saveListAsync() {
        RoutesManager.AsyncTaskSaveRoutes saver = new RoutesManager.AsyncTaskSaveRoutes();
        saver.execute(routes, this);
    }
}
