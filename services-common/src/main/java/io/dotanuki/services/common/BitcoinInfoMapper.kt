package io.dotanuki.services.common

import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.BitcoinPrice
import java.util.*

object BitcoinInfoMapper {

    operator fun invoke(response: MarketPriceResponse) = with(response) {
        BitcoinInfo(
            providedName = name,
            providedDescription = description,
            prices = convertPrice(values, unit)
        )
    }

    private fun convertPrice(values: List<BTCPriceResponse>, currency: String) =
        values
            .map {
                BitcoinPrice(
                    date = assembleUTCDate(it.timestamp),
                    price = it.price,
                    currencyUnit = currency
                )
            }

    private fun assembleUTCDate(timestamp: Long): Date {
        val timeDoesNotMatter = Date(timestamp * 1000)

        val calendar = Calendar.getInstance().apply {
            time = timeDoesNotMatter
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        return calendar.time
    }

}