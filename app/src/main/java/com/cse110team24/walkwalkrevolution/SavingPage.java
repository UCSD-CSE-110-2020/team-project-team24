package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.os.Bundle;

//import com.cse110team24.walkwalkrevolution.models.Route;
//import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SavingPage extends AppCompatActivity {

    public static final String FILE_NAME = "aFileNameICameUpWith.txt";
    public final Context thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_page);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
