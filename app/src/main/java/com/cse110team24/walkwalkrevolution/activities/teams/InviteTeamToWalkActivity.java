package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InviteTeamToWalkActivity extends AppCompatActivity {
    private static final String TAG = "WWR_InviteTeamToWalkActivity";

    private Route mProposedRoute;
    private String mProposedBy;
    private String mTeamUid;
    private SimpleDateFormat mDateTimeFormat;
    private Date mParsedDate;

    private EditText mDateEditText;
    private EditText mTimeEditText;
    private ImageButton mAmPmToggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team_to_walk);

        mDateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        mProposedRoute = (Route) getIntent().getSerializableExtra(RouteDetailsActivity.ROUTE_KEY);
        mProposedBy = getIntent().getStringExtra(IUser.USER_NAME_KEY);
        mTeamUid = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE).getString(IUser.TEAM_UID_KEY, "");
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
        return mAmPmToggleBtn.getBackground().getConstantState().equals(getDrawable(R.drawable.ic_sunny_yellow_24dp).getConstantState());
    }

    private void setClickListenerAmPmToggleButton() {
        mAmPmToggleBtn.setOnClickListener(v -> {
            if (amSelected()) {
                mAmPmToggleBtn.setBackground(getDrawable(R.drawable.ic_moon_black_24dp));
            } else {
                mAmPmToggleBtn.setBackground(getDrawable(R.drawable.ic_sunny_yellow_24dp));
            }
        });
    }

    private void setClickListenerForSend(Button sendToTeamBtn) {
        sendToTeamBtn.setOnClickListener(v -> {
            // TODO: 3/9/20 check if a walk already pending
            if (validateDateAndTime()) {
                if (mParsedDate.getTime() < Calendar.getInstance().getTime().getTime()) {
                    Utils.showToast(this, "Please select a date in the future.", Toast.LENGTH_LONG);
                    return;
                }

                TeamsDatabaseService db = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
                TeamWalk teamWalk = TeamWalk.builder()
                        .addProposedBy(mProposedBy)
                        .addProposedDateAndTime(new Timestamp(mParsedDate))
                        .addProposedRoute(mProposedRoute)
                        .addTeamUid(mTeamUid)
                        .build();
                teamWalk.setWalkUid(db.updateCurrentTeamWalk(teamWalk));
                Utils.showToast(this, "Invitation sent", Toast.LENGTH_SHORT);
                mDateEditText.setText("");
                mTimeEditText.setText("");
            }
        });
    }

    /**
     * Date must be in the [human] format mm/dd/yyyy
     * Time must be in the [human] format hh:mm (12 hour time)
     * side effect: parses date and time if valid into mParsedDate.
     * @return true if both date and time match format
     */
    private boolean validateDateAndTime() {
        String day = mDateEditText.getText().toString();
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
        try {
            mParsedDate = mDateTimeFormat.parse(day + " " + time + " " + ampm);
            Log.i(TAG, "validateDateAndTime: date parsed successfully: " + mParsedDate);
        } catch (ParseException e) {
            Log.e(TAG, "validateDateAndTime: Error parsing date and time", e);
            Utils.showToast(this, "Please enter a valid date and time.", Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    // checks the button's icon to determine if AM or PM time is selected
    private String getAmPmMarker() {
        if (amSelected()) {
            return "AM";
        } else {
            return "PM";
        }
    }
}
