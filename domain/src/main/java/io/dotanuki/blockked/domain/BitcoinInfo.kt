package io.dotanuki.blockked.domain

data class BitcoinInfo(

    val providedName: String,
    val providedDescription: String,
    val prices: List<BitcoinPrice>

)