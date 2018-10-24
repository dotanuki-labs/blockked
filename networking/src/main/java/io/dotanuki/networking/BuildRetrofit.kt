package io.dotanuki.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.stringBased
import kotlinx.serialization.json.JSON
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object BuildRetrofit {

    operator fun invoke(apiURL: String, httpClient: OkHttpClient) =
        with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(httpClient)
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(stringBased(contentType, json::parse, json::stringify))
            build()
        }

    private val json by lazy {
        JSON.nonstrict
    }

    private val contentType by lazy {
        MediaType.parse("application/json")!!
    }

}