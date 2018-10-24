package io.dotanuki.service.blockchaininfo

import io.dotanuki.logger.Logger
import io.dotanuki.networking.HandleConnectivityIssue
import io.dotanuki.networking.HandleErrorByHttpStatus
import io.dotanuki.service.blockchaininfo.util.HandleSerializationError
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ExecutionErrorHandler<T>(private val logger: Logger) : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>) =
        upstream
            .compose(HandleErrorByHttpStatus())
            .compose(HandleConnectivityIssue())
            .compose(HandleSerializationError(logger))
            .doOnError { logger.e("API integration | Failed with  -> $it") }
            .doOnNext { logger.v("API integration -> Success") }

}