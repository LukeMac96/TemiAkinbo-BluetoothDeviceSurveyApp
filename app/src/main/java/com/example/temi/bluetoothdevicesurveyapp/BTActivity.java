package com.example.temi.bluetoothdevicesurveyapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
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

        //Use ArrayAdapter to populate listView with contents of UniqueDevices ArrayList
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
          this,
                android.R.layout.simple_list_item_1,
                mUniqueDevices
        );

        mDevicesList.setAdapter(arrayAdapter);

     }

     //Goes through DB entries and adds unique bluetooth devices to UniqueDevices ArrayList
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
