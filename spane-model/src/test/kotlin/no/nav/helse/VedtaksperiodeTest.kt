package no.nav.helse

import no.nav.helse.SporingEnum.*
import no.nav.helse.TestHjelper.Companion.assertVedtaksperioderAntallOgLengde
import no.nav.helse.TestHjelper.Companion.lagSubsumsjon
import no.nav.helse.TestHjelper.Companion.lagVedtaksPeriode
import no.nav.helse.Vedtaksperiode.Companion.seEtterVedtaksperiodeID
import no.nav.helse.Vedtaksperiode.Companion.håndter
import no.nav.helse.Vedtaksperiode.Companion.seEtterSykmeldingsID
import no.nav.helse.Vedtaksperiode.Companion.seEtterSøknadsID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class VedtaksperiodeTest {

    @Test
    fun `subsumsjon med vedtaksperiodeID blir lagt i eksisterende vedtaksperiode etter vedtaksperiodeID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterVedtaksperiodeID(lagSubsumsjon(sporing = nySubsumsjononSporing), VEDTAKSPERIODE)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon med vedtaksperiodeID blir lagt i eksisterende vedtaksperiode etter søknadsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterVedtaksperiodeID(lagSubsumsjon(sporing = nySubsumsjononSporing), VEDTAKSPERIODE)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)

    }

    @Test
    fun `subsumsjon med vedtaksperiodeID blir lagt i eksisterende vedtaksperiode etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterVedtaksperiodeID(lagSubsumsjon(sporing = nySubsumsjononSporing), VEDTAKSPERIODE)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon med søknadsID blir lagt i eksisterende vedtaksperiode med vedtaksperiode ID etter søknadsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSøknadsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SØKNAD)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }
    @Test
    fun `subsumsjon med søknadsID blir lagt i eksisterende vedtaksperiode etter søknadsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSøknadsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SØKNAD)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon med søknadsID blir lagt i eksisterende vedtaksperiode etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSøknadsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SØKNAD)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    //


    @Test
    fun `subsumsjon med sykmeldingsID blir lagt i eksisterende vedtaksperiode med vedtaksperiodeID etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1",
            "vedtaksperiode" to "v1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSykmeldingsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SYKMELDING)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }
    @Test
    fun `subsumsjon med sykmeldingsID blir lagt i eksisterende vedtaksperiode med søknadsID etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1",
            "soknad" to "sø1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSykmeldingsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SYKMELDING)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }
    @Test
    fun `subsumsjon med sykmeldingsID blir lagt i eksisterende vedtaksperiode etter sykmeldingsID`() {
        val eksisterendeSubsumsjonSporing = mapOf(
            "sykmelding" to "s1"
        )
        val nySubsumsjononSporing = mapOf(
            "sykmelding" to "s1"
        )
        val vedtaksperioder = mutableListOf(lagVedtaksPeriode(3, sporing = eksisterendeSubsumsjonSporing))

        vedtaksperioder.seEtterSykmeldingsID(lagSubsumsjon(sporing = nySubsumsjononSporing), SYKMELDING)

        assertVedtaksperioderAntallOgLengde(vedtaksperioder, 1, 4)
    }

    @Test
    fun `subsumsjon legges i eksisterende vedtaksperiode om den matcher`() {
        val subsumsjonFørste = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))
        val subsumsjonAndre = lagSubsumsjon(sporing = mapOf("sykmelding" to "første"))

        val vedtaksperioder = mutableListOf(Vedtaksperiode(mutableListOf(subsumsjonFørste)))
        vedtaksperioder.seEtterVedtaksperiodeID(subsumsjonAndre, SYKMELDING)

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

        vedtaksperioder.håndter(lagSubsumsjon(sporing = børDupliseresSporing))

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

        vedtaksperioder.håndter(lagSubsumsjon(sporing = nySubsumsjon))

        assertEquals(4, vedtaksperioder[0].antallSubsumsjoner())
        assertEquals(2, vedtaksperioder[1].antallSubsumsjoner())
    }


}