package com.dwilliams.moviesphasetwo.persistence;

import android.app.Application;
import android.util.Log;

import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.Review;
import com.dwilliams.moviesphasetwo.dto.Trailer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DetailViewModel extends AndroidViewModel {

    private final String TAG = DetailViewModel.class.getName();

    private MutableLiveData<List<Integer>> mMovieFavoriteIds;
    private LiveData<List<Review>> mMovieReviews;
    private LiveData<List<Trailer>> mMovieTrailers;
    private AppRepository appRepo;


    public DetailViewModel(@NonNull Application application) {
        super(application);
        this.appRepo = AppRepository.getInstance(application);
        this.mMovieFavoriteIds = appRepo.getFavoriteMovieIds();
    }

    public LiveData<List<Review>> getmMovieReviews(Integer movieId) {
        Log.d(TAG, "getmMovieReviews: ");
        return appRepo.getMovieReviews(movieId);
    }

    public LiveData<List<Trailer>> getmMovieTrailers(Integer movieId) {
        Log.d(TAG, "getmMovieTrailers:  ");
        return appRepo.getTrailers(movieId);
    }


    public  LiveData<List<Integer>> getmMovieFavoriteIds(){
        if(mMovieFavoriteIds.getValue() == null){
            mMovieFavoriteIds.postValue( new ArrayList<>());
        }

        return  mMovieFavoriteIds;
    }


    public void addFavoriteMovie(Movie popularMovie) {
        appRepo.addFavoriteMovie(popularMovie);
    }

    public void deleteFavoriteMovie(Movie popularMovie) {
        appRepo.removeFavoriteMovie(popularMovie);
    }
}
