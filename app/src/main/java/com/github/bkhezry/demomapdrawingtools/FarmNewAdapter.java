package com.github.bkhezry.demomapdrawingtools;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.utils.FarmNew;

import java.util.ArrayList;

public class FarmNewAdapter extends RecyclerView.Adapter<FarmNewAdapter.MyViewHolder> {


    private MainActivityFarm mainActivityUser;
    private ArrayList<FarmNew> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView cropimg;
        private final CustomFontTextView cropname;
        private final CustomFontTextView aqty;
        private final CustomFontTextView aprice;
        private final CustomFontTextView bqty;
        private final CustomFontTextView bprice;
        private final CustomFontTextView cqty;
        private final CustomFontTextView cprice;
        private final CustomFontTextView agradetxt;
        private final CustomFontTextView bgradetxt;
        private final CustomFontTextView cgradetxt;
        private final CustomFontTextView qty;
        private final CustomFontTextView shortcount;
        private final CustomFontTextView mixedcount;
        private final CustomFontTextView dateofharvest;

        public MyViewHolder(View view) {
            super(view);
            cropimg = (ImageView) view.findViewById(R.id.cropimg);
            cropname = (CustomFontTextView) view.findViewById(R.id.cropname);
            aqty = (CustomFontTextView) view.findViewById(R.id.aqty);
            aprice = (CustomFontTextView) view.findViewById(R.id.aprice);
            bqty = (CustomFontTextView) view.findViewById(R.id.bqty);
            bprice = (CustomFontTextView) view.findViewById(R.id.bprice);
            cqty = (CustomFontTextView) view.findViewById(R.id.cqty);
            cprice = (CustomFontTextView) view.findViewById(R.id.cprice);
            qty = (CustomFontTextView) view.findViewById(R.id.qty);
            agradetxt = (CustomFontTextView) view.findViewById(R.id.agradetxt);
            bgradetxt = (CustomFontTextView) view.findViewById(R.id.bgradetxt);
            cgradetxt = (CustomFontTextView) view.findViewById(R.id.cgradetxt);
           shortcount = (CustomFontTextView) view.findViewById(R.id.shortnos);
            mixedcount = (CustomFontTextView) view.findViewById(R.id.mixednos);
            dateofharvest = (CustomFontTextView) view.findViewById(R.id.dateofharvest);

        }
    }


    public FarmNewAdapter(MainActivityFarm mainActivityUser, ArrayList<FarmNew> moviesList) {
        this.moviesList = moviesList;
        this.mainActivityUser = mainActivityUser;
    }

    public void notifyData(ArrayList<FarmNew> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.moviesList = myList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myfarm_listitemcopy, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FarmNew farm = moviesList.get(position);
        holder.cropname.setText(farm.getCropname());
        holder.aqty.setText(farm.getAqty());
        holder.bqty.setText(farm.getBqty());
        holder.cqty.setText(farm.getCqty());
        holder.aprice.setText("₹" + farm.getAprice());
        holder.bprice.setText("₹" + farm.getBprice());
        holder.cprice.setText("₹" + farm.getCprice());
        holder.shortcount.setText(farm.getShorttrees());
        holder.mixedcount.setText(farm.getMixedtrees());
        holder.agradetxt.setText("A (₹" + farm.getAcost() + ")");
        holder.bgradetxt.setText("B (₹" + farm.getBcost() + ")");
        holder.cgradetxt.setText("C (₹" + farm.getCcost() + ")");
        Glide.with(mainActivityUser).load(farm.getImg())
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.cropimg);
        holder.qty.setText(farm.getQty());
        holder.dateofharvest.setText(farm.getDate());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
