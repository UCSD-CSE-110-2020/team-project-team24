package com.cse110team24.walkwalkrevolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final String DECIMAL_FMT = "#0.00";
    private static final long UPDATE_PERIOD = 30_000;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final String HEIGHT_FT_KEY = "Height Feet";
    public static final String HEIGHT_IN_KEY = "Height Remainder Inches";
    public static final String HEIGHT_PREF = "height_preferences";

    private FitnessService fitnessService;

    private Handler handler = new Handler();
    private Runnable runUpdateSteps = new Runnable() {
        @Override
        public void run() {
            fitnessService.updateDailyStepCount();
            handler.postDelayed(this, UPDATE_PERIOD);
        }
    };

    NumberFormat formatter;

    private int heightFeet;
    private float heightRemainderInches;

    private TextView dailyStepsTv;
    private TextView dailyDistanceTv;
    private TextView latestWalkStepsTv;
    private TextView latestWalkDistanceTv;
    private TextView latestWalkTimeElapsedTv;
    private TextView noWalkTv;
    private Button startWalkBtn;
    private Button stopWalkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUIFields();
        saveHeight();
        setFitnessService();
        formatter = new DecimalFormat(DECIMAL_FMT);

        setStartWalkBtnOnClickListener();
        setStopWalkBtnOnClickListner();
        setBottomNavigationOnClickListener();

        handler.post(runUpdateSteps);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateDailyStepCount();
            }
        } else {
            Log.e(TAG, "onActivityResult: Error with google fit result code: " + resultCode);
        }
    }

    public void setDailyStats(long stepCount) {
        Log.i(TAG, "setDailyStats: setting daily stats with steps: " + stepCount);
        dailyStepsTv.setText(String.valueOf(stepCount));
        double distance = fitnessService.getDistanceFromHeight(stepCount, heightFeet, heightRemainderInches);
        dailyDistanceTv.setText(formatter.format(distance));
    }

    public void setLatestWalkStats(long stepCount, long timeElapsed) {
        noWalkTv.setVisibility(View.INVISIBLE);
        latestWalkStepsTv.setText(String.valueOf(stepCount));
        double distanceTraveled = fitnessService.getDistanceFromHeight(stepCount, heightFeet, heightRemainderInches);
        latestWalkDistanceTv.setText(String.format("%s%s", formatter.format(distanceTraveled), " mile(s)"));
        double timeElapsedInSeconds = timeElapsed / 1000;
        double timeElapsedInMinutes = timeElapsedInSeconds / 60;
        latestWalkTimeElapsedTv.setText(String.format("%s%s", formatter.format(timeElapsedInMinutes), " min."));
    }

    private void getUIFields() {
        dailyStepsTv = findViewById(R.id.dailyStepsText);
        dailyDistanceTv = findViewById(R.id.dailyDistanceText);
        latestWalkStepsTv = findViewById(R.id.totalStepsCounter);
        latestWalkDistanceTv = findViewById(R.id.totalDistanceCounter);
        latestWalkTimeElapsedTv = findViewById(R.id.timeElapsedCounter);
        noWalkTv = findViewById(R.id.noWalkToday);
        startWalkBtn = findViewById(R.id.startWalkButton);
        stopWalkBtn = findViewById(R.id.stopWalkButton);
    }

    private void setStartWalkBtnOnClickListener() {
        startWalkBtn.setOnClickListener(view -> {
            startWalkBtn.setEnabled(false);
            startWalkBtn.setVisibility(View.INVISIBLE);
            stopWalkBtn.setVisibility(View.VISIBLE);
            stopWalkBtn.setEnabled(true);
            fitnessService.startRecording();

        });
    }

    private void setStopWalkBtnOnClickListner() {
        stopWalkBtn.setVisibility(View.INVISIBLE);
        stopWalkBtn.setEnabled(false);
        stopWalkBtn.setOnClickListener(view -> {
            startWalkBtn.setEnabled(true);
            startWalkBtn.setVisibility(View.VISIBLE);
            stopWalkBtn.setVisibility(View.INVISIBLE);
            stopWalkBtn.setEnabled(false);
            fitnessService.stopRecording();
        });
    }

    private void setBottomNavigationOnClickListener() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.action_home:
                        break;

                    case R.id.action_routes_list:
                        launchGoToRoutesActivity();
                        break;
                }
                return true;
            }
        });
    }

    public void launchGoToRoutesActivity() {
        Intent intent = new Intent(this, Routes.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void setFitnessService() {
        String fitnessServiceKey = getServiceKey();
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
    }

    private String getServiceKey() {
        return getIntent().getStringExtra(FITNESS_SERVICE_KEY);
    }

    private void saveHeight() {
        SharedPreferences preferences = getSharedPreferences(HEIGHT_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        heightFeet = getIntent().getIntExtra(HEIGHT_FT_KEY, -1);
        heightRemainderInches =  getIntent().getFloatExtra(HEIGHT_IN_KEY, -1);
        editor.putInt(HomeActivity.HEIGHT_FT_KEY, heightFeet);
        editor.putFloat(HomeActivity.HEIGHT_IN_KEY, heightRemainderInches);
        editor.apply();
        Log.i(TAG, "saveHeight: saved height (feet: " + heightFeet + ", inches: " + heightRemainderInches + ").");
    }

}
