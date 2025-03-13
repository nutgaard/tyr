package no.kartverket.no.kartverket.test.logassert

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import ch.qos.logback.classic.Logger as LogbackLogger

class LogAsserts(
    private val appender: ListAppender<ILoggingEvent>
) {
    companion object {
        fun captureLogs(fn: () -> Unit): LogAsserts {
            return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).captureLogs(fn)
        }

        fun Logger.captureLogs(fn: () -> Unit): LogAsserts {
            this as LogbackLogger
            val appender = ListAppender<ILoggingEvent>().apply { start() }
            this.addAppender(appender)
            fn()
            this.detachAppender(appender)
            appender.stop()
            return LogAsserts(appender)
        }
    }


    private var eventIndex: Int = 0

    fun hasSize(expectedSize: Int): LogAsserts {
        assertEquals(expectedSize, appender.list.size, "Number of log messages did not match")
        return this
    }

    fun logline(block: MessageAsserter.() -> Unit): LogAsserts {
        MessageAsserter(appender.list[eventIndex++]).apply(block)
        return this
    }

    fun logline(
        index: Int,
        block: MessageAsserter.() -> Unit,
    ): LogAsserts {
        MessageAsserter(appender.list[index]).apply(block)
        return this
    }

    fun skipline(): LogAsserts {
        eventIndex++
        return this
    }

    class MessageAsserter(
        private val event: ILoggingEvent,
    ) {
        private val markerMap: Map<String, Any?> by lazy {
            event.markerList.associate {
                it.name to null
            }
        }

        fun hasLevel(level: Level) {
            assertEquals(level.toString(), event.level.toString(), "Level did not match")
        }

        fun messageEquals(expected: String) {
            assertEquals(expected, event.message)
        }

        fun messageContains(expected: String) {
            assertTrue(event.message.contains(expected))
        }

        fun hasMarker(markername: String) {
            assertTrue(markerMap.containsKey(markername), "Could not find marker")
        }

        fun markerValueEquals(
            markername: String,
            expectedValue: String,
        ) {
            hasMarker(markername)
            assertEquals(expectedValue, markerMap[markername], "Marker value did not match")
        }

        fun markerValueContains(
            markername: String,
            expectedValue: String,
        ) {
            hasMarker(markername)
            assertEquals(expectedValue, markerMap[markername], "Marker value did not match")
        }
    }
}