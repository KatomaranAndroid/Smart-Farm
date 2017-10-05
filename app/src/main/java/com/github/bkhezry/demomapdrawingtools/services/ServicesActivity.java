package com.github.bkhezry.demomapdrawingtools.services;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.R;

import java.util.ArrayList;
import java.util.List;



public class ServicesActivity extends AppCompatActivity {


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;


    private UserAdapter mAdapter;
    private List<User> userList = new ArrayList<>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String trig = "trigKey";
    int trigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menucattle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(trig)) {
            trigger = sharedpreferences.getInt(trig,1);
        }
        if(trigger==1) {
            toolbar.setTitle("Select Vehicle");
        } if(trigger==2) {
            toolbar.setTitle("Select Labour");
        }if(trigger==3) {
            toolbar.setTitle("Select Food");
        }if(trigger==4) {
            toolbar.setTitle("Select Cattle");
        }
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(ServicesActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ServicesActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ServicesActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ServicesActivity.this, permissionsRequired[1])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ServicesActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ServicesActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(ServicesActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(ServicesActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new UserAdapter(ServicesActivity.this, userList,trigger);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(mAdapter);
        if(trigger==1){
            prepareVehicleData();
        }if(trigger==2){
            prepareLabourData();
        }if(trigger==3){
            prepareFoodData();
        }if(trigger==4){
            prepareCattleData();
        }


    }

    private void prepareCattleData() {

        User user = new User("Cow", R.drawable.cow);
        userList.add(user);

        user = new User("Horse", R.drawable.horse);
        userList.add(user);

        user = new User("Buffalo", R.drawable.buffalo);
        userList.add(user);

        user = new User("Donkey", R.drawable.donkey);
        userList.add(user);

        user = new User("Camel", R.drawable.camel);
        userList.add(user);

        user = new User("Sheep", R.drawable.sheep);
        userList.add(user);

        user = new User("Goat", R.drawable.goat);
        userList.add(user);
        user = new User("Pigs", R.drawable.pigs);
        userList.add(user);
        user = new User("Hen", R.drawable.hen);
        userList.add(user);
        user = new User("Duck", R.drawable.duck);
        userList.add(user);
        user = new User("Mule", R.drawable.mule);
        userList.add(user);
        mAdapter.notifyDataSetChanged();
    }

    private void prepareVehicleData() {
        User user = new User("Tracter plough", R.drawable.trackerplough);
        userList.add(user);

        user = new User("Rotavator", R.drawable.rotavatoricon);
        userList.add(user);

        user = new User("Thresher", R.drawable.threshericon);
        userList.add(user);

        user = new User("Paddy transplanter", R.drawable.paddytransplantericon);
        userList.add(user);

        user = new User("Multi seed drill", R.drawable.multiseeddrilicon);
        userList.add(user);

        user = new User("Small truck", R.drawable.smalltruck);
        userList.add(user);

        user = new User("Medium truck", R.drawable.mediumtruck);
        userList.add(user);

        user = new User("Big truck", R.drawable.bigtruck);
        userList.add(user);

        user = new User("Large truck", R.drawable.largetruck);
        userList.add(user);

        user = new User("Very Large truck", R.drawable.verylargeicon);
        userList.add(user);

        mAdapter.notifyDataSetChanged();
    }

    private void prepareLabourData() {
        User user = new User("Farm labourer", R.drawable.farmlabour);
        userList.add(user);

        user = new User("Mason", R.drawable.mason);
        userList.add(user);

        user = new User("Carpenter", R.drawable.carpenter);
        userList.add(user);

        user = new User("Mechanic", R.drawable.mechanic);
        userList.add(user);

        user = new User("Driver", R.drawable.driver);
        userList.add(user);

        user = new User("House maid", R.drawable.housemaid);
        userList.add(user);

        user = new User("Gardener", R.drawable.gardener);
        userList.add(user);

        user = new User("Cook", R.drawable.cook);
        userList.add(user);

        user = new User("JCB driver", R.drawable.driver);
        userList.add(user);

        user = new User("Rig operator", R.drawable.rigoperator);
        userList.add(user);

        user = new User("Tailor", R.drawable.tailor);
        userList.add(user);

        user = new User("Store keeper", R.drawable.storekeeper);
        userList.add(user);

        user = new User("Watch man", R.drawable.watchman);
        userList.add(user);

        user = new User("Machine operator", R.drawable.machineoperator);
        userList.add(user);

        user = new User("Black smith", R.drawable.blacksmith);
        userList.add(user);

        user = new User("Quarry worker", R.drawable.rigoperator);
        userList.add(user);

        mAdapter.notifyDataSetChanged();
    }

    private void prepareFoodData() {

        User user = new User("Paddy", R.drawable.paddy);
        userList.add(user);

        user = new User("Wheat", R.drawable.wheat);
        userList.add(user);

        user = new User("Coconut", R.drawable.coconut);
        userList.add(user);

        user = new User("Tomato", R.drawable.tomato);
        userList.add(user);

        user = new User("Onion", R.drawable.onion);
        userList.add(user);

        user = new User("Potato", R.drawable.potato);
        userList.add(user);

        user = new User("Garlic", R.drawable.garlic);
        userList.add(user);

        user = new User("Corn", R.drawable.corn);
        userList.add(user);

        user = new User("Green chillies", R.drawable.greenchillies);
        userList.add(user);
        user = new User("Red dry chillies", R.drawable.reddrychillies);
        userList.add(user);
        user = new User("Carrot", R.drawable.carrot);
        userList.add(user);
        user = new User("Beans", R.drawable.beans);
        userList.add(user);
        user = new User("Cauliflower", R.drawable.cauliflower);
        userList.add(user);
        user = new User("Cabbage", R.drawable.cabbage);
        userList.add(user);
        user = new User("Ladyfinger", R.drawable.ladyfinger);
        userList.add(user);
        user = new User("Spinach", R.drawable.spinach);
        userList.add(user);
        user = new User("Bittergourd", R.drawable.bittergourd);
        userList.add(user);
        user = new User("Ginger", R.drawable.ginger);
        userList.add(user);
        user = new User("Corriander", R.drawable.corriander);
        userList.add(user);
        user = new User("Cumin", R.drawable.cumin);
        userList.add(user);
        user = new User("Turmeric", R.drawable.turmeric);
        userList.add(user);
        user = new User("Fenugreek", R.drawable.fenugreek);
        userList.add(user);
        user = new User("Black pepper", R.drawable.blackpepper);
        userList.add(user);
        user = new User("Corriander leaves", R.drawable.corrianderleaves);
        userList.add(user);
        user = new User("Curry leaves", R.drawable.curryleaves);
        userList.add(user);
        user = new User("Tur dal", R.drawable.turdal);
        userList.add(user);
        user = new User("Green gram", R.drawable.greengram);
        userList.add(user);
        user = new User("Black gram", R.drawable.blackgram);
        userList.add(user);
        user = new User("Brown channa", R.drawable.brownchanna);
        userList.add(user);
        user = new User("White channa", R.drawable.whitechanna);
        userList.add(user);
        user = new User("Ragi", R.drawable.ragi);
        userList.add(user);
        user = new User("Pearl millet", R.drawable.pearlmillet);
        userList.add(user);
        user = new User("Groundnut", R.drawable.groundnut);
        userList.add(user);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(ServicesActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(ServicesActivity.this, permissionsRequired[1])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServicesActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ServicesActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(ServicesActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        setupRecyclerView();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(ServicesActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

}
