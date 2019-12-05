package com.example.memedetection.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.memedetection.R;
import com.example.memedetection.Room.Images;
import com.example.memedetection.Utils.Constants;
import com.example.memedetection.ViewModels.ImagesViewModel;
import com.example.memedetection.recyclerview.ImageListAdapter;
import com.example.memedetection.worker.PeriodicMemeDetectionViewModel;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.memedetection.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;
import static com.example.memedetection.Utils.Constants.WHATSAPP_IMAGE_PATH;

public class BulkLocallyOperationalActivity extends AppCompatActivity {

    private ImagesViewModel mImageViewModel;
    private PeriodicMemeDetectionViewModel mPeriodicViewModel;
    private String TAG = "BulkLocallyOperationalActivity";
    RecyclerView recyclerView;
    ImageListAdapter adapter;
    ProgressBar progressBar = null;
    TextView pbText = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        setTitle("Checked Bulk Images Locally");

        initViews();
        initialiseViewModel();
        checkDataBaseRows();
    }


    private void initViews() {

        pbText = (TextView) findViewById(R.id.pbText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);

        adapter = new ImageListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        progressBar.setVisibility(View.GONE);
        pbText.setVisibility(View.GONE);

    }

    // CODE: https://stackoverflow.com/questions/52057041/how-to-get-the-row-count-of-room-database-in-android

    private void checkDataBaseRows() {

        final AtomicInteger fcount = new AtomicInteger();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                int dbRows = mImageViewModel.getRowCount();
                fcount.set(dbRows);

            }
        });

        t.setPriority(10);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int row = fcount.get();
        if (row <= 0) {
            insertMemesImagesDataset();
        }


    }

    private void initialiseViewModel() {
        mImageViewModel = ViewModelProviders.of(this).get(ImagesViewModel.class);
        mPeriodicViewModel = ViewModelProviders.of(this).get(PeriodicMemeDetectionViewModel.class);


        mPeriodicViewModel.getOutputWorkInfo().observe(this, workInfos -> {

            if (workInfos == null || workInfos.isEmpty()) {
                return;
            }


            if (mPeriodicViewModel.getOutputWorkInfo().hasActiveObservers()) {
                Log.e(TAG, "mPeriodicViewModel ACTIVE OBSEVER");

            }


            for (int i = 0; i < workInfos.size(); i++) {
                WorkInfo workInfo = workInfos.get(i);

                if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.ENQUEUED) {
                    Data outputData = workInfo.getOutputData();
                    Log.e(TAG, "mPeriodicViewModel WorkInfo.State.ENQUEUED ");
                    showWorkFinished();

                }
            }
        });


        mImageViewModel.getmAllImagesExceptQueryStatus(IMAGE_STATUS_NOT_CHECKED).observe(this, new Observer<List<Images>>() {
            @Override
            public void onChanged(@Nullable final List<Images> images) {
                // Update the cached copy of the words in the adapter.
                adapter.setImages(images);
                recyclerView.setVisibility(View.VISIBLE);

            }
        });

    }

    void insertMemesImagesDataset() {

        showWorkInProgress();
        File directory = new File(WHATSAPP_IMAGE_PATH);
        File[] files = directory.listFiles();
        String extension = null;
        for (int i = 0; i < files.length; i++) {

            extension = files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1); // Without dot jpg, png
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                Images word = new Images(Constants.WHATSAPP_IMAGE_PATH + files[i].getName(), IMAGE_STATUS_NOT_CHECKED);
                mImageViewModel.insert(word);
            }
        }
        Log.e(TAG, "insertMemesImagesDataset == Done : ");
        showWorkInProgress();
        mPeriodicViewModel.initialisePerioidWork();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribeImageViewModel();
        unSubscribemPeriodicViewModel();


    }

    private void unSubscribemPeriodicViewModel() {

        if (mPeriodicViewModel != null) {
            mPeriodicViewModel.getOutputWorkInfo().removeObservers(this);
        }
    }

    private void unSubscribeImageViewModel() {

        if (mImageViewModel != null && mImageViewModel.getmAllImagesByImageStauts(IMAGE_STATUS_NOT_CHECKED).hasObservers()) {
            mImageViewModel.getmAllImagesByImageStauts(IMAGE_STATUS_NOT_CHECKED).removeObservers(this);
        }
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private void showWorkInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        pbText.setVisibility(View.VISIBLE);

    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private void showWorkFinished() {
        progressBar.setVisibility(View.GONE);
        pbText.setVisibility(View.GONE);
    }


}
