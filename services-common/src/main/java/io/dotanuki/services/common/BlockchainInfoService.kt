package io.dotanuki.services.common

import io.reactivex.Observable

interface BlockchainInfoService {

    fun averageBitcoinPrice() : Observable<BitcoinStatsResponse>

}
