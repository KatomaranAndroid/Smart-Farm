package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlotNewAdapter extends RecyclerView.Adapter<PlotNewAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Plot> moviesList;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView plotimg;
        private final CustomFontTextView plotname;
        CustomFontTextView plotarea;

        public MyViewHolder(View view) {
            super(view);
            plotimg = (CircleImageView) view.findViewById(R.id.profile_image);
            plotname = (CustomFontTextView) view.findViewById(R.id.plotname);
            plotarea = (CustomFontTextView) view.findViewById(R.id.areaofplot);

        }
    }


    public PlotNewAdapter(Context mainActivityUser, ArrayList<Plot> moviesList) {
        this.moviesList = moviesList;
        this.mainActivityUser = mainActivityUser;
    }

    public void notifyData(ArrayList<Plot> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.moviesList = myList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plot_horizontal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Plot plot = moviesList.get(position);
        holder.plotname.setText(plot.getPlotname());
        holder.plotarea.setText(plot.getPlotarea());
        Glide.with(mainActivityUser).load(plot.getPlotimage())
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.plotimg);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
