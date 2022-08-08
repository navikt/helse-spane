package no.nav.helse.spane

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
import io.prometheus.client.CollectorRegistry.defaultRegistry
import io.prometheus.client.exporter.common.TextFormat
import no.nav.helse.logger
import no.nav.helse.spane.db.PersonRepository

fun ktorServer(database: PersonRepository): ApplicationEngine =
    embeddedServer(CIO, applicationEngineEnvironment {

        val appMicrometerRegistry = defaultRegistry

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
                get("/metrics") {
                    call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
                        TextFormat.write004(this, appMicrometerRegistry.metricFamilySamples())
                    }
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
                    val person = database.hentPerson(id)?.deserialiser() ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "not found"
                    )
                    val apiVisitor = APIVisitor()
                    person.accept(apiVisitor)
                    call.respondText(
                        contentType = ContentType.Application.Json,
                        text = objectMapper.writeValueAsString(apiVisitor.personMap)
                    )
                }

                get("/paragraf/{id?}") {
//                    val id = call.parameters["id"] ?: return@get call.respondText(
//                        "Missing id",
//                        status = HttpStatusCode.BadRequest
//                    )

                    // TODO: Slutt å hardcode dette endepunktet

                    val person = database.hentPerson("02126721911")?.deserialiser() ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "not found"
                    )
                    var apiVisitor = APIVisitor()
                    person.accept(apiVisitor)

                    val personer = mutableListOf(apiVisitor.personMap)
                    val nyPerson = database.hentPerson("02028508940")?.deserialiser() ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "not found"
                    )
                    apiVisitor = APIVisitor()
                    nyPerson.accept(apiVisitor)
                    personer.add(apiVisitor.personMap)
                    call.respondText(
                        contentType = ContentType.Application.Json,
                        text = objectMapper.writeValueAsString(personer)
                    )
                }
            }
        }
    })