package com.d.x.movie.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie (
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    @PrimaryKey
    val id: Int,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Float?,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean?,
    val vote_average: Float?,
    val vote_count: Int?,
    @Transient
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)