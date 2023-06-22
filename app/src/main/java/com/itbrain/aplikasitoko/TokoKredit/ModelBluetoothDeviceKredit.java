package com.itbrain.aplikasitoko.TokoKredit;

public class ModelBluetoothDeviceKredit {
    private String deviceName, address;

    public ModelBluetoothDeviceKredit(String deviceName, String address) {
        this.deviceName = deviceName;
        this.address = address;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getAddress() {
        return address;
    }
}

