package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.services.ServicesActivity;

/**
 * Created by vidhushi.g on 16/9/17.
 */

public class PlotServicesActivity extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private TextView tabText;


    public PlotServicesActivity() {
        // Required empty public constructor
    }

    public static PlotServicesActivity newInstance(String param1) {
        PlotServicesActivity fragment = new PlotServicesActivity();
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


    private ViewGroup content;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String trig = "trigKey";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.activity_services, container, false);

        LinearLayout manpower=(LinearLayout)convertView.findViewById(R.id.manpowerlin);
        LinearLayout cattleclin=(LinearLayout)convertView.findViewById(R.id.cattleclin);
        LinearLayout machinelin=(LinearLayout)convertView.findViewById(R.id.machinelin);


        machinelin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getActivity().getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(trig, 1);
                editor.commit();
                Intent io = new Intent(getActivity(), ServicesActivity.class);
                startActivity(io);
            }
        });

        manpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getActivity().getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(trig, 2);
                editor.commit();
                Intent io = new Intent(getActivity(), ServicesActivity.class);
                startActivity(io);
            }
        });

        cattleclin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getActivity().getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(trig, 4);
                editor.commit();
                Intent io = new Intent(getActivity(), ServicesActivity.class);
                startActivity(io);
            }
        });
        return convertView;
    }

}
