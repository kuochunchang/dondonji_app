package com.guojun.dondonji.model;

public interface Configuration {
    String BLUETOOTH_DEVICE_ADDRESS = "bluetooth_device_address";
    String BLUETOOTH_DEVICE_NAME = "bluetooth_device_name";

    public String getName();

    public void setName(String name);

    public String getValue();

    public void setValue(String value);
}
