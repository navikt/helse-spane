package no.nav.helse

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

var logger: Logger = LoggerFactory.getLogger("Spane")
var sikkerlogger: Logger = LoggerFactory.getLogger("tjenestekall")

lateinit var fødselsnr: String

fun main() {
    val config = Konfig.fromEnv()
    fødselsnr = "10877799145"
    ApplicationBuilder(config, ::ktorServer, ::håndterSubsumsjon).startBlocking()
}

private val objectMapper = jacksonObjectMapper()

fun håndterSubsumsjon(value: String) {

    val melding = objectMapper.readTree(value)
    val person = Person()

    if (melding["fodselsnummer"].asText() == fødselsnr) {
        val nySubsumsjon = lagSubsumsjonFraJson(melding)
        person.håndter(nySubsumsjon)
        sikkerlogger.info("Mottok melding som hadde forventet fødselsnummer {}", person.toString())
    } else {
        sikkerlogger.info("Mottok melding som ikke hadde fødselsnr")
    }
}


fun lagSubsumsjonFraJson(melding: JsonNode): Subsumsjon {

    val subsumsjon = Subsumsjon(
        melding.get("id").asText(),
        melding.get("versjon").asText(),
        melding.get("eventName").asText(),
        melding.get("kilde").asText(),
        melding.get("versjonAvKode").asText(),
        melding.get("fodselsnummer").asText(),
        objectMapper.convertValue(melding.get("sporing")),
        ZonedDateTime.parse(melding.get("tidsstempel").asText()),
        melding.get("lovverk").asText(),
        melding.get("lovverksversjon").asText(),
        melding.get("paragraf").asText(),
        Integer.parseInt(melding.get("ledd").asText()),
        Integer.parseInt(melding.get("punktum").asText()),
        melding.get("bokstav").asText(),
        objectMapper.convertValue(melding.get("input")),
        objectMapper.convertValue(melding.get("output")),
        melding.get("utfall").asText(),
    )
    return subsumsjon
}

fun ktorServer(appName: String): ApplicationEngine =
    embeddedServer(CIO, applicationEngineEnvironment {
        /**
         * Konfigurasjon av Webserver (Ktor https://ktor.io/)
         */


        log = logger
        connector {
            port = 8080
        }
        module {
            install(ContentNegotiation) { jackson() }
            install(CallLogging) {
                disableDefaultColors()
                filter { call ->
                    call.request.path().startsWith("/hello")
                }
            }

            routing {
                get("/") {
                    call.respondText(
                        "<html><h1>$appName</h1><html>",
                        ContentType.Text.Html
                    )
                }
                get("/isalive") {
                    call.respondText("OK")
                }

                get("/isready") {
                    call.respondText("OK")
                }

                get("/fnr/{id?}") {
                    val id = call.parameters["id"] ?: return@get call.respondText(
                        "Missing id",
                        status = HttpStatusCode.BadRequest
                    )
                    fødselsnr = id
                    // Sett fødselsnr som 10877799145 eller 24068715888 (har schema feil)
                }
            }
        }
    })