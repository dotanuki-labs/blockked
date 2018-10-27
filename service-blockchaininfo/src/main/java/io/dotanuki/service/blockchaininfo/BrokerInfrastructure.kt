package io.dotanuki.service.blockchaininfo

import io.dotanuki.blockked.domain.services.BlockchainInfoService
import io.dotanuki.services.common.BitcoinInfoMapper
import io.dotanuki.services.common.MarketPriceResponse
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

internal class BrokerInfrastructure(
    private val service: BlockchainInfo,
    private val targetScheduler: Scheduler = Schedulers.trampoline(),
    private val errorHandler: ExecutionErrorHandler<MarketPriceResponse>) : BlockchainInfoService {

    override fun averageBitcoinPrice() =
        service.marketPrice()
            .subscribeOn(targetScheduler)
            .compose(errorHandler)
            .map { BitcoinInfoMapper(it) }

}