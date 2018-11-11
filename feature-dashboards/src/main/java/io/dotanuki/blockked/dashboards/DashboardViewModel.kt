package io.dotanuki.blockked.dashboards

import io.dotanuki.blockked.domain.FetchBitcoinStatistic
import io.dotanuki.common.StateMachine

class DashboardViewModel(
    private val machine: StateMachine<DashboardPresentation>,
    private val broker: FetchBitcoinStatistic) {


    fun retrieveDashboard() =
        broker
            .execute()
            .map { BuildDashboardPresentation(it) }
            .compose(machine)

}