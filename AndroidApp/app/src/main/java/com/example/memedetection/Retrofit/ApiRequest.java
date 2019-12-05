package com.example.memedetection.Retrofit;

import com.example.memedetection.Room.Images;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("/pingserver")
    Call<JsonObject> pingserver();


    @Multipart
    @POST("/predict")
    Call<List<ImagesJsonParser>> bulkImagesTest(@Part List<MultipartBody.Part> images, @Part("file") List<RequestBody> requestBody);


}
