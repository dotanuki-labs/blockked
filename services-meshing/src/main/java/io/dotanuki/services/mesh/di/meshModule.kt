package io.dotanuki.services.mesh.di

import io.dotanuki.blockked.domain.FetchBitcoinStatistic
import io.dotanuki.service.blockchaininfo.di.blockchainInfoModule
import io.dotanuki.services.cache.di.cacheModule
import io.dotanuki.services.mesh.BitcoinMarketPrice
import io.dotanuki.services.mesh.OfflineFirstFetching
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val meshModule = Kodein.Module("services-mesh") {

    importOnce(cacheModule)
    importOnce(blockchainInfoModule)

    bind<FetchBitcoinStatistic>() with provider {
        BitcoinMarketPrice(
            mesh = OfflineFirstFetching(
                remote = instance(),
                local = instance()
            )
        )
    }
}