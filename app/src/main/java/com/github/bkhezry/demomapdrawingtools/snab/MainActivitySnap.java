package com.github.bkhezry.demomapdrawingtools.snab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.bkhezry.demomapdrawingtools.MainActivityFarm;
import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.cal.ui.SampleActivity;
import com.github.bkhezry.demomapdrawingtools.cal.utils.PermissionHelper;
import com.github.bkhezry.demomapdrawingtools.utils.TimeService;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivitySnap extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    public static final String ORIENTATION = "orientation";

    private RecyclerView mRecyclerView;
    private boolean mHorizontal = true;


    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);
        if (PermissionHelper.checkOrRequest(MainActivitySnap.this, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startService(new Intent(this, TimeService.class));
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_mainn);
        toolbar.setOnMenuItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        if (savedInstanceState == null) {
            mHorizontal = true;
        } else {
            mHorizontal = savedInstanceState.getBoolean(ORIENTATION);
        }

        setupAdapter();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ORIENTATION, mHorizontal);
    }

    private void setupAdapter() {

        SnapAdapter snapAdapter = new SnapAdapter();
        if (mHorizontal) {
            snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Income", getAppsEarning()));
            snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Expanse", getAppsExpanse()));
            snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Weather (Pollachi)", getAppsWeather()));
            mRecyclerView.setAdapter(snapAdapter);
        }
    }

    private List<App> getAppsEarning() {
        List<App> apps = new ArrayList<>();
        apps.add(new App("Today:\u20B92,000:50", R.drawable.adduser, 4.6f));
        apps.add(new App("This Week:\u20B930,000:70", R.drawable.adduser, 4.8f));
        apps.add(new App("This Month:\u20B94,50,600:80", R.drawable.adduser, 4.5f));
        apps.add(new App("This Year:\u20B98,45,600:95", R.drawable.adduser, 4.2f));
        return apps;
    }

    private List<App> getAppsExpanse() {
        List<App> apps = new ArrayList<>();
        apps.add(new App("Running expanse:\u20B91,500:40", R.drawable.adduser, 4.6f));
        apps.add(new App("EMI:\u20B920,500:65", R.drawable.adduser, 4.8f));
        apps.add(new App("Quarterly tax:\u20B93,44,600:74", R.drawable.adduser, 4.5f));
        apps.add(new App("RC:\u20B96,65,600:88", R.drawable.adduser, 4.2f));
        return apps;
    }

    private List<App> getAppsWeather() {
        List<App> apps = new ArrayList<>();
        apps.add(new App("Partly Suuny:25\u2103", R.drawable.weather_one, 4.6f));
        apps.add(new App("Cloudy:22\u2103", R.drawable.weather_two, 4.8f));
        apps.add(new App("Mist:26\u2103", R.drawable.weather_three, 4.5f));
        apps.add(new App("Partly Cloudy:23\u2103", R.drawable.weather_four, 4.2f));
        return apps;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, MainActivityFarm.class));
        } else if (item.getItemId() == R.id.action_calendar) {
            if (PermissionHelper.checkOrRequest(MainActivitySnap.this, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                startActivity(new Intent(this, SampleActivity.class));
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, SampleActivity.class));
        }else if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, TimeService.class));
        }
    }

}