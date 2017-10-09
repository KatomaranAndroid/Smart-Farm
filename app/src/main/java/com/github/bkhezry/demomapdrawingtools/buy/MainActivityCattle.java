package com.github.bkhezry.demomapdrawingtools.buy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.MainActivityFarm;
import com.github.bkhezry.demomapdrawingtools.MapsFragActivity;
import com.github.bkhezry.demomapdrawingtools.ProfileActivity;
import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.dp.DbPro;
import com.github.bkhezry.demomapdrawingtools.trees.TreesActivity;
import com.github.bkhezry.demomapdrawingtools.utils.FarmNew;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;


public class MainActivityCattle extends AppCompatActivity {

    public static final String ORIENTATION = "orientation";

    private RecyclerView mRecyclerView;
    DbFarmer dbFarmer;
    DbPro dbPro;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    String farmerId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buy);

        dbFarmer = new DbFarmer(this);
        dbPro = new DbPro(this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(farmerid)) {
            farmerId = sharedpreferences.getString(farmerid, "").trim();
        }
       mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        getSupportActionBar().setTitle("Smart Crop - Coconut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.market);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        setupAdapter();
    }

    private void setupAdapter() {
        SnapAdapter snapAdapter = new SnapAdapter(MainActivityCattle.this);
        snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Tender Coconut", getFarm("Tender coconut")));
        snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Copra", getFarm("Copra")));
        snapAdapter.addSnap(new Snap(Gravity.CENTER_HORIZONTAL, "Coconut", getFarm("Coconut")));
        mRecyclerView.setAdapter(snapAdapter);
    }

    private List<FarmNew> getFarm(String type) {
        List<FarmNew> farmNews = new ArrayList<>();
        List<ArrayList<String>> proList = dbPro.getAllData();
        for (int i = 0; i < proList.size(); i++) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(proList.get(i).get(1)).getAsJsonObject();
                FarmNew farmNew = new Gson().fromJson(o, FarmNew.class);
                if (farmNew.getCropname().equals(type)) {
                    farmNews.add(farmNew);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return farmNews;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_farmer_white, menu);
        MenuItem item = menu.findItem(R.id.market);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent io = new Intent();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.tree:
                Intent market = new Intent(MainActivityCattle.this, TreesActivity.class);
                startActivityForResult(market, 1);
                return true;
            case R.id.account:
                Intent account = new Intent(MainActivityCattle.this, ProfileActivity.class);
                startActivity(account);
                return true;
            case R.id.mykart:
                Intent mykart = new Intent(MainActivityCattle.this, MainActivityFarm.class);
                startActivity(mykart);
                return true;
            case R.id.plot:
                Intent tree = new Intent(MainActivityCattle.this, MapsFragActivity.class);
                startActivity(tree);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
