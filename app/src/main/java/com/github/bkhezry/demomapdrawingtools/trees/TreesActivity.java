package com.github.bkhezry.demomapdrawingtools.trees;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.CustomFontTextView;
import com.github.bkhezry.demomapdrawingtools.MainActivityFarm;
import com.github.bkhezry.demomapdrawingtools.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TreesActivity extends AppCompatActivity {


    List<String> treesList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpop);

        String[] response = MainActivityFarm.getValue().split(",");
        getSupportActionBar().setTitle(response[0]);
        setPopList(Integer.parseInt(response[1]), response[2], response[3]);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new TreesAdapter(treesList, this));

    }


    public void setPopList(int notrees, String yield, String value) {
        treesList = new ArrayList<>();
        for (int i = 0; i < notrees; i++) {
            treesList.add("Tree " + String.valueOf(i) + ", Yield - " + yield + ": Value - " + value);
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


}
