package io.dotanuki.blockked.dashboard.tests

import com.nhaarman.mockitokotlin2.*
import io.dotanuki.blockked.dashboard.BitcoinBroker
import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.BitcoinPrice
import io.dotanuki.blockked.domain.services.BlockchainInfoService
import io.dotanuki.blockked.domain.services.CacheService
import io.dotanuki.common.toDate
import io.reactivex.Observable
import labs.dotanuki.tite.given
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BitcoinBrokerTests {

    private val cache = mock<CacheService>()
    private val remote = mock<BlockchainInfoService>()

    lateinit var broker: BitcoinBroker

    private val fromCache = BitcoinInfo(
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

    private val fromRemote = BitcoinInfo(
        providedName = "Market Price (USD)",
        providedDescription = "Average USD market price across major bitcoin exchanges.",
        prices = listOf(
            BitcoinPrice(
                date = "2018-10-22T22:00:00".toDate(),
                price = 6666.66666666666f,
                currencyUnit = "USD"
            )
        )
    )

    @Before fun `before each test`() {
        broker = BitcoinBroker(cache, remote)
    }

    @Test fun `should emmit data from cache first and from remote service after, updating cache`() {
        `cache has previous available bitcoin info`()
        `remote has fresh bitcoin info`()
        `cache should save bitcoin info`()

        given(broker.marketPrice()) {
            verifyForEmissions {
                items match sequenceOf(fromCache, fromRemote)
            }
        }

        `check bitcoin info saved on cached`(fromRemote)
        `check cache and remote access`()

    }

    @Test fun `should emmit data from remote service, saving into caching when missing`() {
        `cache has not previous available bitcoin info`()
        `remote has fresh bitcoin info`()
        `cache should save bitcoin info`()

        given(broker.marketPrice()) {
            verifyForEmissions {
                items match sequenceOf(fromRemote)
            }
        }

        `check bitcoin info saved on cached`(fromRemote)
        `check cache and remote access`()

    }

    private fun `cache has not previous available bitcoin info`() {
        whenever(cache.retrieveOrNull<BitcoinInfo>(any()))
            .thenReturn(null)
    }


    private fun `check cache and remote access`() {
        verify(cache, CALL_ONCE).retrieveOrNull<BitcoinInfo>(any())
        verify(remote, CALL_ONCE).averageBitcoinPrice()

        verifyNoMoreInteractions(cache)
        verifyNoMoreInteractions(remote)
    }

    private fun `cache has previous available bitcoin info`() {
        whenever(cache.retrieveOrNull<BitcoinInfo>(any()))
            .thenReturn(fromCache)
    }

    private fun `cache should save bitcoin info`() {
        whenever(cache.save(any(), any<BitcoinInfo>()))
            .thenAnswer { Unit }
    }

    private fun `remote has fresh bitcoin info`() {
        whenever(remote.averageBitcoinPrice())
            .thenReturn(
                Observable.just(fromRemote)
            )
    }

    private fun `check bitcoin info saved on cached`(updated: BitcoinInfo) {
        argumentCaptor<BitcoinInfo>().apply {
            verify(cache).save(any(), capture())
            assertThat(firstValue).isEqualTo(updated)
        }
    }

    private companion object {
        val CALL_ONCE = times(1)
    }

}