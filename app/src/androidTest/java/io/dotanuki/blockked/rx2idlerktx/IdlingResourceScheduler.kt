package io.dotanuki.blockked.rx2idlerktx

import androidx.test.espresso.IdlingResource
import io.reactivex.Scheduler


abstract class IdlingResourceScheduler : Scheduler(), IdlingResource