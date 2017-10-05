package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.github.bkhezry.demomapdrawingtools.CustomFontTextView;
import com.github.bkhezry.demomapdrawingtools.MapsFragActivity;
import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.RecyclerTouchListener;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PlotActivity extends Fragment implements IPickResult {

    ArrayList<String> myImage = new ArrayList<>();
    ArrayList<String> myResult = new ArrayList<>();
    ArrayList<String> irriSystem = new ArrayList<>();
    ArrayList<String> irriSource = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private TextView tabText;
    private String result = "";
    private CustomFontTextView landstatustext;


    public PlotActivity() {
        // Required empty public constructor
    }

    public static PlotActivity newInstance(String param1) {
        PlotActivity fragment = new PlotActivity();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.activity_plot_details, container, false);
        CustomFontTextView plotsize = (CustomFontTextView) convertView.findViewById(R.id.plotsize);
        plotsize.setText(MapsFragActivity.dataArea);
        LinearLayout landownrel = (LinearLayout) convertView.findViewById(R.id.landownlin);
        final CustomFontTextView landowntext = (CustomFontTextView) convertView.findViewById(R.id.landownership);
        landownrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle("Select land Ownership:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Rented");
                arrayAdapter.add("Owned");
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Land Ownership", landowntext);
                        } else {
                            landowntext.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });

        LinearLayout landstatusrel = (LinearLayout) convertView.findViewById(R.id.landstatuslin);
        landstatustext = (CustomFontTextView) convertView.findViewById(R.id.landstatus);

        landstatusrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle("Select land Status:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Cultivated");
                arrayAdapter.add("Non Cultivated");
                arrayAdapter.add("Plantation");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Land Status", landstatustext);
                        } else {
                            landstatustext.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });
        LinearLayout soillin = (LinearLayout) convertView.findViewById(R.id.soillin);
        soillin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoilDialog();
            }
        });
        LinearLayout waterlin = (LinearLayout) convertView.findViewById(R.id.waterlin);
        waterlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLandDialog();
            }
        });

        LinearLayout cropsLin = (LinearLayout) convertView.findViewById(R.id.croplin);
        cropsLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCropsDialog();
            }
        });

        return convertView;
    }


    public void showCropsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.crops_popup, null);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        final LinearLayout yearlin = (LinearLayout) dialogView.findViewById(R.id.pastyear);
        final LinearLayout otherlin = (LinearLayout) dialogView.findViewById(R.id.session);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.cancel();
            }
        });
        if (landstatustext.getText().toString().length() > 0) {
            if (landstatustext.getText().toString().equals("Cultivated")) {
                yearlin.setVisibility(View.GONE);
                otherlin.setVisibility(View.VISIBLE);
            } else {
                yearlin.setVisibility(View.VISIBLE);
                otherlin.setVisibility(View.GONE);
            }
        }
        b.show();
    }

    public void showSoilDialog() {
        myImage = new ArrayList<>();
        myImage.add("http://www.pixempire.com/images/preview/add-symbol-icon.jpg");
        myResult = new ArrayList<>();
        myResult.add("Click here to Add results");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_soilentry_details, null);
        LinearLayout soiltestdatelin = (LinearLayout) dialogView.findViewById(R.id.soiltestresultlin);
        final CustomFontTextView soiltestdate = (CustomFontTextView) dialogView.findViewById(R.id.soiltestresult);
        final VerticalAdapter secondAdapter = new VerticalAdapter(getApplicationContext(), myResult);
        final MultiSnapRecyclerView secondRecyclerView = (MultiSnapRecyclerView) dialogView.findViewById(R.id.first_recycler_view1);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.cancel();
            }
        });
        final HorizontalAdapter firstAdapter = new HorizontalAdapter(getActivity(), myImage);
        final MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) dialogView.findViewById(R.id.first_recycler_view);
        final LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(firstAdapter);
        firstRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), firstRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    PickSetup setup = new PickSetup();
                    PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult pickResult) {
                            myImage.add(pickResult.getUri().toString());
                            firstAdapter.notifyData(myImage);
                        }
                    })
                            .show(getActivity());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        soiltestdatelin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        soiltestdate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear)
                                + "/" + String.valueOf(year));
                    }
                });
                cdp.show(getActivity().getSupportFragmentManager(), "Material Calendar Example");
            }
        });

        final LinearLayoutManager secondManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(secondAdapter);
        secondRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), secondRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                    builderSingle.setTitle("Select Results:-");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                    arrayAdapter.add("pH (15 unit)      Slight acidic");
                    arrayAdapter.add("EC (0.14 unit)    General");
                    arrayAdapter.add("OC (0.69 unit)    Slightly sufficient");
                    arrayAdapter.add("N  (2911.08 unit) Medium");
                    arrayAdapter.add("P  (94.74 unit)   Highly sufficient");
                    arrayAdapter.add("K  (199.38 unit)  Medium");
                    arrayAdapter.add("Zn (2.04 unit)    Medium");
                    arrayAdapter.add("S  (3.08 unit)    Medium");
                    arrayAdapter.add("B  (0.21 unit)    Medium");
                    arrayAdapter.add("Fe (27.89 unit)   Medium");
                    arrayAdapter.add("Cu (1.22 unit)    Medium");
                    arrayAdapter.add("Mn (37.60 unit)   Medium");
                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String strName = arrayAdapter.getItem(which);
                            myResult.add(strName);
                            secondAdapter.notifyData(myResult);
                        }
                    });
                    builderSingle.show();

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        b.show();
    }


    public void showLandDialog() {
        irriSource = new ArrayList<>();
        irriSystem = new ArrayList<>();
        irriSystem.add("http://www.pixempire.com/images/preview/add-symbol-icon.jpg");
        irriSource.add("http://www.pixempire.com/images/preview/add-symbol-icon.jpg");
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/maven.ttf");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.myland_item, null);
        final TextView irrigationLifttxt = (TextView) dialogView.findViewById(R.id.irrigationLifttxt);
        final LinearLayout irrigationLiftlin = (LinearLayout) dialogView.findViewById(R.id.irrigationLiftlin);
        final TextView plotiiricationtxt = (TextView) dialogView.findViewById(R.id.plotiiricationtxt);
        final LinearLayout plotiiricationlin = (LinearLayout) dialogView.findViewById(R.id.plotiiricationlin);
        final TextView isystemtxt = (TextView) dialogView.findViewById(R.id.isystemtxt);
        final LinearLayout isystemlin = (LinearLayout) dialogView.findViewById(R.id.isystemlin);
        final TextView istainabilitytxt = (TextView) dialogView.findViewById(R.id.istainabilitytxt);
        final LinearLayout istainabilitylin = (LinearLayout) dialogView.findViewById(R.id.istainabilitylin);

        final LinearLayout submitlin = (LinearLayout) dialogView.findViewById(R.id.r_submitlin);
        final TextView submittxt = (TextView) dialogView.findViewById(R.id.r_submittxt);
        irrigationLifttxt.setTypeface(custom_font);
        plotiiricationtxt.setTypeface(custom_font);
        isystemtxt.setTypeface(custom_font);
        istainabilitytxt.setTypeface(custom_font);
        dialogBuilder.setView(dialogView);
        submittxt.setTypeface(custom_font);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        final TextView itemtittle = (TextView) dialogView.findViewById(R.id.itemtittle);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        itemtittle.setTypeface(custom_font);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.cancel();
            }
        });
        final HorizontalAdapter firstAdapter = new HorizontalAdapter(getActivity(), irriSource);
        final MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) dialogView.findViewById(R.id.first_recycler_view);
        final LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(firstAdapter);
        firstRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), firstRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    PickSetup setup = new PickSetup();
                    PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult pickResult) {
                            irriSource.add(pickResult.getUri().toString());
                            firstAdapter.notifyData(irriSource);
                        }
                    })
                            .show(getActivity());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        final HorizontalAdapter sAdapter = new HorizontalAdapter(getActivity(), irriSystem);
        final MultiSnapRecyclerView sRecyclerView = (MultiSnapRecyclerView) dialogView.findViewById(R.id.first_recycler_view1);
        final LinearLayoutManager sManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        sRecyclerView.setLayoutManager(sManager);
        sRecyclerView.setAdapter(sAdapter);
        sRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), sRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position == 0) {
                    PickSetup setup = new PickSetup();
                    PickImageDialog.build(setup).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult pickResult) {
                            irriSystem.add(pickResult.getUri().toString());
                            sAdapter.notifyData(irriSystem);
                        }
                    })
                            .show(getActivity());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        irrigationLifttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Irrigation Lift:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Diesel (HP)");
                arrayAdapter.add("Kerosene (HP)");
                arrayAdapter.add("Electricity (HP)");
                arrayAdapter.add(" Solar (HP/ KW)");
                arrayAdapter.add("Irrigation");
                arrayAdapter.add("Others");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Irrigation Lift", irrigationLifttxt);
                        } else {
                            irrigationLifttxt.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });
        irrigationLifttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Irrigation Lift:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Diesel (HP)");
                arrayAdapter.add("Kerosene (HP)");
                arrayAdapter.add("Electricity (HP)");
                arrayAdapter.add(" Solar (HP/ KW)");
                arrayAdapter.add("Irrigation");
                arrayAdapter.add("Others");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Irrigation Lift", irrigationLifttxt);
                        } else {
                            irrigationLifttxt.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });

        isystemtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Irrigation system:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Flood");
                arrayAdapter.add("Drip");
                arrayAdapter.add("Sprinkler");
                arrayAdapter.add("Rain gun");
                arrayAdapter.add("Combination of few above");
                arrayAdapter.add("Others");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Irrigation system", isystemtxt);
                        } else {
                            isystemtxt.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });

        istainabilitytxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Irrigation Sustainability:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Year round well irrigated");
                arrayAdapter.add("Rabi and Kharif");
                arrayAdapter.add("Rabi & Zaid");
                arrayAdapter.add("Kharif only");
                arrayAdapter.add("Rabi only");
                arrayAdapter.add("Zaid only");
                arrayAdapter.add("Others");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Irrigation Sustainability", istainabilitytxt);
                        } else {
                            istainabilitytxt.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });
        plotiiricationtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Plot irrigation:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
                arrayAdapter.add("Rain fed");
                arrayAdapter.add("Bore well");
                arrayAdapter.add("Well");
                arrayAdapter.add("Pond");
                arrayAdapter.add("Lake");
                arrayAdapter.add("River");
                arrayAdapter.add("Canal");
                arrayAdapter.add("A combination");
                arrayAdapter.add("Others");

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String strName = arrayAdapter.getItem(which);
                        if (strName.equalsIgnoreCase("Others")) {
                            othersModule("Plot irrigation", plotiiricationtxt);
                        } else {
                            plotiiricationtxt.setText(strName);
                        }
                    }
                });
                builderSingle.show();
            }
        });


        b.show();
    }


    public String othersModule(String tittle, final TextView view) {
        result = "";
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_activity, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setTitle(tittle);
        alertDialogBuilder.setView(dialogView);
        final EditText userInput = (EditText) dialogView
                .findViewById(R.id.et_input);
        userInput.setHint("Enter " + tittle);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,

                                                int id) {
                                if (userInput.getText().toString().length() > 0) {
                                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    result = userInput.getText().toString();
                                    if (result != null) {
                                        view.setText(result.toString());
                                    }
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if (userInput.getText().toString().length() > 0) {
                                    dialog.cancel();
                                }
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return result;
    }


    @Override
    public void onPickResult(PickResult pickResult) {

    }


}

