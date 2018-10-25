package io.dotanuki.blockked.rules

import android.app.Activity
import android.content.Intent
import android.support.test.rule.ActivityTestRule
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins
import kotlin.reflect.KClass

class ScreenLauncher<T : Activity>(klass: KClass<T>) : ActivityTestRule<T>(klass.java, false, false) {

    init {
        RxJavaPlugins.setInitIoSchedulerHandler(
            Rx2Idler.create("RxJava2-IOScheduler")
        )
    }

    fun launchScreen() {
        launchActivity(Intent())
    }

}