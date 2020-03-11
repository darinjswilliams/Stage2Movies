package com.dwilliams.moviesphasetwo.customarrayadapter;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularMovies implements Parcelable {

    String title;
    String posterImage;
    String plotSynopsis;
    String userRating;
    String releaseDate;
    private Integer popularity;
    private Integer rating;
    private String movieId;


    public PopularMovies(){ }

    public PopularMovies(String title, String posterImage, String plotSynopsis, String userRating, String releaseDate, String movieId) {
        this.title = title;
        this.posterImage = posterImage;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    protected PopularMovies(Parcel source){
        this.title = source.readString();
        this.posterImage = source.readString();
        this.plotSynopsis = source.readString();
        this.userRating = source.readString();
        this.releaseDate = source.readString();
        this.movieId = source.readString();

        if(source.readByte() == 0){
            popularity = null;
        } else {
            popularity = source.readInt();
        }

        if(source.readByte() == 0){
            rating = null;
        } else {
            rating = source.readInt();
        }


    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(posterImage);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
        if (isNullData(popularity)){
              dest.writeByte((byte) 0);
        }  else {
            dest.writeByte((byte) 1);
            dest.writeInt(popularity);
        }

        if (isNullData(rating)) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rating);
        }
        dest.writeString(movieId);
    }

    private boolean isNullData(Integer data){
       return data == null;
    }

    public static final Creator<PopularMovies> CREATOR = new Creator<PopularMovies>() {

        @Override
        public PopularMovies createFromParcel(Parcel source) {
            return new PopularMovies(source);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };
}

