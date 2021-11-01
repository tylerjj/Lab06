package com.example.lab06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity {

    // Somewhere in Australia
    private final LatLng myDestinationLatLng = new LatLng(-33.8523341, 151.2106085);
    // Somewhere in Bascom Hall
    private final LatLng myBascomLatLng = new LatLng(43.075428, -89.404381);
    // Map Reference
    private GoogleMap myMap;
    // LocationClient Reference
    private FusedLocationProviderClient myFusedLPClient;
    // Identify a Reference for this particular permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 8675309;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupLocationClient();
        setupMap();
        displayMyLocation();

    }
    private void setupLocationClient(){
        // LocationServices API:
        // https://developers.google.com/android/reference/com/google/android/gms/location/LocationServices
        myFusedLPClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void setupMap(){
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(googleMap -> {

            myMap = googleMap;

            // Code to display marker
            myMap.addMarker(new MarkerOptions()
                    .position(myDestinationLatLng)
                    .title("Destination"));

            myMap.addMarker(new MarkerOptions()
                    .position(myBascomLatLng)
                    .title("Bascom Hall"));
        });
    }

    private void displayMyLocation() {
        // Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        // If not, ask for it
        if (permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // if permission granted, display market at current location
        else {
            myFusedLPClient.getLastLocation()
                    .addOnCompleteListener(this, task-> {
                    Location myLastKnownLocation = task.getResult();
                    if (task.isSuccessful() && myLastKnownLocation != null){

                        LatLng myLatLng = new LatLng(myLastKnownLocation.getLatitude(), myLastKnownLocation.getLongitude());

                        myMap.addPolyline(new PolylineOptions().add(
                                myLatLng, myBascomLatLng));

                        myMap.addMarker(new MarkerOptions()
                        .position(myLatLng));

                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,10));
                    }
            });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }
}