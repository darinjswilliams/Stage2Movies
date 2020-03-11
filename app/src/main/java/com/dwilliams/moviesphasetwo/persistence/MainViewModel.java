package com.dwilliams.moviesphasetwo.persistence;

import android.app.Application;
import android.util.Log;

import com.dwilliams.moviesphasetwo.dao.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    //Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movie;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Actively retrieving records from the Database");
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        movie = database.taskDao().getAll();
    }

    public LiveData<List<Movie>> getMovie(){
        return movie;
    }
}
