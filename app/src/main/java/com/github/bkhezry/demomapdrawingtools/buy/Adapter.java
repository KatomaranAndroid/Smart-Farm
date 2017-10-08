package com.github.bkhezry.demomapdrawingtools.buy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andanhm.quantitypicker.QuantityPicker;
import com.bumptech.glide.Glide;
import com.github.bkhezry.demomapdrawingtools.CustomFontTextView;
import com.github.bkhezry.demomapdrawingtools.ProfileActivity;
import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.dp.DbFarmer;
import com.github.bkhezry.demomapdrawingtools.dp.DbPro;
import com.github.bkhezry.demomapdrawingtools.utils.FarmNew;
import com.github.bkhezry.demomapdrawingtools.utils.Farmer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<FarmNew> mApps;
    private boolean mHorizontal;
    private boolean mPager;
    private AppCompatActivity activity;
    private DbFarmer dbFarmer;
    private DbPro dbPro;
    private String pay;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String otherfarmerid = "otherfarmeridKey";


    public Adapter(AppCompatActivity activity, boolean horizontal, boolean pager, List<FarmNew> apps) {
        mHorizontal = horizontal;
        mApps = apps;
        mPager = pager;
        this.activity = activity;
        dbFarmer = new DbFarmer(activity);
        dbPro = new DbPro(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mPager) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_pager_cattle, parent, false));
        } else {
            return mHorizontal ? new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_cattle, parent, false)) :
                    new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_vertical_cattle, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FarmNew app = mApps.get(position);
        Glide.with(activity).load(app.getImg())
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageView);
        String farmerData = dbFarmer.getDataByFarmerid(app.getFarmeid()).get(1);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(farmerData).getAsJsonObject();
        final Farmer farmer = new Gson().fromJson(o, Farmer.class);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFunction(position);
            }
        });
        holder.farmername.setText(farmer.getName());
        holder.farmername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFunction(position);
            }
        });
        holder.farmerplace.setText(farmer.getAddress1());
        holder.farmerplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFunction(position);
            }
        });
        holder.posteddate.setText("Posted at " + app.getDate());
        holder.posteddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFunction(position);
            }
        });
        holder.bqty.setText("#" + app.getAqty());
        holder.aqty.setText("#" + app.getAqty());
        holder.allqty.setText("#" + app.getAllqty());
        holder.bqty.setText("#" + app.getBqty());
        holder.cqty.setText("#" + app.getCqty());
        holder.allcost.setText("₹" + app.getAllcost());
        holder.acost.setText("₹" + app.getAcost());
        holder.bcost.setText("₹" + app.getBcost());
        holder.ccost.setText("₹" + app.getCcost());
        holder.visitlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app.getName()));
