package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vidhushi.g on 4/10/17.
 */

public class FarmerRegistration extends AppCompatActivity

{

    private CustomFontEditText name;
    private CustomFontEditText contact1;
    private CustomFontEditText contact2;
    private CustomFontEditText coconuttrees;
    private CustomFontEditText coconutyield;
    private CustomFontEditText agentname;
    private CustomFontEditText agentcontact;
    private CustomFontEditText pincode;
    private CustomFontEditText geotag;
    private CustomFontEditText email;
    private CustomFontEditText totalarea;
    private CustomFontTextView submit;
    private CircleImageView image;
    DbFarmer dbFarmer;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    public static final String update = "updateKey";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        dbFarmer = new DbFarmer(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Farmer registration</font>"));
        name = (CustomFontEditText) findViewById(R.id.farmername);
        contact1 = (CustomFontEditText) findViewById(R.id.farmercontact);
        contact2 = (CustomFontEditText) findViewById(R.id.farmercontact2);
        coconuttrees = (CustomFontEditText) findViewById(R.id.coconuttrees);
        coconutyield = (CustomFontEditText) findViewById(R.id.coconutyield);
        agentname = (CustomFontEditText) findViewById(R.id.agentname);
        agentcontact = (CustomFontEditText) findViewById(R.id.agentcontact);
        pincode = (CustomFontEditText) findViewById(R.id.pincode);
        geotag = (CustomFontEditText) findViewById(R.id.geotag);
        email = (CustomFontEditText) findViewById(R.id.email);
        totalarea = (CustomFontEditText) findViewById(R.id.totalarea);

        submit = (CustomFontTextView) findViewById(R.id.r_submittxt);
        image = (CircleImageView) findViewById(R.id.imageview);

        if (sharedpreferences.contains(update)) {
            String data = dbFarmer.getDataByFarmerid(sharedpreferences.getString(farmerid, "")).get(1);
            try {
                JSONObject farmerdata = new JSONObject(data);
                name.setText(farmerdata.getString("name"));
                contact1.setText(farmerdata.getString("contact1"));
                contact2.setText(farmerdata.getString("contact2"));
                coconuttrees.setText(farmerdata.getString("coconuttrees"));
                coconutyield.setText(farmerdata.getString("coconutyield"));
                agentname.setText(farmerdata.getString("agentname"));
                agentcontact.setText(farmerdata.getString("agentcontact"));
                pincode.setText(farmerdata.getString("pincode"));
                geotag.setText(farmerdata.getString("geotag"));
                email.setText(farmerdata.getString("email"));
                totalarea.setText(farmerdata.getString("totalarea"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().length() <= 0
                        || contact1.getText().toString().length() <= 0
                        || contact2.getText().toString().length() <= 0
                        || coconuttrees.getText().toString().length() <= 0
                        || coconutyield.getText().toString().length() <= 0
                        || agentname.getText().toString().length() <= 0
                        || agentcontact.getText().toString().length() <= 0
                        || pincode.getText().toString().length() <= 0
                        || geotag.getText().toString().length() <= 0
                        || email.getText().toString().length() <= 0
                        || totalarea.getText().toString().length() <= 0
                        ) {
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject farmerdata = new JSONObject();
                    try {
                        farmerdata.put("name", name.getText().toString());
                        farmerdata.put("contact1", contact1.getText().toString());
                        farmerdata.put("contact2", contact2.getText().toString());
                        farmerdata.put("coconuttrees", coconuttrees.getText().toString());
                        farmerdata.put("coconutyield", coconutyield.getText().toString());
                        farmerdata.put("agentname", agentname.getText().toString());
                        farmerdata.put("agentcontact", agentcontact.getText().toString());
                        farmerdata.put("pincode", pincode.getText().toString());
                        farmerdata.put("geotag", geotag.getText().toString());
                        farmerdata.put("email", email.getText().toString());
                        farmerdata.put("totalarea", totalarea.getText().toString());
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    }
                    String farmeridnew;
                    if (sharedpreferences.contains(update)) {
                        dbFarmer.addData(sharedpreferences.getString(farmerid, ""), farmerdata.toString());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(update);
                        editor.commit();
                    } else {
                        if (dbFarmer.getCountByFarmerid(pincode.getText().toString() + "farmer_1") == 0) {
                            farmeridnew = pincode.getText().toString() + "farmer_1";
                        } else {
                            farmeridnew = pincode.getText().toString() + "farmer_" + String.valueOf(dbFarmer.getAllData().size() + 1);
                        }
                        dbFarmer.addData(farmeridnew, farmerdata.toString());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(farmerid, farmeridnew);
                        editor.commit();
                    }
                    Intent io = new Intent(FarmerRegistration.this, MainActivityFarm.class);
                    startActivity(io);
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_logout, menu);
        if (!sharedpreferences.contains(update)) {
            MenuItem item = menu.findItem(R.id.logout);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.remove(update);
                editor1.commit();
                finish();
                return true;
            case R.id.logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(farmerid);
                editor.remove(update);
                editor.commit();
                Intent io = new Intent(FarmerRegistration.this, FirstLogin.class);
                startActivity(io);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
