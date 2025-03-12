package no.kartverket.tyr.features.kabac.providers

import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key
import no.kartverket.tyr.features.kabac.CommonAttributes

object BrukersRollerPip : Kabac.PolicyInformationPoint<Set<String>> {
    override val key = Key<Set<String>>(BrukersRollerPip)

    override fun provide(ctx: Kabac.EvaluationContext): Set<String> {
        val principal = ctx.getValue(CommonAttributes.BRUKER_ID)
        // TODO connect to DB to get roles
        return emptySet()
    }

    override fun trace(): Set<Key<*>> = setOf(CommonAttributes.BRUKER_ID)
}