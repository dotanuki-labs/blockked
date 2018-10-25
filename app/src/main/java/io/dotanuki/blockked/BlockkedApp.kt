package io.dotanuki.blockked

import android.app.Application
import io.dotanuki.blockked.dashboard.di.dashboardModule
import io.dotanuki.blockked.di.loggerModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class BlocckedApp : Application(), KodeinAware {

    var overrideBindings: Kodein.MainBuilder.() -> Unit = {}

    override val kodein = Kodein {
        import(loggerModule)
        import(dashboardModule)
    }

}