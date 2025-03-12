package no.kartverket.tyr.features.kabac.providers

import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key

object BrukersRollerPip : Kabac.PolicyInformationPoint<Set<String>> {
    override val key = Key<Set<String>>(BrukersRollerPip)

    override fun provide(ctx: Kabac.EvaluationContext): Set<String> {
        val principal = ctx.getValue(PrincipalPip)
        val subject = requireNotNull(principal.subject)
        // TODO connect to DB to get roles
        return emptySet()
    }
}