package com.d.x.movie.app.adapters

import android.app.Activity
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d.x.movie.app.R
import com.d.x.movie.app.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlin.collections.MutableList as MutableList1

class MovieAdapter(
    var movies: MutableList1<Movie>,
    private val favoriteListener: ((m: Movie) -> Any)
) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    var newList: List<Movie>? = null
        set(value) {
            value?.let {
                val result =
                    DiffUtil.calculateDiff(MovieDiffUtil(this.movies, it.toMutableList()), false)
                this.movies = it.toMutableList()
                result.dispatchUpdatesTo(this)
            }
            field = value
        }

    companion object {
        var genreMap: HashMap<Int, String>? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(
        holder: MovieHolder,
        position: Int,
        payloads: kotlin.collections.MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val isFavorite = payloads[0] as Boolean
            ImageViewCompat.setImageTintList(
                holder.favoriteButton, ColorStateList.valueOf(
                    ContextCompat.getColor(
                        holder.favoriteButton.context,
                        if (isFavorite) R.color.colorAccent else R.color.colorPrimary
                    )
                )
            )
        }
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.year.text = movie.release_date
        val errorDrawable = holder.itemView.context.getDrawable(R.drawable.icons8_no_image_96)
        if (movie.poster_path == null || movie.poster_path.isEmpty()) {
            holder.poster.setImageDrawable(errorDrawable)
        } else {
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
                .error(errorDrawable!!)
                .into(holder.poster)
        }
        if (movie.genre_ids == null || movie.genre_ids.isEmpty()) {
            holder.genre1.visibility = View.INVISIBLE
            holder.genre2.visibility = View.INVISIBLE
            holder.genre3.visibility = View.INVISIBLE
            holder.genre4.visibility = View.INVISIBLE
        } else {
            genreMap?.let { map ->
                movie.genre_ids.forEachIndexed { i, id ->
                    if (i == 0) {
                        holder.genre1.visibility = View.VISIBLE
                        holder.genre1.text = map[id]
                        holder.genre1.setOnClickListener { p0 ->
                            GenreClickListener(
                                p0.context as Activity,
                                id,
                                map[id]
                            ).onClick()
                        }
                        holder.genre2.visibility = View.INVISIBLE
                        holder.genre3.visibility = View.INVISIBLE
                        holder.genre4.visibility = View.INVISIBLE
                    } else if (i == 1) {
                        holder.genre2.visibility = View.VISIBLE
                        holder.genre2.text = map[id]
                        holder.genre2.setOnClickListener { p0 ->
                            GenreClickListener(
                                p0.context as Activity,
                                id,
                                map[id]
                            ).onClick()
                        }
                        holder.genre3.visibility = View.INVISIBLE
                        holder.genre4.visibility = View.INVISIBLE
                    } else if (i == 2) {
                        holder.genre3.visibility = View.VISIBLE
                        holder.genre3.text = map[id]
                        holder.genre3.setOnClickListener { p0 ->
                            GenreClickListener(
                                p0.context as Activity,
                                id,
                                map[id]
                            ).onClick()
                        }
                        holder.genre4.visibility = View.INVISIBLE
                    } else if (i == 3) {
                        holder.genre4.visibility = View.VISIBLE
                        holder.genre4.text = map[id]
                        holder.genre4.setOnClickListener { p0 ->
                            GenreClickListener(
                                p0.context as Activity,
                                id,
                                map[id]
                            ).onClick()
                        }
                    }
                }
            }
        }
        ImageViewCompat.setImageTintList(
            holder.favoriteButton, ColorStateList.valueOf(
                ContextCompat.getColor(
                    holder.favoriteButton.context,
                    if (movie.isFavorite) R.color.colorAccent else R.color.colorPrimary
                )
            )
        )
        holder.favoriteButton.setOnClickListener {
            favoriteListener.invoke(movie)
        }
        holder.itemView.setOnClickListener { p0 ->
            val location = intArrayOf(0, 0)
            holder.poster.getLocationInWindow(location)
            MovieItemClickListener(
                p0.context as Activity,
                location[0],
                location[1],
                holder.poster.width,
                holder.poster.height,
                movie
            ).onClick()
        }
    }

    fun removeItem(movie: Movie) {
        val index = movies.indexOf(movie)
        if (index > -1) {
            movies.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster = itemView.poster_image
        val title = itemView.title
        val year = itemView.year
        val genre1 = itemView.genre_1
        val genre2 = itemView.genre_2
        val genre3 = itemView.genre_3
        val genre4 = itemView.genre_4
        val favoriteButton = itemView.add_favorite
    }
}