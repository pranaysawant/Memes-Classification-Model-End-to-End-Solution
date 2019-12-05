package com.example.memedetection.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memedetection.R;
import com.example.memedetection.ViewModels.NetworkReqRespViewModel;

import static com.example.memedetection.Utils.Constants.SINGLE_IMAGE_TEST_OVER_SERVER;
import static com.example.memedetection.Utils.Constants.SINLGE_IMAGE_TEST_LOCALLY;
import static com.example.memedetection.Utils.Constants.WHICH_PLATFORM;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {


    Button singleLocal, singleServer, multipleLocal, multipleServer, pingBtn = null;
    private static final int REQUEST_WRITE_PERMISSION = 22;
    private NetworkReqRespViewModel networkReqRespViewModel;
    ProgressBar progressBar = null;
    TextView pbText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        singleLocal = (Button) findViewById(R.id.one_local);
        singleServer = (Button) findViewById(R.id.one_image_server);
        multipleLocal = (Button) findViewById(R.id.multiple_local);
        multipleServer = (Button) findViewById(R.id.muliple_iamges_server);
        pingBtn = (Button) findViewById(R.id.pingserverbtn);

        pbText = (TextView) findViewById(R.id.pbText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        pbText.setVisibility(View.GONE);


        singleLocal.setOnClickListener(this);
        singleServer.setOnClickListener(this);
        multipleLocal.setOnClickListener(this);
        multipleServer.setOnClickListener(this);
        pingBtn.setOnClickListener(this);

        networkReqRespViewModel = ViewModelProviders.of(this).get(NetworkReqRespViewModel.class);

        requestPermission();

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ////showWhatsAppImages();
        } else if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            requestPermission();
            Toast.makeText(this, "Without Permission We can not Proceed..! " +
                    "  Setting > App > AppInfo > Change Permisson..!", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.one_local:
                Intent i1 = new Intent(this, SingleMemeTestActivity.class);
                i1.putExtra(WHICH_PLATFORM, SINLGE_IMAGE_TEST_LOCALLY);
                startActivity(i1);
                break;
            case R.id.one_image_server:

                Intent i1s = new Intent(this, SingleMemeTestActivity.class);
                i1s.putExtra(WHICH_PLATFORM, SINGLE_IMAGE_TEST_OVER_SERVER);
                startActivity(i1s);

                break;
            case R.id.multiple_local:
                Intent i2 = new Intent(this, BulkLocallyOperationalActivity.class);
                startActivity(i2);
                break;
            case R.id.muliple_iamges_server:
                Intent i3 = new Intent(this, BulkImagesCheckOnServerActivity.class);
                startActivity(i3);
                break;
            case R.id.pingserverbtn:
                new pingServer().execute();
                break;

        }

    }

    class pingServer extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            pbText.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            return networkReqRespViewModel.getPingServer();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);
            pbText.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(s)) {
                Toast.makeText(SplashActivity.this, "Server Up", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SplashActivity.this, "Server Down", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
