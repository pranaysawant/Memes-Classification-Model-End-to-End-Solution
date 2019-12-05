package com.example.memedetection.worker;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.memedetection.Utils.Constants.TAG_PERIODIC_OUTPUT;

public class PeriodicMemeDetectionViewModel extends AndroidViewModel {

    private WorkManager mWorkManager;
    private LiveData<List<WorkInfo>> mSavedWorkInfo;
    private String TAG= "PeriodicMemeDetectionViewModel";


    public PeriodicMemeDetectionViewModel(@NonNull Application application) {
        super(application);
        mWorkManager = WorkManager.getInstance(application);
        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_PERIODIC_OUTPUT);
    }

    public void initialisePerioidWork() {

        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(PeriodicMemesDetector.class,
                1, TimeUnit.MINUTES)
                .addTag(TAG_PERIODIC_OUTPUT)
                .build();

        mWorkManager.enqueue(periodicWork);
        Log.e(TAG,"mWorkManager --- enqueue");

    }

    public LiveData<List<WorkInfo>> getOutputWorkInfo() { return mSavedWorkInfo; }

}
