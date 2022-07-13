package no.nav.helse

import no.nav.helse.Subsumsjon.Companion.finnVedtaksperioder
import no.nav.helse.Vedtaksperiode.Companion.finnAlle

internal class Person {
    private val vedtaksperioder = mutableListOf<Vedtaksperiode>()

    fun antallVedtaksperioder(): Int {
        return vedtaksperioder.size
    }

    internal fun håndter(subsumsjon: Subsumsjon){
        // todo sjekk vedtaksperiodeID istedet fra sporing

        vedtaksperioder.finnAlle(subsumsjon)


        // for hver periode i listen av vedtaksperioder
        // kjør vedtaksperiode.sjekkSubsumsjon(subsumsjon)


        // subsumsjon.håndtereVidere
        //
    }
}
