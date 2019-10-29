package com.d.x.movie.app.viewModels

import android.app.Application
import com.d.x.movie.app.adapters.MovieAdapter.Companion.genreMap
import com.d.x.movie.app.api.ServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : MyViewModel(application) {

    fun loadFromServer(page: Int) {
        if (page > 5) updateWithDB()
        else if (genreMap == null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ServiceApi.api.getGenres()
                    if (response.isSuccessful) {
                        genreMap = hashMapOf()
                        response.body()?.genres?.forEach { g ->
                            genreMap!![g.id] = g.name
                        }
                        loadFromServer(page)
                    }
                } catch (e: Throwable) {
                    networkError.postValue(e.message)
                    networkBusy.postValue(false)
                    updateWithDB()
                    return@launch
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ServiceApi.api.getMovies(lastMonth, page)
                    if (response.isSuccessful) {
                        if (page == 1) networkBusy.postValue(false)
                        response.body()?.results?.let {
                            movieDao.insertList(it)
                        }
                        updateWithDB()
                        loadFromServer(page + 1)
                    }

                } catch (e: Throwable) {
                    networkError.postValue(e.message)
                    networkBusy.postValue(false)
                    updateWithDB()
                    return@launch
                }
            }
        }
    }
}