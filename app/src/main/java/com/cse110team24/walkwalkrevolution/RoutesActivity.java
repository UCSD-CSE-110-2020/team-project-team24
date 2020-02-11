package com.cse110team24.walkwalkrevolution;

import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.WalkStats;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class RoutesActivity extends AppCompatActivity {

    ArrayList<Route> routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.action_home:
                        launchGoToHomeActivity();
                        break;

                    case R.id.action_routes_list:
                        break;
                }
                return true;
            }
        });

        // Fake user routes info (needs to be deleted later
        RecyclerView rvRoutes = findViewById(R.id.recycler_view);
        Route rt = new Route("Rose Canyon");
        Route rt2 = new Route("Marian Bear");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2066,5,6);
        WalkStats ws = new WalkStats(22, 33, 0.01, calendar);
        rt.setStats(ws);

        routes.add(rt);
        routes.add(rt2);
        Collections.sort(routes);
        RouteAdapter adapter = new RouteAdapter(routes, this);
        rvRoutes.setAdapter(adapter);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void launchGoToHomeActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
