package com.example.closetstack

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class ColorWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var colorChangeListener: ((Int) -> Unit)? = null
    private var hue = 0f
    private var saturation = 1f
    private val wheelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.WHITE
    }
    private var cx = 0f
    private var cy = 0f
    private var radius = 0f
    private var selectorX = 0f
    private var selectorY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cx = w / 2f
        cy = h / 2f
        radius = minOf(cx, cy) - 8f
        selectorX = cx + radius
        selectorY = cy
        buildWheelShader()
    }

    private fun buildWheelShader() {
        val sweepColors = IntArray(361) { i ->
            Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f))
        }
        val sweepShader = SweepGradient(cx, cy, sweepColors, null)
        val radialShader = RadialGradient(
            cx, cy, radius,
            Color.WHITE, Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        )
        wheelPaint.shader = ComposeShader(sweepShader, radialShader, PorterDuff.Mode.SCREEN)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(cx, cy, radius, wheelPaint)
        canvas.drawCircle(selectorX, selectorY, 12f, selectorPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
            val dx = event.x - cx
            val dy = event.y - cy
            val dist = sqrt(dx * dx + dy * dy)
            val clampedDist = minOf(dist, radius)
            val angle = atan2(dy, dx)
            selectorX = cx + clampedDist * cos(angle)
            selectorY = cy + clampedDist * sin(angle)
            hue = ((Math.toDegrees(angle.toDouble()) + 360) % 360).toFloat()
            saturation = clampedDist / radius
            val color = Color.HSVToColor(floatArrayOf(hue, saturation, 1f))
            colorChangeListener?.invoke(color)
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    fun setColorChangeListener(listener: (Int) -> Unit) {
        colorChangeListener = listener
    }
}