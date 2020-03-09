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
import java.util.Date;

public class InviteTeamToWalkActivity extends AppCompatActivity {
    private static final String TAG = "WWR_InviteTeamToWalkActivity";

    private Route proposedRoute;
    private String proposedBy;
    private SimpleDateFormat dateTimeFormat;
    private Date parsedDate;

    private EditText dateEditText;
    private EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team_to_walk);

        dateTimeFormat = new SimpleDateFormat("dd-MM-yyyHH:mm");
        proposedRoute = (Route) getIntent().getSerializableExtra(RouteDetailsActivity.ROUTE_KEY);
        proposedBy = getIntent().getStringExtra(IUser.USER_NAME_KEY);
        getUIElements();
    }

    private void getUIElements() {
        dateEditText = findViewById(R.id.et_proposed_day_invite_team_to_walk_activity);
        timeEditText = findViewById(R.id.et_proposed_time_invite_team_to_walk_activity);
        Button sendToTeamBtn = findViewById(R.id.btn_send_invitation_to_team);
        setClickListenerForSend(sendToTeamBtn);
    }

    private void setClickListenerForSend(Button sendToTeamBtn) {
        sendToTeamBtn.setOnClickListener(v -> {
            if (validateDateAndTime()) {
                // TODO: 3/8/20 send the invitation
            }
        });
    }

    private boolean validateDateAndTime() {
        String day = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        if (day.isEmpty()) {
            Utils.showToast(this, "Please enter a date", Toast.LENGTH_SHORT);
            return false;
        } else if (time.isEmpty()) {
            Utils.showToast(this, "Please enter a time", Toast.LENGTH_SHORT);
            return false;
        }
        dateTimeFormat.setLenient(false);
        day.replaceAll("-", "\\");
        try {
            parsedDate = dateTimeFormat.parse(day + time);
            Log.i(TAG, "validateDateAndTime: date parsed successfully: " + parsedDate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}
