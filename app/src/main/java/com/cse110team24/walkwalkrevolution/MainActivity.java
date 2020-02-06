package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String HEIGHT_FT_KEY = "Height Feet";
    public static final String HEIGHT_IN_KEY = "Height Remainder Inches";
    public static final int MAX_FEET = 8;
    public static final float MAX_INCHES = 11.99f;
    public static final float INVALID_VAL = -1.0f;

    private String fitnessServiceKey = "GOOGLE_FIT";

    EditText feetEditText;
    EditText inchesEditText;
    Button finishBtn;
    int feet;
    float inches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        checkHeight(preferences);

        getFields();

        finishBtn.setEnabled(false);
        TextWatcher textWatcher = getTextWatcher();
        feetEditText.addTextChangedListener(textWatcher);
        inchesEditText.addTextChangedListener(textWatcher);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(HEIGHT_FT_KEY, feet);
                editor.putFloat(HEIGHT_IN_KEY, inches);
                editor.apply();
                launchHomeActivity();
            }
        });

    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    private void getFields() {
        feetEditText = findViewById(R.id.height_feet_et);
        inchesEditText = findViewById(R.id.height_remainder_inches_et);
        finishBtn = findViewById(R.id.finish_btn);
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
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HEIGHT_FT_KEY, feet);
        intent.putExtra(HEIGHT_IN_KEY, inches);
        intent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        finish();
        startActivity(intent);
    }

    private void checkHeight(SharedPreferences preferences) {
        feet = preferences.getInt(HEIGHT_FT_KEY, (int) INVALID_VAL);
        inches = preferences.getFloat(HEIGHT_IN_KEY, INVALID_VAL);
        if (feet > 0 && inches > 0) {
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