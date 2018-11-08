package io.dotanuki.blockked.dashboard.tests

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.blockked.dashboard.BuildDashboardPresentation
import io.dotanuki.blockked.dashboard.DashboardViewModel
import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.FetchBitcoinStatistic
import io.dotanuki.blockked.domain.NetworkingIssue
import io.dotanuki.blockked.domain.TimeBasedMeasure
import io.dotanuki.common.*
import io.reactivex.Observable
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.given
import org.junit.Before
import org.junit.Test

class DashboardViewModelTests {

    lateinit var viewModel: DashboardViewModel

    val mockedBrocker = mock<FetchBitcoinStatistic>()

    val broking = BitcoinStatistic(
        providedName = "Market Price (USD)",
        providedDescription = "Average USD market value across major bitcoin exchanges.",
        prices = listOf(
            TimeBasedMeasure(
                dateTime = "2018-10-21T22:00:00".toDate(),
                value = 6498.485833333333f
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
        whenever(mockedBrocker.execute())
            .thenReturn(
                Observable.just(broking)
            )

        given(viewModel.retrieveDashboard()) {

            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                items match sequenceOf(
                    Launched,
                    Result(BuildDashboardPresentation(broking)),
                    Done
                )
            }
        }
    }

    @Test fun `should emmit states for errored broking integration`() {
        whenever(mockedBrocker.execute())
            .thenReturn(
                Observable.error<BitcoinStatistic>(NetworkingIssue.ConnectionSpike)
            )

        given(viewModel.retrieveDashboard()) {

            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                items match sequenceOf(
                    Launched,
                    Failed(NetworkingIssue.ConnectionSpike),
                    Done
                )
            }
        }
    }
}