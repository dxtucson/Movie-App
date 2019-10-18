package com.d.x.movie.app.adapters

import android.app.Activity
import android.content.Intent
import com.d.x.movie.app.GenreActivity
import com.d.x.movie.app.R

class GenreClickListener( val context: Activity,  val genreId: Int,  val name: String?) {

    fun onClick() {
        Intent(context, GenreActivity::class.java).apply {
            this.putExtra(GenreActivity.INTENT_KEY_GENRE, genreId)
            this.putExtra(GenreActivity.INTENT_KEY_GENRE_NAME, name)
            context.startActivity(this)
            context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
        }
    }
}