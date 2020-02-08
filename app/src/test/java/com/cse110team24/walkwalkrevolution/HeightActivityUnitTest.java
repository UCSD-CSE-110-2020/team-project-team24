package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class HeightActivityUnitTest {

    private HeightActivity testActivity;
    private Button finishBtn;
    private EditText feetEt;
    private EditText inchesEt;

    @Before
    public void setup() {
        ActivityScenario<HeightActivity> scenario = ActivityScenario.launch(HeightActivity.class);
        scenario.onActivity(activity -> {
            testActivity = activity;
            finishBtn = testActivity.findViewById(R.id.finish_btn);
            feetEt = testActivity.findViewById(R.id.height_feet_et);
            inchesEt = testActivity.findViewById(R.id.height_remainder_inches_et);
        });
    }

    @Test
    public void testFinishBtnDisabledBtnOnStart() {
        assertTrue(!finishBtn.isEnabled());
    }

    @Test
    public void testFinishBtnDisabledBadText() {
        feetEt.setText("5");
        assertTrue(!finishBtn.isEnabled());
        feetEt.setText("");
        inchesEt.setText("3");
        assertTrue(!finishBtn.isEnabled());
    }

    @Test
    public void testFinishBtnEnabled() {
        feetEt.setText("5");
        inchesEt.setText("3");
        assertTrue(finishBtn.isEnabled());
    }

    @Test
    public void testHeightSaved() {
        feetEt.setText("5");
        inchesEt.setText("3");
        finishBtn.performClick();
        SharedPreferences preferences = testActivity.getPreferences(Context.MODE_PRIVATE);
        assertEquals(5, preferences.getInt(HomeActivity.HEIGHT_FT_KEY, -1));
        assertEquals(3, preferences.getFloat(HomeActivity.HEIGHT_IN_KEY, -1), 0.1);
    }
}