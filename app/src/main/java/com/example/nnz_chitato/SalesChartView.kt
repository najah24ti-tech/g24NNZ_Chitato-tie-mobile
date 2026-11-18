
package com.example.nnz_chitato

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class SalesChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Data penjualan per bulan (contoh)
    private val values = listOf(35f, 55f, 45f, 75f, 90f)

    private val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "Mei"
    )

    private val sales = listOf(
        35_000_000L,
        55_000_000L,
        45_000_000L,
        75_000_000L,
        90_000_000L
    )

    private val topProducts = listOf(
        "Serum",
        "Sunscreen",
        "Toner",
        "Handbody",
        "Serum"
    )

    private var selectedIndex = -1

    private val density = resources.displayMetrics.density

    private fun dp(value: Float): Float {
        return value * density
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3158B7.toInt()
        strokeWidth = dp(2f)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3158B7.toInt()
        style = Paint.Style.FILL
    }

    private val selectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3158B7.toInt()
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE5EAF5.toInt()
        strokeWidth = dp(1f)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3158B7.toInt()
        textSize = dp(9f)
        textAlign = Paint.Align.CENTER
    }

    private val tooltipBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3158B7.toInt()
        style = Paint.Style.FILL
    }

    private val tooltipText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textSize = dp(10f)
        textAlign = Paint.Align.LEFT
    }

    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFB547.toInt()
        style = Paint.Style.FILL
    }

    private val currencyFormat =
        NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    private fun getPoints(): List<Pair<Float, Float>> {
        val left = dp(24f)
        val right = width - dp(24f)
        val top = dp(28f)
        val bottom = height - dp(36f)

        return values.mapIndexed { index, value ->

            val x = left + (right - left) *
                    index / (values.size - 1)

            val y = bottom - (value / 100f) *
                    (bottom - top)

            Pair(x, y)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (values.isEmpty() || width == 0 || height == 0) {
            return
        }

        val left = dp(24f)
        val right = width - dp(24f)
        val top = dp(28f)
        val bottom = height - dp(36f)

        // Garis bantu grafik
        for (i in 0..3) {
            val y = top + (bottom - top) * i / 3
            canvas.drawLine(left, y, right, y, gridPaint)
        }

        val points = getPoints()

        // Menggambar garis grafik
        val path = Path()

        points.forEachIndexed { index, point ->
            if (index == 0) {
                path.moveTo(point.first, point.second)
            } else {
                path.lineTo(point.first, point.second)
            }
        }

        canvas.drawPath(path, linePaint)

        // Mencari penjualan tertinggi
        val highestIndex = values.indices.maxByOrNull {
            values[it]
        } ?: 0

        // Menggambar titik grafik
        points.forEachIndexed { index, point ->

            // Lingkaran penanda penjualan tertinggi
            if (index == highestIndex) {
                canvas.drawCircle(
                    point.first,
                    point.second,
                    dp(9f),
                    highlightPaint
                )
            }

            // Titik yang dipilih
            if (index == selectedIndex) {
                canvas.drawCircle(
                    point.first,
                    point.second,
                    dp(10f),
                    selectedPaint
                )
            }

            canvas.drawCircle(
                point.first,
                point.second,
                dp(3f),
                dotPaint
            )

            // Nama bulan
            canvas.drawText(
                months[index],
                point.first,
                height - dp(10f),
                textPaint
            )
        }

        // Menampilkan tooltip saat titik dipilih
        if (selectedIndex in points.indices) {
            drawTooltip(
                canvas,
                points[selectedIndex],
                selectedIndex
            )
        }
    }

    private fun drawTooltip(
        canvas: Canvas,
        point: Pair<Float, Float>,
        index: Int
    ) {
        val boxWidth = dp(155f)
        val boxHeight = dp(59f)

        var boxLeft = point.first - boxWidth / 2f
        var boxTop = point.second - boxHeight - dp(15f)

        // Menjaga tooltip agar tidak keluar layar
        boxLeft = boxLeft.coerceIn(
            dp(2f),
            width - boxWidth - dp(2f)
        )

        if (boxTop < dp(2f)) {
            boxTop = point.second + dp(14f)
        }

        val rect = RectF(
            boxLeft,
            boxTop,
            boxLeft + boxWidth,
            boxTop + boxHeight
        )

        // Background tooltip
        canvas.drawRoundRect(
            rect,
            dp(8f),
            dp(8f),
            tooltipBackground
        )

        val x = boxLeft + dp(10f)

        // Bulan
        canvas.drawText(
            "${months[index]} - Penjualan",
            x,
            boxTop + dp(16f),
            tooltipText
        )

        // Nilai penjualan
        canvas.drawText(
            currencyFormat.format(sales[index]),
            x,
            boxTop + dp(32f),
            tooltipText
        )

        // Produk terlaris
        canvas.drawText(
            "Produk terlaris: ${topProducts[index]}",
            x,
            boxTop + dp(48f),
            tooltipText
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {

                val points = getPoints()

                val nearestIndex = points.indices.minByOrNull {
                    abs(points[it].first - event.x)
                } ?: -1

                if (nearestIndex != -1) {
                    val distance = abs(
                        points[nearestIndex].first - event.x
                    )

                    if (distance <= dp(30f)) {
                        selectedIndex = nearestIndex
                        invalidate()
                        return true
                    }
                }

                return true
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                selectedIndex = -1
                invalidate()
                return true
            }
        }

        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
