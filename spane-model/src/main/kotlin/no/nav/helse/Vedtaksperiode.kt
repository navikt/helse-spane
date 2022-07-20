package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevant

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {
        fun MutableList<Vedtaksperiode>.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon: Subsumsjon) {
            if(this.none{it.erRelevant(subsumsjon)}) this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
        }

        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            if(subsumsjon.skalDupliseres()){
                var fantMatch = false
                forEach {
                    if (!fantMatch)
                        fantMatch = it.erRelevant(subsumsjon)
                    else
                        it.erRelevant(subsumsjon)
                }
                if(!fantMatch) {
                    this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
                    // Hvis ikke fant match, lag ny vedtaksperiode
                }
            }
            else {
                this.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon)
            }
        }
    }

    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    // TODO vurder å ikke legge til, bare sjekke
    private fun erRelevant(subsumsjon: Subsumsjon): Boolean {
        return if (subsumsjoner.erRelevant(subsumsjon)) { subsumsjoner += subsumsjon; true }
        else false
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
