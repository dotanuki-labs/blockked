package io.dotanuki.blockked.domain

import io.reactivex.Observable

interface BitcoinBroker {

    fun marketPrice() : Observable<BitcoinInfo>

}