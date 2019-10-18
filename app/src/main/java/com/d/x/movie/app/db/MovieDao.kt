package com.d.x.movie.app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d.x.movie.app.data.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertList(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: Movie)

    @Query("DELETE FROM Movie")
    abstract fun deleteAll()

    @Query("SELECT * FROM Movie")
    abstract fun loadAll(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun loadById(id: Int): Movie

    @Query("SELECT * FROM Movie WHERE isFavorite IS 1")
    abstract fun loadFavoriteMovies(): List<Movie>
}