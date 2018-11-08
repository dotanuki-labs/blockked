package io.dotanuki.services.mesh

import io.dotanuki.blockked.domain.FetchBitcoinStatistic
import io.dotanuki.services.common.BitcoinInfoMapper
import io.dotanuki.services.common.CacheEntry

class BitcoinMarketPrice(private val mesh: OfflineFirstFetching) : FetchBitcoinStatistic {

    override fun execute() =
        mesh(CacheEntry.BTCPrice) { it.averageBitcoinPrice() }
            .map { BitcoinInfoMapper(it) }

}