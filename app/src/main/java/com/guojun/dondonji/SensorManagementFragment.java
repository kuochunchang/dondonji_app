package com.guojun.dondonji;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class SensorManagementFragment extends Fragment {
    private Button mLeftButton;
    private TextView mLeftText;

    private Button mRightButton;
    private TextView mRightText;

    private SensorStatus mLeftSensorStatus;
    private SensorStatus mRightSensorStatus;

    public enum SensorStatus {
        NONE, CONNECTING, CONNECTED, CONNECT_FAIL
    }

    public SensorManagementFragment() {
    }


    public static SensorManagementFragment newInstance() {
        SensorManagementFragment fragment = new SensorManagementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sensor_management, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLeftButton = getActivity().findViewById(R.id.buttonLeft);
        mRightButton = getActivity().findViewById(R.id.buttonRight);

        mLeftText = getActivity().findViewById(R.id.textLeft);
        mRightText = getActivity().findViewById(R.id.textRight);

        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BluetoothDeviceSelectActivity.class);
                Bundle b = new Bundle();
                b.putString("side", "left");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BluetoothDeviceSelectActivity.class);
                Bundle b = new Bundle();
                b.putString("side", "right");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        setLeftSensorStatus(SensorStatus.NONE);
        setRightSensorStatus(SensorStatus.NONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void updateLeftView() {
        switch (mLeftSensorStatus) {
            case CONNECTING:
                mLeftButton.setVisibility(View.INVISIBLE);
                mLeftText.setText("Connecting...");
                break;
            case CONNECTED:
                mLeftButton.setText("Setup");
                mLeftButton.setVisibility(View.VISIBLE);
                mLeftText.setTextColor(getResources().getColor(R.color.colorConnected));
                mLeftText.setText("Sensor Connected");
                break;
            case CONNECT_FAIL:
                mLeftButton.setText("Setup");
                mLeftButton.setVisibility(View.VISIBLE);
                mLeftText.setText("Sensor Disconnect");
                mLeftText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
    }

    private void updateRightView() {
        switch (mRightSensorStatus) {
            case CONNECTING:
                mRightButton.setVisibility(View.INVISIBLE);
                mRightText.setText("Connecting...");
                break;
            case CONNECTED:
                mRightButton.setText("Setup");
                mRightButton.setVisibility(View.VISIBLE);
                mRightText.setTextColor(getResources().getColor(R.color.colorConnected));
                mRightText.setText("Sensor Connected");
                break;
            case CONNECT_FAIL:
                mRightButton.setText("Setup");
                mRightButton.setVisibility(View.VISIBLE);
                mRightText.setText("Sensor Disconnect");
                mRightText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
        }
    }

    public void setLeftSensorStatus(SensorStatus status) {
        mLeftSensorStatus = status;
        updateLeftView();

    }

    public void setRightSensorStatus(SensorStatus status) {
        mRightSensorStatus = status;
        updateRightView();
    }
}
