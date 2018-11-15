package io.dotanuki.networking

import io.dotanuki.blockked.domain.RemoteIntegrationIssue
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import retrofit2.HttpException

class HandleErrorByHttpStatus<T> : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.onErrorResumeNext(this::handleIfRestError)
    }

    private fun handleIfRestError(incoming: Throwable): Observable<T> =
        if (incoming is HttpException) toInfrastructureError(incoming)
        else Observable.error(incoming)

    private fun toInfrastructureError(restError: HttpException): Observable<T> {
        val infraError = mapErrorWith(restError.code())
        return Observable.error(infraError)
    }

    private fun mapErrorWith(code: Int) = when (code) {
        in 400..499 -> RemoteIntegrationIssue.ClientOrigin
        else -> RemoteIntegrationIssue.RemoteSystem
    }

}