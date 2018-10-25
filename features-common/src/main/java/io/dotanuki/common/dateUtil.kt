package io.dotanuki.common

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.parse(this)
}
