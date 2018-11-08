package io.dotanuki.services.common

interface CacheService {

    fun save(key: CacheEntry, value: MarketPriceResponse)

    fun retrieveOrNull(key: CacheEntry): MarketPriceResponse?

    fun remove(key: CacheEntry)

    fun purge()

}