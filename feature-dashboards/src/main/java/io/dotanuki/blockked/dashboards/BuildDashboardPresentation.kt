package io.dotanuki.blockked.dashboards

import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.TimeBasedMeasure
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object BuildDashboardPresentation {

    operator fun invoke(info: List<BitcoinStatistic>) = info.map {
        DashboardPresentation(

            display = DisplayModel(
                title = it.providedName,
                subtitle = it.providedDescription,
                formattedValue = formatPrice(it.measures.last())
            ),

            chart = assembleChart(it)
        )
    }

    private fun assembleChart(info: BitcoinStatistic) = with(info) {
        if (measures.size < 2) ChartModel.Unavailable
        else {
            ChartModel.AvaliableData(
                minValue = extractMinimum(measures),
                maxValue = extractMaximum(measures),
                legend = formatLegend(measures.first(), measures.last()),
                values = buildEntries(measures)

            )
        }
    }

    private fun buildEntries(prices: List<TimeBasedMeasure>) =
        prices.mapIndexed { index, bitcoinPrice ->
            val x = (index + 1).toFloat()
            PlottableEntry(x, bitcoinPrice.value)
        }

    private fun formatLegend(first: TimeBasedMeasure, last: TimeBasedMeasure) =
        "Data sampled, from ${formateDate(first.dateTime)} to ${formateDate(last.dateTime)}"

    private fun extractMaximum(prices: List<TimeBasedMeasure>) =
        prices.asSequence().map { it.value }.max()
            ?.let { if (it == 0.0f) 10.0f else it + it * 0.05f }
            ?: throw IllegalArgumentException("No maximum")

    private fun extractMinimum(prices: List<TimeBasedMeasure>) =
        prices.asSequence().map { it.value }.min()
            ?.let { if (it == 0.0f) -10.0f else it - it * 0.05f }
            ?: throw IllegalArgumentException("No minimum")

    private fun formatPrice(last: TimeBasedMeasure) = with(last) {
        priceFormatter.format(value).replace("$", "")
    }

    private fun formateDate(target: Date) = dateFormatter.format(target)

    private val priceFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

}