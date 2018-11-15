package io.dotanuki.blockked.domain

import io.reactivex.Observable

interface FetchBitcoinStatistic {

    fun execute(target: SupportedStatistic, strategy: FetchStrategy): Observable<BitcoinStatistic>

}