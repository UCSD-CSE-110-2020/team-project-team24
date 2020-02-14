package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.models.Route;

public class RouteDetailsActivity extends AppCompatActivity {
    public static final String ROUTE_KEY = "route";

    Route displayedRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);
    }
}
