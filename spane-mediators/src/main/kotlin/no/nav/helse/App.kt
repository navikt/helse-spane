package no.nav.helse

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

var logger: Logger = LoggerFactory.getLogger("Spydig")
var sikkerlogger: Logger = LoggerFactory.getLogger("tjenestekall")

fun main() {
    val config = Konfig.fromEnv()
    ApplicationBuilder(config, ::ktorServer, ::håndterSubsumsjon).startBlocking()
}

fun håndterSubsumsjon(input: String){

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
            }
        }
    })