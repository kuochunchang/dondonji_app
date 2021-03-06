package com.guojun.dondonji.model;

import java.util.Objects;

public class BluetoothDeviceInfo {
    private String name;
    private String address;

    public BluetoothDeviceInfo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }


    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothDeviceInfo that = (BluetoothDeviceInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, address);
    }
}
