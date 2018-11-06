package com.guojun.dondonji.ui.bluetoothdevicediscovery;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guojun.dondonji.BlankFragment;
import com.guojun.dondonji.MainActivity;
import com.guojun.dondonji.R;
import com.guojun.dondonji.SensorSide;
import com.guojun.dondonji.db.AppDatabase;
import com.guojun.dondonji.db.ConfigurationEntity;
import com.guojun.dondonji.model.BluetoothDeviceInfo;
import com.guojun.dondonji.model.Configuration;

import java.util.LinkedList;

public class BluetoothDeviceDiscoveryFragment extends Fragment {

    private BluetoothDeviceDiscoveryViewModel mViewModel;
    private ListView mDeviceListView;
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceDiscoveryListener mListener;
    private static final String TAG = "DeviceDiscoveryFragment";
    public static final String ARG_DEVICE_SIDE = "side";
    private SensorSide mDeviceSide;

    public static BluetoothDeviceDiscoveryFragment newInstance(SensorSide side) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_SIDE, side.toString());
        fragment.setArguments(args);
        return new BluetoothDeviceDiscoveryFragment();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                mViewModel.addAvailableDevice(deviceName, deviceHardwareAddress);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeviceSide = SensorSide.valueOf(getArguments().getString(ARG_DEVICE_SIDE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bluetooth_device_discovery_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare database
        appDatabase =
                Room.databaseBuilder(
                        getActivity(), AppDatabase.class, "app-db").build();

        mDeviceListView = getActivity().findViewById(R.id.discovered_device_list);
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, mViewModel.getAvailableDevicesLiveData().getValue().get(position).getName());

                String name = mViewModel.getAvailableDevicesLiveData().getValue().get(position).getName();
                String address = mViewModel.getAvailableDevicesLiveData().getValue().get(position).getAddress();

                mListener.onUserSelectedDevice(name, address, mDeviceSide);

                Intent intent =
                        new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        mViewModel = ViewModelProviders.of(this).get(BluetoothDeviceDiscoveryViewModel.class);
        mViewModel.getAvailableDevicesLiveData().observe(this, new Observer<LinkedList<BluetoothDeviceInfo>>() {
            @Override
            public void onChanged(@Nullable LinkedList<BluetoothDeviceInfo> discoveryDevices) {
                // Update the UI.
                Log.d("====", discoveryDevices.toString());
                if (discoveryDevices.size() > 0) {
                    String[] listItems = new String[discoveryDevices.size()];
                    int counter = 0;
                    for (BluetoothDeviceInfo deviceInfo : discoveryDevices) {
                        listItems[counter++] = deviceInfo.getName() + " - " + deviceInfo.getAddress();
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_2, android.R.id.text1, listItems);
                    mDeviceListView.setAdapter(adapter);

                }
            }
        });



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeviceDiscoveryListener) {
            mListener = (DeviceDiscoveryListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeviceDiscoveryListener");
        }
    }

    @Override
    public void onStop() {
        mBluetoothAdapter.cancelDiscovery();
        super.onStop();
    }

    public interface DeviceDiscoveryListener{
        void onUserSelectedDevice(String name, String address, SensorSide side);
    }

    //-----------------------AsyncTasks-----------------------------------
    private AppDatabase appDatabase;
    private class InsertDbTask extends AsyncTask<ConfigurationEntity, Void, Void> {

        @Override
        protected Void doInBackground(ConfigurationEntity... entities) {
            for (ConfigurationEntity entity : entities) {
                appDatabase.configurationDao().update(entity);
            }
            return null;
        }
    }


}
