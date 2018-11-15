package io.dotanuki.blockked.dashboards

import io.dotanuki.blockked.domain.RetrieveStatistics
import io.dotanuki.common.StateMachine

class DashboardViewModel(
    private val machine: StateMachine<List<DashboardPresentation>>,
    private val usecase: RetrieveStatistics) {

    fun retrieveDashboard() =
        usecase
            .execute()
            .map { BuildDashboardPresentation(it) }
            .compose(machine)


}