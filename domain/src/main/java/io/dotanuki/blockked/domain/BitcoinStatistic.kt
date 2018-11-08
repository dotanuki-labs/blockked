package io.dotanuki.blockked.domain

data class BitcoinStatistic(

    val providedName: String,
    val providedDescription: String,
    val prices: List<TimeBasedMeasure>

)