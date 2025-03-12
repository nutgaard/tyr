package no.kartverket.kabac.impl

import no.kartverket.kabac.AttributeValue
import no.kartverket.kabac.Kabac
import no.kartverket.kabac.utils.Key

class PolicyDecisionPointImpl : Kabac.PolicyDecisionPoint {
    private val providerRegister = mutableMapOf<Key<*>, Kabac.PolicyInformationPoint<*>>()

    override fun install(informationPoint: Kabac.PolicyInformationPoint<*>): Kabac.PolicyDecisionPoint {
        providerRegister[informationPoint.key] = informationPoint
        return this
    }

    override fun createEvaluationContext(attributes: List<AttributeValue<*>>): Kabac.EvaluationContext =
        EvaluationContextImpl(providerRegister.values + attributes)
}
