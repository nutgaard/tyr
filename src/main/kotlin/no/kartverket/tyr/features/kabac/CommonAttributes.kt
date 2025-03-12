package no.kartverket.tyr.features.kabac

import io.ktor.server.application.*
import no.kartverket.kabac.utils.Key

object CommonAttributes {
    private val base: String = CommonAttributes::class.java.simpleName

    val CALL = Key<ApplicationCall>("$base.call")
    val BRUKSENHET_ID = Key<String>("$base.bruksenhet-id")
}