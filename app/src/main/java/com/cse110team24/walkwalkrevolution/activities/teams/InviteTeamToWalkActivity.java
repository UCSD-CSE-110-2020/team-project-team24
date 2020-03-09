package com.cse110team24.walkwalkrevolution.activities.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

public class InviteTeamToWalkActivity extends AppCompatActivity {

    private Route proposedRoute;
    private String proposedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team_to_walk);
        proposedRoute = (Route) getIntent().getSerializableExtra(RouteDetailsActivity.ROUTE_KEY);
        proposedBy = getIntent().getStringExtra(IUser.USER_NAME_KEY);
    }
}
