package com.example.temi.bluetoothdevicesurveyapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import java.util.Set;

public class BTActivity extends Activity{

    private BluetoothAdapter BTAdapter;
    private TextView mDevicesList;

    private static final int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mDevicesList = findViewById(R.id.devicesTextView);

        //adapter
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is on if not turn on
        if(!BTAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }

        if(BTAdapter.isEnabled()) {
            Set<BluetoothDevice> devices = BTAdapter.getBondedDevices();
            for (BluetoothDevice device : devices){
                mDevicesList.append("\n" + device.getName());
            }
        }


    }
}
