package io.dotanuki.blockked.dashboard.di

import io.dotanuki.blockked.dashboard.BitcoinBroker
import io.dotanuki.blockked.dashboard.DashboardViewModel
import io.dotanuki.common.StateMachine
import io.dotanuki.common.di.sharedModule
import io.reactivex.android.schedulers.AndroidSchedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val dashboardModule = Kodein.Module("dashboard") {

    import(sharedModule)

    bind() from provider {
        BitcoinBroker(
            cache = instance(),
            blockchainInfo = instance()
        )
    }

    bind() from provider {

        DashboardViewModel(
            broker = instance(),
            machine = StateMachine(
                uiScheduler = AndroidSchedulers.mainThread()
            )
        )
    }

}