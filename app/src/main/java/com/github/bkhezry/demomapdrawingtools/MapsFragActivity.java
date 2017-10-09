package com.github.bkhezry.demomapdrawingtools;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bkhezry.demomapdrawingtools.buy.MainActivityCattle;
import com.github.bkhezry.demomapdrawingtools.trees.TreesActivity;
import com.github.bkhezry.demomapdrawingtools.ui.MainActivity;
import com.github.bkhezry.extramaputils.builder.ExtraMarkerBuilder;
import com.github.bkhezry.extramaputils.builder.ExtraPolygonBuilder;
import com.github.bkhezry.extramaputils.builder.ViewOptionBuilder;
import com.github.bkhezry.extramaputils.model.ExtraMarker;
import com.github.bkhezry.extramaputils.model.ViewOption;
import com.github.bkhezry.extramaputils.utils.MapUtils;
import com.github.bkhezry.mapdrawingtools.model.DataModel;
import com.github.bkhezry.mapdrawingtools.model.DrawingOption;
import com.github.bkhezry.mapdrawingtools.ui.BaseActivity;
import com.github.bkhezry.mapdrawingtools.utils.CalUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MapsFragActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public final static int REQUEST_CHECK_SETTINGS = 0;
    private Location currentLocation;
    private List<LatLng> points = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private Polygon polygon;
    private Polyline polyline;
    private ReactiveLocationProvider locationProvider;
    private Observable<Location> lastKnownLocationObservable;
    private Observable<Location> locationUpdatesObservable;
    private Subscription lastKnownLocationSubscription;
    private Subscription updatableLocationSubscription;
    private Marker currentMarker;
    private CompositeSubscription compositeSubscription;
    private final static String TAG = "MapsFragActivity";
    private boolean isGPSOn = false;
    private DrawingOption drawingOption;
    private View calLayout;
    private TextView areaTextView;
    private TextView lengthTextView;


    private CollapsingToolbarLayout collapsingToolbar;


    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    private MultiSnapRecyclerView mRecyclerView;
    private PlotNewAdapter mRecyclerAdapter;
    ArrayList<Plot> myList = new ArrayList<>();
    private DrawingOption.DrawingType currentDrawingType;
    String mapurl = "";
    String areaData = "";
    public static String dataArea = "";
    public static String urlMap = "";
    public static String plotname = "";
    public static ViewOption exportOption;
    public ViewOption viewOption;
    String crntPlotName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mapfrg);
        getSupportActionBar().setTitle("Plots");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_edit_location_black_24dp);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        currentDrawingType = DrawingOption.DrawingType.POLYGON;
        drawingOption = new DrawingOption(35.744502, 51.368966, 9, Color.argb(60, 0, 0, 255),
                Color.argb(100, 255, 0, 0), 3, true, true, true, currentDrawingType);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpFABs();
        setUpCalculateLayout();
        initRequestingLocation();
        if (drawingOption.getRequestGPSEnabling()) {
            requestActivatingGPS();
        }

        mRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.first_recycler_view);
        mRecyclerAdapter = new PlotNewAdapter(MapsFragActivity.this, myList);
        final LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(firstManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                dataArea = myList.get(position).getPlotarea();
                urlMap = myList.get(position).getPlotimage();
                plotname = myList.get(position).getPlotname();
                exportOption = myList.get(position).getViewOption();
                Intent io = new Intent(MapsFragActivity.this, MainActivity.class);
                startActivity(io);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void setUpFABs() {
        final FloatingActionButton btnSatellite = (FloatingActionButton) findViewById(R.id.btnSatellite);
        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ? GoogleMap.MAP_TYPE_NORMAL : GoogleMap.MAP_TYPE_SATELLITE);
                btnSatellite.setImageResource(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ? R.drawable.ic_satellite_off : R.drawable.ic_satellite_on);
            }
        });

        btnSatellite.setVisibility(drawingOption.getEnableSatelliteView() ? View.VISIBLE : View.GONE);
        FloatingActionButton btnUndo = (FloatingActionButton) findViewById(R.id.btnUndo);
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points.size() > 0) {
                    Marker marker = markerList.get(markerList.size() - 1);
                    marker.remove();
                    markerList.remove(marker);
                    points.remove(points.size() - 1);
                    if (points.size() > 0) {
                        if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYGON) {
                            drawPolygon(points);
                        } else if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYLINE) {
                            drawPolyline(points);
                        }
                    }
                }
            }
        });
        final FloatingActionButton btnDone = (FloatingActionButton) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("plus")) {
                    btnDone.setImageResource(R.drawable.ic_done_black_24dp);
                    btnDone.setTag("done");
                } else {
                    btnDone.setImageResource(R.drawable.ic_add_black_24dp);
                    btnDone.setTag("plus");
                }
                returnCurrentPosition();
            }
        });
        FloatingActionButton btnGPS = (FloatingActionButton) findViewById(R.id.btnGPS);
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPSOn) {
                    requestActivatingGPS();
                } else {
                    if (compositeSubscription != null && locationUpdatesObservable != null) {
                        getLastKnowLocation();
                    }
                }
            }
        });
    }

    private void setUpCalculateLayout() {
        calLayout = findViewById(R.id.calculate_layout);
        calLayout.setVisibility(drawingOption.getEnableCalculateLayout() ? View.VISIBLE : View.GONE);
        areaTextView = (TextView) findViewById(R.id.areaTextView);
        lengthTextView = (TextView) findViewById(R.id.lengthTextView);
    }


    private void initRequestingLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        compositeSubscription = new CompositeSubscription();
        locationProvider = new ReactiveLocationProvider(getApplicationContext());
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();
    }


    private void requestActivatingGPS() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);
        locationUpdatesObservable = locationProvider.getUpdatedLocation(locationRequest);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getLastKnowLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapsFragActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG, "Error happen during show Dialog for Turn of GPS");
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng center = new LatLng(drawingOption.getLocationLatitude(), drawingOption.getLocationLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, drawingOption.getZoom()));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                @IdRes int icon = R.drawable.ic_add_location_light_green_500_36dp;
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(MapsFragActivity.this, icon));
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(bitmap).draggable(true));
                marker.setTag(latLng);
                markerList.add(marker);
                points.add(latLng);
                if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYGON) {
                    drawPolygon(points);
                    setAreaLength(points);
                } else if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYLINE) {
                    drawPolyline(points);
                    setLength(points);
                }


            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                updateMarkerLocation(marker, false);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                updateMarkerLocation(marker, true);
            }
        });
    }

    private void updateMarkerLocation(Marker marker, boolean calculate) {
        LatLng latLng = (LatLng) marker.getTag();
        int position = points.indexOf(latLng);
        points.set(position, marker.getPosition());
        marker.setTag(marker.getPosition());
        if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYGON) {
            drawPolygon(points);
            if (calculate)
                setAreaLength(points);
        } else if (drawingOption.getDrawingType() == DrawingOption.DrawingType.POLYLINE) {
            drawPolyline(points);
            if (calculate)
                setLength(points);
        }
    }


    private void drawPolyline(List<LatLng> latLngList) {
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(drawingOption.getStrokeColor());
        polylineOptions.width(drawingOption.getStrokeWidth());
        polylineOptions.addAll(latLngList);
        polyline = mMap.addPolyline(polylineOptions);
    }


    private void drawPolygon(List<LatLng> latLngList) {
        if (polygon != null) {
            polygon.remove();
        }
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(drawingOption.getFillColor());
        polygonOptions.strokeColor(drawingOption.getStrokeColor());
        polygonOptions.strokeWidth(drawingOption.getStrokeWidth());
        polygonOptions.addAll(latLngList);
        polygon = mMap.addPolygon(polygonOptions);
    }

    @Override
    protected void onLocationPermissionGranted() {
        getLastKnowLocation();
        updateLocation();
    }

    private void updateLocation() {
        if (locationUpdatesObservable != null && compositeSubscription != null) {
            updatableLocationSubscription = locationUpdatesObservable
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            if (currentLocation == null)
                                moveMapToCenter(location);

                            currentLocation = location;
                            moveMarkerCurrentPosition(location);
                        }
                    }, new ErrorHandler());
            compositeSubscription.add(updatableLocationSubscription);
        }
    }

    private void getLastKnowLocation() {
        if (lastKnownLocationObservable != null && compositeSubscription != null) {
            lastKnownLocationSubscription =
                    lastKnownLocationObservable
                            .subscribe(new Action1<Location>() {
                                @Override
                                public void call(Location location) {
                                    currentLocation = location;
                                    moveMapToCenter(location);
                                }
                            }, new ErrorHandler());
            compositeSubscription.add(lastKnownLocationSubscription);
        }
    }


    private class ErrorHandler implements Action1<Throwable> {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(MapsFragActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error occurred", throwable);
        }
    }

    public void moveMapToCenter(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            myLocationMarker(latLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public void moveMarkerCurrentPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMap != null) {
            myLocationMarker(latLng);
        }
    }

    private void myLocationMarker(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.setPosition(latLng);
        } else {
            @IdRes int icon = R.drawable.ic_home_black_24dp;
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(MapsFragActivity.this, icon));
            currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap)
                    .draggable(false));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lastKnownLocationSubscription != null && updatableLocationSubscription != null && compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription.clear();
            updatableLocationSubscription.unsubscribe();
            lastKnownLocationSubscription.unsubscribe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        Log.d(TAG, "User enabled location");
                        getLastKnowLocation();
                        updateLocation();
                        isGPSOn = true;
                        break;
                    case RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d(TAG, "User Cancelled enabling location");
                        isGPSOn = false;
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void returnCurrentPosition() {
        if (points.size() > 0) {
            //Intent returnIntent = new Intent();
            LatLng[] latLngs = new LatLng[points.size()];
            points.toArray(latLngs);
            DataModel dataModel = new DataModel();
            dataModel.setCount(points.size());
            dataModel.setPoints(latLngs);
            //   returnIntent.putExtra(POINTS, dataModel);
            // setResult(RESULT_OK, returnIntent);
            resultFunction(dataModel);
        } else {
            setResult(RESULT_CANCELED);
        }
        // finish();
    }

    private static Bitmap getBitmapFromDrawable(Context context, int icon) {
        Drawable drawable = ContextCompat.getDrawable(context, icon);
        Bitmap obm = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(obm);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return obm;
    }

    private void setAreaLength(List<LatLng> points) {
        areaTextView.setText(getString(R.string.area_label) + String.format(Locale.ENGLISH, "%.2f", CalUtils.getArea(points)) + getString(R.string.mm_label));
        lengthTextView.setText(getString(R.string.length_label) + String.format(Locale.ENGLISH, "%.2f", CalUtils.getLength(points)) + getString(R.string.m_label));
    }

    private void setLength(List<LatLng> points) {
        lengthTextView.setText(getString(R.string.length_label) + String.format(Locale.ENGLISH, "%.2f", CalUtils.getLength(points)) + getString(R.string.m_label));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_farmer_white, menu);
        MenuItem item = menu.findItem(R.id.plot);
        item.setVisible(false);
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
            case R.id.market:
                Intent market = new Intent(MapsFragActivity.this, MainActivityCattle.class);
                startActivityForResult(market, 1);
                return true;
            case R.id.account:
                Intent account = new Intent(MapsFragActivity.this, ProfileActivity.class);
                startActivity(account);
                return true;
            case R.id.mykart:
                Intent mykart = new Intent(MapsFragActivity.this, MainActivityFarm.class);
                startActivity(mykart);
                return true;
            case R.id.tree:
                Intent tree = new Intent(MapsFragActivity.this, TreesActivity.class);
                startActivity(tree);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resultFunction(final DataModel dataModel) {

        if (currentDrawingType == DrawingOption.DrawingType.POLYGON) {
            ViewOption viewOption = new ViewOptionBuilder()
                    .withIsListView(false)
                    .withPolygons(
                            new ExtraPolygonBuilder()
                                    .setFillColor(Color.argb(100, 0, 0, 255))
                                    .setPoints(dataModel.getPoints())
                                    .setStrokeColor(Color.argb(100, 255, 0, 0))
                                    .setStrokeWidth(5)
                                    .setzIndex(9)
                                    .build())
                    .withMarkers(getMarkers(dataModel.getPoints()))
                    .withMapsZoom(9)
                    .build();
            StringBuilder pointsurl = new StringBuilder();
            for (int k = 0; k < viewOption.getPolygons().get(0).getPoints().length; k++) {
                pointsurl.append("|");
                pointsurl.append(String.valueOf(viewOption.getPolygons().get(0).getPoints()[k].latitude));
                pointsurl.append(",");
                pointsurl.append(String.valueOf(viewOption.getPolygons().get(0).getPoints()[k].longitude));
            }
            mapurl = "http://maps.googleapis.com/maps/api/staticmap?" +
                    "zoom=17&size=600x600&maptype=satellite&sensor=false&path=color%3ared|weight:1|fillcolor%3awhite" +
                    pointsurl.toString();
            areaData = String.valueOf(Math.round(viewOption.getPolygons().get(0).getArea() * 0.000247105 * 100.0) / 100.0) + " Acre";
            this.viewOption = viewOption;
            if (mMap != null) {
                MapUtils.showElements(viewOption, mMap, this);
            }
            final Plot mLog = new Plot("", "Plot " + String.valueOf(myList.size() + 1), mapurl, areaData, viewOption);
            myList.add(mLog);
            mRecyclerAdapter.notifyData(myList);
            mRecyclerView.setVisibility(View.VISIBLE);
            points = new ArrayList<>();
            for (int i = 0; i < markerList.size(); i++) {
                markerList.get(i).remove();
            }
            markerList = new ArrayList<>();
        }


    }


    private List<ExtraMarker> getMarkers(LatLng[] points) {
        List<ExtraMarker> extraMarkers = new ArrayList<>();
        @IdRes int icon = R.drawable.ic_beenhere_blue_grey_500_24dp;
        for (LatLng latLng : points) {
            ExtraMarker extraMarker =
                    new ExtraMarkerBuilder()
                            .setCenter(latLng)
                            .setIcon(icon)
                            .build();
            extraMarkers.add(extraMarker);
        }
        ExtraMarker extraMarker =
                new ExtraMarkerBuilder()
                        .setCenter(computeCentroid(points))
                        .setIcon(icon)
                        .build();
        extraMarkers.add(extraMarker);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(computeCentroid(points));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        markerOptions.title("Plot " + String.valueOf(myList.size() + 1));
        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                computeCentroid(points), 9));

        return extraMarkers;
    }


    private LatLng computeCentroid(LatLng[] points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.length;

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        return new LatLng(latitude / n, longitude / n);
    }


    public static String getDataArea() {
        return dataArea;
    }

    public static String getUrlMap() {
        return urlMap;
    }

    public static String getPlotname() {
        return plotname;
    }

    public static ViewOption getExportOption() {
        return exportOption;
    }


}
