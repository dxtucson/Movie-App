package com.d.x.movie.app.viewModels

import android.app.Application
import com.d.x.movie.app.adapters.MovieAdapter.Companion.genreMap
import com.d.x.movie.app.data.MovieComparator
import com.d.x.movie.app.network.ServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class GenreViewModel(application: Application) : MyViewModel(application) {

    fun loadByGenre(page: Int, genreId: Int) {
        if (page > 5) return
        else if (genreMap == null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ServiceApi.api.getGenres()
                    if (response.isSuccessful) {
                        genreMap = hashMapOf()
                        response.body()?.genres?.forEach { g ->
                            genreMap!![g.id] = g.name
                        }
                        loadByGenre(page, genreId)
                    }
                } catch (e: Throwable) {
                    networkError.postValue(e.message)
                    networkBusy.postValue(false)
                    return@launch
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response =
                        ServiceApi.api.getMoviesByGenre(lastMonth, genreId.toString(), page)
                    if (response.isSuccessful) {
                        val list = if (page == 1) mutableListOf() else movieList.value!!.toMutableList()
                        list.addAll(response.body()!!.results)
                        for (movie in list) {
                            movieDao.loadById(movie.id)?.let {
                                movie.isFavorite = it.isFavorite
                            }
                        }
                        Collections.sort(list, MovieComparator())
                        if (page == 1) networkBusy.postValue(false)
                        movieList.postValue(list)
                        loadByGenre(page + 1, genreId)
                    }
                } catch (e: Throwable) {
                    networkError.postValue(e.message)
                    networkBusy.postValue(false)
                    return@launch
                }
            }
        }
    }
}