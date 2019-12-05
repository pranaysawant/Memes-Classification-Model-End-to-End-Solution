package com.example.memedetection.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesJsonParser {

    @Expose
    @SerializedName("imagePath")
    private String imagePath;

    @SerializedName("memeStatus")
    @Expose
    private String imageMemeStatus;

    @SerializedName("response_message")
    @Expose
    private String responseMessage;

    @SerializedName("response_code")
    @Expose
    private String responseCode;

    @SerializedName("response_root")
    @Expose
    private String responseRoot;

    public String getImagePath() {
        return imagePath;
    }

    public String getImageMemeStatus() {
        return imageMemeStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseRoot() {
        return responseRoot;
    }
}
