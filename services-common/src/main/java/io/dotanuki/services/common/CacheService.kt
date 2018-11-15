package io.dotanuki.services.common

import io.dotanuki.blockked.domain.SupportedStatistic

interface CacheService {

    fun save(key: SupportedStatistic, value: BitcoinStatsResponse)

    fun retrieveOrNull(key: SupportedStatistic): BitcoinStatsResponse?

    fun remove(key: SupportedStatistic)

    fun purge()

}