package no.nav.helse

import io.ktor.http.content.*
import no.nav.helse.Subsumsjon.Companion.erRelevant

internal class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {


    companion object {
        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {

            this.forEach {
                if (it.subsumsjoner.erRelevant(subsumsjon)) return
            }
            val nyVedtaksperiode = Vedtaksperiode(mutableListOf(subsumsjon))
            this.add(nyVedtaksperiode)
        }
    }
}
