package no.kartverket.test.logassert

import no.kartverket.no.kartverket.test.logassert.LogAsserts.Companion.captureLogs
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import org.slf4j.helpers.BasicMarkerFactory

class LogAssertsTest {
    private val logger = LoggerFactory.getLogger(LogAssertsTest::class.java)

    @Test
    fun `should capture log calls`() {
        val captures = logger.captureLogs {
            logger.info("Log info message")
            logger.warn("Log warn message")
            logger.error("Log error message")
        }

        captures
            .hasSize(3)
            .logline {
                hasLevel(Level.INFO)
                messageEquals("Log info message")
            }
            .logline {
                hasLevel(Level.WARN)
                messageEquals("Log warn message")
            }
            .logline {
                hasLevel(Level.ERROR)
                messageEquals("Log error message")
            }
    }

    @Test
    internal fun `should capture markers`() {
        val captured =
            logger.captureLogs {
                logger.info(
                    BasicMarkerFactory().getMarker("my-marker"),
                    "My message",
                )
            }

        captured
            .hasSize(1)
            .logline {
                messageEquals("My message")
                hasMarker("my-marker")
            }
    }
}