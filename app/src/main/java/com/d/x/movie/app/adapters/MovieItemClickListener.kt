package com.d.x.movie.app.adapters

import android.app.Activity
import android.content.Intent
import com.d.x.movie.app.MovieDetailActivity

class MovieItemClickListener(
    val context: Activity,
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int,
    val movie: com.d.x.movie.app.data.Movie
) {
    fun onClick() {
        Intent(context, MovieDetailActivity::class.java).apply {
            putExtra(MovieDetailActivity.TITLE, movie.title)
            putExtra(MovieDetailActivity.OVERVIEW, movie.overview)
            putExtra(MovieDetailActivity.POSTER_URL, movie.poster_path)
            putExtra(MovieDetailActivity.POSTER_X, x)
            putExtra(MovieDetailActivity.POSTER_Y, y)
            putExtra(MovieDetailActivity.POSTER_W, w)
            putExtra(MovieDetailActivity.POSTER_H, h)
            context.startActivity(this)
        }
    }
}