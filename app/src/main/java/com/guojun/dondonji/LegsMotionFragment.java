package com.guojun.dondonji;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guojun.dondonji.bwt901cl.SensorData;


public class LegsMotionFragment extends Fragment {

    private LegsMotionViewModel mViewModel;
    private boolean isStart = false;
    private Integer initialAngle = null;
    MotionDecoder lMotionDecoder = new MotionDecoder();

    public static LegsMotionFragment newInstance() {
        return new LegsMotionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.legs_motion_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button buttonStart = getView().findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
            }
        });

        mViewModel = ViewModelProviders.of(this).get(LegsMotionViewModel.class);
        mViewModel.getProgress().observe(this, new Observer<int[]>() {
            @Override
            public void onChanged(@Nullable int[] progress) {

                if(! isStart){
                    return;
                }

                if(initialAngle == null){
                    initialAngle = progress[0];
                    lMotionDecoder.setInitAngle(initialAngle);
                }

                TextView leftCounterTextView = getView().findViewById(R.id.leftCounterA);
                TextView rightCounterTextView = getView().findViewById(R.id.rightCounter);
                Log.d("666", ""+progress[0]);
//                leftCounterTextView.setText(String.valueOf(doubles[0]));

                ProgressBar pbl = getView().findViewById(R.id.leftProgressBar);
                ProgressBar pbr = getView().findViewById(R.id.rightProgressBar);



                MotionDecoder.StatePack state = lMotionDecoder.putAngle(progress[0]);



                pbl.setMax(lMotionDecoder.getTarget_angle());
                pbr.setMax(lMotionDecoder.getTarget_angle());

                pbl.setProgress(state.getProgress().intValue());
                leftCounterTextView.setText(String.valueOf(state.getCount()));

//                rightCounterTextView.setText(state.getStatus().toString());
            }
        });
    }


    public void setSensorData(SensorData sensorData) {
        Log.d("666",sensorData.getAngleX()+"");
        mViewModel.setLeftProgress(Double.valueOf(sensorData.getAngleX()).intValue());

    }
}
