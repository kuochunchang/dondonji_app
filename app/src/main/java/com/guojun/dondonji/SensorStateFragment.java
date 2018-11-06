package com.guojun.dondonji;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class SensorStateFragment extends Fragment {
    private Button mLeftButton;
    private TextView mLeftText;

    private Button mRightButton;
    private TextView mRightText;


    public SensorStateFragment() {


    }



    public static SensorStateFragment newInstance() {
        SensorStateFragment fragment = new SensorStateFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sensor_state, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLeftButton = getActivity().findViewById(R.id.buttonLeft);
        mRightButton = getActivity().findViewById(R.id.buttonRight);
        mLeftText = getActivity().findViewById(R.id.textLeft);
        mRightText = getActivity().findViewById(R.id.textRight);

//        setLeftSensorConnection(false);
//        setRightSensorConnection(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setLeftSensorConnection(boolean connected) {
        if(connected) {
            mLeftButton.setText("Setup");
            mLeftText.setTextColor(getResources().getColor(R.color.colorConnected));
            mLeftText.setText("Sensor Connected");
        }else{
            mLeftButton.setText("Connect");
            mLeftText.setText("Sensor Disconnect");
        }
    }

    public void setRightSensorConnection(boolean connected) {
        if(connected) {
            mRightButton.setText("Setup");
            mLeftText.setTextColor(Color.GREEN);
            mRightText.setText("Sensor Connected");
        }else{
            mRightButton.setText("Connect");
            mRightText.setText("Sensor Disconnect");
        }
    }

}
