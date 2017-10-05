package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class POPActivity extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private TextView tabText;


    public POPActivity() {
        // Required empty public constructor
    }

    public static POPActivity newInstance(String param1) {
        POPActivity fragment = new POPActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }

    List<POP> popList = new ArrayList<>();

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mainpop);
//        setPopList();
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        recyclerView.setAdapter(new SimpleAdapter(recyclerView, popList,this));
//
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.activity_mainpop, container, false);
        setPopList();
        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new SimpleAdapter(recyclerView, popList, getApplicationContext()));
        return convertView;
    }

    public void setPopList() {

        POP pop = new POP("Botany", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_botany.html");
        popList.add(pop);
        pop = new POP("Varieties", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_varieties.html");
        popList.add(pop);
        pop = new POP("Soil and Planting Seasons", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_soilplanting.html");
        popList.add(pop);
        pop = new POP("Planting Material", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_plantingmaterial.html");
        popList.add(pop);
        pop = new POP("Nursery", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_nursery.html");
        popList.add(pop);
        pop = new POP("Spacing and Planting", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_spacing.html");
        popList.add(pop);
        pop = new POP("Water Management", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_watermgnt.html");
        popList.add(pop);
        pop = new POP("Drip Irrigation in Coconut", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_drip.html");
        popList.add(pop);
        pop = new POP("Drought Management and Soil Moisture Conservation", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_dmgnt_soil.html");
        popList.add(pop);
        pop = new POP("Manuring", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_manuring.html");
        popList.add(pop);
        pop = new POP("Weed Management", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_weedmgnt.html");
        popList.add(pop);
        pop = new POP("Intercropping", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_intercropping.html");
        popList.add(pop);
        pop = new POP("Harvesting", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_harvest.html");
        popList.add(pop);
        pop = new POP("Marketing", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_marketting.html");
        popList.add(pop);
        pop = new POP("Yield", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_yield.html");
        popList.add(pop);
        pop = new POP("Pest Management", "http://agritech.tnau.ac.in/crop_protection/crop_prot_crop_insect_oil_coconutmain.html");
        popList.add(pop);
        pop = new POP("Disease Management", "http://agritech.tnau.ac.in/crop_protection/crop_prot_crop%20diseases_plantation_coconut.html");
        popList.add(pop);
        pop = new POP("Special Problems in Coconut", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_splprob.html");
        popList.add(pop);
        pop = new POP("Physiological Disorders", "http://agritech.tnau.ac.in/horticulture/horti_pcrops_coconut_phydis.html");
        popList.add(pop);
        pop = new POP("Market Information", "http://agritech.tnau.ac.in/dmi/2014/index.html");
        popList.add(pop);
        pop = new POP("Integrated Pest Management Package for Coconut", "http://agritech.tnau.ac.in/horticulture/pdf/Coconut%20IPM.pdf");
        popList.add(pop);
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;
        private final List<POP> popList;
        private final Context context;
        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;

        public SimpleAdapter(RecyclerView recyclerView, List<POP> popList, Context context) {
            this.recyclerView = recyclerView;
            this.popList = popList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return popList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
            private ImageView openinbro;
            private TextView tittle;
            private ExpandableLayout expandableLayout;
            private RelativeLayout expandButton;
            private int position;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                expandableLayout.setOnExpansionUpdateListener(this);
                expandButton = (RelativeLayout) itemView.findViewById(R.id.relexpand);
                tittle = (TextView) itemView.findViewById(R.id.list_item_genre_name);
                openinbro = (ImageView) itemView.findViewById(R.id.openinbro);

                expandButton.setOnClickListener(this);
            }

            public void bind(int position) {
                this.position = position;
                final POP pop = popList.get(this.position);
                tittle.setText(pop.getName());
                openinbro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pop.getLink()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(browserIntent);
                    }
                });
                expandButton.setSelected(false);
                expandableLayout.collapse(false);
            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                }

                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    expandButton.setSelected(true);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }

            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout", "State: " + state);
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }
}
