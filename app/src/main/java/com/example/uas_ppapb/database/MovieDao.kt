package com.example.uas_ppapb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<MovieRoom>)

    @Query("SELECT * FROM movie_table WHERE id = :moviesId")
    fun getMoviesById(moviesId: String): LiveData<MovieRoom>

    @get:Query("SELECT * FROM movie_table ORDER BY id ASC")
    val getAllMovies: LiveData<List<MovieRoom>>

    @Query("DELETE FROM movie_table")
    fun deleteAllMovies()
}
