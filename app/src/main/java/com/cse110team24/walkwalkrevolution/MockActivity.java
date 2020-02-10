package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MockActivity extends AppCompatActivity {
    public static final String START_WALK_BTN_VISIBILITY = "start button";
    public static final int REQUEST_CODE = 6;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
    }
}
