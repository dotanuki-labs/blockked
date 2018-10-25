package io.dotanuki.service.blockchaininfo.di

import io.dotanuki.blockked.domain.BitcoinBroker
import io.dotanuki.networking.BuildRetrofit
import io.dotanuki.networking.di.networkingModule
import io.dotanuki.service.blockchaininfo.BlockchainInfo
import io.dotanuki.service.blockchaininfo.BrokerInfrastructure
import io.dotanuki.service.blockchaininfo.ExecutionErrorHandler
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val blockchainInfoModule = Kodein.Module("service-blockchainfo") {

    import(networkingModule)

    bind() from singleton {

        val retrofit = BuildRetrofit(
            apiURL = BlockchainInfo.API_URL,
            httpClient = instance()
        )

        retrofit.create(BlockchainInfo::class.java)
    }

    bind<BitcoinBroker>() with provider {
        BrokerInfrastructure(
            targetScheduler = Schedulers.io(),
            service = instance(),
            errorHandler = ExecutionErrorHandler(
                logger = instance()
            )
        )
    }

}