package no.nav.helse

import no.nav.helse.Pseudovedtaksperiode.Companion.håndter

class Person(
    private val fødselsnummer: String
) {
    private val vedtaksperioder = mutableListOf<Pseudovedtaksperiode>()

    fun antallVedtaksperioder(): Int {
        return vedtaksperioder.size
    }

    fun håndter(subsumsjon: Subsumsjon) {
        vedtaksperioder.håndter(subsumsjon)
    }

    fun håndter(vedtakFattet: VedtakFattet) {
        vedtaksperioder.håndter(vedtakFattet)
    }

    fun håndter(vedtaksperiodeForkastet: VedtaksperiodeForkastet) {
        vedtaksperioder.håndter(vedtaksperiodeForkastet)
    }

    override fun toString(): String {
        return "Person (antall vedtaksperioder: ${antallVedtaksperioder()})"
    }

    fun accept(visitor: PersonVisitor) {
        visitor.preVisitPerson(fødselsnummer)
        visitor.preVisitVedtaksperioder()
        vedtaksperioder.forEach { it.accept(visitor) }
        visitor.postVisitVedtaksperioder()
        visitor.postVisitPerson()
    }


}
