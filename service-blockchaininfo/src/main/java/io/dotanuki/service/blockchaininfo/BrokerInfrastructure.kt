package io.dotanuki.service.blockchaininfo

import io.dotanuki.blockked.domain.BitcoinBroker
import io.dotanuki.service.blockchaininfo.models.MarketPriceResponse
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

internal class BrokerInfrastructure(
    private val service: BlockchainInfo,
    private val targetScheduler: Scheduler = Schedulers.trampoline(),
    private val errorHandler: ExecutionErrorHandler<MarketPriceResponse>) : BitcoinBroker {

    override fun marketPrice() =
        service.marketPrice()
            .subscribeOn(targetScheduler)
            .compose(errorHandler)
            .map { BitcoinInfoMapper(it) }

}