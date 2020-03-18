package com.dwilliams.moviesphasetwo.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dwilliams.moviesphasetwo.constants.Constants;
import com.dwilliams.moviesphasetwo.customarrayadapter.ReviewAdapter;
import com.dwilliams.moviesphasetwo.customarrayadapter.TrailerAdapter;
import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.MovieReviewLists;
import com.dwilliams.moviesphasetwo.dto.Review;
import com.dwilliams.moviesphasetwo.dto.Trailer;
import com.dwilliams.moviesphasetwo.dto.TrailerList;
import com.dwilliams.moviesphasetwo.networkUtils.MoviePlaceHolderApi;
import com.dwilliams.moviesphasetwo.persistence.DetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


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
    private DetailViewModel mViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Bind with ButterKnife
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);


        Intent intent = getIntent();
        popularMovie = intent.getParcelableExtra(Constants.POPULAR_MOVIE);

        //Initialize ViewModel
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

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


        // Get Trailer and Reviews
        getMovieTrailersAndReviews(popularMovie.getId());

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
//        Boolean isMovieFavorite = false;

        // Observe Checkbox
        mViewModel.getmMovieFavoriteIds().observe(this, mFavoriteIds -> {
            Boolean isMovieFavorite = false;

            if(mFavoriteIds != null && mFavoriteIds.contains(popularMovie.getId())){
                Log.d(TAG, "initFavoriteMovieUI: move is set as favorite");
                isMovieFavorite = true;
            }

            favoriteBtn.setChecked(isMovieFavorite);
            Log.d(TAG, "initFavoriteMovieUI: button value" + isMovieFavorite.toString());

            favoriteBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Log.d(TAG, "onClick: adding favorite");
                        mViewModel.addFavoriteMovie(popularMovie);
                    }else{
                        Log.d(TAG, "onClick: removing favorite");
                        mViewModel.deleteFavoriteMovie(popularMovie);
                    }

                }
            });
        });
    }


    private void updateTrailers(TrailerList trailerList){
        Log.d(TAG, "setTrailers: count of trailers ");
        mTrailersAdapter.setTrailers(trailerList.getResults());
    }


    private void updateReviews(MovieReviewLists reviewList){
        mReviewsAdapter.setReviews(reviewList.getResults());
    }

    private void getMovieTrailersAndReviews(Integer movieId){

        Log.d(TAG, "doInBackground: getMovieTrailersAndReviews --> id " + movieId);

        getTrailerDataFeed(movieId);
        getReviewDataFeed(movieId);

    }

    private void getTrailerDataFeed(Integer movieId){


        Log.d(TAG, "getVideoDataFeed: " + movieId);
        mViewModel.getmMovieTrailers(movieId).observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                mTrailersAdapter.setTrailers(trailers);
            }
        });
    }

    private void getReviewDataFeed(Integer movieId){
        Log.d(TAG, "getReviewDataFeed: ");

        mViewModel.getmMovieReviews(movieId).observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                mReviewsAdapter.setReviews(reviews);
            }
        });
    }

}
