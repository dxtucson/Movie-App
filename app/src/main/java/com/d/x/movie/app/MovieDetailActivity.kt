package com.d.x.movie.app

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.d.x.movie.app.posterAnim.PosterExpand
import com.d.x.movie.app.posterAnim.PosterShrink
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTER_URL = "poster_url"
        const val POSTER_X = "poster_x"
        const val POSTER_Y = "poster_y"
        const val POSTER_W = "poster_w"
        const val POSTER_H = "poster_h"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        intent.getStringExtra(TITLE)?.let {
            movie_title.text = it
        }
        intent.getStringExtra(OVERVIEW)?.let {
            overview.text = it
        }
        val errorDrawable = getDrawable(R.drawable.icons8_no_image_96)
        intent.getStringExtra(POSTER_URL)?.let {
            val target = object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    val copy = bitmap.copy(bitmap.config, bitmap.isMutable)
                    poster_image.setImageBitmap(bitmap)
                    val rs = RenderScript.create(this@MovieDetailActivity)
                    val input = Allocation.createFromBitmap(rs, copy)
                    val output = Allocation.createTyped(rs, input.type)
                    val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
                    script.setRadius(25f)
                    script.setInput(input)
                    script.forEach(output)
                    output.copyTo(copy)
                    script.destroy()
                    background_image.setImageBitmap(copy)
                }
            }
            background_image.tag = target
            Picasso.get().load("https://image.tmdb.org/t/p/w500$it")
                .error(errorDrawable!!)
                .into(target)
        }
        background_image.setOnClickListener { onBackPressed() }
        animatePoster()
    }

    private fun animatePoster() {
        val params = poster_image.layoutParams as (ConstraintLayout.LayoutParams)
        val startX = intent.getIntExtra(POSTER_X, params.leftMargin)
        val startY = intent.getIntExtra(POSTER_Y, params.topMargin)
        val startW = intent.getIntExtra(POSTER_W, params.width)
        val startH = intent.getIntExtra(POSTER_H, params.height)
        val expand = PosterExpand(poster_image, startX, startY, startW, startH, null)
        expand.animate()
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, R.anim.fade_out)
        val params = poster_image.layoutParams as (ConstraintLayout.LayoutParams)
        val endX = intent.getIntExtra(POSTER_X, params.leftMargin)
        val endY = intent.getIntExtra(POSTER_Y, params.topMargin)
        val endW = intent.getIntExtra(POSTER_W, params.width)
        val endH = intent.getIntExtra(POSTER_H, params.height)
        val shrink = PosterShrink(poster_image, endX, endY, endW, endH, null)
        shrink.animate()
    }
}
