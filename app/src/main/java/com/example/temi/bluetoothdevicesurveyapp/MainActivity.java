package com.example.temi.bluetoothdevicesurveyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout for created layout
        setContentView(R.layout.activity_main);
    }

    public void onGetMapActivityClick(View view){

        Intent getScreenIntent = new Intent(this, MapsActivity.class);
        //start Activity
        startActivity(getScreenIntent);

    }

    public void onGetBTDevicesActivityClick(View view){

        Intent getScreenIntent = new Intent(this, BTActivity.class);
        //start Activity
        startActivity(getScreenIntent);
    }
}
