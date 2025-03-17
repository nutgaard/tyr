package no.kartverket.tyr.dataminimize.restricted

import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.openApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.kartverket.tyr.dataminimize.restricted.RestrictedDomain.Person

sealed class AccessControlled {
    @Serializable
    sealed class String : AccessControlled() {
        @Serializable
        @SerialName("Access")
        data class Access(val value: kotlin.String) : String()

        @Serializable
        @SerialName("NoAccess")
        data class NoAccess(val reason: kotlin.String) : String()

        companion object {
            fun noAccess(reason: kotlin.String = "No access") = NoAccess(reason)
        }
    }

    @Serializable
    sealed class Int : AccessControlled() {
        @Serializable
        @SerialName("Access")
        data class Access(val value: kotlin.Int) : Int()

        @Serializable
        @SerialName("NoAccess")
        data class NoAccess(val reason: kotlin.String) : Int()

        companion object {
            fun noAccess(reason: kotlin.String = "No access") = NoAccess(reason)
            fun access(value: kotlin.Int) = Access(value)
        }
    }
}

object RestrictedDomain {
    @Serializable
    data class Person(
        val pid: String,
        val name: String,
        val address: AccessControlled.String,
        val age: AccessControlled.Int,
        val gender: AccessControlled.String?,
    )
}

fun Application.restrictedRoutes() {
    routing {
        route("restricted.json") {
            openApi("restricted")
        }

        get("/restricted", {
            specName = "restricted"
            response {
                HttpStatusCode.OK to {
                    body<Person>()
                }
                HttpStatusCode.Unauthorized to {
                    body<String>()
                }
            }
        }) {
            call.respond(
                Person(
                    "1234",
                    "Ola",
                    AccessControlled.String.Access("Gårdveien"),
                    AccessControlled.Int.Access(18),
                    null,
                )
            )
        }

        get("/restricted/restricted", {
            specName = "restricted"
            response {
                HttpStatusCode.OK to {
                    body<Person>()
                }
                HttpStatusCode.Unauthorized to {
                    body<String>()
                }
            }
        }) {
            call.respond(
                Person(
                    "1234",
                    "Ola",
                    AccessControlled.String.noAccess("Cannot access address"),
                    AccessControlled.Int.noAccess("Cannot access age"),
                    AccessControlled.String.noAccess("Cannot access gender"),
                )
            )
        }

    }
}