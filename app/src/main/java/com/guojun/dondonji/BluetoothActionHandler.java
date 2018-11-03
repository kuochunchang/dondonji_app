package com.guojun.dondonji;

import com.guojun.dondonji.bwt901cl.SensorData;

public interface BluetoothActionHandler {
    void onConnected();
    void onConnectFail();
    void onDataReceived(SensorData data);
}
