package no.kartverket.tyr.features.kabac

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import no.kartverket.kabac.AttributeValue
import no.kartverket.kabac.Decision
import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key
import java.util.*

fun Application.kabacRoutes() {
    routing {
        get("/policy") {
            call.respond(KabacConfig.policies.map(Kabac.Policy::toDescription))
        }
        post("/policy/{name}") {
            val name = call.pathParameters["name"]
            val policy = KabacConfig.policies.first { it.key.name == name }
            val input = call.receive<List<AttributeValue<String>>>()

            val (decision, report) = KabacConfig.pep.evaluatePolicyWithContextWithReport(
                bias = Decision.Type.DENY,
                ctx = KabacConfig.pep.createEvaluationContext(input),
                policy = policy
            )
            val out = PolicyResult(
                decision = decision.type,
                message = decision.toString(),
                report = report
            )

            call.respond(out)
        }
    }
}

@Serializable
data class PolicyResult(
    val decision: Decision.Type,
    val message: String,
    val report: String,
)
@Serializable
data class PolicyDescription(
    val name: String,
    val inputs: Set<String>,
    val using: Set<String>,
)

fun Kabac.Policy.toDescription(): PolicyDescription {
    val ctx = KabacConfig.pep.createEvaluationContext()
    val stack = LinkedList<Key<*>>(this.trace().toMutableSet())
    val visited = LinkedList<Key<*>>()
    val unprovided = LinkedList<Key<*>>()
    while (stack.isNotEmpty()) {
        val key = stack.pop()
        if (key in visited) continue

        visited.push(key)
        val provider = ctx.register[key]
        if (provider == null) unprovided.push(key)
        else {
            stack.addAll(provider.trace())
        }
    }

    return PolicyDescription(
        name = this.key.name,
        inputs = unprovided.filter { it != CommonAttributes.CALL }.map { it.name }.toSet(),
        using = visited.map { it.name }.toSet(),
    )
}