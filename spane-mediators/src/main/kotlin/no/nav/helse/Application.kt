package no.nav.helse

import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.nav.helse.spane.db.PersonRepository
import no.nav.helse.spane.kafka.Konsument

class Application(
    konfig: KonsumentKonfig,
    builder: (PersonRepository) -> EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>,
    personRepository: PersonRepository
) {

    private val konsument = Konsument(konfig, konfig.topic, personRepository)
    private val ktor = builder(personRepository)

    fun startBlocking() {
        runBlocking { start() }
    }

    suspend fun start() {
        val ktorServer = ktor.start(false)
        try {
            coroutineScope {
                launch { konsument.start() }
            }
        } finally {
            val gracePeriod = 5000L
            val forcefulShutdownTimeout = 30000L
            logger.info("shutting down ktor, waiting $gracePeriod ms for workers to exit. Forcing shutdown after $forcefulShutdownTimeout ms")
            ktorServer.stop(gracePeriod, forcefulShutdownTimeout)
            logger.info("ktor shutdown complete: end of life. goodbye.")
        }
    }
}