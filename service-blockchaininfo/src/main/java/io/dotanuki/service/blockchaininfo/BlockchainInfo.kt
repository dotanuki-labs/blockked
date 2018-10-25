package io.dotanuki.service.blockchaininfo

import io.dotanuki.service.blockchaininfo.models.MarketPriceResponse
import io.reactivex.Observable
import retrofit2.http.GET

internal interface BlockchainInfo {

    @GET("charts/market-price?timespan=4weeks&format=json") fun marketPrice(): Observable<MarketPriceResponse>


    companion object {
        const val API_URL = "https://api.blockchain.info/"
    }
}