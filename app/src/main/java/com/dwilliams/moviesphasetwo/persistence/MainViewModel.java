package com.dwilliams.moviesphasetwo.persistence;

import android.app.Application;
import android.util.Log;

import com.dwilliams.moviesphasetwo.dao.Movie;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {

    //Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> mFavorites;
    private MutableLiveData<List<Movie>> allMovies;
    private LiveData<List<Movie>> mPopularMovies;
    private LiveData<List<Movie>> mTopRatedMovies;
    private AppRepository appRepo;


    @Nullable
    private Integer menuId;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MainViewModel: Actively retrieving records from the Database");
        appRepo = AppRepository.getInstance(application);
        allMovies = new MutableLiveData<>();
        mPopularMovies = appRepo.getPopularMovies();
        mTopRatedMovies = appRepo.getTopRatedMovie();
        mFavorites = appRepo.geAllFavorites();

    }


    public LiveData<List<Movie>> getPopularMovies(){ return mPopularMovies; }

    public LiveData<List<Movie>> getmTopRatedMovies(){ return mTopRatedMovies; }

    public LiveData<List<Movie>> getFavoriteMovies(){ return mFavorites; }

    public void setmPopularMovies() {
        if(mPopularMovies != null){
            allMovies.setValue(mPopularMovies.getValue());
        } else {
            allMovies.setValue(new ArrayList<>());
        }

    }

    public void setmTopRatedMovies() {
        if(mTopRatedMovies != null){
            allMovies.setValue(mTopRatedMovies.getValue());
        } else {
            allMovies.setValue(new ArrayList<>());
        }
    }

    public void setmFavorites() {
        if(mFavorites != null){
            allMovies.setValue(mFavorites.getValue());
        } else {
            allMovies.setValue(new ArrayList<>());
        }
    }

    public LiveData<List<Movie>> getDefaultMovies() {

        if(allMovies.getValue() == null){
            Log.d(TAG, "getDefaultMovies: getting popular movies");
            allMovies.postValue(mPopularMovies.getValue());
        }

        return allMovies;
    }

    @Nullable
    public  Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId =  menuId;
    }
}
