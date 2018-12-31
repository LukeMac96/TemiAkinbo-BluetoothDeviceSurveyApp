package com.example.temi.bluetoothdevicesurveyapp;

//Wrapper to hold details of bluetooth devices discovered
public class DeviceItem {

    private String deviceName;
    private String address;

    public String getDeviceName(){return deviceName;}

    public String getAddress() {return address;}

    public DeviceItem(){}

    public DeviceItem(String name, String address ) {
        this.deviceName = name;
        this.address = address;
    }
}
