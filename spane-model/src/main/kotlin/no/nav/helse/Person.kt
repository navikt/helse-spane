package no.nav.helse

import no.nav.helse.Vedtaksperiode.Companion.håndter

internal class Person {
    private val vedtaksperioder = mutableListOf<Vedtaksperiode>()

    fun antallVedtaksperioder(): Int {
        return vedtaksperioder.size
    }

    internal fun håndter(subsumsjon: Subsumsjon){
        vedtaksperioder.håndter(subsumsjon)
    }
}
