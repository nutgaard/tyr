package no.kartverket.tyr

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.kartverket.tyr.plugins.Logging
import no.kartverket.tyr.plugins.Monitoring
import no.kartverket.tyr.plugins.Security
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Root")
fun main() {
    val server = embeddedServer(Netty, port = 8080) {
        install(Monitoring.Plugin)
        install(Logging.Plugin)
        install(Security.Plugin) {
            mock = true
            providers += Security.AuthProvider("AzureAd")
        }
        install(ShutDownUrl.ApplicationCallPlugin)
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Head)

            allowHeader(HttpHeaders.Authorization)
            allowCredentials = true
        }
        install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
        install(XForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
        install(DefaultHeaders)

        install(StatusPages) {
            exception<Throwable> { call, cause ->
                logger.error("Unhandled error", cause)
                call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
        }

        routing {
            swaggerUI(path = "swagger")
        }

        module()
    }

    server.start(wait = true)
}
