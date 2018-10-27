package io.dotanuki.services.cache.di

import android.app.Application
import io.dotanuki.blockked.domain.services.CacheService
import io.dotanuki.services.cache.PersistantCache
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val cacheModule = Kodein.Module("service-cache") {

    bind<CacheService>() with singleton {
        PersistantCache(
            context = instance<Application>()
        )
    }
}