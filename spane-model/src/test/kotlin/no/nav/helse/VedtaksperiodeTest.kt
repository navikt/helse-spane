package no.nav.helse

import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.Vedtaksperiode.Companion.hvisIkkeRelevantLagNyVedtaksperiode
import no.nav.helse.Vedtaksperiode.Companion.håndter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VedtaksperiodeTest {

    @Test
    fun `ny vedtaksperiode lages om subsumsjon ikke matcher`() {
        val subsumsjonFørste = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))
        val subsumsjonAndre = lagSubsumsjon(sporing = mapOf("sykmelding" to "andre"))

        val vedtaksperioder = mutableListOf(Vedtaksperiode(mutableListOf(subsumsjonFørste)))
        vedtaksperioder.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjonAndre, SporingNoe.SYKMELDING)

        assertEquals(2, vedtaksperioder.size)
    }

    @Test
    fun `subsumsjon legges i eksisterende vedtaksperiode om den matcher`() {
        val subsumsjonFørste = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))
        val subsumsjonAndre = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))

        val vedtaksperioder = mutableListOf(Vedtaksperiode(mutableListOf(subsumsjonFørste)))
        vedtaksperioder.hvisIkkeRelevantLagNyVedtaksperiode(subsumsjonAndre, SporingNoe.SYKMELDING)

        assertEquals(1, vedtaksperioder.size)
    }


    @Test
    fun `subsumsjon blir duplisert og lagt til i alle rette vedtaksperioder`() {
        val børDupliseresSporing = mapOf(
            "sykmelding" to "relevant",
        )
        val relevantSporing = mapOf(
            "sykmelding" to "relevant",
            "soknad" to "relevant",
            "vedtaksperiode" to "relevant"
        )
        val ikkeRelevantSporing = mapOf(
            "sykmelding" to "ikke-relevant",
            "soknad" to "ikke-relevant",
            "vedtaksperiode" to "ikke-relevant"
        )


        val vedtaksperioder = mutableListOf(
            lagVedtaksPeriode(3, sporing = relevantSporing),
            lagVedtaksPeriode(3, sporing = relevantSporing),
            lagVedtaksPeriode(2, sporing = ikkeRelevantSporing)
        )

        vedtaksperioder.håndter(lagSubsumsjon(sporing= børDupliseresSporing))

        assertEquals(4, vedtaksperioder[0].antallSubsumsjoner())
        assertEquals(4, vedtaksperioder[1].antallSubsumsjoner())
        assertEquals(2, vedtaksperioder[2].antallSubsumsjoner())
    }
    @Test
    fun `subsumsjon blir lagt inn rett sted basert på søknad `() {
        val nySubsumsjon = mapOf(
            "sykmelding" to "relevant",
            "soknad" to "relevant"
        )
        val relevantSporing = mapOf(
            "sykmelding" to "ikke-relevant",
            "soknad" to "relevant",
            "vedtaksperiode" to "relevant"
        )
        val ikkeRelevantSporing = mapOf(
            "sykmelding" to "ikke-relevant",
            "soknad" to "ikke-relevant",
            "vedtaksperiode" to "ikke-relevant"
        )


        val vedtaksperioder = mutableListOf(
            lagVedtaksPeriode(3, sporing = relevantSporing),
            lagVedtaksPeriode(2, sporing = ikkeRelevantSporing)
        )

        vedtaksperioder.håndter(lagSubsumsjon(sporing= nySubsumsjon))

        assertEquals(4, vedtaksperioder[0].antallSubsumsjoner())
        assertEquals(2, vedtaksperioder[1].antallSubsumsjoner())
    }




}