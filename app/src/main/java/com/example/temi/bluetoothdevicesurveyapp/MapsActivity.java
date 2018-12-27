package com.example.temi.bluetoothdevicesurveyapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mDataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        startService(new Intent(this, GPSService.class));
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

        if (ref != null) {
            // Attach a listener to read the data at our posts reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot locSnapshot : dataSnapshot.getChildren()) {
                        LocationData loc = locSnapshot.getValue(LocationData.class);
                        //System.out.println(post);
                        if (loc != null) {
                            // App 2: Todo: Add a map marker here based on the loc downloaded

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(loc.latitude, loc.longitude))
                                    .title("Hello!"));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });


            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(53.283912, -9.063874));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

        }
        else {
            // Add a marker in Sydney and move the camera
            LatLng engBuilding = new LatLng(53.283912, -9.063874);
            mMap.addMarker(new MarkerOptions().position(engBuilding).title("Marker in Engineering Building"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(engBuilding));
        }
    }


}
