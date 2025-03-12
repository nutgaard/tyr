package no.kartverket.tyr.features.kabac

import no.kartverket.kabac.Decision
import no.kartverket.kabac.Kabac
import no.kartverket.kabac.impl.PolicyDecisionPointImpl
import no.kartverket.kabac.impl.PolicyEnforcementPointImpl
import no.kartverket.tyr.features.kabac.policies.BrukerHarTilgangTilBruksenhetPolicy
import no.kartverket.tyr.features.kabac.providers.BrukersRollerPip
import no.kartverket.tyr.features.kabac.providers.BruksenhetPip
import no.kartverket.tyr.features.kabac.providers.PrincipalPip
import no.kartverket.tyr.services.bruksenhet.BruksenhetService

object KabacConfig {
    val policies: List<Kabac.Policy> = listOf(
        BrukerHarTilgangTilBruksenhetPolicy
    )

    val pdp: Kabac.PolicyDecisionPoint = PolicyDecisionPointImpl().apply {
        install(PrincipalPip)
        install(BrukersRollerPip)
        install(BruksenhetPip(object : BruksenhetService {
            override fun hentBruksenhet(id: String): BruksenhetService.Bruksenhet? {
                TODO("Not yet implemented")
            }
        }))
    }

    val pep: Kabac.PolicyEnforcementPoint = PolicyEnforcementPointImpl(
        bias = Decision.Type.DENY,
        policyDecisionPoint = pdp
    )
}