package io.dotanuki.services.common

import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.TimeBasedMeasure
import java.util.*

object BitcoinInfoMapper {

    operator fun invoke(response: BitcoinStatsResponse) = with(response) {
        BitcoinStatistic(
            providedName = name,
            providedDescription = description,
            unitName = unit,
            measures = values.map {
                TimeBasedMeasure(
                    dateTime = assembleFixedTimeDate(it.timestamp),
                    value = it.value
                )
            }
        )
    }

    private fun assembleFixedTimeDate(timestamp: Long): Date {
        val timeDoesNotMatter = Date(timestamp * 1000)

        val calendar = Calendar.getInstance().apply {
            time = timeDoesNotMatter
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        return calendar.time
    }

}