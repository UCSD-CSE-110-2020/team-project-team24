package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveRouteActivity extends AppCompatActivity {
    private static final String TAG = "SaveRouteActivity";

    public static final String FILE_NAME = ".WRR_new_saved_route";
    public static final String WALK_STATS_KEY = "walk_stats";
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
            route.setEnvironment(env);
            route.setStats(stats);
            setRouteType();
            setTerrainType();
            setSurfaceType();
            setTrailType();
            setDifficulty();
            saveNewRouteToStorage();
            setResult(Activity.RESULT_OK);
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
            route = new Route(title);
            route.setStartingLocation(editTextLocation.getText().toString());
            route.setNotes(editTextNotes.getText().toString());
        }
    }

    private void setRouteType() {
        if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.radio_btn_loop) {
            env.setRouteType(RouteEnvironment.RouteType.LOOP);
        }else if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.radio_btn_out_back) {
            env.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
        }
    }

    private void setTerrainType() {
        if(terrainTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_flat) {
            env.setTerrainType(RouteEnvironment.TerrainType.FLAT);
        }else if(terrainTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_hilly) {
            env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
        }
    }

    private void setSurfaceType() {
        if(surfaceTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_even) {
            env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
        }else if(surfaceTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_uneven) {
            env.setSurfaceType(RouteEnvironment.SurfaceType.UNEVEN);
        }
    }

    private void setTrailType() {
        if (landTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_street) {
            env.setTrailType(RouteEnvironment.TrailType.STREETS);
        } else if (landTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_trail) {
            env.setTrailType(RouteEnvironment.TrailType.TRAIL);
        }
    }

    private void setDifficulty() {
        if(difficultyTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_hard) {
            env.setDifficulty(RouteEnvironment.Difficulty.HARD);
        }else if(difficultyTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_easy) {
            env.setDifficulty(RouteEnvironment.Difficulty.EASY);
        }
    }

    private void saveNewRouteToStorage() {
        List<Route> storedRoutes = getStoredRoutes();
        storedRoutes.add(route);
        try {
            RoutesManager.writeList(storedRoutes, RoutesActivity.LIST_SAVE_FILE, this);
        } catch (IOException e) {
            Log.e(TAG, "saveNewRouteToStorage: could not write new route to file", e);
        }
        Log.i(TAG, "saveNewRouteToStorage: " + craftRouteLogMessage());
    }

    private List<Route> getStoredRoutes() {
        List<Route> storedRoutes;
        try {
            storedRoutes = RoutesManager.readList(RoutesActivity.LIST_SAVE_FILE, this);
        } catch (IOException e) {
            Log.e(TAG, "saveNewRouteToStorage: could not load stored routes", e);
            storedRoutes = new ArrayList<>();
        }

        return storedRoutes;
    }

    private String craftRouteLogMessage() {
        if (stats == null ) {
            return "new Route object created with: \n"
                    + "\ttitle: " + '"' + route.getTitle() + "\"\n";
        }
        return "new Route object created with: \n"
                + "\ttitle: " + '"' + route.getTitle() + "\"\n"
                + "\tWalkStats: \n"
                    + "\t\tsteps: " + stats.getSteps() + "\n"
                    + "\t\tdistance (miles): " + stats.getDistance() + "\n"
                    + "\t\ttime elapsed (minutes): " + stats.timeElapsedInMinutes() + "\n"
                    + "\t\tdate of completion: " + stats.getDateCompleted().getTime();
    }

}
