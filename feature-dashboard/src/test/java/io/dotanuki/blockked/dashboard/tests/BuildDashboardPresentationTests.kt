package io.dotanuki.blockked.dashboard.tests

import io.dotanuki.blockked.dashboard.*
import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.BitcoinPrice
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class BuildDashboardPresentationTests {

    @Test fun `should build available chart data from bitcoin info`() {

        val provided = BitcoinInfo(
            providedName = "Market Price (USD)",
            providedDescription = "Average USD market price across major bitcoin exchanges.",
            prices = listOf(
                BitcoinPrice(
                    date = "2018-10-21T22:00:00".toDate(),
                    price = 6498.485833333333f,
                    currencyUnit = "USD"
                ),
                BitcoinPrice(
                    date = "2018-10-22T22:00:00".toDate(),
                    price = 6481.425999999999f,
                    currencyUnit = "USD"
                ),
                BitcoinPrice(
                    date = "2018-10-23T22:00:00".toDate(),
                    price = 6511.321999999999f,
                    currencyUnit = "USD"
                )
            )
        )

        val expected = DashboardPresentation(

            display = DisplayModel(
                formattedValue = "6,511.32",
                title = "Current BTC price",
                subtitle = "Reference date : 2018-10-23\nAverage USD market price across major bitcoin exchanges."
            ),

            chart = ChartModel.AvaliableData(
                minValue = 6481.425999999999f - 100f,
                maxValue = 6511.321999999999f + 100f,
                legend = "Bitcoin price evolution (2018-10-21 to 2018-10-23)",
                values = listOf(
                    PlottableEntry(1.0f, 6498.485833333333f),
                    PlottableEntry(2.0f, 6481.425999999999f),
                    PlottableEntry(3.0f, 6511.321999999999f)
                )
            )
        )

        assertThat(BuildDashboardPresentation(provided)).isEqualTo(expected)
    }

    @Test fun `should build display only, when chart info missing`() {

        val provided = BitcoinInfo(
            providedName = "Market Price (USD)",
            providedDescription = "Average USD market price across major bitcoin exchanges.",
            prices = listOf(
                BitcoinPrice(
                    date = "2018-10-21T22:00:00".toDate(),
                    price = 6498.485833333333f,
                    currencyUnit = "USD"
                )
            )
        )

        val expected = DashboardPresentation(

            display = DisplayModel(
                formattedValue = "6,498.49",
                title = "Current BTC price",
                subtitle = "Reference date : 2018-10-21\nAverage USD market price across major bitcoin exchanges."
            ),

            chart = ChartModel.Unavailable
        )

        assertThat(BuildDashboardPresentation(provided)).isEqualTo(expected)

    }

    private fun String.toDate(): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.parse(this)
    }

}