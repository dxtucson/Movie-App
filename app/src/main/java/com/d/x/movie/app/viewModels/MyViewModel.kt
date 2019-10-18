package com.d.x.movie.app.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.d.x.movie.app.data.Movie
import com.d.x.movie.app.data.MovieComparator
import com.d.x.movie.app.db.AppDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

open class MyViewModel(application: Application) : AndroidViewModel(application) {
    val movieList: MutableLiveData<List<Movie>> = MutableLiveData()
    val networkError: MutableLiveData<String> = MutableLiveData()
    val networkBusy: MutableLiveData<Boolean> = MutableLiveData(false)
    val movieDao = AppDB.getDb(application).movieDao()
    val lastMonth: String by lazy {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -1)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.format(calendar.time)
    }

    fun updateMovie(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.insert(movie)
        }
    }

    fun updateWithDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val listOfMovies = movieDao.loadAll()
            Collections.sort(listOfMovies, MovieComparator())
            movieList.postValue(listOfMovies)
        }
    }
}