package com.example.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    //Somewhere in Australia
    private final LatLng myDestinationLatLng = new LatLng(-33.8523341, 151.2106085);
    //Somewhere in Bascom Hall
    private final LatLng myBascomLatLng = new LatLng(43.075428, -89.404381);
    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}