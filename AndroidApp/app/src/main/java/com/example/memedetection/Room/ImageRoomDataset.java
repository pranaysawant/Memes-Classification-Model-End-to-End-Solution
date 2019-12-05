package com.example.memedetection.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.memedetection.Repository.ImageRepo;

@Database(entities = {Images.class}, version = 1, exportSchema=true)
public abstract class ImageRoomDataset extends RoomDatabase {

    private static volatile ImageRoomDataset INSTANCE;

    public static ImageRoomDataset getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ImageRoomDataset.class) {
                if (INSTANCE == null) {
                    // Create database here

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImageRoomDataset.class, "image_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract ImageDao imageDao();


//    private static RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen (@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//                    new PopulateDbAsync(INSTANCE).execute();
//                }
//            };


//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final ImageDao mDao;
//
//        PopulateDbAsync(ImageRoomDataset db) {
//            mDao = db.imageDao();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
////            Images image = new Images("Hello","not_checked");
////            mDao.insert(image);
////            image = new Images("World","not_checked");
////            mDao.insert(image);
//            Images image = new Images("Hello111111111","not_checked");
////            image.setmImagePath("");
////            image.setmImagePath("not_checked");
//            mDao.insert(image);
//
//
//            return null;
//        }
//    }

}
