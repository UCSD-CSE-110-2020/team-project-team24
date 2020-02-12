package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoutesPage extends AppCompatActivity {

    Button addRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_page);
        addRoute = findViewById(R.id.addRoute);

        addButtonAction();
    }

    public void addButtonAction(){

        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchaddSavingPage();
            }
        });

    }
    public void launchaddSavingPage() {
        Intent intent = new Intent(this, SavingPage.class);
        startActivity(intent);
    }

}
