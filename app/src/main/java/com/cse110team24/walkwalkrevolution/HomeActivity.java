package com.cse110team24.walkwalkrevolution;

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
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final String DISTANCE_FMT = "#0.00";
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

    private int heightFeet;
    private float heightRemainderInches;

    private TextView dailyStepsTv;
    private TextView dailyDistanceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUIFields();
        saveHeight();
        setFitnessService();

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
        NumberFormat formatter = new DecimalFormat(DISTANCE_FMT);
        dailyDistanceTv.setText(formatter.format(distance));
    }

    private void getUIFields() {
        dailyStepsTv = findViewById(R.id.dailyStepsText);
        dailyDistanceTv = findViewById(R.id.dailyDistanceText);
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
