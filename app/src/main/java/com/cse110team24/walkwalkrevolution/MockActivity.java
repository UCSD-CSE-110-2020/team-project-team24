package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;

// TODO: 2020-02-16 add logging to this class
public class MockActivity extends AppCompatActivity {
    private static final String TAG = "MockActivity";
    private static final int ADD_MOCK_CONST = 500;

    public static final String START_WALK_BTN_VISIBILITY_KEY = "start button";
    public static final String ADDED_STEPS_KEY = "added_steps";
    public static final String SETTING_START_TIME_KEY = "setting_start_time";
    public static final String INPUT_TIME_KEY = "input_time";
    public static final String TIME_FMT = "HH:mm:ss";
    public static final int REQUEST_CODE = 6;

    private long totalAddedSteps;
    private boolean settingStartTime;

    private Button stepsMockBtn;
    private Button finishBtn;
    private EditText inputtedTime;
    private TextView totalStepsView;
    private TextView enterTimePromptTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        getUIFields();
        checkWhichTimeToSet();
        setStepsMockOnClickListener();
        setFinishBtnOnClickListener();
    }

    private void getUIFields() {
        stepsMockBtn = findViewById(R.id.btn_increment_steps);
        finishBtn = findViewById(R.id.btn_mock_finish);
        finishBtn.setEnabled(false);
        inputtedTime = findViewById(R.id.et_edit_time);
        totalStepsView = findViewById(R.id.tv_added_steps);
        enterTimePromptTv = findViewById(R.id.tv_enter_time);
        TextWatcher textWatcher = getTextWatcher();
        inputtedTime.addTextChangedListener(textWatcher);
    }

    private void setStepsMockOnClickListener() {
        stepsMockBtn.setOnClickListener(view -> {
            totalAddedSteps += ADD_MOCK_CONST;
            totalStepsView.setText(String.valueOf(totalAddedSteps));
        });
    }

    private void setFinishBtnOnClickListener() {
        finishBtn.setOnClickListener(view -> {
            finishMockActivity();
        });
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                finishBtn.setEnabled(validateTime());
            }
        };
    }

    private void finishMockActivity() {
        Intent intent = new Intent()
                .putExtra(ADDED_STEPS_KEY, totalAddedSteps)
                .putExtra(SETTING_START_TIME_KEY, settingStartTime)
                .putExtra(INPUT_TIME_KEY, inputtedTime.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean validateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FMT);
        sdf.setLenient(false);
        try {
            sdf.parse(inputtedTime.getText().toString());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void checkWhichTimeToSet() {
        settingStartTime = getIntent().getIntExtra(START_WALK_BTN_VISIBILITY_KEY, -1) == View.VISIBLE;
        if (!settingStartTime) {
            enterTimePromptTv.setText(R.string.enter_end_time);
        }
    }
}
