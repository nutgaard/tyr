package no.kartverket.tyr.features.kabac.providers

import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key
import no.kartverket.tyr.features.kabac.CommonAttributes
import no.kartverket.tyr.services.bruksenhet.BruksenhetService

class BruksenhetPip(
    private val service: BruksenhetService
) : Kabac.PolicyInformationPoint<BruksenhetService.Bruksenhet?> {
    override val key = Companion.key

    companion object : Kabac.AttributeKey<BruksenhetService.Bruksenhet?> {
        override val key = Key<BruksenhetService.Bruksenhet?>(BruksenhetPip)
    }

    override fun provide(ctx: Kabac.EvaluationContext): BruksenhetService.Bruksenhet? {
        val bruksenhet = ctx.getValue(CommonAttributes.BRUKSENHET_ID)
        return service.hentBruksenhet(bruksenhet)
    }
}