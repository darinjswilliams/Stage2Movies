package com.dwilliams.moviesphasetwo.customarrayadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwilliams.moviesphasetwo.dto.Review;
import com.dwilliams.moviesphasetwo.popularmovies.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyTrailerHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private Context ctx;

    //Define Arraylist of Review Objects
    private List<Review> mReviewData;


    public ReviewAdapter(Context ctx, List<Review> mReviewData) {
        this.ctx = ctx;
        this.mReviewData = mReviewData;
    }

    @NonNull
    @Override
    public MyTrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        LayoutInflater myInflater = LayoutInflater.from(ctx);
        View myOwnView = myInflater.inflate(R.layout.review_movies_item, parent, false);
        return new MyTrailerHolder(myOwnView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyTrailerHolder holder, int position) {

        //lets set the author and content or review
        holder.reviewHeader.setText(mReviewData.get(position).getAuthor());
        holder.reviewHeader.setText(mReviewData.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        if(mReviewData == null) { return 0; }
        return mReviewData.size();
    }

    //NotifyDataSetChanged in RecycleView
    public void setReviews(List<Review> reviews) {
        Log.d(TAG, "setReviews: ");
        mReviewData = reviews;
        notifyDataSetChanged();
    }


    public class MyTrailerHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.review_header)
        TextView reviewHeader;
        @BindView(R.id.review_content)
        TextView reviewContent;


        public MyTrailerHolder(@NonNull View itemView) {
            super(itemView);

            //Bind the View with ButterKnife
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "MyTrailerHolder: -->  onClickView");
            //Get a context and get position of adapter from RecycleView
            Context context = itemView.getContext();
            int postion = this.getAdapterPosition();

            Review mReviewHolder = mReviewData.get(postion);
        }


    }
}
