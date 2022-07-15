package no.nav.helse

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
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

val fødselsnr = "10877799145"
val person = Person(fødselsnr)

fun main() {
    val config = Konfig.fromEnv()
    ApplicationBuilder(config, ::ktorServer, ::håndterSubsumsjon).startBlocking()
}

private val objectMapper = jacksonObjectMapper()
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .registerModule(JavaTimeModule())

fun håndterSubsumsjon(value: String) {

    val melding = objectMapper.readTree(value)

    if (melding["fodselsnummer"].asText() == fødselsnr) {
        val nySubsumsjon = lagSubsumsjonFraJson(melding)
        person.håndter(nySubsumsjon)
        sikkerlogger.info("Mottok melding som hadde forventet fødselsnummer {}", person.toString())
    }
//    else {
//        sikkerlogger.info("Mottok melding som ikke hadde fødselsnr")
//    }
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
        melding.get("ledd")?.asInt(),
        melding.get("punktum")?.asInt(),
        melding.get("bokstav")?.asText(),
        objectMapper.convertValue(melding.get("input")),
        objectMapper.convertValue(melding.get("output")) ?: emptyMap(),
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
                        this::class.java.classLoader.getResource("static/index.html")!!.readText(),
                        ContentType.Text.Html
                    )
                }
                static("/") {
                    resources("static/")
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
                    if (id == fødselsnr) {
                        val apiVisitor = APIVisitor()
                        person.accept(apiVisitor)
                        call.respondText(
                            contentType = ContentType.Application.Json,
                            text = objectMapper.writeValueAsString(apiVisitor.personMap)
                        )
                    }
                }
            }
        }
    })


typealias APIVedtaksperiode = Map<String, MutableList<Map<String, Any?>>>

class APIVisitor : PersonVisitor {
    val personMap = mutableMapOf<String, Any>("vedtaksperioder" to mutableListOf<APIVedtaksperiode>())
    override fun preVisitPerson(fødselsnummer: String) {
        personMap["fnr"] = fødselsnummer
    }

    override fun preVisitSubsumsjoner() {
        (personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>)
            .add(mutableMapOf("subsumsjoner" to mutableListOf()))

    }

    override fun visitSubsumsjon(
        id: String,
        versjon: String,
        eventName: String,
        kilde: String,
        versjonAvKode: String,
        fødselsnummer: String,
        sporing: Map<String, Any>,
        tidsstempel: ZonedDateTime,
        lovverk: String,
        lovverksversjon: String,
        paragraf: String,
        ledd: Int?,
        punktum: Int?,
        bokstav: String?,
        input: Map<String, Any>,
        output: Map<String, Any>,
        utfall: String
    ) {
        (personMap["vedtaksperioder"] as MutableList<APIVedtaksperiode>).last()["subsumsjoner"]!!.add(
            mapOf(
                "id" to id,
                "versjon" to versjon,
                "eventName" to eventName,
                "kilde" to kilde,
                "versjonAvKode" to versjonAvKode,
                "fødselsnummer" to fødselsnummer,
                "sporing" to sporing,
                "tidsstempel" to tidsstempel,
                "lovverk" to lovverk,
                "lovverksversjon" to lovverksversjon,
                "paragraf" to paragraf,
                "ledd" to ledd,
                "punktum" to punktum,
                "bokstav" to bokstav,
                "input" to input,
                "output" to output,
                "utfall" to utfall
            )
        )
    }

}