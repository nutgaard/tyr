package no.kartverket.tyr.services.bruksenhet

interface BruksenhetService {
    fun hentBruksenhet(id: String): Bruksenhet?

    data class Bruksenhet(
        val id: String,
        val kommuneId: String,
    )
}