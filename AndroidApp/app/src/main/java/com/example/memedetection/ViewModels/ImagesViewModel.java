package com.example.memedetection.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.memedetection.Repository.ImageRepo;
import com.example.memedetection.Room.Images;

import java.util.List;

import static com.example.memedetection.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImagesViewModel extends AndroidViewModel {

    private ImageRepo mRepository;

    public ImagesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ImageRepo(application);
    }

    public LiveData<List<Images>> getmAllImagesByImageStauts(String queryStatus) {
        return mRepository.getmAllImagesByImageStauts(queryStatus);
    }

    public LiveData<List<Images>> getmAllImagesExceptQueryStatus(String queryStatus) {
        return mRepository.getmAllImagesExceptQueryStatus(queryStatus);
    }

    public void insert(Images images) {
        mRepository.insert(images);
    }

    public int getRowCount() {
        return mRepository.getRowCount();
    }
}
