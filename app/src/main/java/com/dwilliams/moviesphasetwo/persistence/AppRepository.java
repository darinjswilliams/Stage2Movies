package com.dwilliams.moviesphasetwo.persistence;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.MovieList;
import com.dwilliams.moviesphasetwo.dto.MovieReviewLists;
import com.dwilliams.moviesphasetwo.dto.Review;
import com.dwilliams.moviesphasetwo.dto.Trailer;
import com.dwilliams.moviesphasetwo.dto.TrailerList;
import com.dwilliams.moviesphasetwo.networkUtils.MoviePlaceHolderApi;
import com.dwilliams.moviesphasetwo.networkUtils.RetrofitClient;
import com.dwilliams.moviesphasetwo.popularmovies.BuildConfig;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppRepository {
    private static AppRepository ourInstance;

    private static final String TAG = AppRepository.class.getSimpleName();

    private LiveData<List<Movie>> movie;
    private AppDatabase appDb;
    private MoviePlaceHolderApi moviePlaceHolderApi;
    private Retrofit retrofit;
    private Call<MovieList> call;
    private MutableLiveData<List<Integer>> mFavoriteIds = new MutableLiveData<>();
    private LiveData<List<Movie>> mMovieArrayList;
    private MutableLiveData<List<Movie>> mPopularArrayList = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> mRatedMovieList = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> mFavoriteMovieList = new MutableLiveData<>();

    private LiveData<List<Movie>> mMovieList;
    private MutableLiveData<List<Trailer>> mTrailerList = new MutableLiveData<>();
    private TrailerList trailerList;
    private MutableLiveData<List<Review>> mMovieReviewLists  = new MutableLiveData<>();
    private MutableLiveData<List<MovieReviewLists>> mReviewList = new MutableLiveData<>();
    private static final String consumerSecret = BuildConfig.CONSUMER_SECRET;
    private AppExecutors appExecutors;

    public static AppRepository getInstance(Application application) {
        if(ourInstance == null){
            ourInstance = new AppRepository(application.getApplicationContext());
        }

        return ourInstance;
    }

    private AppRepository(Context context) {
        appDb = AppDatabase.getsInstance(context);
        appExecutors = AppExecutors.getInstance();
        retrofit = RetrofitClient.getClient();
        moviePlaceHolderApi = retrofit.create(MoviePlaceHolderApi.class);
    }


    //** Retrofit Operations
    public LiveData<List<Movie>> getPopularMovies() {
        //Parse with retofit
        call = moviePlaceHolderApi.getPopularMovies(consumerSecret);


        //Place in background thread
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                Log.d(TAG, "onResponse: AppRepository success..");
                if(response.isSuccessful()){
                    MovieList  posts = response.body();
                    mPopularArrayList.postValue(posts.getResults());

//                    mPopularmoviesAdapter.setmPopularMoviesData(mMovieList);

                }

            }

            @Override
            public void onFailure(Call<MovieList>call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

        return mPopularArrayList;
    }


    public LiveData<List<Movie>> getTopRatedMovie() {
        //Parse with retofit

        call = moviePlaceHolderApi.getTopRatedMovies(consumerSecret);


        //Place in background thread
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                Log.d(TAG, "onResponse: AppRepository success..");
                if(response.isSuccessful()){
                    MovieList  posts = response.body();

                    mRatedMovieList.postValue(posts.getResults());

                }

            }

            @Override
            public void onFailure(Call<MovieList>call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

        return mRatedMovieList;
    }



    public LiveData<List<Trailer>> getTrailers(Integer movieId){
        Call<TrailerList> call = moviePlaceHolderApi.getMovieTrailers(movieId, consumerSecret);
        call.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                if(response.isSuccessful()){
                    TrailerList postTrailers =  response.body();

                    if (postTrailers != null) {
                        Log.d(TAG, "a trailer list has been received");
                        mTrailerList.postValue((response.body().getResults()));
//                         mTrailerList.addAll(response.body().getResults());
                        Log.d(TAG, "onResponse: size mTrailerList");
                    }

                }

            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {
                Log.d(TAG, "onFailure: getMovieTraillers " + t.getLocalizedMessage());

            }
        });

        return mTrailerList;

    }

    public LiveData<List<Review>> getMovieReviews(Integer movieId){

        Call<MovieReviewLists> call = moviePlaceHolderApi.getMovieReviews(movieId, consumerSecret);

        call.enqueue(new Callback<MovieReviewLists>() {
            @Override
            public void onResponse(Call<MovieReviewLists> call, Response<MovieReviewLists> response) {
                if(response.isSuccessful()){
                    MovieReviewLists reviewMovieTrailers  =  response.body();


                    if (reviewMovieTrailers != null) {
                        Log.d(TAG, "a Review list has been received");
                        mMovieReviewLists.postValue(response.body().getResults());

                    }

                }
            }

            @Override
            public void onFailure(Call<MovieReviewLists> call, Throwable t) {
                Log.d(TAG, "onFailure: getReviews " + t.getLocalizedMessage() );
            }
        });

        return mMovieReviewLists;
    }

    //*** Database Operations

    //Retrieve Movie
    //Room does back ground thread automatic, so do not call Executor
    public LiveData<List<Movie>> getMoviesFavorites(int movieId){
        Log.d(TAG, "getMoviesFavorites: calling from appRepo");

        return appDb.taskDao().loadTaskById(movieId);

    }

    public MutableLiveData<List<Integer>> getFavoriteMovieIds(){
        Log.d(TAG, "getFavoriteMovieIds: ");
        appExecutors.mDbExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mFavoriteIds.postValue(appDb.taskDao().getFavoriteMovieIds());
            }
        });
        return  mFavoriteIds;

    }


    public LiveData<List<Movie>> geAllFavorites(){
        Log.d(TAG, "getMoviesFavorites: calling from appRepo");

        return appDb.taskDao().getAll();

    }


    //INSERT Movie
    public void addFavoriteMovie(Movie movie){
        Log.d(TAG, "addFavoriteMovie: ");
        appExecutors.mDbExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDb.taskDao().insertTask(movie);
            }
        });

    }


    //Delete Movie
    public void removeFavoriteMovie(Movie movie){
        Log.d(TAG, "removeFavoriteMovie: ");
        appExecutors.mDbExecutor().execute(new Runnable() {
            @Override
            public void run() {
                appDb.taskDao().deleteTask(movie);
            }
        });

    }






}