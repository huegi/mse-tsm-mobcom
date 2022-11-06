package org.tamberg.myblescannerapp;

public class ScanResultModel {
    private String deviceName;
    private String deviceAddress;

    public ScanResultModel(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }


    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }
}
