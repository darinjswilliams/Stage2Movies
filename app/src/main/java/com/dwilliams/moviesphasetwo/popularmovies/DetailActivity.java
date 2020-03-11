package com.dwilliams.moviesphasetwo.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;



import com.dwilliams.moviesphasetwo.constants.Constants;
import com.dwilliams.moviesphasetwo.customarrayadapter.ReviewAdapter;
import com.dwilliams.moviesphasetwo.customarrayadapter.TrailerAdapter;
import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.Review;
import com.dwilliams.moviesphasetwo.dto.ReviewList;
import com.dwilliams.moviesphasetwo.dto.Trailer;
import com.dwilliams.moviesphasetwo.dto.TrailerList;
import com.dwilliams.moviesphasetwo.networkUtils.MoviePlaceHolderApi;
import com.dwilliams.moviesphasetwo.networkUtils.RetrofitClient;
import com.dwilliams.moviesphasetwo.persistence.AppDatabase;
import com.dwilliams.moviesphasetwo.persistence.AppExecutors;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.titleDetail)
    TextView titleTextView;
    @BindView(R.id.relDateDetail)
    TextView releaseDateTextView;
    @BindView(R.id.plotDetail)
    TextView plotTextView;
    @BindView(R.id.ratingDetail)
    TextView ratingTextView;
    @BindView(R.id.imageDetail)
    ImageView imageOfMovie;
    @BindView(R.id.recycler_view_trailers)
    RecyclerView recVwtrailers;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView recVwReviews;
    @BindView(R.id.favorite_star)
    ToggleButton favoriteBtn;


    private Movie popularMovie;
    private MoviePlaceHolderApi moviePlaceHolderApi;
    private static final String consumerSecret = BuildConfig.CONSUMER_SECRET;
    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;
    private TrailerAdapter mTrailersAdapter;
    private ReviewAdapter mReviewsAdapter;

    // Movie Database Instance
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Bind with ButterKnife
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        //Initialize Database
        mDb = AppDatabase.getsInstance(getApplicationContext());

        //Setup tool bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        Intent intent = getIntent();
        popularMovie = intent.getParcelableExtra(Constants.POPULAR_MOVIE);

        // Get Trailer and Reviews
        getMovieTrailersAndReviews(popularMovie.getId().toString());

        //Get data from Parcelable Object
        String movieTitle = popularMovie.getTitle();
        String moviePosterUrl = popularMovie.getPosterPath();
        String moviePlot = popularMovie.getOverview();
        String movieReleaseDate = popularMovie.getReleaseDate();
        String movieRating = popularMovie.getVoteAverage().toString();
        String movieId = popularMovie.getId().toString();


        int imageResourceId = getResources().getIdentifier(moviePosterUrl, "drawable", getPackageName());

        //USING BUTTER KNIFE TO BIND OBJECTS
        imageOfMovie.setImageResource(imageResourceId);
        titleTextView.setText(movieTitle);
        releaseDateTextView.setText(movieReleaseDate);
        plotTextView.setText(moviePlot);
        ratingTextView.setText(movieRating);

        //USING PICASSO TO LOAD IMAGES

        Log.d(TAG, "onCreate: " + moviePosterUrl);
        String mPopularMovie =   Constants.IMAGE_BASE_URL + Constants.IMAGE_SIZE
                + popularMovie.getPosterPath();

        Picasso.get()
                .load(mPopularMovie)
                .into(imageOfMovie);

        initTrailersUi(popularMovie);
        initReviewsUi(popularMovie);
        initFavoriteMovieUI();

    }

    private void initTrailersUi(Movie movie) {
        Log.d(TAG, "Setting up the Trailers view");
        // Trailers section
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recVwtrailers.setLayoutManager(layoutManager);
        recVwtrailers.setHasFixedSize(true);
        // Create the adapter
        mTrailersAdapter = new TrailerAdapter(this, null);
        recVwtrailers.setAdapter(mTrailersAdapter);
    }

    private void initReviewsUi(Movie movie) {
        Log.d(TAG, "Setting up the Trailers view");
        // Trailers section
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recVwReviews.setLayoutManager(layoutManager);
        recVwReviews.setHasFixedSize(true);
        // Create the adapter
        mReviewsAdapter = new ReviewAdapter(this, null);
        recVwReviews.setAdapter(mReviewsAdapter);
    }

    private void initFavoriteMovieUI(){
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

    private void onSaveButtonClicked(){
        Log.d(TAG, "onSaveButtonClicked: --> lets save data");

        AppExecutors.getInstance().mDbExecutor().execute(new Runnable() {
            @Override
            public void run() {


                if (favoriteBtn.isChecked()) {
                    Log.d(TAG, "run: movie exist updating");
                      int cnt = mDb.taskDao().doesExists(popularMovie.getId());
                      if(cnt > Constants.MOVIE_COUNT){
                          mDb.taskDao().updateTask(popularMovie);
                      }else {
                          mDb.taskDao().insertTask(popularMovie);
                      }
                } else {
                    mDb.taskDao().deleteTask(popularMovie);
                    Log.d(TAG, "run: remove from favorites");
                }
            }
        });
    }


    private void updateTrailers(TrailerList trailerList){
        mTrailersAdapter.updateTrailers(trailerList.getResults());
    }

    private void updateReviews(ReviewList reviewList){
        mReviewsAdapter.updateReviews(reviewList.getResults());
    }

    private void getMovieTrailersAndReviews(String movieId){

        Log.d(TAG, "doInBackground: getMovieTrailersAndReviews --> id " + movieId);
        //Retrofit to parse data
        Retrofit retrofit = RetrofitClient.getClient();

        Log.d(TAG, "doInBackground: after retrofit build");

        moviePlaceHolderApi = retrofit.create(MoviePlaceHolderApi.class);

        getTrailerDataFeed(movieId);
        getReviewDataFeed(movieId);

    }

    private void getTrailerDataFeed(String movieId){
        Log.d(TAG, "getVideoDataFeed: ");
//        Call<TrailerList> call = moviePlaceHolderApi.getMovieTrailers(movieId, consumerSecret);
        Call<com.dwilliams.moviesphasetwo.dto.TrailerList> call = moviePlaceHolderApi.getMovieTrailers(movieId, consumerSecret);
        call.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                if(response.isSuccessful()){
                    TrailerList postTrailers =  response.body();

                    if (postTrailers != null) {
                        Log.d(TAG, "a trailer list has been received");

                        updateTrailers(postTrailers);
                    }

                }

            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {
                Log.d(TAG, "onFailure: getMovieTraillers " + t.getLocalizedMessage());

            }
        });

    }

    private void getReviewDataFeed(String movieId){
        Log.d(TAG, "getReviewDataFeed: ");
        Call<ReviewList> call = moviePlaceHolderApi.getMovieReviews(movieId, consumerSecret);

        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if(response.isSuccessful()){
                    ReviewList posts =  response.body();
                    mReviewList = new ArrayList<Review>();

                    for (Review post : posts.getResults()){
                        Log.d(TAG, "onResponse: getReviewDataFeed" + post.getAuthor());
                        mReviewList.add(post);
                    }

                    if (mReviewList != null) {
                        Log.d(TAG, "a Review list has been received");
                        updateReviews(posts);
                    }

                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                Log.d(TAG, "onFailure: getReviews " + t.getLocalizedMessage() );
            }
        });
    }

}
