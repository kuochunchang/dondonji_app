package com.guojun.dondonji;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class LegsMotionViewModel extends ViewModel {


    private MutableLiveData<MotionStatus> progressLiveData;
    private MotionStatus status;

    public MutableLiveData<MotionStatus> getMotionStatus() {
        if (progressLiveData == null) {
            progressLiveData = new MutableLiveData<MotionStatus>();
            status = new MotionStatus(0, 0, 0, 0);
            progressLiveData.setValue(status);
        }
        return progressLiveData;
    }

    public void updateLeftStatus(int count, int progress) {
        status.counterLeft = count;
        status.stepProgressLeft = progress;
        progressLiveData.setValue(status);
    }

    public void updateRightStatus(int count, int progress) {
        status.counterRight = count;
        status.stepProgressRight = progress;
        progressLiveData.setValue(status);
    }

    public class MotionStatus {
        private int counterLeft;
        private int counterRight;

        private int stepProgressLeft;
        private int stepProgressRight;

        public MotionStatus(int counterLeft, int counterRight, int stepProgressLeft, int stepProgressRight) {
            this.counterLeft = counterLeft;
            this.counterRight = counterRight;
            this.stepProgressLeft = stepProgressLeft;
            this.stepProgressRight = stepProgressRight;
        }

        public int getCounterLeft() {
            return counterLeft;
        }

        public int getCounterRight() {
            return counterRight;
        }

        public int getStepProgressLeft() {
            return stepProgressLeft;
        }

        public int getStepProgressRight() {
            return stepProgressRight;
        }
    }


}
