package io.dotanuki.networking.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val networkingModule = Kodein.Module("networking") {

    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    bind() from singleton {
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

}