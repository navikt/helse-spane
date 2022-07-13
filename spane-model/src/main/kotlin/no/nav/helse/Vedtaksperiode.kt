package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevant

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {
        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            this.forEach {
                if (it.subsumsjoner.erRelevant(subsumsjon)){
                    it.subsumsjoner.add(subsumsjon)
                    return
                }
            }
            val nyVedtaksperiode = Vedtaksperiode(mutableListOf(subsumsjon))
            this.add(nyVedtaksperiode)
        }
    }
}
