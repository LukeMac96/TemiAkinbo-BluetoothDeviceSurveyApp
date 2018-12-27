package com.example.temi.bluetoothdevicesurveyapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class GPSService extends Service implements android.location.LocationListener {

    private LocationManager lm;
    //private BluetoothAdapter mBtAdap;
    private DatabaseReference mDBRef;
    private static final String TAG = "#GPSService";

    public GPSService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return communication channel to the service
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");
        mDBRef = FirebaseDatabase.getInstance().getReference();
        checkLocationPermission();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(lm.NETWORK_PROVIDER, 5000, 10, this);
        }
        catch (SecurityException e) {
            Log.e(TAG, "exception occured " + e.getMessage());
        }
        catch (Exception e) {
            Log.e(TAG, "exception occured " + e.getMessage());
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
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationData obj = new LocationData(location.getLatitude(), location.getLongitude());
        mDBRef.child("location").push().setValue(obj);
        Log.e(TAG, "Location logged:  "+ obj.latitude + " " + obj.longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.e(TAG, "status changed to " + s+ " [" + i + "]");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "provider disabled " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "provider enabled " + s);
    }

    public void checkLocationPermission(){

    }
}
