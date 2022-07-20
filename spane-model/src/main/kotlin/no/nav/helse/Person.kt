package no.nav.helse

import no.nav.helse.Vedtaksperiode.Companion.håndter

class Person(
    private val fødselsnummer: String
) {
    private val vedtaksperioder = mutableListOf<Vedtaksperiode>()

    fun antallVedtaksperioder(): Int {
        return vedtaksperioder.size
    }

    fun håndter(subsumsjon: Subsumsjon) {
        vedtaksperioder.håndter(subsumsjon)
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
