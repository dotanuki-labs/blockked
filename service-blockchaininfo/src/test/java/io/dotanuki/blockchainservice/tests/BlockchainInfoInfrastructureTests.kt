package io.dotanuki.blockchainservice.tests

import io.dotanuki.blockchainservice.tests.util.InfrastructureRule
import io.dotanuki.blockchainservice.tests.util.loadFile
import io.dotanuki.blockked.domain.BlockchainInfoIntegrationIssue
import io.dotanuki.blockked.domain.BlockchainInfoIntegrationIssue.*
import io.dotanuki.burster.using
import io.dotanuki.logger.ConsoleLogger
import io.dotanuki.service.blockchaininfo.BrokerInfrastructure
import io.dotanuki.service.blockchaininfo.ExecutionErrorHandler
import labs.dotanuki.tite.checks.broken
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.nothing
import labs.dotanuki.tite.checks.something
import labs.dotanuki.tite.given
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class BrokerInfrastructureTests {

    @get:Rule val rule = InfrastructureRule()

    lateinit var broker: BrokerInfrastructure

    @Before fun `before each test`() {
        broker = BrokerInfrastructure(
            service = rule.api,
            errorHandler = ExecutionErrorHandler(ConsoleLogger)
        )
    }

    @Test fun `should retrieve Bitcoin market price with success`() {

        rule.defineScenario(
            status = 200,
            response = loadFile("200OK-market-price.json")
        )

        given(broker.averageBitcoinPrice()) {

            assertThatSequence {
                should be completed
                should emmit something
            }
        }
    }

    @Test fun `should map issue for non-desired responses`() {


        using<String, Int, BlockchainInfoIntegrationIssue> {

            burst {
                values("200OK-market-price-broken.json", 200, UnexpectedResponse)
                values("404-not-found.json", 404, ClientOrigin)
                values("503-internal-server.json", 503, RemoteSystem)
            }

            thenWith { json, statusCode, expectedIssue ->

                rule.defineScenario(
                    status = statusCode,
                    response = loadFile(json)
                )

                given(broker.averageBitcoinPrice()) {

                    assertThatSequence {
                        should be broken
                        should emmit nothing
                    }

                    verifyWhenError {
                        fails byError expectedIssue
                    }
                }
            }
        }
    }
}