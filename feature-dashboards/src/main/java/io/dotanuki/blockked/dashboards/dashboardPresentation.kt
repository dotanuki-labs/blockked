package io.dotanuki.blockked.dashboards

import com.github.mikephil.charting.data.Entry

data class DashboardPresentation(
    val display: DisplayModel,
    val chart: ChartModel
)

data class DisplayModel(
    val formattedValue: String,
    val title: String,
    val subtitle: String
)

sealed class ChartModel {

    object Unavailable : ChartModel()

    data class AvaliableData(
        val minValue: Float,
        val maxValue: Float,
        val legend: String,
        val values: List<Plottable>) : ChartModel()
    
}

// Because Entry from MPAndroidChart library does not implement equals() #sadpanda

interface Plottable {

    val xCoordinate: Float
    val yCoordinate: Float

}

data class PlottableEntry(
    private val a: Float,
    private val b: Float) : Entry(a, b), Plottable {

    override val xCoordinate = x
    override val yCoordinate = y

}
