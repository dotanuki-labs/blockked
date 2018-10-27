package io.dotanuki.blockked.dashboard.tests

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.blockked.dashboard.BitcoinBroker
import io.dotanuki.blockked.dashboard.BuildDashboardPresentation
import io.dotanuki.blockked.dashboard.DashboardViewModel
import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.BitcoinPrice
import io.dotanuki.blockked.domain.NetworkingIssue
import io.dotanuki.common.*
import io.reactivex.Observable
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.given
import org.junit.Before
import org.junit.Test

class DashboardViewModelTests {

    lateinit var viewModel: DashboardViewModel

    val mockedBrocker = mock<BitcoinBroker>()

    val broking = BitcoinInfo(
        providedName = "Market Price (USD)",
        providedDescription = "Average USD market price across major bitcoin exchanges.",
        prices = listOf(
            BitcoinPrice(
                date = "2018-10-21T22:00:00".toDate(),
                price = 6498.485833333333f,
                currencyUnit = "USD"
            )
        )
    )

    @Before fun `before each test`() {
        viewModel = DashboardViewModel(
            broker = mockedBrocker,
            machine = StateMachine()
        )
    }

    @Test fun `should emmit states for successful dashboard presentation`() {
        whenever(mockedBrocker.marketPrice())
            .thenReturn(
                Observable.just(broking)
            )

        given(viewModel.retrieveDashboard()) {

            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                items match sequenceOf(
                    InFlight,
                    Result(BuildDashboardPresentation(broking)),
                    Done
                )
            }
        }
    }

    @Test fun `should emmit states for errored broking integration`() {
        whenever(mockedBrocker.marketPrice())
            .thenReturn(
                Observable.error<BitcoinInfo>(NetworkingIssue.ConnectionSpike)
            )

        given(viewModel.retrieveDashboard()) {

            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                items match sequenceOf(
                    InFlight,
                    Failed(NetworkingIssue.ConnectionSpike),
                    Done
                )
            }
        }
    }
}