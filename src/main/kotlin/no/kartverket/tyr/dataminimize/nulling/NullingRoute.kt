package no.kartverket.tyr.dataminimize.nulling

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.openApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import no.kartverket.tyr.dataminimize.nulling.NullingDomain.Person

object NullingDomain {
    @Serializable
    data class Person(
        val pid: String,
        val name: String,
        val address: String?,
        val age: Int?,
        val gender: String?,
    )
}

fun Application.nullingRoutes() {
    routing {
        route("nulling.json") {
            openApi("nulling")
        }

        get("/nulling", {
            specName = "nulling"
            response {
                HttpStatusCode.OK to {
                    body<Person>()
                }
                HttpStatusCode.Unauthorized to {
                    body<String>()
                }
            }
        }) {
            call.respond(Person("1234", "Ola", "Gårdveien", 18, null))
        }

        get("/nulling/restricted", {
            specName = "nulling"
            response {
                HttpStatusCode.OK to {
                    body<Person>()
                }
                HttpStatusCode.Unauthorized to {
                    body<String>()
                }
            }
        }) {
            call.respond(Person("1234", "Ola", null, null, null))
        }
    }
}