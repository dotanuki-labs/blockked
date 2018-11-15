package io.dotanuki.blockked.dashboards.di

import io.dotanuki.blockked.dashboards.DashboardViewModel
import io.dotanuki.blockked.domain.RetrieveStatistics
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

        RetrieveStatistics(
            fetcher = instance()
        )
    }


    bind() from provider {

        DashboardViewModel(
            usecase = instance(),
            machine = StateMachine(
                uiScheduler = AndroidSchedulers.mainThread()
            )
        )
    }

}