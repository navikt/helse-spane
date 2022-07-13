package no.nav.helse

import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.Vedtaksperiode.Companion.håndter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VedtaksperiodeTest {

    @Test
    fun `ny vedtaksperiode lages om subsumsjon ikke matcher`() {
        val subsumsjonFørste = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))
        val subsumsjonAndre = lagSubsumsjon(sporing = mapOf("sykmelding" to "andre"))

        val vedtaksperioder = mutableListOf(Vedtaksperiode(mutableListOf(subsumsjonFørste)))
        vedtaksperioder.håndter(subsumsjonAndre)

        assertEquals(2, vedtaksperioder.size)
    }

    @Test
    fun `subsumsjon legges i eksisterende vedtaksperiode om den matcher`() {
        val subsumsjonFørste = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))
        val subsumsjonAndre = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))

        val vedtaksperioder = mutableListOf(Vedtaksperiode(mutableListOf(subsumsjonFørste)))
        vedtaksperioder.håndter(subsumsjonAndre)

        assertEquals(1, vedtaksperioder.size)
    }
}