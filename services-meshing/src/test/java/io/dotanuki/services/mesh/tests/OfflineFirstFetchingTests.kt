package io.dotanuki.services.mesh.tests

import com.nhaarman.mockitokotlin2.*
import io.dotanuki.services.common.*
import io.dotanuki.services.mesh.OfflineFirstFetching
import io.reactivex.Observable
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.given
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test

class OfflineFirstFetchingTests {

    private val remote = mock<BlockchainInfoService>()
    private val local = mock<CacheService>()

    lateinit var usecase: OfflineFirstFetching

    @Before fun `before each test`() {
        usecase = OfflineFirstFetching(remote, local)
    }


    @Test fun `should fetch from remote, with local cache miss`() {
        `data fetched from server`()
        `data missing on local cache`()

        val execution = usecase.invoke(CacheEntry.BTCPrice) { it.averageBitcoinPrice() }

        given(execution) {
            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                firstItem shouldBe UPDATED_RESPONSE
                count shouldBe 1
            }
        }

        `check remote service interactions`()
        `check local cache interactions`()
    }


    @Test fun `should fetch from remote, with local cache hit`() {
        `data present on local cache`()
        `data fetched from server`()

        val execution = usecase.invoke(CacheEntry.BTCPrice) { it.averageBitcoinPrice() }

        given(execution) {
            assertThatSequence {
                should be completed
            }

            verifyForEmissions {
                items match sequenceOf(OUTDATED_RESPONSE, UPDATED_RESPONSE)
            }
        }

        `check remote service interactions`()
        `check local cache interactions`()

    }

    private fun `check remote service interactions`() {
        verify(remote, times(1)).averageBitcoinPrice()
        verifyNoMoreInteractions(remote)
    }

    private fun `check local cache interactions`() {
        verify(local, times(1)).retrieveOrNull(any())

        argumentCaptor<BitcoinStatsResponse>().apply {
            verify(local, times(1)).save(any(), capture())
            assertThat(firstValue).isEqualTo(UPDATED_RESPONSE)
        }

        verifyNoMoreInteractions(local)
    }

    private fun `data present on local cache`() {
        whenever(local.retrieveOrNull(any()))
            .thenReturn(OUTDATED_RESPONSE)
    }


    private fun `data fetched from server`() {
        whenever(remote.averageBitcoinPrice())
            .thenReturn(
                Observable.just(UPDATED_RESPONSE)
            )
    }

    private fun `data missing on local cache`() {
        whenever(local.retrieveOrNull(any()))
            .thenReturn(null)
    }

    private companion object {

        val OUTDATED_RESPONSE = BitcoinStatsResponse(
            name = "Some name",
            description = "Some description",
            unit = "USD",
            values = emptyList()
        )


        val UPDATED_RESPONSE = BitcoinStatsResponse(
            name = "Some name",
            description = "Some updated description",
            unit = "USD",
            values = listOf(
                StatisticPoint(
                    timestamp = 1000L,
                    value = 100f
                )
            )
        )
    }
}