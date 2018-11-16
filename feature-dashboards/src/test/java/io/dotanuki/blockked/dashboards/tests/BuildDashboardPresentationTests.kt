package io.dotanuki.blockked.dashboards.tests

import io.dotanuki.blockked.dashboards.*
import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.TimeBasedMeasure
import io.dotanuki.common.toDate
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class BuildDashboardPresentationTests {

    @Test fun `should build available chart data from bitcoin info`() {

        val provided = BitcoinStatistic(
            providedName = "Market Price (USD)",
            providedDescription = "Average USD market value across major bitcoin exchanges.",
            unitName = "USD",
            measures = listOf(
                TimeBasedMeasure(
                    dateTime = "2018-10-21T22:00:00".toDate(),
                    value = 6498.485833333333f
                ),
                TimeBasedMeasure(
                    dateTime = "2018-10-22T22:00:00".toDate(),
                    value = 6481.425999999999f
                ),
                TimeBasedMeasure(
                    dateTime = "2018-10-23T22:00:00".toDate(),
                    value = 6511.321999999999f
                )
            )
        )


        val expected = DashboardPresentation(

            display = DisplayModel(
                formattedValue = "$6,511.32",
                title = "Market Price (USD)",
                subtitle = "Average USD market value across major bitcoin exchanges."
            ),

            chart = ChartModel.AvaliableData(
                minValue = 6481.425999999999f - 6481.425999999999f * 0.05f,
                maxValue = 6511.321999999999f + 6511.321999999999f * 0.05f,
                legend = "Data sampled, from Oct 21, 2018 to Oct 23, 2018",
                values = listOf(
                    PlottableEntry(1.0f, 6498.485833333333f),
                    PlottableEntry(2.0f, 6481.425999999999f),
                    PlottableEntry(3.0f, 6511.321999999999f)
                )
            )
        )

        val statistics = listOf(provided)
        val presentations = listOf(expected)

        assertThat(BuildDashboardPresentation(statistics)).isEqualTo(presentations)
    }

    @Test fun `should build display only, when chart info missing`() {

        val provided = BitcoinStatistic(
            providedName = "Market Price (USD)",
            providedDescription = "Average USD market value across major bitcoin exchanges.",
            unitName = "USD",
            measures = listOf(
                TimeBasedMeasure(
                    dateTime = "2018-10-21T22:00:00".toDate(),
                    value = 6498.485833333333f
                )
            )
        )

        val expected = DashboardPresentation(

            display = DisplayModel(
                formattedValue = "$6,498.49",
                title = "Market Price (USD)",
                subtitle = "Average USD market value across major bitcoin exchanges."
            ),

            chart = ChartModel.Unavailable
        )


        val statistics = listOf(provided)
        val presentations = listOf(expected)

        assertThat(BuildDashboardPresentation(statistics)).isEqualTo(presentations)

    }


}