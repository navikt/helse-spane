package no.nav.helse

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.*
import no.nav.helse.spane.kafka.Konsument

class Application(
    private val konsument: Konsument,
    private val ktor: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>,
) {
    private var jobb: Job? = null

    fun startBlocking() {
        try {
            runBlocking {
                start()
                jobb?.join()
            }
        } finally {
            val gracePeriod = 5000L
            val forcefulShutdownTimeout = 30000L
            logger.info("shutting down ktor, waiting $gracePeriod ms for workers to exit. Forcing shutdown after $forcefulShutdownTimeout ms")
            ktor.stop(gracePeriod, forcefulShutdownTimeout)
            logger.info("ktor shutdown complete: end of life. goodbye.")
        }
    }

    fun stopBlocking() {
        runBlocking {
            stop()
            jobb?.cancelAndJoin()
        }
    }

    fun stop() {
        konsument.stop()
        jobb?.cancel()
    }

    fun start() {
        ktor.start(false)
        jobb = CoroutineScope(Dispatchers.IO).launch {
            launch { konsument.start() }
        }
    }
}