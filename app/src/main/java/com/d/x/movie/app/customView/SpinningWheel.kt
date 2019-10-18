package com.d.x.movie.app.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.d.x.movie.app.R
import kotlin.math.min

class SpinningWheel : View {

    companion object {
        private val cosValues = floatArrayOf(0.951f, 0.951f, 0.588f, 0f, -0.588f, -0.951f, -0.951f,	-0.588f, 0f, 0.588f)
        private val sinValues = floatArrayOf(-0.309f, 0.309f, 0.809f, 1.000f, 0.809f, 0.309f, -0.309f, -0.809f, -1f, -0.809f)
    }

    private lateinit var spinnerPaint: Paint
    private lateinit var spinnerRect: RectF
    private val smallBarHPercentage = 0.112f
    private val smallBarWPercentage = 0.29f
    private var smallBarHeight = 0f
    private var smallBarWidth = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var startIndex = 0
    private val updateInterval = 50L
    private var r = 0f

    constructor(ctx: Context) : super(ctx) {
        init()
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        init()
    }

    private fun init() {
        spinnerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        spinnerPaint.color = context.getColor(R.color.colorAccent)
        spinnerRect = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        smallBarHeight = measuredHeight * smallBarHPercentage
        smallBarWidth = measuredWidth * smallBarWPercentage
        centerX = 0.5f * measuredWidth
        centerY = 0.5f * measuredHeight
        r = (0.5f - smallBarWPercentage) * min(measuredWidth, measuredWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0..9) {
            spinnerRect.set(
                centerX + cosValues[i] * r,
                centerY + sinValues[i] * r - 0.5f * smallBarHeight,
                centerX + cosValues[i] * r + smallBarWidth,
                centerY + sinValues[i] * r + 0.5f * smallBarHeight
            )
            canvas?.save()
            canvas?.rotate(i * 36f - 18f, centerX + cosValues[i] * r, centerY + sinValues[i] * r)
            spinnerPaint.alpha = 255 * (i + 1 - startIndex) / 10
            canvas?.drawRoundRect(spinnerRect, 0.5f * smallBarHeight, 0.5f * smallBarHeight, spinnerPaint)
            canvas?.restore()
        }
        handler.postDelayed({
            startIndex++
            if (startIndex > 9) startIndex = 0
            invalidate()
        }, updateInterval)
    }
}