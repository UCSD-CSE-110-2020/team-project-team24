package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final String HEIGHT_FT_KEY = "Height Feet";
    public static final String HEIGHT_IN_KEY = "Height Remainder Inches";
    private static final long UPDATE_PERIOD = 30_000;

    private FitnessService fitnessService;
    private Handler handler = new Handler();
    private long stepCount;
    private Runnable runUpdateSteps = new Runnable() {
        @Override
        public void run() {
            fitnessService.updateDailyStepCount();
            handler.postDelayed(this, UPDATE_PERIOD);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String fitnessServiceKey = getServiceKey();

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
        handler.post(runUpdateSteps);

        Button launchProfileActivity = (Button) findViewById(R.id.startWalkButton);

        launchProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
    }
    public void launchActivity() {
        Intent intent = new Intent(this, RecordWalkActivity.class);
        startActivity(intent);
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

    private String getServiceKey() {
        return getIntent().getStringExtra(FITNESS_SERVICE_KEY);
    }

    // TODO use this method to update steps 
    public void setStepCount(long stepCount) {
        this.stepCount = stepCount;
    }

}
