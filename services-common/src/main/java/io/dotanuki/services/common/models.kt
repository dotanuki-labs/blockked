package io.dotanuki.services.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitcoinStatsResponse(
    val name: String,
    val description: String,
    val unit: String,
    val values: List<StatisticPoint>
)

@Serializable
data class StatisticPoint(
    @SerialName("x") val timestamp: Long,
    @SerialName("y") val price: Float
)
