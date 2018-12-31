package com.example.temi.bluetoothdevicesurveyapp;


import java.util.ArrayList;

//Wrapper class to hold location data i.e. longitude and latitude coordinates, and a list of the devices discovered in the location
public class LocationData {
    public double longitude;
    public double latitude;
    public ArrayList<DeviceItem> bluetoothDevices = new ArrayList<DeviceItem>();


    public LocationData() {

    }

    public LocationData(double lati,double longi) {
        longitude = longi;
        latitude = lati;
    }

    public LocationData(double lati, double longi, ArrayList<DeviceItem> BTDevices) {
        longitude = longi;
        latitude = lati;
        bluetoothDevices = BTDevices;
    }


}
