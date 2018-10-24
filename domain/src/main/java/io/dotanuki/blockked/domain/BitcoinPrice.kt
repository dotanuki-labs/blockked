package io.dotanuki.blockked.domain

import java.util.*

data class BitcoinPrice(
    val dayOfYear: Date,
    val price: Float,
    val currencyUnit: String
)