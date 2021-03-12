package com.fastaccess.ui.modules.repos.code.graph

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withClip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.abs
import kotlin.math.round

class GraphView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val TAG = "GraphView"
    var graphData: List<StatsModel.Item.Week> = emptyList()
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    private val maxLines: Int = 5
    private var widthSpacing: Float = 0f
    private var commits: Int = 0
    private val textBounds = Rect()
    private val path = Path()

    private val linePaint = Paint().apply {
        style = Paint.Style.FILL
        color = if (isInEditMode) Color.BLACK else Color.LTGRAY
        textSize = 13.dp()
    }
    private val graphPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = 0xC8517558.toInt()
        isAntiAlias = true
    }

    private val textPadding = 5.dp()
    private val lineDistance = 40.dp()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO: Find better way to achieve below using floor or ceil functions.
        if (!graphData.isEmpty()) {
            val maxCommit = graphData.maxOf { it.c } // eg: 21
            var roundedCommit = (round(maxCommit.toDouble()/5.0) * 5).toInt() // eg: 25
            if (roundedCommit > maxCommit)
                roundedCommit -= 5 // eg: 20
            commits = roundedCommit / (maxLines - 1) // Since sequence is in AP.
            if (commits % 5 != 0) {
                commits = getNearestRoundTo5(commits.toDouble())
            }

            val heightSpec = MeasureSpec.makeMeasureSpec(lineDistance.toInt() * maxLines, MeasureSpec.EXACTLY)
            linePaint.getTextBounds(maxCommit.toString(), 0, maxCommit.toString().length, textBounds)
            super.onMeasure(widthMeasureSpec, heightSpec)
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var startX = textPadding * 2 + textBounds.width()
        val minHeight = height.toFloat() - textBounds.height() / 4
        widthSpacing = (width - startX) / graphData.size
        path.reset()
        path.moveTo(startX, minHeight)
        graphData.forEachIndexed { i, gm ->
            val maxY = abs(minHeight - (lineDistance * gm.c / maxLines))
            path.lineTo(startX, maxY)
            startX += widthSpacing
        }
        path.lineTo(width.toFloat(), minHeight)
        path.lineTo(startX, minHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        super.onDraw(canvas)
        var decHeight = 0f
        for(i in 0..maxLines) {
            canvas.drawText("${i * commits}", textPadding, height.toFloat() - decHeight, linePaint)
            canvas.drawLine(textPadding * 2 + textBounds.width(), height.toFloat() - decHeight - textBounds.height() / 4, width.toFloat(), height.toFloat() - decHeight - textBounds.height() / 4, linePaint)
            decHeight += lineDistance
        }
        canvas.withClip(path) {
            canvas.drawColor(graphPaint.color)
        }
    }


    private fun getNearestRoundTo5(n: Double): Int {
        return (round(n/5.0) * 5).toInt()
    }

    fun Int.dp() = this * Resources.getSystem().displayMetrics.density
}