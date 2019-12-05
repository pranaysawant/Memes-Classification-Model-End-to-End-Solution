package com.example.memedetection.worker;


import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.memedetection.MemeClassifier;
import com.example.memedetection.MemeDetectionApp;
import com.example.memedetection.Repository.ImageRepo;
import com.example.memedetection.Room.Images;
import com.example.memedetection.Utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeriodicMemesDetector extends Worker {

    private String TAG = "PeriodicMemesDetector";
    private MemeClassifier memeClassifier;
    List<Images> imagesList;
    List<String> mImageUriList = new ArrayList<>();


    private ImageRepo mRepository;


    public PeriodicMemesDetector(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        initialiseImageClassifier(context);
        mRepository = new ImageRepo(MemeDetectionApp.getInstance());

    }

    @NonNull
    @Override
    public Result doWork() {

        Log.e(TAG, "Your Periodic Meme detector is RUNNING NOW");

        if (mImageUriList.size() > 0) {
            mImageUriList.clear();
        }

        Context applicationContext = getApplicationContext();
        ContentResolver resolver = applicationContext.getContentResolver();

        imagesList = mRepository.getImageListByImageStatusNotCheck();

        for (int i = 0; i < imagesList.size(); i++) {
            Images current = imagesList.get(i);
            String imagePath = current.getImagePath();

            if (TextUtils.isEmpty(imagePath)) {
                Log.e(TAG, "Invalid input uri");
                throw new IllegalArgumentException("Invalid input uri");
            }

            Uri uri = Uri.fromFile(new File(imagePath));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                if (bitmap != null) {
                    Bitmap reshapeBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);

                    if(reshapeBitmap!=null){
                        String classLabel = memeClassifier.classifyFrame(reshapeBitmap);
//                    String classLabel = memeClassifier.classifyFrameOneClass(reshapeBitmap);

                        Images images = new Images(imagePath, classLabel);
                        mRepository.insert(images);
                    }else{
                        mRepository.deleteImagesByImagePath(imagePath);
                    }

                } else {
                    mRepository.deleteImagesByImagePath(imagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"IOException imagePath: "+imagePath);
            }


        }

        Data outputData = new Data.Builder().putString(Constants.PERIODIC_TASK, "Done").build();

        // If there were no errors, return SUCCESS
        return Result.success(outputData);

    }


    private void initialiseImageClassifier(Context app) {

        try {
            memeClassifier = MemeClassifier.getInstance(app);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize an image classifier.");
        }
    }

}
