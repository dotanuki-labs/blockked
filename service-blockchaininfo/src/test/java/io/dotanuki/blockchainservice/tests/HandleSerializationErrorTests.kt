package io.dotanuki.blockchainservice.tests

import io.dotanuki.blockked.domain.RemoteIntegrationIssue
import io.dotanuki.logger.ConsoleLogger
import io.dotanuki.service.blockchaininfo.util.HandleSerializationError
import io.reactivex.Observable
import kotlinx.serialization.SerializationException
import labs.dotanuki.tite.checks.broken
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.terminated
import labs.dotanuki.tite.given
import org.junit.Test

class HandleSerializationErrorTests {

    @Test fun `should handle serialization errors`() {
        val parseError = SerializationException("Found comments inside this JSON")
        val execution = Observable.error<Any>(parseError)
        assertHandling(execution, RemoteIntegrationIssue.UnexpectedResponse)
    }


    @Test fun `should not handle any other errors`() {
        val errorToBePropagated = IllegalStateException("Something broke here ...")
        val execution = Observable.error<Any>(errorToBePropagated)
        assertHandling(execution, errorToBePropagated)
    }

    private fun <T> assertHandling(target: Observable<T>, expectedError: Throwable) {

        val handler = HandleSerializationError<T>(ConsoleLogger)
        val execution = target.compose(handler)

        given(execution) {

            assertThatSequence {
                should notBe completed
                should be broken
                should be terminated
            }

            verifyWhenError {
                fails byError expectedError
            }
        }
    }
}