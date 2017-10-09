package com.github.bkhezry.demomapdrawingtools.trees;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.CustomFontEditText;
import com.github.bkhezry.demomapdrawingtools.CustomFontTextView;
import com.github.bkhezry.demomapdrawingtools.MainActivityFarm;
import com.github.bkhezry.demomapdrawingtools.MapsFragActivity;
import com.github.bkhezry.demomapdrawingtools.ProfileActivity;
import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.RecyclerTouchListener;
import com.github.bkhezry.demomapdrawingtools.buy.MainActivityCattle;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.utils.Farmer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TreesActivity extends AppCompatActivity {

    DbFarmer dbFarmer;
    List<String> treesList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    String farmerId = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpop);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(farmerid)) {
            farmerId = sharedpreferences.getString(farmerid, "").trim();
        }
        dbFarmer = new DbFarmer(this);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(dbFarmer.getDataByFarmerid(farmerId).get(1)).getAsJsonObject();
        final Farmer farmer = new Gson().fromJson(o, Farmer.class);
        getSupportActionBar().setTitle(farmer.getName());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_local_florist_white_24dp);
        setPopList(Integer.parseInt(farmer.getCoconuttrees()), farmer.getYield(), "Unknown");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new TreesAdapter(treesList, this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(TreesActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String pincode = farmer.getGeotag().toString().substring(farmer.getGeotag().toString().length() - 6, farmer.getGeotag().toString().length());
                showTreeDialog(treesList.get(position).split(",")[0], pincode + treesList.get(position).split(",")[0]);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public void setPopList(int notrees, String yield, String value) {
        treesList = new ArrayList<>();
        for (int i = 0; i < notrees; i++) {
            treesList.add("Tree " + String.valueOf(i + 1) + ", Yield - " + yield + ": Value - " + value);
        }
    }

    private class TreesAdapter extends RecyclerView.Adapter<TreesAdapter.ViewHolder> {
        private final List<String> treesList;
        private final Context context;

        public TreesAdapter(List<String> treesList, Context context) {
            this.treesList = treesList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.crop_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return treesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView image;
            private CustomFontTextView tittle;
            private CustomFontTextView subtittle;
            private int position;

            public ViewHolder(View itemView) {
                super(itemView);
                tittle = (CustomFontTextView) itemView.findViewById(R.id.plotname);
                subtittle = (CustomFontTextView) itemView.findViewById(R.id.areaofplot);
                image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            }

            public void bind(int position) {
                this.position = position;
                String[] result = treesList.get(this.position).split(",");
                tittle.setText(result[0]);
                subtittle.setText(result[1]);
                Glide.with(TreesActivity.this).load("https://orig15.deviantart.net/84e0/f/2013/121/d/b/palmera_psd_by_gianferdinand-d63q13d.jpg")
                        .dontAnimate()
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.image_placeholder)
                        .into(image);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_farmer_white, menu);
        MenuItem item = menu.findItem(R.id.tree);
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
            case R.id.market:
                Intent market = new Intent(TreesActivity.this, MainActivityCattle.class);
                startActivityForResult(market, 1);
                return true;
            case R.id.account:
                Intent account = new Intent(TreesActivity.this, ProfileActivity.class);
                startActivity(account);
                return true;
            case R.id.mykart:
                Intent mykart = new Intent(TreesActivity.this, MainActivityFarm.class);
                startActivity(mykart);
                return true;
            case R.id.plot:
                Intent tree = new Intent(TreesActivity.this, MapsFragActivity.class);
                startActivity(tree);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTreeDialog(String name, String id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TreesActivity.this);
        LayoutInflater inflater = TreesActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.trees_popup, null);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        final CustomFontEditText treeid = (CustomFontEditText) dialogView.findViewById(R.id.treeid);
        final CustomFontTextView submit = (CustomFontTextView) dialogView.findViewById(R.id.r_submittxt);
        final CustomFontTextView itemtittle = (CustomFontTextView) dialogView.findViewById(R.id.itemtittle);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.cancel();
            }
        });
        treeid.setText(id);
        itemtittle.setText(name);
        b.show();
    }
}
