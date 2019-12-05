package com.example.memedetection.Room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.example.memedetection.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

@Entity(tableName = "image_table")

public class Images {


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imagePath")
    private String imagePath;

    @Ignore
    public Images(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }


    public Images(@NonNull String imagePath, String imageMemeStatus) {
        this.imagePath = imagePath;
        this.imageMemeStatus=imageMemeStatus;
    }


    @NonNull
    public String getImagePath() {
        return imagePath;
    }


    @ColumnInfo(name = "memeStatus",defaultValue = IMAGE_STATUS_NOT_CHECKED)
    private String imageMemeStatus;


    public String getImageMemeStatus() {
        return imageMemeStatus;
    }
}
