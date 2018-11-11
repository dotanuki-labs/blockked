package io.dotanuki.blockked.dashboards

import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.TimeBasedMeasure
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object BuildDashboardPresentation {

    operator fun invoke(info: BitcoinStatistic) = with(info) {
        DashboardPresentation(

            display = DisplayModel(
                formattedValue = formatPrice(prices.last()),
                title = "Current BTC value",
                subtitle = formatSubtitle(prices.last(), providedDescription)
            ),

            chart = assembleChart(info)

        )
    }

    private fun assembleChart(info: BitcoinStatistic) = with(info) {
        if (prices.size < 2) ChartModel.Unavailable
        else {
            ChartModel.AvaliableData(
                minValue = extractMinimum(prices),
                maxValue = extractMaximum(prices),
                legend = formatLegend(prices.first(), prices.last()),
                values = buildEntries(prices)

            )
        }
    }

    private fun buildEntries(prices: List<TimeBasedMeasure>) =
        prices.mapIndexed { index, bitcoinPrice ->
            val x = (index + 1).toFloat()
            PlottableEntry(x, bitcoinPrice.value)
        }

    private fun formatLegend(first: TimeBasedMeasure, last: TimeBasedMeasure) =
        "Bitcoin value evolution (${formateDate(first.dateTime)} to ${formateDate(last.dateTime)})"

    private fun extractMaximum(prices: List<TimeBasedMeasure>) =
        prices.asSequence().map { it.value }.max()
            ?.let { it + BIAS }
            ?: throw IllegalArgumentException("No maximum")

    private fun extractMinimum(prices: List<TimeBasedMeasure>) =
        prices.asSequence().map { it.value }.min()
            ?.let { it - BIAS }
            ?: throw IllegalArgumentException("No minimum")

    private fun formatSubtitle(last: TimeBasedMeasure, providedDescription: String) =
        "Reference dateTime : ${formateDate(last.dateTime)}\n$providedDescription"

    private fun formatPrice(last: TimeBasedMeasure) = with(last) {
        priceFormatter.format(value).replace("$", "")
    }

    private fun formateDate(target: Date) = dateFormatter.format(target)

    private val priceFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    private const val BIAS = 100f
}