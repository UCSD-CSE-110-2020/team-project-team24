package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MockActivity extends AppCompatActivity {
    public static final String START_WALK_BTN_VISIBILITY = "start button";
    public static final int REQUEST_CODE = 6;
    private static final long ADD_MOCK_CONST = 500;

    private long totalAddedSteps;

    private Button stepsMockBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        getUIFields();

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
}
