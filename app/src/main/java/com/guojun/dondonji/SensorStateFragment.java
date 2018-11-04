package com.guojun.dondonji;

import android.content.Context;
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


public class SensorStateFragment extends Fragment {
    private ImageView mImageLeftOffLine;
    private ImageView mImageLeftOnLine;
    private Button mLeftButton;

    private ImageView mImageRightOffLine;
    private ImageView mImageRightOnLine;
    private Button mRightButton;


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
        mImageLeftOffLine = getActivity().findViewById(R.id.imageLeftOffLine);
        mImageLeftOnLine = getActivity().findViewById(R.id.imageLeftOnLine);
        mImageRightOffLine = getActivity().findViewById(R.id.imageRightOffLine);
        mImageRightOnLine = getActivity().findViewById(R.id.imageRightOnLine);
        mLeftButton = getActivity().findViewById(R.id.buttonLeft);
        mRightButton = getActivity().findViewById(R.id.buttonRight);

        setIsLeftSensorConnected(false);
        setIsRightSensorConnected(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setIsLeftSensorConnected(boolean connected) {
        if(connected) {
            mImageLeftOffLine.setVisibility(View.INVISIBLE);
            mImageLeftOnLine.setVisibility(View.VISIBLE);
            mLeftButton.setVisibility(View.INVISIBLE);
        }else{
            mImageLeftOffLine.setVisibility(View.VISIBLE);
            mImageLeftOnLine.setVisibility(View.INVISIBLE);
            mLeftButton.setVisibility(View.VISIBLE);
        }
    }

    private void setIsRightSensorConnected(boolean connected) {
        if(connected) {
            mImageRightOffLine.setVisibility(View.INVISIBLE);
            mImageRightOnLine.setVisibility(View.VISIBLE);
            mRightButton.setVisibility(View.INVISIBLE);
        }else{
            mImageRightOffLine.setVisibility(View.VISIBLE);
            mImageRightOnLine.setVisibility(View.INVISIBLE);
            mRightButton.setVisibility(View.VISIBLE);
        }
    }

}
