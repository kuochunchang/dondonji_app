package com.guojun.dondonji;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class LegsMotionViewModel extends ViewModel {
    private int counterLeft;
    private int counterRight;

    private int[] progress = new int[]{0, 0};

    private MutableLiveData<int[]> progressLiveData = new  MutableLiveData<>();


    public MutableLiveData<int[]> getProgress() {
        return progressLiveData;
    }

    public void setLeftProgress(int progress) {
        this.progress[0] = progress;
        progressLiveData.setValue(this.progress);

    }

    public void setRightProgress(int progress) {
        this.progress[0] = progress;
        progressLiveData.setValue(this.progress);
    }

}
