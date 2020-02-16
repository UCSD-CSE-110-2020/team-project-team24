package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RouteDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RouteDetailsActivity";
    
    public static final String ROUTE_KEY = "route";
    public static final String ROUTE_IDX_KEY = "route_adapter_idx";
    public static final int REQUEST_CODE = 99;

    private Route displayedRoute;
    private int routeIdx;
    private WalkStats stats;

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
    private TextView recentStepsTv;
    private TextView recentDistanceTv;
    private TextView recentTimeElapsedTv;
    private TextView notesTv;
    private TextView neverWalkedPromptTv;
    private Button startWalkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        getRouteInfo();
        findUIElements();
        displayRouteInformation();
    }

    private void getRouteInfo() {
        displayedRoute = (Route) getIntent().getSerializableExtra(ROUTE_KEY);
        routeIdx = getIntent().getIntExtra(ROUTE_IDX_KEY, 0);
        stats = displayedRoute.getStats();
        getSupportActionBar().setTitle(displayedRoute.getTitle());
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
        setStartWalkBtnOnClickListener();
    }

    private void displayRouteInformation() {
        displayStartingLocation();
        displayRouteEnvironment();
        if (stats == null) {
            recentStepsPromptTv.setVisibility(View.GONE);
            recentStepsTv.setVisibility(View.GONE);
            recentDistancePromptTv.setVisibility(View.GONE);
            recentDistanceTv.setVisibility(View.GONE);
            recentTimeElapsedPromptTv.setVisibility(View.GONE);
            recentTimeElapsedTv.setVisibility(View.GONE);
            neverWalkedPromptTv.setVisibility(View.VISIBLE);
        } else {
            displayLatestWalkStats();
        }
        displayNotes();
    }

    private void displayStartingLocation() {
        if ( displayedRoute.getStartingLocation().equals("") ) {
            startingLocPromptTv.setVisibility(View.GONE);
            startingLocTv.setVisibility(View.GONE);
            return;
        }
        startingLocTv.setText(displayedRoute.getStartingLocation());
    }

    private void displayRouteEnvironment() {
        displayRouteType();
        displayTerrainType();
        displaySurfaceType();
        displayLandType();
        displayDifficulty();
    }

    private void displayNotes() {
        if ( displayedRoute.getNotes().equals("") ) {
            notesPromptTv.setVisibility(View.GONE);
            notesTv.setVisibility(View.GONE);
            return;
        }
        notesTv.setText(displayedRoute.getNotes());
    }

    private void displayLatestWalkStats() {
        Log.i(TAG, "displayLatestWalkStats: stats found for current route, displaying them now");
        recentStepsTv.setText(String.valueOf(stats.getSteps()));
        recentDistanceTv.setText(stats.formattedDistance());
        recentTimeElapsedTv.setText(stats.formattedTime());
    }

    private void displayRouteType() {
        RouteEnvironment.RouteType rteType = displayedRoute.getEnvironment().getRouteType();
        if(rteType == null) {
            routeTypePromptTv.setVisibility(View.GONE);
            routeTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (rteType) {
            case LOOP:
                routeTypeTv.setText(String.format("%s", "Loop"));
            case OUT_AND_BACK:
                routeTypeTv.setText(String.format("%s", "Out-and-Back"));
        }
    }

    private void displayTerrainType() {
        RouteEnvironment.TerrainType terrType = displayedRoute.getEnvironment().getTerrainType();
        if(terrType == null) {
            terrainTypePromptTv.setVisibility(View.GONE);
            terrainTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (terrType) {
            case FLAT:
                terrainTypeTv.setText(String.format("%s", "Flat"));
            case HILLY:
                terrainTypeTv.setText(String.format("%s", "Hilly"));
        }
    }

    private void displaySurfaceType() {
        RouteEnvironment.SurfaceType srfceType = displayedRoute.getEnvironment().getSurfaceType();
        if(srfceType == null) {
            surfaceTypePromptTv.setVisibility(View.GONE);
            surfaceTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (srfceType) {
            case EVEN:
                surfaceTypeTv.setText(String.format("%s", "Even"));
            case UNEVEN:
                surfaceTypeTv.setText(String.format("%s", "Uneven"));
        }
    }

    private void displayLandType() {
        RouteEnvironment.TrailType lndType = displayedRoute.getEnvironment().getTrailType();
        if(lndType == null) {
            landTypePromptTv.setVisibility(View.GONE);
            landTypeTv.setVisibility(View.GONE);
            return;
        }
        switch (lndType) {
            case TRAIL:
                landTypeTv.setText(String.format("%s", "Trail"));
            case STREETS:
                landTypeTv.setText(String.format("%s", "Streets"));
        }
    }

    private void displayDifficulty() {
        RouteEnvironment.Difficulty difficulty = displayedRoute.getEnvironment().getDifficulty();
        if(difficulty == null) {
            difficultyPromptTv.setVisibility(View.GONE);
            difficultyTv.setVisibility(View.GONE);
            return;
        }
        switch (difficulty) {
            case EASY:
                difficultyTv.setText(String.format("%s", "Easy"));
            case MODERATE:
                difficultyTv.setText(String.format("%s", "Moderate"));
            case HARD:
                difficultyTv.setText(String.format("%s", "Hard"));
        }
    }

    private void setStartWalkBtnOnClickListener() {
        startWalkBtn.setOnClickListener(view -> {
            returnToRoutesActivityForWalk();
        });
    }

    private void returnToRoutesActivityForWalk() {
        Log.i(TAG, "returnToRoutesActivityForWalk: returning to home for automatic recording");
        Intent intent = new Intent()
                .putExtra(ROUTE_KEY, displayedRoute)
                .putExtra(ROUTE_IDX_KEY, routeIdx);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
