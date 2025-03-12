package no.kartverket.tyr.features.kabac

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.kartverket.kabac.AttributeValue
import no.kartverket.kabac.Decision

fun Application.kabacRoutes() {
    routing {
        get("/policy") {
            call.respond(KabacConfig.policies.map { it.key.name })
        }
        post("/policy/{name}") {
            val name = call.pathParameters["name"]
            val policy = KabacConfig.policies.first { it.key.name == name }
            val input = call.receive<List<AttributeValue<*>>>()

            val (decision, report) = KabacConfig.pep.evaluatePolicyWithContextWithReport(
                bias = Decision.Type.DENY,
                ctx = KabacConfig.pdp.createEvaluationContext(input),
                policy = policy
            )
            call.respond(
                mapOf(
                    "decision" to decision,
                    "report" to report,
                )
            )
        }
    }
}