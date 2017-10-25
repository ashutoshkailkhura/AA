package com.example.android.aa.findhelp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.aa.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ShowInMap extends AppCompatActivity implements
        OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks {
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    TextView myLoc, selectLoc;
    Marker mCurrLocationMarker;
    ArrayList<String> rvalues = new ArrayList<>();
    ImageView visOrDis;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_in_map);
        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        //get xml view reference
        myLoc = (TextView) findViewById(R.id.yourlocation);
        selectLoc = (TextView) findViewById(R.id.selectedlocation);
        visOrDis = (ImageView) findViewById(R.id.cur_loc_img);
        //get lat n lng of selected place
        Intent i = getIntent();
        rvalues = i.getStringArrayListExtra("detail");
        //set view
        selectLoc.setText(rvalues.get(2));


    }

    public void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                ShowInMap.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowInMap.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(ShowInMap.this, "I know you said no, but I'm asking again", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(ShowInMap.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        Location currentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (currentLocation == null) {
            Toast.makeText(this, "Cannot Find Current Location", Toast.LENGTH_SHORT).show();
        } else {
            //marker to current locaion
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            markerOptions.title("Your Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            mCurrLocationMarker = mMap.addMarker(markerOptions);

        }

    }

    private boolean gpsEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(Double.valueOf(rvalues.get(0)), Double.valueOf(rvalues.get(1)));
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(rvalues.get(2)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}