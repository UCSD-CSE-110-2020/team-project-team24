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

    public static final String FILE_NAME = "aFileNameICameUpWith.txt";
    public final Context thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);

        final EditText editTextTitle = findViewById(R.id.editTextTitle);
        final EditText editTextLocation = findViewById(R.id.editTextLocation);
        final EditText editTextNotes = findViewById(R.id.editTextNotes);
        final RadioGroup routeTypeRdGroup = findViewById(R.id.routeTypeRdGroup);
        final RadioGroup terrainTypeRdGroup = findViewById(R.id.terrainTypeRdGroup);
        final RadioGroup surfaceTypeRdGroup = findViewById(R.id.surfaceTypeRdGroup);
        final RadioGroup landTypeRdGroup = findViewById(R.id.landTypeRdGroup);
        final RadioGroup difficultyTypeRdGroup = findViewById(R.id.difficultyTypeRdGroup);



        Button doneButton = (Button) findViewById(R.id.saveButton);

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

                    if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.loopButton) {
                        env.setRouteType(RouteEnvironment.RouteType.LOOP);
                    }else if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.outAndForthButton) {
                        env.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
                    }

                    if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.loopButton) {
                        env.setRouteType(RouteEnvironment.RouteType.LOOP);
                    }else if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.outAndForthButton) {
                        env.setRouteType(RouteEnvironment.RouteType.OUT_AND_BACK);
                    }


                    if(terrainTypeRdGroup.getCheckedRadioButtonId() == R.id.flatButton) {
                        env.setTerrainType(RouteEnvironment.TerrainType.FLAT);
                    }else if(terrainTypeRdGroup.getCheckedRadioButtonId() == R.id.hillyButton) {
                        env.setTerrainType(RouteEnvironment.TerrainType.HILLY);
                    }


                    if(surfaceTypeRdGroup.getCheckedRadioButtonId() == R.id.evenButton) {
                        env.setSurfaceType(RouteEnvironment.SurfaceType.EVEN);
                    }else if(surfaceTypeRdGroup.getCheckedRadioButtonId() == R.id.unevenButton) {
                        env.setSurfaceType(RouteEnvironment.SurfaceType.UNEVEN);
                    }


                    if(difficultyTypeRdGroup.getCheckedRadioButtonId() == R.id.hardButton) {
                        env.setDifficulty(RouteEnvironment.Difficulty.HARD);
                    }else if(difficultyTypeRdGroup.getCheckedRadioButtonId() == R.id.easyButton) {
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
