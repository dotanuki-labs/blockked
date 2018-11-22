package io.dotanuki.blockked

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.blockked.domain.BitcoinStatistic
import io.dotanuki.blockked.domain.FetchBitcoinStatistic
import io.reactivex.Observable

fun given(broker: FetchBitcoinStatistic, block: ScenarioHook.() -> Unit) =
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
class DataFechted(val info: BitcoinStatistic) : HandledCondition()

class ScenarioComposer(private val broker: FetchBitcoinStatistic) {

    fun fetchFailed(condition: IssueFound) {
        whenever(broker.execute(any(), any()))
            .thenReturn(
                Observable.error(condition.error)
            )
    }


    fun marketPriceFetched(data: DataFechted) {
        whenever(broker.execute(any(), any())).thenReturn(
            Observable.just(data.info)
        )
    }

}