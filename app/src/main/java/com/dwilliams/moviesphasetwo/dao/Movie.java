package com.dwilliams.moviesphasetwo.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.dwilliams.moviesphasetwo.constants.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movie")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    private Integer id;

    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    @Expose
    private Integer voteCount;

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    @Expose
    private Double voteAverage;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    @Expose
    private String title;

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    @Expose
    private String posterPath;

    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    @Expose
    private String originalTitle;

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    @Expose
    private String releaseDate;

    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public Movie() {
    }

    /**
     * @param genreIds
     * @param id
     * @param title
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param originalTitle
     * @param voteAverage
     * @param backdropPath
     * @param voteCount
     * @param popularity
     */
    @Ignore
    public Movie(Integer voteCount, Integer id, Double voteAverage, String title, Double popularity, String posterPath, String originalTitle, List<Integer> genreIds, String backdropPath, String overview, String releaseDate) {
        super();
        this.voteCount = voteCount;
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = Constants.IMAGE_BASE_URL + Constants.IMAGE_SIZE + posterPath;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    /**
     * This is the (temporal) Movie constructor used by Room
     *
     * @param id
     * @param title
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param originalTitle
     * @param voteAverage
     * @param backdropPath
     * @param voteCount
     * @param popularity
     */
    public Movie(Integer id, Integer voteCount, Double voteAverage, String title, Double popularity, String posterPath, String originalTitle, String backdropPath, String overview, String releaseDate) {
        super();
        this.id = id;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.genreIds = null;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public Integer getId() {
        return id;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.voteCount);
        dest.writeValue(this.id);
        dest.writeValue(this.voteAverage);
        dest.writeString(this.title);
        dest.writeValue(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalTitle);
        dest.writeList(this.genreIds);
        dest.writeString(this.backdropPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    @Ignore
    protected Movie(Parcel in) {
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.title = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.originalTitle = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.backdropPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

