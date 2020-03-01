package com.cse110team24.walkwalkrevolution;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LoginActivityUnitTest extends TestInjection {

    private LoginActivity testActivity;
    private Button finishBtn;
    private EditText feetEt;
    private EditText inchesEt;

    @Before
    public void setup() {
        super.setup();

        FirebaseApplicationWWR.setAuthServiceFactory(asf);
        FirebaseApplicationWWR.setDatabaseServiceFactory(dsf);

        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            testActivity = activity;
            finishBtn = testActivity.findViewById(R.id.btn_height_finish);
            feetEt = testActivity.findViewById(R.id.et_height_feet);
            inchesEt = testActivity.findViewById(R.id.et_height_remainder_inches);
        });
    }

//    @Test
//    public void testFinishBtnDisabledBtnOnStart() {
//        assertTrue(!finishBtn.isEnabled());
//    }

//    @Test
//    public void testFinishBtnDisabledBadText() {
//        feetEt.setText("5");
//        assertTrue(!finishBtn.isEnabled());
//        feetEt.setText("");
//        inchesEt.setText("3");
//        assertTrue(!finishBtn.isEnabled());
//    }

    @Test
    public void testFinishBtnEnabled() {
        feetEt.setText("5");
        inchesEt.setText("3");
        assertTrue(finishBtn.isEnabled());
    }
}