package com.example.memedetection;

import android.app.Application;


public class MemeDetectionApp extends Application {

    private static MemeDetectionApp uniqueInstance;

    public synchronized static MemeDetectionApp getInstance() {
        return uniqueInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        if (uniqueInstance == null) {
            uniqueInstance = this;
        }
    }
}
