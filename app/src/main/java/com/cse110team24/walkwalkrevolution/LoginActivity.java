package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final int MAX_FEET = 8;
    public static final float MAX_INCHES = 11.99f;
    public static final float INVALID_VAL = -1.0f;

    private String fitnessServiceKey = "GOOGLE_FIT";

    private Intent homeIntent;

    private EditText feetEditText;
    private EditText inchesEditText;
    private Button finishBtn;

    private int feet;
    private float inches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        final SharedPreferences preferences = getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE);
        homeIntent = new Intent(this, HomeActivity.class);

        getConfiguredFields();
        checkHeight(preferences);
        FitnessServiceFactory.put(fitnessServiceKey, homeActivity -> new GoogleFitAdapter(homeActivity));
        finishBtnOnClickListener();
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    private void finishBtnOnClickListener() {
        finishBtn.setOnClickListener(view -> {
            launchHomeActivity();
        });
    }

    private void getConfiguredFields() {
        feetEditText = findViewById(R.id.et_height_feet);
        inchesEditText = findViewById(R.id.et_height_remainder_inches);
        finishBtn = findViewById(R.id.btn_height_finish);
        finishBtn.setEnabled(false);
        TextWatcher textWatcher = getTextWatcher();
        feetEditText.addTextChangedListener(textWatcher);
        inchesEditText.addTextChangedListener(textWatcher);
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
                finishBtn.setEnabled(validateFeet() && validateInches());
            }
        };
    }

    private void launchHomeActivity() {
        Log.i(TAG, "launchHomeActivity: valid height params found; launching home.");
        homeIntent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, fitnessServiceKey)
                    .putExtra(HomeActivity.HEIGHT_FT_KEY, feet)
                    .putExtra(HomeActivity.HEIGHT_IN_KEY, inches);
        finish();
        startActivity(homeIntent);
    }

    private void checkHeight(SharedPreferences preferences) {
        Log.i(TAG, "checkHeight: checking preferences if height already exists");
        feet = preferences.getInt(HomeActivity.HEIGHT_FT_KEY, (int) INVALID_VAL);
        inches = preferences.getFloat(HomeActivity.HEIGHT_IN_KEY, INVALID_VAL);
        if (feet > 0 && inches > 0) {
            Log.i(TAG, "checkHeight: valid height in preferences already exists (feet: " + feet + ", inches: " + inches + ").");
            launchHomeActivity();
        }
    }

    private boolean validateInches() {
        try {
            inches = Float.parseFloat(inchesEditText.getText().toString());
        } catch (NumberFormatException e) {
            inches = INVALID_VAL;
        }
        return inches <= MAX_INCHES && inches > 0;
    }

    private boolean validateFeet() {
        try {
            feet = Integer.parseInt(feetEditText.getText().toString());
        } catch (NumberFormatException e) {
            feet = (int) INVALID_VAL;
        }
        return feet <= MAX_FEET && feet > 0;
    }

}