//                activity.startActivity(browserIntent);
            }
        });

        holder.acard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getAqty().equals("0")) {
                    Toast.makeText(activity, "No more stocks", Toast.LENGTH_SHORT).show();
                } else {
                    OrderPopup(position, activity, "A");
                }
            }
        });
        holder.bcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getBqty().equals("0")) {
                    Toast.makeText(activity, "No more stocks", Toast.LENGTH_SHORT).show();
                } else {
                    OrderPopup(position, activity, "B");
                }
            }
        });
        holder.ccard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getCqty().equals("0")) {
                    Toast.makeText(activity, "No more stocks", Toast.LENGTH_SHORT).show();
                } else {
                    OrderPopup(position, activity, "C");
                }
            }
        });

        holder.allcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.getAllqty().equals("0")) {
                    Toast.makeText(activity, "No more stocks", Toast.LENGTH_SHORT).show();
                } else {
                    OrderPopup(position, activity, "All");
                }
            }
        });

        holder.whatsapplin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri mUri = Uri.parse("smsto:" + farmer.getContact1());
                    Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                    mIntent.setPackage("com.whatsapp");
                    mIntent.putExtra("sms_body", "hi");
                    mIntent.putExtra("chat", true);
                    activity.startActivity(mIntent);
                } catch (Exception e) {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.calllin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + farmer.getContact1()));
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                activity.startActivity(callIntent);
            }
        });

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

        private final LinearLayout visitlin;
        private final LinearLayout calllin;
        private final CustomFontTextView farmername;
        private final CustomFontTextView posteddate;
        private final CustomFontTextView farmerplace;
        private final CustomFontTextView aqty;
        private final CustomFontTextView bqty;
        private final CustomFontTextView cqty;
        private final CustomFontTextView acost;
        private final CustomFontTextView bcost;
        private final CustomFontTextView ccost;
        private final CardView acard;
        private final CardView bcard;
        private final CardView ccard;
        private final CustomFontTextView allqty;
        private final CustomFontTextView allcost;
        private final CardView allcard;
        private final LinearLayout whatsapplin;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            visitlin = (LinearLayout) itemView.findViewById(R.id.visitlin);
            calllin = (LinearLayout) itemView.findViewById(R.id.calllin);
            whatsapplin = (LinearLayout) itemView.findViewById(R.id.whatsapplin);
            farmername = (CustomFontTextView) itemView.findViewById(R.id.farmername);
            posteddate = (CustomFontTextView) itemView.findViewById(R.id.posteddate);
            farmerplace = (CustomFontTextView) itemView.findViewById(R.id.placetext);
            allqty = (CustomFontTextView) itemView.findViewById(R.id.allqty);
            aqty = (CustomFontTextView) itemView.findViewById(R.id.aqty);
            bqty = (CustomFontTextView) itemView.findViewById(R.id.bqty);
            cqty = (CustomFontTextView) itemView.findViewById(R.id.cqty);
            allcost = (CustomFontTextView) itemView.findViewById(R.id.allcost);
            acost = (CustomFontTextView) itemView.findViewById(R.id.acost);
            bcost = (CustomFontTextView) itemView.findViewById(R.id.bcost);
            ccost = (CustomFontTextView) itemView.findViewById(R.id.ccost);
            allcard = (CardView) itemView.findViewById(R.id.allcard);
            acard = (CardView) itemView.findViewById(R.id.acard);
            bcard = (CardView) itemView.findViewById(R.id.bcard);
            ccard = (CardView) itemView.findViewById(R.id.ccard);
        }

        @Override
        public void onClick(View v) {
            sharedpreferences = activity.getSharedPreferences(mypreference,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(otherfarmerid, mApps.get(getAdapterPosition()).getFarmeid());
            editor.commit();
            Intent account = new Intent(activity, ProfileActivity.class);
            activity.startActivity(account);
        }

    }


    public void OrderPopup(final int position, final AppCompatActivity exActivity, final String type) {

        pay = "";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(exActivity);
        LayoutInflater inflater = exActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.order_popup, null);
        final CustomFontTextView costtxt = (CustomFontTextView) dialogView.findViewById(R.id.costtxt);
        final CustomFontTextView submittxt = (CustomFontTextView) dialogView.findViewById(R.id.r_submittxt);
        final QuantityPicker quantityPicker = (QuantityPicker) dialogView.findViewById(R.id.quantityPicker);

        //Returns the selected quantity
        quantityPicker.getQuantity();

        //Allows to set the text style quantity
        quantityPicker.setTextStyle(QuantityPicker.BOLD);

        //Allows to set the minimum quantity
        quantityPicker.setMinQuantity(1);

        //Allows to set the maximum quantity
        quantityPicker.setMaxQuantity(100);

        //Enable/Disable quantity picker
        quantityPicker.setQuantityPicker(true);

        //To set the quantity text color
        quantityPicker.setQuantityTextColor(R.color.colorPrimaryDark);

        //To set the quantity button color
        quantityPicker.setQuantityButtonColor(R.color.colorAccent);

        quantityPicker.setOnQuantityChangeListener(new QuantityPicker.OnQuantityChangeListener() {
            @Override
            public void onValueChanged(int quantity) {
                int pay1 = 0;
                if (type.equals("A")) {
                    pay1 = Integer.parseInt(mApps.get(position).getAcost()) * (quantity * 10);
                } else if (type.equals("B")) {
                    pay1 = Integer.parseInt(mApps.get(position).getBcost()) * (quantity * 10);
                } else if (type.equals("C")) {
                    pay1 = Integer.parseInt(mApps.get(position).getCcost()) * (quantity * 10);
                } else if (type.equals("ALl")) {
                    pay1 = Integer.parseInt(mApps.get(position).getAllcost()) * (quantity * 10);
                }
                pay = String.valueOf(pay1);
                costtxt.setText("₹" + pay);
            }
        });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        final CustomFontTextView itemtittle = (CustomFontTextView) dialogView.findViewById(R.id.itemtittle);
        itemtittle.setText(type + " Grade");
        final ImageView itemclose = (ImageView) dialogView.findViewById(R.id.itemclose);
        itemclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
        if (type.equals("A")) {
            costtxt.setText("₹" + Integer.parseInt(mApps.get(position).getAcost()) * 10);
        } else if (type.equals("B")) {
            costtxt.setText("₹" + Integer.parseInt(mApps.get(position).getBcost()) * 10);
        } else if (type.equals("C")) {
            costtxt.setText("₹" + Integer.parseInt(mApps.get(position).getCcost()) * 10);
        } else if (type.equals("C")) {
            costtxt.setText("₹" + Integer.parseInt(mApps.get(position).getAllcost()) * 10);
        }
        submittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (costtxt.getText().toString().length() <= 0) {
                    Toast.makeText(exActivity, "Please select less than 1 Qty", Toast.LENGTH_SHORT).show();
                } else {
                    FarmNew farmNew = mApps.get(position);
                    if (type.equals("A")) {
                        farmNew.setAqty(String.valueOf(Integer.parseInt(mApps.get(position).getAqty()) -
                                quantityPicker.getQuantity()));
                    } else if (type.equals("B")) {
                        farmNew.setBqty(String.valueOf(Integer.parseInt(mApps.get(position).getBqty()) -
                                quantityPicker.getQuantity()));
                    } else if (type.equals("C")) {
                        farmNew.setCqty(String.valueOf(Integer.parseInt(mApps.get(position).getCqty()) -
                                quantityPicker.getQuantity()));
                    } else if (type.equals("All")) {
                        farmNew.setAllqty(String.valueOf(Integer.parseInt(mApps.get(position).getAllqty()) -
                                quantityPicker.getQuantity()));
                    }
                    dbPro.updatedataByProid(mApps.get(position).getProid(), new Gson().toJson(farmNew));
                    b.cancel();
                    Intent io = new Intent();
                    activity.setResult(RESULT_OK, io);
                    activity.finish();
                    Toast.makeText(exActivity, "Successfully buyed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.show();
    }


    public void profileFunction(int position) {
        sharedpreferences = activity.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(otherfarmerid, mApps.get(position).getFarmeid());
        editor.commit();
        Intent account = new Intent(activity, ProfileActivity.class);
        activity.startActivity(account);
    }
}

