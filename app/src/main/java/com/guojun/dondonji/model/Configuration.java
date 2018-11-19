package com.guojun.dondonji.model;

public interface Configuration {
//    String BLUETOOTH_DEVICE_ADDRESS = "bluetooth_device_address";
//    String BLUETOOTH_DEVICE_NAME = "bluetooth_device_name";
    String LEFT_BLUETOOTH_DEVICE_ADDRESS = "left_bluetooth_device_address";
    String LEFT_BLUETOOTH_DEVICE_NAME = "left_bluetooth_device_name";
    String RIGHT_BLUETOOTH_DEVICE_ADDRESS = "right_bluetooth_device_address";
    String RIGHT_BLUETOOTH_DEVICE_NAME = "right_bluetooth_device_name";

    public String getName();

    public void setName(String name);

    public String getValue();

    public void setValue(String value);
}
