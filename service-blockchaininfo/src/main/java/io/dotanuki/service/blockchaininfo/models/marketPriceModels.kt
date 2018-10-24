package io.dotanuki.service.blockchaininfo.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MarketPriceResponse(
    val name: String,
    val description: String,
    val unit: String,
    val values: List<BTCPriceResponse>
)

@Serializable
internal data class BTCPriceResponse(
    @SerialName("x") val timestamp: Long,
    @SerialName("y") val price: Float
)
