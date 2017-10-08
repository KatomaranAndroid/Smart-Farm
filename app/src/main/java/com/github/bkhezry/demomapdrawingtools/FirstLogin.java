package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.utils.Farmer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vidhushi.g on 6/10/17.
 */

public class FirstLogin extends AppCompatActivity

{


    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    public static final String update = "updateKey";
    DbFarmer dbFarmer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_login_file);


        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        dbFarmer = new DbFarmer(this);

        getSupportActionBar().setTitle("LOGIN");
        final CustomFontEditText username = (CustomFontEditText) findViewById(R.id.username);
        final CustomFontEditText password = (CustomFontEditText) findViewById(R.id.password);
        CustomFontTextView submit = (CustomFontTextView) findViewById(R.id.submittxt);
        CustomFontTextView addfarmer = (CustomFontTextView) findViewById(R.id.addfarmer);

        addfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(FirstLogin.this, FarmerRegistration.class);
                startActivity(io);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length() <= 0 || password.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    List<ArrayList<String>> farmerList = dbFarmer.getAllData();
                    boolean trig = false;
                    for (int i = 0; i < farmerList.size(); i++) {
                        JsonParser parser = new JsonParser();
                        JsonObject o = parser.parse(farmerList.get(i).get(1)).getAsJsonObject();
                        Farmer farmer = new Gson().fromJson(o, Farmer.class);

                        if (username.getText().toString().equals(farmer.getName())) {
                            if (password.getText().toString().equals(farmerList.get(i).get(2))) {
                                trig = true;
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(farmerid, farmerList.get(i).get(0));
                                editor.commit();
                                Intent io = new Intent(FirstLogin.this, MainActivityFarm.class);
                                startActivity(io);
                                finish();
                            }
                        }
                    }
                    if (!trig) {
                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
