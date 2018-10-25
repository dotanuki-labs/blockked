package io.dotanuki.common.di

import io.dotanuki.service.blockchaininfo.di.blockchainInfoModule
import org.kodein.di.Kodein

val sharedModule = Kodein.Module("shared") {

    importOnce(blockchainInfoModule)

}