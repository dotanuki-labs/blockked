package io.dotanuki.services.common.tests

import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.TimeBasedMeasure
import io.dotanuki.services.common.BitcoinInfoMapper
import io.dotanuki.services.common.BitcoinStatsResponse
import io.dotanuki.services.common.StatisticPoint
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class BitcoinInfoMapperTests {

    @Test fun `should map to Bitcoininfo from parsed MarketPrice model`() {

        val response = BitcoinStatsResponse(
            name = "Market Price (USD)",
            description = "Average USD market value across major bitcoin exchanges.",
            unit = "USD",
            values = listOf(
                StatisticPoint(
                    timestamp = 1540166400,
                    value = 6498.485833333333f
                ),
                StatisticPoint(
                    timestamp = 1540252800,
                    value = 6481.425999999999f
                )
            )
        )


        val expected = BitcoinStatistic(
            providedName = "Market Price (USD)",
            providedDescription = "Average USD market value across major bitcoin exchanges.",
            measures = listOf(
                TimeBasedMeasure(
                    dateTime = "2018-10-21".toDate(),
                    value = 6498.485833333333f
                ),
                TimeBasedMeasure(
                    dateTime = "2018-10-22".toDate(),
                    value = 6481.425999999999f
                )
            )
        )

        assertThat(BitcoinInfoMapper(response)).isEqualTo(expected)
    }

    private fun String.toDate(): Date {

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val parsed = formatter.parse(this)
        val calendar = Calendar.getInstance().apply {
            time = parsed
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        return calendar.time
    }

}