package io.dotanuki.common

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

sealed class UIEvent<out T>

object Launched : UIEvent<Nothing>()
data class Failed(val reason: Throwable) : UIEvent<Nothing>()
data class Result<out T>(val value: T) : UIEvent<T>()
object Done : UIEvent<Nothing>()

class StateMachine<T>(private val uiScheduler: Scheduler = Schedulers.trampoline())
    : ObservableTransformer<T, UIEvent<T>> {

    override fun apply(upstream: Observable<T>): Observable<UIEvent<T>> {

        val beginning = Launched
        val end = Observable.just(Done)

        return upstream
            .map { value: T -> Result(value) as UIEvent<T> }
            .onErrorReturn { error: Throwable -> Failed(error) }
            .startWith(beginning)
            .concatWith(end)
            .observeOn(uiScheduler)
    }
}