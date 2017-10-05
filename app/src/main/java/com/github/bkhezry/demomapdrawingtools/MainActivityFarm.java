package com.github.bkhezry.demomapdrawingtools;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.buy.MainActivityCattle;
import com.github.bkhezry.demomapdrawingtools.customdialogs.ContactSearchDialogCompat;
import com.github.bkhezry.demomapdrawingtools.customdialogs.models.ContactModel;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.dp.DbPro;
import com.github.bkhezry.demomapdrawingtools.utils.FarmNew;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;


public class MainActivityFarm extends AppCompatActivity {


    private ArrayList<FarmNew> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FarmNewAdapter mAdapter;
    private LinearLayout submitlin;
    private String imgurl;
    private String qtyttext;
    int aQty = 0;
    int bQty = 0;
    int cQty = 0;
    public static String noOfTrees = "";
    public static String typeCrop = "";
    public static String yiled = "";
    public static String value = "";

    DbPro dbpro;
    DbFarmer dbFarmer;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String farmerid = "farmeridKey";
    public static final String update = "updateKey";
    String farmerId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfarm);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(farmerid)) {
            farmerId = sharedpreferences.getString(farmerid, "").trim();
        }
        dbFarmer = new DbFarmer(this);
        dbpro = new DbPro(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'> My Crop </font>"));

        submitlin = (LinearLayout) findViewById(R.id.r_submitlin);
        TextView submittxt = (TextView) findViewById(R.id.r_submittxt);
        submitlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFarmDialog(false, "", -1);
            }
        });
        submittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFarmDialog(false, "", -1);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new FarmNewAdapter(MainActivityFarm.this, userList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareData();
    }

    private void prepareData() {
        userList = new ArrayList<>();
        List<ArrayList<String>> proList = dbpro.getAllData();
        for (int i = 0; i < proList.size(); i++) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(proList.get(i).get(1)).getAsJsonObject();
                FarmNew farmNew = new Gson().fromJson(o, FarmNew.class);
                if (farmNew.getFarmeid().equals(farmerId)) {
                    userList.add(farmNew);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        mAdapter.notifyData(userList);
    }


    public void showFarmDialog(final boolean edit, final String name, final int position) {
        imgurl = "";
        qtyttext = "";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivityFarm.this);
        LayoutInflater inflater = MainActivityFarm.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.myfarm_dataentery, null);
        final CustomFontTextView subtxt = (CustomFontTextView) dialogView.findViewById(R.id.r_submittxt);
        final CustomFontTextView cropname = (CustomFontTextView) dialogView.findViewById(R.id.cropname);
        final CustomFontTextView date = (CustomFontTextView) dialogView.findViewById(R.id.dateofharvest);
        final EditText agradetxt = (EditText) dialogView.findViewById(R.id.agradetxt);
        final EditText bgradetxt = (EditText) dialogView.findViewById(R.id.bgradetxt);
        final EditText cgradetxt = (EditText) dialogView.findViewById(R.id.cgradetxt);
        final EditText aqty = (EditText) dialogView.findViewById(R.id.aqty);
        final EditText bqty = (EditText) dialogView.findViewById(R.id.bqty);
        final EditText cqty = (EditText) dialogView.findViewById(R.id.cqty);
        final EditText aprice = (EditText) dialogView.findViewById(R.id.aprice);
        final EditText bprice = (EditText) dialogView.findViewById(R.id.bprice);
        final EditText cprice = (EditText) dialogView.findViewById(R.id.cprice);
        final CustomFontTextView qty = (CustomFontTextView) dialogView.findViewById(R.id.qty);
        final EditText shortnos = (EditText) dialogView.findViewById(R.id.shortnos);
        final EditText mixednos = (EditText) dialogView.findViewById(R.id.mixednos);
        final LinearLayout root = (LinearLayout) dialogView.findViewById(R.id.root);
        final CircleImageView cropimg = (CircleImageView) dialogView.findViewById(R.id.image);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/" + "maven" + ".ttf");
        agradetxt.setTypeface(custom);
        bgradetxt.setTypeface(custom);
        cgradetxt.setTypeface(custom);
        aqty.setTypeface(custom);
        bqty.setTypeface(custom);
        cqty.setTypeface(custom);
        aprice.setTypeface(custom);
        bprice.setTypeface(custom);
        cprice.setTypeface(custom);
        shortnos.setTypeface(custom);
        mixednos.setTypeface(custom);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        final CustomFontTextView itemtittle = (CustomFontTextView) dialogView.findViewById(R.id.itemtittle);
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }

        });

        agradetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int lenAfter) {
                String c = String.valueOf(charSequence);
                if ((i == 0 && lenAfter == 1) || (!c.equals(" ") && i > 0)) {
                    if (charSequence.toString().length() > 0 && aqty.getText().toString().length() > 0) {
                        agradetxt.setText(charSequence.toString());
                        aprice.setText(
                                String.valueOf(Integer.parseInt(agradetxt.getText().toString())
                                        * Integer.parseInt(aqty.getText().toString().replace(",", ""))));
                    }
                } else if (i == 0 && lenAfter == 0) {
                    aprice.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        bgradetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int lenAfter) {
                String c = String.valueOf(charSequence);
                if ((i == 0 && lenAfter == 1) || (!c.equals(" ") && i > 0)) {
                    if (charSequence.toString().length() > 0 && bqty.getText().toString().length() > 0) {
                        bgradetxt.setText(charSequence.toString());
                        bprice.setText(
                                String.valueOf(Integer.parseInt(bgradetxt.getText().toString())
                                        * Integer.parseInt(bqty.getText().toString().replace(",", ""))));
                    }
                } else if (i == 0 && lenAfter == 0) {
                    bprice.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        cgradetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int lenAfter) {
                String c = String.valueOf(charSequence);
                if ((i == 0 && lenAfter == 1) || (!c.equals(" ") && i > 0)) {
                    if (charSequence.toString().length() > 0 && cqty.getText().toString().length() > 0) {
                        cgradetxt.setText(charSequence.toString());
                        cprice.setText(
                                String.valueOf(Integer.parseInt(cgradetxt.getText().toString())
                                        * Integer.parseInt(cqty.getText().toString().replace(",", ""))));
                    }
                } else if (i == 0 && lenAfter == 0) {
                    cprice.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        aqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int lenAfter) {
                String c = String.valueOf(charSequence);
                if ((i == 0 && lenAfter == 1) || (!c.equals(" ") && i > 0)) {
                    if (charSequence.toString().length() > 0 && agradetxt.getText().toString().length() > 0) {
                        aprice.setText(
                                String.valueOf(Integer.parseInt(agradetxt.getText().toString())
                                        * Integer.parseInt(charSequence.toString().replace(",", ""))));
                    }
                    if (charSequence.toString().length() > 0 && bqty.getText().toString().length() > 0
                            && cqty.getText().toString().length() > 0) {
                        aQty = Integer.parseInt(aqty.getText().toString().replace(",", ""));
                        bQty = Integer.parseInt(bqty.getText().toString().replace(",", ""));
                        cQty = Integer.parseInt(cqty.getText().toString().replace(",", ""));
                        int aPrice = Integer.parseInt(aprice.getText().toString().replace(",", ""));
                        int bPrice = Integer.parseInt(bprice.getText().toString().replace(",", ""));
                        int cPrice = Integer.parseInt(cprice.getText().toString().replace(",", ""));
                        shortnos.setText(String.valueOf((aQty + bQty + cQty)));
                        mixednos.setText(String.valueOf((aPrice + bPrice + cPrice)));
                    }
                } else if (i == 0 && lenAfter == 0) {
                    aprice.setText("");
                    shortnos.setText("");
                    mixednos.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        bqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int lenAfter) {
                String c = String.valueOf(charSequence);
                if ((i == 0 && lenAfter == 1) || (!c.equals(" ") && i > 0)) {
                    if (charSequence.toString().length() > 0 && bgradetxt.getText().toString().length() > 0) {
                        bprice.setText(
                                String.valueOf(Integer.parseInt(bgradetxt.getText().toString())
                                        * Integer.parseInt(charSequence.toString().replace(",", ""))));
                    }
                    if (charSequence.toString().length() > 0 && aqty.getText().toString().length() > 0
                            && cqty.getText().toString().length() > 0) {
                        aQty = Integer.parseInt(aqty.getText().toString().replace(",", ""));
                        bQty = Integer.parseInt(bqty.getText().toString().replace(",", ""));
                        cQty = Integer.parseInt(cqty.getText().toString().replace(",", ""));
                        int aPrice = Integer.parseInt(aprice.getText().toString().replace(",", ""));
                        int bPrice = Integer.parseInt(bprice.getText().toString().replace(",", ""));
                        int cPrice = Integer.parseInt(cprice.getText().toString().replace(",", ""));
                        shortnos.setText(String.valueOf((aQty + bQty + cQty)));
                        mixednos.setText(String.valueOf((aPrice + bPrice + cPrice)));
                    }
                } else if (i == 0 && lenAfter == 0) {
                    bprice.setText("");
                    shortnos.setText("");
                    mixednos.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cqty.addTextChangedListener(new

                                            TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence charSequence, int i, int i1, int lenAfter) {
                                                    String c = String.valueOf(charSequence);
                                                    if ((i == 0 && lenAfter == 1) || (!c.equals(" ") && i > 0)) {
                                                        if (charSequence.toString().length() > 0 && cgradetxt.getText().toString().length() > 0) {
                                                            cprice.setText(
                                                                    String.valueOf(Integer.parseInt(cgradetxt.getText().toString())
                                                                            * Integer.parseInt(charSequence.toString().replace(",", ""))));
                                                        }
                                                        if (charSequence.toString().length() > 0 && aqty.getText().toString().length() > 0
                                                                && bqty.getText().toString().length() > 0) {
                                                            aQty = Integer.parseInt(aqty.getText().toString().replace(",", ""));
                                                            bQty = Integer.parseInt(bqty.getText().toString().replace(",", ""));
                                                            cQty = Integer.parseInt(cqty.getText().toString().replace(",", ""));
                                                            int aPrice = Integer.parseInt(aprice.getText().toString().replace(",", ""));
                                                            int bPrice = Integer.parseInt(bprice.getText().toString().replace(",", ""));
                                                            int cPrice = Integer.parseInt(cprice.getText().toString().replace(",", ""));
                                                            shortnos.setText(String.valueOf((aQty + bQty + cQty)));
                                                            mixednos.setText(String.valueOf((aPrice + bPrice + cPrice)));
                                                        }
                                                    } else if (i == 0 && lenAfter == 0) {
                                                        cprice.setText("");
                                                        shortnos.setText("");
                                                        mixednos.setText("");
                                                    }
                                                }

                                                @Override
                                                public void afterTextChanged(Editable editable) {

                                                }
                                            });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContactSearchDialogCompat<>(MainActivityFarm.this, "Search...",
                        "What are you looking for...?", null, createSampleContacts(),
                        new SearchResultListener<ContactModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   ContactModel item, int position) {
                                imgurl = item.getImageUrl();
                                Glide.with(MainActivityFarm.this).load(item.getImageUrl())
                                        .dontAnimate()
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.coconut)
                                        .into(cropimg);
                                cropname.setText(item.getTitle());
                                if (position == 0) {
                                    qty.setText("Qty (#)\n");
                                    qtyttext = "Qty (#)";
                                } else {
                                    qty.setText("Qty (Kg)\n");
                                    qtyttext = "Qty (Kg)";
                                }
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        cropimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContactSearchDialogCompat<>(MainActivityFarm.this, "Search...",
                        "What are you looking for...?", null, createSampleContacts(),
                        new SearchResultListener<ContactModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   ContactModel item, int position) {
                                imgurl = item.getImageUrl();
                                Glide.with(MainActivityFarm.this).load(item.getImageUrl())
                                        .dontAnimate()
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.coconut)
                                        .into(cropimg);
                                cropname.setText(item.getTitle());
                                if (position == 0) {
                                    qty.setText("Qty (#)\n");
                                    qtyttext = "Qty (#)";
                                } else {
                                    qty.setText("Qty (Kg)\n");
                                    qtyttext = "Qty (Kg)";
                                }
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        cropname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContactSearchDialogCompat<>(MainActivityFarm.this, "Search...",
                        "What are you looking for...?", null, createSampleContacts(),
                        new SearchResultListener<ContactModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   ContactModel item, int position) {
                                imgurl = item.getImageUrl();
                                Glide.with(MainActivityFarm.this).load(item.getImageUrl())
                                        .dontAnimate()
                                        .thumbnail(0.5f)
                                        .placeholder(R.drawable.coconut)
                                        .into(cropimg);
                                cropname.setText(item.getTitle());
                                if (position == 0) {
                                    qty.setText("Qty (#)\n");
                                    qtyttext = "Qty (#)";
                                } else {
                                    qty.setText("Qty (Kg)\n");
                                    qtyttext = "Qty (Kg)";
                                }
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        subtxt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (cropname.getText().toString().length() <= 0 ||
                        date.getText().toString().length() <= 0 ||
                        aqty.getText().toString().length() <= 0 ||
                        bqty.getText().toString().length() <= 0 ||
                        cqty.getText().toString().length() <= 0 ||
                        aprice.getText().toString().length() <= 0 ||
                        bprice.getText().toString().length() <= 0 ||
                        cprice.getText().toString().length() <= 0 ||
                        agradetxt.getText().toString().length() <= 0 ||
                        bgradetxt.getText().toString().length() <= 0 ||
                        cgradetxt.getText().toString().length() <= 0 ||
                        shortnos.getText().toString().length() <= 0 ||
                        mixednos.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String proid = "";
                    try {
                       JSONObject jsonObject = new JSONObject(dbFarmer.getDataByFarmerid(farmerId).get(1));
                        if (dbpro.getCountByFarmerid(jsonObject.get("pincode") + "pro_1") == 0) {
                            proid = jsonObject.get("pincode") + "pro_1";
                        } else {
                            proid = jsonObject.get("pincode") + "pro_" + String.valueOf(dbpro.getAllData().size() + 1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FarmNew farm = new FarmNew(farmerId, proid, qtyttext, cropname.getText().toString(),
                            aqty.getText().toString(),
                            aprice.getText().toString(),
                            bqty.getText().toString(),
                            bprice.getText().toString(),
                            cqty.getText().toString(),
                            cprice.getText().toString(),
                            agradetxt.getText().toString(),
                            bgradetxt.getText().toString(),
                            cgradetxt.getText().toString(),
                            "#" + shortnos.getText().toString(),
                            "₹" + mixednos.getText().toString(),
                            imgurl, date.getText().toString());

                    Log.e("xxxxxxxxxxxx", new Gson().toJson(farm));
                    dbpro.addData(proid, new Gson().toJson(farm));
                    userList.add(farm);
                    mAdapter.notifyData(userList);
                    b.cancel();
                }
            }
        });
        itemtittle.setText("Fill crop details");
        b.show();
    }

    private ArrayList<ContactModel> createSampleContacts() {
        ArrayList<ContactModel> items = new ArrayList<>();
        // Thanks to https://randomuser.me for the images
        items.add(new ContactModel("Tender coconut", "https://png.pngtree.com/element_origin_min_pic/16/11/17/93889fb25cbf2e65733c3117b6a4b842.jpg"));
        items.add(new ContactModel("Copra", "https://5.imimg.com/data5/OV/DO/MY-13505030/dry-coconut-khobra-500x500.jpg"));
        items.add(new ContactModel("Coconut", "http://www.shawacademy.com/blog/wp-content/uploads/2015/07/coconuts-with-leaves.jpg"));
        return items;
    }


    public static String getValue() {
        return typeCrop + "," + noOfTrees + "," + yiled + "," + value;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_farmer_black, menu);
//        MenuItem item = menu.findItem(R.id.bank);
//        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent io = new Intent();
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.buy:
                Intent ioo = new Intent(MainActivityFarm.this, MainActivityCattle.class);
                startActivityForResult(ioo, 1);
                return true;
            case R.id.account:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(update, "true");
                editor.commit();
                Intent account = new Intent(MainActivityFarm.this, FarmerRegistration.class);
                startActivity(account);
                finish();
                return true;
            case R.id.plot:
                FarmNew farmNew = userList.get(0);
                if (farmNew != null) {
                    typeCrop = farmNew.getCropname().replace(",", "");
                    noOfTrees = "5";
                    yiled = farmNew.getShorttrees().replace("#", "").replace(",", "");
                    value = farmNew.getMixedtrees().replace("₹", "").replace(",", "");
                    Intent intent = new Intent(MainActivityFarm.this, MapsFragActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Atleast one product", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            prepareData();
        }
    }
}