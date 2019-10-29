package com.d.x.movie.app.api

import com.d.x.movie.app.data.GenresResponse
import com.d.x.movie.app.data.GetMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbService {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query(
            value = "release_date.gte",
            encoded = true
        ) dateGte: String,
        @Query(value = "page", encoded = true) page: Int
    ): Response<GetMoviesResponse>

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenresResponse>

    @GET("discover/movie")
    suspend fun getMoviesFromTo(
        @Query(
            value = "release_date.gte",
            encoded = true
        ) dateGte: String,
        @Query(
            value = "release_date.lte",
            encoded = true
        ) dateLte: String,
        @Query(value = "page", encoded = true) page: Int
    ): Response<GetMoviesResponse>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query(
            value = "release_date.gte",
            encoded = true
        ) dateGte: String,
        @Query(
            value = "with_genres",
            encoded = true
        ) genre: String,
        @Query(value = "page", encoded = true) page: Int
    ): Response<GetMoviesResponse>
}