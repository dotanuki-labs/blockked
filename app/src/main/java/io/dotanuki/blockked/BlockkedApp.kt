package io.dotanuki.blockked

import android.app.Application
import io.dotanuki.blockked.dashboards.di.dashboardModule
import io.dotanuki.blockked.di.loggerModule
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class BlockkedApp : Application(), KodeinAware {

    override val kodein = ConfigurableKodein(mutable = true).apply {

        addImport(loggerModule)
        addImport(dashboardModule)

        addConfig {
            bind<Application>() with singleton {
                this@BlockkedApp
            }
        }
    }

}