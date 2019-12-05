package com.example.memedetection.Room;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Images image);

    @Query("DELETE FROM image_table")
    void deleteAll();

    @Query("DELETE FROM image_table WHERE memeStatus = :queryStatus")
    void deleteByStatus(String queryStatus);

    @Query("DELETE FROM image_table WHERE imagePath = :imagePathString")
    void deleteByImagePath(String imagePathString);

    @Query("SELECT * from image_table WHERE memeStatus = :queryText LIMIT 5")
    LiveData<List<Images>> getImageListByImageStatus(String queryText);

    @Query("SELECT * from image_table WHERE memeStatus != :queryText ")
    LiveData<List<Images>> getImageListByNotImageStatus(String queryText);

    @Query("SELECT * from image_table WHERE memeStatus = :queryText LIMIT 100")
    List<Images> getImageListByImageStatusNotCheck(String queryText);


    @Query("SELECT * from image_table WHERE imagePath IN (:queryList)")
    List<Images> getImageDataByImagePath(List<String> queryList);

    @Query("SELECT COUNT(imagePath) FROM image_table")
    int getRowCount();


}
