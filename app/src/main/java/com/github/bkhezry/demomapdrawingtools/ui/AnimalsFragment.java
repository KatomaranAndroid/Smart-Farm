package com.github.bkhezry.demomapdrawingtools.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.CustomFontEditText;
import com.github.bkhezry.demomapdrawingtools.CustomFontTextView;
import com.github.bkhezry.demomapdrawingtools.Plot;
import com.github.bkhezry.demomapdrawingtools.PlotAdapter;
import com.github.bkhezry.demomapdrawingtools.R;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AnimalsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private TextView tabText;


    public AnimalsFragment() {
        // Required empty public constructor
    }

    public static AnimalsFragment newInstance(String param1) {
        AnimalsFragment fragment = new AnimalsFragment();
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

    ArrayList<Plot> popList = new ArrayList<>();
    PlotAdapter plotAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.activity_anipop, container, false);
        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        plotAdapter = new PlotAdapter(getActivity(), popList);
        recyclerView.setAdapter(plotAdapter);

        CustomFontTextView submit = (CustomFontTextView) convertView.findViewById(R.id.r_submittxt);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnimalsDialog();
            }
        });
        return convertView;
    }


    public void showAnimalsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_animalspop, null);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        final CustomFontEditText cattleType = (CustomFontEditText) dialogView.findViewById(R.id.cattleType);
        final CustomFontEditText countText = (CustomFontEditText) dialogView.findViewById(R.id.countText);
        final CustomFontTextView submit = (CustomFontTextView) dialogView.findViewById(R.id.r_submittxt);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cattleType.getText().toString().length() > 0 && countText.getText().toString().length() > 0) {
                    popList.add(new Plot("", cattleType.getText().toString(), "img", countText.getText().toString(), null));
                    plotAdapter.notifyData(popList);
                    b.cancel();
                } else {
                    Toast.makeText(getActivity(), "Enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.show();
    }

}
