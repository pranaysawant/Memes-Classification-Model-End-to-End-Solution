package com.example.memedetection.activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.memedetection.R;
import com.example.memedetection.recyclerview.ImageListAdapter;
import com.example.memedetection.Retrofit.ImagesJsonParser;
import com.example.memedetection.Room.Images;
import com.example.memedetection.Utils.Constants;
import com.example.memedetection.ViewModels.NetworkReqRespViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.memedetection.Utils.Constants.TEST_DATASET_IMAGE_PATH;

public class BulkImagesCheckOnServerActivity extends AppCompatActivity {

    /**
     * This Activity is used to check image is meme or not over REST API Request on server.
     */

    String TAG = "BulkImagesCheckOnServerActivity";
    private NetworkReqRespViewModel networkReqRespViewModel;
    RecyclerView recyclerView;
    ImageListAdapter adapter;

    private List<File> requestList;
    private List<Images> responseList;
    ProgressBar progressBar = null;
    TextView pbText = null;
    Images images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_activity);

        initView();
        prepareImageList();
        initVariables();
        setTitle("Checked Bulk Images on Server");


    }

    private void initVariables() {

        responseList = new ArrayList<>();

        networkReqRespViewModel = ViewModelProviders.of(this).get(NetworkReqRespViewModel.class);

        /**
         * below code is responsible for making Web REST API request.
         */

        networkReqRespViewModel.getPredictClasses(requestList).observe(this, imageResponse -> {

            if (imageResponse != null) {

                Log.e(TAG, "Response :  " + imageResponse);

                for (ImagesJsonParser imagesJsonParser : imageResponse) {

                    if (!TextUtils.isEmpty(imagesJsonParser.getImageMemeStatus())) {
                        images = new Images(Constants.TEST_DATASET_IMAGE_PATH + imagesJsonParser.getImagePath(), imagesJsonParser.getImageMemeStatus());
                        responseList.add(images);
                    }

                }

                if (responseList != null && responseList.size() > 0) {
                    adapter.setImages(responseList);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(this, "Something went wrong...!!! ", Toast.LENGTH_SHORT).show();
                }


                progressBar.setVisibility(View.GONE);
                pbText.setVisibility(View.GONE);
            }
        });


    }

    private void initView() {

        pbText = (TextView) findViewById(R.id.pbText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        pbText.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);

        adapter = new ImageListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    /*
     * Here we are preparing data for REST API request.
     */

    void prepareImageList() {

        requestList = new ArrayList<>();

        Log.d("Files", "Path: " + TEST_DATASET_IMAGE_PATH);
        File directory = new File(TEST_DATASET_IMAGE_PATH);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        String extension = null;
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());

            extension = files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1); // Without dot jpg, png
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                requestList.add(files[i]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribeNetworkViewModel();
    }


    private void unSubscribeNetworkViewModel() {

        if (networkReqRespViewModel != null && networkReqRespViewModel.getPredictClasses(requestList).hasObservers()) {
            networkReqRespViewModel.getPredictClasses(requestList).removeObservers(this);
        }
    }
}
