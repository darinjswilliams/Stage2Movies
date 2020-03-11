package com.dwilliams.moviesphasetwo.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
import com.dwilliams.moviesphasetwo.dao.Movie;
import com.dwilliams.moviesphasetwo.dto.MovieList;
import com.dwilliams.moviesphasetwo.customarrayadapter.MyPopularMovieAdapter;
import com.dwilliams.moviesphasetwo.networkUtils.MoviePlaceHolderApi;
import com.dwilliams.moviesphasetwo.networkUtils.RetrofitClient;
import com.dwilliams.moviesphasetwo.persistence.AppDatabase;
import com.dwilliams.moviesphasetwo.persistence.AppExecutors;
import com.dwilliams.moviesphasetwo.persistence.MainViewModel;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.google.gson.reflect.TypeToken.get;

public class MainActivity extends AppCompatActivity implements MyPopularMovieAdapter.MyPopularMovieAdapterOnClickHandler{

    private  static  final String TAG = MainActivity.class.getSimpleName();

    //Initialize adapter
    private MyPopularMovieAdapter mPopularmoviesAdapter;
    private static MyPopularMovieAdapter.MyPopularMovieAdapterOnClickHandler myClickHandler;
    private RecyclerView myRecyclerView;
    private RelativeLayout mRelativeLayout;
    private MoviePlaceHolderApi moviePlaceHolderApi;
    private static final String consumerSecret = BuildConfig.CONSUMER_SECRET;
    private Call<MovieList> call;
    private List<Movie> mMovieList;

    //keys
    private static final String MOVIE_KEY = "movielist";
    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";

    //TODO - ADD MEMBER VARIABLE FOR THE DATABASE
    private AppDatabase mDb;

    //ViewModel
    private MainViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO - Get instance of Database
        mDb = AppDatabase.getsInstance(getApplicationContext());

        //ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        myRecyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutId);

        /**
         * SET TO GRIDLAYOUT WITH A SPAN OF 2 COLUMNS
         *
         */

        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mPopularmoviesAdapter = new MyPopularMovieAdapter(this,
                (MyPopularMovieAdapter.MyPopularMovieAdapterOnClickHandler) this);

        myRecyclerView.setAdapter(mPopularmoviesAdapter);


        //runData on backround thread fist to get default data
        loadMovieData(Constants.POPULARITY_PARAM);

        //Get Instance of Database
        mDb = AppDatabase.getsInstance(getApplicationContext());

        //Initialize View Model
        initViewModel();

    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        retrieveFavorites();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(!mMovieList.isEmpty()){
            outState.putParcelableArrayList(MOVIE_KEY, (ArrayList<? extends Parcelable>) mMovieList);
        }

        outState.putParcelableArrayList(ON_SAVE_INSTANCE_STATE, (ArrayList<? extends Parcelable>) mViewModel.getMovie().getValue());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Context context = MainActivity.this;

        switch(item.getItemId()){

            case R.id.action_sortPopular:
                loadMovieData(Constants.POPULARITY_PARAM);
                return true;

            case R.id.action_sortRating:
                loadMovieData(Constants.RATNGS_PARAM);
                return true;

            case R.id.action_favorites:
                retrieveFavorites();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovieData(String sortParam){
        myRecyclerView.setVisibility(View.VISIBLE);

        if(checkInternetConnection(this)) {
            Log.i(TAG, "loadMovieData: Calling Aysnc");
             fetchPopularMovie(sortParam);
        }
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
//Todo - live data retrieve
    private void setUpViewModel(){
//        MainViewModel viewModel = ViewModelProviders.of(activity: this).get(MainViewModel.class);
//          viewModel.getTasks.observe(owner: this, (taskEntries) -> {
//              Log.d(TAG, msg: "Receiving database upate from LiveData");
//              mAdapter.setTask(taskEntries);
        }


    public void retrieveFavorites() {
        
        //LiveData Runs outside of main thread
        //Use Executors for insert, update, and delete operations

//        final LiveData<List<Movie>> fetchedMovies = mDb.taskDao().getAll();
        mViewModel.getMovie().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d(TAG, "onChanged: Receiving database update from LiveData in ViewModel");
                mPopularmoviesAdapter.setmPopularMoviesData(movies);
            }
        });

    }


    public void fetchPopularMovie(String sortParam){

        Log.d(TAG, "doInBackground: before retrofit build call");
        //Retrofit to parse data
        Retrofit retrofit = RetrofitClient.getClient();

        Log.d(TAG, "doInBackground: after retrofit build");

        moviePlaceHolderApi = retrofit.create(MoviePlaceHolderApi.class);

        getPosts(sortParam);
    }


    private void getPosts(String sortParam){

        switch(sortParam){
            case Constants.POPULARITY_PARAM:
                Log.d(TAG, "getPosts: calling getPopularMovies");
                call = moviePlaceHolderApi.getPopularMovies(consumerSecret);
                break;

            case Constants.RATNGS_PARAM:
                Log.d(TAG, "getPosts: calling getTopRatedMovie");
                call = moviePlaceHolderApi.getTopRatedMovies(consumerSecret);
                break;

            default:
                Log.d(TAG, "getPosts: calling default getPopular");
                call = moviePlaceHolderApi.getPopularMovies(consumerSecret);

        }

        Log.d(TAG, "getPosts: inside getPosts" + consumerSecret);

        //Place in background thread
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                if(response.isSuccessful()){
                    MovieList  posts = response.body();
                    mMovieList = new ArrayList<Movie>();
                    mMovieList.addAll(posts.getResults());
                    mPopularmoviesAdapter.setmPopularMoviesData(mMovieList);

                }

            }

            @Override
            public void onFailure(Call<MovieList>call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });

    }



    private class SampleDumperPluginsProvier implements DumperPluginsProvider {
        private final Context mContext;

        public SampleDumperPluginsProvier(Context context) {
            mContext = context;
        }

        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<>();
            for(DumperPlugin defaultPlugin: Stetho.defaultDumperPluginsProvider(mContext).get()){
                plugins.add(defaultPlugin);
            }
//            plugins.add(new MyPopularMoviePlugin());
            return plugins;
        }
    }
}
