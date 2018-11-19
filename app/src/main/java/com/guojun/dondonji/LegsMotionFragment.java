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
    private boolean isStarted = false;
    private TextView mLeftCounterTextView;
    private TextView mRightCounterTextView;
    private ProgressBar mLeftProgressBar;
    private ProgressBar mRightProgressBar;

    private ExerciseAdapter mLeftExerciseAdapter = new ExerciseAdapter();
    private ExerciseAdapter mRightExerciseAdapter = new ExerciseAdapter();

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

        mLeftCounterTextView = getView().findViewById(R.id.leftCounter);
        mRightCounterTextView = getView().findViewById(R.id.rightCounter);

        mLeftProgressBar = getView().findViewById(R.id.leftProgressBar);
        mRightProgressBar = getView().findViewById(R.id.rightProgressBar);

        final Button buttonStart = getView().findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStarted) {
                    isStarted = true;
                    buttonStart.setText("Stop");
                }else{
                    isStarted = false;
                    buttonStart.setText("Starts");
                }
            }
        });

        mViewModel = ViewModelProviders.of(this).get(LegsMotionViewModel.class);
        mViewModel.getMotionStatus().observe(this, new Observer<LegsMotionViewModel.MotionStatus>() {
            @Override
            public void onChanged(@Nullable LegsMotionViewModel.MotionStatus status) {

                if(!isStarted){
                    return;
                }

                mLeftProgressBar.setProgress(status.getStepProgressLeft());
                mLeftCounterTextView.setText(Integer.valueOf(status.getCounterLeft()).toString());

                mRightProgressBar.setProgress(status.getStepProgressRight());
                mRightCounterTextView.setText(Integer.valueOf(status.getCounterRight()).toString());

                ;
            }
        });
    }


    public void setLeftSensorData(SensorData sensorData) {
        ExerciseAdapter.MotionInfo mo = mLeftExerciseAdapter.putAngle(sensorData.getAngleX());
        mViewModel.updateLeftStatus(mo.getCount(), mo.getProgress());

    }

    public void setRightSensorData(SensorData sensorData) {
        ExerciseAdapter.MotionInfo mo = mRightExerciseAdapter.putAngle(sensorData.getAngleX());
        mViewModel.updateRightStatus(mo.getCount(), mo.getProgress());

    }
}
