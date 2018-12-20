package io.dotanuki.blockked

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import io.dotanuki.blockked.SwipeRefreshLayoutMatchers.isRefreshing
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not


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
        is displayedWith -> checkDisplayed(target.message)
        is displayed -> checkRefreshing()
        is hidden -> checkNotRefreshing()
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
        is displayedWith -> checkDisplayed(target.message)
        is displayed -> checkDisplayed(R.id.errorStateLabel)
        is hidden -> checkHidden(R.id.errorStateLabel)
    }
}

class DashboardContentCheck {

    infix fun have(content: DashboardContent) = when (content) {

        is noEntries -> checkHidden(R.id.dashboarView)

        is OnlyDisplay -> checkDisplayed(content.bitcoinValue)

        is DisplayAndGraph -> {
            checkDisplayed(R.id.dashboarView)
            checkDisplayed(content.bitcoinValue)
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

private fun checkDisplayed(target: String) {
    onView(firstViewOf(withText(target))).check(matches(isDisplayed()))
}

private fun checkDisplayed(target: Int) {
    onView(firstViewOf(withId(target)))
        .check(matches(isDisplayed()))
}

private fun checkHidden(target: String) {
    onView(firstViewOf(withText(target)))
        .check(matches(not(isDisplayed())))
}

private fun checkHidden(target: Int) {
    onView(firstViewOf(withId(target)))
        .check(matches(not(isDisplayed())))
}

private fun checkNotRefreshing() {
    onView(withId(R.id.swipeToRefresh))
        .check(matches(not(isRefreshing())))
}

private fun checkRefreshing() {
    onView(withId(R.id.swipeToRefresh))
        .check(matches(isRefreshing()))
}

private fun <T> firstViewOf(matcher: Matcher<T>): Matcher<T> {
    return object : BaseMatcher<T>() {
        private var isFirst = true

        override fun matches(item: Any): Boolean {
            if (isFirst && matcher.matches(item)) {
                isFirst = false
                return true
            }
            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("should return first matching item")
        }
    }
}