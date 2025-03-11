package com.example.coinscomp.ui.core.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.example.coinscomp.R

class RhombusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = 0xFFFF0000.toInt() // it is for example, not necessarily
        style = Paint.Style.FILL
    }

    private val path = Path()

    @ColorInt
    var backColor: Int = 0
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    var cornerRadius: Float = 0f
        set(value) {
            field = value
            pathEffect = CornerPathEffect(value)
            invalidate()
        }

    private var pathEffect: CornerPathEffect? = CornerPathEffect(cornerRadius)

    init {
        gravity = Gravity.CENTER

        // it is need for using backColor attribute in xml custom_view
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RhombusView,
            0,
            0
        ).apply {
            try {
                backColor = getColor(R.styleable.RhombusView_backColor, 0xFFFF0000.toInt())
                cornerRadius = getDimension(R.styleable.RhombusView_cornerRadius, 0f)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawRhombus(canvas)
        super.onDraw(canvas)
    }

    private fun drawRhombus(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2

        path.reset()
        path.moveTo(centerX, 0f) // Top point
        path.lineTo(width, centerY) // Right point
        path.lineTo(centerX, height) // Bottom point
        path.lineTo(0f, centerY) // left point
        path.close()

        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null
    }
}