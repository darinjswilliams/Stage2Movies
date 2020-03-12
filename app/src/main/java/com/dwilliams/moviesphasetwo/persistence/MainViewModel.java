package com.dwilliams.moviesphasetwo.persistence;

import android.app.Application;
import android.util.Log;

import com.dwilliams.moviesphasetwo.customarrayadapter.MyPopularMovieAdapter;
import com.dwilliams.moviesphasetwo.dao.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    //Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movie;
    private MyPopularMovieAdapter mPopularmoviesAdapter;

    @Nullable
    private Integer menuId;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Actively retrieving records from the Database");
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        movie = database.taskDao().getAll();
        mPopularmoviesAdapter = new MyPopularMovieAdapter();
    }

    public LiveData<List<Movie>> getMovie(){
        return movie;
    }

    @Nullable
    public  Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId =  menuId;
    }
}
