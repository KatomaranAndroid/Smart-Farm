package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.bkhezry.demomapdrawingtools.R;

import java.util.ArrayList;

/**
 * Created by takusemba on 2017/08/03.
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private Context context;

    public VerticalAdapter(Context context,
                           ArrayList<String> titles) {
        this.titles = titles;
        this.context = context;

    }

    @Override
    public VerticalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vertical, viewGroup, false);
        return new VerticalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalAdapter.ViewHolder holder, int position) {
        String title = titles.get(position);
        holder.title.setText(title);

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
        private TextView title;

        ViewHolder(final View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}