package com.guojun.dondonji;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.guojun.dondonji.bwt901cl.DeviceDataDecoder;
import com.guojun.dondonji.bwt901cl.SensorData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");
    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private DeviceDataDecoder mDeviceDataDecoder;


    public BluetoothService(Handler handler) {
        this.mHandler = handler;
        mDeviceDataDecoder = new DeviceDataDecoder(new DeviceDataDecoder.DecodedDataListener() {
            @Override
            public void onDataDecoded(SensorData data) {

                mHandler.obtainMessage(Constants.MESSAGE_READ, data)
                        .sendToTarget();
            }
        });
    }

    public interface Constants {

        int MESSAGE_CONNECTED = 0;
        int MESSAGE_STATE_CHANGE = 1;
        int MESSAGE_READ = 2;
        int MESSAGE_WRITE = 3;
        int MESSAGE_DEVICE_NAME = 4;
        int MESSAGE_TOAST = 5;
        int MESSAGE_CONN_FAIL = 6;

        // Key names received from the BluetoothChatService Handler
        String DEVICE_NAME = "device_name";
        String TOAST = "toast";

    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;


        public ConnectThread(String address) {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                Log.d(TAG, "Connected!");
            } catch (IOException e) {
                Log.e(TAG, "Unable to connect socket ", e);
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "Unable to close() socket during connection failure", e2);
                }

                mHandler.obtainMessage(Constants.MESSAGE_CONN_FAIL)
                        .sendToTarget();

                return;
            }

            mHandler.obtainMessage(Constants.MESSAGE_CONNECTED)
                    .sendToTarget();

            if (mConnectedThread != null) {
                mConnectedThread.cancel();
            }
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }

        public void cancel() {
            try {
                if(mConnectedThread != null) {
                    mConnectedThread.cancel();
                    mConnectedThread = null;
                }
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "Create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    Log.d(TAG, "message bytes " + bytes);
                    mDeviceDataDecoder.putRawData(buffer, bytes);

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);

                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    public void connect(String deviceAddress) {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mConnectThread = new ConnectThread(deviceAddress);
        mConnectThread.start();

    }

    public void disconnect() {
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
        }
        if(mConnectThread != null) {
            mConnectThread.cancel();
        }
    }

}
