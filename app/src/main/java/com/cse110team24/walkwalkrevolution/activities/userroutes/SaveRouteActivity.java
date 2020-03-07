package com.cse110team24.walkwalkrevolution.activities.userroutes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SaveRouteActivity extends AppCompatActivity {
    private static final String TAG = "WWR_SaveRouteActivity";

    public static final String WALK_STATS_KEY = "walk_stats";
    public static final String NEW_ROUTE_KEY = "new_route";
    public static final int REQUEST_CODE = 7;

    private Route route;
    private RouteEnvironment env;
    private WalkStats stats;

    private EditText editTextTitle;
    private EditText editTextLocation;
    private EditText editTextNotes;
    private RadioGroup routeTypeRdGroup;
    private RadioGroup terrainTypeRdGroup;
    private RadioGroup surfaceTypeRdGroup;
    private RadioGroup landTypeRdGroup;
    private RadioGroup difficultyTypeRdGroup;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);

        getUIFields();

        env = new RouteEnvironment();
        stats = (WalkStats) getIntent().getSerializableExtra(WALK_STATS_KEY);

        doneButton.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();
            createRoute(title);
            if(route == null) {
                return;
            }

            Intent intent = new Intent()
                    .putExtra(NEW_ROUTE_KEY, route);
            setResult(Activity.RESULT_OK, intent);
            Log.i(TAG, "onCreate: new route created: " + route);
            finish();
        });
    }

    private void getUIFields() {
        editTextTitle = findViewById(R.id.et_save_route_title);
        editTextLocation = findViewById(R.id.et_save_route_location);
        editTextNotes = findViewById(R.id.et_route_notes);
        routeTypeRdGroup = findViewById(R.id.radiogroup_route_type);
        terrainTypeRdGroup = findViewById(R.id.rd_group_terrain_type);
        surfaceTypeRdGroup = findViewById(R.id.rd_group_surface_type);
        landTypeRdGroup = findViewById(R.id.rd_group_land_type);
        difficultyTypeRdGroup = findViewById(R.id.rd_group_difficulty);
        doneButton = findViewById(R.id.btn_save_route);
    }

    private void createRoute(String title) {
        if (title.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Title is required", Toast.LENGTH_SHORT).show();
            route = null;
        }
        else {
            createRouteEnv();
            route = new Route.Builder(title)
                    .addStartingLocation(editTextLocation.getText().toString())
                    .addNotes(editTextNotes.getText().toString())
                    .addRouteEnvironment(env)
                    .addWalkStats(stats)
                    .build();
        }
    }

    private void createRouteEnv() {
        setRouteType();
        setTerrainType();
        setSurfaceType();
        setTrailType();
        setDifficulty();
    }

    private void setRouteType() {
        switch(routeTypeRdGroup.getCheckedRadioButtonId()) {
            case R.id.radio_btn_loop:
                env.setRouteType(RouteEnvironment.RouteType.LOOP);
                break;
            case R.id.radio_btn_out_back:
                env.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
        }
    }

    private void setTerrainType() {
        switch(terrainTypeRdGroup.getCheckedRadioButtonId()) {
            case R.id.rd_btn_flat:
                env.setTerrainType(RouteEnvironment.TerrainType.FLAT);
                break;
            case R.id.rd_btn_hilly:
                env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        }
    }

    private void setSurfaceType() {
        switch(surfaceTypeRdGroup.getCheckedRadioButtonId()) {
            case R.id.rd_btn_even:
                env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
                break;
            case R.id.rd_btn_uneven:
                env.setSurfaceType(RouteEnvironment.SurfaceType.UNEVEN);
        }
    }

    private void setTrailType() {
        switch(landTypeRdGroup.getCheckedRadioButtonId()) {
            case R.id.rd_btn_street:
                env.setTrailType(RouteEnvironment.TrailType.STREETS);
                break;
            case R.id.rd_btn_trail:
                env.setTrailType(RouteEnvironment.TrailType.TRAIL);
        }
    }

    private void setDifficulty() {
        switch(difficultyTypeRdGroup.getCheckedRadioButtonId()) {
            case R.id.rd_btn_hard:
                env.setDifficulty(RouteEnvironment.Difficulty.HARD);
                break;
            case R.id.rd_btn_moderate:
                env.setDifficulty(RouteEnvironment.Difficulty.MODERATE);
                break;
            case R.id.rd_btn_easy:
                env.setDifficulty(RouteEnvironment.Difficulty.EASY);
        }
    }
}
