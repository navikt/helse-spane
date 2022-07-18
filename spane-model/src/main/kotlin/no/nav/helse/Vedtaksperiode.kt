package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevant

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {
        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            if(this.none{it.håndter(subsumsjon)}) this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
        }
    }
    
    private fun håndter(subsumsjon: Subsumsjon): Boolean {
        return if (subsumsjoner.erRelevant(subsumsjon)) { subsumsjoner += subsumsjon; true }
        else false
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
