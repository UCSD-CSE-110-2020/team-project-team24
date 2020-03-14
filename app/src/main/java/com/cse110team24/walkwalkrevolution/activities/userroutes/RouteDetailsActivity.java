package com.cse110team24.walkwalkrevolution.activities.userroutes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.teams.InviteTeamToWalkActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.util.Locale;

public class RouteDetailsActivity extends AppCompatActivity {
    private static final String TAG = "WWR_RouteDetailsActivity";
    
    public static final String ROUTE_KEY = "route";
    public static final String ROUTE_IDX_KEY = "route_adapter_idx";
    public static final int REQUEST_CODE = 99;

    private Route mDisplayedRoute;
    private int routeIdx;
    private WalkStats mStats;

    private TextView startingLocPromptTv;
    private TextView routeTypePromptTv;
    private TextView terrainTypePromptTv;
    private TextView surfaceTypePromptTv;
    private TextView landTypePromptTv;
    private TextView difficultyPromptTv;
    private TextView recentStepsPromptTv;
    private TextView recentDistancePromptTv;
    private TextView recentTimeElapsedPromptTv;
    private TextView notesPromptTv;

    private TextView startingLocTv;
    private TextView routeTypeTv;
    private TextView terrainTypeTv;
    private TextView surfaceTypeTv;
    private TextView landTypeTv;
    private TextView difficultyTv;
    private TextView detailsPromptTv;
    private TextView recentStepsTv;
    private TextView recentDistanceTv;
    private TextView recentTimeElapsedTv;
    private TextView notesTv;
    private TextView neverWalkedPromptTv;
    private Button startWalkBtn;
    private TextView checkMarkTv;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        getRouteInfo();
        findUIElements();
        displayRouteInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 3/8/20 detect click
        switch (item.getItemId()) {
            case R.id.action_propose_walk:
                Log.i(TAG, "onOptionsItemSelected: clicked on propose walk");
                if (preferences.getString(IUser.TEAM_UID_KEY, null) != null) {
                    launchProposeTeamWalk();
                } else {
                    Utils.showToast(this, "You need a team to propose walks. Send or accept an invitation!", Toast.LENGTH_LONG);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchProposeTeamWalk() {
        Intent intent = new Intent(this, InviteTeamToWalkActivity.class)
            .putExtra(ROUTE_KEY, mDisplayedRoute)
            .putExtra(IUser.USER_NAME_KEY, Utils.getString(preferences, IUser.USER_NAME_KEY));
        startActivity(intent);
    }

    private void getRouteInfo() {
        mDisplayedRoute = (Route) getIntent().getSerializableExtra(ROUTE_KEY);
        routeIdx = getIntent().getIntExtra(ROUTE_IDX_KEY, 0);
        mStats = mDisplayedRoute.getStats();
        getSupportActionBar().setTitle(mDisplayedRoute.getTitle());
    }

    private void findUIElements() {
        startingLocPromptTv = findViewById(R.id.tv_starting_loc_prompt);
        startingLocTv = findViewById(R.id.tv_starting_loc);
        routeTypePromptTv = findViewById(R.id.tv_rte_type_prompt);
        routeTypeTv = findViewById(R.id.tv_rte_type);
        terrainTypePromptTv = findViewById(R.id.tv_terr_type_prompt);
        terrainTypeTv = findViewById(R.id.tv_terr_type);
        surfaceTypePromptTv = findViewById(R.id.tv_srfce_type_prompt);
        surfaceTypeTv = findViewById(R.id.tv_srfce_type);
        landTypePromptTv = findViewById(R.id.tv_lnd_type_prompt);
        landTypeTv = findViewById(R.id.tv_lnd_type);
        difficultyPromptTv = findViewById(R.id.tv_diff_prompt);
        difficultyTv = findViewById(R.id.tv_diff);
        recentStepsPromptTv = findViewById(R.id.tv_details_recent_steps_prompt);
        recentStepsTv = findViewById(R.id.tv_details_recent_steps);
        recentDistancePromptTv = findViewById(R.id.tv_details_recent_distance_prompt);
        recentDistanceTv = findViewById(R.id.tv_details_recent_distance);
        recentTimeElapsedPromptTv = findViewById(R.id.tv_details_recent_time_elapsed_prompt);
        recentTimeElapsedTv = findViewById(R.id.tv_details_recent_time_elapsed);
        notesPromptTv = findViewById(R.id.tv_notes_prompt);
        notesTv = findViewById(R.id.tv_notes);
        neverWalkedPromptTv = findViewById(R.id.tv_details_never_walked);
        startWalkBtn = findViewById(R.id.btn_details_start_walk);
        detailsPromptTv = findViewById(R.id.tv_details_recent_walk_prompt);
        checkMarkTv = findViewById(R.id.tv_previously_walked_checkmark);
        setStartWalkBtnOnClickListener();
    }

    private void displayRouteInformation() {
        displayStartingLocation();
        displayRouteEnvironment();
        displayLatestWalkStats();
        displayNotes();
    }

    private void displayStartingLocation() {
        String startingLoc = mDisplayedRoute.getStartingLocation();
        if ( startingLoc == null || startingLoc.isEmpty() ) {
            startingLocPromptTv.setVisibility(View.GONE);
            startingLocTv.setVisibility(View.GONE);
            return;
        }
        startingLocTv.setText(mDisplayedRoute.getStartingLocation());
    }

    private void displayRouteEnvironment() {
        RouteEnvironment env = mDisplayedRoute.getEnvironment();
        if( env == null || (env.getRouteType() == null && env.getTerrainType() == null &&
                            env.getSurfaceType() == null && env.getTrailType() == null &&
                            env.getDifficulty() == null) ) {
            findViewById(R.id.tv_route_env_prompt).setVisibility(View.GONE);
            routeTypePromptTv.setVisibility(View.GONE);
            routeTypeTv.setVisibility(View.GONE);
            terrainTypePromptTv.setVisibility(View.GONE);
            terrainTypeTv.setVisibility(View.GONE);
            surfaceTypePromptTv.setVisibility(View.GONE);
            surfaceTypeTv.setVisibility(View.GONE);
            landTypePromptTv.setVisibility(View.GONE);
            landTypeTv.setVisibility(View.GONE);
            difficultyPromptTv.setVisibility(View.GONE);
            difficultyTv.setVisibility(View.GONE);
        }
        else {
            displayRouteType();
            displayTerrainType();
            displaySurfaceType();
            displayLandType();
            displayDifficulty();
        }
    }

    private void displayNotes() {
        String notes = mDisplayedRoute.getNotes();
        if ( notes == null || notes.isEmpty() ) {
            notesPromptTv.setVisibility(View.GONE);
            notesTv.setVisibility(View.GONE);
            return;
        }
        notesTv.setText(mDisplayedRoute.getNotes());
    }

    // TODO: 3/9/20 DRY and SRP this mofo
    private void displayLatestWalkStats() {
        String currName = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE)
                .getString(IUser.USER_NAME_KEY, "");
        if (Utils.checkNotNull(mStats)) {
            if (currName.equals(mDisplayedRoute.getCreatorName())) {
                displayStats(mStats);
                checkMarkTv.setVisibility(View.VISIBLE);
            } else if (Utils.fileExists(mDisplayedRoute.getRouteUid(), this)) {
                Route route = RoutesManager.readSingle(mDisplayedRoute.getRouteUid(), this);
                if (route != null) {
                    mStats = route.getStats();
                    displayStats(mStats);
                    detailsPromptTv.setText(R.string.your_recent_walk);
                    checkMarkTv.setVisibility(View.VISIBLE);
                }
            } else {
                detailsPromptTv.setText(R.string.teamate_recent_walk);
                neverWalkedPromptTv.setVisibility(View.VISIBLE);
                displayStats(mStats);
            }
        } else {
            if (Utils.fileExists(mDisplayedRoute.getRouteUid(), this)) {
                Route route = RoutesManager.readSingle(mDisplayedRoute.getRouteUid(), this);
                if (route != null) {
                    mStats = route.getStats();
                    detailsPromptTv.setText(R.string.your_recent_walk);
                    displayStats(mStats);
                    checkMarkTv.setVisibility(View.VISIBLE);
                }
            } else {
                recentStepsPromptTv.setVisibility(View.GONE);
                recentStepsTv.setVisibility(View.GONE);
                recentDistancePromptTv.setVisibility(View.GONE);
                recentDistanceTv.setVisibility(View.GONE);
                recentTimeElapsedPromptTv.setVisibility(View.GONE);
                recentTimeElapsedTv.setVisibility(View.GONE);
                detailsPromptTv.setVisibility(View.GONE);
                neverWalkedPromptTv.setVisibility(View.VISIBLE);
            }
        }
    }

    private void displayStats(WalkStats stats) {
        recentStepsTv.setText(String.valueOf(stats.getSteps()));
        recentDistanceTv.setText(stats.formattedDistance());
        recentTimeElapsedTv.setText(stats.formattedTime());
    }

    private void displayRouteType() {
        RouteEnvironment.RouteType rteType = mDisplayedRoute.getEnvironment().getRouteType();
        if(rteType == null) {
            routeTypePromptTv.setVisibility(View.GONE);
            routeTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (rteType) {
            case LOOP:
                routeTypeTv.setText(String.format("%s", "Loop"));
                break;
            case OUT_AND_BACK:
                routeTypeTv.setText(String.format("%s", "Out-and-Back"));
                break;
        }
    }

    private void displayTerrainType() {
        RouteEnvironment.TerrainType terrType = mDisplayedRoute.getEnvironment().getTerrainType();
        if(terrType == null) {
            terrainTypePromptTv.setVisibility(View.GONE);
            terrainTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (terrType) {
            case FLAT:
                terrainTypeTv.setText(String.format("%s", "Flat"));
                break;
            case HILLY:
                terrainTypeTv.setText(String.format("%s", "Hilly"));
                break;
        }
    }

    private void displaySurfaceType() {
        RouteEnvironment.SurfaceType srfceType = mDisplayedRoute.getEnvironment().getSurfaceType();
        if(srfceType == null) {
            surfaceTypePromptTv.setVisibility(View.GONE);
            surfaceTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (srfceType) {
            case EVEN:
                surfaceTypeTv.setText(String.format("%s", "Even"));
                break;
            case UNEVEN:
                surfaceTypeTv.setText(String.format("%s", "Uneven"));
                break;
        }
    }

    private void displayLandType() {
        RouteEnvironment.TrailType lndType = mDisplayedRoute.getEnvironment().getTrailType();
        if(lndType == null) {
            landTypePromptTv.setVisibility(View.GONE);
            landTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (lndType) {
            case TRAIL:
                landTypeTv.setText(String.format("%s", "Trail"));
                break;
            case STREETS:
                landTypeTv.setText(String.format("%s", "Streets"));
                break;
        }
    }

    private void displayDifficulty() {
        RouteEnvironment.Difficulty difficulty = mDisplayedRoute.getEnvironment().getDifficulty();
        if(difficulty == null) {
            difficultyPromptTv.setVisibility(View.GONE);
            difficultyTv.setVisibility(View.GONE);
            return;
        }
        switch (difficulty) {
            case EASY:
                difficultyTv.setText(String.format("%s", "Easy"));
                break;
            case MODERATE:
                difficultyTv.setText(String.format("%s", "Moderate"));
                break;
            case HARD:
                difficultyTv.setText(String.format("%s", "Hard"));
                break;
        }
    }

    private void setStartWalkBtnOnClickListener() {
        startWalkBtn.setOnClickListener(view -> {
            returnToRoutesActivityForWalk();
        });
    }

    private void returnToRoutesActivityForWalk() {
        Log.i(TAG, "returnToRoutesActivityForWalk: returning to launching activity for automatic recording");
        Intent intent = new Intent()
                .putExtra(ROUTE_KEY, mDisplayedRoute)
                .putExtra(ROUTE_IDX_KEY, routeIdx);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
