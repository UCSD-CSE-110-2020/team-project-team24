package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.RouteBuilder;
import com.cse110team24.walkwalkrevolution.models.route.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;

public class ScheduledWalkActivity extends AppCompatActivity {

    private Button directionsBtn;
    private String startingLocation;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_walk);

        route = new Route("Tecolote Canyon walk")
            .setStartingLocation("Tecolote Canyon");

        directionsBtn = findViewById(R.id.btn_directions);
        directionsBtn.setOnClickListener(v -> {
            launchMap();
        });
    }

    private void getStartingLocation() {
        startingLocation = route.getStartingLocation();
    }


    private void launchMap() {
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

}
