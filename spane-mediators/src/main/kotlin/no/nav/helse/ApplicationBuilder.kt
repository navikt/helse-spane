package no.nav.helse

import io.ktor.server.engine.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.kafka.SubsumsjonKonsument

class ApplicationBuilder (
    konfig: Konfig,
    builder: (String) -> ApplicationEngine,
    håndterSubsumsjon: (input: String) -> Any?
) {

    private val konsument = SubsumsjonKonsument(konfig, konfig.topic, håndterSubsumsjon)
    private val ktor = builder(konfig.appNavn)

    private val dataSourceBuilder = DataSourceBuilder(konfig)
    private val dataSource = dataSourceBuilder.getDataSource()
    private val personRepository = PersonPostgresRepository(dataSource)


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