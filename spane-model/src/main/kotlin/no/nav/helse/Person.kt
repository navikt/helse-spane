package no.nav.helse

import no.nav.helse.Vedtaksperiode.Companion.håndter

class Person {
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

    fun accept(testVisitor: PersonVisitor) {


    }


}
