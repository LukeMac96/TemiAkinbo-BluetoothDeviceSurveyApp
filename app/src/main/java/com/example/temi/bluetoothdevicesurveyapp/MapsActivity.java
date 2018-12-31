package com.example.temi.bluetoothdevicesurveyapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.*;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mDataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDataBase = FirebaseDatabase.getInstance();
        mDataBaseReference = mDataBase.getReference();

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        final DatabaseReference ref = mDataBaseReference.child("locations").getRef();

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locSnapshot : dataSnapshot.getChildren()) {
                    LocationData loc = locSnapshot.getValue(LocationData.class);
                    long timeMiliSeconds = Long.parseLong(locSnapshot.getKey());
                    //create date object using milliseconds and get a string in expected format
                    Date date = new Date(timeMiliSeconds);
                    String title = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(date) + " Number of Devices: " + loc.bluetoothDevices.size();

                    if (loc != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.latitude, loc.longitude))
                                .title(title)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        //Center Camera to current location
                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(loc.latitude, loc.longitude));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



    }



}

