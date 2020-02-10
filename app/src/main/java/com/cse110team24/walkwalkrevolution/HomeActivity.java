package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

import java.text.ParseException;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final String DECIMAL_FMT = "#0.00";
    private static final long UPDATE_PERIOD = 1000;

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

    private NumberFormat numberFormat;
    private SimpleDateFormat dateFormat;

    private boolean mocking;

    private int heightFeet;
    private float heightRemainderInches;

    private TextView dailyStepsTv;
    private TextView dailyDistanceTv;
    private TextView recentStepsTv;
    private TextView recentDistanceTv;
    private TextView recentTimeElapsedTv;
    private TextView noRecentWalkPromptTv;
    private Button startWalkBtn;
    private Button stopWalkBtn;
    private Button launchMockActivityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUIFields();
        saveHeight();
        numberFormat = new DecimalFormat(DECIMAL_FMT);
        setFitnessService();

        setStartWalkBtnOnClickListener();
        setStopWalkBtnOnClickListner();
        setLaunchMockActivityBtnOnClickListener();

        handler.post(runUpdateSteps);
        Log.i(TAG, "onCreate: handler posted");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == fitnessService.getRequestCode()) {
            if (resultCode == Activity.RESULT_OK) {
                fitnessService.updateDailyStepCount();
            } else {
                Log.e(TAG, "onActivityResult: error with fitness result code: " + resultCode);
            }
        } else if (requestCode == MockActivity.REQUEST_CODE && data != null) {
            setMockedExtras(data);
        }
    }

    public void setDailyStats(long stepCount) {
        Log.i(TAG, "setDailyStats: setting daily stats with steps: " + stepCount);
        dailyStepsTv.setText(String.valueOf(stepCount));
        double distance = fitnessService.getDistanceFromHeight(stepCount, heightFeet, heightRemainderInches);
        dailyDistanceTv.setText(numberFormat.format(distance));
    }

    public void setLatestWalkStats(long stepCount, long timeElapsed) {
        noRecentWalkPromptTv.setVisibility(View.INVISIBLE);
        recentStepsTv.setText(String.valueOf(stepCount));
        double distanceTraveled = fitnessService.getDistanceFromHeight(stepCount, heightFeet, heightRemainderInches);
        recentDistanceTv.setText(String.format("%s%s", numberFormat.format(distanceTraveled), " mile(s)"));
        double timeElapsedInSeconds = timeElapsed / 1000;
        double timeElapsedInMinutes = timeElapsedInSeconds / 60;
        recentTimeElapsedTv.setText(String.format("%s%s", numberFormat.format(timeElapsedInMinutes), " min."));
    }

    private void getUIFields() {
        dailyStepsTv = findViewById(R.id.tv_daily_steps);
        dailyDistanceTv = findViewById(R.id.tv_daily_distance);
        recentStepsTv = findViewById(R.id.tv_recent_steps);
        recentDistanceTv = findViewById(R.id.tv_recent_distance);
        recentTimeElapsedTv = findViewById(R.id.tv_recent_time_elapsed);
        noRecentWalkPromptTv = findViewById(R.id.tv_no_recent_walk_prompt);
        startWalkBtn = findViewById(R.id.btn_start_walk);
        stopWalkBtn = findViewById(R.id.btn_stop_walk);
        launchMockActivityBtn = findViewById(R.id.btn_mock_values);
    }

    private void setStartWalkBtnOnClickListener() {
        startWalkBtn.setOnClickListener(view -> {
            startWalkBtn.setEnabled(false);
            startWalkBtn.setVisibility(View.INVISIBLE);
            stopWalkBtn.setVisibility(View.VISIBLE);
            stopWalkBtn.setEnabled(true);
            if (!mocking) {
                fitnessService.setStartRecordingTime(System.currentTimeMillis());
            }
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
            if (!mocking) {
                fitnessService.setEndRecordingTime(System.currentTimeMillis());
            }
            // TODO: 2020-02-10 set mocking back to false?
            mocking = false;
            fitnessService.stopRecording();
        });
    }

    private void setLaunchMockActivityBtnOnClickListener() {
        launchMockActivityBtn.setOnClickListener(view -> {
            launchMockActivity();
        });
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

    private void launchMockActivity() {
        mocking = true;
        String dailyStepsStr = dailyStepsTv.getText().toString();
        long currentDailySteps = dailyStepsStr.isEmpty() ? 0 : Long.valueOf(dailyStepsStr);

        Intent intent = new Intent(this, MockActivity.class)
                .putExtra(MockActivity.START_WALK_BTN_VISIBILITY_KEY, startWalkBtn.getVisibility())
                .putExtra(MockActivity.EXISTING_STEPS_KEY, currentDailySteps);

        startActivityForResult(intent, MockActivity.REQUEST_CODE);
    }

    private void setMockedExtras(Intent data) {
        dateFormat = new SimpleDateFormat(MockActivity.TIME_FMT);
        String time = data.getStringExtra(MockActivity.INPUT_TIME_KEY);
        Date dateTime = null;
        try {
            dateTime = dateFormat.parse(time);
        } catch (ParseException e) {
            Log.e(TAG, "setMockedExtras: there was a problem parsing the time string " + time, e);
        }

        boolean settingStartTime = data.getBooleanExtra(MockActivity.SETTING_START_TIME_KEY, false);
        long timeMillis = dateTime.getTime();
        if (settingStartTime) {
            fitnessService.setStartRecordingTime(timeMillis);
        } else {
            fitnessService.setEndRecordingTime(timeMillis);
        }
        Log.i(TAG, "setMockedExtras: time + " + time + " correctly parsed with value " + dateTime + " millis: " + timeMillis);

        long stepsToAdd = data.getLongExtra(MockActivity.ADDED_STEPS_KEY, 0);
        Log.d(TAG, "setMockedExtras: steps to add " + stepsToAdd);
        fitnessService.setStepsToAdd(stepsToAdd);
        fitnessService.updateDailyStepCount();
    }

}
