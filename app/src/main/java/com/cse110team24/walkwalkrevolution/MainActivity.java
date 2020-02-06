package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public static final String HEIGHT_FT_KEY = "Height Feet";
    public static final String HEIGHT_IN_KEY = "Height Remainder Inches";

    EditText feetEditText;
    EditText inchesEditText;
    Button finishBtn;
    int feet;
    float inches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feetEditText = findViewById(R.id.height_feet_et);
        inchesEditText = findViewById(R.id.height_remainder_inches_et);
        finishBtn = findViewById(R.id.finish_btn);

        final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        feet = preferences.getInt(HEIGHT_FT_KEY, -1);
        inches = preferences.getFloat(HEIGHT_IN_KEY, -1);
        checkHeight();

        finishBtn.setEnabled(false);
        feetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                finishBtn.setEnabled(false);
                if (validateFeet() && validateInches()) {
                    finishBtn.setEnabled(true);
                }
            }
        });
        inchesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishBtn.setEnabled(false);
                if (validateFeet() && validateInches()) {
                    finishBtn.setEnabled(true);
                }
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(HEIGHT_FT_KEY, feet);
                editor.putFloat(HEIGHT_IN_KEY, inches);
                editor.apply();
                launchHome();

            }
        });

    }

    private void launchHome() {
        Intent intent = new Intent(this, EmptyActivity.class);
        intent.putExtra(HEIGHT_FT_KEY, feet);
        intent.putExtra(HEIGHT_IN_KEY, inches);
        startActivity(intent);
    }

    private void checkHeight() {
        if (feet > 0 && inches > 0) {
            launchHome();
        }
    }

    private boolean validateInches() {
        try {
            inches = Float.parseFloat(inchesEditText.getText().toString());
        } catch (NumberFormatException e) {
            inches = -1;
        }
        return inches <= 12.0 && inches > 0;
    }

    private boolean validateFeet() {
        try {
            feet = Integer.parseInt(feetEditText.getText().toString());
        } catch (NumberFormatException e) {
            feet = -1;
        }
        return feet > 0;
    }
}