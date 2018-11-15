package io.dotanuki.blockked.dashboards

import android.view.View
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.view_dashboard.*

class DashboardEntry(private val presentation: DashboardPresentation) : Item() {

    override fun getLayout() = R.layout.view_dashboard

    override fun bind(viewHolder: ViewHolder, position: Int) = with(viewHolder) {
        val (display, chart) = presentation

        when (chart) {
            is ChartModel.Unavailable -> bitcoinPriceChart.visibility = View.GONE
            is ChartModel.AvaliableData -> {

                val dataSets = listOf(
                    LineDataSet(chart.values.map { it as Entry }, chart.legend).apply {
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
                        setDrawZeroLine(false)
                        axisMaximum = chart.maxValue
                        axisMinimum = chart.minValue
                        valueFormatter = LargeValueFormatter()
                    }

                    data = LineData(dataSets)

                    legend.apply {
                        setDrawInside(false)
                        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                        horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                    }
                }
            }

        }

        displayTitle.text = display.title
        displayValue.text = display.formattedValue
        displaySubtitle.text = display.subtitle
    }

}