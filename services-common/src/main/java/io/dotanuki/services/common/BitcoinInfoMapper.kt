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
                    date = Date(it.timestamp * 1000),
                    price = it.price,
                    currencyUnit = currency
                )
            }

}