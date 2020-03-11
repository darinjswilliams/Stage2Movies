package com.dwilliams.moviesphasetwo.customarrayadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwilliams.moviesphasetwo.constants.Constants;
import com.dwilliams.moviesphasetwo.dao.Movie;

import com.dwilliams.moviesphasetwo.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPopularMovieAdapter extends RecyclerView.Adapter<MyPopularMovieAdapter.MyMovieHolder> {

    private static final String TAG = MyPopularMovieAdapter.class.getSimpleName();

    private Context ctx;

    //Define Arraylist of Movie Objects
    private List<Movie> mMoviesData;
    private MyPopularMovieAdapterOnClickHandler myPopularMovieClickHandler;



    public MyPopularMovieAdapter(Context ctx, MyPopularMovieAdapterOnClickHandler myPopularMovieAdapterOnClickHandler) {
        this.ctx = ctx;
        this.myPopularMovieClickHandler = myPopularMovieAdapterOnClickHandler;
        this.mMoviesData = new ArrayList<>();
    }

    public interface MyPopularMovieAdapterOnClickHandler {
        void onClick(Movie myMovie);
    }

    @NonNull
    @Override
    public MyMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        LayoutInflater myInflater = LayoutInflater.from(ctx);
        View myOwnView = myInflater.inflate(R.layout.movie_image, parent, false);
        return new MyMovieHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMovieHolder holder, int position) {


        //Get movie object
        Movie popularMovies = mMoviesData.get(position);
        Log.d(TAG, "onBindViewHolder:  Call Piscasso lib" + popularMovies.getPosterPath());

        String popularMovie = Constants.IMAGE_BASE_URL + Constants.IMAGE_SIZE
                                    + popularMovies.getPosterPath();
        //Bind Data
        Picasso.get()
                .load(popularMovie)
                .into(holder.popularMovieImage);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mMoviesData.size());
        return (mMoviesData != null ?  mMoviesData.size() : 0);
    }

    public class MyMovieHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView popularMovieImage;

        public MyMovieHolder(@NonNull View itemView) {
            super(itemView);

            popularMovieImage = (ImageView) itemView.findViewById(R.id.myImageView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            Movie myMovie = mMoviesData.get(index);
            myPopularMovieClickHandler.onClick(myMovie);
        }

        public Movie getTitleMovieId(){
            int index = getLayoutPosition();
            Movie myMovie = mMoviesData.get(index);
            return myMovie;
        }
    }

    public void setmPopularMoviesData(List<Movie> mPopularMoviesData) {
        Log.d(TAG, "onPostExecute:Count of Data passed in.." + mPopularMoviesData.size());
        this.mMoviesData.clear();
        this.mMoviesData.addAll(mPopularMoviesData);
        notifyDataSetChanged();
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     * This will populate the favorites activities
     */
//    public void setTasks(List<MovieEntry> taskEntries) {
//        mTaskEntries = taskEntries;
//        notifyDataSetChanged();
//    }



}
