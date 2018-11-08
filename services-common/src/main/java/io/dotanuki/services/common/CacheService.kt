package io.dotanuki.services.common

interface CacheService {

    fun save(key: CacheEntry, value: BitcoinStatsResponse)

    fun retrieveOrNull(key: CacheEntry): BitcoinStatsResponse?

    fun remove(key: CacheEntry)

    fun purge()

}