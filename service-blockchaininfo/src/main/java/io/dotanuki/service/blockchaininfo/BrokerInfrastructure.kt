package io.dotanuki.service.blockchaininfo

import io.dotanuki.services.common.BlockchainInfoService
import io.dotanuki.services.common.MarketPriceResponse
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

internal class BrokerInfrastructure(
    private val service: BlockchainInfo,
    private val errorHandler: ExecutionErrorHandler<MarketPriceResponse>,
    private val targetScheduler: Scheduler = Schedulers.trampoline()) : BlockchainInfoService {

    override fun averageBitcoinPrice() =
        service
            .marketPrice()
            .subscribeOn(targetScheduler)
            .compose(errorHandler)

}