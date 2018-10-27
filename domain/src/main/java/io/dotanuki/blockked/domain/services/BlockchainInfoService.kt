package io.dotanuki.blockked.domain.services

import io.dotanuki.blockked.domain.BitcoinInfo
import io.reactivex.Observable

interface BlockchainInfoService {

    fun averageBitcoinPrice() : Observable<BitcoinInfo>

}
