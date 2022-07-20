package no.nav.helse

import no.nav.helse.SporingNoe.*
import no.nav.helse.Subsumsjon.Companion.erRelevant

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
) {
    internal companion object {
        // har insendte subsumsjon en vedtaksperiode: dersom den har - legg den til der den finner en match etter vedtaksperiodeid

        // har insendte subsumsjon kun søknadsid: dersom den har - legg til der den finner match etter søknadsid


        fun MutableList<Vedtaksperiode>.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon: Subsumsjon, søk : SporingNoe) {
            if (this.none { it.subsumsjoner.erRelevant(subsumsjon,søk) }) this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
        }
        fun MutableList<Vedtaksperiode>.hvisMåDupliseres(subsumsjon: Subsumsjon, søk : SporingNoe) {

            var fantMatch = false
            forEach {
                if (!fantMatch)
                    fantMatch = it.subsumsjoner.erRelevant(subsumsjon, søk)
                else
                    it.subsumsjoner.erRelevant(subsumsjon, søk)
            }
            if (!fantMatch) {
                this.add(Vedtaksperiode(mutableListOf(subsumsjon)))
            }
        }


        fun MutableList<Vedtaksperiode>.håndter(subsumsjon: Subsumsjon) {
            when (subsumsjon.finnSøkeParameter()) {
                SYKMELDING -> {
                    this.hvisMåDupliseres(subsumsjon, SYKMELDING)
                }
                SØKNAD -> {
                    this.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon, SØKNAD)
                }
                VEDTAKSPERIODE -> {
                    this.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon, VEDTAKSPERIODE)
                }
                else -> {
                    println("Fant ikke søkeparameter i sporing")
                }
            }

        }
    }

    fun antallSubsumsjoner(): Int {
        return subsumsjoner.size
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.preVisitSubsumsjoner()
        subsumsjoner.forEach { it.accept(visitor) }
        visitor.postVisitSubsumsjoner()
    }
}
