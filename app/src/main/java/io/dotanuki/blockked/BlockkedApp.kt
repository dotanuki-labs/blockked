package io.dotanuki.blockked

import android.app.Application
import io.dotanuki.blockked.dashboard.di.dashboardModule
import io.dotanuki.blockked.di.loggerModule
import org.kodein.di.KodeinAware
import org.kodein.di.conf.ConfigurableKodein

class BlockkedApp : Application(), KodeinAware {

    override val kodein =  ConfigurableKodein(mutable = true).apply {
        addImport(loggerModule)
        addImport(dashboardModule)
    }

}