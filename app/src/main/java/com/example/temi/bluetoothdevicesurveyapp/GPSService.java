package com.example.temi.bluetoothdevicesurveyapp;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


public class GPSService extends Service implements android.location.LocationListener {

    private LocationManager lm;
    private LocationData obj;
    private Location loc;
    private BluetoothAdapter mBluetoothAdapter;
    private DatabaseReference mDBRef;
    private static final String TAG = "#GPSService";
    private ArrayList<DeviceItem> mBluetoothDevices;
    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.e(TAG, "OnReceive: ACTION DISCOVERY STARTED.");
            }

            // if device discovery completed Upload location data object to DB
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                //new locationData instance with current latitude and longitude, and Arraylist of discovered devices
                obj = new LocationData(loc.getLatitude(), loc.getLongitude(), mBluetoothDevices);
                String timeStamp = Long.toString(System.currentTimeMillis());
                mDBRef.child(timeStamp).setValue(obj);
                Log.e(TAG, "OnReceive: ACTION DISCOVERY FINISHED.");
                Log.e(TAG, "Location logged:  "+ obj.latitude + " " + obj.longitude + " number of devices: " + obj.bluetoothDevices.size());

                mBluetoothDevices.clear();
            }

            //if device discovered add to BluetoothDevices ArrayList as deviceItem object
            else if (action.equals(BluetoothDevice.ACTION_FOUND)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                DeviceItem deviceItem = new DeviceItem(device.getName(), device.getAddress());

                mBluetoothDevices.add(deviceItem);

                Log.e(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return communication channel to the service
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");

        // instance of DB to upload new location data to
        mDBRef = FirebaseDatabase.getInstance().getReference("locations");
        //location manager used to access phones location services
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Bluetooth adapter used to access phones bluetooth functions
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // new instance of ArrayList to hold bluetooth devices
        mBluetoothDevices = new ArrayList<>();

        // Update location every 15 minutes and if moved by 1000 meters
        try {
            if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(lm.NETWORK_PROVIDER,
                        60000*15,
                        1000,
                        this);
            }
            else if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                lm.requestLocationUpdates(lm.GPS_PROVIDER,
                        60000*15,
                        1000,
                        this);
            }

        }
        catch (SecurityException e) {
            Log.e(TAG, "exception occurred " + e.getMessage());
        }
        catch (Exception e) {
            Log.e(TAG, "exception occurred " + e.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (lm != null){
            try {
                lm.removeUpdates(this);
            }
            catch (SecurityException e) {
                Log.e(TAG, "exception occured " + e.getMessage());
            }
            catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }
        unregisterReceiver(mBroadcastReceiver1);
        mBluetoothAdapter.cancelDiscovery();
    }

    //When location changes start bluetooth discovery
    @Override
    public void onLocationChanged(Location location) {
        //
        loc = location;

        Log.d(TAG, "btnDiscover: Looking for unpaired devices. ");

        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }


        IntentFilter discoverDevicesIntent = new IntentFilter();

        discoverDevicesIntent.addAction(BluetoothDevice.ACTION_FOUND);
        discoverDevicesIntent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoverDevicesIntent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        this.registerReceiver(mBroadcastReceiver1, discoverDevicesIntent);
        mBluetoothAdapter.startDiscovery();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.e(TAG, "status changed to " + s+ " [" + i + "]");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "provider enabled " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "provider disabled " + s);
    }



}



