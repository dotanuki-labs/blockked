package io.dotanuki.blockked.domain.services

import io.dotanuki.blockked.domain.CacheEntry

interface CacheService {

    fun <T> save(key: CacheEntry, value: T)

    fun <T> retrieveOrDefault(key: CacheEntry, fallback: T): T

    fun <T> retrieveOrNull(key: CacheEntry): T?

    fun remove(key: CacheEntry)

    fun purge()

}