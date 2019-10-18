package com.d.x.movie.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.d.x.movie.app.data.Movie

@Database(entities = arrayOf(Movie::class), version = 1)
@TypeConverters(ListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}