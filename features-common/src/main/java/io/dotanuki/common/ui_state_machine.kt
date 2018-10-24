package io.dotanuki.common

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

sealed class UIEvent<out T>

object InFlight : UIEvent<Nothing>()
object Updating : UIEvent<Nothing>()
data class Failed(val reason: Throwable) : UIEvent<Nothing>()
data class Result<out T>(val value: T) : UIEvent<T>()
object Done : UIEvent<Nothing>()

class StateMachine<T>(private val uiScheduler: Scheduler = Schedulers.trampoline())
    : ObservableTransformer<T, UIEvent<T>> {

    var executionFeedback: ExecutionFeedback = IN_FLIGHT_ONLY

    override fun apply(upstream: Observable<T>): Observable<UIEvent<T>> {

        val beginning = statesForFeedback()
        val end = Observable.just(Done)

        return upstream
            .map { value: T -> Result(value) as UIEvent<T> }
            .onErrorReturn { error: Throwable -> Failed(error) }
            .startWith(beginning)
            .concatWith(end)
            .observeOn(uiScheduler)
    }

    private fun statesForFeedback(): Observable<UIEvent<T>> {

        val nextEvents = when (executionFeedback) {
            BOTH_FIRST_REFRESH_AFTER -> listOf(InFlight, Updating)
            UPDATING_ONLY -> listOf(Updating)
            IN_FLIGHT_ONLY -> listOf(InFlight)
            else -> emptyList()
        }

        if (executionFeedback == BOTH_FIRST_REFRESH_AFTER) {
            executionFeedback = UPDATING_ONLY
        }

        return Observable.fromIterable(nextEvents)
    }

    companion object {
        val IN_FLIGHT_ONLY = ExecutionFeedback(inFlight = true, refreshable = false)
        val UPDATING_ONLY = ExecutionFeedback(inFlight = false, refreshable = true)
        val BOTH_FIRST_REFRESH_AFTER = ExecutionFeedback(inFlight = true, refreshable = true)
    }

}

data class ExecutionFeedback(
    val inFlight: Boolean,
    val refreshable: Boolean
)