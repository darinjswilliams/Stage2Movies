package com.dwilliams.moviesphasetwo.customarrayadapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwilliams.moviesphasetwo.dto.Trailer;
import com.dwilliams.moviesphasetwo.networkUtils.StreamMoviesUtils;
import com.dwilliams.moviesphasetwo.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyTrailerHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context ctx;

    //Define Arraylist of Movie Objects
    private List<Trailer> mTrailerData;

    public TrailerAdapter(Context ctx, List<Trailer> trailers) {
        this.ctx = ctx;
        this.mTrailerData = trailers;
    }

    public TrailerAdapter(List<Trailer> mTrailerList) {
        this.mTrailerData = mTrailerList;
    }

    @NonNull
    @Override
    public MyTrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: TrailerAdapter ");
        LayoutInflater myInflater = LayoutInflater.from(ctx);
        View myOwnView = myInflater.inflate(R.layout.trailer_menu_thumbnail, parent, false);
        return new MyTrailerHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTrailerHolder holder, int position) {

        // movie title
        holder.trailerID.setText(mTrailerData.get(position).getName());
        // Show movie poster image
        String thumbnailPath = "http://img.youtube.com/vi/" + mTrailerData.get(position).getKey() + "/0.jpg";

        Picasso.get()
                .load(thumbnailPath)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading_error)
                .into(holder.popularMovieImage);
    }

    @Override
    public int getItemCount() {

        if(mTrailerData == null) return 0;
        return mTrailerData.size();
    }

    //Notified RecycleView Change
    public void setTrailers(List<Trailer> trailers) {
        Log.d(TAG, "setTrailers: ");
        mTrailerData = trailers;
        notifyDataSetChanged();
    }


    public class MyTrailerHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{


        @BindView(R.id.movie_trailer_thumbnail_image)
        ImageView popularMovieImage;
        @BindView(R.id.movie_trailer_thumbnail_title)
        TextView trailerID;

        public MyTrailerHolder(@NonNull View itemView) {
            super(itemView);

            //Bind the View with ButterKnife
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            Trailer mTrailer = mTrailerData.get(index);


            try {

                Log.d(TAG, "onClick: key to launch youtube -->" + mTrailer.getKey());
                StreamMoviesUtils.launchTrailerVideoInYoutubeApp(ctx, mTrailer.getKey());

            }catch(ActivityNotFoundException ane){

                Log.d(TAG, "onClick: key to launch youtube failure -->" + ane.getLocalizedMessage());
                StreamMoviesUtils.launchTrailerVideoInYoutubeBrowser(ctx, mTrailer.getKey());
            }

        }

    }

}
