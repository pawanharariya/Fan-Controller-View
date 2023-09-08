package com.example.fancontroller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private enum class FanSpeed(val label: Int) {
    OFF(R.string.fan_off),
    LOW(R.string.fan_low),
    MEDIUM(R.string.fan_medium),
    HIGH(R.string.fan_high);

    fun next() = when (this) {
        OFF -> LOW
        LOW -> MEDIUM
        MEDIUM -> HIGH
        HIGH -> OFF
    }
}

private const val LABEL_OFFSET_DISTANCE = 30
private const val INDICATOR_OFFSET_DISTANCE = -35

class DialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // All allocations done outside for better performance
    private var radius = 0.0f                   // Radius of the circle.
    private var fanSpeed = FanSpeed.OFF         // The active selection.
    private var fanSpeedLowColor = 0
    private var fanSpeedMediumColor = 0
    private var fanSeedMaxColor = 0

    // position variable which will be used to draw label and indicator circle position
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 35.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    init {
        // set the view to be clickable
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.DialView) {
            fanSpeedLowColor = getColor(R.styleable.DialView_fanColor1, 0)
            fanSpeedMediumColor = getColor(R.styleable.DialView_fanColor2, 0)
            fanSeedMaxColor = getColor(R.styleable.DialView_fanColor3, 0)
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        // Maximum circle's diameter can be the minimum side of canvas
        // So radius becomes half of the minimum of width and height
        // We take slight less radius so that we can add speed numbers on Canvas
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    // calculates the coordinates based on fan speed and radial distance from center
    private fun PointF.computeXYForSpeed(pos: FanSpeed, distanceFromCenter: Float) {
        // Angles are in radians.
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 4)
        x = (distanceFromCenter * cos(angle)).toFloat() + width / 2
        y = (distanceFromCenter * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // To set dial's color
        paint.color = when (fanSpeed) {
            FanSpeed.OFF -> Color.GRAY
            FanSpeed.LOW -> fanSpeedLowColor
            FanSpeed.MEDIUM -> fanSpeedMediumColor
            FanSpeed.HIGH -> fanSeedMaxColor
        }

        // Draw a circle from center of canvas
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

        // Draw the indicator circle
        val markerDistance = radius + INDICATOR_OFFSET_DISTANCE
        pointPosition.computeXYForSpeed(fanSpeed, markerDistance)
        paint.color = Color.BLACK
        canvas.drawCircle(pointPosition.x, pointPosition.y, radius / 12, paint)

        // Draw labels around the circle
        val labelDistance = radius + LABEL_OFFSET_DISTANCE
        for (i in FanSpeed.values()) {
            pointPosition.computeXYForSpeed(i, labelDistance)
            val label = resources.getString(i.label)
            canvas.drawText(label, pointPosition.x, pointPosition.y, paint)
        }
    }

    override fun performClick(): Boolean {
        // we need to call super class method first, which enables accessibility events and calls onClickListener
        if (super.performClick()) return true

        // increase the fan speed on click
        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.label)

        // invalidate() the view, so that it re-draws
        invalidate()
        return true
    }
}