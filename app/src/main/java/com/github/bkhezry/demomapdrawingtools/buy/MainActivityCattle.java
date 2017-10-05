package com.github.bkhezry.demomapdrawingtools.buy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.dp.DbPro;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Crop - Coconut");
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
}
