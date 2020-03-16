package com.dwilliams.moviesphasetwo.persistence;

import android.content.Context;
import android.util.Log;

import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dao.MovieDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


//ADD THE TYPE CONVERTERS SO ROOM KNOWS HOW TO DEAL WITH DATE CONVERSION
@Database(entities = {Movie.class}, version = 1,  exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "PopularMovies";
    private  static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG,   "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        //TODO -  Queries should be done in a separate thread to avoid locking the UI
                        //We will allow this only temporally to see that our db is working
                        //remove allowMainThreadQueries after verification.
//                        .fallbackToDestructiveMigration()
//                        .allowMainThreadQueries()
                        .build();
            }
        }

        Log.d(LOG_TAG, "GETTING DATABASE INSTANCE");
        return sInstance;
    }

    public abstract MovieDao taskDao();
}
