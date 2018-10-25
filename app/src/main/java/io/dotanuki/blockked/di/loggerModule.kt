package io.dotanuki.blockked.di

import io.dotanuki.blockked.LogcatLogger
import io.dotanuki.logger.Logger
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val loggerModule = Kodein.Module("logger") {

    bind<Logger>() with singleton {
        LogcatLogger
    }
}