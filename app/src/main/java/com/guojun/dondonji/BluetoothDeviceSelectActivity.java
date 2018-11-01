package com.guojun.dondonji;

import android.bluetooth.BluetoothAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;


import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guojun.dondonji.db.AppDatabase;
import com.guojun.dondonji.db.ConfigurationEntity;
import com.guojun.dondonji.model.Configuration;
import com.guojun.dondonji.ui.bluetoothdevicediscovery.BluetoothDeviceDiscoveryFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class BluetoothDeviceSelectActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_device_select_activity);

       final FloatingActionButton fab = findViewById(R.id.scan_device);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BluetoothDeviceDiscoveryFragment mBluetoothDeviceDiscoveryFragment = BluetoothDeviceDiscoveryFragment.newInstance();
                fragmentTransaction.add(R.id.discovered_device_fragment_container, mBluetoothDeviceDiscoveryFragment);
                fragmentTransaction.commit();
                fab.hide();
            }
        });

        // Prepare database
        appDatabase =
                Room.databaseBuilder(
                        getApplicationContext(), AppDatabase.class, "app-db").build();

        // Initial the ListView of paired bluetooth device on this phone
        ArrayList<BluetoothDevice> boundedDevices = new ArrayList<>();
        boundedDevices.addAll(mBluetoothAdapter.getBondedDevices());

        PairedBluetoothDeviceItemAdapter adapter = new PairedBluetoothDeviceItemAdapter(
                new BluetoothDeviceItemModelBuilder(boundedDevices).build(),
                getApplicationContext());

        ListView listView = findViewById(R.id.paired_bluetooth_device_list);
        listView.setAdapter(adapter);

        // While the bluetooth device selected, return to MainActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<BluetoothDevice> boundedDevices = new ArrayList<>();
                boundedDevices.addAll(mBluetoothAdapter.getBondedDevices());

                BluetoothDevice selectedDevice = boundedDevices.get(position);
                ConfigurationEntity configurationEntity =
                        new ConfigurationEntity(Configuration.BLUETOOTH_DEVICE_ADDRESS, selectedDevice.getAddress());

                InsertDbTask asyncTask = new InsertDbTask();
                asyncTask.execute(configurationEntity);

                configurationEntity =
                        new ConfigurationEntity(Configuration.BLUETOOTH_DEVICE_NAME, selectedDevice.getName());

                asyncTask = new InsertDbTask();
                asyncTask.execute(configurationEntity);

                Intent intent =
                        new Intent(BluetoothDeviceSelectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //-----------------------AsyncTasks-----------------------------------
    private class InsertDbTask extends AsyncTask<ConfigurationEntity, Void, Void> {

        @Override
        protected Void doInBackground(ConfigurationEntity... entities) {
            for (ConfigurationEntity entity : entities) {
                appDatabase.configurationDao().update(entity);
            }
            return null;
        }
    }

    // --------------------ListView related classes--------------------------
    private class PairedBluetoothDeviceItemAdapter extends ArrayAdapter<BluetoothDeviceItemModel> {
        private LayoutInflater inflater = BluetoothDeviceSelectActivity.this.getLayoutInflater();
        private List<BluetoothDeviceItemModel> dataSet;


        PairedBluetoothDeviceItemAdapter(List<BluetoothDeviceItemModel> data, Context context) {
            super(context, R.layout.bluetooth_device_list_item, data);
            this.dataSet = data;
        }

        private class ViewHolder {
            TextView textName;
            TextView textAddress;
        }


        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
            View convertView = inflater.inflate(R.layout.bluetooth_device_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textName = convertView
                    .findViewById(R.id.btName);
            viewHolder.textAddress = convertView
                    .findViewById(R.id.btAddress);

            BluetoothDeviceItemModel device = dataSet.get(position);
            viewHolder.textName.setText(device.getName());
            viewHolder.textAddress.setText(device.getAddress());

            return convertView;
        }

    }

    private class BluetoothDeviceItemModel {
        private String name;
        private String address;

        BluetoothDeviceItemModel(String name, String address) {
            this.name = name;
            this.address = address;
        }

        String getName() {
            return name;
        }

        String getAddress() {
            return address;
        }
    }

    private class BluetoothDeviceItemModelBuilder {
        List<BluetoothDevice> deviceList;

        BluetoothDeviceItemModelBuilder(List<BluetoothDevice> deviceList) {
            this.deviceList = deviceList;
        }


        List<BluetoothDeviceItemModel> build() {
            List<BluetoothDeviceItemModel> result = new ArrayList<>();
            for (BluetoothDevice device : deviceList) {
                BluetoothDeviceItemModel item = new BluetoothDeviceItemModel(device.getName(), device.getAddress());
                result.add(item);
            }
            return result;

        }
    }
}
