package com.guojun.dondonji;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.guojun.dondonji.bwt901cl.DeviceDataDecoder;
import com.guojun.dondonji.bwt901cl.SensorData;
import com.guojun.dondonji.db.AppDatabase;
import com.guojun.dondonji.db.ConfigurationEntity;
import com.guojun.dondonji.model.Configuration;


public class MainActivity extends AppCompatActivity implements BluetoothActionHandler {

    private AppDatabase mAppDatabase;
    private BluetoothService mLeftBluetoothService;
    private BluetoothService mRightBluetoothService;
    private String mLeftDeviceAddress;
    private String mRightDeviceAddress;
    private LegsMotionFragment mMotionFragment;
    private static final String TAG = "MainActivity";
    private boolean mThisDeviceSupportBluetooth = false;
    private boolean mIsBluetoothEnabled = false;
    private static final int ACTION_REQUEST_ENABLE_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView deviceName = findViewById(R.id.bluetooth_device_name);

        // Check if the device has bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            mThisDeviceSupportBluetooth = false;
            deviceName.setText("This device not support bluetooth.");
            return;
        } else {
            mThisDeviceSupportBluetooth = true;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ACTION_REQUEST_ENABLE_REQUEST);

        } else {
            mIsBluetoothEnabled = true;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTION_REQUEST_ENABLE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    mIsBluetoothEnabled = true;
                    onStart();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mThisDeviceSupportBluetooth || !mIsBluetoothEnabled) {
            return;
        }

        TextView deviceName = findViewById(R.id.bluetooth_device_name);

        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();
        ConfigurationEntity addressConfigurationEntity =
                mAppDatabase.configurationDao().getConfiguration(Configuration.BLUETOOTH_DEVICE_ADDRESS);
        ConfigurationEntity nameConfigurationEntity =
                mAppDatabase.configurationDao().getConfiguration(Configuration.BLUETOOTH_DEVICE_NAME);


        if (addressConfigurationEntity == null) {
            deviceName.setText("The bluetooth device has not set up. Please tap the icon at bottom of screen to set up.");
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, BluetoothDeviceSelectActivity.class);
                    startActivity(intent);
                }
            });
            return;
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();

        String deviceAddress = addressConfigurationEntity.getValue();
        mLeftDeviceAddress = deviceAddress;
        deviceName.setText(String.format("%s (%s)", nameConfigurationEntity.getValue(), mLeftDeviceAddress));
        mLeftBluetoothService = new BluetoothService(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                TextView textView = findViewById(R.id.bluetooth_device_status);

                switch (msg.what) {
                    case BluetoothService.Constants.MESSAGE_READ:
                        SensorData data = (SensorData) msg.obj;
                        try {
                            mMotionFragment.setLeftSensorData(data);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                        break;
                    case BluetoothService.Constants.MESSAGE_CONNECTED:
                        textView.setText("Connected");
                        textView.setTextColor(getResources().getColor(R.color.colorConnected));
                        break;
                    case BluetoothService.Constants.MESSAGE_CONN_FAIL:
                        textView.setText("Fail to connect");
                        textView.setTextColor(getResources().getColor(R.color.colorAccent));

                        break;
                }
            }
        });
        mLeftBluetoothService.connect(mLeftDeviceAddress);


        if (mMotionFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            mMotionFragment = LegsMotionFragment.newInstance();
            fragmentTransaction.add(R.id.device_data_container, mMotionFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bluetooth_device_settings) {
            Intent intent = new Intent(this, BluetoothDeviceSelectActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mThisDeviceSupportBluetooth) {
            mLeftBluetoothService.disconnect();
            Log.d(TAG, "releaseService(): unbound.");
        }
        super.onDestroy();
    }

    @Override
    public void onConnected() {
//        TextView textView = MainActivity.this.findViewById(R.id.bluetooth_device_status);
//        textView.setText("Connected");
//        textView.setTextColor(getResources().getColor(R.color.colorConnected));
    }

    @Override
    public void onConnectFail() {
//        TextView textView = MainActivity.this.findViewById(R.id.bluetooth_device_status);
//        textView.setText("Fail to connect");
//        textView.setTextColor(getResources().getColor(R.color.colorAccent));

    }

    @Override
    public void onDataReceived(SensorData data) {
//        if (mMotionFragment != null) {
//            mMotionFragment.setLeftSensorData(data);
//        }
        Log.d("----", data.toString());
    }
}
