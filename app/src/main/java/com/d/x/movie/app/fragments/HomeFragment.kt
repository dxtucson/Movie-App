package com.d.x.movie.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.d.x.movie.app.R
import com.d.x.movie.app.adapters.MovieAdapter
import com.d.x.movie.app.data.Movie
import com.d.x.movie.app.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search_by_date.*

class HomeFragment : Fragment() {

    private var viewModel: HomeViewModel? = null

    private val clickOnFavorite: ((movie: Movie) -> Unit) = {
        it.isFavorite = !it.isFavorite
        adapter.notifyItemChanged(adapter.movies.indexOf(it))
        viewModel?.updateMovie(it)
    }

    private var adapter: MovieAdapter =
        MovieAdapter(listOf<Movie>().toMutableList(), clickOnFavorite)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel?.movieList?.observe(this, Observer {
            adapter.newList = it
        })
        viewModel?.networkError?.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel?.networkBusy?.observe(this, Observer {
            if (it) {
                home_recyclerView.visibility = View.INVISIBLE
                home_loading_view.visibility = View.VISIBLE
            } else {
                home_recyclerView.visibility = View.VISIBLE
                home_loading_view.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (adapter.movies.isEmpty()) {
            viewModel?.networkBusy?.postValue(true)
            viewModel?.loadFromServer(page = 1)
        } else {
            viewModel?.updateWithDB()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (home_recyclerView.layoutManager == null) {
            home_recyclerView.layoutManager = LinearLayoutManager(context)
            home_recyclerView.adapter = adapter
        }
    }
}