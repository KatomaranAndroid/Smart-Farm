package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.R;

import java.util.ArrayList;

/**
 * Created by takusemba on 2017/08/03.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private Context context;

    public HorizontalAdapter(Context context, ArrayList<String> titles) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    public HorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_horizontal, viewGroup, false);
        return new HorizontalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalAdapter.ViewHolder holder, int position) {
        String title = titles.get(position);
        Glide.with(context).load(title)
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.addicon)
                .into(holder.title);
    }


    public void notifyData(ArrayList<String> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.titles = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView title;

        ViewHolder(final View itemView) {
            super(itemView);
            this.title = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }
}