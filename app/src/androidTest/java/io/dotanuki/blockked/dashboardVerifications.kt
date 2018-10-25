package io.dotanuki.blockked

import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed

fun assertThat(block: DashboardVerifications.() -> Unit) =
    DashboardVerifications().apply { block() }

class DashboardVerifications {

    fun loadingIndicator(block: LoadingStateVerifier.() -> Unit) =
        LoadingStateVerifier().apply { block() }

    fun errorReport(block: ErrorStateVerifier.() -> Unit) =
        ErrorStateVerifier().apply { block() }

    fun dashboard(block: DashboardContentVerifier.() -> Unit) =
        DashboardContentVerifier().apply { block() }
}

class LoadingStateVerifier {
    val should by lazy { LoadingStateChecks() }
}

class LoadingStateChecks {

    infix fun be(target: Visibility) = when (target) {
        is displayedWith -> assertDisplayed(target.message)
        is displayed -> assertDisplayed(R.id.loadingIndication)
        is hidden -> assertNotDisplayed(R.id.loadingIndication)
    }
}

class ErrorStateVerifier {
    val should by lazy { ErrorStateChecks() }
}

class DashboardContentVerifier {
    val should by lazy { DashboardContentCheck() }
}

class ErrorStateChecks {

    infix fun be(target: Visibility) = when (target) {
        is displayedWith -> assertDisplayed(target.message)
        is displayed -> assertDisplayed(R.id.errorStateLabel)
        is hidden -> assertNotDisplayed(R.id.errorStateLabel)
    }
}

class DashboardContentCheck {

    infix fun have(content: DashboardContent) = when (content) {

        is noEntries -> {
            assertNotDisplayed(R.id.bitcoinPriceChart)
            assertNotDisplayed(R.id.displayContainer)
        }

        is OnlyDisplay -> {
            assertNotDisplayed(R.id.chartContainer)
            assertDisplayed(content.bitcoinValue)
        }

        is DisplayAndGraph -> {
            assertDisplayed(R.id.bitcoinPriceChart)
            assertDisplayed(content.bitcoinValue)
        }
    }

}


sealed class Visibility
object hidden : Visibility()
object displayed : Visibility()
class displayedWith(val message: String) : Visibility()

sealed class DashboardContent
data class OnlyDisplay(val bitcoinValue: String) : DashboardContent()
class DisplayAndGraph(val bitcoinValue: String) : DashboardContent()
object noEntries : DashboardContent()
