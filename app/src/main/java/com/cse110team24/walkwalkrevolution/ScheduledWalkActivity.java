package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cse110team24.walkwalkrevolution.models.route.Route;

public class ScheduledWalkActivity extends AppCompatActivity {

    private Button directionsBtn;
    private String startingLocation;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_walk);

        // TODO replace fake walk with real
        route = new Route("Tecolote Canyon walk")
            .setStartingLocation("Tecolote Canyon Trail Parking");

        directionsBtn = findViewById(R.id.btn_location);
        directionsBtn.setOnClickListener(v -> {
            launchMap();
        });
    }

    private void getStartingLocation() {
        startingLocation = route.getStartingLocation();
    }


    private void launchMap() {
        getStartingLocation();
        String map = "http://maps.google.co.in/maps?q=" + startingLocation;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    public String getStartingLoc() {
        return startingLocation;
    }

}
