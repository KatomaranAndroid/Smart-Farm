package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.utils.Farmer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vidhushi.g on 8/10/17.
 */

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView profileImg;
    private CustomFontTextView tittle;


    DbFarmer dbFarmer;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    public static final String update = "updateKey";
    public static final String otherfarmerid = "otherfarmeridKey";

    String farmerId = "";
    private MultiSnapRecyclerView mRecyclerView;
    private ProfileAdapter mRecyclerAdapter;
    private ArrayList<Plot> agentList = new ArrayList<>();
    private ProfileAdapter mRecyclerAdapterService;
    private MultiSnapRecyclerView mRecyclerViewService;
    private ArrayList<Plot> serviceList = new ArrayList<>();
    String imagePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbFarmer = new DbFarmer(this);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(otherfarmerid)) {
            farmerId = sharedpreferences.getString(otherfarmerid, "").trim();
        } else {
            farmerId = sharedpreferences.getString(farmerid, "").trim();
        }

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(dbFarmer.getDataByFarmerid(farmerId).get(1)).getAsJsonObject();
        Farmer farmer = new Gson().fromJson(o, Farmer.class);


        profileImg = (CircleImageView) findViewById(R.id.logo);
        tittle = (CustomFontTextView) findViewById(R.id.name);
        TextView trees = (TextView) findViewById(R.id.trees);
        TextView area = (TextView) findViewById(R.id.area);
        TextView yield = (TextView) findViewById(R.id.yield);
        trees.setText(farmer.getCoconuttrees());
        area.setText(farmer.getArea());
        yield.setText(farmer.getYield());
        tittle.setText(farmer.getName());
       Glide.with(ProfileActivity.this).load(farmer.getImage())
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.image_placeholder)
                .into(profileImg);
        LinearLayout editLayout = (LinearLayout) findViewById(R.id.editlinear);
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(update, "true");
                editor.commit();
                Intent account = new Intent(ProfileActivity.this, FarmerRegistration.class);
                startActivity(account);
                finish();
            }
        });

        ImageView addAgent = (ImageView) findViewById(R.id.addagent);
        addAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopup(-1, 1, "Add Agent");
            }
        });

        mRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.agentlist);
        mRecyclerAdapter = new ProfileAdapter(ProfileActivity.this, agentList);
        final LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(firstManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        ImageView addService = (ImageView) findViewById(R.id.addservice);
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopup(-1, 2, "Add Service Provider");
            }
        });
        mRecyclerViewService = (MultiSnapRecyclerView) findViewById(R.id.servicelist);
        mRecyclerAdapterService = new ProfileAdapter(ProfileActivity.this, serviceList);
        final LinearLayoutManager secondManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewService.setLayoutManager(secondManager);
        mRecyclerViewService.setAdapter(mRecyclerAdapterService);
        mRecyclerViewService.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerViewService, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public void addPopup(final int position, final int type, String tittle) {

        imagePath = "";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_agent_popup, null);
        final CustomFontEditText nametxt = (CustomFontEditText) dialogView.findViewById(R.id.name);
        final CustomFontEditText contacttxt = (CustomFontEditText) dialogView.findViewById(R.id.contact);
        final CircleImageView image = (CircleImageView) dialogView.findViewById(R.id.image);
        final CustomFontEditText imagetxt = (CustomFontEditText) dialogView.findViewById(R.id.imagetxt);
        final CustomFontTextView submittxt = (CustomFontTextView) dialogView.findViewById(R.id.r_submittxt);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        final CustomFontTextView itemtittle = (CustomFontTextView) dialogView.findViewById(R.id.itemtittle);
        itemtittle.setText(tittle);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup();
                PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        Glide.with(ProfileActivity.this).load(pickResult.getUri())
                                .centerCrop()
                                .dontAnimate()
                                .thumbnail(0.5f)
                                .placeholder(R.drawable.profile)
                                .into(image);
                        imagePath = pickResult.getUri().toString();
                        imagetxt.setText(pickResult.getUri().toString());
                    }
                })
                        .show(ProfileActivity.this);
            }
        });
        submittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nametxt.getText().toString().length() <= 0 || contacttxt.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter all fileds", Toast.LENGTH_SHORT).show();
                } else {
                    Plot plot = new Plot("", nametxt.getText().toString(), imagePath, contacttxt.getText().toString(), null);
                    if (type == 1) {
                        agentList.add(plot);
                        mRecyclerAdapter.notifyData(agentList);
                    } else if (type == 2) {
                        serviceList.add(plot);
                        mRecyclerAdapterService.notifyData(serviceList);
                    }
                }
                b.cancel();
            }
        });
        b.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(otherfarmerid);
        editor.commit();
    }
}
