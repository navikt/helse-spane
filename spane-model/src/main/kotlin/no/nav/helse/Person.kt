package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.finnVedtaksperioder

class Person {
    private val subsumsjoner = mutableListOf<Subsumsjon>()

    fun antallVedtaksperioder(): Int {
        println(subsumsjoner.finnVedtaksperioder())
        return subsumsjoner.finnVedtaksperioder().size
    }

    internal fun håndter(subsumsjon: Subsumsjon){
        subsumsjoner.add(subsumsjon)
    }
}
