package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.utils.Farmer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vidhushi.g on 6/10/17.
 */

public class PasswordActivity extends AppCompatActivity

{


    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    public static final String update = "updateKey";
    DbFarmer dbFarmer;
    String farmerId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_activity);
        dbFarmer = new DbFarmer(this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(farmerid)) {
            farmerId = sharedpreferences.getString(farmerid, "");
        }
        ArrayList<String> farmerdata = dbFarmer.getDataByFarmerid(farmerId);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(farmerdata.get(1)).getAsJsonObject();
        Farmer farmer = new Gson().fromJson(o, Farmer.class);
        getSupportActionBar().setTitle("Verification");
        final CustomFontEditText username = (CustomFontEditText) findViewById(R.id.username);
        final CustomFontEditText password = (CustomFontEditText) findViewById(R.id.password);
        final CustomFontEditText confirmpassword = (CustomFontEditText) findViewById(R.id.confirmpassword);
        final CustomFontEditText otp = (CustomFontEditText) findViewById(R.id.otp);
        CircleImageView image = (CircleImageView) findViewById(R.id.imageview);
        if (farmer.getImage() != null) {
            Glide.with(PasswordActivity.this).load(farmer.getImage())
                    .centerCrop()
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.profile)
                    .into(image);
        }
        CustomFontTextView submit = (CustomFontTextView) findViewById(R.id.submittxt);
        username.setText(farmer.getName());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length() <= 0 || password.getText().toString().length() <= 0
                        || confirmpassword.getText().toString().length() <= 0 || otp.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().trim().equals(confirmpassword.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password dosn't match", Toast.LENGTH_SHORT).show();
                } else {
                    dbFarmer.updatePassByFarmerid(farmerId, password.getText().toString());
                    Intent io = new Intent(PasswordActivity.this, MainActivityFarm.class);
                    startActivity(io);
                    finish();
                }
            }
        });


    }
}
