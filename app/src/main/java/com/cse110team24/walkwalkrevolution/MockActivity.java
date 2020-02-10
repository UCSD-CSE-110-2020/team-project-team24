package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.DateInterval;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;

public class MockActivity extends AppCompatActivity {
    public static final String START_WALK_BTN_VISIBILITY = "start button";
    public static final String ADDED_STEPS_KEY = "added_steps";
    public static final String INPUT_START_TIME_KEY = "input_start_time";
    public static final String INPUT_END_TIME_KEY = "input_end_time";
    public static final int REQUEST_CODE = 6;

    private static final long ADD_MOCK_CONST = 500;
    private static final String TIME_FMT = "HH:mm:ss";

    private long totalAddedSteps;
    private String timeSelected;
    private boolean settingStartTime;

    private Button stepsMockBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        getUIFields();
        checkWhichTimeToSet();
        setStepsMockOnClickListener();
    }

    private void getUIFields() {
        stepsMockBtn = findViewById(R.id.btn_increment_steps);
        //Enter Time
    }
    private void setStepsMockOnClickListener() {
        stepsMockBtn.setOnClickListener(view -> {
            totalAddedSteps += ADD_MOCK_CONST;
        });
    }

    private void finishMockAcitivity() {
        Intent intent = new Intent()
                .putExtra(ADDED_STEPS_KEY, totalAddedSteps);
        if (settingStartTime) {
            intent.putExtra(INPUT_START_TIME_KEY, timeSelected);
        } else {
            intent.putExtra(INPUT_END_TIME_KEY, timeSelected);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean validateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FMT);
        try {
            sdf.parse(timeSelected);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean checkWhichTimeToSet() {
        if (getIntent().getIntExtra(START_WALK_BTN_VISIBILITY, -1) == View.VISIBLE) {
            settingStartTime = true;
        } else {
            settingStartTime = false;
        }
    }
}
