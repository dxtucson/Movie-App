package com.d.x.movie.app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.d.x.movie.app.db.AppDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    val title = MutableLiveData<String>()
    val overview = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()
    private val movieDao = AppDB.getDb(application).movieDao()

    fun load(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.loadById(id = id)?.let {
                title.postValue(it.title)
                overview.postValue(it.overview)
                imageUrl.postValue(it.poster_path)
            }
        }
    }
}