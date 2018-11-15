package io.dotanuki.blockked.domain

import io.reactivex.Observable
import io.reactivex.functions.Function

class RetrieveStatistics(private val fetcher: FetchBitcoinStatistic) {

    private val cached by lazy {
        retrieveAll(strategy = FetchStrategy.FromPrevious)
    }

    private val updated by lazy {
        retrieveAll(strategy = FetchStrategy.ForceUpdate)
    }

    fun execute() = cached.concatWith(updated)

    private fun retrieveAll(strategy: FetchStrategy) =
        Observable
            .fromIterable(SupportedStatistic.ALL)
            .flatMap { Observable.just(fetcher.execute(it, strategy)) }
            .let { Observable.zip(it, Zipper) }

    private companion object Zipper : Function<Array<Any>, List<BitcoinStatistic>> {
        override fun apply(raw: Array<Any>) = raw.map { it as BitcoinStatistic }
    }

}