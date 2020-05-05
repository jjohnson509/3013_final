package com.cs_3013.android.final_project

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet



class ActionBoard : androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onDraw(canvas: Canvas) {
        drawRoundImage(canvas)
    }


    private fun drawRoundImage(canvas: Canvas) {
        var b: Bitmap = (drawable as BitmapDrawable).bitmap
        val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)

        /* Scale the bitmap */
        val scaledBitmap: Bitmap
        val ratio: Float = bitmap.width.toFloat() / bitmap.height.toFloat()
        val height: Int = Math.round(width / ratio)
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, width.toInt(), height, false)

        /* Cutting the outer of the circle */
        val shader: Shader
        shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val rect = RectF()
        rect.set(0f, 0f, width.toFloat(), height.toFloat())

        val imagePaint = Paint()
        imagePaint.isAntiAlias = true
        imagePaint.shader = shader
        canvas.drawRoundRect(rect, width.toFloat(), height.toFloat(), imagePaint)
    }

}