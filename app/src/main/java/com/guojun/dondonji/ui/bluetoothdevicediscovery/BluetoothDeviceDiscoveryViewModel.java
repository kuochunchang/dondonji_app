package com.guojun.dondonji.ui.bluetoothdevicediscovery;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.guojun.dondonji.model.BluetoothDeviceInfo;

import java.util.LinkedList;

public class BluetoothDeviceDiscoveryViewModel extends ViewModel {
    private MutableLiveData<LinkedList<BluetoothDeviceInfo>> availableDevicesLiveData;
    private LinkedList<BluetoothDeviceInfo> availableDevices = new LinkedList<>();

    public MutableLiveData<LinkedList<BluetoothDeviceInfo>> getAvailableDevicesLiveData() {
        if(availableDevicesLiveData == null){
            availableDevicesLiveData = new MutableLiveData<>();
            availableDevicesLiveData.setValue(availableDevices);
        }
        return availableDevicesLiveData;
    }

    public void addAvailableDevice(String name, String address){
        BluetoothDeviceInfo discoveredDevice = new BluetoothDeviceInfo(name, address);
        if(!availableDevices.contains(discoveredDevice)) {
            availableDevices.add(discoveredDevice);
        }
        availableDevicesLiveData.setValue(availableDevices);
    }


}
