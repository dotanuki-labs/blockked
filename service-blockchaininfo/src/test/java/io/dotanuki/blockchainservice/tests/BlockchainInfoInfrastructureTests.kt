package io.dotanuki.blockchainservice.tests

import io.dotanuki.blockchainservice.tests.util.InfrastructureRule
import io.dotanuki.blockchainservice.tests.util.loadFile
import io.dotanuki.blockked.domain.RemoteIntegrationIssue
import io.dotanuki.blockked.domain.RemoteIntegrationIssue.*
import io.dotanuki.blockked.domain.SupportedStatistic
import io.dotanuki.burster.using
import io.dotanuki.logger.ConsoleLogger
import io.dotanuki.service.blockchaininfo.BrokerInfrastructure
import io.dotanuki.service.blockchaininfo.ExecutionErrorHandler
import io.dotanuki.services.common.BitcoinStatsResponse
import io.dotanuki.services.common.StatisticPoint
import labs.dotanuki.tite.checks.broken
import labs.dotanuki.tite.checks.completed
import labs.dotanuki.tite.checks.nothing
import labs.dotanuki.tite.checks.something
import labs.dotanuki.tite.given
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class BlockchainInfoInfrastructureTests {

    @get:Rule val rule = InfrastructureRule()

    lateinit var infrastructure: BrokerInfrastructure

    @Before fun `before each test`() {
        infrastructure = BrokerInfrastructure(
            service = rule.api,
            errorHandler = ExecutionErrorHandler(ConsoleLogger)
        )
    }

    @Test fun `should retrieve Bitcoin market price with success`() {

        rule.defineScenario(
            status = 200,
            response = loadFile("200OK-market-price.json")
        )

        val expected = BitcoinStatsResponse(
            name = "Market Price (USD)",
            description = "Average USD market value across major bitcoin exchanges.",
            unit = "USD",
            values = listOf(
                StatisticPoint(
                    timestamp = 1540166400,
                    value = 6498f
                ),
                StatisticPoint(
                    timestamp = 1540252800,
                    value = 6481f
                )
            )
        )

        given(infrastructure.fetchStatistics(SupportedStatistic.AverageMarketPrice)) {

            assertThatSequence {
                should be completed
                should emmit something
            }

            verifyForEmissions {
                firstItem shouldBe expected
            }
        }
    }

    @Test fun `should map issue for non-desired responses`() {


        using<String, Int, RemoteIntegrationIssue> {

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

                given(infrastructure.fetchStatistics(SupportedStatistic.AverageMarketPrice)) {

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