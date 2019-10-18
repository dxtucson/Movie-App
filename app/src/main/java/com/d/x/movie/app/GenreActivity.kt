package com.d.x.movie.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.d.x.movie.app.adapters.MovieAdapter
import com.d.x.movie.app.data.Movie
import com.d.x.movie.app.viewModels.GenreViewModel
import kotlinx.android.synthetic.main.activity_genre.*

class GenreActivity : AppCompatActivity() {

    private var viewModel: GenreViewModel? = null
    private val clickOnFavorite: ((movie: Movie) -> Unit) = {
        it.isFavorite = !it.isFavorite
        adapter.notifyItemChanged(adapter.movies.indexOf(it))
        viewModel?.updateMovie(it)
    }
    private var adapter: MovieAdapter =
        MovieAdapter(listOf<Movie>().toMutableList(), clickOnFavorite)

    companion object {
        const val INTENT_KEY_GENRE = "Genre id"
        const val INTENT_KEY_GENRE_NAME = "Genre name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)
        genre_recyclerView.layoutManager = LinearLayoutManager(this)
        genre_recyclerView.adapter = adapter
        viewModel = ViewModelProviders.of(this).get(GenreViewModel::class.java)
        viewModel?.movieList?.observe(this, Observer {
            adapter.newList = it
        })
        viewModel?.networkError?.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        viewModel?.networkBusy?.observe(this, Observer {
            if (it) {
                genre_recyclerView.visibility = View.INVISIBLE
                genre_loading_view.visibility = View.VISIBLE
            } else {
                genre_recyclerView.visibility = View.VISIBLE
                genre_loading_view.visibility = View.GONE
            }
        })

        val genreId = intent.getIntExtra(INTENT_KEY_GENRE, -1)
        if (genreId > -1) {
            viewModel?.networkBusy?.postValue(true)
            viewModel?.loadByGenre(1, genreId)
        }

        intent.getStringExtra(INTENT_KEY_GENRE_NAME)?.let{
            setTitle("${it} Movies" )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right)
    }
}