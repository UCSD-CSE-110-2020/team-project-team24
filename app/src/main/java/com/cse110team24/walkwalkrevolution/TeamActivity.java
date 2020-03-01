package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeamActivity extends Activity {

    private Button sendInviteBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        getUIFields();
        setButtonClickListeners();
    }
    private void getUIFields() {
        sendInviteBtn = findViewById(R.id.btn_invite_team_members);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

    }
    private void setButtonClickListeners() {
        setInviteButtonOnClick();
        setBottomNavigationOnClick();
    }

    private void setInviteButtonOnClick() {
        sendInviteBtn.setOnClickListener(view -> {
            launchInviteRouteActivity();
        });
    }

    private void setBottomNavigationOnClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.action_home) {
                launchGotoHomeActivity();
            }
            if(menuItem.getItemId() == R.id.action_routes_list) {
                launchGoToRoutesActivity();
            }
            return true;
        });
    }
    private void launchInviteRouteActivity() {
        Intent intent = new Intent(this, InviteTeamMemberActivity.class);
        startActivity(intent);
    }

    public void launchGotoHomeActivity() {
        setResult(Activity.RESULT_CANCELED);
        transitionWithAnimation();
    }
    public void launchGoToRoutesActivity() {
        setResult(Activity.RESULT_CANCELED);
        transitionWithAnimation();
    }

    private void transitionWithAnimation() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


}
