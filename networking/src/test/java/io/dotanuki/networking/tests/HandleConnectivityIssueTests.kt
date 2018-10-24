package io.dotanuki.networking.tests

import io.dotanuki.blockked.domain.NetworkingIssue
import io.dotanuki.blockked.domain.NetworkingIssue.*
import io.dotanuki.burster.using
import io.dotanuki.networking.HandleConnectivityIssue
import io.reactivex.Observable
import labs.dotanuki.tite.checks.broken
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.terminated
import labs.dotanuki.tite.given
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HandleConnectivityIssueTests {

    @Test fun `should handle error when catched from proper networking exception`() {

        using<Throwable, NetworkingIssue> {

            burst {
                values(UnknownHostException("No Internet"), HostUnreachable)
                values(ConnectException(), HostUnreachable)
                values(SocketTimeoutException(), OperationTimeout)
                values(NoRouteToHostException(), HostUnreachable)
                values(IOException("Canceled"), ConnectionSpike)
            }

            thenWith { incoming, expected ->
                val execution = Observable.error<Any>(incoming)
                assertHandling(execution, expected)
            }
        }

    }

    @Test fun `should not handle any other errors`() {
        val errorToBePropagated = IllegalStateException("Something broke here ...")
        val execution = Observable.error<Any>(errorToBePropagated)
        assertHandling(execution, errorToBePropagated)
    }

    private fun <T> assertHandling(target: Observable<T>, givenError: Throwable) {

        val handler = HandleConnectivityIssue<T>()
        val execution = target.compose(handler)

        given(execution) {

            assertThatSequence {
                should notBe completed
                should be broken
                should be terminated
            }

            verifyWhenError {
                fails byError givenError
            }
        }
    }
}

