package com.github.bkhezry.demomapdrawingtools.snab;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.bkhezry.demomapdrawingtools.R;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<App> mApps;
    private boolean mHorizontal;
    private boolean mPager;

    public Adapter(boolean horizontal, boolean pager, List<App> apps) {
        mHorizontal = horizontal;
        mApps = apps;
        mPager = pager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mPager) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_pager, parent, false));
        } else {
            return mHorizontal ? new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter, parent, false)) :
                    new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_vertical, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        App app = mApps.get(position);
        String[] result = app.getName().split(":");
        if (result.length == 3) {
            holder.nameTextView.setText(result[0]);
            holder.ratingTextView.setText(result[1]);
            holder.pertcent.setPercent(Integer.parseInt(result[2]));
            holder.tvPercent.setText(result[2]);
            holder.imageView.setVisibility(View.GONE);
            holder.frame.setVisibility(View.VISIBLE);
        } else   if (result.length == 2){
            holder.nameTextView.setText(result[0]);
            holder.ratingTextView.setText(result[1]);
            holder.imageView.setImageResource(app.getDrawable());
            holder.imageView.setVisibility(View.VISIBLE);
            holder.frame.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public FrameLayout frame;
        public ImageView imageView;
        public ColorfulRingProgressView pertcent;
        public TextView tvPercent;
        public TextView nameTextView;
        public TextView ratingTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            frame = (FrameLayout) itemView.findViewById(R.id.frame);
            pertcent = (ColorfulRingProgressView) itemView.findViewById(R.id.crpv);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            tvPercent = (TextView) itemView.findViewById(R.id.tvPercent);
            ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);
        }

        @Override
        public void onClick(View v) {
            Log.d("App", mApps.get(getAdapterPosition()).getName());
        }
    }

}

