package com.dwilliams.moviesphasetwo.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewList implements Parcelable {

    @SerializedName("id")
    @Expose
    private final Integer id;

    @SerializedName("page")
    @Expose
    private final Integer page;

    @SerializedName("results")
    @Expose
    private final List<Review> results;

    public List<Review> getResults() {
        return results;
    }


    private ReviewList(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.results = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<ReviewList> CREATOR = new Creator<ReviewList>() {
        @Override
        public ReviewList createFromParcel(Parcel in) {
            return new ReviewList(in);
        }

        @Override
        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.page);
        dest.writeTypedList(this.results);
    }
}
