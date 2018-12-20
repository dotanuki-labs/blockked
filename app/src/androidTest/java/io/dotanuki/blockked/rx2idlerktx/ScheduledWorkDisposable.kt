package io.dotanuki.blockked.rx2idlerktx

import io.reactivex.disposables.Disposable

class ScheduledWorkDisposable(
    private val work: ScheduledWork,
    private val delegate: Disposable
) : Disposable {

    override fun isDisposed() = work.get() == ScheduledWork.STATE_DISPOSED

    override fun dispose() {
        work.dispose()
        delegate.dispose()
    }
}