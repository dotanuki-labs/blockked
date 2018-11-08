package io.dotanuki.services.mesh

import io.dotanuki.services.common.BlockchainInfoService
import io.dotanuki.services.common.CacheEntry
import io.dotanuki.services.common.CacheService
import io.dotanuki.services.common.MarketPriceResponse
import io.reactivex.Observable

class OfflineFirstFetching(
    private val remote: BlockchainInfoService,
    private val local: CacheService) {

    operator fun invoke(
        target: CacheEntry,
        fetching: (BlockchainInfoService) -> Observable<MarketPriceResponse>) =
            remoteThenCache(target, fetching).let { update ->
                local.retrieveOrNull(target)
                    ?.let { Observable.just(it).concatWith(update) }
                    ?: update
            }

    fun remoteThenCache(
        target: CacheEntry,
        fetch: (BlockchainInfoService) -> Observable<MarketPriceResponse>) =
            fetch
                .invoke(remote)
                .doOnNext { local.save(target, it) }
}