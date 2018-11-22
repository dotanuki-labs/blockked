package io.dotanuki.blockked

import com.nhaarman.mockitokotlin2.mock
import io.dotanuki.blockked.dashboards.DashboardActivity
import io.dotanuki.blockked.domain.*
import io.dotanuki.blockked.rules.BindingsOverwriter
import io.dotanuki.blockked.rules.ScreenLauncher
import io.dotanuki.common.toDate
import org.junit.Rule
import org.junit.Test
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class DashboardAcceptanceTests {

    private val broker = mock<FetchBitcoinStatistic>()

    @get:Rule val launcher = ScreenLauncher(DashboardActivity::class)

    @get:Rule val overwriter = BindingsOverwriter {

        bind<FetchBitcoinStatistic>(overrides = true) with provider {
            broker
        }
    }

    val infoForGraphAndDisplay = BitcoinStatistic(
        providedName = "Market Price (USD)",
        providedDescription = "Average USD market value across major bitcoin exchanges.",
        unitName = "USD",
        measures = listOf(
            TimeBasedMeasure(
                dateTime = "2018-10-21T22:00:00".toDate(),
                value = 6498.48f
            ),
            TimeBasedMeasure(
                dateTime = "2018-10-22T22:00:00".toDate(),
                value = 6481.42f
            ),
            TimeBasedMeasure(
                dateTime = "2018-10-23T22:00:00".toDate(),
                value = 6511.32f
            )
        )
    )

    val justOneBitcoinValue = BitcoinStatistic(
        providedName = "Market Price (USD)",
        providedDescription = "Average USD market value across major bitcoin exchanges.",
        unitName = "USD",
        measures = listOf(
            TimeBasedMeasure(
                dateTime = "2018-10-21T22:00:00".toDate(),
                value = 6498.48f
            )
        )
    )

    @Test fun atDashboardLaunch_givenSuccessAndSeveralBitcoinValues_ThenDisplayAndGraphShown() {

        given(broker) {
            defineScenario {
                criteria = DataFechted(infoForGraphAndDisplay)
            }
        }

        launcher.launchScreen()

        assertThat {

            loadingIndicator {
                should be hidden
            }

            errorReport {
                should be hidden
            }

            dashboard {
                should have DisplayAndGraph(bitcoinValue = "$6,511.32")
            }
        }
    }

    @Test fun atDashboardLaunch_givenJustOneBitcoinValue_ThenOnlyDisplayIsShown() {

        given(broker) {
            defineScenario {
                criteria = DataFechted(justOneBitcoinValue)
            }
        }

        launcher.launchScreen()

        assertThat {

            loadingIndicator {
                should be hidden
            }

            errorReport {
                should be hidden
            }

            dashboard {
                should have OnlyDisplay(bitcoinValue = "$6,498.48")
            }
        }
    }

    @Test fun atDashboardLaunch_givenNetworkingError_thenErrorReported() {
        val networkingError = NetworkingIssue.OperationTimeout
        checkErrorState(networkingError)
    }

    @Test fun atDashboardLaunch_givenIntegrationError_thenErrorReported() {
        val integrationError = RemoteIntegrationIssue.RemoteSystem
        checkErrorState(integrationError)
    }

    private fun checkErrorState(error: Throwable) {

        given(broker) {
            defineScenario {
                criteria = IssueFound(error)
            }
        }

        launcher.launchScreen()

        assertThat {

            loadingIndicator {
                should be hidden
            }

//            errorReport {
//                should be displayedWith(error.toString())
//            }

            dashboard {
                should have noEntries
            }
        }
    }
}