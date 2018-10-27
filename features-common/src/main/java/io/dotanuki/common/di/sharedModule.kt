package io.dotanuki.common.di

import io.dotanuki.service.blockchaininfo.di.blockchainInfoModule
import io.dotanuki.services.cache.di.cacheModule
import org.kodein.di.Kodein

val sharedModule = Kodein.Module("shared") {

    importOnce(cacheModule)
    importOnce(blockchainInfoModule)

}