package com.example.memedetection.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.memedetection.Room.ImageDao;
import com.example.memedetection.Room.ImageRoomDataset;
import com.example.memedetection.Room.Images;

import java.util.List;

import static com.example.memedetection.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImageRepo {

    private ImageDao mImageDao;
    private LiveData<List<Images>> mAllImagesByImageStauts;



    public ImageRepo(Application application) {
        ImageRoomDataset db = ImageRoomDataset.getDatabase(application);
        mImageDao = db.imageDao();
//        mAllNotCheckedimages = mImageDao.getNotCheckedImageList("not_checked");

    }

    public List<Images> getmAllImagesByImagesList(List<String> imagePathList) {
        return mImageDao.getImageDataByImagePath(imagePathList);
    }

    public LiveData<List<Images>> getmAllImagesByImageStauts(String queryStatus) {
        mAllImagesByImageStauts = mImageDao.getImageListByImageStatus(queryStatus);

        return mAllImagesByImageStauts;
    }

    public LiveData<List<Images>> getmAllImagesExceptQueryStatus(String queryStatus) {
        return mImageDao.getImageListByNotImageStatus(queryStatus);
    }

    public List<Images> getImageListByImageStatusNotCheck() {
        return mImageDao.getImageListByImageStatusNotCheck(IMAGE_STATUS_NOT_CHECKED);
    }

    public void deleteImagesByImagePath(String path){
        mImageDao.deleteByImagePath(path);
    }


    public void insert (Images word) {
        new insertAsyncTask(mImageDao).execute(word);
    }

    public void deleteByStatus(String status){

        new deleteAsyncTask(mImageDao).execute(status);

//        mImageDao.deleteByStatus(status);
    }



    public int getRowCount() {
        return mImageDao.getRowCount();
    }

    private static class insertAsyncTask extends AsyncTask<Images, Void, Void> {

        private ImageDao mAsyncTaskDao;

        insertAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Images... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

        private ImageDao mAsyncTaskDao;

        deleteAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteByStatus(params[0]);
            return null;
        }
    }
}
