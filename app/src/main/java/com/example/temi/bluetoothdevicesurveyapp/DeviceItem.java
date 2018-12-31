package com.example.temi.bluetoothdevicesurveyapp;

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
