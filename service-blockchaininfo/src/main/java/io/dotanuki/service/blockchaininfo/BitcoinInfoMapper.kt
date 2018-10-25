package io.dotanuki.service.blockchaininfo

import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.BitcoinPrice
import io.dotanuki.service.blockchaininfo.models.BTCPriceResponse
import io.dotanuki.service.blockchaininfo.models.MarketPriceResponse
import java.util.*

internal object BitcoinInfoMapper {

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