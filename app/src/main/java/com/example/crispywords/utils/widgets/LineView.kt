package com.example.crispywords.utils.widgets

import android.content.Context
import android.graphics.*
import android.view.View


class LineView(context: Context?) : View(context) {

    private var paint: Paint = Paint()
    private val strokeWidth = 60f
    private var startX: Float = -1f
    private var startY: Float = -1f
    private var endX: Float = -1f
    private var endY: Float = -1f

    init {
        paint.setStrokeWidth(strokeWidth)
        paint.strokeCap = Paint.Cap.ROUND
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.ADD))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(startX, startY, endX, endY, paint)
    }

    fun setPosition(color: Int, firstX: Float, firstY: Float, secondX: Float, secondY: Float){
        paint.setColor(color)
        startX = firstX
        startY = firstY
        endX = secondX
        endY = secondY
        invalidate()
    }

}