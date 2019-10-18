package com.d.x.movie.app.data

data class GetMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)