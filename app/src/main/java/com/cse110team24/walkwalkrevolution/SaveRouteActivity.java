package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

public class SaveRouteActivity extends AppCompatActivity {
    private static final String TAG = "SaveRouteActivity";

    public static final String FILE_NAME = "aFileNameICameUpWith.txt";
    public static final String WALK_STATS_KEY = "walk_stats";
    public static final int REQUEST_CODE = 7;
    public final Context thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);

        final EditText editTextTitle = findViewById(R.id.et_save_route_title);
        final EditText editTextLocation = findViewById(R.id.et_save_route_location);
        final EditText editTextNotes = findViewById(R.id.et_route_notes);
        final RadioGroup routeTypeRdGroup = findViewById(R.id.radiogroup_route_type);
        final RadioGroup terrainTypeRdGroup = findViewById(R.id.rd_group_terrain_type);
        final RadioGroup surfaceTypeRdGroup = findViewById(R.id.rd_group_surface_type);
        final RadioGroup landTypeRdGroup = findViewById(R.id.rd_group_land_type);
        final RadioGroup difficultyTypeRdGroup = findViewById(R.id.rd_group_difficulty);



        Button doneButton = (Button) findViewById(R.id.btn_save_route);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Title is required", Toast.LENGTH_SHORT).show();
                } else {
                    Route route = new Route(editTextTitle.getText().toString());
                    //route.setTitle(editTextTitle.getText().toString());
                    route.setStartingLocation(editTextLocation.getText().toString());
                    route.setNotes(editTextNotes.getText().toString());
                    RouteEnvironment env = new RouteEnvironment();

                    if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.radio_btn_loop) {
                        env.setRouteType(RouteEnvironment.RouteType.LOOP);
                    }else if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.radio_btn_out_back) {
                        env.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
                    }

                    if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.radio_btn_loop) {
                        env.setRouteType(RouteEnvironment.RouteType.LOOP);
                    }else if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.radio_btn_out_back) {
                        env.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
                    }


                    if(terrainTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_flat) {
                        env.setTerrainType(RouteEnvironment.TerrainType.FLAT);
                    }else if(terrainTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_hilly) {
                        env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
                    }


                    if(surfaceTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_even) {
                        env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
                    }else if(surfaceTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_uneven) {
                        env.setSurfaceType(RouteEnvironment.SurfaceType.UNEVEN);
                    }


                    if(difficultyTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_hard) {
                        env.setDifficulty(RouteEnvironment.Difficulty.HARD);
                    }else if(difficultyTypeRdGroup.getCheckedRadioButtonId() == R.id.rd_btn_easy) {
                        env.setDifficulty(RouteEnvironment.Difficulty.EASY);
                    }

                    route.setEnvironment(env);

                    try {
                        RoutesManager.writeSingle(route, FILE_NAME, thisActivity);
                    } catch(IOException ex){}

                    finish();
                }
            }
        });
    }

}
