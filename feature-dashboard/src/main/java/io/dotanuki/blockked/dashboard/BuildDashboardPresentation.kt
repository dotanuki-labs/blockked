package io.dotanuki.blockked.dashboard

import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.BitcoinPrice
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object BuildDashboardPresentation {

    operator fun invoke(info: BitcoinInfo) = with(info) {
        DashboardPresentation(

            display = DisplayModel(
                formattedValue = formatPrice(prices.last()),
                title = "Current BTC price",
                subtitle = formatSubtitle(prices.last(), providedDescription)
            ),

            chart = assembleChart(info)

        )
    }

    private fun assembleChart(info: BitcoinInfo) = with(info) {
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

    private fun buildEntries(prices: List<BitcoinPrice>) =
        prices.mapIndexed { index, bitcoinPrice ->
            val x = (index + 1).toFloat()
            PlottableEntry(x, bitcoinPrice.price)
        }

    private fun formatLegend(first: BitcoinPrice, last: BitcoinPrice) =
        "Bitcoin price evolution (${formateDate(first.date)} to ${formateDate(last.date)})"

    private fun extractMaximum(prices: List<BitcoinPrice>) =
        prices.asSequence().map { it.price }.max()
            ?.let { it + BIAS }
            ?: throw IllegalArgumentException("No maximum")

    private fun extractMinimum(prices: List<BitcoinPrice>) =
        prices.asSequence().map { it.price }.min()
            ?.let { it - BIAS }
            ?: throw IllegalArgumentException("No minimum")

    private fun formatSubtitle(last: BitcoinPrice, providedDescription: String) =
        "Reference date : ${formateDate(last.date)}\n$providedDescription"

    private fun formatPrice(last: BitcoinPrice) = with(last) {
        priceFormatter.format(price).replace("$", "")
    }

    private fun formateDate(target: Date) = dateFormatter.format(target)

    private val priceFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    private const val BIAS = 100f
}