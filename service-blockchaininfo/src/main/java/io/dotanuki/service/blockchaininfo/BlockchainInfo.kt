package io.dotanuki.service.blockchaininfo

import io.dotanuki.service.blockchaininfo.models.MarketPriceResponse
import io.reactivex.Observable
import retrofit2.http.GET

internal interface BlockchainInfo {

    @GET("charts/market-price?format=json") fun marketPrice(): Observable<MarketPriceResponse>

}