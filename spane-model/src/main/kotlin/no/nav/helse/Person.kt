package no.nav.helse

import no.nav.helse.PseudoVedtaksperiode.Companion.nyHåndter

class Person(
    private val fødselsnummer: String
) {
    private val vedtaksperioder = mutableListOf<PseudoVedtaksperiode>()

    fun antallVedtaksperioder(): Int {
        return vedtaksperioder.size
    }

    fun håndter(subsumsjon: Subsumsjon) {
        vedtaksperioder.nyHåndter(subsumsjon)
    }

    override fun toString(): String {
        return "Person (antall vedtaksperioder: ${antallVedtaksperioder()})"
    }

    fun accept(visitor: PersonVisitor) {
        visitor.preVisitPerson(fødselsnummer)
        visitor.preVisitVedtaksperioder()
        vedtaksperioder.forEach {it.accept(visitor)}
        visitor.postVisitVedtaksperioder()
        visitor.postVisitPerson()
    }


}
