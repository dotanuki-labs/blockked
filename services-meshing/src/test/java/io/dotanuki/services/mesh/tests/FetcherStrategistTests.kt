package io.dotanuki.services.mesh.tests

import com.nhaarman.mockitokotlin2.*
import io.dotanuki.blockked.domain.FetchStrategy
import io.dotanuki.blockked.domain.SupportedStatistic
import io.dotanuki.services.common.*
import io.dotanuki.services.mesh.FetcherStrategist
import io.reactivex.Observable
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.nothing
import labs.dotanuki.tite.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class FetcherStrategistTests {

    private val remote = mock<BlockchainInfoService>()
    private val local = mock<CacheService>()

    lateinit var fetcher: FetcherStrategist

    @Before fun `before each test`() {
        fetcher = FetcherStrategist(remote, local)
    }

    @Test fun `should fetch from local cache, with cache hit`() {
        `cache has previous data`()

        val execution = fetcher.execute(
            SupportedStatistic.AverageMarketPrice,
            FetchStrategy.FromPrevious
        )

        val mapped = BitcoinInfoMapper(PREVIOUSLY_CACHED)

        given(execution) {
            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                firstItem shouldBe mapped
            }
        }

        verify(local, times(1)).retrieveOrNull(any())
        verifyNoMoreInteractions(local)
        verifyZeroInteractions(remote)
    }

    @Test fun `should fetch from local cache, with cache miss`() {
        `cache has no previous data`()

        val execution = fetcher.execute(
            SupportedStatistic.AverageMarketPrice,
            FetchStrategy.FromPrevious
        )


        given(execution) {
            assertThatSequence {
                should be completed
                should emmit nothing
            }
        }

        verify(local, times(1)).retrieveOrNull(any())
        verifyNoMoreInteractions(local)
        verifyZeroInteractions(remote)
    }

    @Test fun `should fetch from remote service, updating local cache`() {
        `remote exposes updated data`()

        val execution = fetcher.execute(
            SupportedStatistic.AverageMarketPrice,
            FetchStrategy.ForceUpdate
        )

        val mapped = BitcoinInfoMapper(UPDATED)

        given(execution) {
            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                firstItem shouldBe mapped
            }
        }

        verify(remote, times(1)).fetchStatistics(any())
        verifyNoMoreInteractions(remote)

        argumentCaptor<BitcoinStatsResponse>().apply {
            verify(local, times(1)).save(any(), capture())
            assertThat(firstValue).isEqualTo(UPDATED)
        }

        verifyNoMoreInteractions(local)
    }

    private fun `remote exposes updated data`() {
        whenever(remote.fetchStatistics(any()))
            .thenReturn(
                Observable.just(UPDATED)
            )
    }

    private fun `cache has previous data`() {
        whenever(local.retrieveOrNull(any()))
            .thenReturn(PREVIOUSLY_CACHED)
    }


    private fun `cache has no previous data`() {
        whenever(local.retrieveOrNull(any()))
            .thenReturn(null)
    }


    private companion object {
        val PREVIOUSLY_CACHED = BitcoinStatsResponse(
            name = "Market Price (USD)",
            description = "Average USD market value across major bitcoin exchanges.",
            unit = "USD",
            values = listOf(
                StatisticPoint(
                    timestamp = 1540166400,
                    value = 6498f
                ),
                StatisticPoint(
                    timestamp = 1540252800,
                    value = 6481f
                )
            )
        )

        val UPDATED = BitcoinStatsResponse(
            name = "Market Price (USD)",
            description = "Average USD market value across major bitcoin exchanges.",
            unit = "USD",
            values = listOf(
                StatisticPoint(
                    timestamp = 1540166400,
                    value = 6498f
                ),
                StatisticPoint(
                    timestamp = 1540252800,
                    value = 6481f
                ),
                StatisticPoint(
                    timestamp = 1540253400,
                    value = 6500f
                )
            )
        )
    }
}