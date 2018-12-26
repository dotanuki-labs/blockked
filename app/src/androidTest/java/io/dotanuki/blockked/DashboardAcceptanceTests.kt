package io.dotanuki.blockked

import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import io.dotanuki.blockked.dashboards.DashboardActivity
import io.dotanuki.blockked.domain.*
import io.dotanuki.blockked.rules.BindingsOverwriter
import io.dotanuki.blockked.rx2idlerktx.Rx2IdlerKtx
import io.dotanuki.common.toDate
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider


@RunWith(AndroidJUnit4::class)
class DashboardAcceptanceTests {

    init {
        RxJavaPlugins.setInitIoSchedulerHandler(
            Rx2IdlerKtx.create("RxJava2-IOScheduler")
        )
    }


    private val broker = mock<FetchBitcoinStatistic>()

    @get:Rule val overwriter = BindingsOverwriter {
        bind<FetchBitcoinStatistic>(overrides = true) with provider {
            broker
        }
    }

    @Test
    fun atDashboardLaunch_givenSuccessAndSeveralBitcoinValues_ThenDisplayAndGraphShown() {

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

        given(broker) {
            defineScenario {
                criteria = DataFechted(infoForGraphAndDisplay)
            }
        }

        val scenario = launchActivity<DashboardActivity>().apply {
            moveToState(RESUMED)
        }

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

        scenario.close()
    }

    @Test fun atDashboardLaunch_givenJustOneBitcoinValue_ThenOnlyDisplayIsShown() {

        val justOneValueAtChart = BitcoinStatistic(
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

        given(broker) {
            defineScenario {
                criteria = DataFechted(justOneValueAtChart)
            }
        }

        val scenario = launchActivity<DashboardActivity>().apply {
            moveToState(RESUMED)
        }

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

        scenario.close()

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

        val scenario = launchActivity<DashboardActivity>().apply {
            moveToState(RESUMED)
        }

        assertThat {

            loadingIndicator {
                should be hidden
            }

            errorReport {
                should be displayedWith(error.toString())
            }

            dashboard {
                should have noEntries
            }
        }

        scenario.close()

    }
}