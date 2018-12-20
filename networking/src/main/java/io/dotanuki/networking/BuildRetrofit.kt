package io.dotanuki.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.serializationConverterFactory
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
            addConverterFactory(serializationConverterFactory(contentType, JSON.nonstrict))
            build()
        }


    private val contentType by lazy {
        MediaType.parse("application/json")!!
    }

}