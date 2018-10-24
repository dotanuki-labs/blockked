package io.dotanuki.common.tests

import io.dotanuki.common.*
import io.reactivex.Observable
import labs.dotanuki.tite.checks.broken
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.something
import labs.dotanuki.tite.checks.terminated
import labs.dotanuki.tite.given
import org.junit.Before
import org.junit.Test

class StateMachineTests {

    lateinit var machine: StateMachine<User>

    @Before fun `before each test`() {
        machine = StateMachine()
    }

    @Test fun `verify composition with an empty upstream`() {

        val noResults = Observable.empty<User>().compose(machine)
        val events = listOf(InFlight, Done)

        `assert machine execution`(
            incoming = noResults,
            expected = events
        )
    }

    @Test fun `verify composition with a broken upstream`() {

        val failure = IllegalStateException("You failed")
        val errorHappened = Observable.error<User>(failure).compose(machine)
        val events = listOf(InFlight, Failed(failure), Done)

        `assert machine execution`(
            incoming = errorHappened,
            expected = events
        )
    }

    @Test fun `verify composition with an successful upstream`() {

        val user = User("Guarilha")
        val execution = Observable.just(user).compose(machine)
        val events = listOf(InFlight, Result(user), Done)

        `assert machine execution`(
            incoming = execution,
            expected = events
        )
    }

    @Test fun `verify composition with an successful upstream, updating-only as feedback`() {

        machine.executionFeedback = StateMachine.UPDATING_ONLY

        val user = User("Guarilha")
        val execution = Observable.just(user).compose(machine)
        val events = listOf(Updating, Result(user), Done)

        `assert machine execution`(
            incoming = execution,
            expected = events
        )
    }


    @Test fun `verify composition, both-first-updating-after as feedback, success on both shots`() {

        machine.executionFeedback = StateMachine.BOTH_FIRST_REFRESH_AFTER

        val user = User("Guarilha")
        val firstExecution = Observable.just(user).compose(machine)
        val firstShotEvents = listOf(InFlight, Updating, Result(user), Done)

        `assert machine execution`(
            incoming = firstExecution,
            expected = firstShotEvents
        )

        val secondExecution = Observable.just(user).compose(machine)
        val secondShotEvents = listOf(Updating, Result(user), Done)

        `assert machine execution`(
            incoming = secondExecution,
            expected = secondShotEvents
        )
    }

    @Test fun `verify composition, both-first-updating-after as feedback, error on second shot`() {

        machine.executionFeedback = StateMachine.BOTH_FIRST_REFRESH_AFTER

        val user = User("Guarilha")
        val firstExecution = Observable.just(user).compose(machine)
        val firstShotEvents = listOf(InFlight, Updating, Result(user), Done)

        `assert machine execution`(
            incoming = firstExecution,
            expected = firstShotEvents
        )

        val failure = IllegalStateException("You failed")
        val secondExecution = Observable.error<User>(failure).compose(machine)
        val secondShotEvents = listOf(Updating, Failed(failure), Done)

        `assert machine execution`(
            incoming = secondExecution,
            expected = secondShotEvents
        )
    }


    private fun `assert machine execution`(
        incoming: Observable<UIEvent<User>>,
        expected: List<UIEvent<User>>) {

        given(incoming) {

            assertThatSequence {
                should be terminated
                should be completed
                should notBe broken
                should emmit something
            }

            verifyForEmissions {
                items are expected
            }
        }
    }

    data class User(val name: String)
}

