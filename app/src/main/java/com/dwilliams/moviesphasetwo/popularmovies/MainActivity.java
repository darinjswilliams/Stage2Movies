package com.dwilliams.moviesphasetwo.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwilliams.moviesphasetwo.constants.Constants;
import com.dwilliams.moviesphasetwo.customarrayadapter.MyPopularMovieAdapter;
import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.MovieList;
import com.dwilliams.moviesphasetwo.networkUtils.MoviePlaceHolderApi;
import com.dwilliams.moviesphasetwo.persistence.AppRepository;
import com.dwilliams.moviesphasetwo.persistence.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;


public class MainActivity extends AppCompatActivity implements MyPopularMovieAdapter.MyPopularMovieAdapterOnClickHandler{

    private  static  final String TAG = MainActivity.class.getSimpleName();

    //Initialize adapter
    private MyPopularMovieAdapter mPopularmoviesAdapter;
    private static MyPopularMovieAdapter.MyPopularMovieAdapterOnClickHandler myClickHandler;
    private RecyclerView myRecyclerView;
    private RelativeLayout mRelativeLayout;
    private MoviePlaceHolderApi moviePlaceHolderApi;
    private Call<MovieList> call;
    private List<Movie> mMovieList = new ArrayList<>();
    private  List<Movie> mMovieSelection = new ArrayList<>();
    private AppRepository appRepository;

    //keys
    private static final String MOVIE_KEY = "movielist";
    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";
    Parcelable listState;
    int menuItemId;
    String mFavs;

    //ViewModel
    private MainViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize  Recycler and View Model
        initRecycleView();

        initViewModel();


        Log.d(TAG, "onCreate: view model " + mViewModel.getDefaultMovies().getValue());

        if (savedInstanceState != null) {

            mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            listState = savedInstanceState.getParcelable(ON_SAVE_INSTANCE_STATE);
            displayMovies();

        } else {

            mViewModel.getPopularMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> movies) {
                    Log.d(TAG, "onChanged: inside onCreate -> mViewModel observer");

                            mMovieList.addAll(movies);
                            mPopularmoviesAdapter.setMovies(movies);
                            myRecyclerView.setAdapter(mPopularmoviesAdapter);

                }
            });
        }
    }

    private void displayMovies() {
        mPopularmoviesAdapter.setMovies(mMovieList);
        myRecyclerView.setAdapter(mPopularmoviesAdapter);
        myRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        mPopularmoviesAdapter.notifyDataSetChanged();
    }

    private void initRecycleView() {
        myRecyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutId);

        // SET TO GRIDLAYOUT WITH A SPAN OF 2 COLUMNS
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mPopularmoviesAdapter = new MyPopularMovieAdapter(this,
                (MyPopularMovieAdapter.MyPopularMovieAdapterOnClickHandler) this);


        myRecyclerView.setAdapter(mPopularmoviesAdapter);
    }

    private void initViewModel() {
        Log.d(TAG, "initViewModel: called");
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: called");
        super.onSaveInstanceState(outState);

        listState = myRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelableArrayList(MOVIE_KEY, (ArrayList<? extends Parcelable>) mMovieList);
        outState.putParcelable(ON_SAVE_INSTANCE_STATE, listState);

    }

    // Restore list state from bundle
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        Log.d(TAG, "onRestoreInstanceState: called");

        if(state != null) {
            listState = state.getParcelable(ON_SAVE_INSTANCE_STATE);
            mMovieList = state.getParcelableArrayList(MOVIE_KEY);
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(listState != null){
            displayMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Keep track of menu item choosen
    if(checkInternetConnection(this.getApplicationContext())) {

        menuItemId = item.getItemId();

     switch (menuItemId) {


         case R.id.action_sortPopular:
             mViewModel.getPopularMovies().observe(this, mList -> {

                 if(mMovieList != null){
                     mMovieList.clear();
                 }
                     mMovieList.addAll(mList);

                 mPopularmoviesAdapter.setMovies(mList);
                 myRecyclerView.setAdapter(mPopularmoviesAdapter);


             });
             return true;

         case R.id.action_sortRating:

             mViewModel.getmTopRatedMovies().observe(this, mList -> {
                 if(mMovieList != null){
                     mMovieList.clear();
                 }

                 mMovieList.addAll(mList);

                 mPopularmoviesAdapter.setMovies(mList);
                 myRecyclerView.setAdapter(mPopularmoviesAdapter);
             });
             return true;

         case R.id.action_favorites:

             mViewModel.getFavoriteMovies().observe(this, mList -> {
                 if(mMovieList != null){
                     mMovieList.clear();
                 }
                 mMovieList.addAll(mList);

                 mPopularmoviesAdapter.setMovies(mList);
                 myRecyclerView.setAdapter(mPopularmoviesAdapter);
             });

             return true;


         default:
             return super.onOptionsItemSelected(item);
      }
     }
        return true;
    }



    private boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v(TAG, "Internet Connection Not Present");

            Snackbar snackbar = Snackbar
                    .make(mRelativeLayout, "Internet Connection Not Present", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener(){
                        public void onClick(View view){

                        }

                    });

            snackbar.setActionTextColor(Color.RED);

            //Changing Action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();


            return false;
        }
    }


    @Override
    public void onClick(Movie myMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.POPULAR_MOVIE, myMovie);
        startActivity(intent);
    }


}
