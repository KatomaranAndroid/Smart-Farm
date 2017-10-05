package com.github.bkhezry.demomapdrawingtools.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.bkhezry.demomapdrawingtools.MapsFragActivity;
import com.github.bkhezry.demomapdrawingtools.POPActivity;
import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.trees.TreesActivity;
import com.github.bkhezry.extramaputils.utils.MapUtils;
import com.github.bkhezry.mapdrawingtools.model.DrawingOption;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int REQUEST_CODE = 1;
    private MapView mMap;
    private GoogleMap googleMap;
    private DrawingOption.DrawingType currentDrawingType;
    public static String areaData = "";
    private FloatingActionButton btnSatalite;

    private CollapsingToolbarLayout collapsingToolbar;
    private TabPagerAdapter tabPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        getSupportActionBar().setTitle(MapsFragActivity.getPlotname());
        mMap = (MapView) findViewById(R.id.mapLite);
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);

        btnSatalite = (FloatingActionButton) findViewById(R.id.btnSatellite);
        btnSatalite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    googleMap.setMapType(googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ? GoogleMap.MAP_TYPE_NORMAL : GoogleMap.MAP_TYPE_SATELLITE);
                    btnSatalite.setImageResource(googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ? R.drawable.ic_satellite_off : R.drawable.ic_satellite_on);
                }
            }
        });
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabPagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(tabPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setToolbar() {

    }


    class TabPagerAdapter extends FragmentStatePagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if (position == 0) {
                PlotActivity sampleFragment = PlotActivity.newInstance("PLOT");
                return sampleFragment;
            } else if (position == 1) {
                POPActivity sampleFragment = POPActivity.newInstance("POP");
                return sampleFragment;
            } else if (position == 2) {
                AnimalsFragment sampleFragment = AnimalsFragment.newInstance("ANIMALS");
                return sampleFragment;
            } else if (position == 3) {
                PlotServicesActivity sampleFragment = PlotServicesActivity.newInstance("SERVICES");
                return sampleFragment;
            }
            return null;
        }


        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tittle = "";
            if (position == 0) {
                tittle = "PLOT";
            } else if (position == 1) {
                tittle = "POP";
            } else if (position == 2) {
                tittle = "Animals";
            } else if (position == 3) {
                tittle = "SERVICES";
            }
            return tittle;
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        this.googleMap = gMap;
        MapsInitializer.initialize(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.849412, 53.072805), 7));
        googleMap.clear();
        MapUtils.showElements(MapsFragActivity.exportOption, googleMap, this);
        areaData = String.valueOf((MapsFragActivity.exportOption.getPolygons().get(0).getArea() * 0.000247105)) + " acre";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_tree_white, menu);
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
            case R.id.tree:
                Intent ioo = new Intent(MainActivity.this, TreesActivity.class);
                startActivity(ioo);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

