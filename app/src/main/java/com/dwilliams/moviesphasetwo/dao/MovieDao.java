package com.dwilliams.moviesphasetwo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movie ORDER BY id")
     LiveData<List<Movie>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Movie movie);

    @Delete
    void deleteTask(Movie movie);

    @Query("SELECT * FROM Movie WHERE id = :id")
    LiveData<List<Movie>> loadTaskById(int id);

    @Query("Select id FROM Movie")
    List<Integer> getFavoriteMovieIds();

    @Query("SELECT COUNT(*) FROM Movie WHERE id = :id")
    int doesExists(int id);


}
