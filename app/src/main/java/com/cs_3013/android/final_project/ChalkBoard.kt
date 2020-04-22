package com.cs_3013.android.final_project

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.animation.BounceInterpolator


class ChalkBoard(context: Context) : View(context) {
    private var displayWidth: Int = 0
    private var displayHeight: Int = 0

    private var startX = 55.0f
    private var width = 300.0f
    private var stopX = startX + width
    private var height = 300.0f
    private var top = 100.0f
    private var bottom = top + height
    private var deltaX = 40.0f
    private var deltaY = 40.0f
    private var oldX = 0.0f
    private var oldY = 0.0f
    private var x1 = startX
    private var y1 = top
    private var x2 = x1 + width
    private var y2 = y1 + height
    private var fraction = 1.0f
    private var style = BOUNCE
    private var moveFlag = true
    private var paint = Paint()

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val screen = wm.defaultDisplay
        val size = Point()
        screen.getSize(size)
        displayWidth = size.x
        displayHeight = size.y
    }

    fun wander() {
        val anim: ObjectAnimator
        if (moveFlag) {
            oldX = startX
            oldY = top
            startX = (0.90 * displayWidth * Math.random()).toFloat()
            deltaX = startX - oldX
            stopX = startX + width
            top = (0.80 * displayHeight * Math.random()).toFloat()
            deltaY = top - oldY
            bottom = top + height
        }
        when (style) {
            BOUNCE -> {
                anim = getObjectAnimator(500, "fraction", 0.0f, 1.0f)
                anim.interpolator = BounceInterpolator()
                anim.start()
            }
        }
    }

    private fun getObjectAnimator(duration: Int, variable: String, initialValue: Float, finalValue: Float): ObjectAnimator{
        val animation = ObjectAnimator.ofFloat(this, variable, initialValue, finalValue)
        animation.duration = duration.toLong()
        return animation
    }

    private fun setFraction(value: Float) {
        fraction = value
        step()
    }


    private fun step() {
        x1 = oldX + fraction * deltaX
        y1 = oldY + fraction * deltaY
        x2 = x1 + width
        y2 = y1 + height
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 12f
        canvas.drawRoundRect(x1, y1, x2, y2, 5000f, 5000f, paint)
        paint.color = Color.argb(100, 139,0,0)
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(x1, y1, x2, y2, 3000f, 3000f, paint)
    }

    companion object {
        const val BOUNCE = 4
    }


}