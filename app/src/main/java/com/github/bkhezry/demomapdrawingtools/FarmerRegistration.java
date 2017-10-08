package com.github.bkhezry.demomapdrawingtools;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.utils.Farmer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import katomaran.evao.lib.qrmodule.activity.QrScannerActivity;

/**
 * Created by vidhushi.g on 4/10/17.
 */

public class FarmerRegistration extends AppCompatActivity

{
    private int CAMERA_PERMISSION_CODE = 23;

    private CustomFontEditText name;
    private CustomFontEditText contact1;
    private CustomFontEditText contact2;
    private CustomFontEditText coconuttrees;
    private CustomFontEditText coconutyield;
    private CustomFontEditText agentname;
    private CustomFontEditText agentcontact;
    private CustomFontEditText pincode;
    private CustomFontEditText geotag;
    private AutoCompleteTextView email;
    private CustomFontEditText totalarea;
    private CustomFontTextView submit;
    private CircleImageView image;
    private CustomFontEditText aadharnumber;
    private CustomFontEditText fathername;
    private CustomFontEditText irrigated;
    private CustomFontEditText address;
    private CustomFontEditText address1;
    private CustomFontEditText ifscnumber;
    private CustomFontEditText accountnumber;
    private CustomFontEditText shgname;
    private CustomFontEditText fponame;
    DbFarmer dbFarmer;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    public static final String update = "updateKey";
    String imageUri = "";
    private ImageView aadharimage;

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
        aadharimage = (ImageView) findViewById(R.id.aadharimg);
        image = (CircleImageView) findViewById(R.id.imageview);
        aadharnumber = (CustomFontEditText) findViewById(R.id.aadharnumer);
        name = (CustomFontEditText) findViewById(R.id.farmername);
        fathername = (CustomFontEditText) findViewById(R.id.fathername);
        contact1 = (CustomFontEditText) findViewById(R.id.farmercontact);
        contact2 = (CustomFontEditText) findViewById(R.id.farmercontact2);
        coconuttrees = (CustomFontEditText) findViewById(R.id.coconuttrees);
        coconutyield = (CustomFontEditText) findViewById(R.id.coconutyield);
        irrigated = (CustomFontEditText) findViewById(R.id.irrication);
        totalarea = (CustomFontEditText) findViewById(R.id.totalarea);
        geotag = (CustomFontEditText) findViewById(R.id.geotag);
        address = (CustomFontEditText) findViewById(R.id.address);
        address1 = (CustomFontEditText) findViewById(R.id.address1);
        pincode = (CustomFontEditText) findViewById(R.id.pincode);
        email = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        ifscnumber = (CustomFontEditText) findViewById(R.id.ifscnumber);
        accountnumber = (CustomFontEditText) findViewById(R.id.accountnumber);
        shgname = (CustomFontEditText) findViewById(R.id.shgname);
        fponame = (CustomFontEditText) findViewById(R.id.fpoName);
        submit = (CustomFontTextView) findViewById(R.id.r_submittxt);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup();
                PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        Glide.with(FarmerRegistration.this).load(pickResult.getUri())
                                .centerCrop()
                                .dontAnimate()
                                .thumbnail(0.5f)
                                .placeholder(R.drawable.profile)
                                .into(image);
                        imageUri = pickResult.getUri().toString();
                    }
                })
                        .show(FarmerRegistration.this);
            }
        });


        aadharimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isReadStorageAllowed()) {
                    requestStoragePermission();
                } else {
                    startActivityForResult(
                            new Intent(FarmerRegistration.this, QrScannerActivity.class),
                            QrScannerActivity.QR_REQUEST_CODE);
                }
            }
        });
        if (sharedpreferences.contains(update)) {
            String data = dbFarmer.getDataByFarmerid(sharedpreferences.getString(farmerid, "")).get(1);
            try {
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(data).getAsJsonObject();
                Farmer farmer = new Gson().fromJson(o, Farmer.class);
                imageUri = farmer.getImage();
                name.setText(farmer.getName());
                fathername.setText(farmer.getFathername());
                aadharnumber.setText(farmer.getAadharnumber());
                contact1.setText(farmer.getContact1());
                contact2.setText(farmer.getContact1());
                coconuttrees.setText(farmer.getCoconuttrees());
                coconutyield.setText(farmer.getYield());
                irrigated.setText(farmer.getIrrigated());
                totalarea.setText(farmer.getArea());
                geotag.setText(farmer.getGeotag());
                address.setText(farmer.getAddress1());
                address1.setText(farmer.getAddress2());
                pincode.setText(farmer.getPincode());
                email.setText(farmer.getGmail());
                ifscnumber.setText(farmer.getIfscnumber());
                accountnumber.setText(farmer.getAccountnumber());
                shgname.setText(farmer.getShgname());
                fponame.setText(farmer.getFboname());
                Glide.with(FarmerRegistration.this).load(imageUri)
                        .centerCrop()
                        .dontAnimate()
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.profile)
                        .into(image);
            } catch (Exception e) {
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
                        || irrigated.getText().toString().length() <= 0
                        || totalarea.getText().toString().length() <= 0
                        || geotag.getText().toString().length() <= 0
                        || address.getText().toString().length() <= 0
                        || address1.getText().toString().length() <= 0
                        || pincode.getText().toString().length() <= 0
                        || email.getText().toString().length() <= 0
                        || ifscnumber.getText().toString().length() <= 0
                        || accountnumber.getText().toString().length() <= 0
                        || shgname.getText().toString().length() <= 0
                        || fponame.getText().toString().length() <= 0
                        ) {
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String farmeridnew;
                    Farmer farmerdata = new Farmer();
                    String username = "";
                    if (name.getText().toString().contains(fathername.getText().toString())) {
                        username = name.getText().toString();
                    } else {
                        username = name.getText().toString() + " " + fathername.getText().toString();
                    }
                    farmerdata.setData(sharedpreferences.getString(farmerid, ""), imageUri
                            , aadharnumber.getText().toString()
                            , username
                            , fathername.getText().toString()
                            , contact1.getText().toString()
                            , contact2.getText().toString()
                            , coconuttrees.getText().toString()
                            , coconutyield.getText().toString()
                            , irrigated.getText().toString()
                            , totalarea.getText().toString()
                            , geotag.getText().toString()
                            , address.getText().toString()
                            , address1.getText().toString()
                            , pincode.getText().toString()
                            , email.getText().toString()
                            , ifscnumber.getText().toString()
                            , accountnumber.getText().toString()
                            , shgname.getText().toString()
                            , fponame.getText().toString());
                    if (sharedpreferences.contains(update)) {
                        dbFarmer.updatedataByFarmerid(sharedpreferences.getString(farmerid, ""), new Gson().toJson(farmerdata));
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(update);
                        editor.commit();
                        Intent io = new Intent(FarmerRegistration.this, ProfileActivity.class);
                        startActivity(io);
                        finish();
                    } else {
                        if (dbFarmer.getCountByFarmerid(pincode.getText().toString() + "farmer_1") == 0) {
                            farmeridnew = pincode.getText().toString() + "farmer_1";
                        } else {
                            farmeridnew = pincode.getText().toString() + "farmer_" + String.valueOf(dbFarmer.getAllData().size() + 1);
                        }
                        farmerdata.setId(farmeridnew);
                        dbFarmer.addData(farmeridnew, new Gson().toJson(farmerdata));
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(farmerid, farmeridnew);
                        editor.commit();
                        Intent io = new Intent(FarmerRegistration.this, PasswordActivity.class);
                        startActivity(io);
                        finish();
                    }
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
                io.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                io.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(io);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QrScannerActivity.QR_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                JSONObject jsonObj = null;
                try {
                    XmlToJson xmlToJson = new XmlToJson.Builder(data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)).build();
                    jsonObj = xmlToJson.toJson();
                    JSONObject barcodedata = jsonObj.getJSONObject("PrintLetterBarcodeData");
                    Log.d("xxxxxxxxx", barcodedata.toString());
                    String addresstext = "";
                    if (!barcodedata.isNull("street")) {
                        address.setText(barcodedata.getString("street"));
                    } else if (!barcodedata.isNull("lm")) {
                        address.setText(barcodedata.getString("lm"));
                    }

                    if (!barcodedata.isNull("vtc")) {
                        addresstext = addresstext + barcodedata.getString("vtc");
                    }
                    if (!barcodedata.isNull("subdist")) {
                        addresstext = addresstext + barcodedata.getString("subdist");
                    }
                    if (!barcodedata.isNull("dist")) {
                        addresstext = addresstext + barcodedata.getString("dist");
                    }
                    if (!barcodedata.isNull("uid")) {
                        aadharnumber.setText(String.valueOf(barcodedata.getLong("uid")));
                    }
                    if (!barcodedata.isNull("name")) {
                        name.setText(barcodedata.getString("name"));
                    }
                    if (!barcodedata.isNull("gname")) {
                        fathername.setText(barcodedata.getString("gname"));
                    }

                    if (!barcodedata.isNull("pc")) {
                        pincode.setText(barcodedata.getString("pc"));
                    }
                    if (addresstext.toString().length() > 0) {
                        address1.setText(addresstext);
                    }
                } catch (Exception e) {
                    Log.e("xxxxxxxxx", e.toString());
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(
                        new Intent(FarmerRegistration.this, QrScannerActivity.class),
                        QrScannerActivity.QR_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
