package io.dotanuki.services.common.tests

import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.BitcoinPrice
import io.dotanuki.services.common.BTCPriceResponse
import io.dotanuki.services.common.BitcoinInfoMapper
import io.dotanuki.services.common.BitcoinStatsResponse
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class BitcoinInfoMapperTests {

    @Test
    fun `should map to Bitcoininfo from parsed MarketPrice model`() {

        val response = BitcoinStatsResponse(
            name = "Market Price (USD)",
            description = "Average USD market price across major bitcoin exchanges.",
            unit = "USD",
            values = listOf(
                BTCPriceResponse(
                    timestamp = 1540166400,
                    price = 6498.485833333333f
                ),
                BTCPriceResponse(
                    timestamp = 1540252800,
                    price = 6481.425999999999f
                )
            )
        )


        val expected = BitcoinStatistic(
            providedName = "Market Price (USD)",
            providedDescription = "Average USD market price across major bitcoin exchanges.",
            prices = listOf(
                BitcoinPrice(
                    date = "2018-10-21".toDate(),
                    price = 6498.485833333333f,
                    currencyUnit = "USD"
                ),
                BitcoinPrice(
                    date = "2018-10-22".toDate(),
                    price = 6481.425999999999f,
                    currencyUnit = "USD"
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