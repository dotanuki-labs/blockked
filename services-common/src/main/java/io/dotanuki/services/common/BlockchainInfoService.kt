package io.dotanuki.services.common

import io.dotanuki.blockked.domain.SupportedStatistic
import io.reactivex.Observable

interface BlockchainInfoService {

    fun fetchStatistics(statistic : SupportedStatistic) : Observable<BitcoinStatsResponse>

}
