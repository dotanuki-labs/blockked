package io.dotanuki.blockked.dashboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setupViews()
    }

    private fun setupViews() {
        val points = listOf(
            Entry(1f, 10f),
            Entry(2f, 20f),
            Entry(3f, 30f),
            Entry(4f, 25f),
            Entry(5f, 32f),
            Entry(6f, 38f),
            Entry(7f, 39f),
            Entry(8f, 42f),
            Entry(9f, 35f),
            Entry(10f, 30f),
            Entry(11f, 28f),
            Entry(12f, 53f),
            Entry(13f, 50f),
            Entry(14f, 50f),
            Entry(15f, 46f),
            Entry(16f, 44f),
            Entry(17f, 46f),
            Entry(18f, 48f),
            Entry(19f, 54f),
            Entry(20f, 60f),
            Entry(21f, 58f)
        )

        val dataSets = listOf(
            LineDataSet(points, "Bitcoin Price Evolution").apply {
                setDrawValues(false)
            }
        )

        bitcoinPriceChart.apply {
            setPinchZoom(false)
            setTouchEnabled(false)
            description.isEnabled = false
            axisRight.isEnabled = false
            xAxis.isEnabled = false

            setDrawBorders(false)
            setDrawGridBackground(false)

            axisLeft.apply {
                axisMaximum = 60f
                axisMinimum = 0f
                setDrawZeroLine(false)
            }

            data = LineData(dataSets)
            animateX(1500)
        }
    }

}