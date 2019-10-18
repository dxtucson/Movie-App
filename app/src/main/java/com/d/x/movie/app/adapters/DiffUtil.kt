package com.d.x.movie.app.adapters

import androidx.recyclerview.widget.DiffUtil
import com.d.x.movie.app.data.Movie

class MovieDiffUtil(
    private val oldList: MutableList<Movie>,
    private val newList: MutableList<Movie>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].isFavorite == newList[newItemPosition].isFavorite
    }
}