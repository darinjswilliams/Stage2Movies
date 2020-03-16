package com.dwilliams.moviesphasetwo.persistence;

import android.util.Log;

import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.Review;
import com.dwilliams.moviesphasetwo.dto.Trailer;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {

    private final String TAG = DetailViewModel.class.getName();

    private MutableLiveData<List<Integer>> mMovieFavoriteIds;
    private LiveData<List<Review>> mMovieReviews;
    private LiveData<List<Trailer>> mMovieTrailers;
    private AppRepository appRepo;


    public DetailViewModel( AppRepository appRepo, Integer id) {
        this.appRepo = appRepo;
        this.mMovieFavoriteIds = appRepo.getFavoriteMovieIds();
        this.mMovieReviews =  appRepo.getMovieReviews(id);
        this.mMovieTrailers = appRepo.getTrailers(id);

    }


    public LiveData<List<Review>> getmMovieReviews() {
        Log.d(TAG, "getmMovieReviews: ");
        return mMovieReviews;
    }

    public LiveData<List<Trailer>> getmMovieTrailers() {
        Log.d(TAG, "getmMovieTrailers:  ");
        return mMovieTrailers;
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
