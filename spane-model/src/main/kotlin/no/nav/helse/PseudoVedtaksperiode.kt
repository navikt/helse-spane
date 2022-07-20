package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevantSykemelding

class PseudoVedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {

    internal companion object {
        fun MutableList<PseudoVedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            if(this.none{it.håndter(subsumsjon)}) this.add(PseudoVedtaksperiode(mutableListOf(subsumsjon)))
        }
    }

    private fun håndter(subsumsjon: Subsumsjon): Boolean {
        return if (subsumsjoner.erRelevantSykemelding(subsumsjon)) { subsumsjoner += subsumsjon; true }
        else false
    }
}
