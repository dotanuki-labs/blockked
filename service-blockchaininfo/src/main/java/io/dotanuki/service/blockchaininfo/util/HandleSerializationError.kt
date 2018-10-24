package io.dotanuki.service.blockchaininfo.util

import com.sun.xml.internal.ws.encoding.soap.SerializationException
import io.dotanuki.blockked.domain.BlockchainInfoIntegrationIssue
import io.dotanuki.logger.Logger
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.UnknownFieldException

internal class HandleSerializationError<T>(private val logger: Logger) : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): Observable<T> {
        return upstream.onErrorResumeNext(this::handleSerializationError)
    }

    private fun handleSerializationError(error: Throwable): Observable<T> {
        error.message?.let { logger.e(it) } ?: error.printStackTrace()

        val mapped = when (error) {
            is MissingFieldException,
            is UnknownFieldException,
            is SerializationException -> BlockchainInfoIntegrationIssue.UnexpectedResponse
            else -> error
        }

        return Observable.error(mapped)
    }
}