package com.cse110team24.walkwalkrevolution;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.cse110team24.walkwalkrevolution.models.WalkStats;

import java.text.ParseException;
import java.util.Calendar;
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

    private boolean mocking;
    private boolean endTimeSet;

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
        stopWalkBtn.setVisibility(View.INVISIBLE);
        stopWalkBtn.setEnabled(false);
        saveHeight();
        numberFormat = new DecimalFormat(DECIMAL_FMT);
        setFitnessService();

        setButtonOnClickListeners();

        handler.post(runUpdateSteps);
        Log.i(TAG, "onCreate: handler posted");
    }

    private void setButtonOnClickListeners() {
        setStartWalkBtnOnClickListener();
        setStopWalkBtnOnClickListner();
        setLaunchMockActivityBtnOnClickListener();
        setBottomNavigationOnClickListener();
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
        double distance = calculateDistance(stepCount);
        dailyDistanceTv.setText(numberFormat.format(distance));
    }

    private double calculateDistance(long stepCount) {
        return fitnessService.getDistanceFromHeight(stepCount, heightFeet, heightRemainderInches);
    }

    public WalkStats setLatestWalkStats(long stepCount, long timeElapsed) {
        noRecentWalkPromptTv.setVisibility(View.INVISIBLE);
        double distanceTraveled = calculateDistance(stepCount);
        WalkStats stats = new WalkStats(stepCount, timeElapsed, distanceTraveled, Calendar.getInstance());
        recentStepsTv.setText(String.valueOf(stepCount));
        recentDistanceTv.setText(String.format("%s%s", numberFormat.format(distanceTraveled), " mile(s)"));
        double timeElapsedInMinutes = stats.timeElapsedInMinutes();
        recentTimeElapsedTv.setText(String.format("%s%s", numberFormat.format(timeElapsedInMinutes), " min."));
        return stats;
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
            toggleStartAndStopBtns();
            if (!mocking) {
                fitnessService.setStartRecordingTime(System.currentTimeMillis());
            } else {
                showSetEndTimeToast();
            }
            fitnessService.startRecording();
        });
    }

    private void setStopWalkBtnOnClickListner() {
        stopWalkBtn.setOnClickListener(view -> {
            if (mocking && !endTimeSet) {
                showSetEndTimeToast();
                return;
            }
            toggleStartAndStopBtns();
            if (!mocking) {
                fitnessService.setEndRecordingTime(System.currentTimeMillis());
            }
            endTimeSet = false;
            mocking = false;
            fitnessService.stopRecording();
        });
    }

    private void setBottomNavigationOnClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.action_home:
                    break;

                case R.id.action_routes_list:
                    launchGoToRoutesActivity();
                    break;
            }
            return true;
        });
    }

    public void launchGoToRoutesActivity() {
        Intent intent = new Intent(this, RoutesActivity.class);
        startActivityForResult(intent, RoutesActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void setLaunchMockActivityBtnOnClickListener() {
        launchMockActivityBtn.setOnClickListener(view -> {
            launchMockActivity();
        });
    }

    private void toggleStartAndStopBtns() {
        toggleBtn(startWalkBtn);
        toggleBtn(stopWalkBtn);
    }

    private void toggleBtn(Button btn) {
        btn.setEnabled(!btn.isEnabled());
        int visibility = (btn.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
        btn.setVisibility(visibility);
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
        String dailyStepsStr = dailyStepsTv.getText().toString();
        long currentDailySteps = dailyStepsStr.isEmpty() ? 0 : Long.valueOf(dailyStepsStr);

        Intent intent = new Intent(this, MockActivity.class)
                .putExtra(MockActivity.START_WALK_BTN_VISIBILITY_KEY, startWalkBtn.getVisibility());

        startActivityForResult(intent, MockActivity.REQUEST_CODE);
    }

    private void setMockedExtras(Intent data) {
        setMockedTime(data);
        setMockedSteps(data);
        fitnessService.updateDailyStepCount();
    }

    private void setMockedSteps(Intent data) {
        long stepsToAdd = data.getLongExtra(MockActivity.ADDED_STEPS_KEY, 0);
        Log.d(TAG, "setMockedExtras: steps to add " + stepsToAdd);
        fitnessService.setStepsToAdd(stepsToAdd);
    }

    private void setMockedTime(Intent data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(MockActivity.TIME_FMT);
        String time = data.getStringExtra(MockActivity.INPUT_TIME_KEY);
        Date dateTime = parseMockedTime(dateFormat, time);

        boolean settingStartTime = data.getBooleanExtra(MockActivity.SETTING_START_TIME_KEY, false);
        long timeMillis = dateTime.getTime();
        if (settingStartTime) {
            mocking = true;
            fitnessService.setStartRecordingTime(timeMillis);
        } else {
            fitnessService.setEndRecordingTime(timeMillis);
            endTimeSet = true;
        }
    }

    private Date parseMockedTime(SimpleDateFormat dateFormat, String time) {
        Date dateTime = null;
        try {
            dateTime = dateFormat.parse(time);
        } catch (ParseException e) {
            Log.e(TAG, "setMockedExtras: there was a problem parsing the time string " + time, e);
        }
        Log.i(TAG, "setMockedExtras: time string " + time + " correctly parsed with value " + dateTime);
        return dateTime;
    }

    private void showSetEndTimeToast() {
        Toast.makeText(this, "Remember to set an end time for your walk!", Toast.LENGTH_SHORT).show();
    }
}
