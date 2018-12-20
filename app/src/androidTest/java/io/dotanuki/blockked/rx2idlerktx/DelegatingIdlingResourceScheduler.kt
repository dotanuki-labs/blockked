package io.dotanuki.blockked.rx2idlerktx

import androidx.test.espresso.IdlingResource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


class DelegatingIdlingResourceScheduler(
    private val delegate: Scheduler,
    private val name: String
) : IdlingResourceScheduler() {

    private val work = AtomicInteger()
    private var callback: IdlingResource.ResourceCallback? = null

    private val workDelegate = object : WorkDelegate {

        override fun startWork() {
            work.incrementAndGet();
        }

        override fun stopWork() {
            if (work.decrementAndGet() == 0) {
                callback?.onTransitionToIdle();
            }
        }

    }

    override fun getName() = name

    override fun isIdleNow() = work.get() == 0

    override fun registerIdleTransitionCallback(target: IdlingResource.ResourceCallback) {
        callback = target
    }

    override fun createWorker(): Scheduler.Worker {
        val delegateWorker = delegate.createWorker()

        return object : Scheduler.Worker() {

            private val disposables = CompositeDisposable(delegateWorker)

            override fun schedule(action: Runnable): Disposable {
                if (disposables.isDisposed) {
                    return Disposables.disposed()
                }
                val work = createWork(action, 0L)
                val disposable = delegateWorker.schedule(work)
                val workDisposable = ScheduledWorkDisposable(work, disposable)
                disposables.add(workDisposable)
                return workDisposable
            }

            override fun schedule(action: Runnable, delayTime: Long, unit: TimeUnit): Disposable {
                if (disposables.isDisposed) {
                    return Disposables.disposed()
                }
                val work = createWork(action, delayTime)
                val disposable = delegateWorker.schedule(work, delayTime, unit)
                disposables.add(disposable)
                val workDisposable = ScheduledWorkDisposable(work, disposable)
                disposables.add(workDisposable)
                return workDisposable
            }

            override fun schedulePeriodically(
                action: Runnable, initialDelay: Long, period: Long,
                unit: TimeUnit
            ): Disposable {
                if (disposables.isDisposed) {
                    return Disposables.disposed()
                }
                val work = createWork(action, initialDelay)
                val disposable = delegateWorker.schedulePeriodically(work, initialDelay, period, unit)
                disposables.add(disposable)
                val workDisposable = ScheduledWorkDisposable(work, disposable)
                disposables.add(workDisposable)
                return workDisposable
            }

            override fun dispose() {
                disposables.dispose()
            }

            override fun isDisposed(): Boolean {
                return disposables.isDisposed
            }
        }
    }

    fun createWork(incoming: Runnable, delay: Long): ScheduledWork {
        var action = incoming
        if (action is ScheduledWork) {
            // Unwrap any re-scheduled work. We want each scheduler to get its own state machine.
            action = action.target
        }
        val immediate = delay == 0L
        if (immediate) {
            workDelegate.startWork()
        }
        val startingState = if (immediate) ScheduledWork.STATE_SCHEDULED else ScheduledWork.STATE_IDLE
        return ScheduledWork(action, workDelegate, startingState)
    }


}