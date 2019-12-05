package com.example.memedetection.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.memedetection.MemeClassifier;
import com.example.memedetection.R;

import com.example.memedetection.Utils.Constants;
import com.example.memedetection.ViewModels.NetworkReqRespViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.memedetection.Utils.Constants.SINLGE_IMAGE_TEST_LOCALLY;
import static com.example.memedetection.Utils.Constants.WHICH_PLATFORM;

public class SingleMemeTestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public final int PICK_IMAGE = 1;
    ImageView imageView = null;
    TextView isMeme = null;
    TextView pbText = null;
    Button nextImage = null;
    ProgressBar progressBar = null;

    File mUploadmageFile = null;
    List<File> uMemeTestFiles = null;
    Bitmap pickedBitmap = null;


    private static final String TAG = "SingleMemeTestActivity";
    private MemeClassifier memeClassifier;
    String whichPlaform = "";

    private NetworkReqRespViewModel networkReqRespViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();


        Intent intent = getIntent();
        whichPlaform = intent.getStringExtra(WHICH_PLATFORM);
        if (whichPlaform.equalsIgnoreCase(SINLGE_IMAGE_TEST_LOCALLY)) {
            initialiseImageClassifier(this);
            setTitle("Checked Image Locally");


        } else {
            networkReqRespViewModel = ViewModelProviders.of(this).get(NetworkReqRespViewModel.class);
            uMemeTestFiles = new ArrayList<>();
            setTitle("Checked Image on Server");

        }

        requestPermission();

    }


    private void initialiseViews() {

        imageView = (ImageView) findViewById(R.id.preview);
        isMeme = (TextView) findViewById(R.id.isItMeme);
        pbText = (TextView) findViewById(R.id.pbText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        isMeme.setVisibility(View.GONE);
        pbText.setVisibility(View.GONE);
        nextImage = (Button) findViewById(R.id.next_image);
        nextImage.setOnClickListener(this);

    }


    private void initialiseImageClassifier(Context app) {

        try {
            memeClassifier = MemeClassifier.getInstance(app);
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize an image classifier.");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            progressBar.setVisibility(View.VISIBLE);
            pbText.setVisibility(View.VISIBLE);

            Uri uri = data.getData();

            try {

                pickedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                if (whichPlaform.equalsIgnoreCase(SINLGE_IMAGE_TEST_LOCALLY)) {
                    new BackgroundProcessLocal().execute(pickedBitmap);

                } else {

                    uMemeTestFiles.clear();
                    mUploadmageFile = persistImage(pickedBitmap, "temp_file");

                    if (mUploadmageFile.exists()) {
                        uMemeTestFiles.add(mUploadmageFile);
                        networkCall(pickedBitmap);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void networkCall(Bitmap bitmap) {


        networkReqRespViewModel.getPredictClasses(uMemeTestFiles).observe(this, imageResponse -> {


            if (imageResponse != null) {

                progressBar.setVisibility(View.GONE);
                pbText.setVisibility(View.GONE);
                isMeme.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

                imageView.setImageBitmap(bitmap);
                if (!TextUtils.isEmpty(imageResponse.get(0).getImageMemeStatus())) {
                    isMeme.setText(imageResponse.get(0).getImageMemeStatus());
                } else {
                    isMeme.setText(Constants.IMAGE_STATUS_NOT_DEFINED);
                }
            }

        });
    }


    private void openFilePicker() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);//one can be replaced with any action code


    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_image:
                openFilePicker();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (networkReqRespViewModel != null && networkReqRespViewModel.getPredictClasses(uMemeTestFiles).hasObservers()) {
            networkReqRespViewModel.getPredictClasses(uMemeTestFiles).removeObservers(this);
            Log.e(TAG, "unSubscribeSchoolLive networkReqRespViewModel  ");

        }

        if (memeClassifier != null) {
            memeClassifier.close();
        }


    }

    private class BackgroundProcessLocal extends AsyncTask<Bitmap, Void, String> {

        String class_label = "";
        Bitmap bitmap = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            pbText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            isMeme.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            bitmap = bitmaps[0];
            Bitmap reshapeBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
            class_label = memeClassifier.classifyFrame(reshapeBitmap);
            return class_label;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);
            pbText.setVisibility(View.GONE);
            isMeme.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

            imageView.setImageBitmap(bitmap);
            isMeme.setText(s);
        }
    }

    private File persistImage(Bitmap bitmap, String name) {

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

        File imageFile = new File(extStorageDirectory, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing bitmap", e);
        }

        return imageFile;
    }
}
