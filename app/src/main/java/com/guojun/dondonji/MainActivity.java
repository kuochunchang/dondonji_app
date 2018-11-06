package com.guojun.dondonji;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
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

import com.guojun.dondonji.bwt901cl.SensorData;
import com.guojun.dondonji.db.AppDatabase;
import com.guojun.dondonji.db.ConfigurationEntity;
import com.guojun.dondonji.model.Configuration;


public class MainActivity extends AppCompatActivity {

    private AppDatabase mAppDatabase;
    private MotionSensorService mLeftMotionSensorService;
    private MotionSensorService mRightMotionSensorService;
    private String mLeftMotionSensorAddress;
    private String mRightMotionSensorAddress;
    private LegsMotionFragment mMotionFragment;
    private SensorManagementFragment mSensorFragment;
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
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            mThisDeviceSupportBluetooth = false;
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

        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();
        ConfigurationEntity leftAddressConfigurationEntity =
                mAppDatabase.configurationDao().getConfiguration(Configuration.LEFT_BLUETOOTH_DEVICE_ADDRESS);
        ConfigurationEntity leftNameConfigurationEntity =
                mAppDatabase.configurationDao().getConfiguration(Configuration.LEFT_BLUETOOTH_DEVICE_NAME);


        if (leftAddressConfigurationEntity == null) {
//            deviceName.setText("The bluetooth device has not set up. Please tap the icon at bottom of screen to set up.");
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

        if(mLeftMotionSensorService == null) {
            mLeftMotionSensorService = new MotionSensorService(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
//                TextView textView = findViewById(R.id.bluetooth_device_status);

                    switch (msg.what) {
                        case MotionSensorService.Constants.MESSAGE_READ:
                            SensorData data = (SensorData) msg.obj;
                            try {
                                mMotionFragment.setLeftSensorData(data);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                            break;
                        case MotionSensorService.Constants.MESSAGE_CONNECTING:
                            mSensorFragment.setLeftSensorStatus(SensorManagementFragment.SensorStatus.CONNECTING);
                            break;
                        case MotionSensorService.Constants.MESSAGE_CONNECTED:
                            mSensorFragment.setLeftSensorStatus(SensorManagementFragment.SensorStatus.CONNECTED);
                            break;
                        case MotionSensorService.Constants.MESSAGE_CONN_FAIL:
                            mSensorFragment.setLeftSensorStatus(SensorManagementFragment.SensorStatus.CONNECT_FAIL);

                            break;
                    }
                }
            });
        }
        if (mMotionFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            mMotionFragment = LegsMotionFragment.newInstance();
            fragmentTransaction.add(R.id.device_data_container, mMotionFragment);
            fragmentTransaction.commit();
        }

        if (mSensorFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            mSensorFragment = SensorManagementFragment.newInstance();
            fragmentTransaction.add(R.id.device_status_container, mSensorFragment);
            fragmentTransaction.commit();
        }

        String deviceAddress = leftAddressConfigurationEntity.getValue();
        mLeftMotionSensorAddress = deviceAddress;
        mLeftMotionSensorService.connect(mLeftMotionSensorAddress);


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
            mLeftMotionSensorService.disconnect();
            Intent intent = new Intent(this, BluetoothDeviceSelectActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (mThisDeviceSupportBluetooth) {
            mLeftMotionSensorService.disconnect();
            Log.d(TAG, "releaseService(): unbound.");
        }
        super.onDestroy();
    }

}
