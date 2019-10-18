package com.d.x.movie.app.data

class MovieComparator : Comparator<Movie> {
    override fun compare(p0: Movie, p1: Movie): Int {
        return  p1.release_date.compareTo(p0.release_date)
    }
}