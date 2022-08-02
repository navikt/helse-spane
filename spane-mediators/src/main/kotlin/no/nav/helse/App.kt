package no.nav.helse

import no.nav.helse.spane.db.PersonPostgresRepository
import no.nav.helse.spane.håndterSubsumsjon
import no.nav.helse.spane.ktorServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

var logger: Logger = LoggerFactory.getLogger("Spane")
var sikkerlogger: Logger = LoggerFactory.getLogger("tjenestekall")

fun main() {
    val config = Konfig.fromEnv()

    val dataSourceBuilder = DataSourceBuilder(config)
    val personRepository = PersonPostgresRepository(dataSourceBuilder.getDataSource())

    Application(config, ::ktorServer, ::håndterSubsumsjon, personRepository).startBlocking()
}

