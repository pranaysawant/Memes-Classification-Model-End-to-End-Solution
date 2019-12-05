package com.example.memedetection.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memedetection.Retrofit.ApiRequest;
import com.example.memedetection.Retrofit.ImagesJsonParser;
import com.example.memedetection.Retrofit.RetrofitRequest;
import com.example.memedetection.Room.Images;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NetworkRepository {
    private String TAG = "NetworkRepository";
    private ApiRequest apiRequest;
    String pingResponse= "";


    List<MultipartBody.Part> part = null;
    List<RequestBody> description = null;


    public NetworkRepository() {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
    }

    public LiveData<List<ImagesJsonParser>> predictClasses(List<File> queryImgs) {


        part = new ArrayList<>();
        description = new ArrayList<>();


        for (int i = 0; i < queryImgs.size(); i++) {

            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), queryImgs.get(i));
            part.add(MultipartBody.Part.createFormData("file", queryImgs.get(i).getName(), fileReqBody));
            description.add(RequestBody.create(MediaType.parse("text/plain"), "image-type"));

        }

        final MutableLiveData<List<ImagesJsonParser>> data = new MutableLiveData<>();


        apiRequest.bulkImagesTest(part, description).enqueue(new Callback<List<ImagesJsonParser>>() {
            @Override
            public void onResponse(Call<List<ImagesJsonParser>> call, Response<List<ImagesJsonParser>> response) {
                Log.d(TAG, "onResponse response:: " + response);


                if (response.body() != null) {
                    data.setValue(response.body());

                    Log.d(TAG, "ImagePath:: " + response.body().get(0).getImagePath());
                    Log.d(TAG, "articles size:: " + response.body().get(0).getImageMemeStatus());
                }
            }

            @Override
            public void onFailure(Call<List<ImagesJsonParser>> call, Throwable t) {
                data.setValue(null);
                Log.e(TAG,"response onFailure "+call);

            }
        });

        return data;
    }


    public String pingServer() {

        apiRequest.pingserver().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse response:: " + response);
                pingResponse = "Pong";

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onResponse FAIL:: " + t +" -- "+ call);

            }
        });

        return pingResponse;
    }

}
