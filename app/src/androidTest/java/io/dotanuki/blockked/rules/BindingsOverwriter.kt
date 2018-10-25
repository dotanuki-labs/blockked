package io.dotanuki.blockked.rules

import android.support.test.InstrumentationRegistry
import io.dotanuki.blockked.BlockkedApp
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class BindingsOverwriter(private val bindings: Kodein.MainBuilder.() -> Unit) : ExternalResource() {

    init {
        val configurableKodein = app().kodein
        configurableKodein.addConfig { bindings() }
    }

    private fun app() = InstrumentationRegistry.getTargetContext().applicationContext as BlockkedApp

}