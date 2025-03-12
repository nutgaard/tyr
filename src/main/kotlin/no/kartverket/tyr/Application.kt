package no.kartverket.tyr

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.kartverket.tyr.features.kabac.kabacRoutes


fun Application.module() {
    kabacRoutes()
    routing {
        get {
            call.respondText("Hello")
        }
    }
}
