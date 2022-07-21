package no.nav.helse

import no.nav.helse.SporingNoe.*
import no.nav.helse.Subsumsjon.Companion.erRelevant

class Vedtaksperiode(
    private val subsumsjoner: MutableList<Subsumsjon>
    // liste med alle subsumsjoner med kun sykemelding - ikke begrenset til hver vedtaksperiode, men det totale antallet.
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
                    // f eks her - kalle hent søknadsider og se om noen matcher.
                    // flere kan matche - legg inn flere steder her også
                    // dersom den ikke finner noen søknadsideer som matcher skal det ikke lages ny vedtaksperiode, men her skal den dupliseres og legges etter
                }
                VEDTAKSPERIODE -> {
                    this.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjon, VEDTAKSPERIODE)
                    // f eks her - kalle hent vedtaksperiodeider og se om noen matcher.
                    // hent alle
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
