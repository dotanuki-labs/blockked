package io.dotanuki.common.di

import io.dotanuki.common.Disposer
import io.dotanuki.services.mesh.di.meshModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val sharedModule = Kodein.Module("shared") {

    importOnce(meshModule)

    bind() from provider {
        Disposer(
            logger = instance()
        )
    }
}