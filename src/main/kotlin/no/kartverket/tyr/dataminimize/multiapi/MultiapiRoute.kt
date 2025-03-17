package no.kartverket.tyr.dataminimize.multiapi

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.openApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import no.kartverket.tyr.dataminimize.multiapi.MultiapiDomain.Person
import no.kartverket.tyr.dataminimize.multiapi.MultiapiDomain.RestrictedPerson

object MultiapiDomain {
    @Serializable
    data class Person(
        val pid: String,
        val name: String,
        val address: String,
        val age: Int,
        val gender: String?,
    )

    @Serializable
    data class RestrictedPerson(
        val pid: String,
        val name: String,
    )
}

fun Application.multiapiRoutes() {
    routing {
        route("multiapi.json") {
            openApi("multiapi")
        }

        get("/multiapi", {
            specName = "multiapi"
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

        get("/multiapi/restricted", {
            specName = "multiapi"
            response {
                HttpStatusCode.OK to {
                    body<RestrictedPerson>()
                }
                HttpStatusCode.Unauthorized to {
                    body<String>()
                }
            }
        }) {
            call.respond(RestrictedPerson("1234", "Ola"))
        }

    }
}