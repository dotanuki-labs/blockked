package io.dotanuki.blockked.dashboard

import io.dotanuki.common.StateMachine

class DashboardViewModel(
    private val machine: StateMachine<DashboardPresentation>,
    private val broker: BitcoinBroker) {


    fun retrieveDashboard() =
        broker
            .marketPrice()
            .map { BuildDashboardPresentation(it) }
            .compose(machine)

}