package io.dotanuki.service.blockchaininfo

import io.dotanuki.blockked.domain.SupportedStatistic
import io.dotanuki.services.common.BitcoinStatsResponse
import io.dotanuki.services.common.BlockchainInfoService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

internal class BrokerInfrastructure(
    private val service: BlockchainInfo,
    private val errorHandler: ExecutionErrorHandler<BitcoinStatsResponse>,
    private val targetScheduler: Scheduler = Schedulers.trampoline()) : BlockchainInfoService {

    override fun fetchStatistics(statistic: SupportedStatistic): Observable<BitcoinStatsResponse> {
        return service
            .fetchWith(statistic.toString(), ARGS)
            .subscribeOn(targetScheduler)
            .compose(errorHandler)
    }

    private companion object {
        val ARGS = mapOf(
            "timespan" to "4weeks",
            "format" to "json"
        )
    }

}