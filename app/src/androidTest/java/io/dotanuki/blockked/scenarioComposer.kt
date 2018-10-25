package io.dotanuki.blockked

import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.blockked.domain.BitcoinBroker
import io.dotanuki.blockked.domain.BitcoinInfo
import io.dotanuki.blockked.domain.NetworkingIssue
import io.reactivex.Observable

fun given(broker: BitcoinBroker, block: ScenarioHook.() -> Unit) =
    ScenarioHook(ScenarioComposer(broker)).apply { block() }

class ScenarioHook(private val composer: ScenarioComposer) {

    fun defineScenario(setup: Scenario.() -> Unit) =
        Scenario(composer).apply { setup() }.configure()
}

class Scenario(private val composer: ScenarioComposer) {

    lateinit var criteria: HandledCondition

    fun configure() {
        when (criteria) {
            is NetworkRequestTimedOut -> composer.networkingTimedOut()
            is BlockchainInfoIsDown -> composer.remoteServerDown()
            is DataFechted -> composer.marketPriceFetched(criteria as DataFechted)
        }
    }
}


sealed class HandledCondition
object NetworkRequestTimedOut : HandledCondition()
object BlockchainInfoIsDown : HandledCondition()
class DataFechted(val info: BitcoinInfo) : HandledCondition()

class ScenarioComposer(private val broker: BitcoinBroker) {

    fun networkingTimedOut() {
        whenever(broker.marketPrice())
            .thenReturn(
                Observable.error(NetworkingIssue.OperationTimeout)
            )
    }

    fun remoteServerDown() {
        whenever(broker.marketPrice())
            .thenReturn(
                Observable.error(NetworkingIssue.ConnectionSpike)
            )
    }

    fun marketPriceFetched(data: DataFechted) {
        whenever(broker.marketPrice()).thenReturn(
            Observable.just(data.info)
        )
    }

}