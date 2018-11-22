package io.dotanuki.blockked

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import io.dotanuki.blockked.SwipeRefreshLayoutMatchers.isRefreshing
import org.hamcrest.Matchers

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
        is displayed -> checkRefreshing()
        is hidden -> checkNotRefreshing()
    }

    private fun checkNotRefreshing() {
        onView(withId(R.id.swipeToRefresh)).check(matches(Matchers.not(isRefreshing())))
    }

    private fun checkRefreshing() {
        onView(withId(R.id.swipeToRefresh)).check(matches(isRefreshing()))
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
            assertNotDisplayed(R.id.dashboarView)
        }

        is OnlyDisplay -> {
            assertDisplayed(content.bitcoinValue)
        }

        is DisplayAndGraph -> {
            assertDisplayed(R.id.dashboarView)
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
