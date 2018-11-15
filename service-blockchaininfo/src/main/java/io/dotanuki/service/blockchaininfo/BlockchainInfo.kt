package io.dotanuki.service.blockchaininfo

import io.dotanuki.services.common.BitcoinStatsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface BlockchainInfo {

    @GET("charts/{name}") fun fetchWith(
        @Path("name") statistic: String,
        @QueryMap query: Map<String, String>): Observable<BitcoinStatsResponse>

    companion object {
        const val API_URL = "https://api.blockchain.info/"
    }
}