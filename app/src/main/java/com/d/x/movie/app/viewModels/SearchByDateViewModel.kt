package com.d.x.movie.app.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.d.x.movie.app.adapters.MovieAdapter
import com.d.x.movie.app.data.MovieComparator
import com.d.x.movie.app.network.ServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SearchByDateViewModel(application: Application) : MyViewModel(application) {

    val startDate: MutableLiveData<String> = MutableLiveData()
    val endDate: MutableLiveData<String> = MutableLiveData()

    fun setStartDate(date: String) {
        startDate.postValue(date)
    }

    fun setToDate(date: String) {
        endDate.postValue(date)
    }

    fun searchByDateRange(from: String, to: String, page: Int) {
        if (page > 2) return
        else if (MovieAdapter.genreMap == null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ServiceApi.api.getGenres()
                    if (response.isSuccessful) {
                        MovieAdapter.genreMap = hashMapOf()
                        response.body()?.genres?.forEach { g ->
                            MovieAdapter.genreMap!![g.id] = g.name
                        }
                        searchByDateRange(from, to, page)
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
                    val response = ServiceApi.api.getMoviesFromTo(from, to, page)
                    if (response.isSuccessful) {
                        val list =
                            if (page == 1) mutableListOf() else movieList.value!!.toMutableList()
                        list.addAll(response.body()!!.results)
                        Collections.sort(list, MovieComparator())
                        if (page == 1) networkBusy.postValue(false)
                        for (movie in list) {
                            movieDao.loadById(movie.id)?.let {
                                movie.isFavorite = it.isFavorite
                            }
                        }
                        movieList.postValue(list)
                        searchByDateRange(from, to, page + 1)
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