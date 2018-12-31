package com.example.temi.bluetoothdevicesurveyapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
/*
Displays a list of unique Devices previously detected from the database
 */
public class BTActivity extends Activity{

    private FirebaseDatabase mDataBase;
    private DatabaseReference mDataBaseReference;
    private ListView mDevicesList;
    private ArrayList<String> mUniqueDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mUniqueDevices = new ArrayList<>();

        mDataBase = FirebaseDatabase.getInstance();
        mDataBaseReference = mDataBase.getReference();

        mDevicesList = (ListView) findViewById(R.id.devicesTextView);

        getUniqueDevices();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
          this,
                android.R.layout.simple_list_item_1,
                mUniqueDevices
        );

        mDevicesList.setAdapter(arrayAdapter);

     }

     private void getUniqueDevices(){
         final DatabaseReference ref = mDataBaseReference.child("locations").getRef();

         // Attach a listener to read the data at our posts reference
         ref.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot locSnapshot : dataSnapshot.getChildren()) {
                     LocationData loc = locSnapshot.getValue(LocationData.class);

                     if(loc != null) {
                         for (DeviceItem item : loc.bluetoothDevices) {
                             if (!mUniqueDevices.contains(item.getDeviceName())){
                                 mUniqueDevices.add(item.getDeviceName());
                             }
                         }

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
