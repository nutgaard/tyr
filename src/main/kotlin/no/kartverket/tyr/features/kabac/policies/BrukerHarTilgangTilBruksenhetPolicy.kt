package no.kartverket.tyr.features.kabac.policies

import no.kartverket.kabac.Decision
import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key
import no.kartverket.tyr.features.kabac.providers.BrukersRollerPip
import no.kartverket.tyr.features.kabac.providers.BruksenhetPip

object BrukerHarTilgangTilBruksenhetPolicy : Kabac.Policy {
    override val key =  Key<Kabac.Policy>(BrukerHarTilgangTilBruksenhetPolicy)

    override fun evaluate(ctx: Kabac.EvaluationContext): Decision {
        val bruksenhet = requireNotNull(ctx.getValue(BruksenhetPip)) { "Fant ikke bruksenhet" }
        val brukerRoller = ctx.getValue(BrukersRollerPip)

        return when {
            brukerRoller.contains(bruksenhet.kommuneId) -> Decision.Permit()
            else -> Decision.Deny("Bruker har ikke tilgang til kommunen", object : Decision.DenyCause {})
        }
    }
}