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
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RouteDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RouteDetailsActivity";
    
    public static final String ROUTE_KEY = "route";
    public static final int REQUEST_CODE = 99;

    private Route displayedRoute;
    private WalkStats stats;

    private TextView recentStepsTv;
    private TextView recentDistanceTv;
    private TextView recentTimeElapsedTv;
    private TextView neverWalkedPromptTv;
    private Button startWalkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        displayedRoute = (Route) getIntent().getSerializableExtra(ROUTE_KEY);
        stats = displayedRoute.getStats();
        getSupportActionBar().setTitle(displayedRoute.getTitle());

        findUIElements();
        checkRouteCompletion();
    }

    private void findUIElements() {
        recentStepsTv = findViewById(R.id.tv_details_recent_steps);
        recentDistanceTv = findViewById(R.id.tv_details_recent_distance);
        recentTimeElapsedTv = findViewById(R.id.tv_details_recent_time_elapsed);
        neverWalkedPromptTv = findViewById(R.id.tv_details_never_walked);
        startWalkBtn = findViewById(R.id.btn_details_start_walk);
        setStartWalkBtnOnClickListener();
    }

    private void checkRouteCompletion() {
        if (stats == null) {
            neverWalkedPromptTv.setVisibility(View.VISIBLE);
        } else {
            displayLatestWalkStats();
        }
    }

    private void displayLatestWalkStats() {
        Log.i(TAG, "displayLatestWalkStats: stats found for current route, displaying them now");
        recentStepsTv.setText(String.valueOf(stats.getSteps()));
        String distance = formattedDistanceTime(stats.getDistance(), "mile(s)");
        recentDistanceTv.setText(distance);
        String timeElapsed = formattedDistanceTime(stats.timeElapsedInMinutes(), "min.");
        recentTimeElapsedTv.setText(timeElapsed);
    }

    private String formattedDistanceTime(double val, String suffix) {
        NumberFormat numberFormat = new DecimalFormat("#0.00");
        String formattedVal = numberFormat.format(val);
        return String.format("%s %s", formattedVal, suffix);
    }

    private void setStartWalkBtnOnClickListener() {
        startWalkBtn.setOnClickListener(view -> {
            returnToRoutesActivityForWalk();
        });
    }

    private void returnToRoutesActivityForWalk() {
        Intent intent = new Intent().putExtra(ROUTE_KEY, displayedRoute);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}