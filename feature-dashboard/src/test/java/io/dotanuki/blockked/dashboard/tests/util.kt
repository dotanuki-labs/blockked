package io.dotanuki.blockked.dashboard.tests

import java.text.SimpleDateFormat
import java.util.*

internal fun String.toDate(): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.parse(this)
}
