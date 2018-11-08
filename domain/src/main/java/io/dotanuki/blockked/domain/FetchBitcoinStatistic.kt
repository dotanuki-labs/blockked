package io.dotanuki.blockked.domain

import io.reactivex.Observable

interface FetchBitcoinStatistic {

    fun execute(): Observable<BitcoinStatistic>

}