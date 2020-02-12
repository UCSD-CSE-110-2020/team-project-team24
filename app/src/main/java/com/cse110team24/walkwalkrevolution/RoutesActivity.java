package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutesActivity extends AppCompatActivity {
    public static final String TAG = "RoutesActivity";
    public static final int TO_REQUEST_CODE = 9;

    public static final String LIST_SAVE_FILE = ".WWR_route_list_data";
    public static final String SAVE_FILE_KEY = "save_file";

    RouteAdapter adapter;
    RecyclerView rvRoutes;
    FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    List<Route> routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getUIElements();
        setListeners();

        checkForExistingSavedRoutes();
        configureRecyclerViewAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void launchGoToHomeActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void getUIElements() {
        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        rvRoutes = findViewById(R.id.recycler_view);

    }

    private void setListeners() {
        setFabOnClickListener();
        setBottomNavItemSelectedListener();
    }

    private void setFabOnClickListener() {
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }

    private void setBottomNavItemSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.action_home:
                    launchGoToHomeActivity();
                    break;

                case R.id.action_routes_list:
                    break;
            }
            return true;
        });
    }

    private void checkForExistingSavedRoutes() {
        String filename = getIntent().getStringExtra(SAVE_FILE_KEY);
        filename = (filename == null) ? LIST_SAVE_FILE : filename;
        try {
            List<Route> tempList = RoutesManager.readList(filename, this);
            routes.addAll(tempList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "checkForExistingSavedRoutes: no routes found ", e);
        }
        Collections.sort(routes);
        Log.i(TAG, "checkForExistingSavedRoutes: list of saved routes found with size " + routes.size());
    }

    private void configureRecyclerViewAdapter() {
        adapter = new RouteAdapter(routes, this);
        rvRoutes.setAdapter(adapter);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
