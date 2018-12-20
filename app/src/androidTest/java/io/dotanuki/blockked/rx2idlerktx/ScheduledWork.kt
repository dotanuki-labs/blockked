package io.dotanuki.blockked.rx2idlerktx

import java.util.concurrent.atomic.AtomicInteger


class ScheduledWork(
    val target : Runnable,
    private val delegate: WorkDelegate,
    startState: Int
) : AtomicInteger(startState), Runnable {

    override fun toByte() = this.get().toByte()

    override fun toChar() = this.get().toChar()

    override fun toShort() = this.get().toShort()

    override fun run() {
        while (true) {
            val state = get()
            when (state) {
                STATE_IDLE, STATE_SCHEDULED -> if (compareAndSet(state, STATE_RUNNING)) {
                    if (state == STATE_IDLE) {
                        delegate.startWork()
                    }
                    try {
                        target.run()
                    } finally {
                        // Complete with a CAS to ensure we don't overwrite a disposed state.
                        compareAndSet(STATE_RUNNING, STATE_COMPLETED)
                        delegate.stopWork()
                    }
                    return  // CAS success, we're done.
                }

                STATE_RUNNING -> throw IllegalStateException("Already running")

                STATE_COMPLETED -> throw IllegalStateException("Already completed")

                STATE_DISPOSED -> return  // Nothing to do.
            }// CAS failed, retry.
        }
    }

    fun dispose() {
        while (true) {
            val state = get()
            if (state == STATE_DISPOSED) {
                return  // Nothing to do.
            } else if (compareAndSet(state, STATE_DISPOSED)) {
                // If idle, startWork() hasn't been called so we don't need a matching stopWork().
                // If running, startWork() was called but the try/finally ensures a stopWork() call.
                // If completed, both startWork() and stopWork() have been called.
                if (state == STATE_SCHEDULED) {
                    delegate.stopWork() // Scheduled but not running means we called startWork().
                }
                return
            }
        }
    }

    companion object {
        val STATE_IDLE = 0 // --> STATE_RUNNING, STATE_DISPOSED
        val STATE_SCHEDULED = 1 // --> STATE_RUNNING, STATE_DISPOSED
        val STATE_RUNNING = 2 // --> STATE_COMPLETED, STATE_DISPOSED
        val STATE_COMPLETED = 3 // --> STATE_DISPOSED
        val STATE_DISPOSED = 4
    }
}