package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InviteTeamToWalkActivity extends AppCompatActivity {
    private static final String TAG = "WWR_InviteTeamToWalkActivity";

    private Route mProposedRoute;
    private String mProposedBy;
    private SimpleDateFormat mDateTimeFormat;
    private Date mParsedDate;

    private EditText mDateEditText;
    private EditText mTimeEditText;
    private Button mAmPmToggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team_to_walk);

        mDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        mProposedRoute = (Route) getIntent().getSerializableExtra(RouteDetailsActivity.ROUTE_KEY);
        mProposedBy = getIntent().getStringExtra(IUser.USER_NAME_KEY);
        getUIElements();
    }

    private void getUIElements() {
        mDateEditText = findViewById(R.id.et_proposed_day_invite_team_to_walk_activity);
        mTimeEditText = findViewById(R.id.et_proposed_time_invite_team_to_walk_activity);
        Button sendToTeamBtn = findViewById(R.id.btn_send_invitation_to_team);
        setClickListenerForSend(sendToTeamBtn);
        mAmPmToggleBtn = findViewById(R.id.btn_am_pm_toggle_invite_team_to_walk);
        setClickListenerAmPmToggleButton();
    }
    private boolean amSelected() {
        return mAmPmToggleBtn.getBackground().getConstantState().equals(getDrawable(R.drawable.ic_sunny_yellow_5dp).getConstantState());
    }

    private void setClickListenerAmPmToggleButton() {
        mAmPmToggleBtn.setOnClickListener(v -> {
            if (amSelected()) {
                mAmPmToggleBtn.setBackground(getDrawable(R.drawable.ic_moon_black_5dp));
            } else {
                mAmPmToggleBtn.setBackground(getDrawable(R.drawable.ic_sunny_yellow_5dp));
            }
        });
    }

    private void setClickListenerForSend(Button sendToTeamBtn) {
        sendToTeamBtn.setOnClickListener(v -> {
            if (validateDateAndTime()) {
                // TODO: 3/8/20 send the invitation
                if (mParsedDate.getTime() < Calendar.getInstance().getTime().getTime()) {
                    Utils.showToast(this, "Please select a date in the future.", Toast.LENGTH_LONG);
                    return;
                }
            }
        });
    }

    private boolean validateDateAndTime() {
        String day = mDateEditText.getText().toString();
        day = reformatDate(day);
        String time = mTimeEditText.getText().toString();
        String ampm = getAmPmMarker();
        if (day.isEmpty()) {
            Utils.showToast(this, "Please enter a date", Toast.LENGTH_SHORT);
            return false;
        } else if (time.isEmpty()) {
            Utils.showToast(this, "Please enter a time", Toast.LENGTH_SHORT);
            return false;
        }
        mDateTimeFormat.setLenient(false);
        day = day.replaceAll("/", "-");
        try {
            mParsedDate = mDateTimeFormat.parse(day + " " + time + " " + ampm);
            Log.i(TAG, "validateDateAndTime: date parsed successfully: " + mParsedDate);
        } catch (ParseException e) {
            Log.e(TAG, "validateDateAndTime: Error parsing date and time", e);
            return false;
        }

        return true;
    }

    private String reformatDate(String date) {
        String[] dateArr = date.split("/");
        if (dateArr.length == 3) {
            return dateArr[1] + '/' + dateArr[0] + '/' + dateArr[2];
        }
        return date;
    }

    private String getAmPmMarker() {
        if (amSelected()) {
            return "AM";
        } else {
            return "PM";
        }
    }
}
