package com.cse110team24.walkwalkrevolution;

import android.os.Bundle;

//import com.cse110team24.walkwalkrevolution.models.Route;
//import com.cse110team24.walkwalkrevolution.models.RouteEnvironment;
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

public class SavingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        final RadioGroup routeTypeRdGroup = findViewById(R.id.routeTypeRdGroup);
        //final RadioButton loopButton = findViewById(R.id.loopButton);
        //final RadioButton outAndForthButton = findViewById(R.id.outAndForthButton);



        Button doneButton = (Button) findViewById(R.id.saveButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Title is required", Toast.LENGTH_SHORT).show();
                } else {
                    // set terrain type
//                    Route route = new Route("title");
//                    RouteEnvironment env = new RouteEnvironment();
//                    env.setTerrainType(RouteEnvironment.TerrainType.FLAT);
//                    route.setEnvironment(env);
//
//                    int routeTypeRdBtnID = radioButtonGroup.getCheckedRadioButtonId();
//                    if(routeTypeRdGroup.getCheckedRadioButtonId() == R.id.loopButton) {
//
//                    }

                }

                finish();
            }
        });
    }

}
