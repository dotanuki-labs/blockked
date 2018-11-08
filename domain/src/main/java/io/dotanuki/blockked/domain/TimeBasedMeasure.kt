package io.dotanuki.blockked.domain

import java.util.*

data class TimeBasedMeasure(
    val dateTime: Date,
    val value: Float
)