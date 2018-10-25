package io.dotanuki.blockked

import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.blockked.domain.BitcoinBroker
import io.dotanuki.blockked.domain.BitcoinInfo
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
            is IssueFound -> composer.fetchFailed(criteria as IssueFound)
            is DataFechted -> composer.marketPriceFetched(criteria as DataFechted)
        }
    }
}


sealed class HandledCondition
class IssueFound(val error: Throwable) : HandledCondition()
class DataFechted(val info: BitcoinInfo) : HandledCondition()

class ScenarioComposer(private val broker: BitcoinBroker) {

    fun fetchFailed(condition: IssueFound) {
        whenever(broker.marketPrice())
            .thenReturn(
                Observable.error(condition.error)
            )
    }


    fun marketPriceFetched(data: DataFechted) {
        whenever(broker.marketPrice()).thenReturn(
            Observable.just(data.info)
        )
    }

}