package io.dotanuki.blockked.dashboards.tests

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.blockked.dashboards.BuildDashboardPresentation
import io.dotanuki.blockked.dashboards.DashboardViewModel
import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.NetworkingIssue
import io.dotanuki.blockked.domain.RetrieveStatistics
import io.dotanuki.blockked.domain.TimeBasedMeasure
import io.dotanuki.common.*
import io.reactivex.Observable
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.given
import org.junit.Before
import org.junit.Test

class DashboardViewModelTests {

    lateinit var viewModel: DashboardViewModel

    val mockedFetcher = mock<RetrieveStatistics>()

    val statistic = BitcoinStatistic(
        providedName = "Market Price (USD)",
        providedDescription = "Average USD market value across major bitcoin exchanges.",
        unitName = "USD",
        measures = listOf(
            TimeBasedMeasure(
                dateTime = "2018-10-21T22:00:00".toDate(),
                value = 6498.485833333333f
            )
        )
    )

    @Before fun `before each test`() {
        viewModel = DashboardViewModel(
            usecase = mockedFetcher,
            machine = StateMachine()
        )
    }

    @Test fun `should emmit states for successful dashboard presentation`() {

        val provided = listOf(statistic)
        val expected = BuildDashboardPresentation(provided)

        whenever(mockedFetcher.execute())
            .thenReturn(
                Observable.just(provided)
            )

        given(viewModel.retrieveDashboard()) {

            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                items match sequenceOf(
                    Launched,
                    Result(expected),
                    Done
                )
            }
        }
    }

    @Test fun `should emmit states for errored broking integration`() {
        whenever(mockedFetcher.execute())
            .thenReturn(
                Observable.error<List<BitcoinStatistic>>(NetworkingIssue.ConnectionSpike)
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