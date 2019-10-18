package com.d.x.movie.app.db

import android.app.Application
import androidx.room.Room

object AppDB {
    private var db: AppDatabase? = null
    fun getDb(application: Application): AppDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                application,
                AppDatabase::class.java, "db"
            ).build()
        }
        return db!!
    }
}