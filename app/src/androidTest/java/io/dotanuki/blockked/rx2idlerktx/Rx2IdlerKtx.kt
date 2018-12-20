package io.dotanuki.blockked.rx2idlerktx

import androidx.test.espresso.Espresso
import io.reactivex.Scheduler
import io.reactivex.functions.Function
import java.util.concurrent.Callable

object Rx2IdlerKtx {

    fun create(name: String): Function<Callable<Scheduler>, Scheduler> {
        return Function { delegate ->
            val scheduler = DelegatingIdlingResourceScheduler(delegate.call(), name)
            Espresso.registerIdlingResources(scheduler)
            scheduler
        }
    }
}