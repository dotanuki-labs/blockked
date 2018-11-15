package io.dotanuki.networking.tests

import io.dotanuki.blockked.domain.RemoteIntegrationIssue
import io.dotanuki.blockked.domain.RemoteIntegrationIssue.*
import io.dotanuki.burster.using
import io.dotanuki.networking.HandleErrorByHttpStatus
import io.reactivex.Observable
import kotlinx.serialization.SerializationException
import labs.dotanuki.tite.checks.broken
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.terminated
import labs.dotanuki.tite.given
import okhttp3.MediaType

import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class HandleErrorByHttpStatusTests {

    @Test fun `should handle error when mapped from proper HTTP status code`() {

        using<Int, String, RemoteIntegrationIssue> {

            burst {
                values(418, "Teapot", ClientOrigin)
                values(503, "Internal Server Error", RemoteSystem)
            }

            thenWith { httpStatus, errorMessage, mappedError ->

                val httpCause = httpException<Any>(httpStatus, errorMessage)
                val handler = HandleErrorByHttpStatus<Any>()
                val execution = Observable.error<Any>(httpCause).compose(handler)

                assertHandling(execution, mappedError)
            }
        }
    }

    @Test fun `should not handle any other errors`() {
        val errorToBePropagated = SerializationException("Cannot parse Data object")
        val execution = Observable.error<Any>(errorToBePropagated)

        assertHandling(execution, errorToBePropagated)
    }

    private fun <T> assertHandling(target: Observable<T>, expectedError: Throwable) {

        val handler = HandleErrorByHttpStatus<T>()
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

    fun <T> httpException(statusCode: Int, errorMessage: String): HttpException {
        val jsonMediaType = MediaType.parse("application/json")
        val body = ResponseBody.create(jsonMediaType, errorMessage)
        return HttpException(Response.error<T>(statusCode, body))
    }
}