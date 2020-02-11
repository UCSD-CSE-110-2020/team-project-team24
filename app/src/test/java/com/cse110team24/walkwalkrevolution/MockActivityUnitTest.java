package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MockActivityUnitTest {
    private static final String ENTER_START_TIME_PROMPT_TXT = "Enter Desired Start Time:";
    private static final String ENTER_END_TIME_PROMPT_TXT = "Enter Desired End Time:";

    private TextView addedStepsNum;
    private TextView enterTimePrompt;
    private Button finishBtn;
    private Button addStepsBtn;
    private EditText editTime;

    @Before
    public void setup() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MockActivity.class);
        intent.putExtra(MockActivity.START_WALK_BTN_VISIBILITY_KEY, View.VISIBLE);
        ActivityScenario<MockActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            getUIFields(activity);
        });
    }

    @Test
    public void testMockScreenBeforeStartingWalk() {
        assertEquals(addedStepsNum.getText().toString(), "0");
        assertEquals(enterTimePrompt.getText().toString(), ENTER_START_TIME_PROMPT_TXT);
        assertFalse(finishBtn.isEnabled());
    }

    @Test
    public void testMockScreenAfterStartingWalk() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MockActivity.class);
        intent.putExtra(MockActivity.START_WALK_BTN_VISIBILITY_KEY, View.INVISIBLE);
        ActivityScenario<MockActivity> endScenario = ActivityScenario.launch(intent);
        endScenario.onActivity(activity -> {
            getUIFields(activity);
            assertEquals(enterTimePrompt.getText().toString(), ENTER_END_TIME_PROMPT_TXT);
        });
    }

    @Test
    public void testAddOneIncrement() {
        addStepsBtn.performClick();
        assertEquals(addedStepsNum.getText().toString(), "500");
    }

    @Test
    public void testAddMultipleIncrements() {
        for(int i = 0; i < 4; i++) {
            addStepsBtn.performClick();
        }
        assertEquals(addedStepsNum.getText().toString(), "2000");
    }

    @Test
    public void testInvalidFormatTime() {
        editTime.setText("12345678");
        assertEquals(editTime.getText().toString(), "12345678");
        assertFalse(finishBtn.isEnabled());
    }

    @Test
    public void testValidFormatInvalidTime() {
        editTime.setText("12:45:78");
        assertEquals(editTime.getText().toString(), "12:45:78");
        assertFalse(finishBtn.isEnabled());
    }

    @Test
    public void testValidTime() {
        editTime.setText("16:42:23");
        assertEquals(editTime.getText().toString(), "16:42:23");
        assertTrue(finishBtn.isEnabled());
    }

    private void getUIFields(MockActivity activity) {
        addedStepsNum = activity.findViewById(R.id.tv_added_steps);
        enterTimePrompt = activity.findViewById(R.id.tv_enter_time);
        finishBtn = activity.findViewById(R.id.btn_mock_finish);
        addStepsBtn = activity.findViewById(R.id.btn_increment_steps);
        editTime = activity.findViewById(R.id.et_edit_time);
    }
}
