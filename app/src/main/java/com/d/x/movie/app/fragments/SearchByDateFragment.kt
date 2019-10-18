package com.d.x.movie.app.fragments

import android.app.DatePickerDialog
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
import com.d.x.movie.app.viewModels.SearchByDateViewModel
import kotlinx.android.synthetic.main.fragment_search_by_date.*
import java.text.SimpleDateFormat
import java.util.*

class SearchByDateFragment : Fragment() {

    private var viewModel: SearchByDateViewModel? = null

    private val clickOnFavorite: ((movie: Movie) -> Unit) = {
        it.isFavorite = !it.isFavorite
        adapter.notifyItemChanged(adapter.movies.indexOf(it))
        viewModel?.updateMovie(it)
    }

    private var adapter: MovieAdapter =
        MovieAdapter(listOf<Movie>().toMutableList(), clickOnFavorite)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchByDateViewModel::class.java)
        viewModel?.startDate?.observe(this, Observer {
            start_date.setText(it)
            val to = to_date.text.toString()
            if (to.isNotEmpty()) {
                viewModel?.networkBusy?.postValue(true)
                viewModel?.searchByDateRange(it, to, 1)
            }
        })
        viewModel?.endDate?.observe(this, Observer {
            to_date.setText(it)
            val from = start_date.text.toString()
            if (from.isNotEmpty()) {
                viewModel?.networkBusy?.postValue(true)
                viewModel?.searchByDateRange(from, it, 1)
            }
        })
        viewModel?.movieList?.observe(this, Observer {
            adapter.newList = it
        })
        viewModel?.networkError?.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel?.networkBusy?.observe(this, Observer {
            if (it) {
                search_recyclerView.visibility = View.INVISIBLE
                search_loading_view.visibility = View.VISIBLE
            } else {
                search_recyclerView.visibility = View.VISIBLE
                search_loading_view.visibility = View.GONE
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_by_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_date.setOnClickListener { p0 ->
            DatePickerDialog(p0.context).apply {
                setOnDateSetListener { _, yy, mm, dd ->
                    Calendar.getInstance().apply {
                        this.set(Calendar.YEAR, yy)
                        this.set(Calendar.MONTH, mm)
                        this.set(Calendar.DAY_OF_MONTH, dd)
                        viewModel?.setStartDate(
                            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(
                                this.time
                            )
                        )
                    }
                }
                show()
            }
        }
        to_date.setOnClickListener { p0 ->
            DatePickerDialog(p0.context).apply {
                setOnDateSetListener { _, yy, mm, dd ->
                    Calendar.getInstance().apply {
                        this.set(Calendar.YEAR, yy)
                        this.set(Calendar.MONTH, mm)
                        this.set(Calendar.DAY_OF_MONTH, dd)
                        viewModel?.setToDate(
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.US
                            ).format(this.time)
                        )
                    }
                }
                show()
            }
        }
        if (search_recyclerView.layoutManager == null) {
            search_recyclerView.layoutManager = LinearLayoutManager(context)
            search_recyclerView.adapter = adapter
        }
    }
}

