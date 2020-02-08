package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final String HEIGHT_FT_KEY = "Height Feet";
    public static final String HEIGHT_IN_KEY = "Height Remainder Inches";
    private static final long UPDATE_PERIOD = 30_000;
    Button stopButton;
    Button  startButton;

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

        startButton = (Button) findViewById(R.id.startWalkButton);
        stopButton = (Button) findViewById(R.id.stopWalkButton);

        stopButton.setVisibility(INVISIBLE);


        String fitnessServiceKey = getServiceKey();

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
        handler.post(runUpdateSteps);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(INVISIBLE);
                stopButton.setVisibility(VISIBLE);

            }
        });


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setVisibility(INVISIBLE);
                startButton.setVisibility(VISIBLE);

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
