package com.example.memedetection.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.memedetection.Repository.NetworkRepository;
import com.example.memedetection.Retrofit.ImagesJsonParser;

import java.io.File;
import java.util.List;

public class NetworkReqRespViewModel extends AndroidViewModel {

    private NetworkRepository networkRepository;

    public NetworkReqRespViewModel(@NonNull Application application) {
        super(application);
        networkRepository = new NetworkRepository();
    }


    public String getPingServer() {
       return networkRepository.pingServer();
    }

    public LiveData<List<ImagesJsonParser>> getPredictClasses(List<File> queryImgs) {
        return networkRepository.predictClasses(queryImgs);
    }
}
