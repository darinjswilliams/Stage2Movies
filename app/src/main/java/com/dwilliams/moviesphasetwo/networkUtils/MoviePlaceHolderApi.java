package com.dwilliams.moviesphasetwo.networkUtils;

import com.dwilliams.moviesphasetwo.dto.MovieList;
import com.dwilliams.moviesphasetwo.dto.MovieReviewLists;
import com.dwilliams.moviesphasetwo.dto.TrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviePlaceHolderApi {

    @GET("top_rated")
    Call<MovieList>getTopRatedMovies(@Query("api_key") String consumerSecret);

    @GET("popular")
    Call<MovieList>getPopularMovies(@Query("api_key") String consumerSecret);


    @GET("{movie_id}/videos")
    Call<TrailerList>getMovieTrailers(@Path("movie_id") Integer movie_id, @Query("api_key") String consumerSecret);


    @GET("{movie_id}/reviews")
    Call<MovieReviewLists>getMovieReviews(@Path("movie_id") Integer movie_id, @Query("api_key") String consumerSecret);


}
