package com.d.x.movie.app.viewModels

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : MyViewModel(application) {

    fun loadFavorite() {
        CoroutineScope(Dispatchers.IO).launch {
            movieList.postValue(movieDao.loadFavoriteMovies())
        }
    }
}