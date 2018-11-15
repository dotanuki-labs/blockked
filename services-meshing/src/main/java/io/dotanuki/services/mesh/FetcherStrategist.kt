package io.dotanuki.services.mesh

import io.dotanuki.blockked.domain.FetchBitcoinStatistic
import io.dotanuki.blockked.domain.FetchStrategy
import io.dotanuki.blockked.domain.FetchStrategy.ForceUpdate
import io.dotanuki.blockked.domain.FetchStrategy.FromPrevious
import io.dotanuki.blockked.domain.SupportedStatistic
import io.dotanuki.services.common.BitcoinInfoMapper
import io.dotanuki.services.common.BlockchainInfoService
import io.dotanuki.services.common.CacheService
import io.reactivex.Observable

class FetcherStrategist(
    private val remote: BlockchainInfoService,
    private val local: CacheService) : FetchBitcoinStatistic {

    override fun execute(target: SupportedStatistic, strategy: FetchStrategy) = when (strategy) {
        is ForceUpdate -> remoteThenCache(target)
        is FromPrevious -> fromCache(target)
    }

    private fun fromCache(target: SupportedStatistic) =
        local.retrieveOrNull(target)
            ?.let { Observable.just(BitcoinInfoMapper(it)) }
            ?: Observable.empty()

    private fun remoteThenCache(statistic: SupportedStatistic) =
        remote
            .fetchStatistics(statistic)
            .doOnNext { local.save(statistic, it) }
            .map { BitcoinInfoMapper(it) }

}