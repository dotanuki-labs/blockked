package io.dotanuki.blockked.dashboard

import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.CacheEntry
import io.dotanuki.blockked.domain.services.BlockchainInfoService
import io.dotanuki.blockked.domain.services.CacheService
import io.reactivex.Observable

class BitcoinBroker(
    private val cache: CacheService,
    private val blockchainInfo: BlockchainInfoService
) {

    fun marketPrice() =
        cache.retrieveOrNull<BitcoinInfo>(KEY)
            ?.let { Observable.just(it).concatWith(fetchRemotelyThenCache()) }
            ?: fetchRemotelyThenCache()

    private fun fetchRemotelyThenCache() =
        blockchainInfo
            .averageBitcoinPrice()
            .doOnNext { cache.save(KEY, it) }

    companion object {
        val KEY = CacheEntry.AverageBTCPrice
    }

}