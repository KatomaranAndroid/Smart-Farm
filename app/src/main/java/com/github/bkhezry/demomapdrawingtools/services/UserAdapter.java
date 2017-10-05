package com.github.bkhezry.demomapdrawingtools.services;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.R;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private ServicesActivity servicesActivityUser;
    private List<User> moviesList;
    int trigger;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private  RelativeLayout useritem;
        public TextView usertxt;
        public ImageView usericon;


        public MyViewHolder(View view) {
            super(view);
            usertxt = (TextView) view.findViewById(R.id.tittle);
            usericon = (ImageView) view.findViewById(R.id.tittleimg);
            useritem = (RelativeLayout) view.findViewById(R.id.relitem);
        }
    }


    public UserAdapter(ServicesActivity servicesActivityUser, List<User> moviesList, int trigger) {
        this.moviesList = moviesList;
        this.servicesActivityUser = servicesActivityUser;
        this.trigger=trigger;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_cardcattle, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Typeface custom_font = Typeface.createFromAsset(servicesActivityUser.getAssets(), "fonts/maven.ttf");
        final User user = moviesList.get(position);
        holder.usertxt.setText(user.getTitle());
        holder.usertxt.setTypeface(custom_font);

        Glide.with(servicesActivityUser).load(user.getImg())
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.usericon);

        holder.useritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
