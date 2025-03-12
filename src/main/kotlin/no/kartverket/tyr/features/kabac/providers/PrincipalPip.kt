package no.kartverket.tyr.features.kabac.providers

import io.ktor.server.auth.*
import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key
import no.kartverket.tyr.features.kabac.CommonAttributes
import no.kartverket.tyr.plugins.Security

object PrincipalPip : Kabac.PolicyInformationPoint<Security.TokenPrincipal> {
    override val key =  Key<Security.TokenPrincipal>(PrincipalPip)

    override fun provide(ctx: Kabac.EvaluationContext): Security.TokenPrincipal {
        val call = ctx.getValue(CommonAttributes.CALL)
        return requireNotNull(call.principal<Security.TokenPrincipal>()) {
            "Could not find principal from $call"
        }
    }
}