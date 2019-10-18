package com.d.x.movie.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.d.x.movie.app.R
import com.d.x.movie.app.adapters.MovieAdapter
import com.d.x.movie.app.data.Movie
import com.d.x.movie.app.viewModels.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private var viewModel: FavoriteViewModel? = null

    private val clickOnFavorite: ((movie: Movie) -> Unit) = {
        adapter.removeItem(it)
        it.isFavorite = false
        viewModel?.updateMovie(it)
    }

    private var adapter: MovieAdapter =
        MovieAdapter(listOf<Movie>().toMutableList(), clickOnFavorite)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        viewModel?.movieList?.observe(this, Observer {
            adapter.newList = it
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel?.loadFavorite()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (favorite_recyclerView.layoutManager == null) {
            favorite_recyclerView.layoutManager = LinearLayoutManager(context)
            favorite_recyclerView.adapter = adapter
        }
    }
}
