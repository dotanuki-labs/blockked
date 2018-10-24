package io.dotanuki.networking

import io.dotanuki.blockked.domain.NetworkingIssue
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HandleConnectivityIssue<T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.onErrorResumeNext(this::handleIfNetworkingError)

    private fun handleIfNetworkingError(throwable: Throwable) =
        if (isNetworkingError(throwable)) asNetworkingError(throwable)
        else Observable.error(throwable)


    private fun asNetworkingError(throwable: Throwable) = Observable.error<T>(
        mapToDomainError(throwable)
    )

    private fun mapToDomainError(error: Throwable): NetworkingIssue {
        if (isConnectionTimeout(error)) return NetworkingIssue.OperationTimeout
        if (cannotReachHost(error)) return NetworkingIssue.HostUnreachable
        return NetworkingIssue.ConnectionSpike
    }

    private fun isNetworkingError(error: Throwable) =
        isConnectionTimeout(error) || cannotReachHost(error) || isRequestCanceled(error)

    private fun isRequestCanceled(throwable: Throwable) =
        throwable is IOException && throwable.message?.contentEquals("Canceled") ?: false

    private fun cannotReachHost(error: Throwable): Boolean {
        return error is UnknownHostException || error is ConnectException ||  error is NoRouteToHostException
    }

    private fun isConnectionTimeout(error: Throwable) = error is SocketTimeoutException

}