package com.d.x.movie.app.posterAnim

import android.content.res.Resources
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout

class PosterExpand(
    val view: View,
    val startX: Int,
    val startY: Int,
    val startW: Int,
    val startH: Int,
    val onAnimationEnd: (() -> Unit)?
) : android.view.animation.Animation() {

    val params = view.layoutParams as (ConstraintLayout.LayoutParams)
    val totalW = Resources.getSystem().displayMetrics.widthPixels
    val totalH = Resources.getSystem().displayMetrics.heightPixels
    val finalW = 0.38f * totalW
    val finalH = finalW / 0.7f
    val finalHB = params.horizontalBias
    val finalVB = params.verticalBias
    val startHB = (startX.toFloat()) / totalW
    val startVB = (startY.toFloat()) / totalH

    fun animate() {
        params.width = startW
        params.height = startH
        params.horizontalBias = startHB
        params.verticalBias = startVB
        duration = 200
        view.startAnimation(this)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val params = view.layoutParams as (ConstraintLayout.LayoutParams)
        params.horizontalBias = (finalHB - startHB) * interpolatedTime + startHB
        params.verticalBias = (finalVB - startVB) * interpolatedTime + startVB
        params.width = ((finalW - startW) * interpolatedTime + startW).toInt()
        params.height = ((finalH - startH) * interpolatedTime + startH).toInt()
        view.layoutParams = params
    }
}

class PosterShrink(
    val view: View,
    val endX: Int,
    val endY: Int,
    val endW: Int,
    val endH: Int,
    val onAnimationEnd: (() -> Unit)?
) : android.view.animation.Animation() {

    val params = view.layoutParams as (ConstraintLayout.LayoutParams)
    val startW = params.width
    val startH = params.height
    val totalW = Resources.getSystem().displayMetrics.widthPixels
    val totalH = Resources.getSystem().displayMetrics.heightPixels
    val finalW = endW
    val finalH = endH
    val finalHB = (endX.toFloat()) / totalW
    val finalVB = (endY.toFloat()) / totalH
    val startHB = params.horizontalBias
    val startVB = params.verticalBias

    fun animate() {
        duration = 200
        view.startAnimation(this)
        setAnimationListener(object :AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                onAnimationEnd?.invoke()
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val params = view.layoutParams as (ConstraintLayout.LayoutParams)
        params.horizontalBias = (finalHB - startHB) * interpolatedTime + startHB
        params.verticalBias = (finalVB - startVB) * interpolatedTime + startVB
        params.width = ((finalW - startW) * interpolatedTime + startW).toInt()
        params.height = ((finalH - startH) * interpolatedTime + startH).toInt()
        view.layoutParams = params
    }
}