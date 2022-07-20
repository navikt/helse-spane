package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.erRelevantEtterSoknad
import no.nav.helse.Subsumsjon.Companion.erRelevantSykemelding

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {
        // har insendte subsumsjon en vedtaksperiode: dersom den har - legg den til der den finner en match etter vedtaksperiodeid

        // har insendte subsumsjon kun søknadsid: dersom den har - legg til der den finner match etter søknadsid


        fun MutableList<Vedtaksperiode>.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon: Subsumsjon) {
            if (this.none { it.erRelevantSøknad(subsumsjon) }) this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
        }

        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            when (subsumsjon.skalDupliseres()) {
                SporingNoe.SYKMELDING -> {
                    var fantMatch = false
                    forEach {
                        if (!fantMatch)
                            fantMatch = it.subsumsjoner.erRelevantSykemelding(subsumsjon)
                        else
                            it.subsumsjoner.erRelevantSykemelding(subsumsjon)
                    }
                    if (!fantMatch) {
                        this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
                        // Hvis ikke fant match, lag ny vedtaksperiode
                    }
                }
                SporingNoe.SØKNAD -> {
                    this.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon)
                }


                else -> {}
            }

        }
    }

    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    private fun erRelevantSøknad(subsumsjon: Subsumsjon): Boolean {
        return if (subsumsjoner.erRelevantEtterSoknad(subsumsjon)) {
            subsumsjoner += subsumsjon; true
        } else false
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
